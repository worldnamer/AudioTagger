package com.worldnamer.audiotag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.LogManager;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class AudioTag {
	public static void main(String args[]) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		InputStream config = new ByteArrayInputStream("org.jaudiotagger.level=OFF".getBytes());
		LogManager.getLogManager().readConfiguration(config);
		
		OptionParser parser = new OptionParser();
		parser.acceptsAll(Arrays.asList("v", "view"), "view the tags of a file (default, if no options present)");
		parser.acceptsAll(Arrays.asList("a", "artist"), "set the artist of a file").withRequiredArg().ofType(String.class);
		parser.acceptsAll(Arrays.asList("d", "disc"), "set the album of a file").withRequiredArg().ofType(String.class);
		parser.acceptsAll(Arrays.asList("t", "title"), "set the title of a file").withRequiredArg().ofType(String.class);
		
		if (args.length == 0) {
			parser.printHelpOn(System.out);
		} else {
			FileProcessor processor = new FileProcessor();

			OptionSet options = parser.parse(args);
			
			if (options.has("v")) {
				processor.setView(true);
			}
			
			if (options.has("a")) {
				processor.setArtist((String)options.valueOf("a"));
			}
			
			if (options.has("d")) {
				processor.setDisc((String)options.valueOf("d"));
			}
			
			if (options.has("t")) {
				processor.setTitle((String)options.valueOf("t"));
			}
			
			if (processor.hasNoOptions()) {
				processor.setView(true);
			}
			
			processor.processFiles(options.nonOptionArguments());
		}
	}
}