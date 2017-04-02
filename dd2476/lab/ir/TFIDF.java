package ir;

/**
 * Created by hwaxxer on 2017-03-23.
 */
public class TFIDF {
    private Index index;

    public TFIDF(Index index) {
        this.index = index;
    }

    public double getIDF(PostingsList pl) {
        double docFrequency = pl.size();
        double N = index.docIDs.size();
        double idf = Math.log10(N / docFrequency);
        return idf;
    }

    public double getIDFForTerm(String term) {
        return getIDF(index.getPostings(term));
    }

    public double getTF(PostingsEntry entry) {
        return entry.getFrequency();
    }
}
