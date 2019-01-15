package entities;

import java.io.Serializable;

public class CopyOfBook implements Serializable
{
	private String id;
	private String status;
	private String location;
	private String editionNumber;
	private String publicationYear;
	
	public CopyOfBook(String id, String status, String location, String editionNumber, String publicationYear) {
		super();
		this.id = id;
		this.status = status;
		this.location = location;
		this.editionNumber = editionNumber;
		this.publicationYear = publicationYear;
	}

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getLocation() 
	{
		return location;
	}

	public void setLocation(String location) 
	{
		this.location = location;
	}

	public String getEditionNumber() 
	{
		return editionNumber;
	}

	public void setEditionNumber(String editionNumber) 
	{
		this.editionNumber = editionNumber;
	}

	public String getPublicationYear() 
	{
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) 
	{
		this.publicationYear = publicationYear;
	}
	
	
}
