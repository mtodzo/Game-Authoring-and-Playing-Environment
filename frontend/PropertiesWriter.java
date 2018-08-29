package frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * @author Susie Choi
 * Complement to PropertiesReader. Used for writing out new information to Properties files during the course of the program:
 * useful for writing saved user commands/variables to Properties files. 
 * Use by creating a PropertiesWriter object with the path of the ending file as a String arg, the keys/vals as Map elements to write out to that specified file. 
 * 
 */
class PropertiesWriter {

	private String myFilePath; 
	private Properties myProps;
	private HashMap<String, String> myMap; 
	private FileOutputStream myOutputStream; 
	
	protected PropertiesWriter(String filepath, Map<String, String> keysNVals) {
		myFilePath = filepath;
		myProps = new Properties(); 
		FileInputStream in = null;

		try {
			in = new FileInputStream(new File(myFilePath));
			myProps.load(in);
			myMap = (HashMap<String, String>) keysNVals; 
			myOutputStream = new FileOutputStream(myFilePath);
		} catch (FileNotFoundException e) {
			throw new MissingResourceException(myFilePath, "", "");
		} catch (IOException e) {
			throw new MissingResourceException(myFilePath, "", "");
		}
		
	}

	protected void write() {

		for (String key : myMap.keySet()) {
			myProps.setProperty(key, myMap.get(key));
		}
		
		try {
			myProps.store(myOutputStream, null);
		} catch (IOException e) {
			throw new MissingResourceException(myFilePath, "", "");
		}

	}
	
}
