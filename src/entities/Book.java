package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book 
{
	
	public SimpleStringProperty name;
	public SimpleStringProperty author;
	public SimpleIntegerProperty catalognumber;
	public SimpleStringProperty returndate;
	
	public SimpleStringProperty location;
	   
	   public Book(String name, String author, int catalognumber, String location , String returndate) 
	   {
			this.name = new SimpleStringProperty(name);
			this.catalognumber =new SimpleIntegerProperty(catalognumber);
			this.author = new SimpleStringProperty(author);
			this.location =new SimpleStringProperty(location);
			this.returndate =new SimpleStringProperty(returndate);
		}
	   

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name =new SimpleStringProperty(name);
	}



	public String getAuthor() {
		return author.get();
	}

	public void setAuthor(String author) {
		this.author =new SimpleStringProperty(author);
	}
	
	public int getCatalognumber() {
		return catalognumber.get();
	}

	public void setCatalognumber(int catalognumber) {
		this.catalognumber =new SimpleIntegerProperty(catalognumber);
	}



	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location =new SimpleStringProperty(location);
	}
	
	public String getReturndate() {
		return returndate.get();
	}

	public void setReturndate(String returndate) {
		this.returndate =new SimpleStringProperty(returndate);
	}

	   
}
