import java.io.*;

public class Generator {
	
	public static void main(String[] args) throws IOException{
		IndexBinary.generateBinaryIndex();
		GeneratePrefixFile.generatePrefixFile();
	}
}