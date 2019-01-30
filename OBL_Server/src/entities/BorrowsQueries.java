package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class BorrowsQueries
{
	public static String addNewBorrow(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.borrows (subscriberID, borrowDate, expectedReturnDate, isReturnedLate, bookCatalogNumber, copyID)"
						+ " VALUES ('" + borrowToAdd.getSubscriberId() + "', '" + currentDateTimeString + "', '" + borrowToAdd.getExpectedReturnDate() 
						+ "', 'no', '" + borrowToAdd.getBookCatalogNumber() + "', '" + borrowToAdd.getCopyId() + "');";
		return queryMsg;
	}

	public static String getBorrowsTable()
	{
		String queryMsg = "SELECT * FROM obl_db.borrows;";
		return queryMsg;
	}

	public static String getCurrentBorrowsTable()
	{
		String queryMsg = "SELECT * FROM obl_db.borrows" 
						+ " WHERE actualReturnDate is null;";
		return queryMsg;
	}
	
	public static BorrowACopyOfBook searchSpecificBorrow(ResultSet rs, BorrowACopyOfBook borrowToSearch)
	{
		BorrowACopyOfBook borrowToClose = new BorrowACopyOfBook();
		try
		{
			while (rs.next())
			{
				borrowToClose.setBookCatalogNumber(rs.getString(7));
				borrowToClose.setCopyId(rs.getString(8));
				if (borrowToSearch.getBookCatalogNumber().equals(borrowToClose.getBookCatalogNumber())
						&& borrowToSearch.getCopyId().equals(borrowToClose.getCopyId()))
				{
					borrowToClose.setId(rs.getString(1));
					borrowToClose.setSubscriberId(rs.getString(2));
					borrowToClose.setBorrowDate(rs.getString(3));
					borrowToClose.setExpectedReturnDate(rs.getString(4));
					borrowToClose.setIsReturnedLate(rs.getString(6));
				}
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return borrowToClose;
	}
	
	public static String updateIsReturnedLateToYes(BorrowACopyOfBook borrowToClose)
	{
		if (borrowToClose == null)
			return null;
		
		String queryMsg = "UPDATE obl_db.borrows SET isReturnedLate = '" + borrowToClose.getIsReturnedLate() 
						+ "' WHERE (id = '" + borrowToClose.getId() 
						+ "') and (subscriberID = '" + borrowToClose.getSubscriberId() + "');";
		return queryMsg;
	}
	
	public static String updateReturnDate(BorrowACopyOfBook borrowToClose)
	{
		if (borrowToClose == null)
			return null;
		
		String queryMsg = "UPDATE obl_db.borrows SET actualReturnDate = '" + borrowToClose.getActualReturnDate() 
						+ "' WHERE (id = '" + borrowToClose.getId() 
						+ "') and (subscriberID = '" + borrowToClose.getSubscriberId() + "');";
		return queryMsg;
	}
	
	public static String getcurrentDateTimesString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(calendar.getTime());
		return string;
	}
	
	public static String getIsReturnedLate(BorrowACopyOfBook borrow)
	{
		if (borrow == null)
			return null;
		
		String queryMsg = "SELECT isReturnedLate FROM obl_db.borrows WHERE (id = '" + borrow.getId() 
						+ "') and (subscriberID = '" + borrow.getSubscriberId() + "');";
		return queryMsg;
	}
	/*
	 * current = not returned yet
	 */
	public static String getCurrentBorrowsForSubscriberID(String id)
	{
		String queryMsg = "SELECT id, borrowDate, expectedReturnDate,copyID, bookCatalogNumber,subscriberID"
				+ " FROM obl_db.borrows  WHERE actualReturnDate IS NULL AND subscriberID = '" + id + "';";
		return queryMsg;
	}
	
	public static String getCurrentBorrows()
	{
		String queryMsg = "SELECT id, borrowDate, expectedReturnDate,copyID, bookCatalogNumber,subscriberID"
				+ " FROM obl_db.borrows  WHERE actualReturnDate IS NULL;";
		return queryMsg;
	}


	public static ArrayList<BorrowACopyOfBook> createSpecificBorrowListFromRS(ResultSet rs)
	{
		ArrayList<BorrowACopyOfBook> borrowList = new ArrayList<>();
		try
		{
			while (rs.next())
			{
				//just used an existing constructor and used setter for the rest. only needed info for table view
				BorrowACopyOfBook temp = new BorrowACopyOfBook(rs.getString(6), rs.getString(5), rs.getString(4));
				temp.setId(rs.getString(1));
				temp.setBorrowDate(rs.getString(2));
				temp.setExpectedReturnDate(rs.getString(3));
				borrowList.add(temp);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return borrowList;
	}
	
	public static String searchBorrowExtensionFromSubscriberID(String subscriberID)
	{
		if (subscriberID == null)
			return null;
		/*String queryMsg = "SELECT bookName, extensionDate, type, userID"
						+" FROM(SELECT borrows.id AS borrowID, borrows.subscriberID, books.name AS bookName"
					    +" FROM obl_db.books"
						+" inner join borrows ON books.catalogNumber = borrows.BookCatalogNumber"
					    +" WHERE borrows.subscriberID = '" + subscriberID +"') AS borrowBookName"
					    +" INNER JOIN borrow_extension ON borrowBookName.borrowID = borrow_extension.borrowID";*/
		String queryMsg = "SELECT bookName, extensionDate, extensionType, CONCAT(users.firstName, ' ', users.lastName) AS userName" 
					    + " FROM(SELECT bookName, extensionDate, type AS extensionType, userID " 
					    		+ " FROM(SELECT borrows.id AS borrowID, borrows.subscriberID, books.name AS bookName" 
					    				+ " FROM obl_db.books "
					    				+ " INNER JOIN borrows ON books.catalogNumber = borrows.BookCatalogNumber " 
					    				+ " WHERE borrows.subscriberID = '" + subscriberID +"') AS borrowBookName" 
					    		+ " INNER JOIN borrow_extension ON borrowBookName.borrowID = borrow_extension.borrowID) AS borrowExtensionTable" 
					    + " INNER JOIN users ON borrowExtensionTable.userID = users.id;";
		
		return queryMsg;
	}
	
	public static ArrayList<ActivityLog> CreateBorrowExtensionListFromRS(ResultSet rs)
	{
		ArrayList<ActivityLog> logs = new ArrayList<ActivityLog>();
		ActivityLog temp;
		try
		{
			while (rs.next())
			{
				if(rs.getString(3).equals("manual"))
				{
					
					temp = new ActivityLog("Borrow Extension",rs.getString(1),rs.getString(2),rs.getString(3) + " by " + rs.getString(4));
				}
				else
					temp = new ActivityLog("Borrow Extension",rs.getString(1),rs.getString(2),rs.getString(3));
				
				logs.add(temp);
				
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return logs;
	}
	public static String searchBorrowFromSubscriberID(String subscriberID)
	{
		if (subscriberID == null)
			return null;
		String queryMsg = "SELECT borrowDate, name"  
				+" FROM obl_db.borrows INNER JOIN obl_db.books ON borrows.bookCatalogNumber = books.catalogNumber"
				+" WHERE borrows.subscriberID ='"+ subscriberID +"';";
		return queryMsg;
	}
	
	public static ArrayList<ActivityLog> CreateBorrowListFromRS(ResultSet rs)
	{
		ArrayList<ActivityLog> logs = new ArrayList<ActivityLog>();
		try
		{
			while (rs.next())
			{
				ActivityLog temp = new ActivityLog("Borrow",rs.getString(2),rs.getString(1)," ");
				logs.add(temp);
				
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return logs;
	}
	
	public static ArrayList<BorrowACopyOfBook> createBorrowListFromSelectAllRS(ResultSet rs)
	{
		ArrayList<BorrowACopyOfBook> borrowList = new ArrayList<>();
		try
		{
			while (rs.next())
			{
				//just used an existing constructor and used setter for the rest. only needed info for table view
				BorrowACopyOfBook temp = new BorrowACopyOfBook(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4), 
						rs.getString(5), rs.getString(6), rs.getString(7));
				temp.setCopyId(rs.getString(8));
				borrowList.add(temp);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return borrowList;
	}
	
	public static String getBookCurrentBorrows(Book book)
	{
		if (book == null)
			return null;
		String queryMsg = "SELECT id, subscriberID, borrowDate, expectedReturnDate, isReturnedLate, bookCatalogNumber, copyID " 
						+ "FROM obl_db.borrows " 
						+ "WHERE bookCatalogNumber = '" + book.getCatalogNumber() + "' AND actualReturnDate is null;";
		return queryMsg;
	}
	
	public static String getBorrowDetails(String borrowId)
	{
		String queryMsg = "SELECT subscriberID, borrowDate, expectedReturnDate, DATEDIFF(expectedReturnDate,CURDATE()), " 
						+ "bookCatalogNumber, copyID " 
						+ "FROM obl_db.borrows WHERE id = '" + borrowId + "';";
		return queryMsg;
	}
	
	public static String updateExpectedReturnDate(BorrowACopyOfBook borrowToUpdate)
	{
		if (borrowToUpdate == null)
			return null;
		
		String queryMsg = "UPDATE obl_db.borrows SET expectedReturnDate = '" + borrowToUpdate.getExpectedReturnDate()
						+ "' WHERE (id = '" + borrowToUpdate.getId() 
						+ "') and (subscriberID = '" + borrowToUpdate.getSubscriberId() + "');";
		return queryMsg;
	}
	
}
