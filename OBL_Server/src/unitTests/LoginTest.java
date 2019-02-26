package unitTests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.MySQLConnection;
import entities.DBMessage;
import entities.DBMessage.DBAction;
import entities.Subscriber;
import entities.User;
import entitiesQueries.SubscribersQueries;
import entitiesQueries.UsersQueries;

/**
 * The way this tests work (login):
 * 1. setUp - add all the tested users before every test method to DB 
 * 			(all the combination - librarian, manager and subscribers: active, frozen, locked)
 * 2. tests - check what happen when - valid user logged in, not valid user logged in, locked subscriber logged in, every user try to login twice.
 * 3. tearUp - after each test function done - delete all the tested users from the DB.
 * 
 */
public class LoginTest
{
	MySQLConnection oblDB;
	String loggedInStatus = "on";
	// new User(idNum, name, pass, first, last, status, userType)
	User librarian = new User("1", "librarian", "123", "example", "librarian", "off", "librarian");
	User libraryManager = new User("2", "libraryManager", "123", "example", "libraryManager", "off", "library manager");
	Subscriber activeSubscriber = new Subscriber("3", "example", "activeSubscriber", "", "", "active");
	Subscriber lockedSubscriber = new Subscriber("4", "example", "lockedSubscriber", "", "", "locked");
	Subscriber frozenSubscriber = new Subscriber("5", "example", "frozenSubscriber", "", "", "frozen");

	@Before
	public void setUp()
	{
		try
		{
			oblDB = new MySQLConnection("obl_db", "Group7", "root");
			// Add the tested users
			// Librarian and library manager:
			oblDB.executeUpdate(UsersQueries.createUser(librarian));
			oblDB.executeUpdate(UsersQueries.createUser(libraryManager));
			// Subscribers update data:
			// Active:
			activeSubscriber.setUserName("activeSubscriber");
			activeSubscriber.setPassword("123");
			activeSubscriber.setLoginStatus("off");
			activeSubscriber.setType("subscriber");
			// Locked:
			lockedSubscriber.setUserName("lockedSubscriber");
			lockedSubscriber.setPassword("123");
			lockedSubscriber.setLoginStatus("off");
			lockedSubscriber.setType("subscriber");
			// Frozen:
			frozenSubscriber.setUserName("frozenSubscriber");
			frozenSubscriber.setPassword("123");
			frozenSubscriber.setLoginStatus("off");
			frozenSubscriber.setType("subscriber");
			// Subscribers add to users and subscribers table:
			oblDB.executeUpdate(UsersQueries.createUser(activeSubscriber));// add to Users table
			oblDB.executeUpdate(SubscribersQueries.createSubscriber(activeSubscriber));// add to Subscribers table
			oblDB.executeUpdate(UsersQueries.createUser(lockedSubscriber));// add to Users table
			oblDB.executeUpdate(SubscribersQueries.createSubscriber(lockedSubscriber));// add to Subscribers table
			oblDB.executeUpdate(UsersQueries.createUser(frozenSubscriber));// add to Users table
			oblDB.executeUpdate(SubscribersQueries.createSubscriber(frozenSubscriber));// add to Subscribers table

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

	}

	@After
	public void teardown()
	{
		// Get the tested subscribers from the subscriber table and delete them:
		// Active:
		ResultSet subscriberResultSet = oblDB.executeQuery(SubscribersQueries.searchSubscriberByID(activeSubscriber));
		Subscriber testedSubscriber = SubscribersQueries.createSubscriberFromRS(subscriberResultSet);
		oblDB.executeUpdate(SubscribersQueries.removeSubscriber(testedSubscriber));
		// Frozen:
		subscriberResultSet = oblDB.executeQuery(SubscribersQueries.searchSubscriberByID(frozenSubscriber));
		testedSubscriber = SubscribersQueries.createSubscriberFromRS(subscriberResultSet);
		oblDB.executeUpdate(SubscribersQueries.removeSubscriber(testedSubscriber));
		// Locked:
		subscriberResultSet = oblDB.executeQuery(SubscribersQueries.searchSubscriberByID(lockedSubscriber));
		testedSubscriber = SubscribersQueries.createSubscriberFromRS(subscriberResultSet);
		oblDB.executeUpdate(SubscribersQueries.removeSubscriber(testedSubscriber));
		// Remove the tested users:
		oblDB.executeUpdate(UsersQueries.removeUser(librarian));
		oblDB.executeUpdate(UsersQueries.removeUser(libraryManager));
		oblDB.executeUpdate(UsersQueries.removeUser(activeSubscriber));
		oblDB.executeUpdate(UsersQueries.removeUser(frozenSubscriber));
		oblDB.executeUpdate(UsersQueries.removeUser(lockedSubscriber));
		
	}

	@Test
	public void validUser()
	{
		// Librarian:
		DBMessage expecteds = new DBMessage(DBAction.CheckUser, librarian);
		DBMessage actuals = UsersQueries.checkIfUserExist(librarian, oblDB);// The login method
		assertEquals(((User) expecteds.Data).getUserName(), ((User) actuals.Data).getUserName());
		assertEquals(loggedInStatus, ((User) actuals.Data).getLoginStatus());// Make sure he is logged in

		// Library Manager:
		expecteds = new DBMessage(DBAction.CheckUser, libraryManager);
		actuals = UsersQueries.checkIfUserExist(libraryManager, oblDB);// The login method
		assertEquals(((User) expecteds.Data).getUserName(), ((User) actuals.Data).getUserName());
		assertEquals(loggedInStatus, ((User) actuals.Data).getLoginStatus());// Make sure he is logged in

		// Subscribers:

		// Active:
		expecteds = new DBMessage(DBAction.CheckUser, activeSubscriber);
		actuals = UsersQueries.checkIfUserExist(activeSubscriber, oblDB);// The login method
		assertEquals(((User) expecteds.Data).getUserName(), ((User) actuals.Data).getUserName());
		assertEquals(loggedInStatus, ((User) actuals.Data).getLoginStatus());// Make sure he is logged in

		// Frozen:
		expecteds = new DBMessage(DBAction.CheckUser, frozenSubscriber);
		actuals = UsersQueries.checkIfUserExist(frozenSubscriber, oblDB);// The login method
		assertEquals(((User) expecteds.Data).getUserName(), ((User) actuals.Data).getUserName());
		assertEquals(loggedInStatus, ((User) actuals.Data).getLoginStatus());// Make sure he is logged in
	}
	
	@Test
	public void lockedSubscriberAttemptToLogin()
	{
		// Try to login with the locked subscriber
		DBMessage expecteds = new DBMessage(DBAction.CheckUser, "locked");// The expected response from server when subscriber is locked
		DBMessage actuals = UsersQueries.checkIfUserExist(lockedSubscriber, oblDB);// The login method
		assertEquals(expecteds.Data, actuals.Data);
	}
	
	@Test
	public void loggedInUser()
	{
		// In this function we check what happen if any user try to login twice.
		// For each type that can perform login we: 
		// 1. Make a first login.
		// 2. Try to login again.
		
		// Librarian:
		User userLoggedIn = librarian;
		// First Login:
		DBMessage afterLoggin = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals("on", ((User) afterLoggin.Data).getLoginStatus());// Check he is logged in
		// Try to log again:
		DBMessage expected = new DBMessage(DBAction.CheckUser, new User(null, null));
		DBMessage actuals = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals(expected.Data, expected.Data);

		// Library Manager:
		userLoggedIn = libraryManager;
		// First Login:
		afterLoggin = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals("on", ((User) afterLoggin.Data).getLoginStatus());// Check he is logged in
		// Try to log again:
		expected = new DBMessage(DBAction.CheckUser, new User(null, null));
		actuals = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals(expected.Data, expected.Data);

		// Subscribers:

		// active:
		userLoggedIn = activeSubscriber;
		// First Login:
		afterLoggin = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals("on", ((User) afterLoggin.Data).getLoginStatus());// Check he is logged in
		// Try to log again:
		expected = new DBMessage(DBAction.CheckUser, new User(null, null));
		actuals = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals(expected.Data, expected.Data);

		// Frozen:
		userLoggedIn = frozenSubscriber;
		// First Login:
		afterLoggin = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals("on", ((User) afterLoggin.Data).getLoginStatus());// Check he is logged in
		// Try to log again:
		expected = new DBMessage(DBAction.CheckUser, new User(null, null));
		actuals = UsersQueries.checkIfUserExist(userLoggedIn, oblDB);// The login method
		assertEquals(expected.Data, expected.Data);
	}

	@Test
	public void notValidUser()
	{
		// This is the expected message for non existing user
		DBMessage expecteds = new DBMessage(DBAction.CheckUser, null);

		//Try to login with a non existing User Name:
		User notExistingUser = new User("bla bla", "123");
		DBMessage actuals = UsersQueries.checkIfUserExist(notExistingUser, oblDB);
		assertEquals(expecteds.Data, actuals.Data);
		
		//Try to login with wrong password:
		User wrongPasswordUser = new User(librarian.getUserName(), "330");//The real password is 123
		actuals = UsersQueries.checkIfUserExist(wrongPasswordUser, oblDB);
		assertEquals(expecteds.Data, actuals.Data);
	}
}