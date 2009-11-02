package org.obfuscate4e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumHelper {

	public static final String NL = System.getProperty("line.separator");

	/**
	 * helper method to convert an InputStream from a text file to a String type
	 * 
	 * @param in
	 *            the InputStream
	 * @return the InputStream's content as a String
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		String string = "";
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			string += line + NL;
		}

		if (string.lastIndexOf(NL) > 0) {
			string = string.substring(0, string.lastIndexOf(NL));
		}

		bufferedReader.close();
		return string;
	}

	/**
	 * transforms a byte array to a hexadecimal number as a String
	 * 
	 * @param array the byte Array
	 * @return the hexadecimal number as a String
	 */
	public static String byteArrayToHexString(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
					.toUpperCase().substring(1, 3));
		}
		return sb.toString();
	}

	/**
	 * creates a hexadecimal md5 checksum as a String
	 * 
	 * @param message the String to hash
	 * @return the hexadecimal md5 checksum as a String
	 */
	public static String getMd5Checksum(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(message.getBytes("CP1252")));
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

}
