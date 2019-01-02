package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ActionHistory {
	
	public SimpleStringProperty borrowdate;
	public SimpleStringProperty returndate;
	public SimpleStringProperty librarian;
	public SimpleStringProperty bookname;
	public SimpleIntegerProperty copynumber;
	
	   public ActionHistory(String borrowdate, String returndate, String librarian, String bookname, int copynumber) 
	   {
			this.borrowdate = new SimpleStringProperty(borrowdate);
			this.returndate =new SimpleStringProperty(returndate);
			this.bookname= new SimpleStringProperty(bookname);
			this.librarian = new SimpleStringProperty(librarian);
			this.copynumber =new SimpleIntegerProperty(copynumber);
			
		}

	public String getBorrowdate() {
		return borrowdate.get();
	}

	public void setBorrowdate(String borrowdate) {
		this.borrowdate =new SimpleStringProperty(borrowdate);
	}

	public String getReturndate() {
		return returndate.get();
	}

	public void setReturndate(String returndate) {
		this.returndate =new SimpleStringProperty(returndate);
	}

	public String getLibrarian() {
		return librarian.get();
	}

	public void setLibrarian(String librarian) {
		this.librarian =new SimpleStringProperty(librarian);
	}

	public String getBookname() {
		return bookname.get();
	}

	public void setBookname(String bookname) {
		this.bookname =new SimpleStringProperty(bookname);
	}

	public int getCopynumber() {
		return copynumber.get();
	}

	public void setCopynumber(int copynumber) {
		this.copynumber =new SimpleIntegerProperty(copynumber);
	}
	   
	   
	

}
