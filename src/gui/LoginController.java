package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.GuiManager.SCREENS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

public class LoginController implements Initializable
{

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}

	@FXML
	void loginBtnDisplay(ActionEvent event) throws IOException
	{ // press on login button

		//LOGIC OF LOGIN!!!!!
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.librarian);
	}

	@FXML
	void openSearchScreen(MouseEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.SearchBook);
		
	}

}
