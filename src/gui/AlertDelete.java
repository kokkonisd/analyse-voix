package gui;

import java.io.File;
import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Delete verification/alert box class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class AlertDelete {

	// stage & scene variables
	private static Stage window;
	private static Scene scene;
	
	// label variable to display a message
	private static Label message;
	
	// yes & no buttons
	private static Button btnYes;
	private static Button btnNo;
	
	// container variables
	private static HBox buttons;
	private static VBox layout;
	
	// deleted variable to determine wether the user
	// actually deleted something or not
	private static boolean deleted;
	
	// window width & height variables
	private static int WIDTH;
	private static int HEIGHT;

	/**
	 * method to verify the deletion of a subject/entry
	 * 
	 * @param path : the path of the directory/file to delete
	 * @param msg : the message to show to the user
	 * @return boolean : true if yes btn clicked, false if no btn clicked
	 */
	public static boolean display(String path, String msg)
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Delete");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// message initialization
		message = new Label("Are you sure you want to delete " + msg + "?");

		// yes button initialization & setup
		btnYes = new Button("Yes");

		// yes button event handling
		btnYes.setOnAction(e -> {
			// make a new file using the path
			File toDelete = new File(path);

			// if it's a directory
			if(toDelete.isDirectory()){
				// throw away the "database/" part
				String name = path.substring(9);

				// we need to delete every file before we can delete the directory
				for(String file : OpenWindow.listFiles(name)){
					File temp = new File(path + "/" + file);
					// check to see if we're deleting something we're using on the main app
					try{
						if(Objects.equals(AnalyseApp.file1.getPath(), temp.getPath())){
							// if file 1 is deleted, tell DeleteWindow class to disable button 1
							DeleteWindow.g1out = true;
						}
						if(Objects.equals(AnalyseApp.file2.getPath(), temp.getPath())){
							// if file 2 is deleted, tell DeleteWindow class to disable button 2
							DeleteWindow.g2out = true;
						}
					}catch(NullPointerException x){
						/* null pointer exceptions will be thrown if any of the files
						 * on the main app are null, but it's ok
						 */
						System.out.print("Null pointer exception when trying to turn off buttons. ");
						System.out.println("Is it ok : " +
								(AnalyseApp.file1 == null || AnalyseApp.file2 == null));
					}
					// delete the file
					temp.delete();
				}
				// delete the (now empty) directory
				toDelete.delete();

			// if it's a file
			}else{
				// get the parent dir of the file
				File parentDir = new File(toDelete.getParent());

				// turn off buttons if their file is deleted
				try{
					if(Objects.equals(AnalyseApp.file1.getPath(), toDelete.getPath())){
						// if file 1 is deleted, tell DeleteWindow class to disable button 1
						DeleteWindow.g1out = true;
					}
					if(Objects.equals(AnalyseApp.file2.getPath(), toDelete.getPath())){
						// if file 2 is deleted, tell DeleteWindow class to disable button 2
						DeleteWindow.g2out = true;
					}
				}catch(NullPointerException x){
					/* null pointer exceptions will be thrown if any of the files
					 * on the main app are null, but it's ok
					 */
					System.out.print("Null pointer exception when trying to turn off buttons. ");
					System.out.println("Is it ok : " +
							(AnalyseApp.file1 == null || AnalyseApp.file2 == null));
				}

				// delete the file
				toDelete.delete();

				/* if we're deleting the last file in the directory,
				 * delete the directory as well
				 */
				if(OpenWindow.listFiles(parentDir.getName()).length == 0){
					parentDir.delete();
				}
			}
			
			// close the window
			window.close();
			// set the deleted var to true, since the user clicked "yes"
			deleted = true;
		});

		// no button initialization & setup
		btnNo = new Button("No");
		btnNo.setOnAction(e -> {
			// close the window
			window.close();
			// the user cancelled out
			deleted = false;
		});

		// button layout initialization & setup
		buttons = new HBox(20);
		buttons.getChildren().addAll(btnYes, btnNo);
		buttons.setAlignment(Pos.CENTER);

		// layout initialization & setup
		layout = new VBox(20);
		layout.getChildren().addAll(message, buttons);
		layout.setAlignment(Pos.CENTER);

		// scene initialization & setup
		WIDTH = message.getText().length()*6 + 50;
		HEIGHT = 100;

		scene = new Scene(layout, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setMinHeight(HEIGHT);
		window.setMinWidth(WIDTH);
		window.showAndWait();

		// clear the graph
		AnalyseApp.clear.fire();

		// return the value of deleted
		return deleted;
	}
}
