package org.obfuscate4e.tests.testcase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.obfuscate4e.util.ChecksumHelper;

import junit.framework.TestCase;

public class ChecksumHelperTest extends TestCase {

	public void testInputStreamToString() {

		// the usual case
		
		String testString = "FNORD";
		InputStream testInputStream = new ByteArrayInputStream(testString
				.getBytes());
		try {
			assertEquals(testString, ChecksumHelper
					.inputStreamToString(testInputStream));
		} catch (IOException e) {
		}

		// the empty String case
		
		testString = "";
		testInputStream = new ByteArrayInputStream(testString.getBytes());
		try {
			assertEquals(testString, ChecksumHelper
					.inputStreamToString(testInputStream));
		} catch (IOException e) {
		}
		
		// null parameter case
		
		try {
			ChecksumHelper.inputStreamToString(null);
			fail("did not throw exception");
		} catch (Exception e) {
		}

	}

	public void testByteArrayToHexString() {
		
		// usual case
		
		byte[] testByteArray = new byte[256];
		
		for ( int i = 0; i < 256; i++){
			testByteArray[i] = (byte) i;
		}
		
		String expected = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF";
		assertEquals(expected,ChecksumHelper.byteArrayToHexString(testByteArray));
		
		// empty array case
		
		byte[] emptyByteArray = {};

		assertEquals("",ChecksumHelper.byteArrayToHexString(emptyByteArray));

		// null parameter case
		
		try {
			ChecksumHelper.byteArrayToHexString(null);
			fail("did not throw exception");
		} catch (Exception e) {
		}
		
	}

	public void testGetMd5Checksum() {
		
		// the usual case
		
		String stringToBeHashed = "Lorem Ipsum";
		String expectedChecksum = "6DBD01B4309DE2C22B027EB35A3CE18B";
		String actualChecksum = ChecksumHelper.getMd5Checksum(stringToBeHashed);
		
		assertEquals(expectedChecksum, actualChecksum);
		
		// empty String case
		
		stringToBeHashed = "";
		expectedChecksum = "D41D8CD98F00B204E9800998ECF8427E";
		actualChecksum = ChecksumHelper.getMd5Checksum(stringToBeHashed);

		assertEquals(expectedChecksum, actualChecksum);

		// null parameter case
		
		try {
			ChecksumHelper.getMd5Checksum(null);
			fail("did not throw exception");
		} catch (Exception e) {
		}
				
	}

}
