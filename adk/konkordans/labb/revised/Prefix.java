public class Prefix {

	
	/*
	 * Converts a string s into a prefix consisting of the 3 first letters
	 * of s.
	*/
	public static String createPrefix(String s) {
		s = s.toLowerCase();
		if(s.length() < 3) {
			s = s.substring(0, s.length());
		} else {
			s = s.substring(0,3);
		}
		return s;
	}
	/* 
	 * Converts the 3-letter prefix to another base, i.e. a = 1, b = 2 etc
	 * abc -> a*29^2 + b*29 + c
	*/
	public static int hashPrefix(String s) {
		char[] prefix = s.toCharArray();
		int result = 0;
		int base = 0;

		for(int i = 0; i < prefix.length; i++) {
			if(prefix[i] == ('å')) base = 27;
			else if(prefix[i] == ('ä')) base = 28;
			else if(prefix[i] == ('ö')) base = 29;
			else {
				base = prefix[i] - 'a' + 1;
			}
			result += base*Math.pow(29, ((prefix.length - 1) - i));
		}
		return result;
	}
}