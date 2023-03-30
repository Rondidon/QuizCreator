package de.quizgamemaker.test;

import java.io.File;

import de.quizgamemaker.utils.ZipUtilities;

public class TestZIPToFolder {
	public static void main(String[] args) {
		ZipUtilities.zipToFolder(new File("/home/robin/Schreibtisch/TestProjekt.qgm"), new File("/home/robin/Schreibtisch/output"));
	}
}
