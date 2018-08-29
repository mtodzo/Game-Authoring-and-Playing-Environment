
package xml;

import java.io.IOException;

import data.GameData;

/**
 * @author Brendan Cheng 3/29/2018
 *
 * Interface for classes that write XML files. Write method uses other private methods or XMLDocumentBuilder methods to serialize data and save to a file.
 *
 */

public interface XMLWriter {

	/**
	 * Writes the inputted GameData object to an XML file using XStream and/or XMLDocumentBuilder methods. Saves the file at a specified filepath.
	 *
	 * @param g		A GameData object (in this case, AuthoredGame or PlayState) whose data will be read and written to an XML file
	 * @param filepath	filename to which file will be saved. Don't include .xml
	 * @throws IOException 
	 */
	public void write(GameData g, String filepath) throws IOException;

}