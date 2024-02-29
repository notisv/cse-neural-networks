package transferfunctions;

public class SigmoidalFunction implements TransferFunction {

	public double calculate(double value) {
		
		return 1 / (1 + Math.pow(Math.E, - value));
	}

	public double calculateDerivate(double value) {
		
		return (value - Math.pow(value, 2));
	}
}