import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

public class IndexBinary 
{
	
		RandomAccessFile index = null;
		DataOutputStream ipos = null;
		DataOutputStream iword = null;

		try {

			index = new DataInputStream(new BufferedInputStream(new FileInputStream("index")));
			ipos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("IPos")));
			iword = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("IPos")));
			
			while((s = in.readLine()) != null) {
				split = s.split(" ");
				s = split[0];
				
				
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