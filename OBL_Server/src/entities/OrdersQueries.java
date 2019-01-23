package entities;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class OrdersQueries 
{
	public static String addNewOrder(BookOrder bookOrder)
	{
		if (bookOrder == null)
			return null;
		String currentDateTimeString = getcurrentDateTimesString();
		
		String queryMsg = "INSERT INTO obl_db.orders (subscriberID, orderDate, bookCatalogNumber) "
						+ "VALUES ('" + bookOrder.getSubscriberId() + "', '" + currentDateTimeString + "', '" 
						+ bookOrder.getBookCatalogNumber() + "');";
		return queryMsg;
	}
	
	public static String getcurrentDateTimesString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(calendar.getTime());
		return string;
	}
}
