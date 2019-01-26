package gui;

import entities.Book;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
 
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class BookInformationController implements IClientUI
{
	private User userLoggedIn;
	@FXML
	private Label bookNameLabel;

	@FXML
	private Label wantedBookLabel;

	@FXML
	private TextArea descreptionPane;
	
	@FXML
	private TextArea moreInformationTextField;

	@FXML
	private ImageView wantedLogo;

	@FXML
	private JFXButton editDetailsBtn;

	@FXML
	private JFXButton deleteBookBtn;

	@FXML
	private JFXButton orderBookBtn;

	@FXML
	private Label authorLabel;

	@FXML
	private Label categoriesLabel;

	@FXML
	private Label catNumLabel;

	private Book bookToShow;

    @FXML
    private Label availableLabel;
    

    @FXML
    void btn_viewTableOfContent(MouseEvent event)
    {
		this.btn_viewTableOfContent(event);

    }

	public void setBookInformation(Book book)
	{
		descreptionPane.setText(book.getDescription());
		bookToShow = book;
		bookNameLabel.setText(book.getName());
		String authors = book.getAuthorNameList().toString().replace("[", "").replace("]", "");
		authorLabel.setText(authors);
		String categories = book.getCategories().toString().replace("[", "").replace("]", "");
		categoriesLabel.setText(categories);
		
		catNumLabel.setText(book.getCatalogNumber());
		if (book.getClassification().equals("wanted"))
		{
			wantedBookLabel.setVisible(true);
			wantedLogo.setVisible(true);
		} else
		{
			wantedBookLabel.setVisible(false);
			wantedLogo.setVisible(false); 
		}
		if(book.getMaxCopies()-book.getCurrentNumOfBorrows()>0)//book is available
		{
			availableLabel.setText("Available for borrow");
			availableLabel.setTextFill(Color.web("#12d318"));
			orderBookBtn.setDisable(true);
		}
		else
		{
			availableLabel.setText("Not available for borrow");
			availableLabel.setTextFill(Color.RED);
			orderBookBtn.setDisable(false);
		}
		
		moreInformationTextField.setText(""
				+ "Purchase Date: " +book.getPurchaseDate() 
				+ "\nCurrent Borrows: " +book.getCurrentNumOfBorrows() 
				+ "\nMax Copies: " +book.getMaxCopies() 

);
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		// TODO Auto-generated method stub

	}
	


	@Override
	public void setUserLogedIn(User userLoged)
	{
		this.userLoggedIn = userLoged;
		switchWindowPermission();
	}

	@Override
	public User getUserLogedIn()
	{
		return userLoggedIn;
	}
	
	private void switchWindowPermission()
	{
		if(userLoggedIn == null) 
		{
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
		}
		switch(userLoggedIn.getType())
		{
			case "subscriber":
				orderBookBtn.setVisible(true);
				deleteBookBtn.setVisible(false);
				editDetailsBtn.setVisible(false);
				break;
			case "library manager":
				orderBookBtn.setVisible(false);
				deleteBookBtn.setVisible(true);
				editDetailsBtn.setVisible(true);
				break;
			case "librarian":
				orderBookBtn.setVisible(false);
				deleteBookBtn.setVisible(true);
				editDetailsBtn.setVisible(true);
				break;
			case "guest":
				orderBookBtn.setVisible(false);
				deleteBookBtn.setVisible(false);
				editDetailsBtn.setVisible(false);
				break;
		}
		
	}
}
