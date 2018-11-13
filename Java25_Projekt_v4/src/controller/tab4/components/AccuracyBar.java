package controller.tab4.components;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.SymbolAccuracy;
import observer.Observer;

public class AccuracyBar implements Observer
{
	private ProgressBar accuracyBar;
	private Label accuracy;
	private Label symbol;
	private int index;
	
	
	public AccuracyBar(ProgressBar accuracyBar, Label accuracy, Label symbol, int index) 
	{
		this.accuracyBar = accuracyBar;
		this.accuracy = accuracy;
		this.symbol = symbol;
		this.index = index;
	}



	@Override
	public void update(Object obj) {
//		System.out.println("Wywo³ywanie paska "+index);
		ArrayList<SymbolAccuracy> symbolList = ((ExampleTester) obj).getSymbolList();
		
		if(symbolList.size()<index)
		{
			accuracyBar.setProgress(0);
			accuracy.setText("0%");
			symbol.setText("");
			return;
		}
			
		
		accuracyBar.setProgress(symbolList.get(index-1).accuracy);
		accuracy.setText(((int)(symbolList.get(index-1).accuracy*100))+"%");
		symbol.setText(symbolList.get(index-1).symbol);
//		Platform.runLater(()->
//		{
//			
//		});
		
	}
	
	
}
