package entities;

public class SubscribersQueries
{
	public static String createSubscriber(Subscriber subscriber)
	{
		if (subscriber == null)
			return null;
		String queryMsg ="INSERT INTO obl_db.subscribers "
		+ "(subscriberID, phoneNumber, emailAddress,status,currentNumOfBorrows,currentNumOfOrders) VALUES"
		+ "('"+subscriber.getId() +"','"+ subscriber.getPhoneNumber()+"','"+subscriber.getEmail()+"','"
		+subscriber.getStatus()+"',"+ subscriber.getCurrentNumOfBorrows()+","+subscriber.getCurrentNumOfOrders()+");";


		return queryMsg;
	}
}
