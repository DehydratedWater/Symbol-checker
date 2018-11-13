package exapmlemanager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;

import exapmlemanager.indexstructure.ExampleIndex;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ExampleManager 
{
	private ExampleIndex index;
	private String mainPath;
	
	private boolean isBufferReady;
	private String bufferSymbolTag;
	private ArrayList<BufferedImage> trainingBuffer;
	private ArrayList<BufferedImage> previewBuffer;
	
	public ExampleManager(String mainPath)
	{
		File path = new File(mainPath);
		if(path.mkdirs())
		{
			System.out.println("Utworzono folder /examples");
		}
		this.mainPath = mainPath;
		//Loading index file
		File indexFile = new File(mainPath+"/index.obj");
		if(indexFile.exists())
		{
			System.out.println("£adowanie zapisanego indexu");
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(indexFile));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					index = (ExampleIndex)in.readObject();
					System.out.println("Za³adowano index z symbolami: "+index.getSymbolList().keySet());
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
		}
		if(index==null)
		{
			System.out.println("Tworzenie nowej bazy przyk³adów");
			index = new ExampleIndex();
		}
			
		
		
	}
	
	public void serializeIndex() throws IOException
	{
		FileOutputStream fos = new FileOutputStream(mainPath+"/index.obj");
		ObjectOutputStream ous = new ObjectOutputStream(fos);
		ous.writeObject(index);
		ous.flush();
		ous.close();
	}
	
	public void addExample(String symbolTag, WritableImage input, WritableImage input2)
	{
		index.addExample(mainPath, symbolTag);
		
		File preview = new File(mainPath+"/"+symbolTag+"/"+(index.getNumberOfExample(symbolTag))+".png");
		File train = new File(mainPath+"/"+symbolTag+"/"+(index.getNumberOfExample(symbolTag))+"_t.png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(input, null), "PNG", preview);
			ImageIO.write(SwingFXUtils.fromFXImage(input2, null), "PNG", train);
		} catch (IOException e) {
			index.removeExample(symbolTag);
			System.err.println("Can't save example");
			e.printStackTrace();
		}
		
	}
	
	public int getNumberOfExamples(String symbolTag)
	{
		return index.getNumberOfExample(symbolTag);
	}
	
	public Image getExampleTraining(String symbolTag, int index)
	{
//		System.out.println("£adowanie przyk³adu treningowego");
		Image training = null;
//		System.out.println(new File(mainPath+"/"+symbolTag+"/"+index+"_t.png"));
		try {
			FileInputStream fis = new FileInputStream(new File(mainPath+"/"+symbolTag+"/"+index+"_t.png"));
			training = (new Image(fis));
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		System.out.println("Za³adowano");
		return training;
	}
	
	public Image getExamplePreview(String symbolTag, int index)
	{
		System.out.println("£adowanie przyk³adu");
		Image preview = null;
		System.out.println(new File(mainPath+"/"+symbolTag+"/"+index+".png"));
		try {
			FileInputStream fis = new FileInputStream(new File(mainPath+"/"+symbolTag+"/"+index+".png"));
			preview = (new Image(fis));
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return preview;
	}
	
	public void loadBuffer(String symbolTag) throws IOException
	{
		trainingBuffer = new ArrayList<BufferedImage>(getNumberOfExamples(symbolTag));
		previewBuffer = new ArrayList<BufferedImage>(getNumberOfExamples(symbolTag));
		bufferSymbolTag = symbolTag;
		
		for(int i = 0; i <= getNumberOfExamples(symbolTag); i++)
		{
			trainingBuffer.add(ImageIO.read(new File(mainPath+"/"+symbolTag+"/"+i+"_t.png")));
			previewBuffer.add(ImageIO.read(new File(mainPath+"/"+symbolTag+"/"+i+".png")));
		}
		
		isBufferReady = true;
	}
	
	public void clearBuffer()
	{
		trainingBuffer = null;
		previewBuffer = null;
		bufferSymbolTag = null;
		isBufferReady = false;
	}
	
	public void removeSymbol(String symbolTag) 
	{
		System.out.println("Usuwanie folderu "+new File(mainPath+"/"+symbolTag)+" isDirectory "+new File(mainPath+"/"+symbolTag).isDirectory());
		index.removeSymbol(symbolTag);
		try {
			deleteFolder(new File(mainPath+"/"+symbolTag));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFolder(File f) throws IOException 
	{
		  if (f.isDirectory()) 
		  {
		    for (File c : f.listFiles())
		      deleteFolder(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
		}
	
	public void removeExample(String symbolTag, int index)
	{
		this.index.removeExample(symbolTag);
		//System.out.println("Usuwanie: "+(mainPath+"/"+symbolTag+"/"+index+".png")+" "+(new File((mainPath+"/"+symbolTag+"/"+index+".png")).exists()));
		boolean isPreviewRemoved = (new File(mainPath+"/"+symbolTag+"/"+index+".png")).delete();
		boolean isTrainingRemoved = (new File(mainPath+"/"+symbolTag+"/"+index+"_t.png")).delete();
		
		if(!isPreviewRemoved||!isTrainingRemoved)
			System.err.println("Problem usuniêciem przyk³adu");
		
		//System.out.println("Czy usuniêto "+isPreviewRemoved+" "+isTrainingRemoved);
		
		//System.out.println("Zmiana nazwy");
		for(int i = index; i <= getNumberOfExamples(symbolTag); i++)
		{
			new File(mainPath+"/"+symbolTag+"/"+(i+1)+".png").renameTo(new File(mainPath+"/"+symbolTag+"/"+(i)+".png"));
			new File(mainPath+"/"+symbolTag+"/"+(i+1)+"_t.png").renameTo(new File(mainPath+"/"+symbolTag+"/"+(i)+"_t.png"));
		}
	}
	
	public HashSet<String> getSymbolTagSet()
	{
		return new HashSet<>(index.getSymbolList().keySet());
	}
	
	
	public BufferedImage getBufferedTrainingExample(int index)
	{
		return trainingBuffer.get(index);
	}
	
	public BufferedImage getBufferedPreviewExample(int index)
	{
		return previewBuffer.get(index);
	}
	public boolean isBufferReady()
	{
		return isBufferReady;
	}
	public String getBufferedSymbolTag()
	{
		return bufferSymbolTag;
	}

	public boolean hasSymbol(String value) {
		if(index.getSymbolList().containsKey(value))
			return true;
		return false;
	}

	
}
