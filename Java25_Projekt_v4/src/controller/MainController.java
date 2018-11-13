package controller;


import java.io.IOException;

import application.Main;
import controller.tab.Tab1Controller;
import controller.tab.Tab2Controller;
import controller.tab.Tab4Controller;
import exapmlemanager.ExampleManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import model.NetworkManager;

public class MainController
{
	private final String exampleFolderPath = "examples";
	private ExampleManager exampleManager;
	private NetworkManager networkManager;
	private int selectedTab = 1;
	
	@FXML private Tab1Controller tab1Controller;
	@FXML private Tab2Controller tab2Controller;
	
	@FXML private Tab4Controller tab4Controller;
	
	@FXML private Tab t1, t2, t4;
	@FXML private MenuItem closeButton;
	
	

	public void initialize()
	{
		System.out.println("Inicjalizacja ");
		Main.setMainController(this);
		
		System.out.println("Tworzenie menagera przyk³adów");
		setExampleManager(new ExampleManager(exampleFolderPath));
		setNetworkManager(new NetworkManager(exampleManager, exampleFolderPath));
		
		tab1Controller.setMainController(this);
		tab2Controller.setMainController(this);
		tab4Controller.setMainController(this);
		tab1Controller.setExampleMenager(exampleManager);
		tab2Controller.setExampleManager(exampleManager);
		tab2Controller.setNetworkManager(networkManager);
		tab4Controller.setNetworkManager(networkManager);
		tab2Controller.initializeComponentsWithManagers();
		tab4Controller.initializeManagerComponents();
		
		t1.setOnSelectionChanged(e->{setSelectedtab(1);});
		t2.setOnSelectionChanged(e->{setSelectedtab(2);});
		t4.setOnSelectionChanged(e->{setSelectedtab(4);});
		closeButton.setOnAction(e->closeApplication());
		

		System.out.println("Zakoñczona");
	}
	
	@FXML public void save()
	{
		try {
			System.out.println("Zapisywanie zmian");
			tab2Controller.getNetworkManager().getLearnMenager().setStopAll(true);
			tab1Controller.getExampleManager().serializeIndex();
			tab2Controller.getNetworkManager().serializeNetworks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML public void showInfo()
	{
		InfoAbout.showInfo();
	}
	public void test()
	{
		System.out.println("Test");
	}
	
	public void closeApplication()
	{
		System.out.println("Zamykanie programu");
		try {
			tab2Controller.getNetworkManager().getLearnMenager().setStopAll(true);
			tab1Controller.getExampleManager().serializeIndex();
			tab2Controller.getNetworkManager().serializeNetworks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.exit();
	}
	
	public int getSelectedTab() {
		return selectedTab;
	}
	public void setSelectedtab(int nrOfTab) {
		System.out.println("Selected tab: "+nrOfTab);
		this.selectedTab = nrOfTab;
	}
	public ExampleManager getExampleManager() {
		return exampleManager;
	}
	public void setExampleManager(ExampleManager exampleMenager) {
		this.exampleManager = exampleMenager;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	public Tab1Controller getTab1Controller() {
		return tab1Controller;
	}
	public void setTab1Controller(Tab1Controller tab1Controller) {
		this.tab1Controller = tab1Controller;
	}
	public Tab2Controller getTab2Controller() {
		return tab2Controller;
	}
	public void setTab2Controller(Tab2Controller tab2Controller) {
		this.tab2Controller = tab2Controller;
	}
	public Tab4Controller getTab4Controller() {
		return tab4Controller;
	}
	public void setTab4Controller(Tab4Controller tab4Controller) {
		this.tab4Controller = tab4Controller;
	}
	
}
