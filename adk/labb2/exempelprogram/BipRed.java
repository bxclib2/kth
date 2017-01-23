/**
 * Exempel p� in- och utdatahantering f�r maxfl�deslabben i kursen
 * ADK.
 *
 * Anv�nder Kattio.java f�r in- och utl�sning.
 * Se http://kattis.csc.kth.se/doc/javaio
 *
 * @author: Per Austrin
 */

public class BipRed {
    Kattio io;
    
    void readBipartiteGraph() {
	// L�s antal h�rn och kanter
	int x = io.getInt();
	int y = io.getInt();
	int e = io.getInt();
	
	// L�s in kanterna
	for (int i = 0; i < e; ++i) {
	    int a = io.getInt();
	    int b = io.getInt();
	}
    }
    
    
    void writeFlowGraph() {
	int v = 23, e = 0, s = 1, t = 2;
	
	// Skriv ut antal h�rn och kanter samt k�lla och s�nka
	io.println(v);
	io.println(s + " " + t);
	io.println(e);
	for (int i = 0; i < e; ++i) {
	    int a = 1, b = 2, c = 17;
	    // Kant fr�n a till b med kapacitet c
	    io.println(a + " " + b + " " + c);
	}
	// Var noggrann med att flusha utdata n�r fl�desgrafen skrivits ut!
	io.flush();
	
	// Debugutskrift
	System.err.println("Skickade iv�g fl�desgrafen");
    }
    
    
    void readMaxFlowSolution() {
	// L�s in antal h�rn, kanter, k�lla, s�nka, och totalt fl�de
	// (Antal h�rn, k�lla och s�nka borde vara samma som vi i grafen vi
	// skickade iv�g)
	int v = io.getInt();
	int s = io.getInt();
	int t = io.getInt();
	int totflow = io.getInt();
	int e = io.getInt();
	
	for (int i = 0; i < e; ++i) {
	    // Fl�de f fr�n a till b
	    int a = io.getInt();
	    int b = io.getInt();
	    int f = io.getInt();
	}
    }
    
    
    void writeBipMatchSolution() {
	int x = 17, y = 4711, maxMatch = 0;
	
	// Skriv ut antal h�rn och storleken p� matchningen
	io.println(x + " " + y);
	io.println(maxMatch);
	
	for (int i = 0; i < maxMatch; ++i) {
	    int a = 5, b = 2323;
	    // Kant mellan a och b ing�r i v�r matchningsl�sning
	    io.println(a + " " + b);
	}
	
    }
    
    BipRed() {
	io = new Kattio(System.in, System.out);
	
	readBipartiteGraph();
	
	writeFlowGraph();
	
	readMaxFlowSolution();
	
	writeBipMatchSolution();

	// debugutskrift
	System.err.println("Bipred avslutar\n");

	// Kom ih�g att st�nga ner Kattio-klassen
	io.close();
    }
    
    public static void main(String args[]) {
	new BipRed();
    }
}

