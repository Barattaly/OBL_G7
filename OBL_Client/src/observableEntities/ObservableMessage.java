package observableEntities;

import javafx.beans.property.SimpleStringProperty;

public class ObservableMessage
{
	public SimpleStringProperty dateSent;
	public SimpleStringProperty msgContent;
	
	public ObservableMessage(String dateSent,String msgContent)
	{
		this.dateSent = new SimpleStringProperty(dateSent);
		this.msgContent = new SimpleStringProperty(msgContent);
	}

	public String getDateSent()
	{
		return dateSent.get();
	}

	public void setDateSent(String dateSent)
	{
		this.dateSent = new SimpleStringProperty(dateSent);
	}

	public String getMsgContent()
	{
		return msgContent.get();
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = new SimpleStringProperty(msgContent);
	}

}
