package controller.tab2.components;

import java.util.Arrays;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NeuronNetworkSizeSetter 
{
	private static int[] answer;
	public static int[] getNeuronNetworkSize(int numberOfChoosenSymbols)
	{
		System.out.println("Pocz¹tkowa ilosæ symboli to: "+numberOfChoosenSymbols);
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Neuron network setter");
		
		Label label = new Label("Set neuron network parameters");
		Label label1 = new Label("Enter hidden layer size: ");
		Slider sizeOfHiddenLayer = new Slider(10, 600, 40);
		Label set1 = new Label("40/600");
		
		Label label2 = new Label("Enter output layer size:");
		Slider sizeOfOutputLayer = new Slider(numberOfChoosenSymbols, 100, numberOfChoosenSymbols);
		Label set2 = new Label(numberOfChoosenSymbols+"/100");
		Button ok = new Button("OK");
		
		sizeOfHiddenLayer.setOnMouseDragged(e->{set1.setText((int)sizeOfHiddenLayer.getValue()+"/600");});
		sizeOfOutputLayer.setOnMouseDragged(e->{set2.setText((int)sizeOfOutputLayer.getValue()+"/100");});
		
		window.setOnCloseRequest(e->
		{
			answer = new int[] {(int)sizeOfHiddenLayer.getValue(), (int)sizeOfOutputLayer.getValue()};
			System.out.println("Wybrano "+Arrays.toString(answer));
		});
		
		ok.setOnAction(e->
		{
			answer = new int[] {(int)sizeOfHiddenLayer.getValue(), (int)sizeOfOutputLayer.getValue()};
			System.out.println("Wybrano "+Arrays.toString(answer));
			window.close();
		});
		VBox layout = new VBox(5);
		layout.getChildren().addAll(label, label1, set1, sizeOfHiddenLayer, label2, set2, sizeOfOutputLayer, ok);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 600, 200);
		window.setResizable(false);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
