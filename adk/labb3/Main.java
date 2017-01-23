import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.*;
public class Main
{
    final static private int WordLength = 4;
    
    public static void main (String args[]) throws IOException
    {
		BufferedReader stdin =
	    	new BufferedReader(new InputStreamReader(System.in));
		WordList.Read(WordLength, stdin);
		LongestChain lc = new LongestChain(WordLength);
		while (true) {
	    	String line = stdin.readLine();
	    	if (line == null) break;
	    	String tokens[] = line.split(" ");
			CharBuffer startword = CharBuffer.allocate(WordLength);
			startword.put(tokens[0]);
			startword.rewind();
	    	if (tokens.length == 1) {
	    		lc.CheckAllStartWords(startword);
	    	} else if (tokens.length == 2) {
		
				CharBuffer endword = CharBuffer.allocate(WordLength);
				endword.put(tokens[1]);
				endword.rewind();
				WordRec wr = lc.BreadthFirst(startword, endword);
				if (wr == null) {
		    		System.out.println(startword + " " + 
				       endword + ": ingen lösning");
				} else {
		    		System.out.println(startword + " " + 
				       endword + ": "+ wr.ChainLength()
				       + " ord");
		    		wr.PrintChain();
				}
	    	} else{
				System.out.println("felaktig fråga: '" + line + "'");
				System.out.println("syntax för frågor: slutord");
				System.out.println("eller:             startord slutord");
	    	}	
		}
	}
}
