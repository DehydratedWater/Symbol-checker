package controller.tab1.components;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import observer.Observer;
import observer.Publisher;

public class CanvasActivator implements Publisher
{
	private final double BRUSH_SIZE = 20;
	
	private WritableImage lastImage;
	private WritableImage outputImage;
	private Canvas inputCanvas;
	private GraphicsContext inputContext;
	private ArrayList<Observer> observerList;
	private boolean rectCreated;
	 
	
	public CanvasActivator(Canvas canvas)
	{
		
		observerList = new ArrayList<>();
		inputCanvas = canvas;
		inputContext = inputCanvas.getGraphicsContext2D();
		inputContext.setFill(Color.WHITE);
		inputContext.fillRect(0, 0, inputCanvas.getWidth(), inputCanvas.getHeight());
		lastImage = new WritableImage((int)inputCanvas.getWidth(), (int)inputCanvas.getHeight());
		initializeCanvasDrawing();
		clearCanvas();
	}

	public WritableImage[] getTrainingImage()
	{
		return new WritableImage[] {lastImage, getOutputImage()};
	}
	
	public void drawImage(Image img)
	{
		inputContext.drawImage(img, 0, 0);
		inputCanvas.snapshot(null, lastImage);
		notyfieObservers();
	}
	
	private void initializeCanvasDrawing() {
		inputCanvas.setOnMouseDragged(e->{draw(e);
		removeBounds();});

		inputCanvas.setOnMouseReleased(e->{draw(e); inputCanvas.snapshot(null, lastImage); notyfieObservers();});
	}

	private void draw(MouseEvent e) {
		
		removeBounds();
		
		double x = e.getX();
		double y = e.getY();

		if(e.getButton().equals(MouseButton.PRIMARY))
		{
			inputContext.setFill(Color.BLACK);
			inputContext.fillOval(x-BRUSH_SIZE/2, y-BRUSH_SIZE/2, BRUSH_SIZE, BRUSH_SIZE);
		}
		else if(e.getButton().equals(MouseButton.SECONDARY))
		{
			inputContext.setFill(Color.WHITE);
			inputContext.fillOval(x-BRUSH_SIZE/2, y-BRUSH_SIZE/2, BRUSH_SIZE, BRUSH_SIZE);
		}
		
	}

	public void removeBounds() {
		if(isRectCreated())
		{
			inputContext.drawImage(lastImage, 0, 0);
			setRectCreated(false);
		}
	}
	
	public boolean isRectCreated() {
		return rectCreated;
	}

	public void setRectCreated(boolean rectCreated) {
		this.rectCreated = rectCreated;
	}

	public WritableImage getLastImage() {
		return lastImage;
	}

	public void setLastImage(WritableImage lastImage) {
		this.lastImage = lastImage;
	}

	
	public Canvas getInputCanvas() {
		return inputCanvas;
	}

	public void setInputCanvas(Canvas inputCanvas) {
		this.inputCanvas = inputCanvas;
	}
	
	public void clearCanvas()
	{
		inputContext.setFill(Color.WHITE);
		inputContext.fillRect(0, 0, inputCanvas.getWidth(), inputCanvas.getHeight());
		inputCanvas.snapshot(null, lastImage);
		notyfieObservers();
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
		for(Observer o: observerList)
			o.update(this);
	}

	public WritableImage getOutputImage() {
		return outputImage;
	}

	public void setOutputImage(WritableImage outputImage) {
		this.outputImage = outputImage;
	}
}
