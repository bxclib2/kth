import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

public class Konkordans 
{
	
	public static void main(String[] args) throws IOException {

		RandomAccessFile in = null;
		DataOutputStream out = null;
		
		try {
			//in = new BufferedReader(new FileReader("indexfil.txt"));
			in = new RandomAccessFile("index", "r");
			//out = new BufferedWriter(new FileWriter("prefix.txt"));
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("prefixBinary")));

			String s;
			String p;
			String tmp = "";
			String[] split;
			int hashedPrefix;
			long pos = 0;
			int i = 0;
			
			while((s = in.readLine()) != null) {
				split = s.split(" ");
				s = split[0];
				
				s = Prefix.createPrefix(s);
				
				if(!(s.equals(tmp))) {

					tmp = s;
					hashedPrefix = Prefix.hashPrefix(s);
					
					out.writeInt(hashedPrefix);
						
					pos = in.getFilePointer();
						
					out.writeLong(pos);
		
				}
			} 

		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

}