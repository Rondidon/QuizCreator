package com.quizcreator.app.test;

import com.quizcreator.app.tools.ZipUtilities;

public class TestFolderToZIP {

	public static void main(String[] args) {
		ZipUtilities.folderToZIP("/home/robin/++test++", "/home/robin/++test++/test.qgm", "/home/robin/++test++", true);
	}

}
