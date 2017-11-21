package util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


public class MessageToSHA {
	
	private MessageDigest alg = MessageDigest.getInstance("SHA-512");
	
	public MessageToSHA() throws NoSuchAlgorithmException {
		
		String message = "arek";
		computeDigest(message.getBytes());

	}
	
	
	private void computeDigest(byte[] b) {
		alg.reset();
		alg.update(b);
		byte[] hash = alg.digest();
		System.out.println("Hash before encoding: \t"+Arrays.toString(hash));
		System.out.println("Hash before enc (HEX): \t"+MessageToSHA.byteArrayToHex(hash));
		String s1 = Base64.getEncoder().encodeToString(hash);
		byte[] ishash = Base64.getDecoder().decode(s1);
		System.out.println("Encoded String: \t"+s1);
		System.out.println("Hash after decoding: \t"+Arrays.toString(ishash));
		String d = "";
		for (int i=0; i<hash.length; i++) {
			int v = hash[i] & 0xFF;
			if (v < 16) d += "0";
			d += Integer.toString(v, 16).toUpperCase() + " ";
		}
		System.out.println("Hash after decoding: \t"+d);
	}
	
	
	public static String hashString(byte[] b, String al) {
		try {
			MessageDigest alg = MessageDigest.getInstance(al);
			alg.reset();
			alg.update(b);
			byte[] hash = alg.digest();
			return MessageToSHA.byteArrayToHex(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		new MessageToSHA();
	}
}