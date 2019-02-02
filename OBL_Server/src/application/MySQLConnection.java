package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entities.User;
/**
 * This class is responsible for the DB connection with the server.
 *
 */
public class MySQLConnection
{
	private Connection conn;
	public boolean IsConnectionSucceeded = false;

	public MySQLConnection(String dbName, String dbPassword, String userName)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, userName, dbPassword);
		IsConnectionSucceeded = true;
	}

	public int executeUpdate(String msg)
	{
		try
		{
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(msg);
		} catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	public ResultSet executeQuery(String query)
	{
		try
		{
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			return rs;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * public ResultSet CheckIfUserExist(User userToCheck) { try { Statement query =
	 * conn.createStatement(); String queryMsg =
	 * "SELECT * FROM obl_db.users WHERE userName ='"+userToCheck.getUserName()
	 * +"' AND password ='"+userToCheck.getPassWord() +"'"; ResultSet rs =
	 * query.executeQuery(queryMsg);//"SELECT * FROM Students;"); return rs; } catch
	 * (SQLException e) { e.printStackTrace(); return null; } }
	 */

	/*
	 * public ResultSet SelectAllFrom(String msg) { try { Statement query =
	 * conn.createStatement(); ResultSet rs =
	 * query.executeQuery(msg);//"SELECT * FROM Students;"); return rs; } catch
	 * (SQLException e) { e.printStackTrace(); return null; } }
	 * 
	 * public int updateStudent(String msg) { try { Statement stmt =
	 * conn.createStatement(); return stmt.executeUpdate(msg); } catch(Exception e)
	 * { e.printStackTrace(); return 0; } }
	 */

	/*
	 * public static void printCourses(Connection con) { Statement stmt; try { stmt
	 * = con.createStatement(); ResultSet rs =
	 * stmt.executeQuery("SELECT * FROM courses;"); while(rs.next()) { // Print out
	 * the values System.out.println(rs.getString(1)+"  " +rs.getString(2)); }
	 * rs.close();
	 * //stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309"); }
	 * catch (SQLException e) {e.printStackTrace();} }
	 * 
	 * 
	 * public static void createTableCourses(Connection con1) { Statement stmt; try
	 * { stmt = con1.createStatement(); stmt.
	 * executeUpdate("create table courses(num int, userName VARCHAR(40), semestr VARCHAR(10));"
	 * ); stmt.
	 * executeUpdate("load data local infile \"courses.txt\" into table courses");
	 * 
	 * } catch (SQLException e) { e.printStackTrace();}
	 * 
	 * }
	 */

}
