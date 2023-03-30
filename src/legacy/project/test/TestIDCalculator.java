package de.quizgamemaker.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.quizgamemaker.data.*;
import de.quizgamemaker.utils.IDCalculator;

public class TestIDCalculator {

	@Test
	public void testEmptyList() {
		IDCalculator.clearIdList();
		int id = IDCalculator.calculateId();
		assertEquals(0,id);
	}
	
	@Test
	public void testListTwoEntries() {
		IDCalculator.clearIdList();
		int id = IDCalculator.calculateId();
		id = IDCalculator.calculateId();
		assertEquals(1,id);
	}

	@Test
	public void testListWithSpaces() {
		IDCalculator.clearIdList();
		Project project = new Project("");
		AudioFile file = new AudioFile("");
		project.getAudioRessourceList().add(file);
		AudioFile file2 = new AudioFile("");
		project.getAudioRessourceList().add(file2);
		AudioFile file3 = new AudioFile("");
		project.getAudioRessourceList().add(file3);
		project.deleteFromAudioRessourceList(file2.getId());
		project.deleteFromAudioRessourceList(file.getId());
		int id = IDCalculator.calculateId();
		id = IDCalculator.calculateId();
		id = IDCalculator.calculateId();
		assertEquals(3,id);
	}
}
