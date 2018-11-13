package controller.tab1.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import controller.MainController;
import exapmlemanager.ExampleManager;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;

public class TreeManager 
{
	private TreeView<String> exampleTree;
	private TreeItem<String> treeRoot;
	private TreeItem<String> actuallySelected;
	private ComboBox<String> symbolSelector;
	private HashMap<String, TreeItem<String>> treeSymbolElements;
	private CanvasActivator inputActivator;
	private ExampleManager exampleManager;
	private MainController mainController;
	
	public TreeManager(ComboBox<String> symbolSelector, TreeView<String> exampleTree, CanvasActivator inputActivator)
	{
		System.out.println("Inicjalizowanie Mendad¿era drzewa");
		this.symbolSelector = symbolSelector;
		this.inputActivator = inputActivator;
		
		this.exampleTree = exampleTree;
		treeRoot = new TreeItem<String>();
		exampleTree.setRoot(treeRoot);
		treeRoot.setExpanded(true);
		treeSymbolElements = new HashMap<>();
		
		symbolSelector.setOnAction(e->{
			String symbol = symbolSelector.getValue().replaceAll(" ", "_");
			symbol = symbol.toLowerCase();
			symbolSelector.setValue(symbol);
		});
		
		initializeTreeViewInteractions();
		System.out.println("Koniec");
	}
	
	

	public void initializeTreeViewInteractions() {
		exampleTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)->
		{
			System.out.println(newValue.getValue());
			if(newValue.getParent()!=treeRoot)
			{
				actuallySelected = newValue;
				String tab[] = getNameFromTreeItem(newValue);
				System.out.println("Element przyk³adu "+Arrays.toString(tab)+" "+Integer.parseInt(tab[1]));
				
				inputActivator.drawImage(exampleManager.getExamplePreview(tab[0], Integer.parseInt(tab[1])));
			}
			else
			{
				actuallySelected = newValue;
				symbolSelector.setValue(newValue.getValue().substring(8));
				System.out.println("Element symbolu");
			}
		});
	}
	
	public void removeExample()
	{
		System.out.println("Usuwanie przyk³adu");
		String tab[] = getNameFromTreeItem(actuallySelected);
		System.out.println(Arrays.toString(tab));
	
		for(TreeItem<String> item : actuallySelected.getParent().getChildren())
		{
			if(getIndex(item)>Integer.parseInt(tab[1]))
				item.setValue((getIndex(item)-1)+".png");
		}
	
		treeSymbolElements.get(tab[0]).getChildren().remove(Integer.parseInt(tab[1])-1);
		exampleManager.removeExample(tab[0], Integer.parseInt(tab[1]));
	}
	
	public void removeSymbol()
	{
		String symbol = actuallySelected.getValue().substring(8);
		System.out.println(symbol);
		exampleManager.removeSymbol(symbol);
		treeRoot.getChildren().remove(treeSymbolElements.get(symbol));
		treeSymbolElements.remove(symbol);
		symbolSelector.getItems().remove(symbol);
		mainController.getTab2Controller().removeSymbol(symbol);
		mainController.getTab4Controller().removeSymbol(symbol);
	}
	
	public void addExample()
	{
		WritableImage[] images = inputActivator.getTrainingImage();
		System.out.println("Dodawanie");
		exampleManager.addExample(symbolSelector.getValue(), images[0], images[1]);
		treeSymbolElements.get(symbolSelector.getValue()).getChildren().add(new TreeItem<String>(exampleManager.getNumberOfExamples(symbolSelector.getValue())+".png"));
	}
	
	public void addExample(WritableImage[] images, String symbol)
	{
		System.out.println("Dodawanie");
		exampleManager.addExample(symbol, images[0], images[1]);
		treeSymbolElements.get(symbol).getChildren().add(new TreeItem<String>(exampleManager.getNumberOfExamples(symbol)+".png"));
	}

	public void addExampleTag()
	{
		TreeItem<String> symbol = new TreeItem<String>("Symbol: "+symbolSelector.getValue());
		treeSymbolElements.put(symbolSelector.getValue(), symbol);
		treeRoot.getChildren().add(symbol);
		symbolSelector.getItems().add(symbolSelector.getValue());
		mainController.getTab2Controller().addSymbol(symbolSelector.getValue());
		mainController.getTab4Controller().addSymbol(symbolSelector.getValue());
	}
	
	public void loadDataFromIndex() {
		System.out.println("1 "+exampleManager);
		HashSet<String> symbolList = exampleManager.getSymbolTagSet();
		
		System.out.println(symbolList);
		for(String symbolTag: symbolList)
		{
			TreeItem<String> symbol = new TreeItem<String>("Symbol: "+symbolTag);
			mainController.getTab2Controller().addSymbol(symbolTag);
			treeSymbolElements.put(symbolTag, symbol);
			treeRoot.getChildren().add(symbol);
			symbolSelector.getItems().add(symbolTag);
			mainController.getTab4Controller().addSymbol(symbolTag);
		}
		System.out.println("2");
		for(String symbolTag: symbolList)
		{
			for(int i = 1; i <= exampleManager.getNumberOfExamples(symbolTag); i++)
			{
				treeSymbolElements.get(symbolTag).getChildren().add(new TreeItem<String>(i+".png"));
			}
		}
		symbolSelector.setPromptText("Select Symbol");
	}
	
	private String[] getNameFromTreeItem(TreeItem<String> item)
	{
		return new String[]{item.getParent().getValue().substring(8), item.getValue().substring(0, item.getValue().length()-4)};
	}
	
	private int getIndex(TreeItem<String> item)
	{
		return Integer.parseInt(item.getValue().substring(0, item.getValue().length()-4));
	}
	public TreeItem<String> getActuallySelected() {
		return actuallySelected;
	}

	public void setActuallySelected(TreeItem<String> actuallySelected) {
		this.actuallySelected = actuallySelected;
	}

	public TreeItem<String> getTreeRoot() {
		return treeRoot;
	}

	public void setTreeRoot(TreeItem<String> treeRoot) {
		this.treeRoot = treeRoot;
	}

	public ExampleManager getExampleManager() {
		return exampleManager;
	}

	public void setExampleManager(ExampleManager exampleManager) {
		this.exampleManager = exampleManager;
	}

	public void setMainColtroller(MainController mainController2) {
		this.mainController = mainController2;
		
	}
}
