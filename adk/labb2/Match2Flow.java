import java.io.*;

public class Match2Flow {
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
