package entities;

public class CopiesQueries 
{
	public static String searchBookCopyId(BorrowACopyOfBook borrowToAdd)
	{
		if (borrowToAdd == null)
			return null;
		String queryMsg = "SELECT * FROM obl_db.book_" + borrowToAdd.getBookCatalogNumber() + "_copies WHERE id = '" + borrowToAdd.getCopyId()+ "'";
		return queryMsg;
	}
}
