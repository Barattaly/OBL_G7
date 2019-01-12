package gui;

import entities.DBMessage;
import entities.User;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public interface IClientUI
{
	User getUserLogedIn();
	void setUserLogedIn(User userLoged);
	void getMessageFromServer(DBMessage msg);
}
