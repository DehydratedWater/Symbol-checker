package controller.tab;

import java.awt.Toolkit;
import java.util.Set;

import controller.MainController;
import controller.tab4.components.AccuracyBar;
import controller.tab1.components.CanvasActivator;
import controller.tab1.components.CanvasModyfier;
import controller.tab4.components.ExampleTester;
import controller.tab4.components.SymbolLabelControler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.NetworkManager;

public class Tab4Controller
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	
	@FXML private Canvas inputCanvas;
	@FXML private Canvas outputCanvas;
	@FXML private ProgressBar accuracyBar1, accuracyBar2, accuracyBar3, accuracyBar4, accuracyBar5;
	@FXML private Label accuracy1, accuracy2, accuracy3, accuracy4, accuracy5;
	@FXML private Label symbol1, symbol2, symbol3, symbol4, symbol5;
	@FXML private ChoiceBox<String> networkSetter;
	@FXML private Label symbol;
	@FXML private ChoiceBox<String> symbolSelector;
	
	private CanvasActivator inputActivator;
	private ExampleTester exampleTester;
	private NetworkManager networkManager;
	private MainController mainController;
	
	
	@FXML public void initialize()
	{
		System.out.println("Inicializacja tab4 "+symbolSelector);

		inputActivator = new CanvasActivator(inputCanvas);
		inputActivator.addObserver(new CanvasModyfier(outputCanvas));
		
		networkSetter.setOnAction(e->
		{
			System.out.println("Powiadamianie sieci by obliczyæ rezultat: "+exampleTester+" "+networkSetter+" "+inputActivator);
			exampleTester.setChoosenNetwork(networkSetter.getValue()); 
			inputActivator.removeBounds();
			inputActivator.notyfieObservers();
		});
		
		
	}
	
	@FXML public void reset()
	{
		inputActivator.clearCanvas(); 
	}

	@FXML public void addSymbol()
	{
		if(symbolSelector.getValue()==null)
		{
			if (errorSaund != null) errorSaund.run();
			return;
		}
			
		mainController.getTab1Controller().getTreeManager().addExample(inputActivator.getTrainingImage(), symbolSelector.getValue());
		inputActivator.clearCanvas();
	}
	
	public void addNetwork(String networkName) {
		networkSetter.getItems().add(networkName);
		networkSetter.setValue(networkName);
		
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
		
	}
	
	public void initializeManagerComponents()
	{
		exampleTester = new ExampleTester(networkManager);
		inputActivator.addObserver(exampleTester);
		
		AccuracyBar bar1 = new AccuracyBar(accuracyBar1, accuracy1, symbol1, 1);
		AccuracyBar bar2 = new AccuracyBar(accuracyBar2, accuracy2, symbol2, 2);
		AccuracyBar bar3 = new AccuracyBar(accuracyBar3, accuracy3, symbol3, 3);
		AccuracyBar bar4 = new AccuracyBar(accuracyBar4, accuracy4, symbol4, 4);
		AccuracyBar bar5 = new AccuracyBar(accuracyBar5, accuracy5, symbol5, 5);
		
		exampleTester.addObserver(bar1);
		exampleTester.addObserver(bar2);
		exampleTester.addObserver(bar3);
		exampleTester.addObserver(bar4);
		exampleTester.addObserver(bar5);
		
		SymbolLabelControler symbolControl = new SymbolLabelControler(symbol);
		
		exampleTester.addObserver(symbolControl);
		
		if(networkSetter.getItems().size()>0)
			networkSetter.setValue(networkSetter.getItems().get(0));
		
		if(symbolSelector.getItems().size()>0)
			symbolSelector.setValue(symbolSelector.getItems().get(0));
	}

	public void addSymbol(String symbol) {
		symbolSelector.getItems().add(symbol);
	}

	
	public void removeSymbol(String symbol) {
		symbolSelector.getItems().remove(symbol);
		if(symbolSelector.getItems().size()>0)
			symbolSelector.setValue(symbolSelector.getItems().get(0));
	}

	public MainController getMainController() {
		return mainController;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void addNetworks(Set<String> setOfNetworks) {
		networkSetter.getItems().addAll(setOfNetworks);
		
	}

	public void removeNetwork(String value) {
		networkSetter.getItems().remove(value);
		if(networkSetter.getItems().size()>0)
			networkSetter.setValue(networkSetter.getItems().get(0));
	}
	

}
