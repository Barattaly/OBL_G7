package entities;

import java.sql.ResultSet;

public class SubscribersQueries
{
	public static String createSubscriber(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="INSERT INTO obl_db.subscribers "
		+ "(subscriberID, phoneNumber, emailAddress,status) VALUES"
		+ "('"+subscriber.getId() +"','"+ subscriber.getPhoneNumber()+"','"+subscriber.getEmail()+"','"
		+subscriber.getStatus()+"');";
		return queryMsg;
	}
	public static String searchSubscriberByID(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="SELECT * FROM obl_db.subscribers WHERE subscriberID ='" + subscriber.getId() + "'";
		return queryMsg;
	}
	
	public static String getSubscriberStatus(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="SELECT status FROM obl_db.subscribers WHERE subscriberID ='" +subscriber.getId() + "'";
		return queryMsg;
	}
	
	public static Subscriber createSubscriberFromRS(ResultSet rs)
	{
		Subscriber subscriberToCreate = null;
		try
		{
			rs.next();
			//Subscriber(String subscriberNum, String id, String phone, String email, String status)
			
			subscriberToCreate = new Subscriber(rs.getString(1), rs.getString(2), 
					rs.getString(3), rs.getString(4), rs.getString(5));

		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		return subscriberToCreate;
	}
	
	public static String getSubscriberFullInformationByID(String subscriberID)
	  {
	    if (subscriberID == null)
	      return null;
	    String queryMsg ="SELECT userName,password,id,firstName,lastName,phoneNumber,"
	    		+ "emailAddress,subscriberNumber,status "
	    		+ "FROM obl_db.users INNER JOIN obl_db.subscribers"
	        + " ON obl_db.subscribers.subscriberID = obl_db.users.id "
	        + "WHERE obl_db.users.id='"+subscriberID+"';";
	    return queryMsg;
	  }
	
	public static Subscriber createSubscriberFromFullInformationRS(ResultSet rs)
	  {
	    Subscriber subscriberToCreate = null;
	    try
	    {
	      rs.next();
	      /*Subscriber(String uName, String pass, String idNum, 
	       * String first, String last, String phone, String mail,
			String subsNumber,String status)*/
	      subscriberToCreate = new Subscriber(rs.getString(1),rs.getString(2),rs.getString(3),
	    		  rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),
	    		  rs.getString(9));

	    } catch (Exception e)
	    {
	      e.printStackTrace();
	    } 
	    return subscriberToCreate;
	  }
	
	public static String updateSubscriberInformation(Subscriber subscriberToUpdate)
	{
		
		 String queryMsg ="UPDATE obl_db.subscribers" 
			 		+ "\nSET phoneNumber='"+subscriberToUpdate.getPhoneNumber()+"'," +"emailAddress='"+subscriberToUpdate.getEmail()+"',status ='"+subscriberToUpdate.getStatus()+"'" 
			 		+ "\nWHERE subscriberID='"+subscriberToUpdate.getId()+"';";
		 		
		return queryMsg;
		
		
	}

	/*public static String searchSubscriberByID(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String queryMsg ="SELECT * FROM obl_db.subscribers WHERE subscriberID ='" +borrowToAdd.getSubscriberId() + "'";
		return queryMsg;
	}*/
	
	public static String getCurrentNumOfBorrows(Subscriber subscriberToUpdate)
	{
		if (subscriberToUpdate == null)
			return null;
		String queryMsg = "SELECT currentNumOfBorrows FROM obl_db.subscribers WHERE subscriberID = '" + subscriberToUpdate.getId() + "';";
		return queryMsg;
	}
	
	public static String getNumOfLateReturns(Subscriber subscriberToUpdate)
	{
		if (subscriberToUpdate == null)
			return null;
		String queryMsg = "SELECT count(id) FROM obl_db.borrows "
						+ "WHERE subscriberID = '" + subscriberToUpdate.getId() 
						+ "' AND isReturnedLate = 'yes';";
		return queryMsg;
	}
	
	public static String updateSubscriberStatus(Subscriber subscriberToUpdate)
	{
		if (subscriberToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.subscribers SET status= '" + subscriberToUpdate.getStatus()
						+ "' WHERE (subscriberID = '" + subscriberToUpdate.getId() + "');";
		return queryMsg;
	}

	public static String getSubscriberCurrentBorrowsTable(Subscriber subscriberToUpdate)
	{
		String queryMsg = "SELECT * FROM obl_db.borrows" 
						+ " WHERE subscriberID = '" + subscriberToUpdate.getId() + "' AND actualReturnDate is null;";
		return queryMsg;
	}
	
	public static String getSubscriberStatusByUserName(String userName)
	{
		if (userName == null)
			return null;
		String queryMsg = "SELECT status"
				+" FROM obl_db.users INNER JOIN obl_db.subscribers ON  obl_db.users.id = obl_db.subscribers.subscriberID"
				+" WHERE userName = '" + userName + "';";
		return queryMsg;
	}

	public static String getSubscriberIsGraduatedStatus(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="SELECT isGraduated FROM obl_db.subscribers WHERE subscriberID ='" +subscriber.getId() + "'";
		return queryMsg;
	}
	
	public static String updateSubscriberIsGraduatedStatus(Subscriber subscriberToUpdate)
	{
		if (subscriberToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.subscribers SET isGraduated= '" + subscriberToUpdate.getIsGraduatedStatus()
						+ "' WHERE (subscriberID = '" + subscriberToUpdate.getId() + "');";
		return queryMsg;
	}
}
