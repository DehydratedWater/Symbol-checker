package controller.tab4.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import controller.tab1.components.CanvasActivator;
import model.NetworkManager;
import model.SymbolAccuracy;
import observer.Observer;
import observer.Publisher;

public class ExampleTester implements Observer, Publisher
{
	private ArrayList<Observer> observerList;
	private NetworkManager networkManager;
	private String choosenNetwork = "";
	private double[] resoult;
	private ArrayList<SymbolAccuracy> symbolList;
	

	public ExampleTester(NetworkManager networkManager) {
		observerList = new ArrayList<>();
		this.networkManager = networkManager;
	}
	@Override
	public void update(Object obj) {
		
		resoult = networkManager.getResoult(getChoosenNetwork(), ((CanvasActivator) obj).getTrainingImage()[1]);
		System.out.println(resoult);
		System.out.println(Arrays.toString(resoult));
		if(resoult!=null)
		{
			symbolList = new ArrayList<>(resoult.length);
			String[] symbols = networkManager.getSymbolTagList(choosenNetwork);
			for(int i = 0; i < networkManager.getNumberOfSymbols(choosenNetwork); i++)
			{
				symbolList.add(new SymbolAccuracy(symbols[i], resoult[networkManager.getIndexOfSymbolTAB(choosenNetwork, symbols[i])]));
			}
			
			Collections.sort(symbolList);
			
			System.out.println("Powiadamianie obserwatorów o stanie nauki");
			notyfieObservers();
			
			System.out.println(symbolList);
		}
	}
	@Override
	public void addObserver(Observer o) {
		
		observerList.add(o);
		System.out.println("Dodawanie obserwatora "+observerList.size());
		
	}
	@Override
	public void deleteObserver(Observer o) {
		observerList.remove(o);
		
	}
	@Override
	public void notyfieObservers() {
		System.out.println("Iloœæ obserwatorów "+observerList.size());
		for(Observer o : observerList)
			o.update(this);
		
	}
	public String getChoosenNetwork() {
		return choosenNetwork;
	}
	public void setChoosenNetwork(String choosenNetwork) {
		this.choosenNetwork = choosenNetwork;
	}
	public ArrayList<SymbolAccuracy> getSymbolList() {
		return symbolList;
	}
	public void setSymbolList(ArrayList<SymbolAccuracy> symbolList) {
		this.symbolList = symbolList;
	}

	
}
