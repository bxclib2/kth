package ir;

import java.util.*;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;

/**
 * Created by hwaxxer on 04/02/17.
 */
public class SPIMInvert implements Serializable {

    // Member Variables
    private static final int PARTIAL_INDEX_SIZE = 65536;
    private int currentIndexSize = 0;
    private TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> postings = new TreeMap<>();
    private final String cache_path = Paths.get(System.getProperty("user.dir"), "/cache").toString();
    private final String index_path = Paths.get(System.getProperty("user.dir"), "/index").toString();

    static final Path cachedFileIndexPath = Paths.get(System.getProperty("user.dir"), "fileIndex");

    Map<String, File> fileIndex = new HashMap<>();
    Map<String, Integer> indexOffset = new HashMap<>();

    public void save() throws Exception {
        FileOutputStream fileOut =
                new FileOutputStream(cachedFileIndexPath.toFile());
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

    public static SPIMInvert load() {
        SPIMInvert index = null;
        try {
            FileInputStream fileIn = new FileInputStream(cachedFileIndexPath.toFile());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            if (o instanceof SPIMInvert) {
                index = (SPIMInvert) o;
            }
            in.close();
            fileIn.close();

        } catch (Exception e) {
            System.err.println("Did not load index from disk.");
        }
        return index;
    }


    /**
     * Add tokens to the block one at a time
     * @param token a term to placed into the index
     */
    public void addToBlock(String token, int documentID, int offset) {
        if (postings.size() <= PARTIAL_INDEX_SIZE) {
            if (!postings.containsKey(token)) {
                TreeMap<Integer, TreeSet<Integer>> posting = new TreeMap<>();
                TreeSet<Integer> h = new TreeSet<>();
                h.add(offset);
                posting.put(documentID, h);
                postings.put(token, posting);
            } else {
                TreeMap<Integer, TreeSet<Integer>> posting = postings.get(token);
                TreeSet<Integer> offsets = posting.get(documentID);
                if (null == offsets) {
                    offsets = new TreeSet<>();
                    posting.put(documentID, offsets);
                }
                offsets.add(offset);
            }
        } else {
            flushBlock(); // flush the block
            getNewBlock(); // get a new one
            addToBlock(token, documentID, offset); // add again
        }
    }

    /**
     * Writes the current block to disk
     */
    public void flushBlock() {
        if (0 < postings.size()) {
            System.out.println("Flushing block number " + currentIndexSize);
            String path = Paths.get(cache_path, String.valueOf(currentIndexSize) + ".spimi").toString();
            writeBlockToDisk(path);
        }
    }

    /**
     * Resets all of the parameters and provides you with a new postings list
     */
    public void getNewBlock() {
        postings = new TreeMap<>();
        currentIndexSize++;
    }

    public boolean writeBlockToDisk(String location) {
        try {
            FileWriter fw = new FileWriter(location);
            BufferedWriter out = new BufferedWriter(fw);

            for (String token : postings.keySet()) {
                out.write(token + " ");
                TreeMap<Integer, TreeSet<Integer>> posting = postings.get(token);
                for (Map.Entry<Integer, TreeSet<Integer>> entry: posting.entrySet()) {

                    StringJoiner joiner = new StringJoiner(",");
                    for (int offset : entry.getValue()) {
                        joiner.add("" + offset);
                    }
                    String offsetString = joiner.toString();

                    String line = entry.getKey() + ":" + offsetString + " ";
                    out.write(line);
                }
                out.write("\n");
            }
            out.close();
            fw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to disk: " + e);
            return false;
        }

    }

    public TreeMap<String, TreeSet<Long>> readBlockFromDisk(String location) {
        try {
            // get a handle to the input file
            FileReader inputStream = new FileReader(location);
            BufferedReader in = new BufferedReader(inputStream);

            // set up some instance vars
            String line;
            String[] parts;
            TreeMap<String, TreeSet<Long>> pl = new TreeMap<>();

            while ((line = in.readLine()) != null) {
                // read in a line and split it
                parts = line.split(" ");
                // add all the document ID to the terms posting
                TreeSet<Long> ps = new TreeSet<>();
                for (String part : parts) {
                    Scanner scanner = new Scanner(part);
                    scanner.useDelimiter("[:,]");
                    int docID = scanner.nextInt();
                    LinkedList<Integer> offsets = new LinkedList<>();
                    while (scanner.hasNextInt()) {
                        offsets.add(scanner.nextInt());
                    }
                    pl.put(parts[0], ps);
                }
                in.close();
                inputStream.close();
                return pl;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File[] inputFiles() {
        File folder = new File(cache_path);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    public void mergeBlocks() throws IOException {
        System.err.println("Merging blocks...");
        File[] inputFiles = inputFiles();
        merge(inputFiles);
    }

    public class PostingsListData {
        TreeMap<Integer, TreeSet<Integer>> postings = new TreeMap<>();

        public void merge(PostingsListData o) {
            for (Map.Entry<Integer, TreeSet<Integer>> entry : o.postings.entrySet()) {
                if (postings.containsKey(entry.getKey())) {
                    postings.get(entry.getKey()).addAll(entry.getValue());
                } else {
                    postings.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public PostingsList toPostingsList() {
            PostingsList pl = new PostingsList();
            for (Map.Entry<Integer, TreeSet<Integer>> posting : postings.entrySet()) {

                for (int offset : posting.getValue()) {
                    pl.addEntry(posting.getKey(), offset);
                }
            }
            return pl;
        }
    }

    public class PostingsParser implements Comparable<PostingsParser> {
        BufferedReader reader = null;
        PostingsListData postingsListData = null;
        private String word = null;

        public PostingsParser(File file) throws IOException {
            FileReader inputStream = new FileReader(file.toString());
            reader = new BufferedReader(inputStream);
        }

        public void next() throws IOException {
            String line = reader.readLine();
            if (line != null) {
                String[] items = line.split(" ");
                this.word = items[0];
                this.postingsListData = new PostingsListData();

                for (int i = 1; i < items.length; i++) {
                    String part = items[i];
                    Scanner scanner = new Scanner(part);
                    scanner.useDelimiter("[:,]");
                    int docID = scanner.nextInt();
                    TreeSet<Integer> offsets = new TreeSet<>();
                    while (scanner.hasNextInt()) {
                        int offset = scanner.nextInt();
                        offsets.add(offset);
                    }
                    this.postingsListData.postings.put(docID, offsets);
                }
            } else {
                this.word = null;
                this.postingsListData = null;
            }
        }

        public String getWord() {
            return word;
        }

        @Override
        public int compareTo(PostingsParser o) {
            return this.word.compareTo(o.getWord());
        }
    }


    private File getFilePath() {
        String fileName = UUID.randomUUID().toString();
        return Paths.get(index_path, fileName).toFile();
    }
    private void merge(File[] files) {
        PriorityQueue<PostingsParser> queue = new PriorityQueue<>();
        try {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                PostingsParser parser = new PostingsParser(file);
                parser.next();
                queue.add(parser);
            }

            PostingsParser parser = null;
            int POSTINGS_LIST_SIZE = 100;
            PostingsList[] postingsLists = new PostingsList[POSTINGS_LIST_SIZE];

            int index = 0;
            File file = getFilePath();

            while (!queue.isEmpty()) {
                if (index == POSTINGS_LIST_SIZE || file == null) {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(postingsLists);
                    out.close();
                    fileOut.close();
                    postingsLists = new PostingsList[POSTINGS_LIST_SIZE];

                    index = 0;
                    file = getFilePath();
                }
                parser = queue.poll();
                PostingsListData pld = parser.postingsListData;
                while (!queue.isEmpty() && queue.peek().getWord().equals(parser.getWord())) {
                    PostingsParser otherParser = queue.poll();
                    pld.merge(otherParser.postingsListData);
                    otherParser.next();
                    if (otherParser.getWord() != null) {
                        queue.add(otherParser);
                    }
                }
                PostingsList pl = pld.toPostingsList();
                postingsLists[index] = pl;
                fileIndex.put(parser.getWord(), file);
                indexOffset.put(parser.getWord(), index);

                parser.next();
                if (parser.getWord() != null) {
                    queue.add(parser);
                }
                index++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public PostingsList getPostings(String term) {
        PostingsList pl = null;
        File file = fileIndex.get(term);
        if (null != file) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                PostingsList[] postingsLists = (PostingsList[]) in.readObject();
                pl = postingsLists[indexOffset.get(term)];
                in.close();
                fileIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pl;
    }
}

