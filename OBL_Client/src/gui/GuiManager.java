package gui;

import java.util.HashMap;
import java.util.Map;

import client.ClientController;
import gui.GuiManager.SCREENS;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GuiManager
{
	public static ClientController client;
	public static IClientUI CurrentGuiController;
	
	public static Map<String, SCREENS> userTypeFromString = new HashMap<String, SCREENS>()
	{
		{
			put("librarian",SCREENS.librarian);
			put("subscriber",SCREENS.subscriber);
			put("library manager",SCREENS.librarianManager);

		}
	};
	private static Map<SCREENS, String> availableFXML = new HashMap<SCREENS, String>()
	{
		{
			put(SCREENS.login, "/gui/LoginScreen.fxml");
			put(SCREENS.librarian,"/gui/LibrarianScreen.fxml");
			put(SCREENS.searchBook,"/gui/SearchBookScreen.fxml");
			put(SCREENS.bookInformation,"/gui/BookInformationScreen.fxml");
			put(SCREENS.subscriber,"/gui/SubscriberScreen.fxml");
			put(SCREENS.librarianManager,"/gui/LibrarianManagerScreen.fxml");
		}
	};

	public static void ShowErrorPopup(String msg)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Unexpected Error");
		alert.setHeaderText("");
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public static void SwitchScene(SCREENS fxmlPath)
	{
		try
		{
			if(fxmlPath == SCREENS.login)
				client.updateUserLogOut(CurrentGuiController.getUserLogedIn());
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(availableFXML.get(fxmlPath)));
			Parent root = loader.load();
			CurrentGuiController = loader.getController();
			Scene scene = new Scene(root);
			SeondStage.setTitle("Ort Braude Library");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.show();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void InitialPrimeryStage(SCREENS fxmlPath, Stage primaryStage)
	{
		
		try
		{
			client = new ClientController("localhost", ClientController.DEFAULT_PORT);

		} catch (Exception e)
		{
			ShowErrorPopup("Can't Connect Client!");
			client = null;
		} finally
		{
			try
			{
				FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(availableFXML.get(fxmlPath)));
				Parent root = loader.load();
				CurrentGuiController = loader.getController();
				Scene Scene = new Scene(root);
				primaryStage.setScene(Scene);
				primaryStage.setTitle("Ort Braude Server");
				primaryStage.getIcons().add(new Image("/resources/Braude.png"));
				primaryStage.show();
				
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static enum SCREENS
	{
		login,librarian,searchBook,bookInformation,subscriber,librarianManager;
	}

}
