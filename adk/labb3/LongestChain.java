import java.util.Iterator;
import java.nio.*;
import java.util.LinkedList;

class LongestChain
{
    //private Queue q; // k� som anv�nds i breddenf�rsts�kningen
    private CharBuffer res;
	private CharBuffer lastword;
	private WordRec[] queue;
	private int first, last, curr, max;
	private WordRec wr;
	private CharBuffer goalWord; // slutord i breddenf�rsts�kningen
    int wordLength;
    final char [] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
			       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
			       's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '�', 
			       '�', '�', '�' };
    int alphabetLength = alphabet.length;

    public LongestChain(int wordLength) {
		this.wordLength = wordLength;
    }

    // IsGoal kollar om w �r slutordet.
    private boolean IsGoal(CharBuffer w)
    {
		if(w.equals(goalWord)) {
			return true;	
		}
		return false;
    }

    // MakeSons skapar alla ord som skiljer p� en bokstav fr�n x.
    // Om det �r f�rsta g�ngen i s�kningen som ordet skapas s� l�ggs det
    // in i k�n q.
    private WordRec MakeSons(WordRec x, boolean shortestPath)
    {
	 	res = CharBuffer.allocate(wordLength);
		for (int i = 0; i < wordLength; i++) {
			
			res.put(x.word);			
			res.rewind();	
			x.word.rewind();								
	    	for (int c = 0; c < alphabetLength; c++) {
				
				if (alphabet[c] != res.charAt(i)	) {
					res.put(i, alphabet[c]);
					res.rewind();
					
					if (WordList.Contains(res)) {
						wr = new WordRec(res, x);
						
						lastword = res;
						if (IsGoal(res) && shortestPath) {
							return wr;
						}		
						
						//q.Put(wr);
						queue[++last] = wr;
						//queue.offer(wr);
						
						res = CharBuffer.allocate(wordLength);
						
						res.put(x.word);
						res.rewind();
						x.word.rewind();
						//System.out.println(wr.word);

		    		}
				}
	    	}
		}
		return null;
    }

    // BreadthFirst utf�r en breddenf�rsts�kning fr�n startWord f�r att
    // hitta kortaste v�gen till endWord. Den kortaste v�gen returneras
    // som en kedja av ordposter (WordRec).
    // Om det inte finns n�got s�tt att komma till endWord returneras null.
    public WordRec BreadthFirst(CharBuffer startWord, CharBuffer endWord)
    {
		WordList.EraseUsed();
		WordRec start = new WordRec(startWord, null);
		WordList.MarkAsUsedIfUnused(startWord);
		goalWord = endWord;
		first = last = 0;
			
		queue = new WordRec[WordList.size];
				
		queue[++last] = start;
		while(first != last) {

			WordRec wr = MakeSons(queue[++first], true);
			
			if (wr != null) return wr;
		}
		return null;

    }
	public WordRec BreadthFirst2(CharBuffer endWord)
    {
		WordList.EraseUsed();
		WordRec start = new WordRec(endWord, null);
		WordList.MarkAsUsedIfUnused(endWord);

		first = last = 0;
					
		queue = new WordRec[WordList.size];
				
		queue[++last] = start;
		
		WordRec wordrec;
		while(first != last) {
			MakeSons(queue[++first], false);	

			//if (wr != null) return maxWR;
		}
		return BreadthFirst(lastword, endWord);
		
    }
    // CheckAllStartWords hittar den l�ngsta kortaste v�gen fr�n n�got ord
    // till endWord. Den l�ngsta v�gen skrivs ut.
    public void CheckAllStartWords(CharBuffer endWord)
    {
		int maxChainLength = 0;
		WordRec maxChainRec = null;
		int len = 0;
		//while(it.hasNext())
		//{
		WordRec x = BreadthFirst2(endWord);
		    
		    if (x != null) {
			
				len = x.ChainLength();
				if (len > maxChainLength) {
			    	maxChainLength = len;
			    	maxChainRec = x;
			    	// x.PrintChain(); // skriv ut den hittills l�ngsta kedjan
				}
	    	}
		//}
		System.out.println(endWord + ": " + maxChainLength + " ord");
		if (maxChainRec != null) maxChainRec.PrintChain();
    	}
	}
