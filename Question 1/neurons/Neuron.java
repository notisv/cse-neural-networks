package neurons;

import transferfunctions.TransferFunction;

import java.util.Random;

public class Neuron{
	
	private double[] weights;
	private double bias;
	
	private double[] inputs;
	private int numOfInputs;
	
	private TransferFunction transferFunction;
	private Random randomGenerator;
	
	private double [] deltaWeights;
	private double deltaBias;
	private double error;
	
	public Neuron(int numOfInputs, TransferFunction transferFunction) {
		
		this.numOfInputs = numOfInputs;
		this.weights = new double[numOfInputs];
		this.bias = 0; 
		this.transferFunction = transferFunction;
		
		this.deltaWeights = new double[numOfInputs];
		this.deltaBias = 0;
		this.error = 0;
		
		this.randomGenerator = new Random();
		randomizeWeights(); // randomize weights here
	}
	
	// randomize in range [-1,1]
	private void randomizeWeights() {
		
		for (int i = 0; i < numOfInputs ; i ++) {
			
			//random.NextDouble() * (maximum - minimum) + minimum
			weights[i] = 2 * randomGenerator.nextDouble() - 1;
			
		}
		
		bias = 2 * randomGenerator.nextDouble() - 1;
	}
	
	public void setInputs(double inputs[]) {
		
		this.inputs = inputs;
	}
	
	public double getError() {
		
		return error;
	}
	
	public double getWeight(int index) {
		
		return weights[index];
	}
		
	public void printNeuron(){
		
		for (int i = 0 ; i < numOfInputs; i++) {
			
			System.out.printf("Input %d : %f\t", i+1, inputs[i]);
		}
		
		System.out.println();
		
		for (int i = 0 ; i < numOfInputs; i++) {
			
			System.out.printf("Weight %d : %f\t", i+1, weights[i]);
		}
		
		System.out.println();
		System.out.printf("Bias : %f  ", bias);
	}
	
	public double activate() {
		
		double activation = bias;
		for (int i = 0 ; i < numOfInputs; i++) {
			
			activation += weights[i] * inputs[i];
		}
		
		return transferFunction.calculate(activation);
	}

	public void initDelta() {
		
		for (int i = 0; i < numOfInputs; i++) {
			
			deltaWeights[i] = 0;
		}
		
		deltaBias = 0;
	}

	public void calculateDelta(double output, double secondFactor) {
		
		error = transferFunction.calculateDerivate(output) * secondFactor;
		
		for (int i = 0; i < numOfInputs; i++) {
			
			deltaWeights[i] += error * inputs[i];
		}
		
		deltaBias += error;
	}
	
	public void updateWeights(double learningRate) {
		
		for ( int i = 0; i < numOfInputs; i++) {
			
			weights[i] -= learningRate * deltaWeights[i];
		}
		
		this.bias -= learningRate * deltaBias;
	}
}