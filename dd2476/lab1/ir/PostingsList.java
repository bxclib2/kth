/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import javafx.geometry.Pos;

import java.util.ListIterator;
import java.util.LinkedList;
import java.io.Serializable;
import java.util.Comparator;

/**
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable, Comparable<PostingsList> {
    
    /** The postings list as a linked list. */
    private LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();


    /**  Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) {
	return list.get( i );
    }


    /* My shizzle */

    private String word = null;

    public PostingsList() {

    }

    public PostingsList(String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

    public void addEntry(int docID, int offset) {
        if (list.size() == 0) {
            PostingsEntry entry = new PostingsEntry(docID);
            entry.addOffset(offset);
            list.add(entry);
        } else {
            ListIterator<PostingsEntry> startIterator = list.listIterator();
            ListIterator<PostingsEntry> endIterator = list.listIterator(list.size());
            int start = 0;
            int end = list.size();
            while (start < end) {

                PostingsEntry lowEntry = startIterator.next();
                PostingsEntry highEntry = endIterator.previous();

                PostingsEntry entry = null;

                if (lowEntry.docID == docID) {
                    entry = lowEntry;
                } else if (highEntry.docID == docID) {
                    entry = highEntry;
                } else if (docID < lowEntry.docID) {
                    entry = new PostingsEntry(docID);
                    list.add(start, entry);
                } else if (highEntry.docID < docID) {
                    entry = new PostingsEntry(docID);
                    list.add(end, entry);
                }
                if (null != entry) {
                    entry.addOffset(offset);
                    return;
                }
                start = startIterator.nextIndex();
                end = endIterator.previousIndex();
            }
        }
    }

    public static Comparator<PostingsList> FrequencyComparator = new Comparator<PostingsList>() {
        public int compare(PostingsList lhs, PostingsList rhs) {
            return lhs.size() - rhs.size(); // Ascending
        }
    };

    public int compareTo(PostingsList other) {
        return this.word.compareTo(other.getWord());
    }

    public void print() {
        for (int i = 0; i < size(); i++) {
            System.err.println("entry doc ID; " + get(i).docID);
            for (int offset : get(i).getOffsets()) {
                System.err.println("" + offset);
            }
        }
    }
}
	

			   
