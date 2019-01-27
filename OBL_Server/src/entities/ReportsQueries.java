package entities;

public class ReportsQueries
{
	public static String countTotalSubscribers()
	{
		String queryMsg = "SELECT COUNT(*)	FROM obl_db.subscribers";
		return queryMsg;
	}
	public static String countActiveSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) "
				+ "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'active'";
		return queryMsg;
	}	
	public static String countFrozenSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) "
				+ "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'frozen'";		
		return queryMsg;
	}	
	public static String countLockedSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) "
				+ "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'locked'";		
		return queryMsg;
	}
	public static String countCurrentBorrows()
	{
		String queryMsg = "SELECT COUNT(*) "
				+ "FROM obl_db.borrows "
				+ "WHERE obl_db.borrows.actualReturnDate IS NULL";
		return queryMsg;
	}
	/*
	 * This query get the number of subscribers that that didnt return a book on time.
	 */
	public static String countSubscribersNumThatLate()
	{
		String queryMsg = "SELECT COUNT(*) FROM "
				+ "(SELECT * FROM obl_db.borrows "
				+ "WHERE obl_db.borrows.isReturnedLate = 'yes' "
				+ "GROUP BY obl_db.borrows.subscriberID) "
				+ "as lateReturnsPerSub";
		return queryMsg;
	}
	
	
}
