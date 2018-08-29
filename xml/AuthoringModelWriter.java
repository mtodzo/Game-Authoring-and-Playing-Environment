package xml;

import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.GameData;

public class AuthoringModelWriter implements XMLWriter {

	/**
	 * Implementation of XMLWriter that writes an XML file specifying the data written while authoring a game, including tower types, tower functionality, 
	 * enemy types, and path. Utilizes XStream methods to write data about each object in AuthoringModel that is passed to write() method.
	 * 
	 * @author Brendan Cheng 3/31/2018
	 */

	private XStream parser;
	private final String FILE_PATH = "SavedModels/";
	private final String FILE_TYPE = ".xml";

	/**
	 * Initializes XStream parser to write data. Set up parser so it can ignore @XStreamOmitField annotations
	 */
	public AuthoringModelWriter() {
		parser = new XStream(new DomDriver());
		parser.autodetectAnnotations(true);
	}

    /**
     * Implementation of write method that saves AuthoredGame data for authoring to SavedModels folder
     * @throws IOException 
     */
    @Override
    public void write(GameData g, String filepath) throws BadGameDataException, IOException {
	// check type
	if (!g.getClass().getSimpleName().equals("AuthoredGame")) {
	    throw new BadGameDataException("Incorrect GameData: Must use AuthoredGame object to store correct data");
	}
	
	XMLDocumentBuilder.stringToXML(parser.toXML(g), FILE_PATH + filepath + FILE_TYPE);

    }

    
}


