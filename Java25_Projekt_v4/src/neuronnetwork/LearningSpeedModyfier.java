package neuronnetwork;

import java.io.Serializable;

import controller.tab2.components.LearningSpeedSlider;
import observer.Observer;

public class LearningSpeedModyfier implements Observer, Serializable
{
	private static final long serialVersionUID = 1L;
	private double learningSpeed;

	public LearningSpeedModyfier() {}
	
	public LearningSpeedModyfier(double learningSpeed) {
		this.learningSpeed = learningSpeed;
	}

	@Override
	public void update(Object obj) {
		setLearningSpeed(((LearningSpeedSlider)obj).getValue());
	}

	public double getLearningSpeed() {
		return learningSpeed;
	}

	public void setLearningSpeed(double learningSpeed) {
		this.learningSpeed = learningSpeed;
	}
	
	
}
