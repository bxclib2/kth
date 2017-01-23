import java.util.*;
import java.io.*;
import java.nio.*;

// Klassen WordList inneh�ller en ordlista och en datastruktur som h�ller
// reda p� anv�nda ord.

class WordList
{
    static private HashSet<CharBuffer> list; // ordlista
    static private HashSet<CharBuffer> used; // databas med anv�nda ord
    static int wordLength;
    static int size; // antal ord i ordlistan

    // Read l�ser in en ordlista fr�n str�mmen input. Alla ord ska ha
    // wordLength bokst�ver.
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
				   " i filen inneh�ller inte " + 
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
    // Contains sl�r upp w i ordlistan och returnerar ordet om det finns med.
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

    // MarkAsUsedIfUnused kollar om w �r anv�nt tidigare och returneras i s�
    // fall false. Annars markeras w som anv�nt och true returneras.
    static public boolean MarkAsUsedIfUnused(CharBuffer w)
    {
		if (used.contains(w)) {
			return false;
		}				
		used.add(w);
		return true;
    }

    // EraseUsed g�r s� att inga ord anses anv�nda l�ngre.
    static public void EraseUsed()
    {
		used.clear();
    }
}