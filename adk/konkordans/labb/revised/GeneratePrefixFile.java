import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

public class GeneratePrefixFile 
{
	public static void generatePrefixFile() throws IOException{
		
		RandomAccessFile words = null;
		DataOutputStream prefixes = null;
		
		try {
			words = new RandomAccessFile("words", "rw");
		} catch(FileNotFoundException e) {
			System.err.println("File not found.");
		}
		try {
			String word;
			String[] split;
			String previous = "";
			long pos = words.getFilePointer();
			int hashedPrefix;
			
			prefixes = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("prefixes")));
			
			while((word = words.readLine()) != null) {
				split = word.split(" ");
				word = split[0];
				
				// Create a prefix consisting of 3 letters or less
				word = Prefix.createPrefix(word);
				
				// Is it a new word?
				if(!(word.equals(previous))) {
					
					// Save the previous word
					previous = word;
					
					// Create a hashed prefix
					hashedPrefix = Prefix.hashPrefix(word);
					
					// write the hashed prefix to prefixes
					prefixes.writeInt(hashedPrefix);
						
					// get the current position in the wordsfile
					pos = words.getFilePointer();
						
					// write the position to prefixes
					prefixes.writeLong(pos);
		
				}
			} 

		} finally {
			if (words != null) {
				words.close();				
			}
			if (prefixes != null) {
				prefixes.close();
			}
		}
	}

}