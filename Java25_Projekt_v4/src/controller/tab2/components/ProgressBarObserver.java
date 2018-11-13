package controller.tab2.components;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.LearnMenager;
import observer.Observer;

public class ProgressBarObserver implements Observer
{
	private ProgressBar progressBar;
	private Label progressLabel;
	private ProgressBarInterface function;
	private double[] value;
	
	public ProgressBarObserver(ProgressBar progressBar, Label progressLabel, ProgressBarInterface function) {
		this.progressBar = progressBar;
		this.progressLabel = progressLabel;
		this.function = function;
		value = new double[1];
	}
	
	@Override
	public void update(Object obj) 
	{
		LearnMenager lm = (LearnMenager)obj;
//		System.out.println("Aktualizowanie paska postêpu");
		
		Platform.runLater(() -> 
		{
			function.setValue(value, lm);
			progressBar.setProgress(value[0]);
			progressLabel.setText(((int)(value[0]*100))+"%");
		});
		
		
	}
	
}
