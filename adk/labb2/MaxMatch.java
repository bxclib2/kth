
import java.util.*;
import java.io.*;

public class MaxMatch {
	
	int nodes;
	int edges;
	int x;
	int y;
	int s;
 	int t;


	
	Kattio io;
	FlowSolve f;
	
	public static void main(String[] args) {
		
		new MaxMatch();
		
	}
	public MaxMatch() {
		io = new Kattio(System.in, System.out);
		f = new FlowSolve();
		reduce2flow();
		
		f.fordF();
		f.printFlows();
		io.flush();
	}

	public void reduce2flow() {
		nodes = numberOfNodes();
		f.createStuff(nodes + 3);
		f.constructGraph(nodes + 3);
		sourceAndSink();
		printEdges();
		
	}
	public int numberOfNodes() {
		x = io.getInt();
		y = io.getInt();

		return x + y;
	}

	public void sourceAndSink() {
		// source
		s = nodes + 1;
		// sink
		t = nodes + 2;
	}

	public void printEdges() {
		edges = io.getInt();
		
		for(int i = 1; i <= x; i++) {			
			f.addEdge(s, i, 1);
					
		}
		for(int i = 0; i < edges; i++) {
			f.addEdge(io.getInt(), io.getInt(), 1);
		}
		for(int i = x + 1; i < t; i++) {
			f.addEdge(i, t, 1);
		}

	}

		
	class FlowSolve {
		
		public final int UNKNOWN = 0, VISITED = 1;

		private int u, first, last, size, maxFlow, res, nsize, min;
		private Edge edge, edgeRev;

		private ArrayList<Edge>[] edgesFrom;
		private ArrayList<Edge> neighbour;
		private StringBuilder sb;
		private int[] color, queue;
		private Edge[] path;
		
		private  void createStuff(int n) {
			size = n;
			path = new Edge[n];
			color = new int[n];
			queue = new int[n];
	
		}
		private void constructGraph(int n) {
			edgesFrom = new ArrayList[n];
			for (int i = 1; i < n; i++)
	        {	
				edgesFrom[i] = new ArrayList<Edge>();
	        }
		}
		private void printFlows() {
			// print nodes
			io.println(x + " " + y);
	
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
			
					if(edge.f > 0 && (edge.u != s && edge.v != t && edge.v != s && edge.u != t)) {

						sb.append(edge.u);
						sb.append(" ");
						sb.append(edge.v);
						//sb.append(edge.f);
						sb.append("\n");
				
						positiveFlow++;
					}
				}
			}	
			// print e
			io.println(positiveFlow);
			io.println(sb.toString());
		}
		private void fordF() {
	
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
		private boolean BFS()  // Breadth First Search in O(V²)
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

		private void readStuff() {
			nodes = io.getInt();
			s = io.getInt();
			t = io.getInt();
			//e = io.getInt();
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

		private void readEdges(int e) {
			for(int i = e; i-- > 0; ) {
				addEdge(io.getInt(), io.getInt(), io.getInt());
			}
		}

		private void print(String s) {
			System.err.println(s);
		}

	
		class Edge
		{
			Edge rev;
		    int u; // points from
		    int v; // points to
			int f; // flow
			int c;
	
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

	
	class Kattio extends PrintWriter {
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
}