import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class BlackBox {
	Kattio io;
	private int v,s,t,e;
	
	//private int[] flowGraphA;
	//private int[] flowGraphB;
	//private int[] flowGraphC;
	
	private ArrayList[] graph;
	private LinkedList<Integer> que;
	private boolean[] visited;
	private int[] flow;
	private int[] parent;
	private int totalFlow;
	long time;
	long totalTime;
	
	public BlackBox(){
		
		startTime();
		int currentFlow = 1;
		io = new Kattio(System.in, System.out);
		readFlowGraph();
		
		//printGraph();

		
		while(currentFlow != 0)
		{
			//System.out.println("NEW EDMONDKARP");
			reset();
			currentFlow = edmondKarp();
			//System.out.println("CurrentFlow is: " + currentFlow);
			//printGraph();
			totalFlow += currentFlow;
			
		}
		
		//startTime();	
		writeMaxFlowGraph();
		//printGraph();
		io.close();
		
		
		stopTime();
		totalTime += stopTime();
		System.err.println("totaltime: " + totalTime);
		
	}
	
	private double stopTime() {
		return System.currentTimeMillis()-time;
		
	}

	private void startTime() {
		time = System.currentTimeMillis();
		
	}

	private void reset() {
		//visited = new boolean[v+1];
		parent = new int[v+1];
		parent[s] = s;
		flow = new int[v+1];
	}

	private void printGraph() {
		int[] tempArray = new int[4];
		for(int i = 0; i < v+1;i++)
		{
			int linkedListSize = graph[i].size();
			for(int k = 0; k < linkedListSize; k++)
			{
				tempArray = (int[]) graph[i].get(k);
				System.out.println(i + "->" + tempArray[0]);
				System.out.println("Kapacitet:" + tempArray[1]);
				System.out.println("Flöde:" + tempArray[2]);
				System.out.println("RestKap:" + tempArray[3]);
				System.out.println();
			}
		}
		
	}

	private void readFlowGraph() {
		int a,b,c,linkedListSize;
		v = io.getInt();
		s = io.getInt();
		t = io.getInt();
		e = io.getInt();
		reset();
		graph = new ArrayList[v+1];
		for(int i = 0; i < (v+1); i++)
		{
			graph[i] = new ArrayList();
		}
		
		
		for(int i = 0; i < e;i++)
		{
			a = io.getInt();
			b = io.getInt();
			c = io.getInt();
			
			boolean isFound = false;
			
			int[] arrayA = new int[4];
			arrayA[0] = b;	// närliggande nod
			arrayA[1] = c;	// kapacitet       c
			arrayA[2] = 0;	// flöde
			arrayA[3] = c;	// restkapacitet   c
			
			
			int[] tempArray = new int[4];
			linkedListSize = graph[a].size();
			for(int k = 0; k < linkedListSize; k++)
			{
				tempArray = (int[]) graph[a].get(k);
				if(tempArray[0] == b)
				{
					graph[a].remove(k);
					graph[a].add(arrayA);
					isFound = true;
					//System.out.println("Hittade en kopia");
					break;
				}
			}
			if(!isFound) // om kanten inte hittades.
			{
				//System.out.println("Hittade ingen kopia");
				graph[a].add(arrayA);
				
				
				int[] arrayB = new int[4];
				arrayB[0] = a;	// närliggande nod
				arrayB[1] = 0;	// kapacitet
				arrayB[2] = 0;	// flöde
				arrayB[3] = 0;	// restkapacitet
				
				graph[b].add(arrayB);
			}

			
			
		}
		
	}
	
	private int edmondKarp()
	{
		int currentNode;
		int currentFlow;
		
		que = new LinkedList<Integer>();
		
		addQue(s);
		
	
		while(!que.isEmpty())
		{
			
			
			currentNode = que.remove();
			
			//System.out.println("Current node is: " + currentNode);
			//qPrint();
			
			if(currentNode == t){
				//System.out.println("##############");
				
				currentFlow = getFlowFrom(t);
				
				adjustKapFlowRest(t, currentFlow);
				
				return currentFlow;
			}
			else
			{	
				
				addQue(currentNode);
				
			}
		}
		return 0;
	}
	
	private void qPrint() {
		int qLength = que.size();
		for(int j = 0; j < qLength; j++)
		{
			System.out.print(que.get(j) + " ");
			
		}
		System.out.println();
		
		
	}

	private void addQue(int node){
		int linkedListSize = graph[node].size();
		int[] tempArray = new int[4];
		for(int i = 0; i < linkedListSize;i++)
		{
			tempArray = (int[]) graph[node].get(i);
			if(tempArray[3] > 0 && parent[tempArray[0]] == 0)
				{
					//System.out.println("Adding to que: " + tempArray[0]);
					que.addLast(tempArray[0]);
					parent[tempArray[0]] = node;
					flow[tempArray[0]] = tempArray[3];
				}
		}
	}
	

	private int getFlowFrom(int child)
	{
		int currentFlow = Integer.MAX_VALUE;
		int parentNode = parent[child];
		
		//int linkedListSize = graph[parentNode].size();
		while(true)
		{
			//System.out.println("Flow between: " + parentNode + " and " + child + " is: " + flow[child]);
			currentFlow = Math.min(currentFlow, flow[child]);
			if(parentNode == s) break;
			child = parentNode;
			parentNode = parent[child];
		}
		
		return currentFlow;
	}
	
	private void adjustKapFlowRest(int child, int flow)
	{
		int parentNode = parent[child];
		int linkedListSize = graph[parentNode].size();
		for(int i = 0; i < linkedListSize; i++)
		{
			int[] tempArray = (int[]) graph[parentNode].get(i);
			if(tempArray[0] == child)
			{
				//System.out.println("A: Parent: " + parentNode + " Child: " + child);
				tempArray[2] += flow;
				tempArray[3] = (tempArray[1]-tempArray[2]);
				graph[parentNode].remove(i);
				graph[parentNode].add(tempArray);
				break;
			}
		}
		linkedListSize = graph[child].size();
		for(int i = 0; i < linkedListSize; i++)
		{
			int[] tempArray2 = (int[]) graph[child].get(i);
			if(tempArray2[0] == parentNode)
			{
				//System.out.println("B: Parent: " + parentNode + " Child: " + child);
				tempArray2[2] = tempArray2[2] - flow;
				tempArray2[3] = (tempArray2[1]-tempArray2[2]);
				graph[child].remove(i);
				graph[child].add(tempArray2);
				break;
			}
		}
		if(parentNode!=s)adjustKapFlowRest(parentNode, flow);
	}
	
	private void writeMaxFlowGraph() {
		int numberOfFlowE = 0;
		StringBuilder sb = new StringBuilder();
		io.println(v);
		io.println(s + " " + t + " " + totalFlow);
		
		int[] tempArray = new int[4];
		for(int i = 0; i < v+1;i++)
		{
			int linkedListSize = graph[i].size();
			for(int k = 0; k < linkedListSize; k++)
			{
				tempArray = (int[]) graph[i].get(k);
				if(tempArray[2]>0)
				{
					numberOfFlowE++;
					sb.append(i);
					sb.append(" ");
					sb.append(tempArray[0]);
					sb.append(" ");
					sb.append(tempArray[2]);
					sb.append("\n");
					//io.println(i + " " + tempArray[0] + " " + tempArray[2]);
				}
			}
		}
		io.println(numberOfFlowE);
		io.println(sb.toString());
	}
	
	public static void main(String[] args) {
		new BlackBox();

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
