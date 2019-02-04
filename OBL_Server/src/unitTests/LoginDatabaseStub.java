package unitTests;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import application.iSqlConnection;
import entities.Subscriber;
import entities.User;
import entitiesQueries.SubscribersQueries;
import entitiesQueries.UsersQueries;

public class LoginDatabaseStub implements iSqlConnection
{
	private User userToCheck = null;
	private Subscriber subscriberTocheck = null;
	
	public LoginDatabaseStub() {}
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
			if(userToCheck.getUserName().equals("notExist"))return new MockResultSet(0);
			return new MockResultSet(1);// return result set with one row
		}
		if(query.contains(SubscribersQueries.getSubscriberStatusByUserName(subscriberTocheck.getUserName())))
		{
			if(subscriberTocheck==null) return new MockResultSet(0);
			MockResultSet rs = new MockResultSet(1);
			rs.setSubscriber(subscriberTocheck);
			return rs;
		}
		
		
		return new MockResultSet(0);

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
