package entities;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportsQueries
{
	public static String countTotalSubscribers()
	{
		String queryMsg = "SELECT COUNT(*)	FROM obl_db.subscribers";
		return queryMsg;
	}

	public static String countActiveSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) " + "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'active'";
		return queryMsg;
	}

	public static String countFrozenSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) " + "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'frozen'";
		return queryMsg;
	}

	public static String countLockedSubscribers()
	{
		String queryMsg = "SELECT COUNT(*) " + "FROM obl_db.subscribers "
				+ "WHERE obl_db.subscribers.status = 'locked'";
		return queryMsg;
	}

	public static String countCurrentBorrows()
	{
		String queryMsg = "SELECT COUNT(*) " + "FROM obl_db.borrows " + "WHERE obl_db.borrows.actualReturnDate IS NULL";
		return queryMsg;
	}

	/*
	 * This query get the number of subscribers that that didnt return a book on
	 * time.
	 */
	public static String countSubscribersNumThatLate()
	{
		String queryMsg = "SELECT COUNT(*) FROM " + "(SELECT * FROM obl_db.borrows "
				+ "WHERE obl_db.borrows.isReturnedLate = 'yes' " + "GROUP BY obl_db.borrows.subscriberID) "
				+ "as lateReturnsPerSub";
		return queryMsg;
	}

	public static String selectAllFromReports()
	{
		String queryMsg = "SELECT * FROM obl_db.reports_activity";
		return queryMsg;
	}

	public static List<Report_Activity> createListOfReportsFromRS(ResultSet rs)
	{
		List<Report_Activity> list = new ArrayList<Report_Activity>();
		try
		{
			while (rs.next())
			{
				Report_Activity temp = new Report_Activity(rs.getString(2), rs.getInt(5), rs.getInt(6), 
						rs.getInt(8), rs.getInt(7), rs.getInt(3), rs.getInt(4));
				list.add(temp);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public static String addReport(Report_Activity report)
	{
		String queryMsg = "INSERT INTO obl_db.reports_activity "
				+ "(date, numOfBorrows, numOfSubscriberLate,totalSubscribers,activeSubscribers,frozenSubscribers,lockedSubscribers) VALUES"
				+ " ('"+report.getReportDate()+"',"+report.getCurrentNumOfBorrows()+
				","+report.getNumOfLateSubscribers()+","+report.getTotalNumberOfSubscribers()+
				","+report.getActiveSubscribersNumber()+","+report.getFrozenSubscribersNumber()+","+report.getLockedSubscribersNumber()+")";
		return queryMsg;
	}

}
