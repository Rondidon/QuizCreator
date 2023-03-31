package com.quizcreator.app.tools;

import java.io.File;

public class FolderFinder {
	public static String getAppDataFolderLocation() {
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
	
	public static String getWorkFolderLocation() {
		return getAppDataFolderLocation().concat("/temp");
	}
}
