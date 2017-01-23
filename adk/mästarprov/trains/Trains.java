import java.util.Random;
public class Trains {

	private static int[] trains;
	private static Random r;
	private static int M;
	private static int m;
	private static int parkedTrains;
	public static void main(String[] args) {		
		generateTrains(Integer.parseInt(args[0]));
		generateM();
		generateTree();
	}
	private class Node 
	{

		public Comparable data; //node data
		public Node right; //right child
		public Node left; //left child

		public Node(Comparable data) //node constructor
		{

			this(data, null, null);
		}

		public Node(Comparable data, Node left, Node right) //node constructor
		{

			this.data = data;
			this.left = left;
			this.right = right;
		}	
	}

	
	public static void generateTree() {
		
	}
	public static void generateTrains(int numTrains) {
		r = new Random();
		trains = new int[numTrains];
		print("Trainlengths: ");
		for(int i = 0; i < numTrains; i++) {
			trains[i] = r.nextInt(numTrains) + 1;
			print(trains[i]);
		}
	}
	public static void generateM() {
		int res = 0;
		int max = 0;
		for(int i = 0; i < trains.length; i++) {
			res += trains[i];
			if(max < trains[i]) max = trains[i];
		}

		if(trains.length == max) {
			print("M = " + max);
		}
		m = ((trains.length - max) / 2) + max;
		print("m: " + m);
		int[] M = new int[m];
		
	}
	public void findMinM(int node, int trainSize, m) {
		
	}
	public static void print(int i) {
		System.out.println(i);
	}
	public static void print(String s) {
		System.out.println(s);
	}
}