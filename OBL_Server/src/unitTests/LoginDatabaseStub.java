package unitTests;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import application.ISqlConnection;
import entities.Subscriber;
import entities.User;
import entitiesQueries.SubscribersQueries;
import entitiesQueries.UsersQueries;

public class LoginDatabaseStub implements ISqlConnection
{
	private User userToCheck = null;
	private Subscriber subscriberTocheck = null;
	
	public LoginDatabaseStub() 
	{
		subscriberTocheck = new Subscriber("","","","","","","","","");
		
	}
	@Override
	public int executeUpdate(String msg)
	{
		return 0;// Doesn't matter... we do not update any Database
	}

	@Override
	public ResultSet executeQuery(String query)
	{
		if(query.equals(UsersQueries.searchUserByUserNameAndPass(userToCheck)))
		{
			if(userToCheck.getUserName().equals("notExist"))return new ResultSetStub(0);
			return new ResultSetStub(1);// return result set with one row
		}
		
		if(query.contains(SubscribersQueries.getSubscriberStatusByUserName(subscriberTocheck.getUserName())))
		{
			if(subscriberTocheck==null) return new ResultSetStub(0);
			ResultSetStub rs = new ResultSetStub(1);
			rs.setSubscriber(subscriberTocheck);
			return rs;
		}
		
		
		return new ResultSetStub(0);

	}

	public void setUserToCheck(User userToCheck)
	{
		this.userToCheck = userToCheck;
	}
	public void setSubscriberToCheck(Subscriber subscriberTocheck)
	{
		this.subscriberTocheck = subscriberTocheck;
	}
}
