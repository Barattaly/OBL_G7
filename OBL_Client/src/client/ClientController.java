// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;

import java.io.*;

import entities.DBMessage;
import entities.User;
import entities.DBMessage.DBAction;
import gui.GuiManager;

public class ClientController extends AbstractClient
{

	final public static int DEFAULT_PORT = 5555;

	public ClientController(String host, int port) throws IOException
	{
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	public void handleMessageFromServer(Object msg)
	{
		if (!(msg instanceof DBMessage))
			return;// check if the message is DBMessage and not something else
		DBMessage message = (DBMessage) msg;
		DBAction action = message.Action;
		switch (action)
		{
		case isDBRuning:
			GuiManager.dbConnected = (boolean) message.Data;
			break;
		case RETCheckUser:
			GuiManager.CurrentGuiController.getMessageFromServer(message);
			break;

		default:
			break;
		}
	}

	/*
	 * from prototype:
	 * 
	 * public void updateStudentListFromDB(ObservableList<Student> studentList) {
	 * String message = "SELECT * FROM Students";
	 * //handleMessageFromClientUI(message); } public void
	 * updateStudentStatus(Student studentToBeUpdated, String newStatus) {
	 * if(studentToBeUpdated == null || newStatus.isEmpty()) return; String message
	 * = "UPDATE Students SET StatusMembership='"+newStatus+"' WHERE StudentID="
	 * +studentToBeUpdated.getStudentID();
	 * 
	 * //handleMessageFromClientUI(message); }
	 */
	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		} catch (IOException e)
		{
		}

		System.exit(0);
	}

	public void CheckValidUser(User user)
	{
		DBMessage message = new DBMessage(DBAction.CheckUser, user);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void updateUserLogOut(User user)
	{
		DBMessage message = new DBMessage(DBAction.UpdateUserLogout, user);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
//End of ClientController class
