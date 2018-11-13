package controller.tab4.components;

import java.util.ArrayList;

import javafx.scene.control.Label;
import model.SymbolAccuracy;
import observer.Observer;

public class SymbolLabelControler implements Observer
{
	private Label symbol;
	
	public SymbolLabelControler(Label symbol) {
		this.symbol = symbol;
	}
		
	@Override
	public void update(Object obj) {
		ArrayList<SymbolAccuracy> symbolList = ((ExampleTester) obj).getSymbolList();
		
		if(symbolList.size()<1)
		{
			symbol.setText("NULL");
			return;
		}
			
		
		symbol.setText(symbolList.get(0).symbol);
		
	}

}
