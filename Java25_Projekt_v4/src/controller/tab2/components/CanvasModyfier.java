package controller.tab2.components;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import model.LearnMenager;
import observer.Observer;

public class CanvasModyfier implements Observer
{
	private Canvas outputCanvas;
	private final double SCALE = 1.0/10;
	
	public CanvasModyfier(Canvas canvas) {
		outputCanvas = canvas;
	}
	
	@Override
	public void update(Object obj) {
		Platform.runLater(() -> 
		{
		Image training = ((LearnMenager)obj).preview;	
//		System.out.println("Rysowanie przyk³adu");
		if(training!=null)
			drawScaledImageWithoutSmoothing(training);
		});

	}

	private void drawScaledImageWithoutSmoothing(Image img)
	{
		outputCanvas.getGraphicsContext2D().setFill(Color.WHITE);
		outputCanvas.getGraphicsContext2D().fillRect(0, 0, outputCanvas.getWidth(), outputCanvas.getHeight());
		PixelReader reader = img.getPixelReader();
		PixelWriter writer = outputCanvas.getGraphicsContext2D().getPixelWriter();
		for (int y = 0; y < outputCanvas.getHeight(); y++) {
		    for (int x = 0; x < outputCanvas.getWidth(); x++) {
		        writer.setArgb(x, y, reader.getArgb((int)(x * SCALE), (int)(y * SCALE)));
		    }
		}
	}
}
