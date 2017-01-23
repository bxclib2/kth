import java.io.*;
import java.nio.*;
import java.util.Scanner;

public class Konkordans 
{
	private static long[] position;
	private static RandomAccessFile words;
	private static RandomAccessFile korpus;
	private static DataInputStream in;
	private static RandomAccessFile positions;
	private int count;
	private String line;
	private String[] split;
	private String s;
	public static void main(String[] args) throws IOException {
				
		if(args.length == 0) {
			System.out.println("Correct use: 'java Konkordans yourWord'.");
			System.exit(1);
		}
		Konkordans k = new Konkordans(args);
	}
	public Konkordans(String[] args) throws IOException{

		try {
	
			System.out.print("Loading prefix...");

			words = new RandomAccessFile("words", "r");
			positions = new RandomAccessFile("positions", "rw");
			korpus = new RandomAccessFile("korpus", "r");
			in = new DataInputStream(new BufferedInputStream(new FileInputStream("prefixes")));
			
			long l;
			int i;
			int size = 29*(int)((Math.pow(29, 2) + 29 + 1));
			position = new long[size];
			while(true) {
				// i is the hashed prefix word
				i = in.readInt();
				// l is the position in the words-file
				l = in.readLong();	
				// add the position at the hashed index of the word
				position[i] = l;
			}
			
		} catch(EOFException e) {
			in.close();
			System.out.println(" Done!");
						
			seek(args[0]);
		} 
	}
	private void seek(String word) {
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
				// to make sure we do not reach the end of posision
				if((prefix + i) < position.length) {
					nextpos = 0;
					break;
				}
			}
			
			while((nextpos - pos) > 1000) {
				long mid = (pos + nextpos) / 2;
				words.seek(mid);
				words.readLine();
				line = words.readLine();
				split = line.split(" ");
				s = split[0];
				// w > s
				if(word.compareToIgnoreCase(s) > 0) {
					pos = mid;
				// w < s
				} else if(word.compareToIgnoreCase(s) < 0){
					nextpos = mid;
				} else break;
			}
			// Go to the position (and back a bit so we don't miss the word)
			if(pos > 1000) {
				words.seek(pos - 100);
				words.readLine();

				
			} else { // We're in the beginning of the file and don't want a negative offset
				words.seek(0);
			}
			long firstpos;
			long lastpos;
			while((line = words.readLine()) != null) {

				split = line.split(" ");
				s = split[0].toLowerCase();
				firstpos = Long.parseLong(split[1]);
				if(s.equals(word)) {
					String nextWord = words.readLine();
					
					// are we at the end of the word-index?
					if(nextWord == null) {
						lastpos = positions.length();
					} else {
						String[] split2 = nextWord.split(" ");
						lastpos = Long.parseLong(split2[1]);
					}
					
					long diff = (lastpos-firstpos) / 8;
					
					long post = System.currentTimeMillis();
					System.out.println("Found " + diff + " hits in.. " + (post - pre) + " ms.");
					
					if(diff > 30) {
						System.out.println("Do you want to print them all? y/n");
						int in = System.in.read();
						if(in == 'j' || in == 'y') {
							printResults(firstpos, diff, System.out);
						} else {
							System.out.println("Ok. Bye!");
						}
					} else {
						printResults(firstpos, diff, System.out);
					}	
				}
				// s > w
				if(word.compareToIgnoreCase(s) < 0) {
					break;
				}	
			}
			
			
			
		} catch(IOException e) {
			System.out.println("fel: " + e);
		}
	}
	
	private void printResults(long firstpos, long positionsToRead, OutputStream out) throws IOException{
		
		FileOutputStream fdout = new FileOutputStream(FileDescriptor.out);
	    BufferedOutputStream bos = new BufferedOutputStream(fdout, 65536); // pretty big buffer, we're possibly printing loads
	    PrintStream ps = new PrintStream(bos, false); // auto-flush on \n off
	    System.setOut(ps);
	
		byte[] bytearray = new byte[(int)positionsToRead*60];
		positions.seek(firstpos);
		byte[] b = new byte[60];
		long korpPos;
		for(long l = 0; l < positionsToRead; l++) {
		 	b = new byte[60];
			korpPos = positions.readLong();
			if(korpPos < 30) {
				korpus.seek(korpPos);
			} else {
				korpus.seek(korpPos - 30);
			}
			korpus.read(b);
			out.write(removeNewline(b));
			out.write(10);
		}
    }
 	private byte[] removeNewline(byte[] b)
    {
        for (int i = 0; i < b.length; i++) {
            if (b[i] == '\n') {
                b[i] = ' ';
			}	
		}

        return b;
    }
}