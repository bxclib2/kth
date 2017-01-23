import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class MaxFlow {

	ArrayList<ArrayList<Edge>> al;
	ArrayList<Integer> path;
	Kattio io;
	int v, s, d, e;
	int maxFlow;
	
	public MaxFlow(){
		io = new Kattio(System.in, System.out);
		
		readGraph();
		
		edmondKarp();
		
		writeSolution();
	}
	
	public void readGraph(){
		v = io.getInt();
		s = io.getInt();
		d = io.getInt();
		e = io.getInt();
		
		
		al = new ArrayList<ArrayList<Edge>>(v);
		
		if(s == d) {
			System.err.println("Felaktig källa/sänka!");
			System.exit(1);
		}

		for(int i = 0; i <= v; i++){
			al.add(null);
		}

		int a, b, cap;

		//initializes the grannylist
		for(int i = 0; i < e; i++){
			a = io.getInt(); 
			b = io.getInt();
			cap = io.getInt(); //throws the capacity in the bin
			addEdge(a, b, cap);
			addEdge(b, a, 0);
			//al.get(a).get(al.get(a).size()-1).inverse = al.get(b).get(al.get(b).size()-1);			
			//al.get(b).get(al.get(b).size()-1).inverse = al.get(a).get(al.get(a).size()-1);
		}
	}
	
	public void edmondKarp(){

		maxFlow = 0;
		int min, child, parent;
		ArrayList<Edge> list;
		while(true){
			min = bFS();
			if(min == 0)
				break;
			
			maxFlow += min;
			
			child = d;
			
			while(child != s){
				parent = path.get(child);
				list = al.get(parent);
				for(int i = 0; i < list.size(); i++){
					if(list.get(i).neighbour == child){
						list.get(i).flow += min;
						break;
					}
				}
				list = al.get(child);
				for(int i = 0; i < list.size(); i++){
					if(list.get(i).neighbour == parent){
						list.get(i).flow -= min;
						break;
					}
				}
				child = parent;
			}
		}
	}
	
	public int bFS(){
		path = new ArrayList<Integer>(v);
		ArrayList<Integer> mp = new ArrayList<Integer>(v);
		LinkedList<Integer> que = new LinkedList<Integer>();
		ArrayList<Edge> neighbours;
		Edge edge;
		int curr;
		
		for(int i = 0; i <= v; i++){
			path.add(-1);
			mp.add(0);
		}
		
		path.set(s,-2);
		mp.set(s, Integer.MAX_VALUE);
		que.add(s);
		while(que.size() > 0){
			curr = que.getFirst();
			
			neighbours = al.get(curr);
			if(neighbours == null)
				return 0;
			for(int i = 0; i < neighbours.size(); i++){
				edge = neighbours.get(i);
				if(edge.cap - edge.flow > 0 && path.get(edge.neighbour) == -1){
					path.set(edge.neighbour, curr);
					mp.set(edge.neighbour, Math.min(mp.get(curr), edge.cap - edge.flow));
					if(edge.neighbour != d){
						que.add(edge.neighbour);
					}
					else
						return mp.get(d);
				}
			}
		}
		return 0;
	}
	
public void writeSolution(){
		
		ArrayList<Edge> matches = new ArrayList<Edge>();
		ArrayList<Edge> list = new ArrayList<Edge>();
		Edge edge;
		
		for(int i = 0; i < al.size(); i++){
			list = al.get(i);
			if(list != null){
				edge = null;
				for(int j = 0; j < list.size(); j++){
					if(list.get(j).flow > 0){
						edge = new Edge(i, list.get(j).neighbour, list.get(j).flow, null);
					}
					if(edge != null){
						matches.add(edge);
						edge = null;
					}
				}
			}
		}
		
		/*int maxFlow = 0;
		list = al.get(s);
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getFlow() > 0)
					maxFlow += list.get(i).getFlow();
			}
		}*/		

		io.println(v);
		io.println(s + " " + d + " " + maxFlow);
		io.println(matches.size());

		for(int i = 0; i < matches.size(); i++){
			edge = matches.get(i);
			io.println(edge.neighbour + " " + edge.cap + " " + edge.flow);
		}

		io.flush();
	}
	

	public Edge addEdge(int a, int b, int cap){
		Edge tmp = new Edge(b, cap, 0, null);
		ArrayList<Edge> list = al.get(a);
		boolean found = false;
		if(list == null){
			list = new ArrayList<Edge>();
			list.add(tmp);
			al.set(a, list);
		}
		else{
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).neighbour == b){
					list.get(i).cap += cap;
					found = true;
					break;
				}
			}
			if(!found){
				list.add(tmp);
			}
		}
		return tmp;
	}	
	
	
	public static void main(String[] args) {
		new MaxFlow();

	}

}

class Edge{
	
	int neighbour;
	int cap;
	int flow;
	Edge inverse;
	
	public Edge(int neighbour, int cap, int flow, Edge inverse){
		this.neighbour = neighbour;
		this.cap = cap;
		this.flow = flow;
		this.inverse = inverse;
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

class LinkedList<T> implements Iterable<T>{

	private ListNode<T> first;
	private ListNode<T> last;
	private int size;
	
	public LinkedList() {
		reset();
	}
	
	public T getFirst(){
		T fsd = first.getData();
		removeFirst();
		return fsd;
	}
	
	public T removeFirst(){
		if(isEmpty()) return null;
		
		ListNode<T> tmp = first;
		first = tmp.getNext();	
		if(last == tmp) last = null;
		size--;
		return tmp.getData();
	}

 

	public void add(T data){
		ListNode<T> n = new ListNode<T>(data);
		if(isEmpty()){
			first = n;
		} else {
			last.setNext(n);
		}
		last = n;
		size++;
	}
	public void addLast(T data){
		add(data);
	}
	
	public boolean isEmpty(){
		return first == null? true:false;
	}
	
	public void reset(){
		first = null;
		last = null;
		size = 0;
	}
	
	public int size(){
		return size;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>(){

			private ListNode<T> next = first;
			
			public boolean hasNext() {
				return next == null? false:true;
			}

			public T next() {
				ListNode<T> tmp = next;
				next = next.getNext();
				return tmp.getData();
			}

			public void remove() {
				throw new UnsupportedOperationException();
				
			}
			
		};
	}
}


class ListNode<T>{

private T data;
private ListNode<T> next;

public ListNode(T data, ListNode<T> next) {
		this.data = data;
		this.next = next;
	}

	public ListNode(T data){
		this(data,null);
	}

	public T getData(){
		return data;
	}
	public ListNode<T> getNext(){
		return next;
	}
	
	public void setNext(ListNode<T> next){
		this.next = next;
	}
}