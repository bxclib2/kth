package ir;

/**
 * Created by hwaxxer on 2017-03-25.
 */

import java.util.*;

public class Searcher {

    private Index index;

    public Searcher(Index index) {
        this.index = index;
    }

    public ArrayList<PostingsList> termsSortedByFrequency(Query query) {
        ArrayList<PostingsList> sortedTerms = new ArrayList<>();
        for (String term : query.terms) {
            PostingsList pl = index.getPostings(term);
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
            PostingsList pl = index.getPostings(term);
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

    //public ArrayList<Triplet> positionalIntersect(PostingsList lhs, PostingsList rhs, int k) {
    //    ArrayList<Triplet> answer = new ArrayList<>();
    //    int lhsIndex = 0;
    //    int rhsIndex = 0;
    //    while (lhsIndex < lhs.size() && rhsIndex < rhs.size()) {

    //        PostingsEntry lhsEntry = lhs.get(lhsIndex);
    //        PostingsEntry rhsEntry = rhs.get(rhsIndex);
    //        int lhsDocID = lhsEntry.docID;
    //        int rhsDocID = rhsEntry.docID;

    //        if (lhsDocID == rhsDocID) {
    //            LinkedList<Integer> list = new LinkedList<>();

    //            LinkedList<Integer> lhsOffsets = lhsEntry.getOffsets();
    //            LinkedList<Integer> rhsOffsets = rhsEntry.getOffsets();

    //            ListIterator<Integer> lhsOffsetIterator = lhsOffsets.listIterator();
    //            ListIterator<Integer> rhsOffsetIterator = rhsOffsets.listIterator();

    //            int lhsOffset = lhsOffsetIterator.next();
    //            int rhsOffset = rhsOffsetIterator.next();

    //            while (0 <= lhsOffset) {
    //                while (0 <= rhsOffset) {
    //                    if (Math.abs(lhsOffset - rhsOffset) <= k) {
    //                        list.add(rhsOffset);
    //                    } else if (rhsOffset > lhsOffset) {
    //                        break;
    //                    }
    //                    rhsOffset = rhsOffsetIterator.hasNext() ? rhsOffsetIterator.next() : -1;
    //                }
    //                while (0 != list.size() && Math.abs(list.getFirst() - lhsOffset) > k) {
    //                    list.removeFirst();
    //                }
    //                ListIterator<Integer> iterator = list.listIterator();
    //                while (iterator.hasNext()) {
    //                    answer.add(new Triplet(lhsDocID, lhsOffset, iterator.next()));
    //                }
    //                lhsOffset = lhsOffsetIterator.hasNext() ? lhsOffsetIterator.next() : -1;
    //            }
    //            lhsIndex++;
    //            rhsIndex++;
    //        } else if (lhsDocID < rhsDocID) {
    //            lhsIndex++;
    //        } else {
    //            rhsIndex++;
    //        }
    //    }
    //    return answer;
    //}

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
}
