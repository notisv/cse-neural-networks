package transferfunctions;

public class HyperbolicFunction implements TransferFunction {

		public double calculate(double value) {
			
			return Math.tanh(value);
		}

		
		public double calculateDerivate(double value) {
			
			return 1 - Math.pow(value, 2);
		}
}