package util;

import java.security.NoSuchAlgorithmException;

public class ParseRoles {
	
	public ParseRoles() throws NoSuchAlgorithmException {
		
		//String message = "Administrator Developer";
		//String message = "Administrator, Developer";
		String message = "   Administrator,";
		parse(message);

	}
	
	
	public static String[] parse(String str) {
		if(str != null) {
			String[] split = str.trim().split("\\s*(=>|,|\\s)\\s*");
//			System.out.println(split.length);
//			for(String s : split) {
//				System.out.println("|"+s+"|");
//			}
			return split;
		}
		return new String[0];
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		new ParseRoles();
	}
}