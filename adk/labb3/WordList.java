import java.util.*;
import java.io.*;
import java.nio.*;

// Klassen WordList innehåller en ordlista och en datastruktur som håller
// reda på använda ord.

class WordList
{
    static private HashSet<CharBuffer> list; // ordlista
    static private HashSet<CharBuffer> used; // databas med använda ord
    static int wordLength;
    static int size; // antal ord i ordlistan

    // Read läser in en ordlista från strömmen input. Alla ord ska ha
    // wordLength bokstäver.
    static public void Read(int wordLength_, BufferedReader input)
	throws IOException
    {
		wordLength = wordLength_;
		size = 0;
		list = new HashSet<CharBuffer>();
		while (true) {
			CharBuffer c = CharBuffer.allocate(wordLength);
	    	String s = input.readLine();
	    	if (s.equals("#")) break;
	    	if (s.length() != wordLength) {
				System.out.println("Rad " + size + 
				   " i filen innehåller inte " + 
				   wordLength + " tecken.");
			} else {
				c = c.put(s);
				c.rewind();								
	    		list.add(c);
			}
	    	size++;
		}
		used = new HashSet<CharBuffer>();
    }

    // WordAt returnerar ordet med angivet index i ordlistan.
    /*static public String WordAt(int index)
    {
		if (index >= 0 && index < size) return (String) list.elementAt(index);
		else return null;
    }*/
	static public Iterator<CharBuffer> getIterator() {
		return list.iterator();
	}
    // Contains slår upp w i ordlistan och returnerar ordet om det finns med.
    // Annars returneras null.
    static public boolean Contains(CharBuffer w)
    {		
		if (list.contains(w) && !(used.contains(w))) {
			used.add(w);
			return true; 
		}
		else {
			return false;
		}
    }
	static public boolean Contains(char[] w)
    {		
		CharBuffer tmp = CharBuffer.allocate(4);
		tmp.put(w);
		tmp.rewind();
		if (list.contains(tmp) && !(used.contains(tmp))) {
			used.add(tmp);
			return true; 
		}
		else {
			return false;
		}
    }

    // MarkAsUsedIfUnused kollar om w är använt tidigare och returneras i så
    // fall false. Annars markeras w som använt och true returneras.
    static public boolean MarkAsUsedIfUnused(CharBuffer w)
    {
		if (used.contains(w)) {
			return false;
		}				
		used.add(w);
		return true;
    }

    // EraseUsed gör så att inga ord anses använda längre.
    static public void EraseUsed()
    {
		used.clear();
    }
}