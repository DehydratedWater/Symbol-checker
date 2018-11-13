package exapmlemanager.indexstructure;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class ExampleIndex implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Symbol> symbolList;

	public ExampleIndex()
	{
		symbolList = new HashMap<String, Symbol>();
	}
	
	public HashMap<String, Symbol> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(HashMap<String, Symbol> symbolList) {
		this.symbolList = symbolList;
	}
	
	public int getNumberOfExample(String exampleTag)
	{
		return symbolList.get(exampleTag).getNumberOfExamples();
	}
	public void addExample(String path, String exampleTag)
	{
		if(symbolList.get(exampleTag)==null)
		{
			
			File newTag = new File(path+"/"+exampleTag);
			boolean isCreated = newTag.mkdirs();
			System.out.println("Utworzono "+(path+"/"+exampleTag)+" --> "+isCreated);

			symbolList.put(exampleTag, new Symbol(exampleTag));
			symbolList.get(exampleTag).addNewExample();
		}	
		else
			symbolList.get(exampleTag).addNewExample();
	}
	
	public void removeExample(String symbolTag)
	{
		symbolList.get(symbolTag).removeExample();
	}

	public void removeSymbol(String symbolTag) 
	{
		symbolList.remove(symbolTag);
	}
	
}
