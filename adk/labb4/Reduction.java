import java.util.*;
import java.io.*;

public class Reduction {
    
	private int nodes, edges, colours;  //antal roller, scener, skådespelare
	private Kattio io;
	private ArrayList[] edgeArray;     //kanterna representerar scener
		
	public static void main(String[] args){
		Reduction r = new Reduction();
	}
	
	public Reduction() {
        io = new Kattio(System.in, System.out);

		// read number of roles, scenes, actors
		readNodes();
		readEdges();
		readColours();
		
		edgeArray = new ArrayList[edges];
		Arrays.fill(edgeArray, new ArrayList<Integer>());
		
		graphColouring();

		io.close();
    }
	
	private void graphColouring() {

		int from;
		int to;
		for(int i = 0; i < edges; i++) {
		  	from = io.getInt();
		   	to = io.getInt();
		    edgeArray[from].add(to);
		}
	}

	public void readNodes(){
		nodes = io.getInt();
	}
	
	public void readEdges(){
		edges = io.getInt();
	}
	
	public void readColours(){
		colours = io.getInt();
	}
	
	/** Simple yet moderately fast I/O routines.
	 *
	 * Example usage:
	 *
	 * Kattio io = new Kattio(System.in, System.out);
	 *
	 * while (io.hasMoreTokens()) {
	 *    int n = io.getInt();
	 *    double d = io.getDouble();
	 *    double ans = d*n;
	 *
	 *    io.println("Answer: " + ans);
	 * }
	 *
	 * io.close();
	 *
	 *
	 * Some notes:
	 *
	 * - When done, you should always do io.close() or io.flush() on the
	 *   Kattio-instance, otherwise, you may lose output.
	 *
	 * - The getInt(), getDouble(), and getLong() methods will throw an
	 *   exception if there is no more data in the input, so it is generally
	 *   a good idea to use hasMoreTokens() to check for end-of-file.
	 *
	 * @author: Kattis
	 */

	class Kattio extends PrintWriter {
	    public Kattio(InputStream i) {
			super(new BufferedOutputStream(System.out));
			r = new BufferedReader(new InputStreamReader(i));
	    }
	    public Kattio(InputStream i, OutputStream o) {
			super(new BufferedOutputStream(o));
			r = new BufferedReader(new InputStreamReader(i));
	    }

	    public boolean hasMoreTokens() {
			return peekToken() != null;
	    }

	    public int getInt() {
			return Integer.parseInt(nextToken());
	    }

	    public double getDouble() { 
			return Double.parseDouble(nextToken());
	    }

	    public long getLong() {
			return Long.parseLong(nextToken());
	    }

	    public String getWord() {
			return nextToken();
	    }



	    private BufferedReader r;
	    private String line;
	    private StringTokenizer st;
	    private String token;

	    private String peekToken() {
		if (token == null) 
		    try {
			while (st == null || !st.hasMoreTokens()) {
			    line = r.readLine();
			    if (line == null) return null;
			    st = new StringTokenizer(line);
			}
			token = st.nextToken();
		    } catch (IOException e) { }
		return token;
	    }

	    private String nextToken() {
			String ans = peekToken();
			token = null;
			return ans;
	    }
	}
}


