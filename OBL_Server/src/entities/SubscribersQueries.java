package entities;

import java.sql.ResultSet;

public class SubscribersQueries
{
	public static String createSubscriber(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="INSERT INTO obl_db.subscribers "
		+ "(subscriberID, phoneNumber, emailAddress,status,currentNumOfBorrows,currentNumOfOrders) VALUES"
		+ "('"+subscriber.getId() +"','"+ subscriber.getPhoneNumber()+"','"+subscriber.getEmail()+"','"
		+subscriber.getStatus()+"',"+ subscriber.getCurrentNumOfBorrows()+","+subscriber.getCurrentNumOfOrders()+");";
		return queryMsg;
	}
	public static String searchSubscriberByID(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="SELECT * FROM obl_db.subscribers WHERE subscriberID ='" +subscriber.getId() + "'";
		return queryMsg;
	}
	
	public static Subscriber CreateSubscriberFromRS(ResultSet rs)
	{
		Subscriber subscriberToCreate = null;
		try
		{
			rs.next();
			subscriberToCreate = new Subscriber(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getInt(6), rs.getInt(7));

		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		return subscriberToCreate;
	}
	
	public static String searchSubscriberByID(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String queryMsg ="SELECT * FROM obl_db.subscribers WHERE subscriberID ='" +borrowToAdd.getSubscriberId() + "'";
		return queryMsg;
	}
}
