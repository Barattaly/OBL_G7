package entities;
import java.io.Serializable;

@SuppressWarnings("serial")
public class DBMessage implements Serializable
{
	public DBAction Action;
	public Object Data;
	
	public DBMessage(DBAction action,Object data)
	{
		this.Action = action;
		this.Data = data;
	}
	
	public enum DBAction
	{
		BorrowCopyOfBook,UpdateUserLogout,CheckUser,isDBRuning,CreateSubscriber, GetAllBooksList,ViewSubscriberCard;
	}
}
