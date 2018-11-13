package model;

public class SymbolAccuracy implements Comparable<SymbolAccuracy>
{
	public String symbol;
	public double accuracy;
	
	public SymbolAccuracy(String symbol, double accuracy)
	{
		this.symbol = symbol;
		this.accuracy = accuracy;
	}

	@Override
	public int compareTo(SymbolAccuracy o) {
		if(o.accuracy==accuracy)
			return 0;
		if(o.accuracy>accuracy)
			return 1;
		else
			return -1;
	}
	
	public String toString()
	{
		return symbol+" --> "+accuracy;
	}
}
