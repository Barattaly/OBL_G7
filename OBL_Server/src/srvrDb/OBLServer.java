package srvrDb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import entities.UsersQueries;
import entities.BorrowACopyOfBook;
import entities.BorrowsQueries;
import entities.CopiesQueries;
import entities.CopyOfBook;
import entities.ActivityLog;
import entities.Book;
import entities.BookOrder;
import entities.BooksQueries;
import entities.DBMessage;
import entities.Employee;
import entities.EmployeeQueries;
import entities.OrdersQueries;
import entities.ReturnesQueries;
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
	ArrayList<ActivityLog> activityLogList;

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

	public void connectToDB(String dbName, String dbPassword, String userName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		oblDB = new MySQLConnection(dbName, dbPassword, userName);
	}

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
		// update gui for getting message
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
				searchSubscriberByID((String) dbMessage.Data, client);
				break;
			}
			case CreateNewBorrow:
			{
				createNewBorrow((BorrowACopyOfBook) dbMessage.Data, client);
				break;
			}
			case UpdateSubscriberCard:
			{
				updateSubscriberInformation((Subscriber) dbMessage.Data, client);
				break;
			}
			case ReturnBook:
			{
				returnBook((BorrowACopyOfBook) dbMessage.Data, client);
				break;
			}
			case GetEmployeeList:
			{
				getEmployeeList(client);
				break;
			}
			case CreateNewOrder:
			{
				createNewOrder((BookOrder) dbMessage.Data, client);
				break;
			}
			case GetCurrentBorrowsForSubID:
			{
				getCurrentBorrowForSubscriberID((String) dbMessage.Data, client);
				break;
			}
			case GetCurrentBorrows:
			{
				getCurrentBorrow(client);
				break;
			}
			case GetActivityLog:
			{
				getActivityLog((String)dbMessage.Data, client);
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
				e1.printStackTrace();
			}
		}
	}

	private void updateSubscriberInformation(Subscriber subscriberToUpdate, ConnectionToClient client)
			throws IOException
	{
		if (subscriberToUpdate == null)
		{
			return;

		} else
		{
			String query = UsersQueries.updateUserInformation(subscriberToUpdate);
			oblDB.executeUpdate(query);
			query = SubscribersQueries.updateSubscriberInformation(subscriberToUpdate);
			oblDB.executeUpdate(query);
		}
	}

	private void searchSubscriberByID(String subscriberID, ConnectionToClient client) throws IOException
	{
		String query = SubscribersQueries.getSubscriberFullInformationByID(subscriberID);
		ResultSet rs = oblDB.executeQuery(query); // here I found the subscriber by subscriber ID that enter in txt
													// filed
		int rowCount = getRowCount(rs);
		if (rowCount == 0) // check if that subscriber is not really exist
		{
			DBMessage returnMsg = new DBMessage(DBAction.ViewSubscriberCard, null);
			client.sendToClient(returnMsg);
			return;
		} else
		{ // return the subscriber we found to the client
			Subscriber subscriber = SubscribersQueries.createSubscriberFromFullInformationRS(rs);
			DBMessage returnMsg = new DBMessage(DBAction.ViewSubscriberCard, subscriber);
			client.sendToClient(returnMsg);
		}
	}

	private void getListOfAllBooks(ConnectionToClient client) throws SQLException, IOException
	{
		String query = BooksQueries.SelectAllBooksEachRowForNewAuthor();
		ResultSet rs = oblDB.executeQuery(query);
		Map<Integer, Book> booksList = BooksQueries.createBookListFromRS(rs);
		// now we need to get the categories,authors,orders,copies and more...
		for (int key : booksList.keySet())// for each book - find the categories + maxCopies + currentNumOfBorrows
		{
			query = BooksQueries.getCategoriesForBookId(booksList.get(key).getCatalogNumber());
			rs = oblDB.executeQuery(query);
			booksList.get(key).setCategories(new ArrayList<>());
			while (rs.next())
			{
				booksList.get(key).getCategories().add(rs.getString(1));
			}
			// update max copies:
			query = CopiesQueries.getBookMaxCopies(booksList.get(key));
			rs = oblDB.executeQuery(query);
			rs.next();
			booksList.get(key).setMaxCopies(rs.getInt(1));
			// ArrayBlockingQueue<BookOrder> orders = new
			// ArrayBlockingQueue<BookOrder>(rs.getInt(1));
			// update current number of borrows:
			query = BooksQueries.getCurrentNumOfBorrows(booksList.get(key));
			rs = oblDB.executeQuery(query);
			rs.next();
			booksList.get(key).setCurrentNumOfBorrows(rs.getInt(1));
			// update current number of orders:
			query = BooksQueries.getCurrentNumOfOrders(booksList.get(key));
			rs = oblDB.executeQuery(query);
			rs.next();
			booksList.get(key).setCurrentNumOfOrders(rs.getInt(1));
			// update copies list
			query = CopiesQueries.getBookCopiesDetails(booksList.get(key));
			ResultSet rsBookCopies = oblDB.executeQuery(query);
			try
			{
				ArrayList<CopyOfBook> copies = new ArrayList<>();
				while (rsBookCopies.next())
				{
					CopyOfBook copyOfBook = new CopyOfBook(rsBookCopies.getString(1), rsBookCopies.getString(2));
					copies.add(copyOfBook);
				}
				booksList.get(key).setCopies(copies);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			// update order list
			query = OrdersQueries.getBookCurrentOrders(booksList.get(key));
			ResultSet rsBookOrders = oblDB.executeQuery(query);
			try
			{
				ArrayList<BookOrder> arrayList = new ArrayList<BookOrder>();
				while (rsBookOrders.next())
				{
					BookOrder bookOrder = new BookOrder(rsBookOrders.getString(1), rsBookOrders.getString(2),
							rsBookOrders.getString(3), rsBookOrders.getString(4), rsBookOrders.getString(5),
							rsBookOrders.getString(6));
					try
					{
						// orders.add(bookOrder);
						arrayList.add(bookOrder);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				booksList.get(key).setOrders(arrayList);
			} catch (Exception e)
			{
				e.printStackTrace();
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
			if(isUserLocked(userToCheck, client)) return;
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
	/*
	 * The function check if the user is locked and if yes she send the message for the client
	 */
	private boolean isUserLocked(User userToCheck, ConnectionToClient client) throws SQLException, IOException
	{
		String query = SubscribersQueries.getSubscriberStatusByUserName(userToCheck.getUserName());
		ResultSet rs2 = oblDB.executeQuery(query);
		int rowsNumber = getRowCount(rs2);
		if (rowsNumber == 1)
		{
			rs2.next();
			DBMessage returnMsg2;
			if(rs2.getString(1).equals("locked"))
			{
				returnMsg2=new DBMessage(DBAction.CheckUser, "locked");
				client.sendToClient(returnMsg2);
				return true;
			}
		}
		return false;
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
		subscriberToCreate.setStatus("active");
		query = SubscribersQueries.createSubscriber(subscriberToCreate);
		oblDB.executeUpdate(query);// add to Subscribers table

		query = SubscribersQueries.searchSubscriberByID(subscriberToCreate);
		ResultSet rs = oblDB.executeQuery(query);
		subscriberToCreate = SubscribersQueries.createSubscriberFromRS(rs);
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
		Subscriber subscriber = new Subscriber(borrowToAdd.getSubscriberId());
		boolean isReturnDateValid = false;
		if (!isSubscriberExist(subscriber)) // check if the subscriber id is exist
		{
			borrowToAdd.setSubscriberId("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (!isSubscriberStatusActive(subscriber)) // check if the subscriber status is not active
															// (frozen/locked)
		{
			borrowToAdd.setSubscriberId("1");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (!isBookExist(book)) // check if the book catalog number doesn't exist
		{
			borrowToAdd.setBookCatalogNumber("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (isBookArchived(book)) // check if the book is archived
		{
			borrowToAdd.setBookCatalogNumber("-1");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (!ifAllBookCopiesAreUnavailable(book)) // check if all of the copies of the book are unavailable
		{
			borrowToAdd.setBookCatalogNumber("-2");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (!isCopyExist(book)) // check if the copy id doesn't exist
		{
			borrowToAdd.setCopyId("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (!isCopyIsAvailable(book)) // check if the copy status is unavailable
		{
			borrowToAdd.setCopyId("-1");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else
		{
			if (getBookClassification(book).equals("ordinary")) // check if the book classification is ordinary
			{
				if (LocalDate.parse(borrowToAdd.getExpectedReturnDate())
						.isAfter((LocalDate.parse(getCurrentDateAsString()).plusDays(13))))
				{
					borrowToAdd.setExpectedReturnDate("0");
					DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
					client.sendToClient(returnMsg);
					return;
				} else
					isReturnDateValid = true;

			} else if (getBookClassification(book).equals("wanted")) // check if the book classification is wanted
			{
				if (LocalDate.parse(borrowToAdd.getExpectedReturnDate())
						.isAfter((LocalDate.parse(getCurrentDateAsString()).plusDays(2))))
				{
					borrowToAdd.setExpectedReturnDate("1");
					DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
					client.sendToClient(returnMsg);
					return;
				} else
					isReturnDateValid = true;
			}
			if (isReturnDateValid)
			{
				updateDateFormat(borrowToAdd);
				String query = BorrowsQueries.addNewBorrow(borrowToAdd);
				oblDB.executeUpdate(query); // add a new borrow to Borrows table

				query = CopiesQueries.updateCopyStatusToUnavailable(book);
				oblDB.executeUpdate(query); // update copy status to unavailable

				DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
				client.sendToClient(returnMsg);
				return;
			}
		}
	}

	private void returnBook(BorrowACopyOfBook borrowToClose, ConnectionToClient client) throws IOException
	{
		Book book = new Book(borrowToClose.getBookCatalogNumber());
		CopyOfBook copyOfBook = new CopyOfBook(borrowToClose.getCopyId());
		ArrayList<CopyOfBook> copies = new ArrayList<>();
		copies.add(copyOfBook);
		book.setCopies(copies);

		if (!isBookExist(book)) // check if the book catalog number doesn't exist
		{
			borrowToClose.setBookCatalogNumber("0");
			DBMessage returnMsg = new DBMessage(DBAction.ReturnBook, borrowToClose);
			client.sendToClient(returnMsg);
			return;
		} else if (!ifAllBookCopiesAreAvailable(book)) // check if all of the copies of the book are available
		{
			borrowToClose.setBookCatalogNumber("-1");
			DBMessage returnMsg = new DBMessage(DBAction.ReturnBook, borrowToClose);
			client.sendToClient(returnMsg);
			return;
		} else if (!isCopyExist(book)) // check if the copy id doesn't exist
		{
			borrowToClose.setCopyId("0");
			DBMessage returnMsg = new DBMessage(DBAction.ReturnBook, borrowToClose);
			client.sendToClient(returnMsg);
			return;
		} else if (!isCopyIsUnavailable(book)) // check if the copy status is available
		{
			borrowToClose.setCopyId("-1");
			DBMessage returnMsg = new DBMessage(DBAction.ReturnBook, borrowToClose);
			client.sendToClient(returnMsg);
			return;
		} else
		{
			String query = BorrowsQueries.getCurrentBorrowsTable();
			ResultSet rsBorrowsTable = oblDB.executeQuery(query); // get borrows table

			// find the borrow we want to update actual return date from the borrows table.
			BorrowACopyOfBook borrowFromBorrowsTable = BorrowsQueries.searchSpecificBorrow(rsBorrowsTable,
					borrowToClose);

			borrowToClose.setId(borrowFromBorrowsTable.getId());
			borrowToClose.setSubscriberId(borrowFromBorrowsTable.getSubscriberId());
			borrowToClose.setBorrowDate(borrowFromBorrowsTable.getBorrowDate());
			borrowToClose.setExpectedReturnDate(borrowFromBorrowsTable.getExpectedReturnDate());
			/*
			 * if a subscriber is late at return, the server's daily check of late returns
			 * will change his borrow "isLateReturn" status to "yes"
			 */
			borrowToClose.setIsReturnedLate(borrowFromBorrowsTable.getIsReturnedLate());
			Subscriber subscriberToUpdate = new Subscriber(borrowToClose.getSubscriberId());

			query = SubscribersQueries.getSubscriberStatus(subscriberToUpdate);
			ResultSet rsSubscriberStatus = oblDB.executeQuery(query); // get borrows table
			try
			{
				rsSubscriberStatus.next();
				String subscriberStatus = rsSubscriberStatus.getString(1);
				if (subscriberStatus.equals("frozen"))
				{
					/*
					 * search in borrows table if the subscriber is late at return another book if
					 * exist: subscriber status doesn't change, stay frozen
					 */
					if (!ifSubscriberLateAnotherReturn(borrowToClose))
					{
						subscriberToUpdate.setStatus("active");
						query = SubscribersQueries.updateSubscriberStatusToActive(subscriberToUpdate);
						oblDB.executeUpdate(query); // update subscriber status to active
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			query = BorrowsQueries.updateReturnDate(borrowToClose);
			oblDB.executeUpdate(query); // update actual return date

			query = CopiesQueries.updateCopyStatusToAvailable(book);
			oblDB.executeUpdate(query); // update copy status to available

			DBMessage returnMsg = new DBMessage(DBAction.ReturnBook, borrowToClose);
			client.sendToClient(returnMsg);
			return;
		}
	}

	private void createNewOrder(BookOrder bookOrder, ConnectionToClient client) throws IOException
	{
		String query = OrdersQueries.addNewOrder(bookOrder);
		oblDB.executeUpdate(query); // add a new order to Orders table

		DBMessage returnMsg = new DBMessage(DBAction.CreateNewOrder, bookOrder);
		client.sendToClient(returnMsg);
		return;
	}

	private boolean isBookExist(Book bookToCheck)
	{
		String query = BooksQueries.searchBookByCatalogNumber(bookToCheck);// search by book catalog number
		ResultSet rsCatalogNumber = oblDB.executeQuery(query);

		int numberOfCatalogNumbers = getRowCount(rsCatalogNumber);
		if (numberOfCatalogNumbers > 0) // means that the book exist
		{
			return true;
		}
		return false;
	}

	private boolean isBookArchived(Book bookToCheck)
	{
		String query = BooksQueries.getArchiveStatus(bookToCheck);// search by book catalog number
		ResultSet rsArchiveStatus = oblDB.executeQuery(query);
		try
		{
			rsArchiveStatus.next();
			String archiveStatus = rsArchiveStatus.getString(1);
			if (archiveStatus.equals("yes"))
				return true;
			return false;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private boolean ifAllBookCopiesAreUnavailable(Book bookToCheck)
	{
		int maxCopies, numOfUnavailableCopies;
		String query = CopiesQueries.getBookMaxCopies(bookToCheck); // search by copy ID
		ResultSet rsBookMaxCopies = oblDB.executeQuery(query);
		try
		{
			rsBookMaxCopies.next();
			maxCopies = rsBookMaxCopies.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		query = CopiesQueries.getNumOfUnavailableCopies(bookToCheck);// search by copy ID
		ResultSet rsSumOfUnavailableCopies = oblDB.executeQuery(query);
		try
		{
			rsSumOfUnavailableCopies.next();
			numOfUnavailableCopies = rsSumOfUnavailableCopies.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		if (maxCopies == numOfUnavailableCopies)
		{
			return false;
		}
		return true;
	}

	private boolean ifAllBookCopiesAreAvailable(Book bookToCheck)
	{
		String query = BooksQueries.getCurrentNumOfBorrows(bookToCheck);// search by book catalog number
		ResultSet rsCurrentNumOfBorrows = oblDB.executeQuery(query);
		try
		{
			rsCurrentNumOfBorrows.next();
			int currentNumOfBorrows = rsCurrentNumOfBorrows.getInt(1);
			if (currentNumOfBorrows == 0)
				return false;
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private String getBookClassification(Book bookToCheck)
	{
		String query = BooksQueries.getClassificationOfBook(bookToCheck);// search by book catalog number
		ResultSet rsClassification = oblDB.executeQuery(query);
		try
		{
			rsClassification.next();
			return rsClassification.getString(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private boolean isCopyExist(Book bookToCheck)
	{
		String query = CopiesQueries.getCopyDetails(bookToCheck);// search by copy id
		ResultSet rsCopyId = oblDB.executeQuery(query);

		int rsNumberOfRows = getRowCount(rsCopyId);
		if (rsNumberOfRows > 0) // means that the copy of the book is exist
		{
			return true;
		}
		return false;
	}

	private boolean isCopyIsAvailable(Book bookToCheck)
	{
		String query = CopiesQueries.getCopyStatus(bookToCheck);// search by copy id
		ResultSet rsCopyStatus = oblDB.executeQuery(query);

		try
		{
			rsCopyStatus.next();
			String copyStatus = rsCopyStatus.getString(1);
			if (copyStatus.equals("unavailable"))
				return false;
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private boolean isCopyIsUnavailable(Book bookToCheck)
	{
		String query = CopiesQueries.getCopyStatus(bookToCheck);// search by copy id
		ResultSet rsCopyStatus = oblDB.executeQuery(query);

		try
		{
			rsCopyStatus.next();
			String copyStatus = rsCopyStatus.getString(1);
			if (copyStatus.equals("available"))
				return false;
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private boolean isSubscriberExist(Subscriber subscriberToCheck)
	{
		String query = SubscribersQueries.searchSubscriberByID(subscriberToCheck);// search by copy id
		ResultSet rsSubscriber = oblDB.executeQuery(query);

		int rsNumberOfRows = getRowCount(rsSubscriber);
		if (rsNumberOfRows > 0) // means that the subscriber is exist
		{
			return true;
		}
		return false;
	}

	private boolean isSubscriberStatusActive(Subscriber subscriberToCheck)
	{
		String query = SubscribersQueries.getSubscriberStatus(subscriberToCheck);// search by subscriber id
		ResultSet rsSubscriberStatus = oblDB.executeQuery(query);
		try
		{
			rsSubscriberStatus.next();
			String status = rsSubscriberStatus.getString(1);
			if (status.equals("active"))
			{
				return true;
			}
			return false;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
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

	private int getBookCurrentNumOfOrders(Book book)
	{
		String query = BooksQueries.getCurrentNumOfOrders(book);// search by book catalog number
		ResultSet rsBookCurrentNumOfOrders = oblDB.executeQuery(query);
		try
		{
			rsBookCurrentNumOfOrders.next();
			return rsBookCurrentNumOfOrders.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/*
	 * private int getSubcriberCurrentNumOfBorrows(Subscriber subscriberToUpdate) {
	 * String query =
	 * SubscribersQueries.getCurrentNumOfBorrows(subscriberToUpdate);// search by
	 * subscriber ID ResultSet rsCurrentNumOfBorrows = oblDB.executeQuery(query);
	 * int currentNumOfBorrows; try { rsCurrentNumOfBorrows.next();
	 * currentNumOfBorrows = rsCurrentNumOfBorrows.getInt(1); return
	 * currentNumOfBorrows; } catch (Exception e) { e.printStackTrace(); return -1;
	 * } }
	 */

	private int getSubscriberNumOfLateReturns(Subscriber subscriberToUpdate)
	{
		String query = SubscribersQueries.getNumOfLateReturns(subscriberToUpdate);// search by subscriber ID
		ResultSet rsCurrentNumOfLateReturns = oblDB.executeQuery(query);
		try
		{
			rsCurrentNumOfLateReturns.next();
			return rsCurrentNumOfLateReturns.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	private boolean ifSubscriberLateAnotherReturn(BorrowACopyOfBook borrowToClose)
	{
		Subscriber subscriberToUpdate = new Subscriber(borrowToClose.getSubscriberId());
		String query = SubscribersQueries.getSubscriberCurrentBorrowsTable(subscriberToUpdate);
		ResultSet rsSubscriberCurrentBorrowsTable = oblDB.executeQuery(query); // get subscriber's current borrows table
		try
		{
			while (rsSubscriberCurrentBorrowsTable.next())
			{
				// check all of the current borrows except the borrow we want to close
				if (!rsSubscriberCurrentBorrowsTable.getString(1).equals(borrowToClose.getId()))
				{
					/*
					 * if exist a borrow with "isReturnedLate" = "yes" below the borrow we want to
					 * close, than he is steel late at return => status will remain frozen
					 */
					if (rsSubscriberCurrentBorrowsTable.getString(6).equals("yes"))
					{
						return true;
					}
				}
			}
			return false;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * This method suppose to run automatically every 24 hours. If subscriber is
	 * late at return a copy of a book, his status will change to "frozen", and the
	 * borrow "isReturnedLate" flag will change to "yes".
	 */
	private void checkAndUpdateLateReturns()
	{
		String query = BorrowsQueries.getCurrentBorrowsTable();
		ResultSet rsCurrentBorrowsTable = oblDB.executeQuery(query); // get current borrows table
		BorrowACopyOfBook borrowFromBorrowsTable = new BorrowACopyOfBook();
		ArrayList<Subscriber> subscribersLateReturnsAtListThreeTimes = new ArrayList<Subscriber>();
		try
		{
			while (rsCurrentBorrowsTable.next())
			{
				borrowFromBorrowsTable.setId(rsCurrentBorrowsTable.getString(1));
				borrowFromBorrowsTable.setSubscriberId(rsCurrentBorrowsTable.getString(2));
				borrowFromBorrowsTable.setExpectedReturnDate(rsCurrentBorrowsTable.getString(4));

				String expectedBorrowDate = borrowFromBorrowsTable.getExpectedReturnDate();
				String returnDate = getCurrentDateAsString();

				Subscriber subscriberToUpdate = new Subscriber(borrowFromBorrowsTable.getSubscriberId());
				int lateReturnsCount = getSubscriberNumOfLateReturns(subscriberToUpdate);

				if (LocalDate.parse(returnDate).isAfter(LocalDate.parse(expectedBorrowDate))) // check if the subscriber
																								// is late at return
				{
					lateReturnsCount++;
					if (lateReturnsCount >= 3)
					{
						subscribersLateReturnsAtListThreeTimes.add(subscriberToUpdate);

					}
					query = BorrowsQueries.getIsReturnedLate(borrowFromBorrowsTable);
					ResultSet rsIsReturnedLate = oblDB.executeQuery(query); // get the value of "isReturnedLate" flag
					try
					{
						rsIsReturnedLate.next();
						if (rsIsReturnedLate.getString(1).equals("no"))
						{
							borrowFromBorrowsTable.setIsReturnedLate("yes");
							query = BorrowsQueries.updateIsReturnedLateToYes(borrowFromBorrowsTable);
							oblDB.executeUpdate(query); // update the flag at the borrow if the subscriber returned the
														// copy late
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					// Subscriber subscriberToUpdate = new
					// Subscriber(borrowFromBorrowsTable.getSubscriberId());
					subscriberToUpdate.setStatus("frozen");
					query = SubscribersQueries.getSubscriberStatus(subscriberToUpdate);
					ResultSet rsSubscriberStatus = oblDB.executeQuery(query); // update subscriber's status to frozen
					try
					{
						rsSubscriberStatus.next();
						if (rsSubscriberStatus.getString(1).equals("active"))
						{
							query = SubscribersQueries.updateSubscriberStatusToFrozen(subscriberToUpdate);
							oblDB.executeUpdate(query); // update subscriber's status to frozen
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		/*
		 * Send message to the library manager in order to approve deep freeze of the
		 * subscriber card the message will include the hashMap:
		 * subscribersLateReturnsAtListThreeTimes
		 */
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
		if (isDBRunning())
		{
			// if server was off -> all users are disconnected.
			oblDB.executeUpdate("UPDATE obl_db.users SET loginStatus = 'off'");

		}
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

	private void getEmployeeList(ConnectionToClient client) throws SQLException, IOException
	{
		String query = EmployeeQueries.SelectAllEmployees();
		ResultSet rs = oblDB.executeQuery(query);
		ArrayList<Employee> empList = EmployeeQueries.createEmpListFromRS(rs);

		client.sendToClient(new DBMessage(DBAction.GetEmployeeList, empList));
	}

	private void getCurrentBorrowForSubscriberID(String id, ConnectionToClient client) throws IOException
	{
		String query = BorrowsQueries.getCurrentBorrowsForSubscriberID(id);
		ResultSet rs = oblDB.executeQuery(query);
		ArrayList<BorrowACopyOfBook> borrowList = BorrowsQueries.createBorrowListFromRS(rs);

		client.sendToClient(new DBMessage(DBAction.GetCurrentBorrowsForSubID, borrowList));
	}

	private void getCurrentBorrow(ConnectionToClient client) throws IOException
	{
		String query = BorrowsQueries.getCurrentBorrows();
		ResultSet rs = oblDB.executeQuery(query);
		ArrayList<BorrowACopyOfBook> borrowList = BorrowsQueries.createBorrowListFromRS(rs);

		client.sendToClient(new DBMessage(DBAction.GetCurrentBorrows, borrowList));
	}
	private void getActivityLog(String subscriberID, ConnectionToClient client )throws IOException 
	{
		activityLogList = new ArrayList<ActivityLog>();
		ArrayList<ActivityLog> temp = getOrderActivityLog(subscriberID);
		if (temp != null)
			activityLogList.addAll(temp);
		temp = getReturnActivityLog(subscriberID);
		if (temp != null)
			activityLogList.addAll(temp);
		temp = getBorrowActivityLog(subscriberID);
		if (temp != null)
			activityLogList.addAll(temp);
		temp = getBorrowExtensionActivityLog(subscriberID);
		if (temp != null)
			activityLogList.addAll(temp);
		
		if (activityLogList == null)
		{
			 DBMessage returnMsg = new DBMessage(DBAction.GetActivityLog, null);
		      client.sendToClient(returnMsg);
		      return;
		}
		
	     DBMessage returnMsg = new DBMessage(DBAction.GetActivityLog, activityLogList);
	     client.sendToClient(returnMsg);
	}
	
	private ArrayList<ActivityLog> getBorrowExtensionActivityLog(String subscriberID)throws IOException 
	{
		
	    String query =BorrowsQueries.searchBorrowExtensionFromSubscriberID(subscriberID);
	    ResultSet rs = oblDB.executeQuery(query); 
	    int rowCount = getRowCount(rs);
	    if(rowCount == 0)
	    {
	      return null;
	    }
	    else
	    { 

	      ArrayList<ActivityLog> temp=BorrowsQueries.CreateBorrowExtensionListFromRS(rs);
	      return temp;
	    } 


	}
	
	private ArrayList<ActivityLog> getOrderActivityLog(String subscriberID)throws IOException 
	{
		
	    String query =OrdersQueries.searchOrdersFromSubscriberID(subscriberID);
	    ResultSet rs = oblDB.executeQuery(query); 
	    int rowCount = getRowCount(rs);
	    if(rowCount == 0)
	    {
	      return null;
	    }
	    else
	    { 

	      ArrayList<ActivityLog> temp=OrdersQueries.CreateOrdersListFromRS(rs);
	      return temp;
	    } 


	}

	private ArrayList<ActivityLog> getReturnActivityLog(String subscriberID)throws IOException 
	{
		
	    String query = ReturnesQueries.searchReturnFromSubscriberID(subscriberID);
	    ResultSet rs = oblDB.executeQuery(query); 
	    int rowCount = getRowCount(rs);
	    if(rowCount == 0)
	    {
	      return null;
	    }
	    else
	    { 
	      ArrayList<ActivityLog> temp=ReturnesQueries.CreateReturnListFromRS(rs);
	      return temp;
	    } 


	}
	
	private ArrayList<ActivityLog> getBorrowActivityLog(String subscriberID)throws IOException 
	{
		
	    String query = BorrowsQueries.searchBorrowFromSubscriberID(subscriberID);
	    ResultSet rs = oblDB.executeQuery(query); 
	    int rowCount = getRowCount(rs);
	    if(rowCount == 0)
	    {
	      return null;
	    }
	    else
	    { 

	      ArrayList<ActivityLog> temp=BorrowsQueries.CreateBorrowListFromRS(rs);
	      return temp;
	    } 


	}
	
}