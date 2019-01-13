package entities;


public class Subscriber extends User
{
	private int subscriberId;
	private String phoneNumber;
	private String email;
	private String status;
	private int currentNumOfBorrows = 0;
	private int currentNumOfOrders = 0;

	public Subscriber(String uName, String pass, String idNum, String first, String last,String phone, String mail)
	{
		super(uName, pass,idNum,first,last);
		status = "active";
		email =mail;
		phoneNumber = phone;
	}
	public Subscriber(String uName, String pass, String idNum, String first, String last)
	{
		super(uName, pass,idNum,first,last);
		status = "active";
		email =null;
		phoneNumber = null;
	}
	public int getSubscriberId()
	{
		return subscriberId;
	}


	public String getPhoneNumber()
	{
		return phoneNumber;
	}


	public String getEmail()
	{
		return email;
	}


	public String getStatus()
	{
		return status;
	}


	public int getCurrentNumOfBorrows()
	{
		return currentNumOfBorrows;
	}


	public int getCurrentNumOfOrders()
	{
		return currentNumOfOrders;
	}


	public void setSubscriberId(int subscriberId)
	{
		this.subscriberId = subscriberId;
	}


	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}


	public void setStatus(String status)
	{
		this.status = status;
	}


	public void setCurrentNumOfBorrows(int currentNumOfBorrows)
	{
		this.currentNumOfBorrows = currentNumOfBorrows;
	}


	public void setCurrentNumOfOrders(int currentNumOfOrders)
	{
		this.currentNumOfOrders = currentNumOfOrders;
	}
}
