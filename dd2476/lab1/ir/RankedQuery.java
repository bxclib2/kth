package ir;

import java.util.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

class RankedQuery {

    private SPIMInvert index;
    private Map<String, Double> pageRanks = new HashMap();
    private Map<String, String> docIDs;
    private Map<Integer, Integer> docLengths;

    RankedQuery(SPIMInvert index, Map<String, String> docIDs, Map<Integer, Integer> docLengths) {
        this.index = index;
        this.docIDs = docIDs;
        this.docLengths = docLengths;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("pagerank/power-iteration-map"));
            this.pageRanks = (Map<String, Double>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PostingsList search(Query query, int rankingType) {
        PostingsList resultingPostingsList = new PostingsList();
        Set<Integer> addedDocIDs = new HashSet<>();

        float pageRankRatio = 0.5f;

        for (String term : query.terms) {

            PostingsList pl = index.getPostings(term);
            int docFrequency = pl.size();
            double idf = Math.log10(docIDs.size() / docFrequency);

            if (pl == null) continue;

            for (int i = 0; i < pl.size(); i++) {
                PostingsEntry entry = pl.get(i);
                int docID = entry.docID;
                if (addedDocIDs.contains(docID)) continue;

                double tfidfScore = (entry.getFrequency() * idf) / docLengths.get(docID);

                String docName = docIDs.get("" + docID);
                // sigh, strip .f
                docName = docName.substring(0, docName.length() - 2);
                double pageRankScore = pageRanks.get(docName);

                switch (rankingType) {
                    case Index.TF_IDF:
                        entry.score = tfidfScore;
                        break;
                    case Index.PAGERANK:
                        entry.score = pageRankScore;
                        break;
                    case Index.COMBINATION:
                        entry.score = pageRankRatio * pageRankScore + tfidfScore * (1 - pageRankRatio);
                        break;
                }
                resultingPostingsList.addEntry(entry);
                addedDocIDs.add(docID);
            }
        }

        resultingPostingsList.sortByScore();
        return resultingPostingsList;
    }
}