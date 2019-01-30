package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.MySQLConnection;

import entities.Report_LateReturns.NumberAndDurationOfLates;

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
	
	public static Report_LateReturns CreateLateReturnsReport(srvrDb.MySQLConnection oblDB) throws SQLException
	{
		Report_LateReturns reportGenereted = new Report_LateReturns();
		Map<String, NumberAndDurationOfLates> mapOfBooksToInfo = new HashMap<>();
		String query = "SELECT bookCatalogNumber,DATEDIFF(actualReturnDate,expectedReturnDate) FROM obl_db.borrows WHERE isReturnedLate = 'yes';";
		ResultSet rs = oblDB.executeQuery(query);
		//count amount of lates
		while(rs.next())
		{
			String catalogNumer = rs.getString(1);
			int lateDuration = rs.getInt(2);
			if(mapOfBooksToInfo.keySet().contains(catalogNumer))
			{
				int currentNumOfLates = mapOfBooksToInfo.get(catalogNumer).getNumberOfLates();
				int currentDuration = mapOfBooksToInfo.get(catalogNumer).getDurationOfLates();
				currentNumOfLates++;
				currentDuration +=lateDuration;
				mapOfBooksToInfo.get(catalogNumer).setNumberOfLates(currentNumOfLates);
				mapOfBooksToInfo.get(catalogNumer).setDurationOfLates(currentDuration);
			}
			else
			{
				mapOfBooksToInfo.put(catalogNumer, reportGenereted.new NumberAndDurationOfLates());//the first late
				
				mapOfBooksToInfo.get(catalogNumer).setNumberOfLates(1);//the first late
				mapOfBooksToInfo.get(catalogNumer).setDurationOfLates(lateDuration);
			}
		}
		//calc the avarage of duration
		for(String key : mapOfBooksToInfo.keySet())
		{
			int avarage = mapOfBooksToInfo.get(key).getDurationOfLates()/mapOfBooksToInfo.get(key).getNumberOfLates();
			mapOfBooksToInfo.get(key).setDurationOfLates(avarage);
		}
		//calc total amount of returns
		for(String key : mapOfBooksToInfo.keySet())
		{
			query = "SELECT COUNT(actualReturnDate) FROM obl_db.borrows WHERE bookCatalogNumber = " + key;
			rs = oblDB.executeQuery(query);
			rs.next();
			int numberOfReturn = rs.getInt(1);
			float avarageNumberOfLates = (float)mapOfBooksToInfo.get(key).getNumberOfLates()/numberOfReturn; 
			mapOfBooksToInfo.get(key).setAvarageNumberOfLates(avarageNumberOfLates);
		}
		reportGenereted.setBookToNumberAndDurationOfLates(mapOfBooksToInfo);
		return reportGenereted;
	}
}
