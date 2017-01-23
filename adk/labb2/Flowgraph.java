import java.io.*;
 
public class FlowGraph
{
	public static final int WHITE = 0, GRAY = 1, BLACK = 2;
	private int[][] flow, capacity, res_capacity;
	private int[] parent, color, queue;
	private int[] min_capacity;
	private int size, source, sink, first, last, e, nodes, s, t;
	private int max_flow;
	private String str;
	
	private BufferedReader in;
 
	public static void main(String[] args) {
		FlowGraph f = new FlowGraph();
	}
	public FlowGraph()
	{
		 // Read "size" value, "capacity[size][size]" matrix,
		   // as well as "source" and "sink" node indexes (0-based)
		   // from an input text file.
		in = new BufferedReader(new InputStreamReader(System.in));
		
		readNodes();
		size = nodes + 1;
		flow = new int[size][size];
		res_capacity = new int[size][size];
		parent = new int[size];
		min_capacity = new int[size];
		color = new int[size];
		queue = new int[size];
		
		sourceAndSink();
		e = readE();
		readEdges(e);
		maxFlow();
		printFlows();
	}
	public void printFlows() {
		// print nodes
		print(nodes);
		
		// print source and sink with flow
		print(s + " " + t + " " + max_flow);

		
		//print flows
		int positiveFlow = 0;
		String[] flowStr = new String[e];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(flow[i][j] > 0) {
					flowStr[positiveFlow] = i + " " + j + " " + flow[i][j];
					positiveFlow++;
				}
			}
		}
		// print e
		print(positiveFlow);
		for(int i = 0; i < positiveFlow; i++) {
			print(flowStr[i]);
		}
	}
	
	public void readNodes() {
		nodes = readInt();
	}
	public void addEdge(int v, int w, int c) {
		/*
        if (v < 0 || v > size || w < 0 || w > size)
            throw new IllegalArgumentException(
                "Out of range: v = " + v + ", w = " + w + ".");
        if (c < 0)
            throw new IllegalArgumentException(
                "Illegal cost: c = " + c + ".");
		*/

        
        //edgeCosts.put(new Edge(v, w), c);
		res_capacity[v][w] = c;
		
    }
 	public void sourceAndSink() {
		str = readLine();
		String[] split = str.split(" ");
	
		s = s2i(split[0]);
		t = s2i(split[1]);
	}
	public int readE() {
		e = readInt();
		return e;
	}
	public void readEdges(int e) {
		for(int i = 0; i < e; i++) {
			readEdge();
		}
	}
	public void readEdge() {
		String[] split = readLine().split(" ");
		addEdge(s2i(split[0]),s2i(split[1]),s2i(split[2]));	
	}
	public int s2i(String s) {
		return Integer.parseInt(s);
	}
	public String readLine() {
		try {
			return in.readLine();
		} catch(IOException e) {}
		return null;
	}
	public int readInt() {
		return Integer.parseInt(readLine());
	}
	public static void print(String s) {
		System.out.println(s);
	}
	public static void print(int i) {
		System.out.println(i);
	}
	private void maxFlow()  // Edmonds-Karp algorithm with O(V³E) complexity
	{
 
 
		while (BFS(s))
		{
			max_flow += min_capacity[t];
			int v = t, u;
			while (v != s)
			{
				u = parent[v];
				flow[u][v] += min_capacity[t];
				flow[v][u] -= min_capacity[t];
				res_capacity[u][v] -= min_capacity[t];
				res_capacity[v][u] += min_capacity[t];
				v = u;
			}
		}
	}
 
	private boolean BFS(int source)  // Breadth First Search in O(V²)
	{
		for (int i = 0; i < size; i++)
		{
			color[i] = WHITE;
			min_capacity[i] = Integer.MAX_VALUE;
		}
 
		first = last = 0;
		queue[last++] = source;
		color[source] = GRAY;
 
		while (first != last)  // While "queue" not empty..
		{
			int v = queue[first++];
			for (int u = 0; u < size; u++)
				if (color[u] == WHITE && res_capacity[v][u] > 0)
				{
					min_capacity[u] = Math.min(min_capacity[v], res_capacity[v][u]);
					parent[u] = v;
					color[u] = GRAY;
					if (u == t) return true;
					queue[last++] = u;
				}
		}
		return false;
	}
 
}