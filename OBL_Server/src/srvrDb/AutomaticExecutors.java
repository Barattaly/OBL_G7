package srvrDb;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import entities.BorrowACopyOfBook;
import entities.BorrowsQueries;
import entities.OblMessage;
import entities.OblMessagesQueries;
import entities.Subscriber;
import entities.SubscribersQueries;

public class AutomaticExecutors 
{
	private static MySQLConnection oblDB; 
	
	public AutomaticExecutors(MySQLConnection oblDb)
	{
		if(oblDb == null)
			return;
		AutomaticExecutors.oblDB = oblDb;
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(() -> checkAndUpdateLateReturns(), 0, 15, TimeUnit.MINUTES);
		/*executor.scheduleAtFixedRate(() -> task2(), 0, 3, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> task3(), 0, 1, TimeUnit.MINUTES);*/
	}
	
	/*checkAndUpdateLateReturns
	 * This method suppose to run automatically every 24 hours. If subscriber is
	 * late at return a copy of a book, his status will change to "frozen", and the
	 * borrow "isReturnedLate" flag will change to "yes".
	 * If it's the third late of the same subscriber - than message with the subscriber
	 * details is send to the library manager in order to change subscriber card
	 * status to "deep freeze" */
	static void checkAndUpdateLateReturns()
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
				boolean existInArrayList = false;
				// check if the subscriber is late at return
				if (LocalDate.parse(returnDate).isAfter(LocalDate.parse(expectedBorrowDate))) 
				{
					lateReturnsCount++;
					if (lateReturnsCount >= 3)
					{
						for (Subscriber subscriberToCheck : subscribersLateReturnsAtListThreeTimes)
						{
							if(subscriberToCheck.getId().equals(subscriberToUpdate.getId()))
								existInArrayList = true;
						}
						if(!existInArrayList)
							subscribersLateReturnsAtListThreeTimes.add(subscriberToUpdate);
					}
					query = BorrowsQueries.getIsReturnedLate(borrowFromBorrowsTable);
					// get the value of "isReturnedLate" flag
					ResultSet rsIsReturnedLate = oblDB.executeQuery(query); 
					try
					{
						rsIsReturnedLate.next();
						if (rsIsReturnedLate.getString(1).equals("no"))
						{
							borrowFromBorrowsTable.setIsReturnedLate("yes");
							query = BorrowsQueries.updateIsReturnedLateToYes(borrowFromBorrowsTable);
							// update the flag at the borrow if the subscriber returned the copy late
							oblDB.executeUpdate(query);
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
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
		 * subscriber card the message will include the ArrayList:
		 * subscribersLateReturnsAtListThreeTimes
		 */
		OblMessage message;
		String messageContent;
		for (Subscriber subscriber : subscribersLateReturnsAtListThreeTimes)
		{
			messageContent = "The subscriber: " + subscriber.getId()
					  	   + " is late at return of 3 books.\n"
					  	   + "Please approve to change the subscriber card status to deep freeze";
			message = new OblMessage(messageContent, "library manager");
			query = OblMessagesQueries.sendMessageToLibraryManager(message);
			oblDB.executeUpdate(query); // add a new message to messages table
		}
	}
	
	private static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}
	
	private static int getSubscriberNumOfLateReturns(Subscriber subscriberToUpdate)
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
	
	public void setOblDb(MySQLConnection oblDb)
	{
		this.oblDB = oblDb;
	}

}
