package entities;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Book;

public class BooksQueries
{
	public static String SelectAllBooksEachRowForNewAuthor()
	{
		String queryMsg = "SELECT * FROM obl_db.books " + "INNER JOIN obl_db.books_authors ON "
				+ "obl_db.books.catalogNumber = obl_db.books_authors.bookCatalogNumber;";
		return queryMsg;
	}
	
	public static String getCatagoriesForBookId(String catalogNumber)
	{
		String queryMsg = "SELECT categoryName FROM obl_db.books " 
				+"INNER JOIN obl_db.books_categories ON "
				+"obl_db.books.catalogNumber = obl_db.books_categories.bookCatalogNumber "
               + "WHERE catalogNumber = '"+catalogNumber+"'";
		return queryMsg;
	}
	
	public static Map<Integer,Book> CreateBookListFromRS(ResultSet rs)
	{
		Map<Integer,Book> Books = new HashMap<Integer,Book>();
		try
		{
			while (rs.next())
			{
				// for each author we have a different row!
				Book temp = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getString(10),
						rs.getString(11));
				String author = rs.getString(13);// 12 is the id again
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


}
