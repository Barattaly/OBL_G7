package entities;

import java.sql.ResultSet;

public class UsersQueries
{

	public static String CheckUser(User userToCheck)
	{
		if (userToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.users WHERE userName ='" + userToCheck.getUserName()
				+ "' AND password ='" + userToCheck.getPassWord() + "'";
		return queryMsg;
	}
	public static String updateUserLogedin(User userToUpdate)
	{
		if (userToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.users SET loginStatus = '"+userToUpdate.getLoginStatus()+"' WHERE id = '"+userToUpdate.getId()+"'";
		return queryMsg;
	}

	public static User CreateUser(ResultSet rs)
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

}
