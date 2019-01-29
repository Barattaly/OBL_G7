package defaultPackage;


import client.ClientController;
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

	@Override
	public void start(Stage primaryStage)
	{
		GuiManager.InitialPrimeryStage(SCREENS.login,primaryStage);	
	}
}
