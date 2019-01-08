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
		try
		{
			clientController = new ClientController("localhost", ClientController.DEFAULT_PORT);

		} catch (Exception e)
		{
			GuiManager.ShowErrorPopup("Can't Connect Client!");
			clientController = null;
		}
		finally 
		{
			GuiManager.InitialPrimeryStage(SCREENS.login,primaryStage);
		}
	}

}
