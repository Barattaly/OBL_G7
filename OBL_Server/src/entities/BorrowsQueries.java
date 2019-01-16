package entities;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class BorrowsQueries 
{
	public static String addNewBorrow(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String currentDateTimeString = getNowAsString();
		
		String queryMsg = "INSERT INTO obl_db.borrows (subscriberID, borrowDate, expectedReturnDate, isReturnedLate, bookCatalogNumber)"
						+ " VALUES ('" + borrowToAdd.getSubscriberId() + "', '" + currentDateTimeString + "', '" + borrowToAdd.getExpectedReturnDate() 
						+ "', 'no', '" + borrowToAdd.getBookCatalogNumber() +"');";
		return queryMsg;
	}
	
	public static String getNowAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(calendar.getTime());
		return string;
	}
	
	public static String searchBookByCatalogNumber(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.books WHERE books.catalogNumber = '" + borrowToAdd.getBookCatalogNumber()+ "';";
		return queryMsg;
	}
}
