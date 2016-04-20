package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generic alert box class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class AlertBox {
	
	// stage & scene variables
	private static Stage window;
	private static Scene scene;
	
	// label variable
	private static Label message;
	
	// button variable
	private static Button btnOk;
	
	// main layout variable
	private static VBox layout;
	
	// window width & height variables
	static int WIDTH;
	static int HEIGHT;
	
	
	/**
	 * method to handle the communication
	 * of messages or errors to the user
	 * 
	 * @param msg : String to show in the alert box
	 */
	public static void display(String msg)
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("Alert");
		
		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);
		
		// message initialization
		message = new Label(msg);
		
		// button initialization & setup
		btnOk = new Button("Ok");
		btnOk.setOnAction(e -> window.close());
		
		// layout initialization & setup
		layout = new VBox(20);
		layout.getChildren().addAll(message, btnOk);
		layout.setAlignment(Pos.CENTER);
		
		// scene initialization & setup
		WIDTH = msg.length()*6 + 10;
		HEIGHT = 100;
		
		scene = new Scene(layout, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setMinWidth(WIDTH);
		window.setMinHeight(HEIGHT);
		window.showAndWait();
	}
}
