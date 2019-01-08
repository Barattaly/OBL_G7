package client;

import java.io.IOException;

import entities.Student;
import javafx.collections.ObservableList;

public class LibrarianModel 
{
	 /**
	   * The default port to connect on.
	   */
	  /*final public static int DEFAULT_PORT = 5555;
	    

	  private ClientController client;
	  private ObservableList<Student> librarianStudentListREF = null;

	  public LibrarianModel(String host, int port) 
	  {
	    try 
	    {
	      client= new ClientController(host, port, this);
	    } 
	    catch(IOException exception) 
	    {
	      //System.out.println("Error: Can't setup connection!"
	      //         + " Terminating client.");
	      //System.exit(1);
	    }
	  }
	  public void updateStudentListFromDB(ObservableList<Student> studentList)
	  {
		  String message = "SELECT * FROM Students"; 
		  client.handleMessageFromClientUI(message);
		  librarianStudentListREF = studentList;  
	  }
	  public void updateStudentStatus(Student studentToBeUpdated, String newStatus) 
	  {
		  if(studentToBeUpdated == null || newStatus.isEmpty()) return;
		  String message = 
		  "UPDATE Students SET StatusMembership='"+newStatus+"' WHERE StudentID="+studentToBeUpdated.getStudentID();
		  
		  client.handleMessageFromClientUI(message);
	  }
		@Override
		public void display(String msg) 
		{
			String dbMessage[] =  msg.split("-");			
			switch(dbMessage[0])//the action
			{
				case "SELECTALL":
				{
					try
					{
						for(int i =1;i<dbMessage.length;i++)
						{
							String row[] = dbMessage[i].split(",");
							Student student = new Student(row[1], Integer.parseInt(row[0]), row[2]);
							librarianStudentListREF.add(student);
						}
					}
					catch(Exception e) {}
				}
				case "UPDATE":
				{
				}
			}
		}*/
		
}