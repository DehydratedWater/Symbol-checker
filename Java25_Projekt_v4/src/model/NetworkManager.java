package model;

import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import controller.tab2.components.LearningSpeedSlider;
import controller.tab2.components.NeuronNetworkSizeSetter;
import exapmlemanager.ExampleManager;
import javafx.scene.image.WritableImage;
import neuronnetwork.NeuronNetwork;
import neuronnetwork.pattern.ActivationFunction;
import neuronnetwork.pattern.SigmoidFunction;
import observer.Publisher;

public class NetworkManager implements NeuronNetworkManagerInterface
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	
	private HashMap<String, NeuronNetwork> neuronNetworkMap;
	private LearnMenager learnMenager;

	private ExampleManager exampleManager;
	private String path;
	private final int deflautInputLayerSize = 600;
	
	private final ActivationFunction deflautActivationFunction = new SigmoidFunction();

	private String neuronNetworkName = "neuronNetwork.obj";
	
	public NetworkManager(ExampleManager exampleManager, String path)
	{
		System.out.println("£adowanie Managera Sieci: "+exampleManager);
		this.exampleManager = exampleManager;
		this.path = path;
		try {
			deserializeNetworks(path+"/"+neuronNetworkName);
		} catch (ClassNotFoundException | IOException e) {
			neuronNetworkMap = new HashMap<>();
			System.out.println("Utworzono nowy menad¿er sieci");
			e.printStackTrace();
		}
		learnMenager = new LearnMenager();
		
	}
	
	@Override
	public void addNewNeuronNetwork(String networkName, int[] layers, ActivationFunction activationFunction, Publisher learningSpeedSource)
	{
		NeuronNetwork nn = new NeuronNetwork(layers, activationFunction);
		nn.setLearningSpeedPublisher(learningSpeedSource);
		neuronNetworkMap.put(networkName, new NeuronNetwork(layers, activationFunction));
		System.out.println("Dodanio sieæ neuronow¹: "+networkName+" iloœæ sieci: "+neuronNetworkMap.keySet().size());
	}
	
	@Override
	public void addNewNeuronNetwork(String networkName, int outputLayerSize, Publisher learningSpeedSource)
	{
		int size[] = NeuronNetworkSizeSetter.getNeuronNetworkSize(outputLayerSize);
		NeuronNetwork nn = new NeuronNetwork(new int[] {deflautInputLayerSize, size[0], size[1]}, deflautActivationFunction);
		nn.setLearningSpeedPublisher(learningSpeedSource);
		neuronNetworkMap.put(networkName, nn);
		System.out.println("Dodanio sieæ neuronow¹: "+networkName+" iloœæ sieci: "+neuronNetworkMap.keySet().size());
	}
	

	@Override
	public void removeNeuronNetwork(String networkName, LearningSpeedSlider learningSpeedSlider) {
		System.out.println("Usuwanie sieci");
		if(learnMenager.isLearning())
		{
			if(errorSaund!=null) errorSaund.run();
			return;
		}
		disconnectNetworkWithSlider(networkName, learningSpeedSlider);
		neuronNetworkMap.remove(networkName);
		
	}

	@Override
	public void learnNeuronNetwork(String networkName, ArrayList<String> symbolTab) {
		System.out.println("Uczenie sieci: "+networkName+" nastêpuj¹cej listy symboli: "+symbolTab);
		learnMenager.learnNetwork(symbolTab, neuronNetworkMap.get(networkName), exampleManager);
	}

	@Override
	public double[] getResoult(String networkName, WritableImage example) {
//		System.out.println("Kalkulowanie wyniku "+networkName+" "+example);
		if(neuronNetworkMap.containsKey(networkName))
			return neuronNetworkMap.get(networkName).calculateNetwork(learnMenager.getInputFromBufferImage(example));
		else 
			return null;
	}

	public int getNumberOfSymbols(String networkName)
	{
		return neuronNetworkMap.get(networkName).getNumberOfSymbol();
	}

	public int getMaxNumberOfSymbols(String networkName)
	{
		return neuronNetworkMap.get(networkName).getOutputSize();
	}

	@Override
	public String[] getSymbolTagList(String networkName) 
	{
		return neuronNetworkMap.get(networkName).getSymbolTab();
	}

	public int getIndexOfSymbol(String networkName, String symbol)
	{
		return neuronNetworkMap.get(networkName).getIndexForSymbolTAB(symbol);
	}
	
	public int getIndexOfSymbolTAB(String networkName, String symbol)
	{
		return neuronNetworkMap.get(networkName).getIndexForSymbolTAB(symbol);
	}
	
	@Override
	public boolean networkExists(String networkName) {
		if(neuronNetworkMap.containsKey(networkName))
			return true;
		return false;
	}

	
	public LearnMenager getLearnMenager() {
		return learnMenager;
	}

	@Override
	public void serializeNetworks() throws IOException {
		System.out.println("Zapisywanie sieci...");
		FileOutputStream fos = new FileOutputStream(path+"/"+neuronNetworkName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(neuronNetworkMap);
		oos.flush();
		fos.flush();
		oos.close();
		fos.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeNetworks(String path) throws IOException, ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		neuronNetworkMap = (HashMap<String, NeuronNetwork>)ois.readObject();
		System.out.println("Za³adowano sieæ");
		System.out.println(neuronNetworkMap.keySet());
		ois.close();
		fis.close();
		
		
	}
	
	public Set<String> getSetOfNetworks()
	{
		return neuronNetworkMap.keySet();
	}
	
	public void connectNetworksWithSlider(Publisher learningSpeedSource)
	{
		System.out.println("£¹czenie sieci ze sliderem: ");
		for(String name : neuronNetworkMap.keySet())
			neuronNetworkMap.get(name).setLearningSpeedPublisher(learningSpeedSource);
	}
	
	public void disconnectNetworkWithSlider(String name, Publisher learningSpeedSource)
	{
		learningSpeedSource.deleteObserver(neuronNetworkMap.get(name).getLerningSpeedModyfier());
	}

}
