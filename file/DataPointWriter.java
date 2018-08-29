package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataPointWriter {
	
	public static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy_hh-mm-ss"; 
	public static final String DEFAULT_DATAPOINTS_FILEPATH = "graphing/";
	public static final String DEFAULT_FILENAME_SEPARATOR = "_";
	public static final int DEFAULT_SECOND_DIVISOR = 1000; 
	
	private PrintWriter myWriter; 

	public DataPointWriter(String gameName, String subfolder) throws FileNotFoundException {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		String formattedDate = df.format(c.getTime());
		
		String scoreFileName = DEFAULT_DATAPOINTS_FILEPATH+subfolder+gameName+DEFAULT_FILENAME_SEPARATOR+formattedDate; 
		File scoreFile = new File(scoreFileName);
		scoreFile.getParentFile().mkdirs(); 
		myWriter = new PrintWriter(scoreFile);
	}
	
	public void recordDataPoint(int y) {
		long recordedX = System.currentTimeMillis() / DEFAULT_SECOND_DIVISOR; 
		this.myWriter.write(Long.toString(recordedX)+" ");
		this.myWriter.write(Integer.toString(y)+"\n");
		this.myWriter.flush();
	}
	
}
