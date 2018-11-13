package controller.tab2.components;

import javafx.application.Platform;
import javafx.scene.control.Label;
import model.LearnMenager;
import observer.Observer;

public class LabelRefresher implements Observer
{
	private Label label;
	private LabelRefresherSetter labelRefresher;
	
	public LabelRefresher(Label label, LabelRefresherSetter labelRefresherSetter) {
		this.label = label;
		this.labelRefresher = labelRefresherSetter; 
	}
	
	@Override
	public void update(Object obj) 
	{
		LearnMenager lm = (LearnMenager) obj;
		Platform.runLater(()->{labelRefresher.setValueOnLabel(label, lm);});
	}

}
