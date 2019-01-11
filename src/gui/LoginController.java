package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import entities.DBMessage;
import entities.DBMessage.DBAction;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;

public class LoginController implements Initializable,IClientUI
{
	
	private Node thisNode;
	@FXML
	private JFXTextField userNameTextField;

	@FXML
	private JFXPasswordField passwordTextFIeld;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
	}

	@FXML
	void loginBtnClick(ActionEvent event)
	{ // press on login button
		try
		{
			thisNode = ((Node) event.getSource());
			GuiManager.client.CheckValidUser(new User(userNameTextField.getText(), passwordTextFIeld.getText()));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void openSearchScreen(MouseEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.searchBook);

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		try
		{
		if (msg.Action == DBAction.RETCheckUser)
		{
			if ((User) msg.Data != null)
			{
				// Avoid throwing IllegalStateException by running from a non-JavaFX thread.
				Platform.runLater(
				  () -> 
				  {
					thisNode.getScene().getWindow().hide();
					GuiManager.SwitchScene(SCREENS.librarian);
				  }
				);
				
			}
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
