
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.ListIterator;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;

public class Flowproblem3 {
	
	public static final int UNKNOWN = 0, VISITED = 1;

	private String str;
	private int nodes, s, t, v, u, e, first, last, size, maxFlow;
	private static Edge edge, edgeRev;
	
	private static ArrayList<ArrayList <Edge>> edgesFrom;
	private static Kattio kattio;
	
	//private static ListIterator<Edge> it;
		
	private static int[] color, queue;
	private static int[] minC;
	private static Edge[] path;
	
	public static void main(String[] args) {
		Flowproblem3 f = new Flowproblem3();
	}
	
	public Flowproblem3() {
		kattio = new Kattio(System.in, System.out);
		readNodes();
		createStuff();
		constructGraph(size);
		sourceAndSink();
		e = readE();
		readEdges(e);
		double pre = System.currentTimeMillis();
		fordF();
		//BFS();
		double post = System.currentTimeMillis();
		
		printFlows();
		kattio.close();
		System.err.println("time: " + (post - pre));

	}
	
	private void createStuff() {
		size = nodes + 1;
		path = new Edge[size];
		minC = new int[size];
		color = new int[size];
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
		String[] flowStr = new String[e];
		for(int i = 1; i < edgesFrom.size(); i++) {
			for(int j = 0; j < edgesFrom.get(i).size(); j++) {
				//edge = it.next();
				edge = edgesFrom.get(i).get(j);
				if(edge.f > 0) {
					flowStr[positiveFlow] = edge.u + " " + edge.v + " " + edge.f;
					positiveFlow++;
				}
			}
		}
		// print e
		kattio.println(positiveFlow);
		for(int i = 0; i < positiveFlow; i++) {
			kattio.println(flowStr[i]);
		}
	}
	private void fordF() {
		
		while (BFS())
		{
			maxFlow += minC[t];
			v = t;
			while (v != s)
			{
				path[v].f += minC[t];
				path[v].rev.f -= minC[t];

				path[v].res -= minC[t];
				path[v].rev.res += minC[t];	
				v = path[v].u;			
			}

		}
	}
	private boolean BFS()  // Breadth First Search in O(V²)
	{
		for (int i = 1; i < size; i++) {
			
			minC[i] = Integer.MAX_VALUE;
		}
		color = new int[size];
		first = last = 0;
		queue[last++] = s;

		while (first != last) {
			//it = edgesFrom[queue[first++]].listIterator(0);
			//while(it.hasNext()) {
			u = queue[first++];
			for(int v = 0; v < edgesFrom.get(u).size(); v++) {
				//edge = it.next();
				edge = edgesFrom.get(u).get(v);
				if (color[edge.v] != VISITED && edge.res > 0)
				{
					minC[edge.v] = Math.min(minC[edge.u], edge.res);
					path[edge.v] = edge;
					color[edge.v] = VISITED;
					if (edge.v == t) {
						return true;
					}
					
					queue[last++] = edge.v;
				}
			}
		}
		return false;
	}
	
    private static class Edge
	{
		private Edge rev;
	    private int u; 
	    private int v;
		private int f;
		private int res;
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
	private void readNodes() {
		nodes = kattio.getInt();
	}
	private void constructGraph(int nodes) {
		//System.err.println(nodes + " size:" + size);
		edgesFrom = new ArrayList<ArrayList<Edge>>();
		for (int i = 0; i < nodes; i++)
        {
            edgesFrom.add(i, new ArrayList<Edge>());
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
	
		//it = edgesFrom[v].listIterator(0);
		//while(it.hasNext()){
		for(int i = 0; i < edgesFrom.get(v).size(); i++) {
			edge = edgesFrom.get(v).get(i);
			//edge = it.next();
			if(u == edge.v) {
				edge.rev.res += c;
				edge.rev.c += c;
				return;
			}
		}
		
		edge = new Edge(u,v);
				
		edge.c = c;
		edge.res = c;
		
		edgesFrom.get(u).add(edge);
		edgesFrom.get(v).add(edge.rev);
		
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