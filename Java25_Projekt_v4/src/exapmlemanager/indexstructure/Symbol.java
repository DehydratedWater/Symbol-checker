package exapmlemanager.indexstructure;

import java.io.Serializable;

public class Symbol implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String symbolTag;
	private int numberOfExamples;
	
	public Symbol(String symbolTag)
	{
		this.symbolTag = symbolTag;
	}
	
	public void addNewExample()
	{
		numberOfExamples++;
	}
	
	public void removeExample()
	{
		numberOfExamples--;
	}
	
	public String getSymbolTag() {
		return symbolTag;
	}
	public void setSymbolTag(String symbolTag) {
		this.symbolTag = symbolTag;
	}
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	public void setNumberOfExamples(int numberOfExamples) {
		this.numberOfExamples = numberOfExamples;
	}

}
