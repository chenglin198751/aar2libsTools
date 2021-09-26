package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {

	public static String zipUncompress(String inputFile) {
		String destDirPath = null;
		try {
			File srcFile = new File(inputFile);
			if (!srcFile.exists()) {
				System.out.println(srcFile.getAbsolutePath() + " not exist");
				return null;
			}

			final String aar = ".aar";
			if (inputFile.toLowerCase().endsWith(aar)) {
				destDirPath = inputFile.substring(0, inputFile.length() - aar.length());
			}

			File temp = new File(destDirPath);
			if (temp.exists()) {
				FileUtils.deleteDirectory(temp);
			}

			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					srcFile.mkdirs();
				} else {
					File targetFile = new File(destDirPath + File.separator + entry.getName());

					if (!targetFile.getParentFile().exists()) {
						targetFile.getParentFile().mkdirs();
					}
					targetFile.createNewFile();

					InputStream is = zipFile.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len;
					byte[] buf = new byte[1024];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}

					fos.close();
					is.close();
				}
			}
			zipFile.close();
		} catch (Exception e) {
			System.out.println("zipUncompress error = " + e.toString());
		}
		return destDirPath;
	}

}
