package gui;

import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.ParseException;
import analyse.WavDatabase;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main class for the AnalyseVoix application.
 * 
 * Made in January - April 2016, as part of the 
 * project DI14 - Analyse de la voix, in Polytech' Tours.
 * 
 * @author Dimitris Kokkonis
 *
 */
public class AnalyseApp extends Application {

	/*---- MAIN WINDOW GLOBAL VARIABLES ----*/

	// window var, our main stage
	private static Stage window;

	// the scene that contains all the main elements
	private static Scene scene;

	// three different layouts to put the labels, the buttons and the graph in
	static VBox windowLayout;
	static HBox buttons;
	static VBox labels;

	// two labels to indicate which files are used
	public static Label file1Name;
	public static Label file2Name;

	// the graph - this is a class variable because we want to 
	// manipulate it through different methods
	public static LineChart<Number,Number> lineChart;

	// the four buttons: graph 1, graph 2, difference 2-1, clear
	public static Button g1;
	public static Button g2;
	public static Button dif;
	public static Button clear;

	// the two files to draw graphs from
	public static File file1 = null;
	public static File file2 = null; 



	/*---- MENUBAR GLOBAL VARIABLES ----*/

	// a menubar to hold everything
	MenuBar menuBar;

	// primary menu buttons
	Menu menuFile;
	Menu menuHelp;
	MenuItem mnGuide;

	// file menu elements
	Menu menuOpen;
	MenuItem mnFile1;
	MenuItem mnFile2;
	MenuItem mnAnalyze;
	Menu menuDelete;
	MenuItem mnDeleteName;
	MenuItem mnDeleteFile;

	// border pane to contain the menubar, and also the main window
	BorderPane windowPane;

	/**
	 * overriden start method, necessary since we're implementing
	 * an extension of the Application class
	 */
	@Override
	public void start(Stage primaryStage) throws ParseException {
		/* ==== MAIN WINDOW ELEMENTS ==== */
		
		// stage initialization & setup
		window = primaryStage;
		window.setTitle("AnalyseVoix");

		// create a new WavDatabase object to get data to draw 
		WavDatabase database = new WavDatabase();

		// graph axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		// axes' labels
		xAxis.setLabel("Frequency (Hz)");
		yAxis.setLabel("Amplitude");
		// initialize the chart, passing the axes as parameters
		lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		// set the chart title
		lineChart.setTitle("Frequency Analysis");

		// filename label initialization & setup
		file1Name = new Label("File 1: " + null);
		file2Name = new Label("File 2: " + null);

		// label container initialization & setup
		labels = new VBox(10);
		labels.getChildren().addAll(file1Name, file2Name);
		labels.setAlignment(Pos.CENTER_LEFT);

		// graph 1 button
		g1 = new Button("Graph 1");
		// g1 disabled by default
		g1.setDisable(true);

		// graph 1 button event handling
		g1.setOnAction(e -> {
			try{
				// call the DrawArray method and draw graph 1
				DrawArray(database.getData(file1), "File 1");

				// disable the button, graph 1 is already drawn
				g1.setDisable(true);
			}catch (FileNotFoundException x){
				// in case of an exception, pop an alert box
				AlertBox.display("ERROR: File not found.");
			}
		});


		// graph 2 button
		g2 = new Button("Graph 2");
		// g2 disabled by default
		g2.setDisable(true);

		// graph 2 button actions
		g2.setOnAction(e -> {
			try{
				// call the DrawArray method, and draw graph 2
				DrawArray(database.getData(file2), "File 2");
				// disable the button, graph 2 is already drawn
				g2.setDisable(true);
			}catch (FileNotFoundException t){
				// in case of an exception, pop an alert box
				AlertBox.display("ERROR: File not found.");
			}
		});


		// difference button
		dif = new Button("Difference 2-1");
		// it's disabled by default
		dif.setDisable(true);

		// difference button event handling
		dif.setOnAction(e -> {
			try{
				// call DrawDifference method, and draw the difference 2-1
				DrawDifference(database.getData(file2), database.getData(file1), "Difference 2-1");
			}catch (FileNotFoundException t){
				// in case of an exception, pop an alert box
				AlertBox.display("ERROR: File not found.");
			}
		});


		// clear button
		clear = new Button("Clear");

		// clear button event handling
		clear.setOnAction(e -> {
			// clear all the data in the chart
			lineChart.getData().clear();
			// re-enable all buttons (if the files permit it)
			if(file1 != null){
				g1.setDisable(false);
			}
			if(file2 != null){
				g2.setDisable(false);
			}
			if(file1 != null && file2 != null){
				dif.setDisable(false);
			}
		});

		// button container initialization & setup
		buttons = new HBox(20);
		buttons.getChildren().addAll(g1, g2, dif, clear);
		buttons.setAlignment(Pos.CENTER);



		/* ==== MENU BAR ==== */

		// menu bar initialization & setup
		menuBar = new MenuBar();

		// File menu initialization & setup
		menuFile = new Menu("File");

		// Open menu (child to File) initialization & setup
		menuOpen = new Menu("Open");

		// File 1 & 2 menu items (children to File)
		// initialization & setup
		mnFile1 = new MenuItem("File 1...");
		mnFile2 = new MenuItem("File 2...");

		// add File 1 & 2 menu items to Open menu
		menuOpen.getItems().addAll(mnFile1, mnFile2);

		// file1 & file2 menu items event handling
		mnFile1.setOnAction(e -> {
			// call the OpenWindow class, passing graph 1 button as a parameter
			OpenWindow.display(g1);

			/* after the OpenWindow class is done,
			 * do some housekeeping:
			 * enable dif button if both files are open
			 * also change the text of the file1Name var */
			if(file1 != null && file2 != null){
				dif.setDisable(false);
			}
			try{
				file1Name.setText("File 1: " + file1.getPath());
			}catch(NullPointerException x){
				file1Name.setText("File 1: null");
			}
		});

		// similar, but this time it's graph 2 button
		mnFile2.setOnAction(e -> {
			OpenWindow.display(g2);
			if(file1 != null && file2 != null){
				dif.setDisable(false);
			}
			try{
				file2Name.setText("File 2: " + file2.getPath());
			}catch(NullPointerException x){
				file2Name.setText("File 2: null");
			}
		});

		// Analyze menu item initialization & setup
		mnAnalyze = new MenuItem("Analyze...");
		mnAnalyze.setOnAction(e -> AnalyzeWindow.display());

		// Delete menu initialization & setup
		menuDelete = new Menu("Delete");

		// Delete Subject & Entry menu items
		// initialization & setup
		mnDeleteName = new MenuItem("Delete Subject...");
		mnDeleteFile = new MenuItem("Delete Entry...");
		menuDelete.getItems().addAll(mnDeleteName, mnDeleteFile);

		// delete buttons event handling
		mnDeleteName.setOnAction(e -> DeleteWindow.displayName());
		mnDeleteFile.setOnAction(e -> DeleteWindow.displayFile());

		// Help menu initialization & setup
		menuHelp = new Menu("Help");
		
		// Guide menu item initialization & setup
		mnGuide = new MenuItem("User's Guide...");
		menuHelp.getItems().add(mnGuide);
		
		// Guide menu item event handling
		mnGuide.setOnAction(e -> {
			try{
				HelpWindow.display();
			}catch(MalformedURLException x){
				AlertBox.display("Malformed url.");
			}
		});

		// add all the submenus to File menu
		menuFile.getItems().addAll(menuOpen, mnAnalyze, menuDelete);

		// add all menus to the menubar
		menuBar.getMenus().addAll(menuFile, menuHelp);

		// code to handle menubar not appearing on top left on OSX
		// windows and linux should be fine by default
		final String os = System.getProperty("os.name");
		if(os != null && os.startsWith("Mac")){
			menuBar.useSystemMenuBarProperty().set(true);
		}

		// border pane to hold the menuBar
		windowPane = new BorderPane();
		windowPane.setTop(menuBar);
		

		// windowLayout initialization & setup
		windowLayout = new VBox(20);
		windowLayout.getChildren().addAll(labels, buttons, lineChart);
		windowLayout.setAlignment(Pos.CENTER);
		
		windowPane.setCenter(windowLayout);

		// scene initialization & setup
		scene = new Scene(windowPane, getScreenWorkingWidth(), getScreenWorkingHeight());

		// set the stage's scene and show the window
		window.setScene(scene);
		window.setMinWidth(600);
		window.setMinHeight(400);
		window.show();
	}


	/**
	 * method to draw a single array
	 * 
	 * we also suppress "unchecked" warnings, those are a side-effect
	 * of the lineChart and its data handling
	 * 
	 * @param t : frequency array
	 * @param name : data name to display at the bottom of the graph
	 */
	@SuppressWarnings("unchecked")
	private void DrawArray(Double[] t, String name)
	{
		// add the data provided by the getGraphData method to the graph
		lineChart.getData().add(getGraphData(t, name));

		// add tooltips
		// iterate through every point of the chart
		for (XYChart.Series<Number, Number> s : lineChart.getData()) {
			for (XYChart.Data<Number, Number> d : s.getData()) {

				// tooltip format: "Frequency: x Hz"
				Tooltip tool = new Tooltip("Frequency: " + d.getXValue().toString() + " Hz");

				// install the tooltip to every node
				Tooltip.install(d.getNode(), tool);
			}
		}

		// after the difference is drawn, disable the button
		dif.setDisable(true);
	}


	/**
	 * method to draw the difference of two arrays
	 * practically it concerns the difference of the two graphs
	 * 
	 * we also suppress "unchecked" warnings, those are a side-effect
	 * of the lineChart and its data handling
	 * 
	 * @param t1 : first frequency array
	 * @param t2 : second frequency array
	 * @param name : data name to display at the bottom of the graph
	 */
	@SuppressWarnings("unchecked")
	private void DrawDifference(Double[] t1, Double[] t2, String name)
	{
		// add the data using getGraphData and difference methods
		lineChart.getData().add(getGraphData(difference(t1, t2), name));

		// add tooltips
		// iterate through every point of the chart
		for (XYChart.Series<Number, Number> s : lineChart.getData()) {
			for (XYChart.Data<Number, Number> d : s.getData()) {

				// tooltip format: "Frequency: x Hz"
				Tooltip tool = new Tooltip("Frequency: " + d.getXValue().toString() + " Hz");

				// install the tooltip to every node
				Tooltip.install(d.getNode(), tool);
			}
		}

		/* disable difference button, it's already displayed
		 * 
		 * disable the two graph buttons as well, no need to display them individually
		 * on top of the difference graph
		 */
		dif.setDisable(true);
		g1.setDisable(true);
		g2.setDisable(true);
	}


	/**
	 * method to transform a Double[] to a XYChart.Series to be
	 * handled by the lineChart
	 * 
	 * this is used in the DrawArray and DrawDifference methods
	 * 
	 * @param t : frequency array
	 * @param name : data name to display at the bottom of the graph
	 * @return XYChart.Series object, basically a lineChart-compatible data set
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private XYChart.Series getGraphData(Double[] t, String name)
	{
		// create a local series var to return
		XYChart.Series series = new XYChart.Series();

		// read up to at most 2000 Hz
		int max = Math.min(2001, t.length);

		// loop to add every element of the array to the series
		for(int i = 0; i < max; i++){
			series.getData().add(new XYChart.Data(i, t[i]));        	
		}

		// set the series' name
		series.setName(name);

		// return the series
		return series;
	}


	/**
	 * method which returns the difference of two arrays
	 * 
	 * the formula is: difference[i] = t2[i] - t1[i]
	 * 
	 * @param t1 : first frequency array
	 * @param t2 : second frequency array
	 * @return Double[], the array which contains the difference
	 */
	private Double[] difference(Double[] t1, Double[] t2)
	{
		// read up to at most 2000 Hz
		int max = Math.min(2001, Math.min(t1.length, t2.length));

		// local var to hold the array to return
		Double[] t = new Double[max];

		// loop to apply t[i] = t1[i] - t2[i], pretty straightforward
		for(int i = 0; i < t.length; i++){
			t[i] = t1[i] - t2[i];
		}

		// return the difference array
		return t;
	}


	/**
	 * method to get the width of the monitor
	 * @return int : width of the monitor
	 */
	private static int getScreenWorkingWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}

	/**
	 * method to get the height of the monitor
	 * @return int : height of the monitor
	 */
	private static int getScreenWorkingHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}

	/**
	 * main method, launches the program
	 * 
	 * @param args : arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
