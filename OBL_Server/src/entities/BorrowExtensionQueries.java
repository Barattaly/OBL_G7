package entities;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class BorrowExtensionQueries 
{
	public static String addNewBorrowExtension(BorrowExtension borrowToExtend)
	{
		if (borrowToExtend == null)
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.borrows_extensions (borrowID, extensionDate, type, userID) "
						+ "VALUES ('" + borrowToExtend.getBorrow().getId() + "', '" + currentDateTimeString + "', '" + borrowToExtend.getExtensionType() 
						+ "', '" + borrowToExtend.getBorrow().getSubscriberId() + "');";
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
