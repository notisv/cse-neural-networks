package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import datasetgenerator.DatasetGenerator;
import datasetgenerator.Point;

public class MLPDriver extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static String BUILD_VERSION = "v1.0";
	
	private static DatasetGenerator myDataset; 	// static because it doesnt change
	private static MLP myMLP;							// not static bc we want to be able to handle multiple MLPs for testing
	
	private static ArrayList<ArrayList<Point>> expectedCategories;
	private static ArrayList<ArrayList<Point>> actualCategories;
	
	public static void createDatasets() {
		
		// randomly create 8000 points (x1,x2) inside the square [-1,1]x[-1,1]
		// training set: 4000 points - control set: 4000 points
		myDataset = new DatasetGenerator(8000, -1, 1, -1, 1);
		myDataset.createTrainingSet();
		myDataset.createTestSet();
		myDataset.introduceNoiseToTrainingSet(0.1f);
		
		myDataset.exportDatasetTXT();
		myDataset.exportTrainingSetTXT();
		myDataset.exportTestSetTXT();
	}
	
	public static void importDatasets() {
		
		// import datasets from file
		myDataset = new DatasetGenerator();
		myDataset.importDatasets();
		
		//myDataset.printDataset();
		//myDataset.printTrainingSet();
		//myDataset.printTestSet();
	}
	
	public MLPDriver(String windowTitle) {
		
		super(windowTitle);	
	}
	
	// usage: MLPDriver.java <number of hidden layers>
	public void createAndTrainMLP(DatasetGenerator dataset, int hiddenLayers) {
		
		myMLP = new MLP(dataset, hiddenLayers);
		
		System.out.print("\nStarted training...");
		myMLP.train();
		System.out.println("Finished training.");
		
		System.out.print("Started Testing...");
		myMLP.test();
		System.out.println("Finished Testing.");
		
		myMLP.printStats();
		//myMLP.printNetwork();
	}
	
	// usage: MLPDriver.java <number of hidden layers> <H1 neurons> <H2 neurons> <tanh or relu> <learning rate> <batch size>
	public void createAndTrainMLP(DatasetGenerator dataset, int hiddenLayers, int h1neurons, int h2neurons, String activationFunction, Double learnRate, int batchSize) {
		
		myMLP = new MLP(dataset, hiddenLayers, h1neurons, h2neurons, activationFunction, learnRate, batchSize);
		
		System.out.println("\nStarted training...");
		myMLP.train();
		System.out.println("Finished training.");
		
		System.out.print("\nStarted Testing...");
		myMLP.test();
		System.out.println("Finished Testing.");
		
		myMLP.printStats();
		//myMLP.printNetwork();
		
		//String mlpInfo = "hidden layers = %d\nh1 neurons = %d\nh2 neurons = %d\ntransfer function = %s\nlearning rate = %.3f\nbatch size = %d\n\n";
		//System.out.printf(mlpInfo, hiddenLayers, h1neurons, h2neurons, activationFunction, learnRate, batchSize);
	}
	
	// usage: MLPDriver.java <number of hidden layers> <H1 neurons> <H2 neurons> <H3 neurons> <tanh or relu> <learning rate> <batch size>
	public void createAndTrainMLP(DatasetGenerator dataset, int hiddenLayers, int h1neurons, int h2neurons, int h3neurons, String activationFunction, Double learnRate, int batchSize) {
		
		myMLP = new MLP(dataset, hiddenLayers, h1neurons, h2neurons, h3neurons, activationFunction, learnRate, batchSize);
		
		System.out.println("\nStarted training...");
		myMLP.train();
		System.out.println("Finished training.");
		
		System.out.print("\nStarted Testing...");
		myMLP.test();
		System.out.println("Finished Testing.");
		
		myMLP.printStats();
		//myMLP.printNetwork();
		
		//String mlpInfo = "hidden layers = %d\nh1 neurons = %d\nh2 neurons = %d\nh3 neurons = %d\ntransfer function = %s\nlearning rate = %.3f\nbatch size = %d\n\n";
		//System.out.printf(mlpInfo, hiddenLayers, h1neurons, h2neurons, h3neurons, activationFunction, learnRate, batchSize);
	}
	
	// panel generation
	public void populateApplicationWindow_expected() {
		
		// create jFreeChart dataset
		XYDataset dataset = createJFreeChartDataset_expected();
		
		// create scatter plot
	    String plotName = "MLP Test Set Categorization Plot - Expected";
	    JFreeChart chart = ChartFactory.createScatterPlot(
        		plotName, "x axis", "y axis", dataset, PlotOrientation.VERTICAL, true, true, false);
	    
	    // change background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 230, 200));
        
        // create panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        
        // export plot to png
        try {
        	
        	File imageFile = new File("expected_plot.png");
        	OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
        	BufferedImage image = chart.createBufferedImage(1000, 800);
        	ChartUtilities.writeBufferedImageAsPNG(out, image);
        	
        } catch (IOException e) {
        	
        	System.out.println("An error occurred.");
		    e.printStackTrace();
        }
	}
	
	// jFreeChart dataset generation
	private XYDataset createJFreeChartDataset_expected() {
		
		expectedCategories = myMLP.getExpectedCategories();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		XYSeries cat1 = new XYSeries("Category 1");
		for (Point point : expectedCategories.get(0)) {
			 
			 cat1.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat1);
		 
		 XYSeries cat2 = new XYSeries("Category 2");
		 for (Point point : expectedCategories.get(1)) {
			 
			 cat2.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat2);
			 
		 XYSeries cat3 = new XYSeries("Category 3");
		 for (Point point : expectedCategories.get(2)) {
				 
			 cat3.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat3);
		 
		 XYSeries cat4 = new XYSeries("Category 4");
		 for (Point point : expectedCategories.get(3)) {
				 
			 cat4.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat4);
		 
		 return dataset;
	}
	
	// panel generation
	public void populateApplicationWindow_actual() {
		
		// create jFreeChart dataset
		XYDataset dataset = createJFreeChartDataset_actual();
		
		// create scatter plot
	    String plotName = "MLP Test Set Categorization Plot - Actual";
	    JFreeChart chart = ChartFactory.createScatterPlot(
        		plotName, "x axis", "y axis", dataset, PlotOrientation.VERTICAL, true, true, false);
	    
	    // change background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 230, 200));
        
        // create panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        
        // export plot to png
        try {
        	
        	File imageFile = new File("actual_plot.png");
        	OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
        	BufferedImage image = chart.createBufferedImage(1000, 800);
        	ChartUtilities.writeBufferedImageAsPNG(out, image);
        	
        } catch (IOException e) {
        	
        	System.out.println("An error occurred.");
		    e.printStackTrace();
        }
	}
	
	// jFreeChart dataset generation
	private XYDataset createJFreeChartDataset_actual() {
		
		actualCategories = myMLP.getActualCategories();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		XYSeries cat1 = new XYSeries("Category 1");
		for (Point point : actualCategories.get(0)) {
			 
			 cat1.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat1);
		 
		 XYSeries cat2 = new XYSeries("Category 2");
		 for (Point point : actualCategories.get(1)) {
			 
			 cat2.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat2);
			 
		 XYSeries cat3 = new XYSeries("Category 3");
		 for (Point point : actualCategories.get(2)) {
				 
			 cat3.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat3);
		 
		 XYSeries cat4 = new XYSeries("Category 4");
		 for (Point point : actualCategories.get(3)) {
				 
			 cat4.add(point.getCoordinates()[0], point.getCoordinates()[1]);
		 }
		 dataset.addSeries(cat4);
		 
		 return dataset;
	}
	
	public static void main (String[] args) {
		
		System.out.println("========== Multi Layer Perceptron Driver " + BUILD_VERSION + " ==========\n");
		
		//createDatasets();
		importDatasets();
		
		MLPDriver myDriver = new MLPDriver("Question 1 - Multi Layer Perceptron");
		//myDriver.createAndTrainMLP(myDataset, 2);
		//myDriver.createAndTrainMLP(myDataset, 3);
		
		if (Integer.valueOf(args[0]) == 2) {
			
			int h1neurons = Integer.valueOf(args[1]);
			int h2neurons = Integer.valueOf(args[2]);
			String transferFunction = args[3];
			Double learningRate = Double.valueOf(args[4]);
			int batchSize = Integer.valueOf(args[5]);
			
			myDriver.createAndTrainMLP(myDataset, 2, h1neurons, h2neurons, transferFunction, learningRate, batchSize);
		}
		
		if (Integer.valueOf(args[0]) == 3) {
			
			int h1neurons = Integer.valueOf(args[1]);
			int h2neurons = Integer.valueOf(args[2]);
			int h3neurons = Integer.valueOf(args[3]);
			String transferFunction = args[4];
			Double learningRate = Double.valueOf(args[5]);
			int batchSize = Integer.valueOf(args[6]);
			
			myDriver.createAndTrainMLP(myDataset, 3, h1neurons, h2neurons, h3neurons, transferFunction, learningRate, batchSize);
		}
		
		myDriver.populateApplicationWindow_expected();
		myDriver.setSize(1000, 800);
		myDriver.setLocationRelativeTo(null);
		myDriver.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myDriver.setVisible(true);
		
		MLPDriver myOtherDriver = new MLPDriver("Question 1 - Multi Layer Perceptron");
		myOtherDriver.populateApplicationWindow_actual();
		myOtherDriver.setSize(1000, 800);
		myOtherDriver.setLocationRelativeTo(null);
		myOtherDriver.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myOtherDriver.setVisible(true);	
	}
}