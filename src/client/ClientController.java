// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import java.io.*;

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
		
	}


	public void handleMessageFromClientUI(DBMessage message)
	{
		try
		{
			sendToServer(message);
		} catch (IOException e)
		{
			/*
			 * clientUI.display ("Could not send message to server.  Terminating client.");
			 */
			quit();
		}
	}
	/*  				from prototype:
	 * 
	public void updateStudentListFromDB(ObservableList<Student> studentList)
	  {
		  String message = "SELECT * FROM Students"; 
		  //handleMessageFromClientUI(message);
	  }
	  public void updateStudentStatus(Student studentToBeUpdated, String newStatus) 
	  {
		  if(studentToBeUpdated == null || newStatus.isEmpty()) return;
		  String message = 
		  "UPDATE Students SET StatusMembership='"+newStatus+"' WHERE StudentID="+studentToBeUpdated.getStudentID();
		  
		  //handleMessageFromClientUI(message);
	  }
*/
	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		} 
		catch (IOException e)
		{
		}
		
		System.exit(0);
	}

}
//End of ClientController class
