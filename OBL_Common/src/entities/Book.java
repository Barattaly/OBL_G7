package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable
{
	private String catalogNumber;
	private String name;
	private String purchaseDate;
	private String classification;
	private String description;
	private int maxCopies;
	private String location;
	private int currentNumOfBorrows;
	private int currentNumOfOrders;
	private String tableOfContenPath;
	private String isArchived;
	private ArrayList<CopyOfBook> copies;
	private ArrayList<String> authorNameList;
	private ArrayList<String> categories;

	
	
	public Book(String catalogNumber, String name, String purchaseDate,
			String classification, String description, int maxCopies, String location,
	 int currentNumOfBorrows, int currentNumOfOrders, String tableOfContenPath, String isArchived)
	{
		this.catalogNumber =catalogNumber;
		this.name = name;
		this.purchaseDate= purchaseDate;
		this.classification = classification;
		this.description = description;
		this.maxCopies = maxCopies;
		this.location = location;
		this.currentNumOfBorrows = currentNumOfBorrows;
		this.currentNumOfOrders = currentNumOfOrders;
		this.tableOfContenPath = tableOfContenPath;
		this.isArchived = isArchived;

	}
	
	
	public Book(String catalogNumber, String name, String purchaseDate, String classification, String description,
			int maxCopies, int currentNumOfBorrows, int currentNumOfOrders, String tableOfContenPath, String isArchived,
			ArrayList<CopyOfBook> copies) 
	{
		this.catalogNumber = catalogNumber;
		this.name = name;
		this.purchaseDate = purchaseDate;
		this.classification = classification;
		this.description = description;
		this.maxCopies = maxCopies;
		this.currentNumOfBorrows = currentNumOfBorrows;
		this.currentNumOfOrders = currentNumOfOrders;
		this.tableOfContenPath = tableOfContenPath;
		this.isArchived = isArchived;
		this.copies = copies;
	}

	public Book(String catalogNumber)
	{
		this.catalogNumber = catalogNumber;
	}
	public String getCatalogNumber() 
	{
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) 
	{
		this.catalogNumber = catalogNumber;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getPurchaseDate() 
	{
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) 
	{
		this.purchaseDate = purchaseDate;
	}
	public String getClassification() 
	{
		return classification;
	}
	public void setClassification(String classification) 
	{
		this.classification = classification;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public int getMaxCopies() 
	{
		return maxCopies;
	}
	public void setMaxCopies(int maxCopies) 
	{
		this.maxCopies = maxCopies;
	}
	public int getCurrentNumOfBorrows() 
	{
		return currentNumOfBorrows;
	}
	public void setCurrentNumOfBorrows(int currentNumOfBorrows) 
	{
		this.currentNumOfBorrows = currentNumOfBorrows;
	}
	public int getCurrentNumOfOrders() 
	{
		return currentNumOfOrders;
	}
	public void setCurrentNumOfOrders(int currentNumOfOrders) 
	{
		this.currentNumOfOrders = currentNumOfOrders;
	}
	public String getTableOfContenPath() 
	{
		return tableOfContenPath;
	}
	public void setTableOfContenPath(String tableOfContenPath) 
	{
		this.tableOfContenPath = tableOfContenPath;
	}
	public String getIsArchived() 
	{
		return isArchived;
	}
	public void setIsArchived(String isArchived) 
	{
		this.isArchived = isArchived;
	}

	public ArrayList<CopyOfBook> getCopies() 
	{
		return copies;
	}

	public void setCopies(ArrayList<CopyOfBook> copies) 
	{
		this.copies = copies;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public ArrayList<String> getAuthorNameList()
	{
		return authorNameList;
	}

	public void setAuthorNameList(ArrayList<String> authorNameList)
	{
		this.authorNameList = authorNameList;
	}


	public ArrayList<String> getCategories()
	{
		return categories;
	}


	public void setCategories(ArrayList<String> categories)
	{
		this.categories = categories;
	}
	
	
}
