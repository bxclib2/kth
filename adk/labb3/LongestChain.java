import java.util.Iterator;
import java.nio.*;
import java.util.LinkedList;

class LongestChain
{
    //private Queue q; // kö som används i breddenförstsökningen
    private CharBuffer res;
	private CharBuffer lastword;
	private WordRec[] queue;
	private int first, last, curr, max;
	private WordRec wr;
	private CharBuffer goalWord; // slutord i breddenförstsökningen
    int wordLength;
    final char [] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
			       'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
			       's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'å', 
			       'ä', 'ö', 'é' };
    int alphabetLength = alphabet.length;

    public LongestChain(int wordLength) {
		this.wordLength = wordLength;
    }

    // IsGoal kollar om w är slutordet.
    private boolean IsGoal(CharBuffer w)
    {
		if(w.equals(goalWord)) {
			return true;	
		}
		return false;
    }

    // MakeSons skapar alla ord som skiljer på en bokstav från x.
    // Om det är första gången i sökningen som ordet skapas så läggs det
    // in i kön q.
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

    // BreadthFirst utför en breddenförstsökning från startWord för att
    // hitta kortaste vägen till endWord. Den kortaste vägen returneras
    // som en kedja av ordposter (WordRec).
    // Om det inte finns något sätt att komma till endWord returneras null.
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
    // CheckAllStartWords hittar den längsta kortaste vägen från något ord
    // till endWord. Den längsta vägen skrivs ut.
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
			    	// x.PrintChain(); // skriv ut den hittills längsta kedjan
				}
	    	}
		//}
		System.out.println(endWord + ": " + maxChainLength + " ord");
		if (maxChainRec != null) maxChainRec.PrintChain();
    	}
	}
