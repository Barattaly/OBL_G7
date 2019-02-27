package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import client.IClientUI;
import entities.DBMessage;
import entities.Subscriber;
import entities.DBMessage.DBAction;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LoginController implements IClientUI, Initializable
{

	private Node thisNode;
	@FXML
	private JFXTextField userNameTextField;
	@FXML
	private Label warningLabel;
	@FXML
	private JFXPasswordField passwordTextFIeld;
	@FXML
	private JFXButton loginBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		if (GuiManager.client == null)
		{
			warningLabel.setText("Error connecting to server");
			loginBtn.setDisable(true);
			userNameTextField.setDisable(true);
			passwordTextFIeld.setDisable(true);
		} else if (!GuiManager.dbConnected)
		{
			warningLabel.setText("Error connecting to database\nPlease check server.");
			loginBtn.setDisable(true);
			userNameTextField.setDisable(true);
			passwordTextFIeld.setDisable(true);
		}
	}

	@FXML
	void loginBtnClick(ActionEvent event)
	{ // press on login button
		try
		{
			warningLabel.setText("");
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

		if (GuiManager.dbConnected)
		{
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			GuiManager.SwitchScene(SCREENS.searchBook);
			User guest = new User();
			guest.setType("guest");
			GuiManager.CurrentGuiController.setUserLogedIn(guest);

		}

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		try
		{
			if (msg.Action == DBAction.CheckUser)
			{
				if (msg.Data == null)
				{
					Platform.runLater(() -> {
						warningLabel.setText("Wrong user user name or password.");
					});
				} else if (msg.Data instanceof User)
				{
					if (((User) msg.Data).getUserName() == null)// if the user already connected
					{
						Platform.runLater(() -> {
							warningLabel.setText("User already connected");
						});

					} else
					{
						// Avoid throwing IllegalStateException by running from a non-JavaFX thread.
						Platform.runLater(() -> {
							thisNode.getScene().getWindow().hide();
							GuiManager.SwitchScene(GuiManager.userTypeFromString.get(((User) msg.Data).getType()));
							GuiManager.CurrentGuiController.setUserLogedIn(((User) msg.Data));
						});
					}

				} else if (msg.Data instanceof String)
				{
					if (((String) msg.Data).equals("locked")) // if the user lock
					{
						Platform.runLater(() -> {
							warningLabel.setText("User is locked");

						});
					}

				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserLogedIn()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
