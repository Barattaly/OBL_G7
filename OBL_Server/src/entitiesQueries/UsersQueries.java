package entitiesQueries;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.MySQLConnection;
import application.iSqlConnection;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import entities.DBMessage.DBAction;
import ocsf.server.ConnectionToClient;
/**
 * This class hold all the queries for users
 * And the creation of users entities from ResaultSet
 *
 */
public class UsersQueries
{
	public static DBMessage checkIfUserExist(User userToCheck,iSqlConnection oblDB)
	{
		String query = UsersQueries.searchUserByUserNameAndPass(userToCheck);
		ResultSet rs = oblDB.executeQuery(query);
		DBMessage returnMsg;
		int rowsNumber = getRowCount(rs);
		//Check if the user exist in the DB
		if (rowsNumber == 1)
		{
			try
			{
				rs.next();
			} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
			// if exist - create it
			User user = UsersQueries.CreateUserFromRS(rs);
			if (isUserLocked(userToCheck, oblDB))// check if this is a subscriber and if he is locked
				return new DBMessage(DBAction.CheckUser, "locked");// This is the message for locked user.

			if (user.getLoginStatus().equals("on"))//check if he is logged already
			{
				user = new User(null, null);
				return new DBMessage(DBAction.CheckUser, user);// This is the message for logged already user.
			} else// if the user exist AND is not locked or logged in -> connect him! 
			{
				user.setLoginStatus("on");
				query = UsersQueries.updateUserloginStatus(user);
				oblDB.executeUpdate(query);
				query = UsersQueries.getMessagesForUser(user);
				List<String> messages = UsersQueries.createMessagesLisrFromRS(oblDB.executeQuery(query));
				user.setMessages(messages);
				returnMsg = new DBMessage(DBAction.CheckUser, user);// This is the message for valid user.
			}
			return returnMsg;
		} else
		{
			returnMsg = new DBMessage(DBAction.CheckUser, null);// This is the message for non-existing user.
			return returnMsg;
		}
	}
	
	private static boolean isUserLocked(User userToCheck,iSqlConnection oblDB)
	{
		String query = SubscribersQueries.getSubscriberStatusByUserName(userToCheck.getUserName());
		ResultSet rs2 = oblDB.executeQuery(query);
		int rowsNumber = getRowCount(rs2);
		if (rowsNumber == 1)
		{
			try
			{
				rs2.next();
				if (rs2.getString(1).equals("locked"))
				{
					return true;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public static String searchUserByUserName(User userToCheck)
	{
		if (userToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.users WHERE userName ='" + userToCheck.getUserName()+"'";
		return queryMsg;
	}

	public static String searchUserByUserNameAndPass(User userToCheck)
	{
		if (userToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.users WHERE userName ='" + userToCheck.getUserName()
				+ "' AND password ='" + userToCheck.getPassWord() + "'";
		return queryMsg;
	}
	public static String createUser(User userToAdd)
	{
		if (userToAdd == null)
			return null;
		String queryMsg ="INSERT INTO obl_db.users (`id`, `userName`, `password`, `firstName`, `lastName`,  `loginStatus`,`type`) "
				+ "VALUES ('"+userToAdd.getId()+"', '"+userToAdd.getUserName()+"', '"+userToAdd.getPassWord()
				+"', '"+userToAdd.getFirstName()+"', "+ "'"+userToAdd.getLastName()+"', 'off'"
						+ ",'"+userToAdd.getType()+"');";

		return queryMsg;
	}
	public static String updateUserloginStatus(User userToUpdate)
	{
		if (userToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.users SET loginStatus = '"+userToUpdate.getLoginStatus()+"' WHERE id = '"+userToUpdate.getId()+"'";
		return queryMsg;
	}

	public static User CreateUserFromRS(ResultSet rs)
	{
		User userToCreate = null;
		try
		{
			userToCreate = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7));

		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		return userToCreate;
	}

	public static String searchUserByID(User userToCheck)
	{
		if (userToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.users WHERE id ='" + userToCheck.getId()+"'";
		return queryMsg;
	}
	
	public static String updateUserInformation(Subscriber subscriberToUpdate)
	{
		
		 String queryMsg ="UPDATE obl_db.users" +
				 "\nSET firstName='"+subscriberToUpdate.getFirstName()+"'," +  "lastName='"+subscriberToUpdate.getLastName()+"'"
				 +"\nWHERE id='"+subscriberToUpdate.getId() +"';";
		return queryMsg;
		
		
	}

	public static String getMessagesForUser(User user)
	{
		String queryMsg ="SELECT * FROM obl_db.messages WHERE (recipientUserType ='"+user.getType()+"' AND recipientUserId IS NULL ) "
				+ "OR recipientUserId ='"+user.getId()+"'";
		return queryMsg;
	}

	public static List<String> createMessagesLisrFromRS(ResultSet rs)
	{
		List<String> messages = new ArrayList<String>();
		try
		{
		while(rs.next())
		{
			String msg = rs.getString(2);
			msg +=rs.getString(3);
			messages.add(msg);
		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages = new ArrayList<String>();
		}
		return messages;
	}
	
	private static int getRowCount(ResultSet resultSet)
	{
		if (resultSet == null)
		{
			return 0;
		}
		try
		{
			resultSet.last();
			return resultSet.getRow();
		} catch (SQLException exp)
		{
			exp.printStackTrace();
		} finally
		{
			try
			{
				resultSet.beforeFirst();
			} catch (SQLException exp)
			{
				exp.printStackTrace();
			}
		}
		return 0;
	}
	
	public static String removeUser(User userToRemove)
	{
		if (userToRemove == null)
			return null;
		String queryMsg = "DELETE FROM obl_db.Users WHERE id='"+userToRemove.getId()+"'";
		return queryMsg;
	}
}
