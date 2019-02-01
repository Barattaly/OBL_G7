package entitiesQueries;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import entities.BorrowExtension;

public class BorrowExtensionQueries 
{
	public static String addNewBorrowExtension(BorrowExtension borrowToExtend)
	{
		if (borrowToExtend == null)
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.borrow_extension (borrowID, extensionDate, type, userID) "
						+ "VALUES ('" + borrowToExtend.getBorrow().getId() + "', '" + currentDateTimeString + "', '" + borrowToExtend.getExtensionType() 
						+ "', '" + borrowToExtend.getUserId() + "');";
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
