package entities;

public class CopiesQueries 
{
	public static String getCopyDetails(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = '" + bookToCheck.getCatalogNumber() 
						+ "' AND id = '" + bookToCheck.getCopies().get(0).getId() + "';";
		return queryMsg;
	}
	
	public static String getBookCopiesDetails(Book book)
	{
		if (book == null)
			return null;
		String queryMsg = "SELECT id, status FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = '" + book.getCatalogNumber() + "';";
		return queryMsg;
	}
	
	public static String getCopyStatus(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT status FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = '" + bookToCheck.getCatalogNumber() 
						+ "' AND id = '" + bookToCheck.getCopies().get(0).getId() + "';";
		return queryMsg;
	}
	
	public static String getBookMaxCopies(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT COUNT(id) FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = " + bookToCheck.getCatalogNumber() + ";";
		return queryMsg;
	}
	
	public static String getNumOfUnavailableCopies(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT COUNT(id) FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = " + bookToCheck.getCatalogNumber() 
						+ " AND status = 'unavailable';";
		return queryMsg;
	}
	
	public static String getNumOfAvailableCopies(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT COUNT(id) FROM obl_db.books_copies "
						+ "WHERE bookCatalogNumber = " + bookToCheck.getCatalogNumber() 
						+ " AND status = 'available';";
		return queryMsg;
	}
	
	public static String getNumOfLostCopies(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT COUNT(id) FROM obl_db.books_copies"
						+ "WHERE bookCatalogNumber = " + bookToCheck.getCatalogNumber() 
						+ "AND status = 'lost';";
		return queryMsg;
	}
	
	public static String updateCopyStatusToUnavailable(Book bookToUpdate)
	{
		if (bookToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.books_copies SET status = 'unavailable' WHERE id = '"
						+ bookToUpdate.getCopies().get(0).getId() + "';";
		return queryMsg;
	}
	
	public static String updateCopyStatusToAvailable(Book bookToUpdate)
	{
		if (bookToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.books_copies SET status = 'available' WHERE id = '"
						+ bookToUpdate.getCopies().get(0).getId() + "';";
		return queryMsg;
	}
}
