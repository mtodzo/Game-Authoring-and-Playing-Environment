package xml;

import java.lang.reflect.Constructor;


/**
 * Static class that creates XMLWriters/XMLReaders to handle data parsing.
 *
 * @author Brendan Cheng
 *
 */

public class XMLFactory {

    public XMLFactory() {

    }

    /**
     * Uses reflection to create an XMLWriter of the specified type
     *
     * @param writerType
     * String representation of class name of desired writer type
     *
     * @return
     * Instance of that implementation of XMLWriter
     */
    public static XMLWriter generateWriter(String writerType) throws ReflectionErrorException {
	try {
	    Class<?> writer = Class.forName("xml." + writerType);
	    Constructor<?> c = writer.getConstructor();
	    return (XMLWriter) c.newInstance();
	} catch (Exception e) {
	    throw new ReflectionErrorException();
	}
    }

    /**
     * Uses reflection to create an XMLReader of the specified type
     *
     * @param readerType
     * String representation of class name of desired reader type
     *
     * @return
     * Instance of that implementation of XMLReader
     */
    public static XMLReader generateReader(String readerType) throws ReflectionErrorException {
	try {
	    Class<?> reader = Class.forName("xml." + readerType);
	    Constructor<?> c = reader.getConstructor();
	    return (XMLReader) c.newInstance();
	} catch (Exception e) {
	    throw new ReflectionErrorException();
	}
    }

}

