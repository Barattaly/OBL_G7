package gui;

import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import entities.DBMessage;
import entities.DBMessage.DBAction;
import gui.GuiManager.SCREENS;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GuiManager
{
	public static ClientController client;
	public static IClientUI CurrentGuiController;
	public static boolean dbConnected = false;

	public static Map<String, SCREENS> userTypeFromString = new HashMap<String, SCREENS>()
	{
		{
			put("librarian", SCREENS.librarian);
			put("subscriber", SCREENS.subscriber);
			put("library manager", SCREENS.librarianManager);

		}
	};
	private static Map<SCREENS, String> availableFXML = new HashMap<SCREENS, String>()
	{
		{
			put(SCREENS.login, "/gui/LoginScreen.fxml");
			put(SCREENS.librarian, "/gui/LibrarianScreen.fxml");
			put(SCREENS.searchBook, "/gui/SearchBookScreen.fxml");
			put(SCREENS.bookInformation, "/gui/BookInformationScreen.fxml");
			put(SCREENS.subscriber, "/gui/SubscriberScreen.fxml");
			put(SCREENS.librarianManager, "/gui/LibrarianManagerScreen.fxml");
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
	public static void ShowMessagePopup(String msg)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("");
		alert.setContentText(msg);
		alert.showAndWait();
	}

	public static void SwitchScene(SCREENS fxmlPath)
	{
		try
		{
			if (fxmlPath == SCREENS.login)
				client.updateUserLogOut(CurrentGuiController.getUserLogedIn());
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(availableFXML.get(fxmlPath)));
			Parent root = loader.load();
			CurrentGuiController = loader.getController();
			Scene scene = new Scene(root);
			SeondStage.setTitle("Ort Braude Library");
			SeondStage.setOnCloseRequest(e -> shutDown());// make sure safe shutdown
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void InitialPrimeryStage(SCREENS fxmlPath, Stage primaryStage)
	{

		try
		{
			client = new ClientController("localhost", ClientController.DEFAULT_PORT);// get connection
			client.sendToServer(new DBMessage(DBAction.isDBRuning, null));// check DB
		} catch (Exception e)
		{
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
				primaryStage.setOnCloseRequest(e -> shutDown());// make sure safe shutdown
				primaryStage.setTitle("Ort Braude Library");
				primaryStage.getIcons().add(new Image("/resources/Braude.png"));
				primaryStage.show();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void shutDown()
	{
		if(client == null) return;
		try
		{
			client.updateUserLogOut(CurrentGuiController.getUserLogedIn());
			client.closeConnection();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static enum SCREENS
	{
		login, librarian, searchBook, bookInformation, subscriber, librarianManager;
	}

	public static void preventLettersType(JFXTextField textField) 
	{
		textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() 
		   {
			   @Override
			   public void handle(KeyEvent e) {
			    if (!"0123456789".contains(e.getCharacter())) {
				        e.consume();
				}
			   }
			});
	}
}
