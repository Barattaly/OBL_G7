package unitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.MySQLConnection;
import application.OBLServer;
import entities.DBMessage;
import entities.DBMessage.DBAction;
import entities.Subscriber;
import entities.User;
import entitiesQueries.UsersQueries;

public class LoginTest
{
	LoginDatabaseStub oblDB;// = new LoginDatabaseStub();

	@Before
	public void setUp()
	{
		oblDB = new LoginDatabaseStub();
	}
	
	@Test
	public void testLockedSubscriber()
	{
		//MAKE SURE THIS DATA IN DB IS CORRECT!
		//oblDB = new LoginDatabaseStub();
		Subscriber lockedSubscriber = new Subscriber("1");//set id 1
		lockedSubscriber.setUserName("Exist");
		lockedSubscriber.setPassword("AnyPass");
		lockedSubscriber.setStatus("locked");
		
		oblDB.setUserToCheck(lockedSubscriber);//for existing user
		oblDB.setSubscriberToCheck(lockedSubscriber);//for locked status subscriber
		
		DBMessage expecteds = new DBMessage(DBAction.CheckUser, "locked");
		DBMessage actuals = UsersQueries.checkIfUserExist(lockedSubscriber, oblDB);
		assertEquals(expecteds.Data, actuals.Data);
	}
	
	@Test
	public void testNotExistingUser()
	{
		User notExistingUser = new User("notExist","bla bla");

		//oblDB = new LoginDatabaseStub();
		oblDB.setUserToCheck(notExistingUser);//for DB return there is no user like this.

		DBMessage expecteds = new DBMessage(DBAction.CheckUser, null);
		DBMessage actuals = UsersQueries.checkIfUserExist(notExistingUser, oblDB);
		assertEquals(expecteds.Data, actuals.Data);
	}

}
