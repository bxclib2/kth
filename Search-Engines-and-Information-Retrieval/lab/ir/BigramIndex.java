package ir;

import java.util.*;

/**
 * Created by hwaxxer on 2017-03-25.
 */
public class BigramIndex implements Index {
    private HashMap<String, PostingsList> index = new HashMap<>();
    private HashMap<Integer, LinkedList<String>> forwardIndex = new HashMap<>();

    private TFIDF tfidf;
    private RankedQuery rankedQuery;

    private String[] bigram = new String[2];

    public BigramIndex() {
        this.tfidf = new TFIDF(this);
    }

    private int currentDocID = -1;

    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) {
        if (currentDocID == docID) {
            // Existing document
            bigram[0] = bigram[1];
            bigram[1] = token;
        } else {
            // New document
            bigram[0] = null;
            bigram[1] = token; // initial case
        }
        currentDocID = docID;

        if (bigram[0] != null) {
            String biword = bigram[0] + " " + bigram[1];
            PostingsList pl = getPostings(biword);
            if (pl == null) {
                pl = new PostingsList();
                index.put(biword, pl);
            }
            pl.addEntry(docID, offset);
        }
    }

    Query toBigramQuery(Query query) {
        Query newQuery = new Query();
        for (int i = 0; i < query.terms.size() - 1; i++) {
           newQuery.terms.add(query.terms.get(i) + " " + query.terms.get(i+1));
           newQuery.weights.add(new Double(1));
        }
        return newQuery;
    }

    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
        return this.index.keySet().iterator();
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
        return this.index.get(token);
    }

    /**
     *  Searches the index for postings matching the query.
     */
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
        Query bigramQuery = toBigramQuery(query);

        long start = System.nanoTime();
        PostingsList pl = null;
        switch (queryType) {
            case INTERSECTION_QUERY:
                System.err.println("Phrase query not supported for Bigram search");
                break;
            case PHRASE_QUERY:
                System.err.println("Phrase query not supported for Bigram search");
                break;
            case RANKED_QUERY:
                pl = rankedQuery.search(bigramQuery, rankingType, this.tfidf);
                break;
        }

        System.err.println("Bigram query time: " + ((System.nanoTime() - start) / 1e9));
        return pl;
    }

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
        this.rankedQuery = new RankedQuery(this);
    }
}
