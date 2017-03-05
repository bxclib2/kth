/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */  


package ir;

import java.util.*;
import java.lang.Math;

/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();

    long start = System.currentTimeMillis();

    private SPIMInvert spimi;
    private boolean shouldIndex = false;

    private HashMap<Integer, Integer> docLengths = new HashMap();

    private RankedQuery rankedQuery;

    public HashedIndex() {
        this.spimi = SPIMInvert.load();
        shouldIndex = false;
        if (this.spimi == null) {
            System.err.println("catching the exception");
            this.spimi = new SPIMInvert();
            this.shouldIndex = true;
        }
    }

    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) {
        if (token.equals("welleducated")) {
            System.err.println("docid: " + docID);
        }
        if (token.equals("demographically")) {
            System.err.println("okkkkk: " + docID);
        }
        if (shouldIndex) {
            this.spimi.addToBlock(token, docID, offset);
        }

        Integer length = 0;
        if (docLengths.containsKey(docID)) {
            length = docLengths.get(docID);
        }
        docLengths.put(docID, length + 1);

        //PostingsList pl = getPostings(token);
        //if (pl == null) {
        //    pl = new PostingsList();
        //    index.put(token, pl);
        //}
        //pl.addEntry(docID, offset);
    }


    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
        return this.index.keySet().iterator();
    }


    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
        //return this.index.get(token);
        return spimi.getPostings(token);
    }


    /**
     *  Searches the index for postings matching the query.
     */
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
        System.err.println("query: " + query.terms);
        switch (queryType) {
            case INTERSECTION_QUERY:
                return intersectionQuery(query, rankingType, structureType);
            case PHRASE_QUERY:
                return positionalIntersectionQuery(query);
            case RANKED_QUERY:
                return rankedQuery.search(query, rankingType);
        }

        return null;
    }

    public ArrayList<PostingsList> termsSortedByFrequency(Query query) {
        ArrayList<PostingsList> sortedTerms = new ArrayList<>();
        for (String term : query.terms) {
            PostingsList pl = getPostings(term);
            if (null != pl) {
                sortedTerms.add(pl);
            }
        }
        sortedTerms.sort(PostingsList.FrequencyComparator);
        return sortedTerms;
    }

    public PostingsList intersectionQuery(Query query, int rankingType, int structureType) {
        ArrayList<PostingsList> sortedPostingsLists = termsSortedByFrequency(query);
        if (query.terms.size() != sortedPostingsLists.size()) {
           return null;
        }
        PostingsList result = sortedPostingsLists.remove(0);
        while (0 < result.size() && !sortedPostingsLists.isEmpty()) {
            result = intersect(result, sortedPostingsLists.remove(0));
        }
        return result;
    }

    public PostingsList intersect(PostingsList lhs, PostingsList rhs) {
        PostingsList answer = new PostingsList(lhs.getWord());
        int lhsIndex = 0;
        int rhsIndex = 0;
        while (lhsIndex < lhs.size() && rhsIndex < rhs.size()) {
            int lhsDocID = lhs.get(lhsIndex).docID;
            int rhsDocID = rhs.get(rhsIndex).docID;
            if (lhsDocID == rhsDocID) {
                answer.addEntry(lhsDocID, 0);
                lhsIndex++;
                rhsIndex++;
            } else if (lhsDocID < rhsDocID) {
                lhsIndex++;
            } else {
                rhsIndex++;
            }
        }
        return answer;
    }

    class Triplet {
        int docID;
        int pos1;
        int pos2;

        public Triplet(int docID, int pos1, int pos2) {
            this.docID = docID;
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
    }

    public PostingsList positionalIntersectionQuery(Query query) {
        ArrayList<PostingsList> terms = new ArrayList<>();
        for (String term : query.terms) {
            PostingsList pl = getPostings(term);
            if (pl == null) {
                return null;
            }
            terms.add(pl);
        }

        ArrayList<PostingsList> sortedPostingsLists = new ArrayList<>(terms);
        sortedPostingsLists.sort(PostingsList.FrequencyComparator);

        PostingsList result = sortedPostingsLists.remove(0);
        int lhsIndex = terms.indexOf(result);
        while (0 < result.size() && !sortedPostingsLists.isEmpty()) {
            PostingsList pl = sortedPostingsLists.remove(0);
            int rhsIndex = terms.indexOf(pl);
            ArrayList<Triplet> triplets = phraseSearch(result, pl, rhsIndex - lhsIndex);
            result = new PostingsList();
            for (Triplet triplet: triplets) {
                result.addEntry(triplet.docID, triplet.pos1);
            }
        }
        return result;
    }

    public ArrayList<Triplet> positionalIntersect(PostingsList lhs, PostingsList rhs, int k) {
        ArrayList<Triplet> answer = new ArrayList<>();
        int lhsIndex = 0;
        int rhsIndex = 0;
        while (lhsIndex < lhs.size() && rhsIndex < rhs.size()) {

            PostingsEntry lhsEntry = lhs.get(lhsIndex);
            PostingsEntry rhsEntry = rhs.get(rhsIndex);
            int lhsDocID = lhsEntry.docID;
            int rhsDocID = rhsEntry.docID;

            if (lhsDocID == rhsDocID) {
                LinkedList<Integer> list = new LinkedList<>();

                LinkedList<Integer> lhsOffsets = lhsEntry.getOffsets();
                LinkedList<Integer> rhsOffsets = rhsEntry.getOffsets();

                ListIterator<Integer> lhsOffsetIterator = lhsOffsets.listIterator();
                ListIterator<Integer> rhsOffsetIterator = rhsOffsets.listIterator();

                int lhsOffset = lhsOffsetIterator.next();
                int rhsOffset = rhsOffsetIterator.next();

                while (0 <= lhsOffset) {
                    while (0 <= rhsOffset) {
                        if (Math.abs(lhsOffset - rhsOffset) <= k) {
                            list.add(rhsOffset);
                        } else if (rhsOffset > lhsOffset) {
                            break;
                        }
                        rhsOffset = rhsOffsetIterator.hasNext() ? rhsOffsetIterator.next() : -1;
                    }
                    while (0 != list.size() && Math.abs(list.getFirst() - lhsOffset) > k) {
                        list.removeFirst();
                    }
                    ListIterator<Integer> iterator = list.listIterator();
                    while (iterator.hasNext()) {
                        answer.add(new Triplet(lhsDocID, lhsOffset, iterator.next()));
                    }
                    lhsOffset = lhsOffsetIterator.hasNext() ? lhsOffsetIterator.next() : -1;
                }
                lhsIndex++;
                rhsIndex++;
            } else if (lhsDocID < rhsDocID) {
                lhsIndex++;
            } else {
                rhsIndex++;
            }
        }
        return answer;
    }

    public ArrayList<Triplet> phraseSearch(PostingsList lhs, PostingsList rhs, int k) {
        ArrayList<Triplet> answer = new ArrayList<>();
        int lhsIndex = 0;
        int rhsIndex = 0;
        while (lhsIndex < lhs.size() && rhsIndex < rhs.size()) {

            PostingsEntry lhsEntry = lhs.get(lhsIndex);
            PostingsEntry rhsEntry = rhs.get(rhsIndex);
            int lhsDocID = lhsEntry.docID;
            int rhsDocID = rhsEntry.docID;

            if (lhsDocID == rhsDocID) {

                LinkedList<Integer> lhsOffsets = lhsEntry.getOffsets();
                LinkedList<Integer> rhsOffsets = rhsEntry.getOffsets();

                ListIterator<Integer> lhsOffsetIterator = lhsOffsets.listIterator();
                ListIterator<Integer> rhsOffsetIterator = rhsOffsets.listIterator();

                int lhsOffset = lhsOffsetIterator.next();
                int rhsOffset = rhsOffsetIterator.next();

                while (0 <= lhsOffset && 0 <= rhsOffset) {
                    if (rhsOffset - lhsOffset == k) {
                        answer.add(new Triplet(lhsDocID, lhsOffset, rhsOffset));
                        lhsOffset = lhsOffsetIterator.hasNext() ? lhsOffsetIterator.next() : -1;
                        rhsOffset = rhsOffsetIterator.hasNext() ? rhsOffsetIterator.next() : -1;
                    } else if (rhsOffset - lhsOffset > k) {
                        lhsOffset = lhsOffsetIterator.hasNext() ? lhsOffsetIterator.next() : -1;
                    } else {
                        rhsOffset = rhsOffsetIterator.hasNext() ? rhsOffsetIterator.next() : -1;
                    }
                }
                lhsIndex++;
                rhsIndex++;
            } else if (lhsDocID < rhsDocID) {
                lhsIndex++;
            } else {
                rhsIndex++;
            }
        }
        return answer;
    }

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
        if (shouldIndex) {
            spimi.flushBlock();
            System.err.println("Done adding blocks in: " + ( System.currentTimeMillis() - start) * 1e-3);
            try {
                start = System.currentTimeMillis();
                spimi.mergeBlocks();
                spimi.save();
                System.err.println("Done merging in: " + ( System.currentTimeMillis() - start) * 1e-3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.rankedQuery = new RankedQuery(spimi, docIDs, docLengths);
    }
}
