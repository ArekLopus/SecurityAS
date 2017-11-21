package util;


import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class MessageToBase64 {
	
	public MessageToBase64() throws NoSuchAlgorithmException {
		
		String message = "arek:arek";
		System.out.println("String to Encode: \t"+message);
		computeDigest(message.getBytes());

	}
	
	
	private void computeDigest(byte[] bytes) {
		
		String encoding = Base64.getEncoder().encodeToString(bytes);
		System.out.println("Encoded String: \t"+encoding);
		byte[] bb = Base64.getDecoder().decode(encoding);
		System.out.print("Decoded String: \t");
		for(byte b: bb) {
			System.out.print((char)b);
		}
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		new MessageToBase64();
	}
}