package com.worldnamer.audiotag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class FileProcessor {
	private boolean view;
	private String artist;
	private String disc;
	private String title;
	
	public boolean hasNoOptions() {
		return !view && (artist == null) && (disc == null) && (title == null);
	}
	
	public void setView(boolean view) {
		this.view = view;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setDisc(String disc) {
		this.disc = disc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void processFiles(List<String> files) {
		for (String fileString : files) {
			File file = new File(fileString);
			if (file.exists()) {
				if (file.isDirectory()) {
					System.out.println(file.getName() + " is a directory.");
				} else {
					try {
						AudioFile audioFile = AudioFileIO.read(file);
						
						Tag tag = audioFile.getTag();
						
						boolean needToWrite = false;
						
						if (artist != null) {
							tag.setField(FieldKey.ARTIST, artist);
							needToWrite = true;
						}

						if (disc != null) {
							tag.setField(FieldKey.ALBUM, disc);
							needToWrite = true;
						}

						if (title != null) {
							tag.setField(FieldKey.TITLE, title);
							needToWrite = true;
						}
						
						if (needToWrite) {
							audioFile.commit();
						}

						if (view) {
							System.out.println(tag.getFirst(FieldKey.TITLE) + "," + tag.getFirst(FieldKey.ALBUM) + "," + tag.getFirst(FieldKey.ARTIST));
						}
					} catch (CannotReadException ex) {
						System.out.println(file + " is not a recognized audio file.");
					} catch (IOException ex) {
						System.out.println(file + " is not a recognized audio file.");
					} catch (TagException ex) {
						System.out.println(file + " is not a recognized audio file.");
					} catch (ReadOnlyFileException ex) {
						System.out.println(file + " is not a recognized audio file.");
					} catch (InvalidAudioFrameException ex) {
						System.out.println(file + " is not a recognized audio file.");
					} catch (CannotWriteException ex) {
						System.out.println(file + " could not be written to. " + ex.getCause());
					}
				}
			} else {
				System.out.println(file.getName() + " is not a valid audio file.");
			}
		}
	}
}
