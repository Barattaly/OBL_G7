package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import gui.GuiManager.SCREENS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.input.MouseEvent;

public class LoginController implements Initializable
{
    @FXML
    private JFXTextField userNameTexxtField;

    @FXML
    private JFXPasswordField passwordTextFIeld;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
			
	}

	@FXML
	void loginBtnClick(ActionEvent event)
	{ // press on login button
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window

		GuiManager.SwitchScene(SCREENS.librarianManager);

	}

	@FXML
	void openSearchScreen(MouseEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.searchBook);
		
	}

}
