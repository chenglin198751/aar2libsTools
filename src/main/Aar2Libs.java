package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Aar2Libs {
	private static List<String> aars = new ArrayList<>();

	public static void main(String[] args) {
		args = new String[] { "E:/aaa/" };

		if (args == null || args.length <= 0) {
			System.out.println("please input aar directory path !");
			return;
		}

		String dir = args[0];
		System.out.println("aar directory is " + dir);

		File aarDirs = new File(dir);
		if (!aarDirs.exists()) {
			System.out.println("aar parent path not exists");
			return;
		}

		if (!aarDirs.isDirectory()) {
			System.out.println("please input aars directory path !");
			return;
		}

		File[] files = aarDirs.listFiles();
		if (files.length <= 0) {
			System.out.println("aars directory is empty !");
			return;
		}

		aars.clear();
		for (int i = 0; i < files.length; i++) {
			File aarFile = files[i];
			if (aarFile.getName().toLowerCase().endsWith(".aar")) {
				String path = ZipUtils.zipUncompress(aarFile.getAbsolutePath());
				if (path != null) {
					aars.add(path);
				}
			}
		}

		final String result = dir + File.separator + "result" + File.separator;
		File temp = new File(result);
		if (temp.exists()) {
			FileUtils.deleteDirectory(temp);
		}

		// 1、处理assets文件夹
		FileUtils.mergeAssets(aars, result + "assets");

		// 2、处理libs,jni文件夹
		FileUtils.mergeJni(aars, result + "libs");
	}

}
