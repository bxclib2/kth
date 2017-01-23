
import java.util.*;
import java.io.*;

public class Flowproblem {
	
	public  final int UNKNOWN = 0, VISITED = 1;

	private  int nodes, s, t, u, e, first, last, size, maxFlow, res, nsize, min;
	private  Edge edge, edgeRev;
	
	private  ArrayList<Edge>[] edgesFrom;
	private  ArrayList<Edge> neighbour;
	private  Kattio kattio;
	private  StringBuilder sb;
	private  int[] color, queue;
	private  Edge[] path;
	
	public static void main(String[] args) {
		new Match2Flow();
		new Flowproblem();
		
	}
	
	public Flowproblem() {
		
		//double pre = System.currentTimeMillis();
		
		kattio = new Kattio(System.in, System.out);
		readStuff();
		createStuff();
		constructGraph(size);
		readEdges(e);
		fordF();
		
		printFlows();
		kattio.flush();
		//double post = System.currentTimeMillis();
		
		//System.err.println("time: " + (post - pre));
	}     
	
	private  void createStuff() {
		size = nodes + 1;
		path = new Edge[size];
		color = new int[size];
		queue = new int[size];
		
	}
	private  void printFlows() {
		// print nodes
		kattio.println(nodes);
		
		// print source and sink with flow
		
		//System.err.println(s + " " + t + " " + maxFlow);
		
		//print flows
		int positiveFlow = 0;
		sb = new StringBuilder();
		
		
		for(int i = size; i-- > 1; ) {
			neighbour = edgesFrom[i];
			nsize = neighbour.size();
			for(int j = nsize; j-- > 0; ) {
				edge = neighbour.get(j);
				
				if(edge.f > 0) {

					sb.append(edge.u);
					sb.append(" ");
					sb.append(edge.v);
					sb.append(" ");
					sb.append(edge.f);
					sb.append("\n");
					
					positiveFlow++;
				}
			}
		}
		kattio.println(s + " " + t + " " + maxFlow);
		
		// print e
		kattio.println(positiveFlow);
		kattio.println(sb.toString());
	}
	private  void fordF() {
		
		int v;
		while (BFS())
		{
			maxFlow += min;
			v = t;
			while (v != s)
			{
				
				//edge = path[v];
				path[v].c -= min;
				path[v].rev.c += min;
				path[v].f += min;
				path[v].rev.f -= min;

				v = path[v].u;			
			}

		}
	}
	private  boolean BFS()  // Breadth First Search in O(V²)
	{
		color = new int[size];
		first = last = 0;
		queue[last++] = s;
		min = Integer.MAX_VALUE;
		
		while (first != last) {

			u = queue[first++];
			neighbour = edgesFrom[u];
			nsize = neighbour.size();
			for(int v = nsize; v-- > 0; ) {
				edge = neighbour.get(v);
				if (color[edge.v] != VISITED && edge.c > 0)
				{
					path[edge.v] = edge;
					
					if (edge.v != t) {
						color[edge.v] = VISITED;
						queue[last++] = edge.v;
					} else {
						v = t;
						while(v != s) {
							edge = path[v];
							min = Math.min(edge.c, min);
							v = edge.u;
						}						
						return true;							
					}
					
				}
			}
		}
		return false;
	}
	
	private  void readStuff() {
		nodes = kattio.getInt();
		s = kattio.getInt();
		t = kattio.getInt();
		e = kattio.getInt();
	}
	private  void constructGraph(int nodes) {
		edgesFrom = new ArrayList[nodes];
		
		for (int i = nodes; i-- > 1;)
        {
			edgesFrom[i] = new ArrayList<Edge>();
        }
	}
	
    /**
     * Inserts an undirected edge with edge cost c between v and w.
     * Time complexity: O(1).
     * 
     * @param  c edge cost, c >= 0.
     * @param  v vertex.
     * @param  w vertex.
     * @throws IllegalArgumentException if v or w are out of range.
     * @throws IllegalArgumentException if c < 0.
     */
    private  void addEdge(int u, int v, int c) {
	
		// if it already has been added, we need to add the inverse capacity (u -> v)
		neighbour = edgesFrom[u];
		nsize = neighbour.size();
		for(int i = nsize; i-- > 0;) {
			edge = neighbour.get(i);
			if(v == edge.v) {
				edge.c = c;
				return;
			}
		}
		
		edge = new Edge(u,v);
				
		edge.c = c; // put capacity
		
		edgesFrom[u].add(edge); // u -> v
		edgesFrom[v].add(edge.rev); // v -> u
    }

	private  void readEdges(int e) {
		for(int i = e; i-- > 0; ) {
			addEdge(kattio.getInt(), kattio.getInt(), kattio.getInt());
		}
	}

	private void print(String s) {
		System.err.println(s);
	}
	private  class Edge
	{
		private Edge rev;
	    private int u; // points from
	    private int v; // points to
		private int f; // flow
		private int c;
		
	    Edge(int u, int v) {
			this.u = u;
			this.v = v;
			rev = new Edge(v,u,this);
		}
		Edge(int u, int v, Edge e) {
			this.u = u;
			this.v = v;
			rev = e;
		}
		
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



	    private  BufferedReader r;
	    private  String line;
	    private  StringTokenizer st;
	    private  String token;

	    private  String peekToken() {
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

	static class Match2Flow {
		static InputStreamReader converter = new InputStreamReader (System.in);
		static BufferedReader in = new BufferedReader (converter);

		static int nodes;
		static int edges;
		static int x;
		static int y;
		static int s;
		static int t;
		public static void main(String[] args) {
			Match2Flow.reduce2flow();
			Match2Flow.reverse2match();
		}

		public static void reverse2match() {
			removeSourceAndSink();
		}
		public static void removeSourceAndSink() {

			print(x + " " + y);

			getString();
			getString();
			int flowEdges = parse(getString());

			String edge;
			String[] split;
			int j = 0;
			String[] toPrint = new String[flowEdges];
			try {
				for(int i = 0; i < flowEdges; i++) {
					edge = getString();
					edge = edge.substring(0, edge.length() - 2);

					split = edge.split(" ");
					if ((parse(split[0]) == s) || (parse(split[0]) == t) || 
						(parse(split[1]) == s) || (parse(split[1]) == t)) {

					} else {
						toPrint[j] = edge;
						j++;
					}

				}
				print(j);
				for(int i = 0; i < j; i++) {
					print(toPrint[i]);
				}

			} catch (IndexOutOfBoundsException e) {}

		}
		public static void reduce2flow() {
			numberOfNodes();
			sourceAndSink();
			printEdges();
			System.out.flush();
		}
		public static void numberOfNodes() {
			String number = getString();
			String[] split = number.split(" ");
			try {
				x = parse(split[0]);
				y = parse(split[1]);
			} catch(IndexOutOfBoundsException e) {}
			nodes = x + y;
			print(nodes + 2);	
		}

		public static void sourceAndSink() {
			// source
			s = nodes + 1;
			// sink
			t = nodes + 2;
			print(s + " " + t);
		}

		public static void printEdges() {
			edges = parse(getString());
			int addedEdges = edges + nodes;
			print(addedEdges);
			// Start reading the edges
			for(int i = 1; i <= x; i++) {
				print(s + " " + i + " 1");
			}
			for(int i = 0; i < edges; i++) {
				print(getString() + " 1");
			}
			for(int i = x + 1; i <= nodes; i++) {
				print(i + " " + t + " 1");
			}

		}

		public static int parse(String s) {
			try {
				return Integer.parseInt(s);
			} catch( NumberFormatException e) {
				return -1;
			}
		}

		// Read a String from standard system input
		public static String getString() {
			try {
				return in.readLine();
			} catch( IOException e ) {
				return null;
			}
		}
		public static void print(String s) {
			System.out.println(s);
		}
		public static void print(int i) {
			System.out.println(i);
		}
	}
	
	
}