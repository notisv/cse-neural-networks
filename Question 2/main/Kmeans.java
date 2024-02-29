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

import main.DatasetGenerator.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Kmeans extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private static int numberOfClusters;
	
	private static ArrayList<Point> dataset;
	
	private static ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>();
	private static ArrayList<Point> centerPoints = new ArrayList<Point>();
	
	private static ArrayList<ArrayList<Float>> pointFromCenterDistances = new ArrayList<ArrayList<Float>>();
	private static ArrayList<ArrayList<Float>> previousPointFromCenterDistances = new ArrayList<ArrayList<Float>>();
	
	private static ArrayList<Float> totalDispersions = new ArrayList<Float>();
	
	private static Random randomGenerator = new Random();
	
	private static boolean areWvectorsDifferent = true;
	
	private static ArrayList<ArrayList<ArrayList<Point>>> clusters_of_every_run = new ArrayList<ArrayList<ArrayList<Point>>>();
	private static ArrayList<ArrayList<Point>> centers_of_every_run = new ArrayList<ArrayList<Point>>();
	
	private static ArrayList<Point> centerPoints_MIN_DISPERSION;
	private static ArrayList<ArrayList<Point>> clusters_MIN_DISPERSION;
	
	private static float MIN_DISPERSION = Float.MAX_VALUE;
	private static int run_of_MIN_DISPERSION;
	
	// panel generation
	public Kmeans(String windowTitle, int numberOFClusters, int numberOfRuns, String dirString) {
		
		super(windowTitle);
		
		// create jFreeChart dataset
        XYDataset dataset = createJFreeChartDataset();
        
        // create scatter plot
        String plotName = "K Means Clustering Example - " + numberOFClusters + " clusters & " + numberOfRuns + " runs";
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
        	
        	File imageFile = new File("./" + dirString + "/" + numberOFClusters + "clusters_ " + numberOfRuns + "runs.png");
        	OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
        	BufferedImage image = chart.createBufferedImage(1000, 800);
        	ChartUtilities.writeBufferedImageAsPNG(out, image);
        	
        } catch (IOException e) {
        	
        	System.out.println("An error occurred.");
		    e.printStackTrace();
        }
	}
	
	// jFreeChart dataset generation
	private XYDataset createJFreeChartDataset() {
		
		 XYSeriesCollection dataset = new XYSeriesCollection();
		 
		 XYSeries centerSeries = new XYSeries("Centers");
		 
		 for (Point point : centerPoints_MIN_DISPERSION) {
			 
			 centerSeries.add(point.getX(), point.getY());
	     }
		 
		 dataset.addSeries(centerSeries);
		  
		 int i = 0;
		 for (ArrayList<Point> cluster : clusters_MIN_DISPERSION) {
			 
			 XYSeries clusterSeries = new XYSeries("Cluster " + i);
	         for (Point point : cluster) {
	        	 
	        	 clusterSeries.add(point.getX(), point.getY());
	         }
	         
	         i++;
	         dataset.addSeries(clusterSeries);
	    }

	    return dataset;
	}
	
	// choose the center points of our clusters at random 
	private static void initializeData() {
		
		ArrayList<Integer> indexesOfCenterPoints = new ArrayList<>();
		int indexOfCenterPoint;
		
		for (int i = 0; i < numberOfClusters; i++) {
			
			ArrayList<Point> cluster = new ArrayList<>();
			clusters.add(cluster);
			indexOfCenterPoint = randomGenerator.nextInt(dataset.size());
			
			// ensure we dont pick the same center point twice
			while (indexesOfCenterPoints.contains(indexOfCenterPoint)) {
				
				indexOfCenterPoint = randomGenerator.nextInt(dataset.size());
			}
			
			indexesOfCenterPoints.add(indexOfCenterPoint);
            centerPoints.add(dataset.get(indexOfCenterPoint));
		}
	}
	
	public static void main (String[] args) {
		
		numberOfClusters = Integer.valueOf(args[1]);
		
		/*
		// user defines how many teams K means will cluster
		System.out.print("Enter number of teams (3, 5, 7, 9, 11, 13): ");  
		Scanner userInput = new Scanner(System.in);
		numberOfClusters = userInput.nextInt();
		userInput.close();
		*/
		
		// usage: use argument "new" to create a new dataset or use argument "<dataset_name.txt>" to import an existing .txt dataset
		DatasetGenerator dataGen = new DatasetGenerator(args[0]);
		
		//DatasetGenerator dataGen = new DatasetGenerator("new"); // use if you want to create a new dataset
		//DatasetGenerator dataGen = new DatasetGenerator("<dataset_name.txt>"); // use if you want to import an existing dataset
		
		dataset = dataGen.getDataset();
		//dataGen.exportDatasetCSV();
		//dataGen.exportDatasetTXT();
		
		initializeData();
		
		// run k means X times
		int numberOfRuns = Integer.valueOf(args[2]);
		
		String dirString = "output_" + numberOfClusters + "clusters_" + numberOfRuns + "runs";
		File directory = new File(dirString);
		directory.mkdir();
		
		try {
					
			for (int i = 0; i < numberOfRuns; i++) {
				
				FileWriter myWriter = new FileWriter("./" + dirString + "/details_run" + (i+1) + ".txt");
				
				//System.out.println("\n---------- Start Run " + (i+1) + " ----------");
				myWriter.write("\n---------- Start Run " + (i+1) + " ----------\n");
				
				// ArrayList<ArrayList<Float>> pointFromCenterDistances
				// contains the distance between each point and its cluster center, for every cluster
				pointFromCenterDistances = new ArrayList<>();
				for (int m = 0; m < numberOfClusters; m++) {
					
	                ArrayList<Float> distances = new ArrayList<>();
	                pointFromCenterDistances.add(distances);
	            }
				previousPointFromCenterDistances = pointFromCenterDistances;
				
				do {
					
					// compute euclidean distance between a point and each cluster center, for every point
					for (Point point : dataset) {
						
						ArrayList<Float> distancesFromCenters = new ArrayList<>();
						
						// compute euclidean distance between a point and each cluster center
						for (Point centerPoint: centerPoints) {
							
							float distanceFromCluster = (float) Math.sqrt(Math.pow((point.getX() - centerPoint.getX()), 2) + Math.pow((point.getY() - centerPoint.getY()), 2));
							distancesFromCenters.add(distanceFromCluster);
						}
						
						// add the point to the cluster that minimizes the distance from its respective cluster center
						clusters.get(distancesFromCenters.indexOf(Collections.min(distancesFromCenters))).add(point);
						pointFromCenterDistances.get(distancesFromCenters.indexOf(Collections.min(distancesFromCenters))).add(Collections.min(distancesFromCenters));
					}
					
					// set the new cluster center with respect to the mean value of the cluster points, for every cluster
					for (ArrayList<Point> cluster : clusters) {
						
						int clusterIndex = clusters.indexOf(cluster);
						//System.out.println("\nCluster " + clusterIndex + ": Center = " + centerPoints.get(clusterIndex).toString());
						myWriter.write("\nCluster " + clusterIndex + ": Center = " + centerPoints.get(clusterIndex).toString() + "\n");
						
						float sumX = 0;
						float sumY = 0;
						
						int displayLimit = 0;
						for (Point point : cluster) {
							
							//System.out.print(point.toString() + " ");
							myWriter.write(point.toString() + " ");
	                        sumX += point.getX();
	                        sumY += point.getY();
	                        
	                        displayLimit++;
	                        if (displayLimit == 10) {
	                        	
	                        	myWriter.write("\n");
	                        	displayLimit = 0;
	                        }
	                        
						}
						//System.out.println();
						//System.out.println("SumX of cluster " + clusterIndex + ": " + sumX);
						//System.out.println("SumY of cluster " + clusterIndex + ": " + sumY);
						//System.out.println("Average of cluster " + clusterIndex + ": " + sumX / cluster.size() + "," + sumY / cluster.size());
						
						myWriter.write("\n");
						//myWriter.write("SumX of cluster " + clusterIndex + ": " + sumX + "\n");
						//myWriter.write("SumY of cluster " + clusterIndex + ": " + sumY + "\n");
						//myWriter.write("Average of cluster " + clusterIndex + ": " + sumX / cluster.size() + "," + sumY / cluster.size() + "\n");
						
						
						// set the new cluster center
						centerPoints.set(clusterIndex, new Point(sumX / cluster.size(), sumY / cluster.size()));
	                    //System.out.println("Cluster " + clusterIndex + ": New center = " + centerPoints.get(clusterIndex).toString());
	                    myWriter.write("Cluster " + clusterIndex + ": New center = " + centerPoints.get(clusterIndex).toString() + "\n");
					}
					
					
					// are the w_j vectors different between 2 runs?
					// if yes then start a new iteration
					// if no then end the run
					for (int k = 0; k < pointFromCenterDistances.size(); k++) {
						
	                    for (int l = 0; l < pointFromCenterDistances.get(k).size(); l++) {
	                    	
	                        if (previousPointFromCenterDistances.get(k).get(l).equals(pointFromCenterDistances.get(k).get(l))) {
	                        	
	                        	areWvectorsDifferent = false;
	                            break;
	                        }
	                    }
	                    
	                    if (!areWvectorsDifferent) {
	                    	
	                    	break;
	                    }
	                }
					
					// if the w_j vectors are different then prepare for a new iteration
					if (areWvectorsDifferent) {
						
						previousPointFromCenterDistances = pointFromCenterDistances;
					}
					
					clusters_of_every_run.add(clusters);
					centers_of_every_run.add(centerPoints);
					
					// clear the clusters after every iteration
					if (i != (numberOfRuns-1)) {
						
	                    for (ArrayList<Point> cluster : clusters) {
	                    	
	                        cluster.clear();
	                    }
	                }
					
				// if the w_j vectors are different between 2 runs then start a new iteration, else end
				} while (areWvectorsDifferent);
				
				// set the flag to true for the next run
				areWvectorsDifferent = true;
				
				// compute the total dispersion by adding the dispersions of each cluster
				float totalDispersion = 0;
				for (int d = 0; d < clusters.size(); d++) {
					
					float clusterDispersion = 0;
					for (Float clusterDistances : pointFromCenterDistances.get(d)) {
						clusterDispersion += clusterDistances;
	                }
					
					totalDispersion += clusterDispersion;
				}
				
				// ArrayList<Float> totalDispersions holds the total cluster dispersion for each run
				totalDispersions.add(totalDispersion);
				
				//System.out.println("\nRun " + (i+1) + " dispersion = " + totalDispersion);
				myWriter.write("\nRun " + (i+1) + " dispersion = " + totalDispersion + "\n");
				
				//System.out.println("\n---------- End Run " + (i+1) + " ----------");
				myWriter.write("\n---------- End Run " + (i+1) + " ----------\n");
				
				myWriter.close();
				
				// keep the run with the minimum dispersion
				if (totalDispersion < MIN_DISPERSION) {
					
					//System.out.print((i+1) + " " + totalDispersion + " " + MIN_DISPERSION + "\n");
					MIN_DISPERSION = totalDispersion;
					run_of_MIN_DISPERSION = (i+1);
					clusters_MIN_DISPERSION = clusters_of_every_run.get(i);
					centerPoints_MIN_DISPERSION = centers_of_every_run.get(i);					
				}
			}
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		
		/*
		// print the coordinates of the final cluster centers
		System.out.println("\n---------- Final centroid coordinates ----------");
		for (int x = 0; x < centerPoints_MIN_DISPERSION.size(); x++) {
			
			System.out.println("Cluster " + (x+1) + " center = " + centerPoints_MIN_DISPERSION.get(x));
		}
		
		// print the total dispersion across all runs
		System.out.println("\n---------- Dispersions across all runs ----------");
		for (int z = 0; z < totalDispersions.size(); z++) {
			
			System.out.println("Run " + (z+1) + " dispersion = " + totalDispersions.get(z));
		}
		*/
		
		// create final_centroids_and_dispersions.txt
		try {
			
			FileWriter myWriter = new FileWriter("./" + dirString + "/final_centroids_dispersions.txt");
			
			myWriter.write("\n---------- K means clustering example ----------\n");
			
			myWriter.write("Number of clusters = " + clusters_MIN_DISPERSION.size() + "\n");
			myWriter.write("Number of runs = " + numberOfRuns + "\n");
			
			myWriter.write("\n---------- Final centroid coordinates ----------\n");
			for (int x = 0; x < centerPoints_MIN_DISPERSION.size(); x++) {
				
				myWriter.write("Cluster " + (x+1) + " center = " + centerPoints_MIN_DISPERSION.get(x) + "\n");
			}
			
			myWriter.write("\n---------- Dispersions across all runs ----------\n");
			for (int z = 0; z < totalDispersions.size(); z++) {
				
				myWriter.write("Run " + (z+1) + " dispersion = " + totalDispersions.get(z) + "\n");
			}
			myWriter.write("\nMinimum dipersion occured on run " + run_of_MIN_DISPERSION + " = " + MIN_DISPERSION);
			
			FileWriter myOtherWriter = new FileWriter("./" + dirString + "/pd_minDispersion_" + clusters_MIN_DISPERSION.size() + "clusters_" + numberOfRuns + "runs.txt");
			myOtherWriter.write("clusters,min-dispersion\n");
			myOtherWriter.write(clusters_MIN_DISPERSION.size() + "," + MIN_DISPERSION + "\n");
			
			myWriter.close();
			myOtherWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		
		System.out.print("\nPlease check the created files for the results...");  
		
		// create application window
		Kmeans myKmeansWindow = new Kmeans("Question 2 - K Means Clustering", numberOfClusters, numberOfRuns, dirString);
		myKmeansWindow.setSize(1000, 800);
		myKmeansWindow.setLocationRelativeTo(null);
		myKmeansWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		myKmeansWindow.setVisible(true);
		
		// create message dialog
		JOptionPane.showMessageDialog(null, "Please check the created files for the results", "Question 2 - K Means Clustering", JOptionPane.PLAIN_MESSAGE);
	}
}
