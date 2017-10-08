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

/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    //private HashMap<String,PostingsList> index = new HashMap<>();
    private BigramIndex bigramIndex;

    long start = System.currentTimeMillis();

    private SPIMInvert spimi;
    private boolean shouldIndex = false;

    private HashMap<Integer, LinkedList<String>> forwardIndex = new HashMap<>();

    private RankedQuery rankedQuery;
    private TFIDF tfidf;

    private Searcher searcher;

    public HashedIndex() {
        this.spimi = SPIMInvert.load();
        this.shouldIndex = false;
        if (this.spimi == null) {
            this.spimi = new SPIMInvert();
            this.shouldIndex = true;
        }
        this.tfidf = new TFIDF(this);
        this.searcher = new Searcher(this);
        this.bigramIndex = new BigramIndex();
    }

    /**
     *  Inserts this token in the index.
     */
    static int currentCocID = -1;
    public void insert( String token, int docID, int offset ) {
        if (this.shouldIndex) {
            this.spimi.addToBlock(token, docID, offset);
        }

        LinkedList<String> docTokens;

        if (currentCocID == docID) {
            // Existing document
            docTokens = this.forwardIndex.get(docID);
        } else {
            // New document
            docTokens = new LinkedList<>();
            this.forwardIndex.put(docID, docTokens);
            currentCocID = docID;

        }
        docTokens.add(token);

        //PostingsList pl = getPostings(token);
        //if (pl == null) {
        //    pl = new PostingsList();
        //    index.put(token, pl);
        //}
        //pl.addEntry(docID, offset);

        this.bigramIndex.insert(token, docID, offset);
    }


    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
        return this.spimi.getDictionary();
        //return this.index.keySet().iterator();
    }

    public HashMap<Integer, LinkedList<String>> getForwardIndex() {
        return forwardIndex;
    }

    public double getIDF(String term) {
        return this.tfidf.getIDFForTerm(term);
    }

    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
        return spimi.getPostings(token);
        //return this.index.get(token);
    }

    /**
     *  Searches the index for postings matching the query.
     */
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
        long start = System.nanoTime();
        int minSubphraseResult = 100;
        double subPhraseScoreFactor = 0.5;

        PostingsList pl = null;

        switch (structureType) {
            case SUBPHRASE:
                pl = searcher.positionalIntersectionQuery(query);
                if (pl.size() > minSubphraseResult) {
                    break;
                }
                // Fallthrough
            case BIGRAM:
                PostingsList bigramList = bigramIndex.search(query, queryType, rankingType, structureType);
                if (structureType == SUBPHRASE) {
                    System.err.println("fallthrough bigram: " + bigramList);
                    if (pl != null && bigramList != null) {
                        pl.mergeWithPostingsList(bigramList, subPhraseScoreFactor);
                        if (pl.size() > minSubphraseResult) {
                            System.err.println("pl size: " + pl.size());
                            break;
                        }
                    } else {
                        pl = bigramList;
                    }
                } else {
                    pl = bigramList;
                    break;
                }
            case UNIGRAM:
                // Classic Bigram or Unigram search
                PostingsList unigramList = unigramSearch(query, queryType, rankingType);
                if (structureType == SUBPHRASE) {
                    if (pl != null && unigramList != null) {
                        System.err.println("fallthrough unigram");
                        pl.mergeWithPostingsList(unigramList, subPhraseScoreFactor);
                    } else {
                        pl = unigramList;
                    }
                } else {
                    pl = unigramList;
                    break;
                }
        }
        System.err.println("Query time: " + ((System.nanoTime() - start) / 1e9));
        return pl;
    }

    private PostingsList unigramSearch(Query query, int queryType, int rankingType) {
        PostingsList pl = null;
        switch (queryType) {
            case INTERSECTION_QUERY: {
                pl = searcher.intersectionQuery(query, rankingType, UNIGRAM);
                break;
            }
            case PHRASE_QUERY: {
                pl = searcher.positionalIntersectionQuery(query);
                break;
            }
            case RANKED_QUERY: {
                pl = rankedQuery.search(query, rankingType, this.tfidf);
                break;
            }
        }
        return pl;
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
        this.rankedQuery = new RankedQuery(this);//, docIDs, docLengths);
        this.bigramIndex.cleanup();
    }
}
