package com.quizcreator.app.test;

import com.quizcreator.app.data.RegisteredPerson;
import com.quizcreator.app.tools.KeyCalculator;

public class TestKeyCalculator {
	public static void main(String[] args) {
		KeyCalculator kc = new KeyCalculator();
		kc.saveKeyFile("Robin","Kindler");
		RegisteredPerson person = kc.openKeyFile();
		System.out.println(person.getForename() + " " + person.getSurname());
	}
}
