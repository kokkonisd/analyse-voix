package gui;

import java.io.File;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Open file class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class OpenWindow {

	// stage and scene variables
	private static Stage window;
	private static Scene scene;

	// container variables and main layout
	private static VBox fileChoice;
	private static HBox buttons;
	private static VBox layout;

	// button variables
	private static Button btnDone;
	private static Button btnCancel;

	// label variables
	private static Label nameMessage;
	private static Label fileMessage;

	// combobox variables
	private static ComboBox<String> namesInDatabase;
	private static ComboBox<String> filesInDatabase;

	// window width & height variables
	private static int WIDTH;
	private static int HEIGHT;

	/**
	 * method to handle the opening of files
	 * 
	 * @param btn : Button that corresponds to the file opened
	 */
	public static void display(Button btn)
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Open File");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// text field info label initialization & setup
		nameMessage = new Label("Please choose a person:");
		fileMessage = new Label("Please choose a file:");

		// name and file combo boxes initialization
		namesInDatabase = new ComboBox<String>();
		filesInDatabase = new ComboBox<String>();

		// check to see if the database is empty
		if(listDirectories().length == 0){
			// pop an alert box and close the window
			AlertBox.display("The database is empty. Please analyze something first.");
			window.close();
		}else{
			// populate the name combo box with directory names
			for(String dir_name : listDirectories()){
				// directories get added to the combo box in a readable format
				namesInDatabase.getItems().add(Format.dirToReadable(dir_name));
			}

			// name combo box action handler - this refreshes the file combo box
			namesInDatabase.setOnAction(e -> {
				// empty the file combo box
				filesInDatabase.getItems().clear();
				// populate the file combo box with file names
				for(String file_name : listFiles(Format.dirToData(namesInDatabase.getValue()))){
					// files get added to the combo box in a readable format
					filesInDatabase.getItems().add(Format.fileToReadable(file_name));
				}
				// set the default value for the file - it's the first element of listFiles
				filesInDatabase.setValue(Format.fileToReadable(listFiles(
						Format.dirToData(namesInDatabase.getValue()))[0]));
			});

			// set the default value for the name - it's the first element of listDirectories
			namesInDatabase.setValue(Format.dirToReadable(listDirectories()[0]));

			// populate the file combo box with file names BEFORE the name is chosen
			for(String f : listFiles(Format.dirToData(namesInDatabase.getValue()))){
				filesInDatabase.getItems().add(Format.fileToReadable(f));
			}
			// set the default value for the file - as always, it's the first element of listFiles
			filesInDatabase.setValue(Format.fileToReadable(listFiles(
					Format.dirToData(namesInDatabase.getValue()))[0]));

			// combo box container initialization & setup
			fileChoice = new VBox(20);
			fileChoice.getChildren().addAll(nameMessage, namesInDatabase, fileMessage, filesInDatabase);
			fileChoice.setAlignment(Pos.CENTER);

			// button initialization & setup
			btnDone = new Button("Done");
			btnCancel = new Button("Cancel");

			// done button event handling
			btnDone.setOnAction(e -> {
				// if the file value is valid
				if(Format.dirToData(filesInDatabase.getValue()) != null){
					// enable the button corresponding to the number of the file
					btn.setDisable(false);
					
					// if it's the first button
					if(btn == AnalyseApp.g1){
						// change the file to the file the user chose
						// convert everything to raw data format, of course
						AnalyseApp.file1 = new File("database/" + 
								Format.dirToData(namesInDatabase.getValue()) + 
								"/" + Format.fileToData(filesInDatabase.getValue()));
					// if it's the second button
					}else if(btn == AnalyseApp.g2){
						// same but with file2 instead
						AnalyseApp.file2 = new File("database/" + 
								Format.dirToData(namesInDatabase.getValue()) + 
								"/" + Format.fileToData(filesInDatabase.getValue()));
					}
				// if the file is not valid
				}else{
					// set the corresponding file to null
					if(btn == AnalyseApp.g1){
						AnalyseApp.file1 = null;
					}else if(btn == AnalyseApp.g2){
						AnalyseApp.file2 = null;
					}
				}
				
				// clear the line graph
				AnalyseApp.clear.fire();
				
				// close the window
				window.close();
			});

			// cancel button event handling
			btnCancel.setOnAction(e -> {
				// close the window
				window.close();
			});
			
			// button container initialization & setup
			buttons = new HBox(20);
			buttons.getChildren().addAll(btnDone, btnCancel);
			buttons.setAlignment(Pos.CENTER);

			// main layout initialization & setup
			layout = new VBox(20);
			layout.getChildren().addAll(fileChoice, buttons);
			layout.setAlignment(Pos.CENTER);

			// fix width and height
			WIDTH = 350;
			HEIGHT = 300;

			// scene initialization & setup, show window
			scene = new Scene(layout, WIDTH, HEIGHT);
			window.setScene(scene);
			window.setMinWidth(WIDTH);
			window.setMinHeight(HEIGHT);
			window.showAndWait();

		}

	}

	
	/**
	 * method to list files in a directory in the database
	 * 
	 * @param name : name of the directory
	 * @return String[] : array of filenames in a directory
	 */
	public static String[] listFiles(String name)
	{
		// ArrayList to add filenames to
		ArrayList<String> files = new ArrayList<String>();
		// directory variable, it's in the database
		File folder = new File("database/" + name);
		// use the listFiles() method to get a File[] object
		File[] listOfFiles = folder.listFiles();

		// iterate through the File[] object (the list of filenames)
		for(int i = 0; i < listOfFiles.length; i++) {
			// if the element is a file and is not a system file (= it doesn't start with '.')
			if(listOfFiles[i].isFile() && listOfFiles[i].getName().charAt(0) != '.') {
				// add it to the ArrayList
				files.add(listOfFiles[i].getName());
			}
		}
		
		// return an array of filenames
		return files.toArray(new String[]{});
	}

	/**
	 * method to list directories in the database
	 * 
	 * @return String[] : array of directory names
	 */
	public static String[] listDirectories()
	{
		// ArrayList to add directory names to
		ArrayList<String> dirs = new ArrayList<String>();
		// directory variable, it's the database itself
		File folder = new File("database");
		// use the listFiles() method to get a File[] object
		File[] listOfDirs = folder.listFiles();

		// iterate through the File[] object (the list of directory names)
		for(int i = 0; i < listOfDirs.length; i++) {
			// if the element is a directory
			if(listOfDirs[i].isDirectory()) {
				// add it to the ArrayList
				dirs.add(listOfDirs[i].getName());
			}
		}
		
		// return an array of directory names 
		return dirs.toArray(new String[]{});
	}
}
