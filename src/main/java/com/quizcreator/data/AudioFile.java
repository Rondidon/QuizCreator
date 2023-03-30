package com.quizcreator.data;

import java.io.File;
import java.util.UUID;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioFile {
	private UUID id;
	private String location;
	private String description = "";
	private AudioFilePlaybackMode playbackMode = AudioFilePlaybackMode.ONCE;
	private double volume = 100.0;
	private Media media;
	private MediaPlayer mplayer;
	
	/**
	 * Constructor for cases when the id is unknown 
	 * and has to be calculated by the system
	 * @param location : Relative Location on hard disk
	 */
	public AudioFile(String location) {
		this.id = UUID.randomUUID();
		setLocation(location);
		if(Program.DEBUG) System.out.println("New Audio File registered: " + getLocation());
	}
	
	/**
	 * Constructor for cases when the id has to be specific (load project fe)
	 * @param id
	 * @param location
	 */
	public AudioFile(final String location, final UUID id) {
		setId(id);
		setLocation(location);
		if (Program.DEBUG) System.out.println("New Audio File registered: Location: " + getLocation() + ", id: " + getId());
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
		try {
			File f = new File(location);
			media = new Media(f.toURI().toString());
			mplayer = new MediaPlayer(media);
		}
		catch(final Exception e) {
			System.out.println("AudioFile : Media file not found!");
			e.printStackTrace();
		}
	}

	public AudioFilePlaybackMode getPlaybackMode() {
		return playbackMode;
	}

	public void setPlaybackMode(final AudioFilePlaybackMode playbackMode) {
		this.playbackMode = playbackMode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void playOnce() {
		try {
			mplayer.stop();
			mplayer.play();
			mplayer.setOnEndOfMedia(null);
		}
		catch(Exception e) {
			System.out.println("Problem with JavaFX MediaPlayer");
		}
	}
	
	public void playLoop() {
		try {
			mplayer.stop();
			mplayer.setOnEndOfMedia(new Runnable() {
			       @Override
				public void run() {
			           mplayer.seek(Duration.ZERO);
			         }
			     });
			mplayer.play();
		}
		catch(Exception e) {
			System.out.println("Problem with JavaFX MediaPlayer");
		}
	}
	
	public void stop() {
		try {
			mplayer.stop();
			mplayer.setOnEndOfMedia(null);
		}
		catch(Exception e) {
			System.out.println("Problem with JavaFX MediaPlayer");
		}
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
		try {
			mplayer.setVolume(volume);
		}
		catch(Exception e) {
			System.out.println("Problem with JavaFX MediaPlayer");
		}
	}
	
	public double getVolume() {
		return this.volume;
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