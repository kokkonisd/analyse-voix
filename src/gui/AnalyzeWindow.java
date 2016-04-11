package gui;

import java.io.File;
import java.io.IOException;

import WavFile.WavFileException;
import analyse.WavDatabase;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Analyze window class for the Analyse application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class AnalyzeWindow {
	/* WINDOW GLOBAL VARS */

	// stage and scene variables
	private static Stage window;
	private static Scene scene;

	// buttons
	private static Button btnAnalyze;
	private static Button btnCancel;
	private static Button btnOpen;

	// containers and main layout
	private static HBox buttons;
	private static VBox firstName;
	private static VBox lastName;
	private static HBox openGroup;
	private static VBox layout;

	// labels
	private static Label messageOpen;
	private static Label messageFirstName;
	private static Label messageLastName;

	// textfields
	private static TextField name1;
	private static TextField name2;

	// wav file that the user chooses
	private static File wavfile;

	// width and height of the window
	private static int WIDTH;
	private static int HEIGHT;

	
	/**
	 * method to handle the analysis of files
	 */
	public static void display()
	{
		/* ==== Window Setup ==== */
		
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Analyze New File");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// first and last name fields, initialization & setup
		messageFirstName = new Label("Please enter the first name of the subject");
		messageLastName = new Label("Please enter the last name of the subject");
		name1 = new TextField();
		name2 = new TextField();
		firstName = new VBox(10);
		firstName.getChildren().addAll(messageFirstName, name1);
		firstName.setAlignment(Pos.CENTER_LEFT);
		lastName = new VBox(10);
		lastName.getChildren().addAll(messageLastName, name2);
		lastName.setAlignment(Pos.CENTER_LEFT);

		// open file group initialization & setup
		messageOpen = new Label("Please choose a file to analyze:");
		btnOpen = new Button("No File Chosen");
		openGroup = new HBox(10);
		openGroup.getChildren().addAll(messageOpen, btnOpen);
		openGroup.setAlignment(Pos.CENTER_LEFT);

		// control button group initialization & setup
		btnAnalyze = new Button("Analyze & Save");
		btnCancel = new Button("Cancel");
		buttons = new HBox(20);
		buttons.getChildren().addAll(btnAnalyze, btnCancel);
		buttons.setAlignment(Pos.CENTER);

		// main layout initialization & setup
		layout = new VBox(20);
		layout.getChildren().addAll(firstName, lastName, openGroup, buttons);

		
		/* ==== Button Actions ==== */

		// btnOpen event handling
		btnOpen.setOnAction(e -> btnOpenAction());

		// btnAnalyze
		// boolean binding to check if both names are filled in
		// it's true iff both text fields are not empty
		BooleanBinding bb = new BooleanBinding() {
			{
				super.bind(name1.textProperty(), name2.textProperty());
			}
			@Override
			protected boolean computeValue() {
				return (name1.getText().isEmpty() || name2.getText().isEmpty());
			}
		};

		btnAnalyze.disableProperty().bind(bb); // disabled by default
		// btnAnalyze event handling
		btnAnalyze.setOnAction(e -> {
			try{
				btnAnalyzeAction();
			}catch(IOException x){
				System.out.println("[IOException in AnalyzeWindow]");
			}catch(WavFileException w){
				System.out.println("[WavFileException in AnalyzeWindow]");
			}
		});

		// btnCancel event handling
		btnCancel.setOnAction(e -> window.close());

		// scene initialization & setup
		WIDTH = 400;
		HEIGHT = 240;

		scene = new Scene(layout, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setMinWidth(WIDTH);
		window.setMinHeight(HEIGHT);
		window.showAndWait();
	}

	
	/**
	 * method to handle btnOpen events
	 */
	private static void btnOpenAction()
	{
		// create a file chooser object to pick the wav file
		FileChooser wavChoose = new FileChooser();
		wavChoose.setTitle("Open Sound File");

		// only allow .wav files
		wavChoose.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Wav Files", "*.wav"));

		// this will be a popup to this class' window
		wavfile = wavChoose.showOpenDialog(window);

		// change the name of the button to represent the file chosen by the user
		if(wavfile != null){
			btnOpen.setText(Format.shorten(wavfile.getName()));
		}
	}

	
	/**
	 * method to handle btnAnalyze events
	 * @throws IOException
	 * @throws WavFileException
	 */
	private static void btnAnalyzeAction() throws IOException, WavFileException
	{
		// if the file is not valid
		if(wavfile == null){
			// pop an alert box informing the user
			AlertBox.display("You did not specify a file.");
		// if either of the names are not valid
		}else if(!checkName(name1.getText()) || !checkName(name2.getText())){
			// pop an alert box informing the user
			AlertBox.display("The name contains invalid characters. You may only use A-Z, a-z.");
		// if everything is in order
		}else{
			// format the name as specified by the database: name-surname
			String name = name1.getText().toLowerCase() + "-" + name2.getText().toLowerCase();

			// log the data and get the file name
			WavDatabase database = new WavDatabase();
			String fileName = database.logData(name, wavfile); // the logData method throws back the filename

			// if the user hasn't chosen a file1, make this the file1 automatically
			if(AnalyseApp.file1 == null){
				// fetch the file using the name and the fileName var
				AnalyseApp.file1 = new File("database/"+ name + "/" + fileName);
				// enable the button
				AnalyseApp.g1.setDisable(false);
				// set the label to represent the name of the new file
				AnalyseApp.file1Name.setText("File 1: " + AnalyseApp.file1.getPath());
				// same for file2
			}else if(AnalyseApp.file2 == null){
				AnalyseApp.file2 = new File("database/"+ name + "/" + fileName);
				AnalyseApp.g2.setDisable(false);
				AnalyseApp.file2Name.setText("File 2: " + AnalyseApp.file2.getPath());
			}

			// if both files are valid, enable the difference button
			if(AnalyseApp.file1 != null && AnalyseApp.file2 != null){
				AnalyseApp.dif.setDisable(false);
			}

			// close the window when you're done
			window.close();
		}
	}

	
	/**
	 * method to check the validity of the name
	 * and surname entered by the user
	 * 
	 * both should contain characters from
	 * A-Z and a-z ONLY, since we're using
	 * the names as filenames
	 * 
	 * @param s : string to check for validity
	 * @return boolean : valid = true, not valid = false
	 */
	private static boolean checkName(String s){
		// iterate through the string
		for(int i = 0; i<s.length(); i++){
			// if the character is other than A-Z, a-z, the string is not valid, return false
			if(!((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z'))){
				return false;
			}
		}
		// if all characters are valid, return true
		return true;
	}
}
