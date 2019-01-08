package defaultPackage;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class mainClient extends Application
{

	static Stage LoginStage; // save login stage

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
			Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginScreen.fxml"));
			Scene Loginscene = new Scene(root);
			primaryStage.setScene(Loginscene);
			primaryStage.setTitle("Ort Braude Library");
			primaryStage.getIcons().add(new Image("resources/Braude.png"));
			primaryStage.show();
			LoginStage = primaryStage; // save login stage

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Stage getLoginStage()
	{
		return LoginStage;
	}

}
