package controller.tab;

import java.awt.Toolkit;

import controller.MainController;
import controller.tab1.components.CanvasActivator;
import controller.tab1.components.CanvasModyfier;
import controller.tab1.components.TreeManager;
import exapmlemanager.ExampleManager;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeView;

public class Tab1Controller 
{
	final Runnable errorSaund = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
	
	@FXML private TreeView<String> exampleTree;
	@FXML private ComboBox<String> symbolSelector;
	@FXML private Canvas inputCanvas;
	@FXML private Canvas outputCanvas;
	
	private ExampleManager exampleManager;
	private TreeManager treeManager;
	private CanvasActivator inputActivator;
	
	
	@FXML public void initialize()
	{
		System.out.println("Inicjalizowanie tab1");
		
		inputActivator = new CanvasActivator(inputCanvas);
		inputActivator.addObserver((new CanvasModyfier(outputCanvas)));
		
		treeManager = new TreeManager(symbolSelector, exampleTree, inputActivator);
		resetDrawing();
	}

	
	@FXML public void addExample()
	{
		System.out.println("Dodawanie przyk³adu");
		System.out.println(symbolSelector.getValue());
		if(symbolSelector.getValue()==null||symbolSelector.getValue().equals(""))
		{
			if (errorSaund != null) errorSaund.run();
			return;
		}
			
		
		if(!exampleManager.hasSymbol(symbolSelector.getValue()))
		{
			treeManager.addExampleTag();
		}
		
		treeManager.addExample();
		resetDrawing();
	}
	
	
	@FXML public void removeExample()
	{
		System.out.println("Usuwanie przyk³adu");
		if(treeManager.getActuallySelected()==null)
		{
			if(errorSaund!=null) errorSaund.run();
			return;
		}
		
		if(treeManager.getActuallySelected().getParent()==treeManager.getTreeRoot()) //Usuwanie ca³ego symbolu
		{
			treeManager.removeSymbol();
		}
		else //usuwanie konkretnego przyk³adu
		{
			treeManager.removeExample();
		}
		resetDrawing();
	}
	
	@FXML public void resetDrawing()
	{
		inputActivator.clearCanvas();
	}
	
	public TreeManager getTreeManager()
	{
		return treeManager;
	}
	
	public void setMainController(MainController mainController) {
		treeManager.setMainColtroller(mainController);
	}
	
	public ExampleManager getExampleManager() {
		return exampleManager;
	}

	public void setExampleMenager(ExampleManager exampleMenager) {
		System.out.println("Usawianie EM");
		this.exampleManager = exampleMenager;
		treeManager.setExampleManager(exampleMenager);
		treeManager.loadDataFromIndex();
	}
}
