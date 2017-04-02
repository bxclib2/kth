/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2012
 */

import java.util.*;
import java.io.*;


public class PageRank{

    /**  
     *   Maximal number of documents. We're assuming here that we
     *   don't have more docs than we can keep in main memory.
     */
    final static int MAX_NUMBER_OF_DOCS = 2000000;

    /**
     *   Mapping from document names to document numbers.
     */
    Hashtable<String,Integer> docNumber = new Hashtable<String,Integer>();

    /**
     *   Mapping from document numbers to document names
     */
    String[] docName = new String[MAX_NUMBER_OF_DOCS];

    /**  
     *   A memory-efficient representation of the transition matrix.
     *   The outlinks are represented as a Hashtable, whose keys are 
     *   the numbers of the documents linked from.<p>
     *
     *   The value corresponding to key i is a Hashtable whose keys are 
     *   all the numbers of documents j that i links to.<p>
     *
     *   If there are no outlinks from i, then the value corresponding 
     *   key i is null.
     */
    Hashtable<Integer,Hashtable<Integer,Boolean>> link = new Hashtable<Integer,Hashtable<Integer,Boolean>>();

    /**
     *   The number of outlinks from each node.
     */
    int[] out = new int[MAX_NUMBER_OF_DOCS];

    /**
     *   The number of documents with no outlinks.
     */
    int numberOfSinks = 0;

    /**
     *   The probability that the surfer will be bored, stop
     *   following links, and take a random jump somewhere.
     */
    final static double BORED = 0.15;

    /**
     *   Convergence criterion: Transition probabilities do not 
     *   change more that EPSILON from one iteration to another.
     */
    final static double EPSILON = 0.0001;

    /**
     *   Never do more than this number of iterations regardless
     *   of whether the transistion probabilities converge or not.
     */
    final static int MAX_NUMBER_OF_ITERATIONS = 1000;


    final static String articleTitlesFilename = "articleTitles.txt";
    final static String pageRanksFilename = "pageRanks.pr";

    
    /* --------------------------------------------- */


    public PageRank( String filename ) {
	int noOfDocs = readDocs( filename );
	computePagerank( noOfDocs );
    }


    /* --------------------------------------------- */


    /**
     *   Reads the documents and creates the docs table. When this method 
     *   finishes executing then the @code{out} vector of outlinks is 
     *   initialised for each doc, and the @code{p} matrix is filled with
     *   zeroes (that indicate direct links) and NO_LINK (if there is no
     *   direct link. <p>
     *
     *   @return the number of documents read.
     */
    int readDocs( String filename ) {
	int fileIndex = 0;
	try {
	    System.err.print( "Reading file... " );
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    String line;
	    while ((line = in.readLine()) != null && fileIndex<MAX_NUMBER_OF_DOCS ) {
		int index = line.indexOf( ";" );
		String title = line.substring( 0, index );
		Integer fromdoc = docNumber.get( title );
		//  Have we seen this document before?
		if ( fromdoc == null ) {	
		    // This is a previously unseen doc, so add it to the table.
		    fromdoc = fileIndex++;
		    docNumber.put( title, fromdoc );
		    docName[fromdoc] = title;
		}
		// Check all outlinks.
		StringTokenizer tok = new StringTokenizer( line.substring(index+1), "," );
		while ( tok.hasMoreTokens() && fileIndex<MAX_NUMBER_OF_DOCS ) {
		    String otherTitle = tok.nextToken();
		    Integer otherDoc = docNumber.get( otherTitle );
		    if ( otherDoc == null ) {
			// This is a previousy unseen doc, so add it to the table.
			otherDoc = fileIndex++;
			docNumber.put( otherTitle, otherDoc );
			docName[otherDoc] = otherTitle;
		    }
		    // Set the probability to 0 for now, to indicate that there is
		    // a link from fromdoc to otherDoc.
		    if ( link.get(fromdoc) == null ) {
			link.put(fromdoc, new Hashtable<Integer,Boolean>());
		    }
		    if ( link.get(fromdoc).get(otherDoc) == null ) {
			link.get(fromdoc).put( otherDoc, true );
			out[fromdoc]++;
		    }
		}
	    }
	    if ( fileIndex >= MAX_NUMBER_OF_DOCS ) {
		System.err.print( "stopped reading since documents table is full. " );
	    }
	    else {
		System.err.print( "done. " );
	    }
	    // Compute the number of sinks.
	    for ( int i=0; i<fileIndex; i++ ) {
		if ( out[i] == 0 )
		    numberOfSinks++;
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "File " + filename + " not found!" );
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + filename );
	}
	System.err.println( "Read " + fileIndex + " number of documents" );
	return fileIndex;
    }


    /* --------------------------------------------- */


    /*
     *   Computes the pagerank of each document.
     */
    void computePagerank( int numberOfDocs ) {
        double[] probabilities;
        Map<String, String> articleTitles;

        try {
            articleTitles = getArticleTitles();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("power-iteration-scores"));
            probabilities = (double[])in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            long start = System.currentTimeMillis();
            probabilities = powerIteration(numberOfDocs);
            float seconds = 1e-3f * (System.currentTimeMillis() - start);
            System.err.println("Power iteration: " + seconds);
        }

        Map<String, Double> pageRanks = getPageRanks(probabilities, articleTitles);
        /* For score / plotting functions */
        int topK = 30;
        boolean ascending = false;
        computeScores(probabilities, numberOfDocs, topK, ascending);
        int N = numberOfDocs * 10;
        double[] visits = mc4(numberOfDocs, N);
        ArrayList<PRResult> results = getTopK(visits, 5, false);
        for (int i = 0; i < 5; i++) {
            PRResult r = results.get(i);
            System.err.println("results: " + r.docID +  ", score: " + r.score);
        }

        System.err.println("ok : " + pageRanks.get("Davis.f"));
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("power-iteration-map"));
            out.writeObject(pageRanks);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Map<String, String> getArticleTitles() throws IOException {
        Map<String, String> articleTitles = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(articleTitlesFilename));
        String line;
        while (null != (line = in.readLine())) {
            String[] parts = line.split(";");
            String internalID = parts[0];
            String name = parts[1];
            articleTitles.put(internalID, name);
        }
        return articleTitles;
    }

    void computeScores(double[] probabilities, int numberOfDocs, int topK, boolean ascending) {
        ArrayList<PRResult> topPages = getTopK(probabilities, topK, ascending);
        for (int i = 0; i < 30; i ++) {
            PRResult r = topPages.get(i);
            System.err.println(r.docID + ": " + r.score);
        }
        //int N = numberOfDocs;
        //ArrayList<ArrayList<PRResult>> simulations = new ArrayList<>();
//      //  simulations.add(mc1(numberOfDocs, N, numberOfDocs, ascending));
//      //  simulations.add(mc2(numberOfDocs, N, numberOfDocs, ascending));
//      //  simulations.add(mc3(numberOfDocs, N, numberOfDocs, ascending));
//      //  simulations.add(mc4(numberOfDocs, N, numberOfDocs, ascending));
//      //  simulations.add(mc5(numberOfDocs, N, numberOfDocs, ascending));
        //for (int i = 1; i < 20; i += 2) {
        //    double[] visits = mc5(numberOfDocs, N*i);
        //    simulations.add(getTopK(visits, topK, ascending));
        //}

        //StringJoiner joiner = new StringJoiner(", ");
        //for (int m = 0; m < simulations.size(); m++) {
        //    ArrayList<PRResult> topMC = simulations.get(m);
        //    double recall = 0;
        //    double rss = 0;
        //    for (int i = 0; i < topK; i++) {
        //        PRResult piResult = topPages.get(i);
        //        for (int j = 0; j < numberOfDocs; j++) {
        //            PRResult mcResult = topMC.get(j);
        //            if (mcResult.docID.equals(piResult.docID)) {
        //                if (j < topK) {
        //                    recall += 1. / topK;
        //                }
        //                double diff = Math.pow(piResult.score - mcResult.score, 2);
        //                rss += diff;
        //                //System.err.println("power score: " + piResult.score + ", mc: " + mcResult.score + ", rss: " + diff);
        //            }
        //        }
        //    }
        //    joiner.add("" + rss);
        //    System.err.println("recall: " + recall + ", RSS: " + rss);
        //}
        //System.err.println(joiner.toString());
    }

    class PRResult {
        String docID;
        double score;

        public PRResult(String docID, double score) {
            this.docID = docID;
            this.score = score;
        }
    }

    double[] mc1(int numberOfDocs, int N) {
        /* MC end-point with random start */
        long start = System.currentTimeMillis();
        Random random = new Random();
        double[] visits = new double[numberOfDocs];

        int walk = 0;
        while (walk++ < N) {
            int page = random.nextInt(numberOfDocs);
            while (random.nextFloat() > BORED) {
                Hashtable<Integer, Boolean> outgoing = link.get(page);
                if (outgoing == null) {
                    // Dangling node, jump to random
                    page = random.nextInt(numberOfDocs);
                } else {
                    int nrOfLinks = out[page];
                    ArrayList<Integer> keys = new ArrayList<>(outgoing.keySet());
                    page = keys.get(random.nextInt(nrOfLinks));
                }
            }
            visits[page] += 1. / N;
        }
        float seconds = 1e-3f * (System.currentTimeMillis() - start);
        System.err.println("MC1 time: " + seconds);
        return visits;
    }

    double[] mc2(int numberOfDocs, int N) {
        /* MC end-point with cyclic start */
        long start = System.currentTimeMillis();
        int M = N / numberOfDocs;
        Random random = new Random();
        double[] visits = new double[numberOfDocs];

        for (int m = 0; m < M; m++) {
            for (int n = 0; n < numberOfDocs; n++) {
                int page = n;
                while (random.nextFloat() > BORED) {
                    Hashtable<Integer, Boolean> outgoing = link.get(page);
                    if (outgoing == null) {
                        // Dangling node, jump to random
                        page = random.nextInt(numberOfDocs);
                    } else {
                        int nrOfLinks = out[page];
                        ArrayList<Integer> keys = new ArrayList<>(outgoing.keySet());
                        page = keys.get(random.nextInt(nrOfLinks));
                    }
                }
                visits[page] += 1. / (M*numberOfDocs);
            }
        }
        float seconds = 1e-3f * (System.currentTimeMillis() - start);
        System.err.println("MC2 time: " + seconds);
        return visits;
    }

    double[] mc3(int numberOfDocs, int N) {
        /* MC complete path */
        long start = System.currentTimeMillis();
        int M = N / numberOfDocs;
        Random random = new Random();
        double[] visits = new double[numberOfDocs];

        for (int m = 0; m < M; m++) {
            for (int n = 0; n < numberOfDocs; n++) {
                int page = n;
                LinkedList<Integer> visitedPages = new LinkedList<>();
                visitedPages.add(page);
                int t = 1;
                while (random.nextFloat() > BORED) {
                    t++;
                    Hashtable<Integer, Boolean> outgoing = link.get(page);
                    if (outgoing == null) {
                        // dangling node, jump to random
                        page = random.nextInt(numberOfDocs);
                    } else {
                        int nrOfLinks = out[page];
                        ArrayList<Integer> keys = new ArrayList<>(outgoing.keySet());
                        page = keys.get(random.nextInt(nrOfLinks));
                    }
                    visitedPages.add(page);
                }
                Iterator<Integer> iterator = visitedPages.listIterator();
                while (iterator.hasNext()) {
                    page = iterator.next();
                    visits[page] += 1. / (N * t);
                }
            }
        }
        float seconds = 1e-3f * (System.currentTimeMillis() - start);
        System.err.println("MC3 time: " + seconds);
        return visits;
    }

    double[] mc4(int numberOfDocs, int N) {
        /* MC complete path, stop at dangling nodes */
        long start = System.currentTimeMillis();
        int M = N / numberOfDocs;
        Random random = new Random();
        double[] visits = new double[numberOfDocs];

        int totalVisits = 0;
        for (int m = 0; m < M; m++) {
            for (int n = 0; n < numberOfDocs; n++) {
                int page = n;
                visits[page] += 1.;
                totalVisits++;
                while (random.nextFloat() > BORED) {
                    Hashtable<Integer, Boolean> outgoing = link.get(page);
                    if (outgoing == null) {
                        // Dangling node, break
                        break;
                    } else {
                        int nrOfLinks = out[page];
                        ArrayList<Integer> keys = new ArrayList<>(outgoing.keySet());
                        page = keys.get(random.nextInt(nrOfLinks));
                    }
                    visits[page] += 1.;
                    totalVisits++;
                }
            }
        }
        for (int i = 0; i < numberOfDocs; i++) {
            visits[i] /= totalVisits;
        }
        float seconds = 1e-3f * (System.currentTimeMillis() - start);
        System.err.println("MC4 time: " + seconds);
        return visits;
    }

    double[] mc5(int numberOfDocs, int N) {
        /* MC complete path, stop at dangling nodes */
        long start = System.currentTimeMillis();
        Random random = new Random();
        double[] visits = new double[numberOfDocs];

        int totalVisits = 1;
        Hashtable<Integer, Boolean> outgoing;
        for (int n = 0; n < N; n++) {
            int page = random.nextInt(numberOfDocs);
            visits[page] += 1.;
            while (random.nextFloat() > BORED) {
                outgoing = link.get(page);
                if (null == outgoing) {
                    break;
                }
                totalVisits++;
                ArrayList<Integer> keys = new ArrayList<>(outgoing.keySet());
                int nrOfLinks = out[page];
                page = keys.get(random.nextInt(nrOfLinks));
                visits[page] += 1.;
            }
        }
        for (int i = 0; i < numberOfDocs; i++) {
            visits[i] /= totalVisits;
        }
        float seconds = 1e-3f * (System.currentTimeMillis() - start);
        System.err.println("MC5 time: " + seconds);
        return visits;
    }

    double[] powerIteration(int numberOfDocs) {
		double[][] scores = getTransitionProbabilities(numberOfDocs);
        double[] probabilities = scores[0];

        double delta;
        int iteration = 0;
        while (++iteration < MAX_NUMBER_OF_ITERATIONS) {
            delta = 0;
            double[] newRow = new double[numberOfDocs];
            for (int i = 0; i < numberOfDocs; i++) {
                for (int j = 0; j < numberOfDocs; j++) {
                    double score = probabilities[j] * scores[j][i];
                    newRow[i] += score;
                }
                delta += Math.abs(probabilities[i] - newRow[i]);
            }
            probabilities = newRow;
            if (delta < EPSILON) {
                break;
            }
            System.err.println("iteration: " + iteration + ", delta: " + delta);
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("power-iteration-scores"));
            out.writeObject(probabilities);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return probabilities;
    }

    double[][] getTransitionProbabilities(int numberOfDocs) {
        double[][] transitions = new double[numberOfDocs][numberOfDocs];
        double NOT_BORED = (1 - BORED);
        double uniform = 1. / numberOfDocs;

        for (int i = 0; i < numberOfDocs; i++) {
            int nrOfLinks = out[i];
            Hashtable<Integer, Boolean> outgoing = link.get(i);
            for (int j = 0; j < numberOfDocs; j++) {
                double score;
                if (nrOfLinks == 0) {
                    score = uniform;
                } else {
                    score = BORED * uniform;
                    if (outgoing.containsKey(j)) {
                        score += NOT_BORED * (1. / nrOfLinks);
                    }
                }
                transitions[i][j] = score;
            }
        }
        return transitions;
    }

    ArrayList<PRResult> getTopK(double[] scores, int k, boolean ascending) {
        Integer[] idx = argsort(scores, ascending);
        ArrayList<PRResult> topPages = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int docID = idx[i];
            String name = docName[docID];
            double score = scores[docID];
            System.err.println("doc ID: " + docID + ", doc name: " + name + ", score: " + score);
            PRResult result = new PRResult(name, score);
            topPages.add(result);
            //System.err.println("doc: " + name + ", score: " + score);
        }
        return topPages;
    }

    Map<String, Double> getPageRanks(double[] scores, Map<String, String> articleTitles) {
        Map<String, Double> pageRanks = new HashMap<>();
        for (int i = 0; i < scores.length; i++) {
            String name = docName[i];
            double score = scores[i];
            String docName = articleTitles.get(name);
            pageRanks.put(docName, score);
        }
        return pageRanks;
    }


    public Integer[] argsort(double[] array, Boolean ascending) {
        int length = array.length;
        Integer[] idx = new Integer[length];
        for (int i = 0; i < length; i++) {
            idx[i] = i;
        }

        Arrays.sort(idx, new Comparator<Integer>() {
            @Override public int compare(final Integer lhs, final Integer rhs) {
                if (ascending) {
                    return Double.compare(array[lhs], array[rhs]);
                } else {
                    return Double.compare(array[rhs], array[lhs]);
                }
            }
        });
        return idx;
    }

    /* --------------------------------------------- */


    public static void main( String[] args ) {
	if ( args.length != 1 ) {
	    System.err.println( "Please give the name of the link file" );
	}
	else {
	    new PageRank( args[0] );
	}
    }
}
