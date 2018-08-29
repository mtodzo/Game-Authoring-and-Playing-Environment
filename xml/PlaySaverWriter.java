package xml;

import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.GameData;

/**
 * Implementation of XMLWriter that writes an XML file specifying the current state of a Play. Saved data includes tower info (placement, upgrades), projectile
 * info (velocity, direction of motion, and location), enemy info (locations and health of each enemy), Path info, and player info (i.e. health remaining,
 * resources, level number).
 * 
 * @author Brendan Cheng 3/31/2018
 */

public class PlaySaverWriter implements XMLWriter {

	private XStream parser;
	private final String FILE_PATH = "SavedPlays/";
	private final String FILE_TYPE = "SavedPlays/";
	
	public PlaySaverWriter() {
		parser = new XStream(new DomDriver());
		parser.autodetectAnnotations(true);
	}

	@Override
	public void write(GameData g, String filepath) throws BadGameDataException, IOException  {
		// check type
		if (!g.getClass().getSimpleName().equals("PlayState")) {
			throw new BadGameDataException("Incorrect GameData: Must use PlayState object to store correct data");
		}
		XMLDocumentBuilder.stringToXML(parser.toXML(g), FILE_PATH + filepath + FILE_TYPE);
	}
}

