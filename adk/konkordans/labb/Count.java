import java.io.*;

public class Count 
{
	private static RandomAccessFile index;
	private static RandomAccessFile korpus;
	private static DataInputStream in;
	private int count;
	private String line;
	private String[] split;
	private String s;
	private long[] position;
	public static void main(String[] args) throws IOException {
		
		if(args.length == 0) {
			System.out.println("Correct use: 'java Count yourWord'.");
			System.exit(1);
		}
		Count s = new Count(args);
	}
	public Count(String[] args) throws IOException{

		try {
			System.out.print("Loading prefix...");

			index = new RandomAccessFile("index", "r");
			korpus = new RandomAccessFile("korpus", "r");
			in = new DataInputStream(new BufferedInputStream(new FileInputStream("prefix")));
			
			long l;
			int i;
			int size = (int)((Math.pow(30, 2) + 30 + 1) * 29);
			position = new long[size];
			while(true) {
				i = in.readInt();
				l = in.readLong();
			
				position[i] = l;
			}
			
		} catch(EOFException e) {
			in.close();
			System.out.println(" Done!");
						
			count(args[0]);
		} 
	}
	private void count(String word) {
		try {
			long pre = System.currentTimeMillis();
			word = word.toLowerCase();
			int prefix = Prefix.hashPrefix(Prefix.createPrefix(word));
			System.out.println("Searching for " + word + ", starting with hashed prefix: " + prefix);
			long pos = position[prefix];
			if(pos == 0) {
				System.out.println("Couldn't find the word.");
				System.exit(0);
			}
			int i = 0;
			long nextpos = position[prefix + 1];
			
			while(nextpos == 0) {
				i++;
				nextpos = position[prefix + i];
			}
			long btotal = 0;
			System.out.println("Pos: " + pos + ", nextpos: " + nextpos);
			while((nextpos - pos) > 1000) {
				System.out.println("Gör binärsökning.");
				long preb = System.currentTimeMillis();
				
				long mid = (pos + nextpos) / 2;
				index.seek(mid);
				index.readLine();
				line = index.readLine();
				split = line.split(" ");
				s = split[0];
				// w >= s
				if(word.compareTo(s) > 0) {
					pos = mid;
				} else if(word.compareTo(s) < 0){
					nextpos = mid;
				} else break;
				long postb = System.currentTimeMillis();
				btotal = (postb - preb);
			}
			System.out.println("Binärsökningen tog " + (btotal) + " ms.");
			
			System.out.println("Börjar linjärsökning.");
			long prel = System.currentTimeMillis();
			index.seek(pos);
			while(true) {
				line = index.readLine();
				split = line.split(" ");
				s = split[0].toLowerCase();
				if(s.equals(word)) {
					count++;
				}
				// s > w
				if(word.compareToIgnoreCase(s) < 0) {
					System.out.println(count + " förekomster av ordet " + word);
					break;
				}	
			}
			long postl = System.currentTimeMillis();
			System.out.println("Linjärsökning tog " + (postl - prel) + " milliseconds.");
			
			long post = System.currentTimeMillis();
			System.out.println("..in " + (post - pre) + " milliseconds.");
			
			
		} catch(IOException e) {
			System.out.println("fel: " + e);
		}
	}
	public static String[] makeSplit(String line) {
		String[] words = line.split(" ");
		return words;
		
	}
}