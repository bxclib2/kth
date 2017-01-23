import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class IndexBinary 
{
	
	private static BufferedReader index = null;
	private static BufferedWriter words = null;
	//private static FileChannel positions = null;	
	private static DataOutputStream positions = null;
	private static FileOutputStream fis = null;
	private static ByteBuffer b = null;
	private static long pos;
	private static long posOfPreviousWord;
	private static String[] split;
	private static String word;
	private static String previous;

	
	public static void generateBinaryIndex() throws IOException{
		try {

			index = new BufferedReader(new InputStreamReader(new FileInputStream("index")));
			words = new BufferedWriter(new FileWriter("words"));
			positions = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("positions")));
            	
			previous = "";
			posOfPreviousWord = 0;
			// Read from the old index
			
			while((word = index.readLine()) != null) {
				
				split = word.split(" ");
				
				// the word
				word = split[0];
				
				// the position in the korpus
				pos = Long.parseLong(split[1]);
				
				// Is it the same word?
				if(word.equals(previous)) {
							
					positions.writeLong(pos);
					posOfPreviousWord += 8;
					
				} else {
					words.write(word + " " + posOfPreviousWord + "\n");
					// remember this word
					previous = word;
					
					positions.writeLong(pos);
					posOfPreviousWord += 8;
					
					
				}

			} 

		} finally {
			if (index != null) {
				index.close();
			}
			if (words != null) {
				words.write(previous + " " + posOfPreviousWord);
				words.close();
			}
			if (positions != null) {
				positions.close();
			}
		}
	}
}
