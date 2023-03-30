package com.quizcreator.data;

import java.io.Serializable;

public class RegisteredPerson implements Serializable {

	private static final long serialVersionUID = -7390197034662213645L;
	private String forename;
	private String surname;
	
	public RegisteredPerson() {
		setForename("");
		setSurname("");
	}
	
	public RegisteredPerson(String forename, String surname) {
		setForename(forename);
		setSurname(surname);
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getForename() {
		return forename;
	}
	public void setForename(String forename) {
		this.forename = forename;
	}
	@Override
	public String toString() {
		return getSurname() + ", " + getForename();
	}
}
