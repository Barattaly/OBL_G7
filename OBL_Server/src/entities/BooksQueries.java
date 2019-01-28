package entities;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import entities.Book;

public class BooksQueries
{
public static String SelectAllBooksEachRowForNewAuthor()
	{
		String queryMsg = "SELECT  catalogNumber,name,purchaseDate,"
				+ "classification,description,location,"
				+ "editionNumber,publicationYear,tableOfContentPath,"
				+ "archived,authorName FROM obl_db.books " + "INNER JOIN obl_db.books_authors ON "
				+ "obl_db.books.catalogNumber = obl_db.books_authors.bookCatalogNumber;";
		return queryMsg;
	}
	
	public static String getCategoriesForBookId(String catalogNumber)
	{
		String queryMsg = "SELECT categoryName FROM obl_db.books " 
				+"INNER JOIN obl_db.books_categories ON "
				+"obl_db.books.catalogNumber = obl_db.books_categories.bookCatalogNumber "
               + "WHERE catalogNumber = '"+catalogNumber+"'";
		return queryMsg;
	}
	
	public static Map<Integer,Book> createBookListFromRS(ResultSet rs)
	{
		Map<Integer,Book> Books = new HashMap<Integer,Book>();
		try
		{
			while (rs.next())
			{
				/*Book(String catalogNumber, String name, String purchaseDate,
				String classification, String description, String location,
				String editionNumber,String publicationYear,
				String tableOfContenPath, String isArchived)*/
				
				Book temp = new Book(rs.getString(1),rs.getString(2), rs.getString(3), 
						rs.getString(4),rs.getString(5), 
						rs.getString(6), rs.getString(7), 
						rs.getString(8), rs.getString(9), 
						rs.getString(10));
				String author = rs.getString(11);// 11 is the author name
				temp.setAuthorNameList(new ArrayList<>());
				int tempCatNum = Integer.parseInt(temp.getCatalogNumber());
				if(Books.containsKey(tempCatNum))
				{
					Books.get(tempCatNum).getAuthorNameList().add(author);
				}
				else//New book!
				{
					temp.getAuthorNameList().add(author);
					Books.put(Integer.parseInt(temp.getCatalogNumber()),temp);
				}
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
		String queryMsg = "SELECT classification FROM obl_db.books WHERE books.catalogNumber = '"
						+ bookToCheck.getCatalogNumber()+ "';";
		return queryMsg;
	}
	
	public static String getCurrentNumOfBorrows(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT count(id) FROM obl_db.borrows" 
						+ " WHERE bookCatalogNumber = '" + bookToCheck.getCatalogNumber()
						+ "' AND actualReturnDate is null;";
		return queryMsg;
	}
	
	public static String getCurrentNumOfOrders(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT count(id) FROM obl_db.orders" 
						+ " WHERE bookCatalogNumber = '" + bookToCheck.getCatalogNumber()
						+ "' AND status = 'active';";
		return queryMsg;
	}
	
	public static String searchBookByCatalogNumber(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.books WHERE books.catalogNumber = '" + bookToCheck.getCatalogNumber()+ "';";
		return queryMsg;
	}
	
	public static String getArchiveStatus(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT archived FROM obl_db.books "
						+ "WHERE books.catalogNumber = '" + bookToCheck.getCatalogNumber()+ "';";
		return queryMsg;
	}
	
	public static String getBookName(Book book)
	{
		if (book == null)
			return null;
		String queryMsg = "SELECT name FROM obl_db.books "
						+ "WHERE books.catalogNumber = '" + book.getCatalogNumber()+ "';";
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

	public static String updateBookArciveStatus(String catalogNumber) //shiranush new query
	{
		String queryMsg = "UPDATE obl_db.books SET archived = 'yes' WHERE books.catalogNumber = '"
						+ catalogNumber + "';";
		return queryMsg;
	}
	
	public static String AddBook(Book book) 
	{
		String queryMsg = "INSERT INTO obl_db.books (`name`, `purchaseDate`, `classification`, `description`, `location`,  `editionNumber`,`publicationYear`,`tableOfContentPath`,`archived`)"
				+" VALUES ('"+book.getName()+"', '"+book.getPurchaseDate()+"', '"+book.getClassification()
						+"', '" +book.getDescription()+ "', '"+book.getLocation()+"','"+book.getEditionNumber()
								+ "','"+book.getPublicationYear()+ "','"+book.getTableOfContenPath()+ "','yes'" +");";
				
		return queryMsg;
	}
	
}
