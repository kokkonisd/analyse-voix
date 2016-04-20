package gui;

import java.util.regex.Pattern;

/**
 * Name/file formatting class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class Format {

	/**
	 * method to transform a raw directory name to a readable one
	 * 
	 * raw directory names are by default:
	 * name-surname
	 * 
	 * @param dirName : name of the directory
	 * @return String : formatted name ("SURNAME Name")
	 */
	public static String dirToReadable(String dirName)
	{
		// seperate the name and the surname
		String[] t = dirName.split(Pattern.quote("-"));

		// return "SURNAME Name"
		return t[1].toUpperCase() + " " + t[0].substring(0,1).toUpperCase() + t[0].substring(1);
	}


	/**
	 * method to transform a raw filename to a readable one
	 * 
	 * raw filenames are by default:
	 * name-surname_DD-MM-YYYY_hh-mm-ss.txt
	 * 
	 * @param fileName : name of the file
	 * @return String : formatted name ("SURNAME Name, DD/MM/YYYY, hh:mm:ss")
	 */
	public static String fileToReadable(String fileName)
	{
		// separate name, date and time
		String[] t = fileName.split(Pattern.quote("_"));

		// return "SURNAME Name, DD/MM/YYYY, hh:mm:ss"
		// name is handled by dirToReadable()
		// finally, we throw away the ".txt" part
		return dirToReadable(t[0]) + ", " + t[1].replace('-', '/') + ", " 
		+ t[2].replace('-', ':').substring(0, t[2].length()-4);
	}


	/**
	 * method to transform a readable directory name to a raw one
	 * 
	 * readable directory names are by default:
	 * SURNAME Name
	 * 
	 * @param dirName : name of the directory
	 * @return String : formatted name ("name-surname")
	 */
	public static String dirToData(String dirName)
	{
		// separate the name and the surname
		String[] t = dirName.split(Pattern.quote(" "));

		// return "name-surname"
		return t[1].toLowerCase() + "-" + t[0].toLowerCase();
	}


	/**
	 * method to transform a readable filename to a raw one
	 * 
	 * readable filenames are by default:
	 * SURNAME Name, DD/MM/YYYY, hh:mm:ss
	 * 
	 * @param fileName : name of the file
	 * @return String : formatted name ("name-surname_DD-MM-YYYY_hh-mm-ss.txt")
	 */
	public static String fileToData(String fileName)
	{
		// separate name, date and time
		String[] full = fileName.split(Pattern.quote(", "));

		// return "name-surname_DD-MM-YYYY_hh-mm-ss.txt"
		// name is handled by dirToData()
		// finally, we add the ".txt" part
		return dirToData(full[0]) + "_" + full[1].replace('/', '-') + "_" + full[2].replace(':', '-') + ".txt";
	}

	
	/**
	 * method to abbreviate filenames
	 * in order to get them to fit on a button
	 * 
	 * @param button_name : the string to abbreviate
	 * @return String : an abbreviated string
	 */
	public static String shorten(String button_name)
	{
		// if it's less than 14 characters long (that's the limit)
		if(button_name.length() <= 14){
			// don't touch it
			return button_name;
		// if it's more than 14 characters long
		}else{
			// return the first 11, then add "..."
			return button_name.substring(0, 10) + "...";
		}
	}

}
