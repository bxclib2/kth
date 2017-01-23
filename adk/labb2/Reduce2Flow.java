import java.util.*;
import java.io.*;

public class Reduce2Flow {

	private int nodes;
	private int edges;
	private int x;
	private int y;
	private int s;
 	private int t;
	private Kattio io;

	public static void main(String[] args) {
		new Reduce2Flow();
	}
	public Reduce2Flow() {
		io = new Kattio(System.in, System.out);
		double pre = System.currentTimeMillis();
		reduce2flow();
		reverse2match();
		double post = System.currentTimeMillis();
		System.err.println(post-pre);
	}
	private void reduce2flow() {
		numberOfNodes();
		sourceAndSink();		
		printEdges();		
	}
	private void numberOfNodes() {
		x = io.getInt();
		y = io.getInt();

		nodes = x + y;
	}

	private void sourceAndSink() {
		// source
		s = nodes + 1;
		// sink
		t = nodes + 2;
	}

	private void printEdges() {
		edges = io.getInt();
		io.println(nodes + 2);	
		io.println(s + " " + t);
		io.println(edges+nodes);
		// Start reading the edges
		for(int i = 1; i <= x; i++) {
			io.println(s + " " + i + " 1");
		}
		for(int i = 0; i < edges; i++) {
			io.println(io.readLine() + " 1");
		}
		for(int i = x + 1; i <= nodes; i++) {
			io.println(i + " " + t + " 1");
		}
		io.flush();
	}
	private void reverse2match() {
		removeSourceAndSink();
	}
	private void removeSourceAndSink() {

		io.readLine();
		io.readLine();
				
		int flowEdges = io.getInt();
		
		io.println(x + " " + y);
		int j = 0;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < flowEdges; i++) {
				
			x = io.getInt();
			y = io.getInt();
			io.getInt();
			if(x != s && x != t && y != s && y != t) {
				//io.println(from + " " + to);
				sb.append(x + " " + y + "\n");
				j++;				
			}

		}
		io.println(j);
		io.println(sb.toString());
		io.flush();
	}

	private  class Kattio extends PrintWriter {
	    public Kattio(InputStream i) {
			super(new BufferedOutputStream(System.out));
			r = new BufferedReader(new InputStreamReader(i));
	    }
	    public Kattio(InputStream i, OutputStream o) {
			super(new BufferedOutputStream(o));
			r = new BufferedReader(new InputStreamReader(i));
	    }

	    public  boolean hasMoreTokens() {
			return peekToken() != null;
	    }

	    public  int getInt() {
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

		public String readLine() {
			try {
				return r.readLine();
			} catch(IOException e) {}
			return null;
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

	    private  String nextToken() {
			String ans = peekToken();
			token = null;
			return ans;
	    }
	}
}