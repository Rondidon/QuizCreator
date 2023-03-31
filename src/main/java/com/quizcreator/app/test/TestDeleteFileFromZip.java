package com.quizcreator.app.test;

import com.quizcreator.app.tools.ZipUtilities;

public class TestDeleteFileFromZip {
	public static void main(String[] args) {
		ZipUtilities.deleteFileFromZIP("/home/robin/++test++/test.qgm", "/test/test.mp3");
	}
}
