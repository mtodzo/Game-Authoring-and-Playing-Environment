package xml;

import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import engine.PlayState;

public class PlayLoader implements XMLReader {

    private XStream parser;
    private final String FILE_PATH = "SavedPlays/";
    private final String FILE_TYPE = ".xml";
    
    public PlayLoader() {
	parser = new XStream(new DomDriver());
    }
    
    @Override
    public PlayState createModel(String filename) {
	    File f = new File(FILE_PATH + filename + FILE_TYPE);
	    PlayState g = (PlayState) parser.fromXML(f);
	    return g;
    }
    
}
