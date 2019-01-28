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
import entities.Subscriber;
import entities.SubscribersQueries;

public class Executors 
{
	private static MySQLConnection oblDB; 
	
	public Executors(MySQLConnection oblDb)
	{
		if(oblDb == null)
			return;
		this.oblDB = oblDb;
		//call to automate func
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(() -> checkAndUpdateLateReturns(), 0, 5, TimeUnit.MINUTES);
		/*executor.scheduleAtFixedRate(() -> task2(), 0, 3, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> task3(), 0, 1, TimeUnit.MINUTES);*/
	}
	
	/*checkAndUpdateLateReturns
	 * This method suppose to run automatically every 24 hours. If subscriber is
	 * late at return a copy of a book, his status will change to "frozen", and the
	 * borrow "isReturnedLate" flag will change to "yes".
	 */
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
