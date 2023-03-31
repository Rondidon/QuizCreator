package com.quizcreator.app.data;

import java.io.File;
import java.util.UUID;

public class ImageFile {
	
	private UUID id;
	private String location;
	private String description;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 * @param srcLocation : Relative location on hard disk
	 */
	public ImageFile(String srcLocation) {
		this.id = UUID.randomUUID();
		setLocation(srcLocation);
		if(Program.DEBUG) System.out.println("New image file registered: Location: " + getLocation() + ", id: " + getId());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id the uuid
	 * @param srcLocation the relative location on hard disc
	 */
	public ImageFile(final String srcLocation, final UUID id) {
		setId(id);
		setLocation(srcLocation);
		if(Program.DEBUG) System.out.println("New image file registered: Location: " + getLocation() + ", id: " + getId());
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(final UUID id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isAvailable() {
		File f = new File(location);
		if(f.exists()) {
			return true;
		}
		else {
			return false;
		}
	}
}
