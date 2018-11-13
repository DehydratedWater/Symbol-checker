package controller.tab;


import java.awt.Toolkit;
import java.util.ArrayList;

import controller.MainController;
import controller.tab2.components.CanvasModyfier;
import controller.tab2.components.LabelRefresher;
import controller.tab2.components.LearnSpeedLabel;
import controller.tab2.components.LearningSpeedSlider;
import controller.tab2.components.ProgressBarObserver;
import controller.tab2.components.TreeViewManager;
import exapmlemanager.ExampleManager;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import model.NetworkManager;

public class Tab2Controller
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	
	
	@FXML private TreeView<CheckBox> symbolSelector;
	@FXML private ComboBox<String> networkSelector;
	@FXML private Slider learningSpeedSelector;
	@FXML private Label limitOfSymbols;
	@FXML private Label numberOfSymbols;
	@FXML private Label numberOfThreads;
	
	@FXML private Label learningSpeedLabel;
	@FXML private Canvas exampleCanvas;
	@FXML private CheckBox showExamples;
	
	@FXML private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
	@FXML private Label progress1, progress2, progress3, progress4;
	
	private MainController mainController;
	private ExampleManager exampleManager;
	private LearningSpeedSlider learningSpeedSlider;
	private TreeViewManager treeViewManager;
	private NetworkManager networkManager;

	public void initialize()
	{
		System.out.println("Inicjalizowanie tab2");

		treeViewManager = new TreeViewManager(symbolSelector, numberOfSymbols);
		learningSpeedSlider = new LearningSpeedSlider(learningSpeedSelector);
		learningSpeedSlider.addObserver(new LearnSpeedLabel(learningSpeedLabel));
		learningSpeedSlider.notyfieObservers();
		
		exampleCanvas.getGraphicsContext2D().setFill(Color.WHITE);
		exampleCanvas.getGraphicsContext2D().fillRect(0, 0, exampleCanvas.getWidth(), exampleCanvas.getHeight());
		
		showExamples.setOnAction(e->{networkManager.getLearnMenager().showExample=showExamples.isSelected();});
		
		networkSelector.setOnAction((e)->
		{
			if(!networkSelector.getItems().contains(networkSelector.getValue()))
			{
				String symbol = networkSelector.getValue().replaceAll(" ", "_");
				symbol = symbol.toLowerCase();
				networkSelector.setValue(symbol);
				
				System.out.println("wpisywanie nowego tekstu "+networkSelector.getValue());
				treeViewManager.enableAllSymbols();
				limitOfSymbols.setText("100");
				numberOfSymbols.setText("0");
				treeViewManager.setLimitOfSelectedSymbols(100);
			}
			else
			{
				treeViewManager.setUsedSymbolDisabled(networkManager.getSymbolTagList(networkSelector.getValue()));
				treeViewManager.setLimitOfSelectedSymbols(networkManager.getMaxNumberOfSymbols(networkSelector.getValue()));
				limitOfSymbols.setText(treeViewManager.getLimitOfSelectedSymbols()+"");
				numberOfSymbols.setText(treeViewManager.getNumberOfSelectedSymbols()+"");
//				treeViewManager.setLimitOfSelectedSymbols(networkManager.getMaxNumberOfSymbols(networkSelector.getValue()));
			}
				
		});
		System.out.println("Koniec tab2");
	}


	private void initializeAllProgressBars() {
		ProgressBarObserver progressBarObserver1 = new ProgressBarObserver(progressBar1, progress1, (v,lm)->{v[0] = 1.0-lm.networkAccuracy;});
		ProgressBarObserver progressBarObserver2 = new ProgressBarObserver(progressBar2, progress2, (v,lm)->{v[0] = 1-lm.exampleAccuracy;});
		ProgressBarObserver progressBarObserver3 = new ProgressBarObserver(progressBar3, progress3, (v,lm)->{v[0] = lm.cycleProgress;});
		ProgressBarObserver progressBarObserver4 = new ProgressBarObserver(progressBar4, progress4, (v,lm)->{v[0] = lm.learning;});
		networkManager.getLearnMenager().addObserver(progressBarObserver1);
		networkManager.getLearnMenager().addObserver(progressBarObserver2);
		networkManager.getLearnMenager().addObserver(progressBarObserver3);
		networkManager.getLearnMenager().addObserver(progressBarObserver4);
		LabelRefresher threadsRefresher = new LabelRefresher(numberOfThreads, (e,a)->{e.setText(a.numberOfThreads+"");}); 
		networkManager.getLearnMenager().addObserver(threadsRefresher);
	}
	private void initializeCanvas(){
		networkManager.getLearnMenager().addObserver(new CanvasModyfier(exampleCanvas));
	}

	@FXML public void learn()
	{
		ArrayList<String> symbolTag = getListOfChoosenSymbols();
		String networkName = networkSelector.getValue();
		System.out.println("Rozpoczêcie uczenia siê sieci "+networkName+": "+symbolTag);
		
		System.out.println(""+networkName+"");
		if(networkName==null||symbolTag.size()==0||networkName==""||networkName==" "||networkName.length()==0)
		{
			System.out.println("Brak wybralej sieci");
			if(errorSaund!=null)errorSaund.run();
			return;
		}

		
		if(!networkManager.networkExists(networkName))//Stwórz sieæ
		{
			System.out.println("Dodawanie sieci");
			networkSelector.getItems().add(networkName);
			mainController.getTab4Controller().addNetwork(networkName);
			networkManager.addNewNeuronNetwork(networkName, treeViewManager.getNumberOfSelectedSymbols(), learningSpeedSlider);
			treeViewManager.setLimitOfSelectedSymbols(networkManager.getMaxNumberOfSymbols(networkSelector.getValue()));
		}
		
		
		treeViewManager.setUsedSymbolDisabled(symbolTag);	
		networkManager.learnNeuronNetwork(networkName, symbolTag);
		
		limitOfSymbols.setText(treeViewManager.getLimitOfSelectedSymbols()+"");
		numberOfSymbols.setText(treeViewManager.getNumberOfSelectedSymbols()+"");


		System.out.println("Koñczenie w¹tku");
	}
	
	@FXML public void removeNetwork()
	{

		if(networkSelector.getValue()==null||networkSelector.getValue().equals("")||networkManager.getLearnMenager().isLearning())
		{
			System.out.println("Nie usuwanie sieci poniewa¿ pracuje");
			if(errorSaund!=null) errorSaund.run();
			return;
		}
		System.out.println("Usuwanie sieci: "+networkSelector.getValue());
		networkManager.removeNeuronNetwork(networkSelector.getValue(), learningSpeedSlider);
		mainController.getTab4Controller().removeNetwork(networkSelector.getValue());
		networkSelector.getItems().remove(networkSelector.getValue());
		
		
	}
	@FXML public void stop()
	{
		System.out.println("Zatrzymanie uczenia siê");
		networkManager.getLearnMenager().stop();
	}
	
	//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
	public void initializeComponentsWithManagers()
	{
		System.out.println("Inicjalizacja komponentów na managerami");
		networkSelector.getItems().addAll(networkManager.getSetOfNetworks());
		if(networkSelector.getItems().size()>0)
		{
			networkSelector.setValue(networkSelector.getItems().get(0));
			treeViewManager.setUsedSymbolDisabled(networkManager.getSymbolTagList(networkSelector.getValue()));
			treeViewManager.setLimitOfSelectedSymbols(networkManager.getMaxNumberOfSymbols(networkSelector.getValue()));
			limitOfSymbols.setText(treeViewManager.getLimitOfSelectedSymbols()+"");
			numberOfSymbols.setText(treeViewManager.getNumberOfSelectedSymbols()+"");
		}
		mainController.getTab4Controller().addNetworks(networkManager.getSetOfNetworks());
		initializeAllProgressBars();
		initializeCanvas();
		networkManager.connectNetworksWithSlider(learningSpeedSlider);
	}
	
	public ExampleManager getExampleManager() {
		return exampleManager;
	}

	public void setExampleManager(ExampleManager exampleManager) {
		this.exampleManager = exampleManager;
	}

	public void setNetworkManager(NetworkManager networkManager)
	{
		this.networkManager = networkManager;
	}
	
	public void addSymbol(String symbolTag) 
	{
		treeViewManager.addSymbol(symbolTag);
	}
	
	public void removeSymbol(String symbolTag)
	{
		treeViewManager.removeSymbol(symbolTag);
	}
	

	public ArrayList<String> getListOfChoosenSymbols()
	{
		return treeViewManager.getListOfChoosenSymbols();
	}
	public MainController getMainController() {
		return mainController;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	public Slider getLearningSpeedSelector() {
		return learningSpeedSelector;
	}
}
