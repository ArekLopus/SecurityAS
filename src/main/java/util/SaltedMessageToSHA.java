package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SaltedMessageToSHA {
	
	private MessageDigest alg = MessageDigest.getInstance("SHA-512");
	
	public SaltedMessageToSHA() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String message = "darek";
		encodeString(message, "UTF-8");

	}
	
	
	private void encodeString(String str, String charset) throws UnsupportedEncodingException {
		alg.reset();
		alg.update(str.getBytes(charset));
		String salt = "LetsHaveSomeSalt";
		alg.update(salt.getBytes(charset));
		byte[] hash = alg.digest();
		System.out.println("Hash after salt (HEX):  \t"+SaltedMessageToSHA.byteArrayToHex(hash));
	}
	
	
	public static String hashStringSalted(String str, String algName, String charset) {
		try {
			MessageDigest alg = MessageDigest.getInstance(algName);
			alg.reset();
			alg.update(str.getBytes(charset));
			String salt = "LetsHaveSomeSalt";
			alg.update(salt.getBytes(charset));
			byte[] hash = alg.digest();
			return SaltedMessageToSHA.byteArrayToHex(hash);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		new SaltedMessageToSHA();
	}
}