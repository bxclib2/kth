
import java.util.*;
import java.io.*;

public class Flowproblem5 {
	
	public static final int UNKNOWN = 0, VISITED = 1;

	private String str;
	private int nodes, s, t, v, u, e, first, last, size, maxFlow;
	private static Edge edge, edgeRev;
	
	private static ArrayList<Edge>[] edgesFrom;
	private static Kattio kattio;
			
	private static int[] visited, queue;
	private static int[] minC;
	private static Edge[] path;
	
	public static void main(String[] args) {
		Flowproblem5 f = new Flowproblem5();
	}
	
	public Flowproblem5() {
		kattio = new Kattio(System.in, System.out);
		readNodes();
		createStuff();
		constructGraph(size);
		sourceAndSink();
		e = readE();
		readEdges(e);
		//double pre = System.currentTimeMillis();
		fordF();
		//BFS();
		//double post = System.currentTimeMillis();
		
		printFlows();
		kattio.close();
		
		//System.err.println("time: " + (post - pre));

	}
	
	private void createStuff() {
		size = nodes + 1;
		path = new Edge[size];
		minC = new int[size];
		visited = new int[size];
		queue = new int[size];
	}
	private void printFlows() {
		// print nodes
		kattio.println(nodes);
		
		// print source and sink with flow
		
		kattio.println(s + " " + t + " " + maxFlow);
		//System.err.println(s + " " + t + " " + maxFlow);
		
		//print flows
		int positiveFlow = 0;
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < edgesFrom.length; i++) {
			for(int j = 0; j < edgesFrom[i].size(); j++) {
				edge = edgesFrom[i].get(j);
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
		// print e
		kattio.println(positiveFlow);
		kattio.println(sb.toString());
	}
	private Edge findRev(Edge e) {
		for(int i = 0; i < edgesFrom[e.v].size(); i++) {
			edgeRev = edgesFrom[e.v].get(i);
			if(edgeRev.v == e.u) {	
				return edgeRev;
			}
			
		}
		return null; // should never happen
	}
	private void fordF() {
		
		for(int i = 1; i < edgesFrom.length; i++) {
			for(int j = 0; j < edgesFrom[i].size(); j++) {
				edge = edgesFrom[i].get(j);
				edge.res = edge.c;
			} 
		}
		
		while (BFS())
		{
			maxFlow += minC[t];
			v = t;
			while (v != s)
			{
				edge = path[v];
				edgeRev = findRev(edge);
								
				edge.f += minC[t];
				edgeRev.f -= minC[t];

				edge.res -= minC[t];
				edgeRev.res += minC[t];

				v = edge.u;			
			}

		}
	}
	private boolean BFS()  // Breadth First Search in O(V²)
	{
		Arrays.fill(minC, Integer.MAX_VALUE);
		visited = new int[size];
		first = last = 0;
		queue[last++] = s;

		while (first != last) {

			u = queue[first++];
			for(int v = 0; v < edgesFrom[u].size(); v++) {
				edge = edgesFrom[u].get(v);
				if (visited[edge.v] != VISITED && edge.res > 0)
				{
					minC[edge.v] = Math.min(minC[edge.u], edge.res);
					path[edge.v] = edge;
					
					if (edge.v == t) {
						return true;
					}
					visited[edge.v] = VISITED;
					queue[last++] = edge.v;
				}
			}
		}
		return false;
	}
	
    private static class Edge
	{
	    private int u; 
	    private int v;
		private int f;
		private int res;
		private int c;
		
	    Edge(int u, int v) {
			this.u = u;
			this.v = v;
		}
		Edge(int u, int v, Edge e) {
			this.u = u;
			this.v = v;
		}
		
	}
	private void readNodes() {
		nodes = kattio.getInt();
	}
	private void constructGraph(int nodes) {
		edgesFrom = new ArrayList[nodes];
		for (int i = 1; i < nodes; i++)
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
    private void addEdge(int u, int v, int c) {
	/*
		for(int i = 0; i < edgesFrom[v].size(); i++) {
			edge = edgesFrom[v].get(i);
			if(u == edge.v) {
				edge.rev.res += c;
				edge.rev.c += c;
				return;
			}
		}
		
		edge = new Edge(u,v);
				
		edge.c = c;
		edge.res = c;
		
		edgesFrom[u].add(edge);
		edgesFrom[v].add(edge.rev);
		*/
		for(int i = 0; i < edgesFrom[v].size(); i++) {
			edge = edgesFrom[v].get(i);
			if(v == edge.v) {
				edge.c = c;
				return;
			}
		}
		
		edge = new Edge(u,v);
		edgeRev = new Edge(v,u);	
		edge.c = c;
		
		edgesFrom[u].add(edge);
		edgesFrom[v].add(edgeRev);
		
    }
	private void sourceAndSink() {	
		s = kattio.getInt();
		t = kattio.getInt();
	}
	private int readE() {
		e = kattio.getInt();
		return e;
	}
	private void readEdges(int e) {
		for(int i = 0; i < e; i++) {
			addEdge(kattio.getInt(), kattio.getInt(), kattio.getInt());
		}
	}

	private static void print(String s) {
		System.err.println(s);
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

	
	static class Kattio extends PrintWriter {
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