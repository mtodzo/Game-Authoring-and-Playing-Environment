package xml;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.javafx.tools.packager.Log;

/**
 * @author Brendan Cheng
 * 
 *	Class containing static methods that are common to writing all XML files. Some methods can be used to manually write files if the user wishes to 
 *	avoid using XStream and instead create a manual serializer. Other methods can be used if the user prefers to serialize data using XStream.
 */

public class XMLDocumentBuilder {

    /**
     * Creates new instance of a document to be written to
     * 
     * @return document to be written to
     * @throws ParserConfigurationException 
     */
    public static Document initializeDoc() throws ParserConfigurationException {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	return db.newDocument();
    }

    /**
     * Transforms a document object that has been written into a Document object into an XML file with the specified filepath and name
     * 
     * @param doc   Document being saved
     * @param file  File object containing filepath to which doc will be written and saved
     * @throws TransformerFactoryConfigurationError 
     * @throws TransformerException 
     */
    public static void saveXMLFile(Document doc, File file) throws TransformerFactoryConfigurationError, TransformerException {
	Transformer transformer = TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	DOMSource source = new DOMSource(doc);
	StreamResult newFile = new StreamResult(file);
	transformer.transform(source, newFile);
    }

    /**
     * Used to add String data to nodes in an XML file. The expected output should be as follows:
     * <elemName> data </elemName>
     * 
     * @param doc: Document to be added
     * @param elemName: Name of the node/element
     * @param data: The data to be added
     * @return A node to be added to the xml file
     */
    public static Node addData(Document doc, String elemName, String data) {
	Element e = doc.createElement(elemName);
	e.appendChild(doc.createTextNode(data));
	return e;
    }

    /**
     * Creates a new node, adds it to file by appending it to a parent node, then returns that element to be used by other methods 
     * 
     * @param parent Parent node to be appended to
     * @param name	 Node name/type
     * @return		 Node that has been appended to parent
     */
    public static Element appendElement(Element parent, String name, Document file) {
	Element e = file.createElement(name);
	parent.appendChild(e);
	return e;
    }

    /**
     * Writes and saves a file containing only the inputted String. Can be used in conjunction with XStream to easily write large files from one String
     * 
     * @param data	data to be written
     * @param filename	name of file to be written to
     */
    public static void stringToXML(String data, String filename) throws IOException {
	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	writer.write(data);
	writer.close();
    }

}
