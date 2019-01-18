package entities;

public class CopiesQueries 
{
	public static String searchBookCopyId(Book bookToCheck)
	{
		if (bookToCheck == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.book_" + bookToCheck.getCatalogNumber()
						+ "_copies WHERE id = '" + bookToCheck.getCopies().get(0).getId()+ "'";
		return queryMsg;
	}
	public static String changeCopyStatus(Book bookToUpdate)
	{
		if (bookToUpdate == null)
			return null;
		String queryMsg = "UPDATE obl_db.book_" + bookToUpdate.getCatalogNumber() 
						+ "_copies SET status = 'unavailable' WHERE (id = '"
						+ bookToUpdate.getCopies().get(0).getId() + "');";
		return queryMsg;
	}
}
