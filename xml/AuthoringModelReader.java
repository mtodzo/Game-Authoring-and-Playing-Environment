package xml;

import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import authoring.AuthoredGame;

/**
 * Reads an XML document using XStream to reconstruct an AuthoredGame object to either be further edited by authoring environment or to create a game engine
 * in the game play environment
 * 
 * @author Brendan Cheng
 *
 */

public class AuthoringModelReader implements XMLReader {

	private XStream parser;
	private final String FILE_PATH = "SavedModels/";
	private final String FILE_TYPE = ".xml";
	
	public AuthoringModelReader() {
		parser = new XStream(new DomDriver()); // DomDriver writes XML with proper spacing/indentation
	}
	
	@Override
	public AuthoredGame createModel(String filename) {
	    File f = new File(FILE_PATH + filename + FILE_TYPE);
	    AuthoredGame g = (AuthoredGame) parser.fromXML(f);
	    return g;
	}

}
