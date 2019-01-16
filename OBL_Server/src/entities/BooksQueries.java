package entities;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import entities.Book;

public class BooksQueries
{
	public static String SelectAllBooks()
	{
		String queryMsg = "SELECT * FROM obl_db.books "
				+ "INNER JOIN obl_db.books_authors ON "
				+ "obl_db.books.catalogNumber = obl_db.books_authors.bookCatalogNumber;";
				return queryMsg;
	}

	public static List<Book> CreateBookListFromRS(ResultSet rs)
	{
		List<Book> Books = new ArrayList<Book>();
		try
		{
			while(rs.next())
			{
				Book temp = new Book(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getString(10),rs.getString(11));
				String authors = rs.getString(13);//12 is the id again
				//need to make the authors a list and add to book
				Books.add(temp);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		return Books;
	}
	
	public static String getClassificationOfBook(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT classification FROM obl_db.books WHERE books.catalogNumber = '" + bookToCheck.getCatalogNumber()+ "';";
		System.out.println(queryMsg);
		return queryMsg;
	}
	
	/*public static String getNowAsString(String format)
	{
		Calendar calStartDate = new GregorianCalendar();
		calStartDate.setTime(new Date());
		Date startDate = calStartDate.getTime();
		DateFormat dateFormat = new SimpleDateFormat(format);
		String dateTimeString = dateFormat.format(startDate);
		return dateTimeString;
	}*/
	
	
}
