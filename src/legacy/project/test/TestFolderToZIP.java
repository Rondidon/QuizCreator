package de.quizgamemaker.test;

import de.quizgamemaker.utils.ZipUtilities;

public class TestFolderToZIP {

	public static void main(String[] args) {
		ZipUtilities.folderToZIP("/home/robin/++test++", "/home/robin/++test++/test.qgm", "/home/robin/++test++", true);
	}

}
