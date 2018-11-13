package neuronnetwork;

import java.io.Serializable;
import java.util.Arrays;

import neuronnetwork.pattern.ActivationFunction;
import observer.Observer;
import observer.Publisher;

public class NeuronNetwork implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Layer[] layersTab;
	private ActivationFunction activationFunction;
	private int inputSize;
	private LearningSpeedModyfier learningSpeedModyfier;
	
	private String[] symbolTab;
	private int numberOfSymbol = 0;
	
	public NeuronNetwork(int[] layers, ActivationFunction activationFunction)
	{

//		symbolTagIndexList = new HashMap<>();
		learningSpeedModyfier = new LearningSpeedModyfier(0.01);
		this.inputSize = layers[0];
		layersTab = new Layer[layers.length-1];
		layersTab[0] = new Layer(layers[1], layers[0]);
		for(int i = 1; i < layers.length-1; i++)
			layersTab[i] = new Layer(layers[i+1], layers[i]);
		this.activationFunction = activationFunction;
		symbolTab = new String[getOutputSize()];
	}
	
	public NeuronNetwork(int[] layers, double learningSpeed, ActivationFunction activationFunction)
	{
//		symbolTagIndexList = new HashMap<>();
		learningSpeedModyfier = new LearningSpeedModyfier(learningSpeed);
		this.inputSize = layers[0];
		layersTab = new Layer[layers.length-1];
		layersTab[0] = new Layer(layers[1], layers[0]);
		for(int i = 1; i < layers.length-1; i++)
			layersTab[i] = new Layer(layers[i+1], layers[i]);
		this.activationFunction = activationFunction;
		symbolTab = new String[getOutputSize()];
	}
	
	public boolean addSymbolTAB(String symbol)
	{
		System.out.println("Dodawanie symbolu: "+symbol+" "+Arrays.toString(symbolTab));
		for(int i = 0; i < numberOfSymbol; i++)
		{
			if(symbolTab[i].equals(symbol))
			{
				System.out.println("Taki symbol ju¿ istnieje");
				return true;
			}
				
		}
		System.out.println("KK");
		
		if(numberOfSymbol==symbolTab.length)
		{
			System.out.println("Przekroczono iloœæ syboli");
			return false;
		}
			
		
		symbolTab[numberOfSymbol] = symbol;
		numberOfSymbol++;
		System.out.println("Dodawanie symbolu: "+symbol+" "+Arrays.toString(symbolTab));
		return true;
	}
	
	public int getIndexForSymbolTAB(String symbol)
	{
		for(int i = 0; i < inputSize; i++)
		{
			if(symbolTab[i].equals(symbol))
				return i;
		}
		return -1;
	}
	
	public String[] getSymbolTab() {
		return symbolTab;
	}

	public double[] calculateNetwork(double[] input) throws RuntimeException
	{
		if(input.length!=inputSize)
			throw new RuntimeException("Wrong size of input data");
		
		for(int i = 0; i < layersTab[0].layerSize(); i++)
		{
			double activationSum = 0;
			for(int j = 0; j < input.length; j++)
			{
				activationSum+=input[j]*layersTab[0].neuronTab[i].weights[j];
			}
			activationSum+=layersTab[0].neuronTab[i].bias;
			layersTab[0].neuronTab[i].activationSum = activationSum;
			layersTab[0].neuronTab[i].value = activationFunction.getActivation(activationSum);
		}

		for(int k = 1; k < layersTab.length; k++)
		{
			for(int i = 0; i < layersTab[k].layerSize(); i++)
			{
				double activationSum = 0;
				for(int j = 0; j < layersTab[k-1].layerSize(); j++)
				{
					activationSum+=layersTab[k].neuronTab[i].weights[j]*layersTab[k-1].neuronTab[j].value;
				}
				activationSum+=layersTab[k].neuronTab[i].bias;
				layersTab[k].neuronTab[i].activationSum = activationSum;
				layersTab[k].neuronTab[i].value = activationFunction.getActivation(activationSum);
			}
		}

		double[] output = new double[layersTab[layersTab.length-1].layerSize()];
		
		for(int i = 0; i < output.length; i++)
		{
			output[i] = layersTab[layersTab.length-1].neuronTab[i].value;
		}
		
		return output;
		
	}
	
	public double learnNetwork(double[] input, double[] optimal) throws RuntimeException
	{
		double output[] = calculateNetwork(input);
		double networkError = 0;
		
		Layer lastLayer = layersTab[layersTab.length-1];
		
		for(int i = 0; i < lastLayer.layerSize(); i++)
		{
			lastLayer.neuronTab[i].error = (optimal[i]-output[i])*activationFunction.getDerivative(lastLayer.neuronTab[i].activationSum);	
			networkError+=Math.abs(optimal[i]-output[i]);
		}
		
		for(int r = layersTab.length-2; r >= 0; r--)
		{
			Layer upperLayer = layersTab[r];
			Layer dipperLayer = layersTab[r+1];
			for(int i = 0; i < upperLayer.layerSize(); i++)
			{
				double errorSum = 0;
				for(int j = 0; j < dipperLayer.layerSize(); j++)
				{
					errorSum+=dipperLayer.neuronTab[j].weights[i]*dipperLayer.neuronTab[j].error;
				}
				upperLayer.neuronTab[i].error = errorSum*activationFunction.getDerivative(upperLayer.neuronTab[i].activationSum);
			}
		}
		
		Layer firstLayer = layersTab[0];
		for(int i = 0; i < firstLayer.layerSize(); i++)
		{
			for(int j = 0; j < firstLayer.neuronTab[i].weights.length; j++)
			{
				firstLayer.neuronTab[i].weights[j]+=learningSpeedModyfier.getLearningSpeed()*firstLayer.neuronTab[i].error*input[j];
			}
		}
		
		for(int k = 1; k < layersTab.length; k++)
		{
			for(int i = 0; i < layersTab[k].layerSize(); i++)
			{
				for(int j = 0; j < layersTab[k].neuronTab[i].weights.length; j++)
				{
					layersTab[k].neuronTab[i].weights[j]+=learningSpeedModyfier.getLearningSpeed()*layersTab[k].neuronTab[i].error*layersTab[k-1].neuronTab[j].value;
				}
			}
		}
		networkError/=optimal.length;
		
		//System.out.println("Uczenie dla: "+Arrays.toString(input)+" -> "+Arrays.toString(output)+" opt: "+Arrays.toString(optimal)+" mist: "+networkError+"%");
		return networkError;
		
	}
	
	public double learnNetwork(double[] input, double[] optimal, double minimalAccuracy) throws RuntimeException
	{
		double output[] = calculateNetwork(input);
		double networkError = 0;
		
		Layer lastLayer = layersTab[layersTab.length-1];
		
		for(int i = 0; i < lastLayer.layerSize(); i++)
		{
			lastLayer.neuronTab[i].error = (optimal[i]-output[i])*activationFunction.getDerivative(lastLayer.neuronTab[i].activationSum);	
			networkError+=Math.abs(optimal[i]-output[i]);
		}
		
		networkError/=optimal.length;
		if(networkError<minimalAccuracy)
		{
			System.out.println("Pomijanie uczenia sieci");
			return networkError;
		}
		
		for(int r = layersTab.length-2; r >= 0; r--)
		{
			Layer upperLayer = layersTab[r];
			Layer dipperLayer = layersTab[r+1];
			for(int i = 0; i < upperLayer.layerSize(); i++)
			{
				double errorSum = 0;
				for(int j = 0; j < dipperLayer.layerSize(); j++)
				{
					errorSum+=dipperLayer.neuronTab[j].weights[i]*dipperLayer.neuronTab[j].error;
				}
				upperLayer.neuronTab[i].error = errorSum*activationFunction.getDerivative(upperLayer.neuronTab[i].activationSum);
			}
		}
		
		Layer firstLayer = layersTab[0];
		for(int i = 0; i < firstLayer.layerSize(); i++)
		{
			for(int j = 0; j < firstLayer.neuronTab[i].weights.length; j++)
			{
				firstLayer.neuronTab[i].weights[j]+=learningSpeedModyfier.getLearningSpeed()*firstLayer.neuronTab[i].error*input[j];
			}
		}
		
		for(int k = 1; k < layersTab.length; k++)
		{
			for(int i = 0; i < layersTab[k].layerSize(); i++)
			{
				for(int j = 0; j < layersTab[k].neuronTab[i].weights.length; j++)
				{
					layersTab[k].neuronTab[i].weights[j]+=learningSpeedModyfier.getLearningSpeed()*layersTab[k].neuronTab[i].error*layersTab[k-1].neuronTab[j].value;
				}
			}
		}
		
		
		//System.out.println("Uczenie dla: "+Arrays.toString(input)+" -> "+Arrays.toString(output)+" opt: "+Arrays.toString(optimal)+" mist: "+networkError+"%");
		return networkError;
		
	}

	
	public void setLearningSpeedPublisher(Publisher publisher) {
		publisher.addObserver(learningSpeedModyfier);
	}

	public Observer getLerningSpeedModyfier()
	{
		return learningSpeedModyfier;
	}
	
	public int getOutputSize() {
		return layersTab[layersTab.length-1].neuronTab.length;
	}

	public int getNumberOfSymbol() {
		return numberOfSymbol;
	}

	public void setNumberOfSymbol(int numberOfSymbol) {
		this.numberOfSymbol = numberOfSymbol;
	}
}
