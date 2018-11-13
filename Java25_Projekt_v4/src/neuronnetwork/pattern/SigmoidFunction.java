package neuronnetwork.pattern;

public class SigmoidFunction implements ActivationFunction
{
	private static final long serialVersionUID = 1L;

	@Override
	public double getActivation(double sum) {
		return 1/(1+Math.pow(Math.E, -sum));
	}

	@Override
	public double getDerivative(double sum) {
		double act = getActivation(sum);
		return act*(1-act);
	}

}
