package org.obfuscate4e.tests.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzipper {

	private static final byte[] buffer = new byte[0xFFFF];

	public static void unzip(ZipFile zipFile, String location) {
		try {
			Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();

			while (zipEntryEnum.hasMoreElements()) {
				ZipEntry zipEntry = zipEntryEnum.nextElement();
				extractEntry(zipFile, zipEntry, location);
			}
		} catch (Exception e) {
			System.out.println("error while unzipping");
		}
	}

	private static void extractEntry(ZipFile zf, ZipEntry entry, String destDir)
			throws IOException {
		File file = new File(destDir, entry.getName());

		if (entry.isDirectory())
			file.mkdirs();
		else {
			new File(file.getParent()).mkdirs();

			InputStream is = null;
			OutputStream os = null;

			try {
				is = zf.getInputStream(entry);
				os = new FileOutputStream(file);

				for (int len; (len = is.read(buffer)) != -1;)
					os.write(buffer, 0, len);
			} finally {
				if (os != null)
					os.close();
				if (is != null)
					is.close();
			}
		}
	}
}
