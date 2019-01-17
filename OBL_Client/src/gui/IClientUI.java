package gui;

import entities.DBMessage;
import entities.User;

public interface IClientUI
{

	User getUserLogedIn();
	void setUserLogedIn(User userLoged);
	void getMessageFromServer(DBMessage msg);
}
