package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book 
{
	
	public SimpleStringProperty name;
	public SimpleIntegerProperty catalogynumber;
	public SimpleStringProperty author;
	public SimpleIntegerProperty numberofcopys;
	public SimpleStringProperty subject;
	public SimpleStringProperty location;
	   
	   public Book(String name, int catalogynumber, String author, int numberofcopys, String subject, String location) 
	   {
			this.name = new SimpleStringProperty(name);
			this.catalogynumber =new SimpleIntegerProperty(catalogynumber);
			this.author = new SimpleStringProperty(author);
			this.numberofcopys =new SimpleIntegerProperty(numberofcopys);
			this.subject =new SimpleStringProperty(subject);
			this.location =new SimpleStringProperty(location);
		}
	   

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name =new SimpleStringProperty(name);
	}

	public int getCatalogynumber() {
		return catalogynumber.get();
	}

	public void setCatalogynumber(int catalogynumber) {
		this.catalogynumber =new SimpleIntegerProperty(catalogynumber);
	}

	public String getAuthor() {
		return author.get();
	}

	public void setAuthor(String author) {
		this.author =new SimpleStringProperty(author);
	}

	public int getNumberofcopys() {
		return numberofcopys.get();
	}

	public void setNumberofcopys(int numberofcopys) {
		this.numberofcopys =new SimpleIntegerProperty(numberofcopys);
	}

	public String getSubject() {
		return subject.get();
	}

	public void setSubject(String subject) {
		this.subject =new SimpleStringProperty(subject);
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location =new SimpleStringProperty(location);
	}
	   
}
