package com.quizcreator.app.test;

import java.io.File;

import com.quizcreator.app.tools.ZipUtilities;

public class TestZIPToFolder {
	public static void main(String[] args) {
		ZipUtilities.zipToFolder(new File("/home/robin/Schreibtisch/TestProjekt.qgm"), new File("/home/robin/Schreibtisch/output"));
	}
}
