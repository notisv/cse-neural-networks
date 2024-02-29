package main;

import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DatasetGenerator {

	private ArrayList<Point> dataset;
	
	public static class Point {
		
		private float x;
		private float y;
		
		public Point(float x, float y) {
			
			this.x = x;
			this.y = y;
		}
		
		public Point(float xRangeMin, float xRangeMax, float yRangeMin, float yRangeMax) {
		
			x = xRangeMin + (new Random().nextFloat() * (xRangeMax - xRangeMin));
			y = yRangeMin + (new Random().nextFloat() * (yRangeMax - yRangeMin));
		}

		public float getX() {
			
			return this.x;
		}
		
		public float getY() {
			
			return this.y;
		}
	
		public Point createDeepCopy() {
			
			return new Point(this.x, this.y);
		}
		
		public String toString() {
			
			return (String.valueOf(x) + "," + String.valueOf(y));
		}
	}
	
	// dataset
	public DatasetGenerator(String args) {
		
		if (args.equals("new")) {
			
			populateDataset(); // create new dataset
			
		} else {
			
			// import dataset from file
			importDatasetTXT(args);
		}
		
	}
	
	private void populateDataset() {
		
		// this can be done more elegantly following proper OOP techniques
		dataset = new ArrayList<Point>();
		dataset.addAll(populateSquare1());
		dataset.addAll(populateSquare2());
		dataset.addAll(populateSquare3());
		dataset.addAll(populateSquare4());
		dataset.addAll(populateSquare5());
		dataset.addAll(populateSquare6());
		dataset.addAll(populateSquare7());
		dataset.addAll(populateSquare8());
		dataset.addAll(populateSquare9());
		dataset.addAll(populateSquare10());
	}
	
	private ArrayList<Point> populateSquare1(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(0.75f, 1.25f, 0.75f, 1.25f));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare2(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(0, 0.5f, 0, 0.5f));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare3(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(0, 0.5f, 1.5f, 2));
		}
		return square;
	}

	private ArrayList<Point> populateSquare4(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(1.5f, 2, 0, 0.5f));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare5(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(1.5f, 2, 1.5f, 2));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare6(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 75; i++) {
			
			square.add(new Point(0.6f, 0.8f, 0, 0.4f));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare7(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 75; i++) {
			
			square.add(new Point(0.6f, 0.8f, 1.6f, 2));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare8(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 75; i++) {
			
			square.add(new Point(1.2f, 1.4f, 0, 0.4f));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare9(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 75; i++) {
			
			square.add(new Point(1.2f, 1.4f, 1.6f, 2));
		}
		return square;
	}
	
	private ArrayList<Point> populateSquare10(){
		
		ArrayList<Point> square = new ArrayList<Point>();
		for(int i = 0; i < 150; i++) {
			
			square.add(new Point(0, 2, 0, 2));
		}
		return square;
	}
	
	public ArrayList<Point> getDataset(){
		
		return dataset;
	}
	
	public void printDataset() {
		
		System.out.println("Dataset:");
		for(int i = 0; i < dataset.size(); i++) {
			
			System.out.println("" + (i+1) + ": " + dataset.get(i));
		}
		System.out.println();
	}
	
	public void exportDatasetCSV() {
		
		try {
			
			//FileWriter myWriter = new FileWriter("dataset.txt");
			FileWriter myWriter = new FileWriter("dataset.csv");
			myWriter.write("x value,y value\n");
			for(int i = 0; i < dataset.size(); i++) {
				
				myWriter.write(dataset.get(i).toString() + "\n");
			}
			myWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	public void exportDatasetTXT() {
		
		try {
			
			FileWriter myWriter = new FileWriter("dataset.txt");
			for(int i = 0; i < dataset.size(); i++) {
				
				myWriter.write(dataset.get(i).toString() + "\n");
			}
			myWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	public void importDatasetTXT(String txtFilename) {
		
		dataset = new ArrayList<Point>();
		
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(txtFilename));
			String currentLine;
			
			while ((currentLine = bufferedReader.readLine()) != null) {
				
				dataset.add(new Point(Float.valueOf(currentLine.split(",")[0]), Float.valueOf(currentLine.split(",")[1])));
			}
			
			bufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	public static void main (String[] args) {
		
		DatasetGenerator myDataset = new DatasetGenerator("new");
		//myDataset.printDataset();
		myDataset.exportDatasetCSV();
		myDataset.exportDatasetTXT();
		/*
		DatasetGenerator myDataset = new DatasetGenerator("datasetImportTest.txt");
		myDataset.printDataset();
		myDataset.exportDatasetTXT();
		*/
	}
}
