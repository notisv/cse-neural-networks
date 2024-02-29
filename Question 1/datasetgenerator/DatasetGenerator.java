package datasetgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DatasetGenerator {
	
	private ArrayList<Point> dataset;
	private ArrayList<Point> trainingSet;
	private ArrayList<Point> testSet;
	
	// dataset
	public DatasetGenerator() {
		
		
	}
	
	public DatasetGenerator(int numberOfPoints, float x1rangeMin, float x1rangeMax, float x2rangeMin, float x2rangeMax) {
		
		populateDataset(numberOfPoints, x1rangeMin, x1rangeMax, x2rangeMin, x2rangeMax);
	}
	
	private void populateDataset(int numberOfPoints, float x1rangeMin, float x1rangeMax, float x2rangeMin, float x2rangeMax) {
		
		dataset = new ArrayList<Point>();
		for(int i = 0; i < numberOfPoints; i++) {
			
			dataset.add(new Point(x1rangeMin, x1rangeMax, x2rangeMin, x2rangeMax));
		}
	}
	
	public ArrayList<Point> getDataset(){
		
		return dataset;
	}
	
	public void printDataset() {
		
		System.out.println("Dataset:");
		for(Point point: dataset) {
			
			System.out.println(point + "," + point.getCategory());
		}
		System.out.println();
	}
	
	public void exportDatasetTXT() {
		
		try {
			
			FileWriter myWriter = new FileWriter("dataset.txt");
			for(int i = 0; i < dataset.size(); i++) {
				
				myWriter.write(dataset.get(i).toString() + "," + dataset.get(i).getCategory() + "\n");
			}
			myWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	// training set
	public void createTrainingSet() {
		
		// first half of the dataset becomes the training set 
		trainingSet = new ArrayList<Point>();
		for(int i = 0; i < (dataset.size() / 2); i++) {
			
			trainingSet.add(dataset.get(i).createDeepCopy());
		}
	}
	
	public ArrayList<Point> getTrainingSet(){
		
		return trainingSet;
	}
	
	public void printTrainingSet() {
		
		System.out.println("Training Set:");
		for(Point point: trainingSet) {
			
			System.out.println(point + "," + point.getCategory());
		}
		System.out.println();
	}
	
	public void exportTrainingSetTXT() {
		
		try {
			
			FileWriter myWriter = new FileWriter("training_set.txt");
			for(int i = 0; i < trainingSet.size(); i++) {
				
				myWriter.write(trainingSet.get(i).toString() + "," + trainingSet.get(i).getCategory() + "\n");
			}
			myWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	// test set	
	public void createTestSet() {
		
		// second half of the dataset becomes the test set
		testSet = new ArrayList<Point>();
		for(int i = (dataset.size() / 2); i < dataset.size(); i++) {
			
			testSet.add(dataset.get(i).createDeepCopy());
		}
	}
	
	public ArrayList<Point> getTestSet(){
		
		return testSet;
	}
	
	public void printTestSet() {
		
		System.out.println("Test Set:");
		for(Point point: testSet) {
			
			System.out.println(point + "," + point.getCategory());
		}
		System.out.println();
	}
	
	public void exportTestSetTXT() {
		
		try {
			
			FileWriter myWriter = new FileWriter("test_set.txt");
			for(int i = 0; i < testSet.size(); i++) {
				
				myWriter.write(testSet.get(i).toString() + "," + testSet.get(i).getCategory() + "\n");
			}
			myWriter.close();
			
		} catch (IOException e){
			
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	// noise introduction
	public void introduceNoiseToTrainingSet(float probability) {
		
		for(Point point: this.trainingSet) {
			
			ArrayList<String> categories = new ArrayList<>();
			categories.add("C1");
			categories.add("C2");
			categories.add("C3");
			categories.add("C4");
			
			if (new Random().nextFloat() <= probability) {
				
				categories.remove(point.getCategory());
				
				int newCategoryIndex = new Random().nextInt(categories.size());
				String newCategory = categories.get(newCategoryIndex);
				
				//System.out.println("Point " + point + " changed category from " + point.getCategory() + " to " + newCategory);
				point.setCategory(newCategory);
				
				//System.out.print("Remaining categories: ");
				//System.out.println(categories);
			}
		}
	}

	// imports
	public void importDatasets() {
		
		// datasets must have the names dataset.txt, training_set.txt, test_set.txt
		// and be located in the root project directory
		
		importDataset();
		importTrainingSet();
		importTestSet();
	}
	
	private void importDataset() {
		
		this.dataset = new ArrayList<Point>();
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader("dataset.txt"));
			String currentLine;
			
			while ((currentLine = bufferedReader.readLine()) != null) {
				
				String[] pointArgs = currentLine.split(",");
				this.dataset.add(new Point(Float.valueOf(pointArgs[0]), Float.valueOf(pointArgs[1]), pointArgs[2]));
			}
			
			bufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	private void importTrainingSet() {
		
		this.trainingSet = new ArrayList<Point>();
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader("training_set.txt"));
			String currentLine;
			
			while ((currentLine = bufferedReader.readLine()) != null) {
				
				String[] pointArgs = currentLine.split(",");
				this.trainingSet.add(new Point(Float.valueOf(pointArgs[0]), Float.valueOf(pointArgs[1]), pointArgs[2]));
			}
			
			bufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	private void importTestSet() {
		
		this.testSet = new ArrayList<Point>();
		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader("test_set.txt"));
			String currentLine;
			
			while ((currentLine = bufferedReader.readLine()) != null) {
				
				String[] pointArgs = currentLine.split(",");
				this.testSet.add(new Point(Float.valueOf(pointArgs[0]), Float.valueOf(pointArgs[1]), pointArgs[2]));
			}
			
			bufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	public static void main (String[] args) {
		
		// randomly create 8000 points (x1,x2) inside the square [-1,1]x[-1,1]
		// training set: 4000 points - control set: 4000 points
		DatasetGenerator myDataset = new DatasetGenerator(8000, -1, 1, -1, 1);
		myDataset.createTrainingSet();
		myDataset.createTestSet();
		myDataset.introduceNoiseToTrainingSet(0.1f);
		
		myDataset.exportDatasetTXT();
		myDataset.exportTrainingSetTXT();
		myDataset.exportTestSetTXT();
		/*
		DatasetGenerator myDataset = new DatasetGenerator(20, -1, 1, -1, 1);
		//myDataset.printDataset();
		myDataset.exportDatasetTXT();
		
		myDataset.createTrainingSet();
		//myDataset.printTrainingSet();
		
		myDataset.createTestSet();
		//myDataset.printTestSet();
		myDataset.exportTestSetTXT();
		
		myDataset.introduceNoiseToTrainingSet(0.1f);
		//myDataset.printTrainingSet();
		myDataset.exportTrainingSetTXT();
		*/
		/*
		DatasetGenerator myDataset = new DatasetGenerator();
		myDataset.importDatasets();
		myDataset.printDataset();
		myDataset.printTrainingSet();
		myDataset.printTestSet();
		*/
	}
}