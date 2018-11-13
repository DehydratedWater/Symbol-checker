package controller.tab1.components;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import observer.Observer;

public class CanvasModyfier implements Observer
{
	private Canvas outputCanvas;
	private final double SCALE = 1.0/10;
	
	public CanvasModyfier(Canvas canvas) {
		outputCanvas = canvas;
		outputCanvas.getGraphicsContext2D().setFill(Color.WHITE);
		outputCanvas.getGraphicsContext2D().fillRect(0, 0, outputCanvas.getWidth(), outputCanvas.getHeight());
	}
	
	@Override
	public void update(Object obj) {
		CanvasActivator inputCanvas = (CanvasActivator)obj;
		
		generateOutputCanvas(inputCanvas);
	}
	
	private void generateOutputCanvas(CanvasActivator inputCanvasActivator)
	{
		
		Canvas inputCanvas = inputCanvasActivator.getInputCanvas();
		
		WritableImage inputImage = new WritableImage((int)inputCanvas.getWidth(), (int)inputCanvas.getHeight());
		inputCanvas.snapshot(null, inputImage);
		PixelReader reader = inputImage.getPixelReader();
		
		double[] minMax = findBounds(inputCanvas, reader);
		
		double width = (minMax[2]-minMax[0]);
		double height = (minMax[3]-minMax[1]);
		System.out.println(minMax[0]+" "+minMax[1]+" / "+width+" "+height);
		if(width<0)
			width = inputCanvas.getWidth();
		if(height<0)
			height = inputCanvas.getHeight();
		
		System.out.println(minMax[0]+" "+minMax[1]+" / "+width+" "+height);
		
		
		
		double scaleH = inputCanvas.getHeight()/height;
		double scaleW = inputCanvas.getWidth()/width;
		double scale = (scaleH<scaleW) ? scaleH : scaleW;
//		System.out.println("skale: "+scaleW+" "+scaleH);
		
		positionImageOnCanvas(inputCanvas, minMax[0], minMax[1], width, height, scale);
		
		WritableImage wi = new WritableImage((int)(inputCanvas.getWidth()*SCALE), (int)(inputCanvas.getHeight()*SCALE));
		SnapshotParameters sp =  new SnapshotParameters();
		sp.setTransform(Transform.scale(SCALE, SCALE));
		outputCanvas.snapshot(sp, wi);
		inputCanvasActivator.setOutputImage(wi);
		drawScaledImageWithoutSmoothing(wi);
		
		inputCanvasActivator.setRectCreated(true);
		inputCanvas.getGraphicsContext2D().setLineWidth(1);
		inputCanvas.getGraphicsContext2D().setStroke(Color.RED);
		inputCanvas.getGraphicsContext2D().strokeRect(minMax[0], minMax[1], width, height);
	}

	private double[] findBounds(Canvas inputCanvas, PixelReader reader) {
		double[] minMax = new double[4];
		minMax[0] = inputCanvas.getWidth();
		minMax[1] = inputCanvas.getHeight();
		minMax[2] = 0;
		minMax[3] = 0;
		
		for (int y = 0; y < inputCanvas.getHeight(); y++) {
		    for (int x = 0; x < inputCanvas.getWidth(); x++) {
		        if(reader.getArgb(x, y)!=java.awt.Color.WHITE.getRGB())
		        {
		        	if(x<minMax[0])
		        		minMax[0] = x;
		        	if(x>minMax[2])
		        		minMax[2] = x;
		        	if(y<minMax[1])
		        		minMax[1] = y;
		        	if(y>minMax[3])
		        		minMax[3] = y;
		        }
		    }
		}
		return minMax;
	}

	private void positionImageOnCanvas(Canvas inputCanvas, double minX, double minY, double width, double height,
			double scale) {
		Canvas centerdImage = new Canvas(width, height);
		GraphicsContext centeredContext = centerdImage.getGraphicsContext2D();
		WritableImage centeredImage = new WritableImage((int)inputCanvas.getWidth(), (int)inputCanvas.getHeight());
		inputCanvas.snapshot(null, centeredImage);
		
		
		centeredContext.drawImage(centeredImage, -minX, -minY);
		WritableImage cutedImage = new WritableImage((int)inputCanvas.getWidth(), (int)inputCanvas.getHeight());
		SnapshotParameters sp3 =  new SnapshotParameters();
		sp3.setTransform(Transform.scale(scale, scale));
		
		centerdImage.snapshot(sp3, cutedImage);
		outputCanvas.getGraphicsContext2D().fillRect(0, 0, outputCanvas.getWidth(), outputCanvas.getHeight());
		outputCanvas.getGraphicsContext2D().drawImage(cutedImage, (outputCanvas.getWidth()-width*scale)/2, (outputCanvas.getHeight()-height*scale)/2);
	}

	private void drawScaledImageWithoutSmoothing(WritableImage img)
	{
		PixelReader reader = img.getPixelReader();
		PixelWriter writer = outputCanvas.getGraphicsContext2D().getPixelWriter();
		for (int y = 0; y < outputCanvas.getHeight(); y++) {
		    for (int x = 0; x < outputCanvas.getWidth(); x++) {
		        writer.setArgb(x, y, reader.getArgb((int)(x * SCALE), (int)(y * SCALE)));
		    }
		}
	}


}
