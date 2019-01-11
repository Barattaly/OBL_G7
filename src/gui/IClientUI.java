package gui;

import entities.DBMessage;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public interface IClientUI
{
	void getMessageFromServer(DBMessage msg);
}
