package entities;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ReturnesQueries
{

	public static String searchReturnFromSubscriberID(String subscriberID)
	{
		if (subscriberID == null)
			return null;
		String queryMsg = "SELECT actualReturnDate, isReturnedLate , name "
				+ " FROM obl_db.borrows INNER JOIN obl_db.books ON borrows.bookCatalogNumber = books.catalogNumber"
				+ " WHERE borrows.subscriberID ='" + subscriberID + "';";
		return queryMsg;
	}

	public static ArrayList<ActivityLog> CreateReturnListFromRS(ResultSet rs)
	{
		ArrayList<ActivityLog> logs = new ArrayList<ActivityLog>();
		try
		{
			while (rs.next())
			{
				ActivityLog temp = new ActivityLog("Return", rs.getString(3), rs.getString(1), rs.getString(2));
				logs.add(temp);

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return logs;
	}
}
