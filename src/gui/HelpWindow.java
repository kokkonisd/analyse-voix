package gui;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * Help / User's Guide displayer class for the Analyse application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class HelpWindow {

	// stage & scene variables
	private static Stage window;
	private static Scene scene;

	// VBox container variable
	private static VBox layout;

	// html handling variables
	private static WebView guideView;
	private static WebEngine guideEngine;

	// path string variable
	private static String path;

	// width & height variables
	private static int WIDTH;
	private static int HEIGHT;

	/**
	 * method to display the user's guide (in html form)
	 */
	public static void display()
	{
		// stage initialization & setup
		window = new Stage();
		window.setTitle("User's Guide");

		// the window is modal (the main app window is non accessible)
		window.initModality(Modality.APPLICATION_MODAL);

		// WebView & WebEngine initialization & setup
		guideView = new WebView();
		guideEngine = guideView.getEngine();

		// getting the path to the project's root folder 
		path = System.getProperty("user.dir");
		// loading the help page in english
		guideEngine.load("file://" + path + "/html/guide_en.html"); 

		// main layout initialization & setup
		layout = new VBox(20);
		layout.getChildren().addAll(guideView);
		layout.setAlignment(Pos.CENTER);

		// scene initialization & setup
		WIDTH = 800;
		HEIGHT = 400;

		scene = new Scene(layout, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setMinWidth(WIDTH);
		window.setMinHeight(HEIGHT);
		window.showAndWait();
	}

}
