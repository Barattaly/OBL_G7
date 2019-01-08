package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book 
{
	
	public SimpleStringProperty name;
	public SimpleIntegerProperty catalognumber;
	public SimpleStringProperty author;
	public SimpleIntegerProperty numberofcopys;
	public SimpleStringProperty categories;
	public SimpleStringProperty location;
	   
	   public Book(String name, int catalognumber, String author, int numberofcopys, String categories, String location) 
	   {
			this.name = new SimpleStringProperty(name);
			this.catalognumber =new SimpleIntegerProperty(catalognumber);
			this.author = new SimpleStringProperty(author);
			this.numberofcopys =new SimpleIntegerProperty(numberofcopys);
			this.categories =new SimpleStringProperty(categories);
			this.location =new SimpleStringProperty(location);
		}
	   

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name =new SimpleStringProperty(name);
	}

	public int getCatalognumber() {
		return catalognumber.get();
	}

	public void setCatalognumber(int catalognumber) {
		this.catalognumber =new SimpleIntegerProperty(catalognumber);
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

	public String getCategories() {
		return categories.get();
	}

	public void setCategories(String categories) {
		this.categories =new SimpleStringProperty(categories);
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(String location) {
		this.location =new SimpleStringProperty(location);
	}
	   
}
