package neuronnetwork;

import java.io.Serializable;

public class Neuron implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected double[] weights;
	transient protected double value;
	protected double bias;
	transient protected double error;
	transient protected double activationSum;
	
	public Neuron(int previousLayerSize)
	{
		weights = new double[previousLayerSize];
		bias = (Math.random()>0.5) ? Math.random() : -Math.random();
		for(int i = 0; i < weights.length; i++)
			weights[i] = (Math.random()>0.5) ? Math.random() : -Math.random();
	}
}
