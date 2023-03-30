package com.quizcreator.tools;

import java.io.File;

public class FolderTools {
	
	/**
	 * Cleans up the work temp folder
	 */
	public static void eraseWorkFolder() {
		eraseFolder(FolderFinder.getWorkFolderLocation());
	}
	
	/**
	 * Erases a folder and all of its content including subfolders
	 * @param location
	 */
	public static void eraseFolder(String location) {
		File[] files = new File(location).listFiles();
		if(files!=null) {
			for(File f: files) {
			    if(f.isFile()) {
			    	f.delete();
			    }
			    if(f.isDirectory()) {
			    	eraseFolder(f.getPath());
			    	f.delete();
			    }
			}
		}
	}
	
	public static boolean isAvailable(String location) {
		File f = new File(location);
		if(f.exists()) {
			return true;
		}
		else {
			return false;
		}
	}
}
