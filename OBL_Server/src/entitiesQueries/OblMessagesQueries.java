package entitiesQueries;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import entities.OblMessage;
/**
 * This class hold all the queries for messages
 * And the creation of messages entities from ResaultSet
 *
 */
public class OblMessagesQueries 
{
	public static String sendMessageToLibrarians(OblMessage messageToSend)
	{
		if (messageToSend == null)	
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.messages (messageSentDate, messageContent, recipientUserType) "
						+ "VALUES ('" + currentDateTimeString + "', '" + messageToSend.getMessageContent() 
						+ "', '" + messageToSend.getRecipientUserType() + "');";
		return queryMsg;
	}
		
	public static String sendMessageToLibraryManager(OblMessage messageToSend)
	{
		if (messageToSend == null)	
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.messages (messageSentDate, messageContent, recipientUserType) "
						+ "VALUES ('" + currentDateTimeString + "', '" + messageToSend.getMessageContent() 
						+ "', '" + messageToSend.getRecipientUserType() + "');";
		return queryMsg;
	}
	
	public static String sendMessageToSubscriber(OblMessage messageToSend)
	{
		if (messageToSend == null)	
			return null;
		String currentDateTimeString = getcurrentDateTimesString();

		String queryMsg = "INSERT INTO obl_db.messages (messageSentDate, messageContent, recipientUserType, recipientUserId) "
						+ "VALUES ('" + currentDateTimeString + "', '" + messageToSend.getMessageContent() 
						+ "', '" + messageToSend.getRecipientUserType() + "'," + messageToSend.getRecipientUserId() + ");";
		return queryMsg;
	}

	public static String getcurrentDateTimesString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(calendar.getTime());
		return string;
	}
}
