package model;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import exapmlemanager.ExampleManager;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import neuronnetwork.NeuronNetwork;
import observer.Observer;
import observer.Publisher;

public class LearnMenager implements Publisher
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	private ArrayList<Observer> observerList;
	
	
	private boolean stop;
	private boolean stopAll;
	private boolean isLearning;
	
	public double cycleProgress;
	public double networkAccuracy = 1;
	public double exampleAccuracy;
	public double learning;
	public Image preview;
	public String lastSymbolTag;
	public boolean showExample;
	public int numberOfThreads;
	
	public LearnMenager()
	{
		observerList = new ArrayList<Observer>();
	}
	
	public void learnNetwork(ArrayList<String> symbolTab, NeuronNetwork network, ExampleManager exampleManager)
	{
		if(numberOfThreads>=10)
		{
			if(errorSaund!=null) errorSaund.run();
			return;
		}
			
			
		System.out.println("Uruchamianie w¹tku ucz¹cego");
		LearnThreadType2 lt = new LearnThreadType2(network, symbolTab, exampleManager);
		Thread th = new Thread(lt);
		th.start();
		
	}


	
	private class LearnThreadType2 extends Task<Void>
	{
		NeuronNetwork network;
		ArrayList<String> symbolTab;
		ExampleManager exampleManager;
		
		
		public LearnThreadType2(NeuronNetwork network, ArrayList<String> symbolTab, ExampleManager exampleManager) {
			this.network = network;
			this.symbolTab = symbolTab;
			this.exampleManager = exampleManager;
		}


		@Override
		protected Void call() throws Exception 
		{
			learnNetwork();
			return null;
		}
		
		private void learnNetwork() throws InterruptedException
		{
			
			isLearning = true;
			numberOfThreads++;
			int nrOfThread = numberOfThreads;
			int index[] = new int[symbolTab.size()];
			double[] optimal = new double[network.getOutputSize()];
			double[] zero = new double[600];
//			double networkError = 0;
			double sumError = 0;
			double exampleError = 0;
			int numberOfExamples = 0;
			
			for(String s : symbolTab)
				network.addSymbolTAB(s);
			double cycles = 1000;
			
			//Przetestowaæ TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST 
			Random r = new Random();
			for(int i = 0; i < index.length; i++)
				index[i] = r.nextInt(exampleManager.getNumberOfExamples(symbolTab.get(i)));
			
			for(int i = 0; i < cycles; i++)
			{
			learning = i/cycles;
			notyfieObservers();
			network.learnNetwork(zero, optimal);
			
			int directionS1 = (r.nextInt(2)==1) ? 1 : -1;
			int directionS2 = (r.nextInt(2)==1) ? 1 : -1;
			
			for(int s1 = 0; s1 < symbolTab.size(); s1++)
			{
//				System.out.println(directionS1+" "+directionS2);
				cycleProgress = ((double)s1)/(symbolTab.size()-1);
				String symbol1 = symbolTab.get(s1);
				for(int s2 = 0; s2 < symbolTab.size(); s2++)
				{
					if(stop&&nrOfThread==numberOfThreads)
					{
						System.out.println("(stop)Przerywanie w¹tku nauki nr: "+nrOfThread);
						stop = false;
						numberOfThreads--;
						if(numberOfThreads==0)
							isLearning = false;
						notyfieObservers();
						return;
					}
					if(stopAll)
					{
						System.out.println("(All)Przerywanie w¹tku nauki nr: "+nrOfThread);
						return;
					}
					String symbol2 = symbolTab.get(s2);
//					if(symbol1.equals(symbol2))
//						break;
//					System.out.println(symbol1+" vs "+symbol2);
					double[] input1 = getInputData(symbol1, index[s1]);
					optimal[network.getIndexForSymbolTAB(symbol1)] = 1;
					exampleError = network.learnNetwork(input1, optimal);
					optimal[network.getIndexForSymbolTAB(symbol1)] = 0;
					
					sumError += exampleError;
					numberOfExamples++;
					networkAccuracy = sumError/numberOfExamples;
					exampleAccuracy = exampleError;
					
					if(showExample)
					{
						notyfieObservers();
						Thread.sleep(200);
					}
					
					double[] input2 = getInputData(symbol2, index[s2]);
					optimal[network.getIndexForSymbolTAB(symbol2)] = 1;
					exampleError = network.learnNetwork(input2, optimal);
					optimal[network.getIndexForSymbolTAB(symbol2)] = 0;
					
					sumError += exampleError;
					numberOfExamples++;
					networkAccuracy = sumError/numberOfExamples;
					exampleAccuracy = exampleError;
					
					if(showExample)
					{
						notyfieObservers();
						Thread.sleep(200);
					}
					
					
					index[s1]+=directionS1;
					index[s2]+=directionS2;
					if(index[s1]>=exampleManager.getNumberOfExamples(symbol1)-1)
						index[s1] = 0;
					if(index[s1]<0)
						index[s1] = exampleManager.getNumberOfExamples(symbol1)-1;
					
					if(index[s2]>=exampleManager.getNumberOfExamples(symbol2)-1)
						index[s2] = 0;
					if(index[s2]<0)
						index[s2] = exampleManager.getNumberOfExamples(symbol2)-1;
				}
			}
			}
			learning = 1;
			numberOfThreads--;
			if(numberOfThreads==0)
				isLearning = false;
			notyfieObservers();
			System.out.println("Koñczenie w¹tku nauki nr: "+numberOfThreads);
			
		}
		
		private double[] getInputData(String symbol, int index)
		{
			Image im = exampleManager.getExampleTraining(symbol, index+1);
			if(showExample)
				preview = im;
			else
				preview = null;
			return getInputFromBufferImage(im);
		}
		
	}
	
	public void stop()
	{
		if(isLearning)
			stop = true;
	}
	
	public double[] getResoult(NeuronNetwork network, Image example)
	{
		return network.calculateNetwork(getInputFromBufferImage(example));
	}
	
	private double[] getInputFromBufferImage(Image trainExample)
	{
		double[] input = new double[(int) (trainExample.getWidth()*trainExample.getHeight())];
	
		int index = 0;
//		int czarne = 0;
		for(int w = 0; w < trainExample.getWidth(); w++)
		{
			for(int h = 0; h < trainExample.getHeight(); h++)
			{
				input[index] = (trainExample.getPixelReader().getArgb(w, h)!=Color.WHITE.getRGB()) ? 1 : 0;
//				czarne+=input[index];
				index++;
			}
		}
//		System.out.println("Iloœæ czarnych: "+czarne);
		return input;
	}
	
	public double[] getInputFromBufferImage(WritableImage trainExample)
	{
		double[] input = new double[(int) (trainExample.getWidth()*trainExample.getHeight())];
	
		int index = 0;
//		int czarne = 0;
		for(int w = 0; w < trainExample.getWidth(); w++)
		{
			for(int h = 0; h < trainExample.getHeight(); h++)
			{
				input[index] = (trainExample.getPixelReader().getArgb(w, h)!=Color.WHITE.getRGB()) ? 1 : 0;
//				czarne+=input[index];
				index++;
			}
		}
//		System.out.println("Iloœæ czarnych: "+czarne);
		return input;
	}

	@Override
	public void addObserver(Observer o) {
		observerList.add(o);
	}


	@Override
	public void deleteObserver(Observer o) {
		observerList.remove(o);
	}


	@Override
	public void notyfieObservers() {
		for(Observer o : observerList)
			o.update(this);
		
	}

	public boolean isLearning() {
		return isLearning;
	}

	public boolean isStopAll() {
		return stopAll;
	}

	public void setStopAll(boolean stopAll) {
		this.stopAll = stopAll;
	}
}
