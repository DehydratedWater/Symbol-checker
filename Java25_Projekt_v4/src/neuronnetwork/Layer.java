package neuronnetwork;

import java.io.Serializable;

public class Layer implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected Neuron[] neuronTab;
	
	public Layer(int layerSize, int previousLayerSize)
	{
		neuronTab = new Neuron[layerSize];
		for (int i = 0; i < neuronTab.length; i++) 
			neuronTab[i] = new Neuron(previousLayerSize);
	}
	
	public int layerSize()
	{
		return neuronTab.length;
	}
}
