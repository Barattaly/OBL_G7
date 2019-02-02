package observableEntities;

import javafx.beans.property.SimpleStringProperty;

/**
 * An activity log of subscriber entity to show in javafx TableView
 *
 */

public class ObservableActivityLog
{

	public SimpleStringProperty activity;
	public SimpleStringProperty bookname;
	public SimpleStringProperty date;
	public SimpleStringProperty comments;

	public ObservableActivityLog(String activity, String bookname, String date, String comments)
	{
		this.activity = new SimpleStringProperty(activity);
		this.bookname = new SimpleStringProperty(bookname);
		this.date = new SimpleStringProperty(date);
		this.comments = new SimpleStringProperty(comments);

	}

	public String getActivity()
	{
		return activity.get();
	}

	public void setActivity(String activity)
	{
		this.activity = new SimpleStringProperty(activity);
	}

	public String getBookname()
	{
		return bookname.get();
	}

	public void setBookname(String bookname)
	{
		this.bookname = new SimpleStringProperty(bookname);
	}

	public String getDate()
	{
		return date.get();
	}

	public void setDate(String date)
	{
		this.date = new SimpleStringProperty(date);
	}

	public String getComments()
	{
		return comments.get();
	}

	public void setComments(String comments)
	{
		this.comments = new SimpleStringProperty(comments);
	}
}
