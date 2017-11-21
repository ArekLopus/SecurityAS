package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Test {

	public Test() {
	}

	
	
	
	public static void main(String[] args) {

		try {
			//String webPage = "http://localhost:8080/Security/";
			//String webPage = "https://localhost:8443/Security/basicAuth/index.xhtml";
			String webPage = "http://localhost:8080/Security/basicAuth/index.xhtml";
			String authStringEnc = "YXJlazphcmVr";
			
			
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();

			System.out.println("*** BEGIN ***");
			System.out.println(result);
			System.out.println("*** END ***");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
