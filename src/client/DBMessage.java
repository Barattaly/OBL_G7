package client;
import java.io.Serializable;
import java.sql.ResultSet;

public class DBMessage implements Serializable
{
	public DBAction Action;
	public Object Data;
	
	public DBMessage(DBAction action,Object data)
	{
		this.Action = action;
		this.Data = data;
	}
	
	public static enum DBAction
	{
		AddSubscriber,GetUser,REMOVE,UPDATE,GET;
	}
}
