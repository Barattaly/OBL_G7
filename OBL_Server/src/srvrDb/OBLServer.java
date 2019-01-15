package srvrDb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import entities.UsersQueries;
import entities.BorrowACopyOfBook;
import entities.Book;
import entities.BooksQueries;
import entities.DBMessage;
import entities.Subscriber;
import entities.SubscribersQueries;
import entities.DBMessage.DBAction;
import entities.User;
import javafx.scene.control.TextArea;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class OBLServer extends AbstractServer
{

	public TextArea logREF = null;
	private MySQLConnection oblDB;

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public OBLServer(int port, String dbName, String dbPassword, String userName)
	{
		super(port);
		try
		{
			oblDB = new MySQLConnection(dbName, dbPassword, userName);
		} catch (Exception e)
		{
		}
	}

	public OBLServer(int port, TextArea log)
	{
		super(port);
		logREF = log;
	}

	public void connectToDB(String dbName, String dbPassword, String userName) throws SQLException
	{
		oblDB = new MySQLConnection(dbName, dbPassword, userName);
	}
	/*
	 * if (dbMessage.toString().contains("SELECT *")) { try { ResultSet res =
	 * oblDB.SelectAllFrom((String) dbMessage); ResultSetMetaData rsmd =
	 * res.getMetaData(); int columnsNumber = rsmd.getColumnCount(); String
	 * messageToSend = "SELECTALL-"; while (res.next()) { for (int i = 1; i <=
	 * columnsNumber; i++) { String columnValue = res.getString(i); messageToSend =
	 * messageToSend + columnValue + ","; } messageToSend = messageToSend + "-"; }
	 * client.sendToClient(messageToSend); } catch (Exception e) {
	 * System.out.println(e.getMessage()); } } else if
	 * (dbMessage.toString().contains("UPDATE")) { try {
	 * oblDB.updateStudent(dbMessage.toString()); } catch (Exception e) {
	 * System.out.println(e.getMessage()); } }
	 */

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param dbMessage The message received from the client.
	 * @param client    The connection from which the message originated.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromClient(Object obj, ConnectionToClient client)
	{
		if (!(obj instanceof DBMessage))
			return;
		DBMessage dbMessage = (DBMessage) obj;
		if (dbMessage.Action == DBAction.isDBRuning)
		{
			try
			{
				client.sendToClient(new DBMessage(DBAction.isDBRuning, isDBRunning()));
				return;

			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		//update gui for getting message
		logREF.setText("Message received from client: " + client + System.lineSeparator() + logREF.getText());
		if (!isDBRunning())
			return;
		try
		{
			switch (dbMessage.Action)
			{

			case CheckUser:
			{
				checkIfUserExist((User) dbMessage.Data, client);
				break;
			}
			case UpdateUserLogout:
			{
				updateUserLogout((User) dbMessage.Data, client);

				break;
			}
			case CreateSubscriber:
			{
				createSubscriber((Subscriber) dbMessage.Data, client);
				break;
			}
			case GetAllBooksList:
			{
				getListOfAllBooks(client);
				break;
			}
			case CreateNewBorrow:
			{
				//CreateNewBorrow((BorrowACopyOfBook) dbMessage.Data, client)
				break;
			}
			default:
				break;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				client.sendToClient(null);
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void getListOfAllBooks(ConnectionToClient client) throws SQLException, IOException
	{
		String query = BooksQueries.SelectAllBooks();
		ResultSet rs = oblDB.executeQuery(query);
		int numOfROws = getRowCount(rs);
		rs.next();
		List<Book> booksList= BooksQueries.CreateBookListFromRS(rs);
		client.sendToClient(new DBMessage(DBAction.GetAllBooksList, booksList));
		
	}

	private void updateUserLogout(User userToUpdate, ConnectionToClient client)
	{
		if (userToUpdate == null)
			return;
		userToUpdate.setLoginStatus("off");
		String query = UsersQueries.updateUserloginStatus(userToUpdate);
		oblDB.executeUpdate(query);
	}

	private void checkIfUserExist(User userToCheck, ConnectionToClient client) throws SQLException, IOException
	{
		String query = UsersQueries.searchUserByUserNameAndPass(userToCheck);
		ResultSet rs = oblDB.executeQuery(query);
		int rowsNumber = getRowCount(rs);
		if (rowsNumber == 1)
		{
			rs.next();
			User user = UsersQueries.CreateUserFromRS(rs);
			DBMessage returnMsg;
			if (user.getLoginStatus().equals("on"))
			{
				user = new User(null, null);
				returnMsg = new DBMessage(DBAction.CheckUser, user);
			} else
			{
				user.setLoginStatus("on");
				query = UsersQueries.updateUserloginStatus(user);
				oblDB.executeUpdate(query);
				returnMsg = new DBMessage(DBAction.CheckUser, user);
			}
			client.sendToClient(returnMsg);
			return;
		} else
		{
			DBMessage returnMsg = new DBMessage(DBAction.CheckUser, null);
			client.sendToClient(returnMsg);
		}

	}

	private void createSubscriber(Subscriber subscriberToCreate, ConnectionToClient client) throws IOException
	{
		User userToCheck = (User) subscriberToCreate;
		if (isUserExist(userToCheck))
		{
			DBMessage returnMsg = new DBMessage(DBAction.CreateSubscriber, null);
			client.sendToClient(returnMsg);
			return;
		}
		userToCheck.setType("subscriber");
		String query = UsersQueries.createSubscriberUser(userToCheck);
		oblDB.executeUpdate(query);// add to Users table
		query = SubscribersQueries.createSubscriber(subscriberToCreate);
		oblDB.executeUpdate(query);// add to Subscribers table

		query = SubscribersQueries.searchSubscriberByID(subscriberToCreate);
		ResultSet rs = oblDB.executeQuery(query);
		subscriberToCreate = SubscribersQueries.CreateSubscriberFromRS(rs);
		subscriberToCreate.FillInformationFromUser(userToCheck);

		DBMessage returnMsg = new DBMessage(DBAction.CreateSubscriber, subscriberToCreate);
		client.sendToClient(returnMsg);
	}

	private boolean isUserExist(User userToCheck)
	{
		String query = UsersQueries.searchUserByUserName(userToCheck);// search by user name
		ResultSet rsUserName = oblDB.executeQuery(query);

		query = UsersQueries.searchUserByID(userToCheck);// search by user name
		ResultSet rsID = oblDB.executeQuery(query);

		int numberOfIDs = getRowCount(rsID);
		int numberOfUserNames = getRowCount(rsUserName);

		if (numberOfUserNames > 0 || numberOfIDs > 0) // means that the user already exist
		{
			return true; 
		}
		return false;
	}

	/*private void CreateNewBorrow(BorrowACopyOfBook borrowToAdd, ConnectionToClient client) throws IOException
	{
		if (!isBookExist(borrowToAdd))
		{
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, null);
			client.sendToClient(returnMsg);
			return;
		}
		
		String query = BooksQueries.addNewBorrow(borrowToAdd);
		oblDB.executeUpdate(query);// add a new borrow to Borrows table
		query = CopiesQueries.changeCopyStatus(borrowToAdd);
		oblDB.executeUpdate(query);// update the copy status
		
		query = SubscribersQueries.searchSubscriberByID(borrowToAdd);
		ResultSet rs = oblDB.executeQuery(query);
		subscriberToCreate = SubscribersQueries.CreateSubscriberFromRS(rs);
		subscriberToCreate.FillInformationFromUser(userToCheck);
		
		DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
		client.sendToClient(returnMsg);
	}
	
	private boolean isBookExist(BorrowACopyOfBook bookToCheck)
	{
		String query = BooksQueries.searchBookByCatalogNumber(bookToCheck);// search by user name
		ResultSet rsCatalogNumber = oblDB.executeQuery(query);

		int numberOfCatalogNumbers = getRowCount(rsCatalogNumber);
		if (numberOfCatalogNumbers > 0) // means that the book exist
		{
			return true;
		}
		return false;
	}*/

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println("Server has stopped listening for connections.");
	}

	public boolean isDBRunning()
	{
		if (oblDB == null)
			return false;
		return oblDB.IsConnectionSucceeded;
	}

	private int getRowCount(ResultSet resultSet)
	{
		if (resultSet == null)
		{
			return 0;
		}
		try
		{
			resultSet.last();
			return resultSet.getRow();
		} catch (SQLException exp)
		{
			exp.printStackTrace();
		} finally
		{
			try
			{
				resultSet.beforeFirst();
			} catch (SQLException exp)
			{
				exp.printStackTrace();
			}
		}
		return 0;
	}

}