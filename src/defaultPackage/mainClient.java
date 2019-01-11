package defaultPackage;


import client.ClientController;
import gui.GuiManager;
import gui.GuiManager.SCREENS;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainClient extends Application
{

	static ClientController clientController;
	
	public static void main(String[] args)
	{
		
		launch(args);
	}

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0] The host to connect to (the ip of the server computer).
	 */
	@Override
	public void start(Stage primaryStage)
	{
		
		GuiManager.InitialPrimeryStage(SCREENS.login,primaryStage);
		
	}

}
