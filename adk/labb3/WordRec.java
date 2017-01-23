import java.nio.*;
class WordRec
{
    CharBuffer word;
    WordRec father; // pekare till ordposten som skapat detta ord

    public WordRec(CharBuffer word, WordRec father) {
		this.word = word;
		this.father = father;
    }
    // ChainLength returnerar antalet ord i en kedja av ordposter. 
    public int ChainLength()
    {
		int i = 0;
		for (WordRec x = this; x != null; x = x.father) i++;
		return i;
    }

    private void PrintChainHelp()
    {
		if (father != null) father.PrintChainHelp();
		PrintWord();
		System.out.print(" -> ");
    }

    // PrintChain skriver ut en kedja av ordposter.
    public void PrintChain()
    {
		if (father != null) father.PrintChainHelp();
		PrintWord();
		System.out.println();
    }

    // PrintWord skriver ut ett ord.
    public void PrintWord()
    {
		System.out.print(word);
	}

}

