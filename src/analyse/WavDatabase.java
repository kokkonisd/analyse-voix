package analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import WavFile.WavFileException;

/**
 * Database management class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class WavDatabase {

	/**
	 * method to get the current date and time
	 * 
	 * @return String : the date and time ("DD-MM-YYYY_hh-mm-ss")
	 */
	private String getDateTime(){
		// set the date format as described above, get calendar instance
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		Calendar cal = Calendar.getInstance();

		// return the current date-time string
		return dateFormat.format(cal.getTime());
	}

	
	/**
	 * method to log a subject's name
	 * 
	 * @param name : String, name of the subject
	 */
	private void logName(String name)
	{
		// get the path of the new folder
		Path name_folder = Paths.get("database/" + name);

		// if folder doesn't exist
		if(!Files.exists(name_folder)){ 
			// create the folder
			File dir = new File("database/" + name);
			// if unable to create folder, throw RuntimeException
			if(!dir.mkdir()){
				throw new RuntimeException("[could not create directory\"" + name + "\"]");
			}
		}
	}

	
	/**
	 * method to log an entry
	 * 
	 * @param name : String, the subject's name
	 * @param in_wav : File, the file to analyze
	 * @return String : the filename to be used in the gui
	 * @throws IOException
	 * @throws WavFileException
	 */
	public String logData(String name, File in_wav) throws IOException, WavFileException
	{
		/* this will create the folder if it doesn't exist
		or will do nothing if the folder already exists */
		logName(name);

		// set the name of the new file as "name-surname_DD-MM-YYYY_hh-mm-ss.txt"
		File out_file = new File("database/"+ name + "/" + name + "_" + getDateTime() + ".txt");

		// perform an fft on the wav file
		WavFFT fft = new WavFFT(in_wav);
		fft.WriteToFile(out_file);
		fft.close();

		// return the file so we can grab the name
		return out_file.getName();
	}

	
	/**
	 * method to get the data from a frequency file
	 * 
	 * @param f : frequency file to read
	 * @return Double[] : the array of frequency data
	 * @throws FileNotFoundException
	 */
	public Double[] getData(File f) throws FileNotFoundException
	{
		// ArrayList to hold the data
		ArrayList<Double> data = new ArrayList<Double>();
		
		// Scanner to read the file
		Scanner sc = new Scanner(f);

		// read the file and add the numbers to the ArrayList
		while(sc.hasNextLine()){
			String s = sc.nextLine();
			data.add(Double.parseDouble(s));
		}

		// close the Scanner
		sc.close();
		
		// return the array of data to be used in the gui
		return data.toArray(new Double[]{});
	}
}
