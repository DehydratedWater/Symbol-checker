package controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoAbout 
{
	private static final String title = "Aplikacja rozpoznaj¹ca symbole\n";
	private static final String info = "Aplikacja prezentuje wykorzystanie sieci neuronowych do rozpoznawania okreœlonych przez u¿ytkownika symboli. Do uczenia sieci jest wykorzystany algorytm wstecznej propagacji b³êdu.\n\n"
			+ "1. Wykorzystuj¹c kartê pierwsz¹ (\"Creating Examples\") mo¿na utworzyæ w³asne symbole oraz przyk³ady, które póŸniej mog¹ pos³u¿yæ do wyuczenia sieci ich rozpoznawania. Istnieje równie¿ mo¿liwoœæ usuwania istniej¹cych przyk³adów oraz symboli.\n\n"
			+ "2. Wykorzystuj¹c drug¹ kartê (\"Learning\") mo¿na stworzyæ nowe sieci neuronowe przypisuj¹c im konkretne kategorie symboli. Mo¿na równie¿ dodaæ do istniej¹cych sieci nowe symbole lub ponowiæ proces nauki wybranych.\n\n"
			+ "3. Wykorzystuj¹c trzeci¹ kartê (\"Testing\") mo¿na narysowaæ dowolny symbol i sprawdziæ jak jest rozpoznawany dla wybranej sieci. Istnieje równie¿ mo¿liowœæ dodania narysowanego symbolu do wybranej istniej¹cej kategorii.\n\n" ;
	
	public static void showInfo()
	{
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("About..");
		
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		
		Text text1 = new Text(title);
		text1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		text1.setWrappingWidth(400);
		text1.setTextAlignment(TextAlignment.CENTER);
		Text text2 = new Text(info);
		
		text2.setFont(new Font(16));
		text2.setWrappingWidth(600);
		text2.setTextAlignment(TextAlignment.LEFT);
		
		Text text3 = new Text("Autor: Ignacy Daszkiewicz");
		text3.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		text3.setWrappingWidth(600);
		text3.setTextAlignment(TextAlignment.RIGHT);
		
		Button close = new Button("OK");
		close.setOnAction(e->{stage.close();});
		
		root.getChildren().addAll(text1, text2, text3, close);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setWidth(650);
		stage.setHeight(530);
		stage.setResizable(false);
		stage.showAndWait();
	}
}
