
import java.util.*;
import java.io.*;

public class Flowproblem {
	
	public static final int UNKNOWN = 0, VISITED = 1;

	private static int nodes, s, t, u, e, first, last, size, maxFlow, res, nsize, min;
	private static Edge edge, edgeRev;
	
	private static ArrayList<Edge>[] edgesFrom;
	private static ArrayList<Edge> neighbour;
	private static Kattio io;
	private static StringBuilder sb;
	private static int[] color, queue;
	private static Edge[] path;
	
	public static void main(String[] args) {
		new Flowproblem();
		
	} 
	public Flowproblem() {
		//double pre = System.currentTimeMillis();
		io = new Kattio(System.in, System.out);
				
		readStuff();
		createStuff();
		constructGraph(size);
		readEdges(e);
		fordF();
		
		printFlows();
		io.close();
		//double post = System.currentTimeMillis();
		
		//System.err.println("time: " + (post - pre));
	}
	private final void createStuff() {
		size = nodes + 1;
		path = new Edge[size];
		color = new int[size];
		queue = new int[size];
		
	}
	
	private final void printFlows() {
		// print nodes
		io.println(nodes);
		
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
		io.println(s + " " + t + " " + maxFlow);
		
		// print e
		io.println(positiveFlow);
		io.println(sb.toString());
	}
	private final void fordF() {
		
		int v;
		while ((min = BFS()) != 0)
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
	private final int BFS() {
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
				if (color[edge.v] == 0 && edge.c > 0)
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
						return min;							
					}
					
				}
			}
		}
		return 0;
	}
	
	private final void readStuff() {
		nodes = io.getInt();
		s = io.getInt();
		t = io.getInt();
		e = io.getInt();
	}
	private final void constructGraph(int nodes) {
		edgesFrom = new ArrayList[nodes];
		
		for (int i = nodes; i-- > 1;) {
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
    private static final void addEdge(int u, int v, int c) {
	
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

	private static final void readEdges(int e) {
		for(int i = e; i-- > 0; ) {
			addEdge(io.getInt(), io.getInt(), io.getInt());
		}
	}

	private static class Edge {
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
	 * io io = new io(System.in, System.out);
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
	 *   io-instance, otherwise, you may lose output.
	 *
	 * - The getInt(), getDouble(), and getLong() methods will throw an
	 *   exception if there is no more data in the input, so it is generally
	 *   a good idea to use hasMoreTokens() to check for end-of-file.
	 *
	 * @author: Kattis
	 */

	
	private static class Kattio extends PrintWriter {
	    public Kattio(InputStream i) {
			super(new BufferedOutputStream(System.out));
			r = new BufferedReader(new InputStreamReader(i));
	    }
	    public Kattio(InputStream i, OutputStream o) {
			super(new BufferedOutputStream(o));
			r = new BufferedReader(new InputStreamReader(i));
	    }

	    public static boolean hasMoreTokens() {
			return peekToken() != null;
	    }

	    public static int getInt() {
			return Integer.parseInt(nextToken());
	    }

	    public static double getDouble() { 
			return Double.parseDouble(nextToken());
	    }

	    public static long getLong() {
			return Long.parseLong(nextToken());
	    }

	    public static String getWord() {
			return nextToken();
	    }

	    private static BufferedReader r;
	    private static String line;
	    private static StringTokenizer st;
	    private static String token;

	    private static String peekToken() {
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
		private static String nextToken() {
			String ans = peekToken();
			token = null;
			return ans;
			
	    }
	}
}