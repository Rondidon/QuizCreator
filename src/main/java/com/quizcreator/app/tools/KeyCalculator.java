package com.quizcreator.app.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.quizcreator.app.data.RegisteredPerson;
/**
 * Generates a keyfile.key file based on the forename and surname of the registered person who purchased EQC.
 * @author robin
 *
 */
public class KeyCalculator {
	
	private static String location;
			
	public KeyCalculator() {
		location = FolderFinder.getAppDataFolderLocation() + "/keyfile.key";
	}
	
	/**
	 * 	/**
	 * Writes the keyfile.key file onto the HDD based on the fore and surname of a RegisteredPerson.
	 * @param forename
	 * @param surname
	 */
	public void saveKeyFile(String forename, String surname) {
		RegisteredPerson person = new RegisteredPerson();
		person.setForename(forename);
		person.setSurname(surname);
		try {
			OutputStream os = new FileOutputStream(location);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(person);
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Location for FileOutputStream in KeyCalculator: saveKeyFile could not be found");
		} catch (IOException e) {
			System.out.println("ObjectOutputStream didnt work out in KeyCalculator: saveKeyFile");
		}
	}
	/**
	 * Reads the keyfile.key file on the HDD and returns a RegisteredPerson
	 * @return
	 */
	public RegisteredPerson openKeyFile() {
		RegisteredPerson person = new RegisteredPerson();
		FileInputStream fis;
		try {
			fis = new FileInputStream(location);
			ObjectInputStream ois = new ObjectInputStream(fis);
			person = (RegisteredPerson)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("Location for FileInputStream in KeyCalculator: openKeyFile could not be found. No key file?");
			return null;
		} catch (IOException e) {
			System.out.println("ObjectInputStream didnt work out in KeyCalculator: openKeyFile. Key file modified or broken?");
			return null;
		} catch (Exception e) {
			System.out.println("ois.readObject() probably didnt work out in KeyCalculator: openKeyFile. Key file broken.");
			return null;
		}
		return person;
	}
}
