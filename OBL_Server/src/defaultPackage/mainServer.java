package defaultPackage;

import gui.ServerScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class mainServer extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader =  new FXMLLoader(getClass().getResource("/gui/ServerScreen.fxml"));
			Parent root = loader.load();
			ServerScreenController guiController = loader.getController();
			Scene serverScene = new Scene(root);
			primaryStage.setScene(serverScene);
			primaryStage.setOnCloseRequest(e -> guiController.shutDown());//make sure safe shutdown
			primaryStage.setTitle("Ort Braude Server");
			primaryStage.getIcons().add(new Image("/resources/Server-icon.png"));

			primaryStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *        is entered.
	 */
	public static void main(String[] args)
	{
		
		launch(args);
		/*String dbName = null;
		String dbPassword = null;
	    Scanner scanner = new Scanner(System.in);

		try
		{
			System.out.println("Please Enter the DB Schema Name:");
			dbName =  scanner.next();
			System.out.println("Please Enter the Password of the connection:");
			dbPassword = scanner.next();
		}
		catch(Exception ex)
		{
			System.out.println("Error reading data!\nDefault values: obl_schema, Group7");
			dbName = "obl_schema";
			dbPassword = "Group7";
		}
    
		OBLServer server = new OBLServer(DEFAULT_PORT,dbName,dbPassword);

		try 
		{
			server.listen(); // Start listening for connections
			System.out.println("Enter 'exit' to destroy server");

			while(true)
			{
				if(scanner.next().equals("exit")) 
					{
						server.close();
						System.exit(0);
					}
			}
				

		} 
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
			System.out.println("ERROR - Could not listen for clients!");
		}*/
	}

}
