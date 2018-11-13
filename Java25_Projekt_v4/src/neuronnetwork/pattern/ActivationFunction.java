package neuronnetwork.pattern;

import java.io.Serializable;

public interface ActivationFunction extends Serializable
{
	public double getActivation(double sum);
	public double getDerivative(double sum);
}
