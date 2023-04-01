package com.quizcreator.app.tools;

import java.io.File;

public class FolderTools {

	public String getAppDataFolderLocation() {
		String folderName = "EasyQuizCreator";
		String os = System.getProperty("os.name");
		File f;
		String result;
		//Windows
		if (os.startsWith("Windows")) {
			result = System.getProperty("user.home") + "/" + folderName;
		}
		//Others like MacOS / Linux / Unix
		else {
			result = System.getProperty("user.home") + "/." + folderName;
		}
		f = new File(result);
		f.mkdirs();
		return result;
	}

	public String getTempFolderLocation() {
		return getAppDataFolderLocation().concat("/temp");
	}

	public boolean isLocationPresent(String location) {
		File f = new File(location);
		return f.exists();
	}

	public boolean eraseTempFolder() {
		return eraseFolderRecursive(getTempFolderLocation());
	}

	private boolean eraseFolderRecursive(String location) {
		File[] files = new File(location).listFiles();
		if(files == null) {
			return false;
		}
		for (final File f: files) {
			if(f.isFile()) {
				if (!f.delete()) {
					return false;
				}
			} else if (f.isDirectory()) {
				eraseFolderRecursive(f.getPath());
				if (!f.delete()) {
					return false;
				}
			}
		}
		return true;
	}
}
