package application;

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
	 * This method is responsible for the creation of the server instance.
	 *
	 * @param no parameters needed.
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

}
