package srvrDb;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import entities.UsersQueries;
import entities.BorrowACopyOfBook;
import entities.BorrowExtension;
import entities.BorrowExtensionQueries;
import entities.BorrowsQueries;
import entities.CopiesQueries;
import entities.CopyOfBook;
import entities.ActivityLog;
import entities.Book;
import entities.BookOrder;
import entities.BookOrder.orderQueueCheckOptions;
import entities.BooksQueries;
import entities.DBMessage;
import entities.Employee;
import entities.EmployeeQueries;
import entities.OblMessagesQueries;
import entities.OblMessage;
import entities.OrdersQueries;
import entities.Report_Activity;
import entities.Report_BorrowDurationInfo;
import entities.Report_LateReturns;
import entities.ReportsQueries;
import entities.ReturnesQueries;
import entities.SendEmail;
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
	private final String pathToSavePDF = ".\\src\\resources\\tablesOfContent\\";

	/**
	 * Constructs an instance of the OBL server.
	 *
	 * @param port The port number to connect on.
	 */
	public OBLServer(int port, String dbName, String dbPassword, String userName)
	{
		super(port);
		try
		{
			oblDB = new MySQLConnection(dbName, dbPassword, userName);
			if (isDBRunning())
			{
				// if server was off -> all users are disconnected.
				oblDB.executeUpdate("UPDATE obl_db.users SET loginStatus = 'off'");

			}

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
		if (isDBRunning())
		{
			// if server was off -> all users are disconnected.
			oblDB.executeUpdate("UPDATE obl_db.users SET loginStatus = 'off'");

		}
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
		{
			if (obj instanceof String)
			{
				String msg = (String) obj;
				if (!msg.isEmpty())
				{
					if (msg.substring(0, 11).equals("graduation:"))
					{
						try
						{
							String studentID = msg.substring(11);
							graduateStudent(studentID);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			return;
		}
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
				sendListOfAllBooks(client);
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
				getActivityLog((String) dbMessage.Data, client);
				break;
			}
			case Reports_getAvarageBorrows:
			{
				reports_getAvarageBorrows(client);
				break;
			}
			case MoveBookToArchive:
			{
				moveBookToArchive((String) dbMessage.Data, client);
				break;
			}
			case ViewTableOfContent:
			{
				sendPDFtoClient((Book) dbMessage.Data, client);
				break;
			}
			case Reports_Activity:
			{
				reports_createAcitivityReport(client);
				break;
			}
			case Reports_getList:
			{
				reports_getList(client);
				break;
			}
			case Reports_Add:
			{
				reports_AddNewToList((Report_Activity) dbMessage.Data, client);
				break;
			}
			case Reports_LateReturns:
			{
				reports_LateReturns(client);
				break;
			}
			case AddBook:
			{
				addNewBook((Book) dbMessage.Data, client);
				break;
			}
			case EditBookDetails:
			{
				changeBookDetails((Book) dbMessage.Data, client);
				break;
			}
			case BorrowExtension:
			{
				createBorrowExtension((BorrowExtension) dbMessage.Data, client);
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
			if (subscriberToUpdate.getStatus() == null)
			{
				subscriberToUpdate.setStatus("active");
			}
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

	private Map<Integer, Book> createAlistOfAllBooks() throws SQLException
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
				ArrayBlockingQueue<BookOrder> orders = new ArrayBlockingQueue<BookOrder>(
						booksList.get(key).getMaxCopies());
				while (rsBookOrders.next())
				{
					BookOrder bookOrder = new BookOrder(rsBookOrders.getString(1), rsBookOrders.getString(2),
							rsBookOrders.getString(3), rsBookOrders.getString(4), rsBookOrders.getString(5),
							rsBookOrders.getString(6));
					try
					{
						orders.add(bookOrder);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				booksList.get(key).setOrders(orders);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			// update borrows list
			query = BorrowsQueries.getBookCurrentBorrows(booksList.get(key));
			ResultSet rsBookBorrows = oblDB.executeQuery(query);
			try
			{
				ArrayList<BorrowACopyOfBook> borrows = new ArrayList<BorrowACopyOfBook>();
				while (rsBookBorrows.next())
				{
					BorrowACopyOfBook borrow = new BorrowACopyOfBook(rsBookBorrows.getString(1),
							rsBookBorrows.getString(2), rsBookBorrows.getString(3), rsBookBorrows.getString(4),
							rsBookBorrows.getString(5), rsBookBorrows.getString(6), rsBookBorrows.getString(7));
					try
					{
						borrows.add(borrow);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				booksList.get(key).setBorrows(borrows);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return booksList;
	}

	private void sendListOfAllBooks(ConnectionToClient client) throws SQLException, IOException
	{
		Map<Integer, Book> booksList = createAlistOfAllBooks();
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
			if (isUserLocked(userToCheck, client))
				return;
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
	 * The function check if the user is locked and if yes she send the message for
	 * the client
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
			if (rs2.getString(1).equals("locked"))
			{
				returnMsg2 = new DBMessage(DBAction.CheckUser, "locked");
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
		BookOrder bookOrder;
		boolean isReturnDateValid = false;
		if (!isSubscriberExist(subscriber)) // check if the subscriber id is exist
		{
			borrowToAdd.setSubscriberId("0");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		}
		/*
		 * check if there is order for this book. if exist, only the first in the order
		 * queue can borrow this book
		 */
		if (orderQueueCheckForNewBorrow(book, subscriber) == orderQueueCheckOptions.SubscriberIsNotFirstInOrdersQueue)
		// the subscriber is not the one who ordered the book
		{
			borrowToAdd.setSubscriberId("1");
			DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
			client.sendToClient(returnMsg);
			return;
		} else if (orderQueueCheckForNewBorrow(book,
				subscriber) == orderQueueCheckOptions.SubscriberIsFirstInOrdersQueue
				|| orderQueueCheckForNewBorrow(book, subscriber) == orderQueueCheckOptions.OrdersQueueIsEmpty)
		// the subscriber is the subscriber who ordered the book, or the orders queue is
		// empty
		{
			if (!isSubscriberStatusActive(subscriber))
			// check if the subscriber status is not active (frozen/deep freeze/locked)
			{
				borrowToAdd.setSubscriberId("2");
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
			} else if (isAllBookCopiesAreUnavailable(book)) // check if all of the copies of the book are
															// unavailable
			{
				borrowToAdd.setBookCatalogNumber("-2");
				DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
				client.sendToClient(returnMsg);
				return;
			} else if (isAlreadyBorrowedCopyOfTheBook(borrowToAdd)) // check if the subscriber already borrowing a
																	// copy of this book
			{
				borrowToAdd.setBookCatalogNumber("-3");
				DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
				client.sendToClient(returnMsg);
				return;
			} else if (!isCopyExist(book)) // check if the copy id doesn't exist
			{
				borrowToAdd.setCopyId("0");
				DBMessage returnMsg = new DBMessage(DBAction.CreateNewBorrow, borrowToAdd);
				client.sendToClient(returnMsg);
				return;
			} else if (isCopyIsUnavailable(book)) // check if the copy status is unavailable
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

				} else if (getBookClassification(book).equals("wanted")) // check if the book classification is
																			// wanted
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

					if (orderQueueCheckForNewBorrow(book,
							subscriber) == orderQueueCheckOptions.SubscriberIsFirstInOrdersQueue)
					{
						query = OrdersQueries.getBookCurrentOrders(book); // search by book catalog number
						ResultSet rsBookCurrentOrders = oblDB.executeQuery(query);
						try
						{
							rsBookCurrentOrders.next();
							bookOrder = new BookOrder(rsBookCurrentOrders.getString(2));
							bookOrder.setId(rsBookCurrentOrders.getString(1));
							bookOrder.setStatus("closed");
							query = OrdersQueries.updateOrderStatus(bookOrder);
							oblDB.executeUpdate(query); // update order status to closed
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
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
		} else if (isAllBookCopiesAreAvailable(book)) // check if all of the copies of the book are available
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
		} else if (isCopyIsAvailable(book)) // check if the copy status is available
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
			/* if a subscriber is late at return, the server's daily check of late returns
			 * will change his borrow "isLateReturn" status to "yes" */
			borrowToClose.setIsReturnedLate(borrowFromBorrowsTable.getIsReturnedLate());
			Subscriber subscriberToUpdate = new Subscriber(borrowToClose.getSubscriberId());
			
			query = SubscribersQueries.getSubscriberStatus(subscriberToUpdate);
			ResultSet rsSubscriberIsGraduated = oblDB.executeQuery(query); // get subscriber isGraduated value
			
			query = SubscribersQueries.getSubscriberStatus(subscriberToUpdate);
			ResultSet rsSubscriberStatus = oblDB.executeQuery(query); // get subscriber status
			try
			{
				rsSubscriberStatus.next();
				String subscriberStatus = rsSubscriberStatus.getString(1);
				if (subscriberStatus.equals("frozen"))
				{
					
					//graduation
					
					/* search in borrows table if the subscriber is late at return another book.
					 * if exist: subscriber status doesn't change, stay frozen */
					if (!isSubscriberLateAnotherReturn(borrowToClose))
					{
						subscriberToUpdate.setStatus("active");
						query = SubscribersQueries.updateSubscriberStatus(subscriberToUpdate);
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

			/*
			 * search in orders table if there is an order for this book, if exist, set the
			 * return date as book arrive date of this order, and send an email to the
			 * subscriber who ordered this book.
			 */
			int count = 0;
			query = OrdersQueries.getBookCurrentOrders(book);
			ResultSet rsBookCurrentOrders = oblDB.executeQuery(query); // get subscriber status
			try
			{
				BookOrder orderToUpdate = new BookOrder(book.getCatalogNumber(), true);
				while (rsBookCurrentOrders.next() && count == 0) // update only the first order's book arrive date
				{
					String bookArriveDate = rsBookCurrentOrders.getString(5);
					if (bookArriveDate == null)
					{
						orderToUpdate.setId(rsBookCurrentOrders.getString(1));
						orderToUpdate.setSubscriberId(rsBookCurrentOrders.getString(2));
						orderToUpdate.setBookArriveDate(borrowToClose.getActualReturnDate());
						query = OrdersQueries.updateBookArriveDate(orderToUpdate);
						oblDB.executeUpdate(query); // update book arrive date
						count++;
						Subscriber subscriberToInform = new Subscriber(orderToUpdate.getSubscriberId());

						String fullName = null;
						query = SubscribersQueries.getSubscriberFullInformationByID(subscriberToInform.getId());
						ResultSet rsSubscriberDetails = oblDB.executeQuery(query); // get subscriber details
						try
						{
							rsSubscriberDetails.next();
							fullName = rsSubscriberDetails.getString(4).substring(0, 1).toUpperCase()
									+ rsSubscriberDetails.getString(4).substring(1) + " "
									+ rsSubscriberDetails.getString(5).substring(0, 1).toUpperCase()
									+ rsSubscriberDetails.getString(5).substring(1);
							subscriberToInform.setPhoneNumber(rsSubscriberDetails.getString(6));
							subscriberToInform.setEmail(rsSubscriberDetails.getString(7));
						} catch (Exception e)
						{
							e.printStackTrace();
						}

						query = BooksQueries.getBookName(book);
						ResultSet rsBookName = oblDB.executeQuery(query); // get book name
						String bookName = "";
						try
						{
							rsBookName.next();
							bookName = rsBookName.getString(1);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						String emailSubject = "The book you ordered has arrived to the library";
						String emailMessage = "Dear " + fullName + ",\n" + "The book: \"" + bookName
								+ "\" that you have ordered has arrived to the library.\n"
								+ "You have two days to borrow this book, otherwise, your order will be canceled.";

						/* send an email to the subscriber */
						SendEmail email = new SendEmail();
						email.sendEmail(subscriberToInform.getEmail(), emailSubject, emailMessage);
					}
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}

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

	private void createBorrowExtension(BorrowExtension borrowToExtend, ConnectionToClient client) throws IOException
	{
		Book bookToCheck = null;
		int daysUntilExpectedReturnDate = 0; // check if this is the right number to initialize
		boolean isReturnDateValid = false;
		String query = BorrowsQueries.getBorrowDetails(borrowToExtend.getBorrow().getId());
		ResultSet rsBorrowDetails = oblDB.executeQuery(query);
		Subscriber subscriber = null;
		try // get the details of the wanted borrow to extend
		{
			rsBorrowDetails.next();
			borrowToExtend.getBorrow().setSubscriberId(rsBorrowDetails.getString(1));
			borrowToExtend.getBorrow().setBorrowDate(rsBorrowDetails.getString(2));
			borrowToExtend.getBorrow().setExpectedReturnDate(rsBorrowDetails.getString(3));
			borrowToExtend.getBorrow().setBookCatalogNumber(rsBorrowDetails.getString(5));
			borrowToExtend.getBorrow().setCopyId(rsBorrowDetails.getString(6));
			borrowToExtend.getBorrow().setActualReturnDate("0"); // avoid null value
			bookToCheck = new Book(borrowToExtend.getBorrow().getBookCatalogNumber());
			// check if orders queue exist for the borrowed book, if exist than extend the
			// borrow is unavailable
			if (orderQueueCheckForNewBorrowExtesion(bookToCheck) == orderQueueCheckOptions.OrdersQueueIsNotEmpty)
			{
				borrowToExtend.getBorrow().setBookCatalogNumber("0");
				DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
				client.sendToClient(returnMsg);
				return;
			}
			daysUntilExpectedReturnDate = rsBorrowDetails.getInt(4);
			subscriber = new Subscriber(borrowToExtend.getBorrow().getSubscriberId());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// check if current date is more than 7 days until expected return date - than
		// extend the borrow is unavailable
		if (daysUntilExpectedReturnDate > 7)
		{
			borrowToExtend.getBorrow().setActualReturnDate("1");
			DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
			client.sendToClient(returnMsg);
			return;
		} else if (!isSubscriberStatusActive(subscriber))
		// check if the subscriber status is not active (frozen/deep freeze/locked)
		{
			borrowToExtend.getBorrow().setSubscriberId("0");
			DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
			client.sendToClient(returnMsg);
			return;
		} else if (isBookArchived(bookToCheck)) // check if the book is archived
		{
			borrowToExtend.getBorrow().setBookCatalogNumber("-1");
			DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
			client.sendToClient(returnMsg);
			return;
		}
		// check if the book classification is wanted - than extend the borrow is
		// unavailable
		else if (getBookClassification(bookToCheck).equals("wanted"))
		{
			borrowToExtend.getBorrow().setBookCatalogNumber("-2");
			DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
			client.sendToClient(returnMsg);
			return;
		} else
		{
			updateDateFormat(borrowToExtend.getBorrow());
			query = BorrowExtensionQueries.addNewBorrowExtension(borrowToExtend);
			oblDB.executeUpdate(query); // add a new borrow extension to borrows_extensions table

			borrowToExtend.getBorrow().setExpectedReturnDate(borrowToExtend.getExtensionDate());
			query = BorrowsQueries.updateExpectedReturnDate(borrowToExtend.getBorrow());
			oblDB.executeUpdate(query); // update expected return date of the specific borrow on borrows table
			if (borrowToExtend.getExtensionType().equals("automatic")) // mean that the subscriber wanted to extend
																		// borrow return date
			{
				String messageContent = "The subscriber: " + borrowToExtend.getBorrow().getSubscriberId()
						+ " was sucessfully excuted a borrow extension.\n" + " Borrow number: "
						+ borrowToExtend.getBorrow().getId();
				OblMessage message = new OblMessage(messageContent, "librarian");
				query = OblMessagesQueries.sendMessageToLibrarians(message);
				oblDB.executeUpdate(query); // add a new message to messages table
			}
		}
		DBMessage returnMsg = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
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

	private boolean isAllBookCopiesAreUnavailable(Book bookToCheck)
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
		ResultSet rsNumOfUnavailableCopies = oblDB.executeQuery(query);
		try
		{
			rsNumOfUnavailableCopies.next();
			numOfUnavailableCopies = rsNumOfUnavailableCopies.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		if (maxCopies == numOfUnavailableCopies)
		{
			return true;
		}
		return false;
	}

	private boolean isAlreadyBorrowedCopyOfTheBook(BorrowACopyOfBook borrowToCheck)
	{
		String query = BorrowsQueries.getCurrentBorrowsForSubscriberID(borrowToCheck.getSubscriberId()); // search by
																											// copy ID
		ResultSet rsSubscriberCurrentBorrows = oblDB.executeQuery(query);
		try
		{
			while (rsSubscriberCurrentBorrows.next())
			{
				if (rsSubscriberCurrentBorrows.getString(5).equals(borrowToCheck.getBookCatalogNumber()))
				{
					return true;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private boolean isAllBookCopiesAreAvailable(Book bookToCheck)
	{
		int maxCopies, numOfAvailableCopies;
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
		query = CopiesQueries.getNumOfAvailableCopies(bookToCheck);// search by copy ID
		ResultSet rsNumOfAvailableCopies = oblDB.executeQuery(query);
		try
		{
			rsNumOfAvailableCopies.next();
			numOfAvailableCopies = rsNumOfAvailableCopies.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		if (maxCopies == numOfAvailableCopies)
		{
			return true;
		}
		return false;
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
			if (copyStatus.equals("available"))
				return true;
			return false;
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
			if (copyStatus.equals("unavailable"))
				return true;
			return false;
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

	private orderQueueCheckOptions orderQueueCheckForNewBorrow(Book book, Subscriber subscriber)
	{
		String query = OrdersQueries.getBookCurrentOrders(book);// search by book catalog number
		ResultSet rsBookCurrentOrders = oblDB.executeQuery(query);

		int bookNumOfCurrentOrders = getRowCount(rsBookCurrentOrders);
		if (bookNumOfCurrentOrders > 0) // means that the book orders queue is not empty
		{
			try
			{
				rsBookCurrentOrders.next();
				String subscriberID = rsBookCurrentOrders.getString(2);
				// check if the subscriber is first on orders queue
				if (subscriberID.equals(subscriber.getId()))
				{
					return orderQueueCheckOptions.SubscriberIsFirstInOrdersQueue;
				}
				return orderQueueCheckOptions.SubscriberIsNotFirstInOrdersQueue; // if the first subscriber on orders
																					// queue is not the same subscriber
			} catch (Exception e)
			{
				e.printStackTrace();
				return orderQueueCheckOptions.Error;
			}
		}
		// means that the book orders queue is empty
		return orderQueueCheckOptions.OrdersQueueIsEmpty;
	}

	private orderQueueCheckOptions orderQueueCheckForNewBorrowExtesion(Book book)
	{
		String query = OrdersQueries.getBookCurrentOrders(book);// search by book catalog number
		ResultSet rsBookCurrentOrders = oblDB.executeQuery(query);

		int bookNumOfCurrentOrders = getRowCount(rsBookCurrentOrders);
		if (bookNumOfCurrentOrders > 0)
		{
			
			//check if exist an active order with borrowArriveDate = null
			
			// means that the book orders queue is not empty
			return orderQueueCheckOptions.OrdersQueueIsNotEmpty;
		}
		// means that the book orders queue is empty
		return orderQueueCheckOptions.OrdersQueueIsEmpty;
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

	/*
	 * private int getBookCurrentNumOfOrders(Book book) { String query =
	 * BooksQueries.getCurrentNumOfOrders(book);// search by book catalog number
	 * ResultSet rsBookCurrentNumOfOrders = oblDB.executeQuery(query); try {
	 * rsBookCurrentNumOfOrders.next(); return rsBookCurrentNumOfOrders.getInt(1); }
	 * catch (Exception e) { e.printStackTrace(); return -1; } }
	 */

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

	/*
	 * private int getSubscriberNumOfLateReturns(Subscriber subscriberToUpdate) {
	 * String query = SubscribersQueries.getNumOfLateReturns(subscriberToUpdate);//
	 * search by subscriber ID ResultSet rsCurrentNumOfLateReturns =
	 * oblDB.executeQuery(query); try { rsCurrentNumOfLateReturns.next(); return
	 * rsCurrentNumOfLateReturns.getInt(1); } catch (Exception e) {
	 * e.printStackTrace(); return -1; } }
	 */

	private boolean isSubscriberLateAnotherReturn(BorrowACopyOfBook borrowToClose)
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
		ArrayList<BorrowACopyOfBook> borrowList = BorrowsQueries.createSpecificBorrowListFromRS(rs);

		client.sendToClient(new DBMessage(DBAction.GetCurrentBorrowsForSubID, borrowList));
	}

	private void getCurrentBorrow(ConnectionToClient client) throws IOException
	{
		String query = BorrowsQueries.getCurrentBorrows();
		ResultSet rs = oblDB.executeQuery(query);
		ArrayList<BorrowACopyOfBook> borrowList = BorrowsQueries.createSpecificBorrowListFromRS(rs);

		client.sendToClient(new DBMessage(DBAction.GetCurrentBorrows, borrowList));
	}

	private void getActivityLog(String subscriberID, ConnectionToClient client) throws IOException
	{
		ArrayList<ActivityLog> activityLogList;
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

		if (activityLogList.isEmpty())
		{
			DBMessage returnMsg = new DBMessage(DBAction.GetActivityLog, null);
			client.sendToClient(returnMsg);
			return;
		}

		DBMessage returnMsg = new DBMessage(DBAction.GetActivityLog, activityLogList);
		client.sendToClient(returnMsg);
	}

	private ArrayList<ActivityLog> getBorrowExtensionActivityLog(String subscriberID) throws IOException
	{

		String query = BorrowsQueries.searchBorrowExtensionFromSubscriberID(subscriberID);
		ResultSet rs = oblDB.executeQuery(query);
		int rowCount = getRowCount(rs);
		if (rowCount == 0)
		{
			return null;
		} else
		{
			ArrayList<ActivityLog> temp = BorrowsQueries.CreateBorrowExtensionListFromRS(rs);
			return temp;
		}

	}

	private ArrayList<ActivityLog> getOrderActivityLog(String subscriberID) throws IOException
	{

		String query = OrdersQueries.searchOrdersFromSubscriberID(subscriberID);
		ResultSet rs = oblDB.executeQuery(query);
		int rowCount = getRowCount(rs);
		if (rowCount == 0)
		{
			return null;
		} else
		{

			ArrayList<ActivityLog> temp = OrdersQueries.CreateOrdersListFromRS(rs);
			return temp;
		}
	}

	private ArrayList<ActivityLog> getReturnActivityLog(String subscriberID) throws IOException
	{

		String query = ReturnesQueries.searchReturnFromSubscriberID(subscriberID);
		ResultSet rs = oblDB.executeQuery(query);
		int rowCount = getRowCount(rs);
		if (rowCount == 0)
		{
			return null;
		} else
		{
			ArrayList<ActivityLog> temp = ReturnesQueries.CreateReturnListFromRS(rs);
			return temp;
		}

	}

	private ArrayList<ActivityLog> getBorrowActivityLog(String subscriberID) throws IOException
	{
		String query = BorrowsQueries.searchBorrowFromSubscriberID(subscriberID);
		ResultSet rs = oblDB.executeQuery(query);
		int rowCount = getRowCount(rs);
		if (rowCount == 0)
		{
			return null;
		} else
		{

			ArrayList<ActivityLog> temp = BorrowsQueries.CreateBorrowListFromRS(rs);
			return temp;
		}
	}

	private void reports_getAvarageBorrows(ConnectionToClient client) throws SQLException, IOException
	{
		// book id -> book
		Map<Integer, Book> booksList = createAlistOfAllBooks();
		ArrayList<BorrowACopyOfBook> borrowList;
		String query = BorrowsQueries.getBorrowsTable();
		ResultSet rs = oblDB.executeQuery(query);
		borrowList = BorrowsQueries.createBorrowListFromSelectAllRS(rs);
		Report_BorrowDurationInfo data = new Report_BorrowDurationInfo();
		Map<String, Integer> wantedBooks = new HashMap<String, Integer>();
		Map<String, Integer> regularBooks = new HashMap<String, Integer>();
		for (Integer key : booksList.keySet())
		{
			int average = 0;
			int count = 0;
			for (BorrowACopyOfBook borrow : borrowList)
			{
				if (borrow.getActualReturnDate() != null && borrow.getBookCatalogNumber().equals(key.toString()))
				{

					SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
					String borrowDate = borrow.getBorrowDate().substring(0, 10);
					String returnDate = borrow.getActualReturnDate().substring(0, 10);
					try
					{
						Date date1 = myFormat.parse(borrowDate);
						Date date2 = myFormat.parse(returnDate);
						long diff = date2.getTime() - date1.getTime();
						long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						average += diffInDays;
						count++;
					} catch (ParseException e)
					{
						e.printStackTrace();
					}

				}
			}
			if (count != 0)
				average = average / count;
			if (booksList.get(key).getClassification().equals("wanted"))
			{
				wantedBooks.put(booksList.get(key).getCatalogNumber(), average);
			} else
				regularBooks.put(booksList.get(key).getCatalogNumber(), average);
		}
		data.setRegularBooks(regularBooks);
		data.setWantedBooks(wantedBooks);
		DBMessage returnMsg = new DBMessage(DBAction.Reports_getAvarageBorrows, data);
		client.sendToClient(returnMsg);
	}

	private void moveBookToArchive(String catalogNumber, ConnectionToClient client) throws IOException
	{
		String query = BooksQueries.updateBookArciveStatus(catalogNumber);
		oblDB.executeUpdate(query);
	}

	/**
	 * in this function we get book catalog number and pull the path from db, then
	 * move the file to byte array and send it to the client
	 * 
	 * @throws SQLException
	 */
	private void sendPDFtoClient(Book book, ConnectionToClient client) throws IOException, SQLException
	{
		String localPath = pathToSavePDF + book.getCatalogNumber() + ".pdf";
		String jarPath = "resources\\tablesOfContent\\" + book.getCatalogNumber() + ".pdf";
		File file;
		byte[] mybytearray = null;
		try
		{
			/*localPath = rs.getString(9);
			if (localPath.charAt(0) == '.')
			{
				localPath = localPath.substring(2, localPath.length());

			}
			ClassLoader classLoader = getClass().getClassLoader();
			String temp = "resources\\tablesOfContent\\Table of content - Linear algebra.pdf";
			file = new File(classLoader.getResource(temp).toURI());*/
			file = new File(localPath);

			mybytearray = Files.readAllBytes(file.toPath());
		} catch (Exception e)
		{
			client.sendToClient(new DBMessage(DBAction.ViewTableOfContent, null));
			return;
		}
		DBMessage returnMsg = new DBMessage(DBAction.ViewTableOfContent, mybytearray);
		client.sendToClient(returnMsg);

	}

	private void reports_createAcitivityReport(ConnectionToClient client) throws IOException
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String reportDate = format.format(calendar.getTime());
		int totalNumberOfSubscribers = 0;
		int activeSubscribersNumber = 0;
		int lockedSubscribersNumber = 0;
		int frozenSubscribersNumber = 0;
		int currentNumOfBorrows = 0;
		int numOfLateSubscribers = 0;
		Report_Activity report;
		// Total:
		String query = ReportsQueries.countTotalSubscribers();
		ResultSet rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			totalNumberOfSubscribers = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// active:
		query = ReportsQueries.countActiveSubscribers();
		rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			activeSubscribersNumber = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// frozen:
		query = ReportsQueries.countFrozenSubscribers();
		rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			frozenSubscribersNumber = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// locked:
		query = ReportsQueries.countLockedSubscribers();
		rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			lockedSubscribersNumber = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// current borrow:
		query = ReportsQueries.countCurrentBorrows();
		rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			currentNumOfBorrows = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// late subscribers:
		query = ReportsQueries.countSubscribersNumThatLate();
		rs = oblDB.executeQuery(query);
		try
		{
			rs.next();
			numOfLateSubscribers = rs.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		report = new Report_Activity(reportDate, totalNumberOfSubscribers, activeSubscribersNumber,
				lockedSubscribersNumber, frozenSubscribersNumber, currentNumOfBorrows, numOfLateSubscribers);
		client.sendToClient(new DBMessage(DBAction.Reports_Activity, report));
	}

	private void reports_getList(ConnectionToClient client) throws IOException
	{
		String query = ReportsQueries.selectAllFromReports();
		ResultSet rs = oblDB.executeQuery(query);
		List<Report_Activity> listOfReports = ReportsQueries.createListOfReportsFromRS(rs);
		DBMessage returnMsg = new DBMessage(DBAction.Reports_getList, listOfReports);
		client.sendToClient(returnMsg);
	}

	private void reports_AddNewToList(Report_Activity report, ConnectionToClient client) throws IOException
	{
		String query = ReportsQueries.addReport(report);
		if (oblDB.executeUpdate(query) != 0)
		{
			client.sendToClient(new DBMessage(DBAction.Reports_Add, report));
		} else
			client.sendToClient(new DBMessage(DBAction.Reports_Add, null));
	}

	private void reports_LateReturns(ConnectionToClient client) throws IOException, SQLException
	{
		Report_LateReturns report = ReportsQueries.CreateLateReturnsReport(oblDB);
		client.sendToClient(new DBMessage(DBAction.Reports_LateReturns, report));
	}

	private void addNewBook(Book book, ConnectionToClient client) throws IOException, SQLException
	{
		String query;
		int rowCount = 0;
		ResultSet rs;
		DBMessage returnMsg;

		query = BooksQueries.SearchBookByNameAndEdition(book);
		rs = oblDB.executeQuery(query);
		rowCount = getRowCount(rs);
		if (rowCount == 1)// book name already exist
		{
			returnMsg = new DBMessage(DBAction.AddBook, null);
			client.sendToClient(returnMsg);
			return;
		}

		book.setTableOfContenPath("");// we no longer put this in DB

		query = BooksQueries.AddBook(book);
		if (oblDB.executeUpdate(query) == 0)
		{
			returnMsg = new DBMessage(DBAction.AddBook, null);
			client.sendToClient(returnMsg);
			return;
		}

		query = BooksQueries.GetCatalogNumberByNameAndEdition(book);
		rs = oblDB.executeQuery(query);
		rowCount = getRowCount(rs);
		if (rowCount == 0)
		{
			returnMsg = new DBMessage(DBAction.AddBook, null);
			client.sendToClient(returnMsg);
			return;
		}
		try
		{
			rs.next();
			book.setCatalogNumber(rs.getString(1));
		} catch (SQLException e)
		{
			returnMsg = new DBMessage(DBAction.AddBook, null);
			client.sendToClient(returnMsg);
		}
		
		if (book.getTocArraybyte() != null)
		{
			createFileFromByteArray(book.getTocArraybyte(), book.getCatalogNumber(), "pdf",	pathToSavePDF);
		}

		rowCount = 0;

		for (String author : book.getAuthorNameList())
		{

			query = BooksQueries.SearchAuthor(author);
			rs = oblDB.executeQuery(query);
			rowCount = getRowCount(rs);
			if (rowCount == 0)
			{
				query = BooksQueries.AddAuthor(author);
				oblDB.executeUpdate(query);
				query = BooksQueries.AddBookAuthors(book.getCatalogNumber(), author);
				oblDB.executeUpdate(query);
			}
		}
		try
		{
			rowCount = 0;
			for (String category : book.getCategories())
			{

				query = BooksQueries.SearchCategory(category);
				rs = oblDB.executeQuery(query);
				rowCount = getRowCount(rs);
				if (rowCount == 0)
				{
					query = BooksQueries.AddCategory(category);
					oblDB.executeUpdate(query);
					query = BooksQueries.AddBookCategory(book.getCatalogNumber(), category);
					oblDB.executeUpdate(query);
				}
			}

			int copies = book.getMaxCopies();

			for (int i = 0; i < copies; i++)
			{
				query = BooksQueries.AddCopy(book.getCatalogNumber());
				oblDB.executeUpdate(query);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			returnMsg = new DBMessage(DBAction.AddBook, null);
			client.sendToClient(returnMsg);
		}
		returnMsg = new DBMessage(DBAction.AddBook, "Success");
		client.sendToClient(returnMsg);
	}

	private boolean createFileFromByteArray(byte[] bytes, String fileName, String fileType, String filePath)
	{
		File outputFile = new File(filePath + fileName + "." + fileType);

		try (FileOutputStream outputStream = new FileOutputStream(outputFile);)
		{

			outputStream.write(bytes); // write the bytes and your done.
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private void changeBookDetails(Book book, ConnectionToClient client) throws IOException, SQLException
	{
		String query;
		ResultSet rs;
		int rowCount=0;
		
		//first check if the book name and edition number are not already exist in the program
		query = BooksQueries.SearchBookByNameAndEdition(book);
		rs = oblDB.executeQuery(query);
		rowCount = getRowCount(rs);
		if (rowCount == 1)// book name already exist in different book
		{
			rs.next();
			String catalogNumber=rs.getString(1);
			if(!(catalogNumber.equals(book.getCatalogNumber())))
			{
				DBMessage returnMessage= new DBMessage(DBAction.EditBookDetails, null);
				client.sendToClient(returnMessage);
				return;
			}
		}
		
		query = BooksQueries.changeBookFields(book);
		oblDB.executeUpdate(query);
		// update book authors :
		ArrayList<String> newAuthorsList = book.getAuthorNameList();
		query = BooksQueries.getAuthorsFromBook(book);
		rs = oblDB.executeQuery(query);
		int numberOfOldCatagories = getRowCount(rs);
		for (int i = 0; i < numberOfOldCatagories; i++)
		{
			try
			{
				rs.next();
				String oldAuthor = rs.getString(1);
				for (String newAuthor : newAuthorsList)
				{
					query = BooksQueries.getAuthor(newAuthor);
					ResultSet rs2 = oblDB.executeQuery(query);
					int rowCount2 = getRowCount(rs2);
					if (rowCount2 == 0)
					{
						query = BooksQueries.addAuthor(newAuthor);
						oblDB.executeUpdate(query);
						query = BooksQueries.addAuthorToBook(newAuthor, book);
						oblDB.executeUpdate(query);
					} else
					{
						query = BooksQueries.getAuthorFromBook(book, newAuthor);
						rs2 = oblDB.executeQuery(query);
						rowCount2 = getRowCount(rs2);
						if (rowCount2 == 0)
						{
							query = BooksQueries.addAuthorToBook(newAuthor, book);
							oblDB.executeUpdate(query);
						}
					}

					if (!(newAuthorsList.contains(oldAuthor)))// in case we want to delete author from db
					{
						query = BooksQueries.deleteAuthor(oldAuthor, book);
						oblDB.executeUpdate(query);
					}
				}

			} catch (SQLException exp)
			{
				exp.printStackTrace();
				DBMessage returnMessage = new DBMessage(DBAction.EditBookDetails,null);
				client.sendToClient(returnMessage);
			}

		}
		// update book categories
		ArrayList<String> newCategories = book.getCategories();
		query = BooksQueries.getCategoriesForBookId(book.getCatalogNumber());
		rs = oblDB.executeQuery(query);
		numberOfOldCatagories = getRowCount(rs);
		for (int i = 0; i < numberOfOldCatagories; i++)
		{
			try
			{
				rs.next();
				String oldCategory = rs.getString(1);
				for (String oldCatagory : newCategories)
				{
					query = BooksQueries.getCategoryByName(oldCatagory);
					ResultSet rs2 = oblDB.executeQuery(query);
					int rowCount2 = getRowCount(rs2);
					if (rowCount2 == 0) // in case there is not such category at all so we want to add it
					{
						query = BooksQueries.addCagegory(oldCatagory);
						oblDB.executeUpdate(query);
						query = BooksQueries.addCagegoryToBook(oldCatagory, book);
						oblDB.executeUpdate(query);
					} else // add only to books_categories table
					{
						query = BooksQueries.getCategoriesFromBook(book, oldCatagory);
						rs2 = oblDB.executeQuery(query);
						rowCount2 = getRowCount(rs2);
						if (rowCount2 == 0)
						{
							query = BooksQueries.addCagegoryToBook(oldCatagory, book);
							oblDB.executeUpdate(query);
						}
					}
				}
				if (!(newCategories.contains(oldCategory))) // in case we want to delete a category
				{
					query = BooksQueries.deleteCategory(oldCategory, book);
					oblDB.executeUpdate(query);
				}

			} catch (SQLException exp)
			{
				exp.printStackTrace();
				DBMessage returnMessage = new DBMessage(DBAction.EditBookDetails,null);
				client.sendToClient(returnMessage);
			}
		}
		// update book copies
		query = CopiesQueries.getBookCopiesDetails(book);
		ResultSet currentCopies = oblDB.executeQuery(query);
		List<String> updatedCopies = new ArrayList<String>();
		//removing copies:
		for (CopyOfBook copy : book.getCopies())
		{
			updatedCopies.add(copy.getId());
		}
		while (currentCopies.next())
		{
			if (!updatedCopies.contains(currentCopies.getString(1))) //if needed to remove this copy from db
			{
				query=CopiesQueries.deleteCopyFromBook(currentCopies.getString(1));
				oblDB.executeUpdate(query);
			}
		}
		//adding copies:
		int copies = book.getMaxCopies();
		for (int i = 0; i < copies; i++)
		{
			query = BooksQueries.AddCopy(book.getCatalogNumber());
			oblDB.executeUpdate(query);
		}
		DBMessage returnMessage = new DBMessage(DBAction.EditBookDetails, book);
		client.sendToClient(returnMessage);
	}

	public MySQLConnection getConnection()
	{
		return oblDB;
	}
 
	private void graduateStudent(String studentID) throws IOException, SQLException
	{
		String query;
		ResultSet rs;
		int rowCount=0;
		String status;
		if(studentID.length()==8)
		{
			studentID="0"+studentID;
		}
		Subscriber subscriber=new Subscriber(studentID);
		query=SubscribersQueries.searchSubscriberByID(subscriber);
		rs=oblDB.executeQuery(query);
		rowCount=getRowCount(rs);
		if(rowCount==0)
		{
			//this subscriber does not exist
			return;
		}
		query=SubscribersQueries.getSubscriberStatus(subscriber);
		rs=oblDB.executeQuery(query);
		rs.next();
		status=rs.getString(1);
		subscriber.setStatus(status);
		query=BorrowsQueries.getCurrentBorrowsForSubscriberID(subscriber.getId());
		rs=oblDB.executeQuery(query);
		rowCount=getRowCount(rs);
		if(rowCount==0) //if there are not borrowed books for this subscriber (we don't care if his status is active or frozen)
		{
			subscriber.setStatus("locked");
			query=SubscribersQueries.updateSubscriberStatus(subscriber);
			oblDB.executeUpdate(query);
			//need to add here update graduation to 'yes'
		}
		else if (rowCount>0) //in case there are current borrows so the subscriber can't be locked
		{
			subscriber.setStatus("frozen");
			query=SubscribersQueries.updateSubscriberStatus(subscriber);
			oblDB.executeUpdate(query);
			//need to add here update graduation to 'yes'
		}
	
	}
}

