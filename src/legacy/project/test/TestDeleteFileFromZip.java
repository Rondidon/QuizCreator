package de.quizgamemaker.test;

import de.quizgamemaker.utils.ZipUtilities;

public class TestDeleteFileFromZip {
	public static void main(String[] args) {
		ZipUtilities.deleteFileFromZIP("/home/robin/++test++/test.qgm", "/test/test.mp3");
	}
}
