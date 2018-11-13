package model;

import java.io.IOException;
import java.util.ArrayList;

import controller.tab2.components.LearningSpeedSlider;
import javafx.scene.image.WritableImage;
import neuronnetwork.pattern.ActivationFunction;
import observer.Publisher;

public interface NeuronNetworkManagerInterface 
{
	public void addNewNeuronNetwork(String networkName, int[] layers, ActivationFunction activationFunction, Publisher learningSpeedSource);
	public void addNewNeuronNetwork(String networkName, int outputLayerSize, Publisher learningSpeedSource);
	public void removeNeuronNetwork(String networkName, LearningSpeedSlider learningSpeedSlider);
	
	public void learnNeuronNetwork(String networkName, ArrayList<String> symbolTag);

	public double[] getResoult(String networkName, WritableImage example);
	String[] getSymbolTagList(String networkName);
	boolean networkExists(String networkName);
	
	void serializeNetworks() throws IOException;
	void deserializeNetworks(String path) throws IOException, ClassNotFoundException;
	
}
