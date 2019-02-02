package observableEntities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * A book entity to show in javafx TableView
 *
 */
public class ObservableBook
{

	public SimpleStringProperty name;
	public SimpleStringProperty author;
	public SimpleIntegerProperty catalognumber;
	public SimpleStringProperty location;
	public SimpleStringProperty catagories;
	public SimpleBooleanProperty isAvailableToBorrow;


	public ObservableBook(String name, String author, int catalognumber, String location, String catagories,boolean available)//, String returndate)
	{
		this.name = new SimpleStringProperty(name);
		this.catalognumber = new SimpleIntegerProperty(catalognumber);
		this.author = new SimpleStringProperty(author);
		this.location = new SimpleStringProperty(location);
		this.catagories = new SimpleStringProperty(catagories);
		this.isAvailableToBorrow = new SimpleBooleanProperty(available);
	}

	public String getName()
	{
		return name.get();
	}

	public void setName(String name)
	{
		this.name = new SimpleStringProperty(name);
	}

	public String getAuthor()
	{
		return author.get();
	}

	public void setAuthor(String author)
	{
		this.author = new SimpleStringProperty(author);
	}

	public int getCatalognumber()
	{
		return catalognumber.get();
	}

	public void setCatalognumber(int catalognumber)
	{
		this.catalognumber = new SimpleIntegerProperty(catalognumber);
	}

	public String getLocation()
	{
		return location.get();
	}

	public void setLocation(String location)
	{
		this.location = new SimpleStringProperty(location);
	}

	public String getCatagories()
	{
		return catagories.get();
	}

	public void setCatagories(String catagories)
	{
		this.catagories = new SimpleStringProperty(catagories);
	}
	
	public boolean getIsAvailableToBorrow()
	{
		return isAvailableToBorrow.get();
	}

	public void setIsAvailableToBorrow(boolean isAvailableToBorrow)
	{
		this.isAvailableToBorrow = new SimpleBooleanProperty(isAvailableToBorrow);
	}

}
