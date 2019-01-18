package entities;

public class Subscriber extends User
{
	private String subscriberNumber;
	private String phoneNumber;
	private String email;
	private String status;
	private int currentNumOfBorrows = 0;
	private int currentNumOfOrders = 0;

	public Subscriber(String subscriberNum, String id, String phone, String email, String status,int numOfBorrow, int numofOrders)
	{
		super();
		  subscriberNumber = subscriberNum;
		  this.idNumber = id;
		  phoneNumber = phone;
		  this.email = email;
		  this.status = status;
		  currentNumOfBorrows = numOfBorrow;
		  currentNumOfOrders = numofOrders;
	}

	public Subscriber(String uName, String pass, String idNum, String first, String last, String phone, String mail,
			String subsNumber)
	{
		super(uName, pass, idNum, first, last);
		subscriberNumber = subsNumber;
		status = "active";
		email = mail;
		phoneNumber = phone;
	}

	public Subscriber(String uName, String pass, String idNum, String first, String last)
	{
		super(uName, pass, idNum, first, last);
		status = "active";
		email = null;
		phoneNumber = null;
	}

	public Subscriber(String subscriberNumber, String phoneNumber, String email, String status, int currentNumOfBorrows,
			int currentNumOfOrders, String userName, String idNumber, String firstName, String lastName) // tal cons
	{
		super(userName, idNumber, firstName, lastName);
		this.subscriberNumber = subscriberNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
		this.currentNumOfBorrows = currentNumOfBorrows;
		this.currentNumOfOrders = currentNumOfOrders;
	}
	
	public Subscriber(String id)
	{
		super();
		  this.idNumber = id;
	}
	
	public String getSubscriberNumber()
	{
		return subscriberNumber;
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

	public void setSubscriberNumber(String subscriberId)
	{
		this.subscriberNumber = subscriberId;
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

	public void FillInformationFromUser(User userToCheck)
	{
		this.userName = userToCheck.userName;
		this.password = userToCheck.password;
		this.idNumber = userToCheck.idNumber;
		this.firstName = userToCheck.firstName;
		this.lastName = userToCheck.lastName;
		this.loginStatus = userToCheck.loginStatus;
		this.type = userToCheck.type;
		}
}
