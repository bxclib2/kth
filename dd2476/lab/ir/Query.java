/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Hedvig Kjellström, 2012
 */  

package ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Query {
    
    public LinkedList<String> terms = new LinkedList<String>();
    public LinkedList<Double> weights = new LinkedList<Double>();

    /**
     *  Creates a new empty Query 
     */
    public Query() {
    }
	
    /**
     *  Creates a new Query from a string of words
     */
    public Query( String queryString  ) {
	StringTokenizer tok = new StringTokenizer( queryString );
	while ( tok.hasMoreTokens() ) {
	    terms.add( tok.nextToken() );
	    weights.add( new Double(1) );
	}    
    }
    
    /**
     *  Returns the number of terms
     */
    public int size() {
	return terms.size();
    }
    
    /**
     *  Returns a shallow copy of the Query
     */
    public Query copy() {
	Query queryCopy = new Query();
	queryCopy.terms = (LinkedList<String>) terms.clone();
	queryCopy.weights = (LinkedList<Double>) weights.clone();
	return queryCopy;
    }
    
    /**
     *  Expands the Query using Relevance Feedback
     */
    public void relevanceFeedback( PostingsList results, boolean[] docIsRelevant, Indexer indexer, boolean indexElimination ) {
	// results contain the ranked list from the current search
	// docIsRelevant contains the users feedback on which of the 10 first hits are relevant

        long start = System.nanoTime();

        double alpha = 1.;
        double beta = 0.80;

        // Normalize query
        for (int w = 0; w < weights.size(); w++) {
            String term = terms.get(w);
            double idf = indexer.index.getIDF(term);
            weights.set(w, weights.get(w) * alpha * idf / terms.size());
        }

        double IDF_LIMIT = 1;

        HashMap<Integer, LinkedList<String>> forwardIndex = indexer.index.getForwardIndex();

        double relevantDocs = 0;
        for (int i = 0; i < docIsRelevant.length; i++) {
            if (docIsRelevant[i]) {
                relevantDocs += 1.0;
            }
        }
        for (int i = 0; i < docIsRelevant.length; i++) {
            if (docIsRelevant[i]) {
                PostingsEntry entry = results.get(i);
                LinkedList<String> tokens = forwardIndex.get(entry.docID);
                double docLength = Index.docLengths.get("" + entry.docID);
                for (String term: tokens) {
                    int existingIndex = terms.indexOf(term);
                    double idf = indexer.index.getIDF(term);
                    double score = beta / relevantDocs * idf / docLength;
                    if (existingIndex != -1) {
                        double oldWeight = weights.get(existingIndex);
                        weights.set(existingIndex, oldWeight + score);
                    } else {
                        // Index elimination
                        if (indexElimination) {
                            if (IDF_LIMIT < idf) {
                                weights.add(score);
                                terms.add(term);
                            }
                        } else {
                            weights.add(score);
                            terms.add(term);
                        }
                    }
                }
            }
        }
        //System.err.println("weights: " + weights);
        System.err.println("Relevance feedback time: " + ((System.nanoTime() - start) / 1e9));
    }
}

    
