package main;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class FileUtils {

	public static void mergeAssets(List<String> aars, String target) {
		for (int i = 0; i < aars.size(); i++) {
			File asstes = new File(aars.get(i) + File.separator + "assets");
			if (asstes.exists() && asstes.isDirectory()) {
				copyDirectory(asstes, new File(target));
			}
		}
	}

	public static void mergeJni(List<String> aars, String target) {
		for (int i = 0; i < aars.size(); i++) {
			// 1、复制jni下的so
			File jni = new File(aars.get(i) + File.separator + "jni");
			if (jni.exists() && jni.isDirectory()) {
				copyDirectory(jni, new File(target));
			}

			// 2、复制classes.jar并重命名
			File dir = new File(aars.get(i));
			if (dir.exists() && dir.isDirectory()) {
				for (File jarFile : dir.listFiles()) {
					if (jarFile.getName().toLowerCase().endsWith(".jar")) {
						File renameFile = new File(target + File.separator + dir.getName() + ".jar");
						copy(jarFile, renameFile);
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------

	public static void deleteDirectory(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteDirectory(files[i]);
				}
			}
			file.delete();
		}
	}

	public static void copyDirectory(File fromDir, File toDir) {
		try {
			if (!fromDir.isDirectory()) {
				return;
			}

			if (!toDir.exists()) {
				toDir.mkdirs();
			}

			File[] files = fromDir.listFiles();
			for (File file : files) {
				String strFrom = fromDir + File.separator + file.getName();
				String strTo = toDir + File.separator + file.getName();
				if (file.isDirectory()) {
					copyDirectory(new File(strFrom), new File(strTo));
				}
				if (file.isFile()) {
					copy(new File(strFrom), new File(strTo));
				}
			}
		} catch (Exception e) {
			System.out.println("copy Directory error:" + e.toString());
		}

	}

	public static void copy(File source, File dest) {
		try {
			if (dest.exists()) {
				dest.delete();
			}
			Files.copy(source.toPath(), dest.toPath());
		} catch (Exception e) {
			System.out.println("copy file error:" + e.toString());
		}

	}
}
