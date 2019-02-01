package application;

import gui.GuiManager;
import gui.GuiManager.SCREENS;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainClient extends Application
{	
	public static void main(String[] args)
	{
		launch(args);
	}
	/**
	 * The start of the client side application is in the method of the GUIController - InitialPrimaryStage
	 */
	@Override
	public void start(Stage primaryStage)
	{
		GuiManager.InitialPrimeryStage(SCREENS.login,primaryStage);	
	}
}
