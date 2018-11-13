package controller.tab2.components;

import javafx.scene.control.Label;
import observer.Observer;

public class LearnSpeedLabel implements Observer
{
	private Label learningSpeedLabel;
	
	public LearnSpeedLabel(Label learningSpeedLabel)
	{
		this.learningSpeedLabel = learningSpeedLabel;
	}

	@Override
	public void update(Object obj) 
	{
		learningSpeedLabel.setText(((LearningSpeedSlider)obj).getValue()+"");
	}

}
