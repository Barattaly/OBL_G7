package entities;

import java.sql.ResultSet;
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
	
	public static BorrowACopyOfBook searchSpecificBorrow(ResultSet rs, BorrowACopyOfBook borrowToSearch)
	{
		BorrowACopyOfBook borrowToClose = new BorrowACopyOfBook();
		try
		{
			while (rs.next())
			{
				borrowToClose.setBookCatalogNumber(rs.getString(7));
				borrowToClose.setCopyId(rs.getString(8));
				if(borrowToSearch.getBookCatalogNumber().equals(borrowToClose.getBookCatalogNumber()) &&
						borrowToSearch.getCopyId().equals(borrowToClose.getCopyId()))
				{
					borrowToClose.setId(rs.getString(1));
					borrowToClose.setSubscriberId(rs.getString(2));
					borrowToClose.setBorrowDate(rs.getString(3));
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
	
	public static String getIsReturnedLate(BorrowACopyOfBook borrowToClose)
	{
		if (borrowToClose == null)
			return null;
		
		String queryMsg = "SELECT isReturnedLate FROM obl_db.borrows WHERE (id = '" + borrowToClose.getId() 
						+ "') and (subscriberID = '" + borrowToClose.getSubscriberId() + "');";
		return queryMsg;
	}
}
