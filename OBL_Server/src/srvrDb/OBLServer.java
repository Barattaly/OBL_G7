package srvrDb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Copies;

import entities.UsersQueries;
import entities.BorrowACopyOfBook;
import entities.BorrowsQueries;
import entities.CopiesQueries;
import entities.CopyOfBook;
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
			case ViewSubscriberCard:
			{
				searchSubscriberByID((String)dbMessage.Data, client);
				break;
			}
			case CreateNewBorrow:
			{
				createNewBorrow((BorrowACopyOfBook) dbMessage.Data, client);
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
	private void searchSubscriberByID(String subscriberID, ConnectionToClient client) throws IOException 
	  {
	    String query = SubscribersQueries.getSubscriberFullInformationByID(subscriberID);
	    ResultSet rs = oblDB.executeQuery(query); //here I found the subscriber by subscriber ID that enter in txt filed
	    int rowCount = getRowCount(rs);
	    if(rowCount == 0) //check if that subscriber is not really exist
	    {
	      DBMessage returnMsg = new DBMessage(DBAction.ViewSubscriberCard, null);
	      client.sendToClient(returnMsg);
	      return;
	    }
	    else
	    { //return the subscriber we found to the client
	      Subscriber subscriber=SubscribersQueries.CreateSubscriberFromFullInformationRS(rs);
	      DBMessage returnMsg = new DBMessage(DBAction.ViewSubscriberCard, subscriber);
	      client.sendToClient(returnMsg);
	    } 
	  }

	private void getListOfAllBooks(ConnectionToClient client) throws SQLException, IOException
	{
		String query = BooksQueries.SelectAllBooksEachRowForNewAuthor();
		ResultSet rs = oblDB.executeQuery(query);
		Map<Integer,Book>  booksList = BooksQueries.CreateBookListFromRS(rs);
		//now we need to get the authors:
		for(int key:booksList.keySet())
		{
			query = BooksQueries.getCatagoriesForBookId(booksList.get(key).getCatalogNumber());
			rs = oblDB.executeQuery(query);
			booksList.get(key).setCategories(new ArrayList<>());
			while(rs.next())
			{
				booksList.get(key).getCategories().add(rs.getString(1));
			}
		}
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

	private void createNewBorrow(BorrowACopyOfBook borrowToAdd, ConnectionToClient client) throws IOException
	{
		Book book = new Book(borrowToAdd.getBookCatalogNumber());
		CopyOfBook copyOfBook = new CopyOfBook(borrowToAdd.getCopyId());
		ArrayList<CopyOfBook> copies = new ArrayList<>();
		copies.add(copyOfBook);
		book.setCopies(copies);
		boolean isReturnDateValid = false;
		if (!isBookExist(borrowToAdd))
		{
			borrowToAdd.setBookCatalogNumber("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		}
		
		else if (!isCopyExist(book))
		{
			borrowToAdd.setCopyId("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		}
		
		else if (!isSubscriberExist(borrowToAdd))
		{
			borrowToAdd.setSubscriberId("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		}
		else 
		{
			if(getBookClassification(borrowToAdd).equals("ordinary"))
			{
				if (LocalDate.parse(borrowToAdd.getExpectedReturnDate())
					.isAfter((LocalDate.parse(getCurrentDateAsString()).plusDays(13)))) 
				{
					borrowToAdd.setExpectedReturnDate("0");
					DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
					client.sendToClient(returnMsg);
					return;
				}
				else
					isReturnDateValid = true;
				
			}
			else if (getBookClassification(borrowToAdd).equals("wanted")) 
			{
				if (LocalDate.parse(borrowToAdd.getExpectedReturnDate())
						.isAfter((LocalDate.parse(getCurrentDateAsString()).plusDays(2))))
				{
					borrowToAdd.setExpectedReturnDate("1");
					DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
					client.sendToClient(returnMsg);
					return;
				}
				else
					isReturnDateValid = true;
			}
			if (isReturnDateValid) 
			{
				updateDateFormat(borrowToAdd);
				String query = BorrowsQueries.addNewBorrow(borrowToAdd);
				oblDB.executeUpdate(query);// add a new borrow to Borrows table

				
				query = CopiesQueries.changeCopyStatus(book);
				oblDB.executeUpdate(query);// update copy status to unavailable
				 

				Book bookToUpdate = new Book(borrowToAdd.getBookCatalogNumber());
				int bookCurrentNumOfBorrows = getBookCurrentNumOfBorrows(bookToUpdate);
				bookToUpdate.setCurrentNumOfBorrows(bookCurrentNumOfBorrows + 1);

				query = BooksQueries.updateCurrentNumOfBorrows(bookToUpdate);
				oblDB.executeUpdate(query);// update current number of borrows of the borrowed book
			
				Subscriber subscriberToUpdate = new Subscriber(borrowToAdd.getSubscriberId());
				int subscriberCurrentNumOfBorrows = getSubcriberCurrentNumOfBorrows(subscriberToUpdate);
				subscriberToUpdate.setCurrentNumOfBorrows(subscriberCurrentNumOfBorrows + 1);

				query = SubscribersQueries.updateCurrentNumOfBorrows(subscriberToUpdate);
				oblDB.executeUpdate(query);// update current number of borrows of the subscriber
				
				DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
				client.sendToClient(returnMsg);
				return;
			}
		}
	}
	
	private boolean isBookExist(BorrowACopyOfBook borrowToAdd)
	{
		String query = BorrowsQueries.searchBookByCatalogNumber(borrowToAdd);// search by book catalog number
		ResultSet rsCatalogNumber = oblDB.executeQuery(query);

		int numberOfCatalogNumbers = getRowCount(rsCatalogNumber);
		if (numberOfCatalogNumbers > 0) // means that the book exist
		{
			return true;
		}
		return false;
	}
	
	private String getBookClassification(BorrowACopyOfBook borrowToAdd)
	{
		String query = BooksQueries.getClassificationOfBook(borrowToAdd);// search by book catalog number
		ResultSet rsClassification = oblDB.executeQuery(query);
		try 
		{
			rsClassification.next();
			return rsClassification.getString(1);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	private boolean isCopyExist(Book bookToCheck)
	{
		String query = CopiesQueries.searchBookCopyId(bookToCheck);// search by copy id
		ResultSet rsCatalogNumber = oblDB.executeQuery(query);

		int numberOfCatalogNumbers = getRowCount(rsCatalogNumber);
		if (numberOfCatalogNumbers > 0) // means that the copy of the book is exist
		{
			return true;
		}
		return false;
	}
	
	private boolean isSubscriberExist(BorrowACopyOfBook bookToCheck)
	{
		String query = SubscribersQueries.searchSubscriberByID(bookToCheck);// search by copy id
		ResultSet rsCatalogNumber = oblDB.executeQuery(query);

		int numberOfCatalogNumbers = getRowCount(rsCatalogNumber);
		if (numberOfCatalogNumbers > 0) // means that the copy of the book is exist
		{
			return true;
		}
		return false;
	}
	
	private void updateDateFormat(BorrowACopyOfBook borrowToAdd) 
	{
		String year = borrowToAdd.getExpectedReturnDate();
		year = year.substring(0, 4);
		String monthDay = borrowToAdd.getExpectedReturnDate();
		monthDay = monthDay.substring(4, 10);
		int year1 = Integer.parseInt(year);
		borrowToAdd.setExpectedReturnDate("" + year1 + monthDay);
	}
	
	private int getBookCurrentNumOfBorrows(Book bookToUpdate)
	{
		String query = BooksQueries.getCurrentNumOfBorrows(bookToUpdate);// search by book catalog number
		ResultSet rsClassification = oblDB.executeQuery(query);
		int currentNumOfBorrows;
		try 
		{
			rsClassification.next();
			currentNumOfBorrows = Integer.parseInt(rsClassification.getString(1));
			return currentNumOfBorrows;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	private int getSubcriberCurrentNumOfBorrows(Subscriber subscriberToUpdate)
	{
		String query = SubscribersQueries.getCurrentNumOfBorrows(subscriberToUpdate);// search by subscriber ID
		ResultSet rsClassification = oblDB.executeQuery(query);
		int currentNumOfBorrows;
		try 
		{
			rsClassification.next();
			currentNumOfBorrows = Integer.parseInt(rsClassification.getString(1));
			return currentNumOfBorrows;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return -1;
		}
	}
	
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
	
	public static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}
}