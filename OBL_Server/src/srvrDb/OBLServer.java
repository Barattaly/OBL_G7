package srvrDb;

import java.sql.ResultSet;
import java.sql.SQLException;
import entities.UsersQueries;
import entities.DBMessage;
import entities.DBMessage.DBAction;
import entities.User;
import javafx.scene.control.TextArea;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class OBLServer extends AbstractServer
{

	public TextArea logREF = null;
	private MySQLConnection oblDB;

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public OBLServer(int port, String dbName, String dbPassword, String userName)
	{
		super(port);
		try
		{
			oblDB = new MySQLConnection(dbName, dbPassword, userName);
		} catch (Exception e)
		{
		}
	}

	public OBLServer(int port, TextArea log)
	{
		super(port);
		logREF = log;
	}

	public void connectToDB(String dbName, String dbPassword, String userName) throws SQLException
	{
		oblDB = new MySQLConnection(dbName, dbPassword, userName);
	}
	/*
	 * if (dbMessage.toString().contains("SELECT *")) { try { ResultSet res =
	 * oblDB.SelectAllFrom((String) dbMessage); ResultSetMetaData rsmd =
	 * res.getMetaData(); int columnsNumber = rsmd.getColumnCount(); String
	 * messageToSend = "SELECTALL-"; while (res.next()) { for (int i = 1; i <=
	 * columnsNumber; i++) { String columnValue = res.getString(i); messageToSend =
	 * messageToSend + columnValue + ","; } messageToSend = messageToSend + "-"; }
	 * client.sendToClient(messageToSend); } catch (Exception e) {
	 * System.out.println(e.getMessage()); } } else if
	 * (dbMessage.toString().contains("UPDATE")) { try {
	 * oblDB.updateStudent(dbMessage.toString()); } catch (Exception e) {
	 * System.out.println(e.getMessage()); } }
	 */

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param dbMessage The message received from the client.
	 * @param client    The connection from which the message originated.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromClient(Object obj, ConnectionToClient client)
	{
		if (!(obj instanceof DBMessage))
			return;
		DBMessage dbMessage = (DBMessage) obj;
		if (dbMessage.Action == DBAction.isDBRuning)
		{
			try
			{
				client.sendToClient(new DBMessage(DBAction.isDBRuning, isDBRunning()));
				return;

			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		logREF.setText("Message received from client: " + client + System.lineSeparator() + logREF.getText());
		if(!isDBRunning()) return;
		try
		{
			switch (dbMessage.Action)
			{

			case CheckUser:
			{
				User userToCheck = (User) dbMessage.Data;
				String query = UsersQueries.searchUserByUserNamePass(userToCheck);
				ResultSet rs = oblDB.executeOBLQuery(query);
				int rowsNumber = getRowCount(rs);
				if (rowsNumber == 1)
				{
					rs.next();
					User user = UsersQueries.CreateUserFromRS(rs);
					DBMessage returnMsg;
					if (user.getLoginStatus().equals("on"))
					{
						user = new User(null, null);
						returnMsg = new DBMessage(DBAction.RETCheckUser, user);
					} else
					{
						user.setLoginStatus("on");
						query = UsersQueries.updateUserloginStatus(user);
						oblDB.executeUpdate(query);
						returnMsg = new DBMessage(DBAction.RETCheckUser, user);
					}
					client.sendToClient(returnMsg);
					return;
				} else
				{
					DBMessage returnMsg = new DBMessage(DBAction.RETCheckUser, null);
					client.sendToClient(returnMsg);
				}

				break;
			}
			case UpdateUserLogout:
			{
				User userToUpdate = (User) dbMessage.Data;
				if (userToUpdate == null)
					return;
				userToUpdate.setLoginStatus("off");
				String query = UsersQueries.updateUserloginStatus(userToUpdate);
				oblDB.executeUpdate(query);
			}
			default:
				break;
			}

		} catch (Exception e)
		{
			e.printStackTrace();

		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println("Server has stopped listening for connections.");
	}

	public boolean isDBRunning()
	{
		if (oblDB == null)
			return false;
		return oblDB.IsConnectionSucceeded;
	}

	private int getRowCount(ResultSet resultSet)
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

}