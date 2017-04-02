package ir;

import java.util.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class RankedQuery {

    private Index index;
    private Map<String, Double> pageRanks = new HashMap();

    RankedQuery(Index index) {
        this.index = index;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("pagerank/power-iteration-map"));
            this.pageRanks = (Map<String, Double>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PostingsList search(Query query, int rankingType, TFIDF tfidf) {
        PostingsList resultingPostingsList = new PostingsList();
        HashMap<Integer, PostingsEntry> addedDocIDs = new HashMap<>();

        float pageRankRatio = 0.8f;

        int t = 0;
        for (String term : query.terms) {

            PostingsList pl = index.getPostings(term);
            if (pl == null) continue;

            double idf = tfidf.getIDF(pl);

            for (int i = 0; i < pl.size(); i++) {
                PostingsEntry entry = pl.get(i).copy();
                int docID = entry.docID;

                // Get term frequency before replacing with previous entry
                double tf = tfidf.getTF(entry);

                if (addedDocIDs.containsKey(docID)) {
                    entry = addedDocIDs.get(docID);
                }

                double docLength = Index.docLengths.get("" + docID);
                double tfidfScore = query.weights.get(t) * tf * idf / docLength;

                String docName = Index.docIDs.get("" + docID);
                // sigh, strip .f
                docName = docName.substring(0, docName.length() - 2);
                double pageRankScore = pageRanks.get(docName);

                switch (rankingType) {
                    case Index.TF_IDF:
                        entry.score += tfidfScore;
                        break;
                    case Index.PAGERANK:
                        entry.score += pageRankScore;
                        break;
                    case Index.COMBINATION:
                        entry.score += (10 * pageRankRatio * pageRankScore + tfidfScore * (1 - pageRankRatio));
                        break;
                }
                if (!addedDocIDs.containsKey(docID)) {
                    resultingPostingsList.addEntry(entry);
                    addedDocIDs.put(docID, entry);
                }
            }
            t++;
        }

        resultingPostingsList.sortByScore();
        return resultingPostingsList;
    }
}