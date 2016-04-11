package gui;

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
 * Delete subject / entry class for the Analyse application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class DeleteWindow {

	// stage & scene variables
	private static Stage window;
	private static Scene scene;

	// label variables
	private static Label message;
	private static Label submessage;

	// button variables
	private static Button btnDelete;
	private static Button btnCancel;

	// container variables & main layout
	private static HBox buttons;
	private static ComboBox<String> names;
	private static ComboBox<String> files;
	private static VBox layout;

	// a boolean var to check if we should close the window or not
	private static boolean deleted;

	// these vars exist because I can't access the buttons from 2 child classes deep
	// variables for intermediary class control
	public static boolean g1out;
	public static boolean g2out;

	// width & height variables
	private static int WIDTH;
	private static int HEIGHT;


	/**
	 * method to handle the deletion of subjects
	 */
	public static void displayName()
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Delete Subject");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// message initialization
		message = new Label("Please choose a subject to delete:");

		// check to see if the database is empty
		if(OpenWindow.listDirectories().length == 0){
			// pop an alert box and close the window
			AlertBox.display("The database is empty. Please analyze something first.");
			window.close();
		}else{

			// name list initialization & setup
			names = new ComboBox<String>();
			for(String s : OpenWindow.listDirectories()){
				names.getItems().add(Format.dirToReadable(s));
			}
			names.setValue(Format.dirToReadable(OpenWindow.listDirectories()[0]));

			// delete button initialization & setup
			btnDelete = new Button("Delete");
			btnDelete.setOnAction(e -> {
				deleted = AlertDelete.display("database/" + Format.dirToData(names.getValue()), 
						"subject \"" + names.getValue() + "\"");

				// handling the deleted files
				// if the file used in position 1 is deleted
				if(g1out == true){
					// disable g1, set file1 and its label to null
					AnalyseApp.g1.setDisable(true);
					AnalyseApp.file1 = null;
					AnalyseApp.file1Name.setText("File 1: " + null);
					AnalyseApp.dif.setDisable(true);
				}
				// if the file used in position 2 is deleted
				if(g2out == true){
					// disable g2, set file2 and its label to null
					AnalyseApp.g2.setDisable(true);
					AnalyseApp.file2 = null;
					AnalyseApp.file2Name.setText("File 2: " + null);
					AnalyseApp.dif.setDisable(true);
				}

				// if the user has deleted something,
				// they didn't cancel out, so close the window
				if(deleted == true){
					window.close();
				}
			});

			// cancel button & button layout setup
			btnCancel = new Button("Cancel");
			btnCancel.setOnAction(e -> window.close());

			buttons = new HBox(20);
			buttons.getChildren().addAll(btnDelete, btnCancel);
			buttons.setAlignment(Pos.CENTER);

			// layout initialization & setup
			layout = new VBox(20);
			layout.getChildren().addAll(message, names, buttons);
			layout.setAlignment(Pos.CENTER);

			// scene initialization & setup
			WIDTH = 400;
			HEIGHT = 300;

			scene = new Scene(layout, WIDTH, HEIGHT);
			window.setScene(scene);
			window.setMinWidth(WIDTH);
			window.setMinHeight(HEIGHT);
			window.showAndWait();
		}
	}


	/**
	 * method to handle the deletion of entries
	 */
	public static void displayFile()
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Delete Entry");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// message & submessage initialization
		message = new Label("Please choose a subject to delete from:");
		submessage = new Label("Please choose an entry to delete:");

		// check to see if the database is empty
		if(OpenWindow.listDirectories().length == 0){
			// pop an alert box and close the window
			AlertBox.display("The database is empty. Please analyze something first.");
			window.close();
		}else{

			// two combo boxes, to choose a name and a file
			names = new ComboBox<String>();
			files = new ComboBox<String>();
			// populate directory list
			for(String d : OpenWindow.listDirectories()){
				names.getItems().add(Format.dirToReadable(d));
			}

			// update file list when the name changes
			names.setOnAction(e -> {
				files.getItems().clear();
				for(String f : OpenWindow.listFiles(Format.dirToData(names.getValue()))){
					files.getItems().add(Format.fileToReadable(f));
				}
				files.setValue(Format.fileToReadable(OpenWindow.listFiles(
						Format.dirToData(names.getValue()))[0]));
			});

			// set the default value for the name
			names.setValue(Format.dirToReadable(OpenWindow.listDirectories()[0]));

			// populate the file list
			for(String f : OpenWindow.listFiles(Format.dirToData(names.getValue()))){
				files.getItems().add(Format.fileToReadable(f));
			}
			// set the default value for the file
			files.setValue(Format.fileToReadable(OpenWindow.listFiles(
					Format.dirToData(names.getValue()))[0]));

			// delete button initialization & setup
			btnDelete = new Button("Delete");
			btnDelete.setOnAction(e -> {
				deleted = AlertDelete.display("database/" + Format.dirToData(names.getValue()) + "/" +
						Format.fileToData(files.getValue()), "the entry \"" + files.getValue() + "\"");

				// handling the deleted files
				// same as in displayName()
				if(g1out == true){
					AnalyseApp.g1.setDisable(true);
					AnalyseApp.file1 = null;
					AnalyseApp.file1Name.setText("File 1: " + null);
					AnalyseApp.dif.setDisable(true);
				}
				if(g2out == true){
					AnalyseApp.g2.setDisable(true);
					AnalyseApp.file2 = null;
					AnalyseApp.file2Name.setText("File 2: " + null);
					AnalyseApp.dif.setDisable(true);
				}

				if(deleted == true){
					window.close();
				}
			});

			// cancel button & button layout setup
			btnCancel = new Button("Cancel");
			btnCancel.setOnAction(e -> window.close());

			buttons = new HBox(20);
			buttons.getChildren().addAll(btnDelete, btnCancel);
			buttons.setAlignment(Pos.CENTER);

			// layout initialization & setup
			layout = new VBox(20);
			layout.getChildren().addAll(message, names, submessage, files, buttons);
			layout.setAlignment(Pos.CENTER);

			// scene initialization & setup
			WIDTH = 400;
			HEIGHT = 300;

			scene = new Scene(layout, WIDTH, HEIGHT);
			window.setScene(scene);
			window.setMinWidth(WIDTH);
			window.setMinHeight(HEIGHT);
			window.showAndWait();
		}
	}
}
