package main;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import datasetgenerator.DatasetGenerator;
import datasetgenerator.Point;

import neurons.Neuron;

import transferfunctions.TransferFunction;
import transferfunctions.HyperbolicFunction;
import transferfunctions.ReluFunction;
import transferfunctions.SigmoidalFunction;

public class MLP {
	
	private static int D = 2;			// number of inputs (in H1) - our inputs are x1, x2
	private static int K = 4;			// number of categories
	private static int H1;			// number of neurons in H1
	private static int H2;			// number of neurons in H2
	private static int H3; 			// number of neurons in H3
	
	private static double trainingThreshold = 0.01; // difference between two training errors
	private static double worthiness = 75; // generalization ability at  which the MLP is deemed worthy
	
	// tanh or relu
	private static TransferFunction transferFunction; // HyperbolicFunction() or ReluFunction()
	
	private static int B;				// batch size - 1 is linear update, N = 4000 in our case is team update
	private static int numHiddenLayers;	// number of hidden layers in network
	private static double learningRate;
	
	private double currentEpochError = 0;
	private double lastEpochError = 0;
	
	private int correctExamples = 0;
	private int wrongExamples = 0;
	private int netOutputCategories[] = new int[K];
	
	private ArrayList<Neuron[]> layers = new ArrayList<Neuron[]>();
	private Neuron[] neuronsH1; 		// hidden layer 1
	private Neuron[] neuronsH2; 		// hidden layer 2
	private Neuron[] neuronsH3; 		// hidden layer 3
	private Neuron[] neuronsOutput; 	// output layer
	
	private static ArrayList<Point> trainingSet;
	private static ArrayList<Point> testSet;
	
	private ArrayList<Point> expectedCategory1 = new ArrayList<Point>();
	private ArrayList<Point> expectedCategory2 = new ArrayList<Point>();
	private ArrayList<Point> expectedCategory3 = new ArrayList<Point>();
	private ArrayList<Point> expectedCategory4 = new ArrayList<Point>();
	
	private ArrayList<Point> actualCategory1 = new ArrayList<Point>();
	private ArrayList<Point> actualCategory2 = new ArrayList<Point>();
	private ArrayList<Point> actualCategory3 = new ArrayList<Point>();
	private ArrayList<Point> actualCategory4 = new ArrayList<Point>();
	
	public ArrayList<ArrayList<Point>> getExpectedCategories(){
		
		ArrayList<ArrayList<Point>> expectedCategories = new ArrayList<ArrayList<Point>>();
		expectedCategories.add(expectedCategory1);
		expectedCategories.add(expectedCategory2);
		expectedCategories.add(expectedCategory3);
		expectedCategories.add(expectedCategory4);
		return expectedCategories;
	}
	
	public ArrayList<ArrayList<Point>> getActualCategories(){
		
		ArrayList<ArrayList<Point>> actualCategories = new ArrayList<ArrayList<Point>>();
		actualCategories.add(actualCategory1);
		actualCategories.add(actualCategory2);
		actualCategories.add(actualCategory3);
		actualCategories.add(actualCategory4);
		return actualCategories;
	}
	
	public MLP(DatasetGenerator datasets, int hiddenLayers) {
		
		trainingSet = datasets.getTrainingSet();
		testSet = datasets.getTestSet();
		
		numHiddenLayers = hiddenLayers;
		H1 = 6;
		H2 = 6;
		H3 = 6;
		transferFunction = new HyperbolicFunction();
		B = 40;
		learningRate = 0.006;
		
		initializeHiddenLayers(hiddenLayers);
	}
	
	public MLP(DatasetGenerator datasets, int hiddenLayers, int h1neurons, int h2neurons, String activationFunc, Double learnRate, int batchSize) {
		
		trainingSet = datasets.getTrainingSet();
		testSet = datasets.getTestSet();
		
		numHiddenLayers = hiddenLayers;
		H1 = h1neurons;
		H2 = h2neurons;
		if (activationFunc.equals("tanh")) { transferFunction = new HyperbolicFunction(); }
		if (activationFunc.equals("relu")) { transferFunction = new ReluFunction(); }
		learningRate = learnRate;
		B = batchSize;
		
		printMLPinfo(hiddenLayers, h1neurons, h2neurons, activationFunc, learnRate, batchSize);
		
		initializeHiddenLayers(hiddenLayers);
	}
	
	public MLP(DatasetGenerator datasets, int hiddenLayers, int h1neurons, int h2neurons, int h3neurons, String activationFunc, Double learnRate, int batchSize) {
		
		trainingSet = datasets.getTrainingSet();
		testSet = datasets.getTestSet();
		
		numHiddenLayers = hiddenLayers;
		H1 = h1neurons;
		H2 = h2neurons;
		H3 = h3neurons;
		if (activationFunc.equals("tanh")) { transferFunction = new HyperbolicFunction(); }
		if (activationFunc.equals("relu")) { transferFunction = new ReluFunction(); }
		learningRate = learnRate;
		B = batchSize;
		
		printMLPinfo(hiddenLayers, h1neurons, h2neurons, h3neurons, activationFunc, learnRate, batchSize);
		
		initializeHiddenLayers(hiddenLayers);
	}
	
	public void printMLPinfo(int hiddenLayers, int h1neurons, int h2neurons, String activationFunc, Double learnRate, int batchSize) {
		
		System.out.println("---------- New MLP created ----------");
		String mlpInfo = "hidden layers = %d\nh1 neurons = %d\nh2 neurons = %d\ntransfer function = %s\nlearning rate = %.3f\nbatch size = %d\n\n";
		System.out.printf(mlpInfo, hiddenLayers, h1neurons, h2neurons, activationFunc, learnRate, batchSize);
	}
	
	public void printMLPinfo(int hiddenLayers, int h1neurons, int h2neurons, int h3neurons, String activationFunc, Double learnRate, int batchSize) {
		
		System.out.println("---------- New MLP created ----------");
		String mlpInfo = "hidden layers = %d\nh1 neurons = %d\nh2 neurons = %d\nh3 neurons = %d\ntransfer function = %s\nlearning rate = %.3f\nbatch size = %d\n\n";
		System.out.printf(mlpInfo, hiddenLayers, h1neurons, h2neurons, h3neurons, activationFunc, learnRate, batchSize);
	}
	
	private void initializeHiddenLayers(int hiddenLayers) {
		
		System.out.println("---------- Initializing MLP ----------");
		if (hiddenLayers == 2) { initialize_MLP_2HL(); }
		if (hiddenLayers == 3) { initialize_MLP_3HL(); }
	}
	
	private void initialize_MLP_2HL() {
		
		neuronsH1 = initHiddenLayer(H1,D);
		layers.add(neuronsH1);
		
		neuronsH2 = initHiddenLayer(H2,H1);
		layers.add(neuronsH2);
		
		neuronsOutput = initOutputLayer(K, H2);
		layers.add(neuronsOutput);
	}
	
	private void initialize_MLP_3HL() {
		
		neuronsH1 = initHiddenLayer(H1,D);
		layers.add(neuronsH1);
		
		neuronsH2 = initHiddenLayer(H2,H1);
		layers.add(neuronsH2);
		
		neuronsH3 = initHiddenLayer(H3,H2);
		layers.add(neuronsH3);
		
		neuronsOutput = initOutputLayer(K, H3);
		layers.add(neuronsOutput);	
	}

	private Neuron[] initHiddenLayer(int numOfNeurons, int numOfInputs) {
		
		Neuron [] neuronsList = new Neuron[numOfNeurons];
		
		for (int i = 0 ; i < numOfNeurons ; i++) {
			
			neuronsList[i] = new Neuron(numOfInputs, transferFunction);
			
		}
		
		System.out.println("Hidden Layer : " + numOfNeurons + " neurons, " + numOfInputs + " inputs");
		return neuronsList;
	}
	
	private Neuron[] initOutputLayer(int numOfNeurons, int numOfInputs) {
		
		Neuron [] neuronsList = new Neuron[numOfNeurons];
		
		for (int i = 0 ; i < numOfNeurons ; i++) {
			
			neuronsList[i] = new Neuron(numOfInputs, new SigmoidalFunction());
			
		}
		
		System.out.println("Output Layer : " + numOfNeurons + " neurons, " + numOfInputs + " inputs");
		return neuronsList;
	}
	
	public void printNetwork() {
		
		System.out.println("\n---------- MLP Network ----------\n");
		int i = 1;
		for (Neuron[] neuronsList: layers) {
			
			if (i == layers.size()) {
				
				System.out.printf("~~~~~~~~~~ Output Layer ~~~~~~~~~~\n");
				
			} else {
				
				System.out.printf("~~~~~~~~~~ Hidden Layer %d ~~~~~~~~~~\n", i);
			}
			
			printLayer(neuronsList);
			System.out.println();
			i++;
		}
	}
	
	private void printLayer(Neuron [] neuronsList) {
		
		for (int i = 0; i < neuronsList.length; i++) {
			
			System.out.printf("\n\tNeuron %d\n", i+1);
			neuronsList[i].printNeuron();
			System.out.println();
		}
	}

	public double[] forwardPass(double inputs[]) {
		
		double [][] temp_inputs = initTempInputs(inputs);
		int layerNum = 0;
		//System.out.println("\nForward propagate");
		
		for (Neuron[] neuronsList: layers) {
			
			layerNum++;
			//System.out.printf("\tLayer %d\n", layerNum);
			
			int i = 0;
			for (Neuron neuron : neuronsList) {
				
				//System.out.printf("Neuron %d\t", i+1);
				neuron.setInputs(temp_inputs[layerNum-1]);		// neuron's input is set previous levels output 
				temp_inputs[layerNum][i] = neuron.activate();	// activate i-th neuron of layerNum-th layer
				
				//System.out.printf("Output %d: %f\n", i+1, temp_inputs[layerNum][i]);
				i++;
			}
		}
		
		return temp_inputs[layerNum];	// last level's output
	}
	
	private double[][] initTempInputs(double inputs[]){
		
		double[] outputH1 = new double[H1];
		double[] outputH2 = new double[H2];
		double[] outputH3 = new double[H3];
		double[] netOutput = new double[K];
		
		if (numHiddenLayers == 2) {
			
			double [][] temp_inputs = {inputs, outputH1, outputH2,netOutput};
			return temp_inputs;
		}
		
		double [][] temp_inputs = {inputs, outputH1, outputH2, outputH3,netOutput};
		return temp_inputs;
	}

	public void backPropagate(double inputs[], double expected[]) {
		
		double output [] = forwardPass(inputs);
		double diff   [] = calculateDiff(output,expected);
		currentEpochError += calculateEpochError(diff);
				
		int numLastLayer = layers.size()-1;
		
		for (int i = numLastLayer; i > -1; i--) {

			Neuron [] layer = layers.get(i);
			
			int j = 0;	// count neurons in layer
			for (Neuron neuron : layer) {

				// output Layer
				if (i == numLastLayer) {		
					
					neuron.calculateDelta(output[j], diff[j]);
					
				// hidden Layer	
				} else {		
					
					Neuron [] prevLayer = layers.get(i+1);
					double sum = 0;
					
					for (Neuron prevLayerNeuron: prevLayer) {
						
						double error = prevLayerNeuron.getError();
						double weight = prevLayerNeuron.getWeight(j);
						sum += weight * error;
						
					}
					
					neuron.calculateDelta(neuron.activate(), sum);
				}	
				
				j++;
			}
		}
	}
	
	private double[] calculateDiff(double output[], double expected[]) {
		
		double diff [] = new double[K];
		
		for (int i = 0; i < K; i++) {
			
			diff[i] = output[i] - expected[i];
		}
		
		return diff;
	}
	
	private double calculateEpochError(double diff[]) {
		
		double sum = 0;
		
		for(int i = 0; i < K; i++) {
			
			sum +=  Math.pow(Math.abs(diff[i]), 2);
		}
		
		return 0.5 * sum;
	}
	
	private double calculateDiffEpochError(int epoch, double currentEpochError) {
		
		//System.out.printf("Error of epoch %d : %.4f\n", epoch, currentEpochError);
		
		// show the error of the first 10 epochs only
		if (epoch < 10) {
			
			System.out.printf("Error of epoch %d : %.4f\n", epoch, currentEpochError);
			
			if (epoch == 9) { System.out.println(".........."); }
		}
		
		double diffEpochError = Math.abs(currentEpochError - lastEpochError);
		lastEpochError = currentEpochError;
		
		return diffEpochError;
	}
	
	public void train() {
		
		int epoch = 0;
		while(true) {
			
			currentEpochError = 0;
			int numOfBatches = 0;

			for (int i = 0; i < trainingSet.size(); i += B) {
				
				numOfBatches ++;
				for(int j = i; j < numOfBatches*B; j++) {
					
					Point point = trainingSet.get(i);
					backPropagate(point.getCoordinates(), point.createCategoryVector());
				}
				
				//update weights
				for(Neuron [] layer : layers) {
					
					for(Neuron neuron : layer) {
						
						neuron.updateWeights(learningRate);
						neuron.initDelta();
					}
				}
			}
			
			double diffEpochError = calculateDiffEpochError(epoch, currentEpochError);
			
			if (diffEpochError < trainingThreshold && epoch > 700) {
				
				//System.out.println("..........");
				System.out.printf("Error of epoch %d : %.4f\n", epoch, currentEpochError);
				break;
			}
			
			epoch++;
		}
	}
	
	public void test() {
		
		for (Point point: testSet) {
			
			double [] netOutput = forwardPass(point.getCoordinates());
			checkCategory(netOutput, point);
		}
	}
	
	private void checkCategory(double [] netOutput, Point point) {
		
		int maxCategory = 1;
		double maxElem = netOutput[0];
		
		for (int i = 1; i < K; i++) {
			
			if (maxElem < netOutput[i]) {
				
				maxCategory = i + 1;
				maxElem = netOutput[i];
			}
		}
		
		netOutputCategories[maxCategory-1]++;
		String outputCategory = "C" + maxCategory;
		
		if (outputCategory.equals(point.getCategory())) {
			
			correctExamples++;
			
			if (point.getCategory().equals("C1")) { actualCategory1.add(point); }
			if (point.getCategory().equals("C2")) { actualCategory2.add(point); }
			if (point.getCategory().equals("C3")) { actualCategory3.add(point); }
			if (point.getCategory().equals("C4")) { actualCategory4.add(point); }
			
		} else {
			
			wrongExamples++;
		}
	}

	public void printStats() {
		
		System.out.println("\n---------- Statistics ----------");
		System.out.printf("Training set size: %d (batch size = %d)\n", trainingSet.size(), B);
		System.out.printf("Test set size: %d\n", testSet.size());
		System.out.println("Correct categorizations:  " + this.correctExamples);
		System.out.println("Wrong categorizations: " + this.wrongExamples);
		
		double percentage = (correctExamples / (double)testSet.size()) * 100;
		System.out.println();
		System.out.printf("Generalization Ability @ %.2f%s\n", percentage, "%");
		System.out.println(percentage > worthiness ? "The MLP is deemed worthy.\n" : "The MLP is deemed not worthy.\n");
		
		int expectedCategories[] = countExpectedCategories();
		
		System.out.printf("C1: %d (expected: %d)\n",netOutputCategories[0], expectedCategories[0]);
		System.out.printf("C2: %d (expected: %d)\n",netOutputCategories[1], expectedCategories[1]);
		System.out.printf("C3: %d (expected: %d)\n",netOutputCategories[2], expectedCategories[2]);
		System.out.printf("C4: %d (expected: %d)\n",netOutputCategories[3], expectedCategories[3]);
		
		exportStatisticsToFile();
	}
	
	private void exportStatisticsToFile() {
		
		// export statistics to txt file
        try {
        	
        	FileWriter myWriter = new FileWriter("./MLP_stats.txt");
        	
        	myWriter.write("\n---------- Statistics ----------\n");
        	myWriter.write("Training set size: " + trainingSet.size() + " (batch size = " + B + ")\n");
        	myWriter.write("Test set size: " + testSet.size() + "\n");
        	myWriter.write("Correct categorizations:  " + this.correctExamples + "\n");
        	myWriter.write("Wrong categorizations: " + this.wrongExamples + "\n");
    		
    		double percentage = (correctExamples / (double)testSet.size()) * 100;
    		String percentageStr = String.format("%.2f", percentage);
    		myWriter.write("\nGeneralization Ability @ " + percentageStr + "%\n");
    		myWriter.write(percentage > worthiness ? "The MLP is deemed worthy.\n" : "The MLP is deemed not worthy.\n");
    		
    		int expectedCategories[] = countExpectedCategories();
    		
    		myWriter.write("\nC1: " + netOutputCategories[0] + " (expected: " + expectedCategories[0] + ")\n");
    		myWriter.write("C2: " + netOutputCategories[1] + " (expected: " + expectedCategories[1] + ")\n");
    		myWriter.write("C3: " + netOutputCategories[2] + " (expected: " + expectedCategories[2] + ")\n");
    		myWriter.write("C4: " + netOutputCategories[3] + " (expected: " + expectedCategories[3] + ")\n");
    		
        	myWriter.close();
        	
        } catch (IOException e) {
        	
        	System.out.println("An error occurred.");
		    e.printStackTrace();
        }
	}
	
	private int[] countExpectedCategories() {
		
		int expectedCategories[] = new int[K];
		
		for(Point point : testSet) {
			
			switch(point.getCategory()) {
			
	        case "C1": 
	        	expectedCategories[0] ++;
	        	expectedCategory1.add(point);
	            break; 
	            
	        case "C2": 
	        	expectedCategories[1] ++;
	        	expectedCategory2.add(point);
	            break; 
	            
	        case "C3": 
	        	expectedCategories[2] ++;
	        	expectedCategory3.add(point);
	            break; 
	            
	        case "C4": 
	        	expectedCategories[3] ++;
	        	expectedCategory4.add(point);
	            break; 
			}
		}
		
		return expectedCategories;
	}
}