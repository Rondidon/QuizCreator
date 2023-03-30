package de.quizgamemaker.test;

import de.quizgamemaker.data.RegisteredPerson;
import de.quizgamemaker.utils.KeyCalculator;

public class TestKeyCalculator {
	public static void main(String[] args) {
		KeyCalculator kc = new KeyCalculator();
		kc.saveKeyFile("Robin","Kindler");
		RegisteredPerson person = kc.openKeyFile();
		System.out.println(person.getForename() + " " + person.getSurname());
	}
}
