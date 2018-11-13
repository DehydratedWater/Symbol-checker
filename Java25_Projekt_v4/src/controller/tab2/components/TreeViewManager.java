package controller.tab2.components;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeViewManager 
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	private TreeItem<CheckBox> symbolRoot;
	private HashMap<String, TreeItem<CheckBox>> symbolList;
	private TreeView<CheckBox> symbolSelector;
	private int numberOfSelectedSymbols;
	private int limitOfSelectedSymbols = 100;
	private Label labelOfSelectedSymbols;

	public TreeViewManager(TreeView<CheckBox> symbolSelector, Label labelOfSelectedSymbols)
	{
		this.symbolSelector = symbolSelector;
		symbolRoot = new TreeItem<CheckBox>();
		this.symbolSelector.setRoot(symbolRoot);
		symbolList = new HashMap<>();
		this.labelOfSelectedSymbols = labelOfSelectedSymbols;
	}
	
	public void addSymbol(String symbolTag)
	{
		System.out.println("Dodawanie: "+symbolTag);
		TreeItem<CheckBox> item = new TreeItem<CheckBox>(new CheckBox(symbolTag));
		item.getValue().setOnAction(e->
		{
			
			if(item.getValue().isSelected())
				numberOfSelectedSymbols++;
			else
				numberOfSelectedSymbols--;
//			System.out.println(numberOfSelectedSymbols); 
			if(numberOfSelectedSymbols>limitOfSelectedSymbols)
			{
				if(errorSaund!=null) errorSaund.run();
				item.getValue().setSelected(false);
				numberOfSelectedSymbols--;
			}
			labelOfSelectedSymbols.setText(numberOfSelectedSymbols+"");
		});
		symbolList.put(symbolTag, item);
		symbolRoot.getChildren().add(item);
	}
	public void removeSymbol(String symbolTag)
	{
		symbolRoot.getChildren().remove(symbolList.get(symbolTag));
	}

	public void setUsedSymbolDisabled(String[] symbols)
	{
		enableAllSymbols();
		for(String symbol : symbols)
		{
			if(symbol!=null)
				numberOfSelectedSymbols++;
		}
		
		for(TreeItem<CheckBox> item : symbolRoot.getChildren())
		{
			for(String symbol : symbols)
				if(item.getValue().getText().equals(symbol))
				{
					
					item.getValue().setSelected(true);
					item.getValue().setDisable(true);
				}
		}
	}
	
	public void setUsedSymbolDisabled(ArrayList<String> symbols)
	{
		enableAllSymbols();
		for(String symbol : symbols)
		{
			if(symbol!=null)
				numberOfSelectedSymbols++;
		}
		for(TreeItem<CheckBox> item : symbolRoot.getChildren())
		{
			for(String symbol : symbols)
				if(item.getValue().getText().equals(symbol))
				{
					item.getValue().setSelected(true);
					item.getValue().setDisable(true);
				}
		}
	}
	
	public void enableAllSymbols()
	{
		numberOfSelectedSymbols = 0;
		for(TreeItem<CheckBox> item : symbolRoot.getChildren())
		{
			item.getValue().setDisable(false);
			item.getValue().setSelected(false);
		}
	}
	
	public ArrayList<String> getListOfChoosenSymbols()
	{
		ArrayList<String> symbols = new ArrayList<>();
		for(TreeItem<CheckBox> item : symbolRoot.getChildren())
		{
			if(item.getValue().isSelected())
				symbols.add(item.getValue().getText());
		}
		return symbols;
	}
	
	public int getNumberOfSelectedSymbols() {
		return numberOfSelectedSymbols;
	}

	public void setNumberOfSelectedSymbols(int numberOfSelectedSymbols) {
		this.numberOfSelectedSymbols = numberOfSelectedSymbols;
	}

	public int getLimitOfSelectedSymbols() {
		return limitOfSelectedSymbols;
	}

	public void setLimitOfSelectedSymbols(int limitOfSelectedSymbols) {
		this.limitOfSelectedSymbols = limitOfSelectedSymbols;
	}
}
