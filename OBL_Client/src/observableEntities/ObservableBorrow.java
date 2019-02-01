package observableEntities;

import javafx.beans.property.SimpleStringProperty;

public class ObservableBorrow
{

	public SimpleStringProperty borrowId;
	public SimpleStringProperty borrowDate;
	public SimpleStringProperty returnDate;
	public SimpleStringProperty copyId;
	public SimpleStringProperty catalogNumber;
	public SimpleStringProperty subscriberId;

	public ObservableBorrow(String borrowNumber, String borrowDate,
			String returnDate, String copyId, String catalogNumber, String subscriberId)
	{
		this.borrowId = new SimpleStringProperty(borrowNumber);
		this.borrowDate = new SimpleStringProperty(borrowDate);
		this.returnDate = new SimpleStringProperty(returnDate);
		this.copyId = new SimpleStringProperty(copyId);
		this.catalogNumber = new SimpleStringProperty(catalogNumber);
		this.subscriberId = new SimpleStringProperty(subscriberId);
	}

	public String getBorrowId()
	{
		return borrowId.get();
	}

	public void setBorrowId(String borrowNumber)
	{
		this.borrowId = new SimpleStringProperty(borrowNumber);
	}

	public String getBorrowDate()
	{
		return borrowDate.get();
	}

	public void setBorrowDate(String borrowDate)
	{
		this.borrowDate = new SimpleStringProperty(borrowDate);
	}

	public String getReturnDate()
	{
		return returnDate.get();
	}

	public void setReturnDate(String returnDate)
	{
		this.returnDate = new SimpleStringProperty(returnDate);
	}

	public String getCopyId()
	{
		return copyId.get();
	}

	public void setCopyId(String copyId)
	{
		this.copyId = new SimpleStringProperty(copyId);
	}

	public String getCatalogNumber()
	{
		return catalogNumber.get();
	}

	public void setCatalogNumber(String catalogNumber)
	{
		this.catalogNumber = new SimpleStringProperty(catalogNumber);
	}

	public String getSubscriberId()
	{
		return subscriberId.get();
	}

	public void setSubscriberId(String subscriberId)
	{
		this.subscriberId = new SimpleStringProperty(subscriberId);
	}
}
