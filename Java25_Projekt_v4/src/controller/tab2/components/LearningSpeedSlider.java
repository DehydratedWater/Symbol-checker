package controller.tab2.components;

import java.util.ArrayList;

import javafx.scene.control.Slider;
import observer.Observer;
import observer.Publisher;

public class LearningSpeedSlider implements Publisher
{
	private ArrayList<Observer> observerList;
	private double value = 0.1;
	private Slider learningSpeedSelector;
	
	public LearningSpeedSlider(Slider learningSpeedSelector)
	{
		this.learningSpeedSelector = learningSpeedSelector;
		observerList = new ArrayList<>();
		learningSpeedInitialization();
	}

	private void learningSpeedInitialization() 
	{
		learningSpeedSelector.setShowTickMarks(true);
		learningSpeedSelector.setShowTickLabels(true);
		learningSpeedSelector.setMajorTickUnit(0.1);
		learningSpeedSelector.setMin(0);
		learningSpeedSelector.setMax(1);
		learningSpeedSelector.setValue(value);
		
		learningSpeedSelector.setOnMouseClicked(e->
		{ 
			valuateSlider();
		});
		learningSpeedSelector.setOnMouseDragged(e->
		{ 
			valuateSlider();
		});
	}

	private void valuateSlider() {
		value = (((int)(learningSpeedSelector.getValue()*10000)))/10000.0;
			if(value==0)
				value=0.0001;
			notyfieObservers();
	}
	
	public double getValue()
	{
		return value;
	}
	
	@Override
	public void addObserver(Observer o) {
		observerList.add(o);
		
	}

	@Override
	public void deleteObserver(Observer o) {
		observerList.remove(o);
		
	}

	@Override
	public void notyfieObservers() {
		for(Observer o : observerList)
			o.update(this);
		
	}
	

}
