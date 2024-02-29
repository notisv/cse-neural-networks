package transferfunctions;

public class ReluFunction implements TransferFunction {

	public double calculate(double value) {
		
		if (value > 0) {
			
			return value;
		}
		
		return 0;
	}

	public double calculateDerivate(double value) {
		
		if (value > 0) {
			
			return 1;
		}
		
		return 0;
	}
}