package gui;

import entities.Book;
import entities.BookOrder;
import entities.BorrowACopyOfBook;
import entities.CopyOfBook;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.lang.invoke.StringConcatFactory;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;



import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.mail.imap.CopyUID;

import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BookInformationController implements IClientUI 
{
	private User userLoggedIn;

	private Subscriber subscriberLoggedIn;

	@FXML
	private JFXTextArea bookNameTextArea;

	@FXML
	private Label wantedBookLabel;

	@FXML
	private TextArea descreptionPane;

	@FXML
	private ImageView wantedLogo;

	@FXML
	private JFXButton editDetailsBtn;

	@FXML
	private JFXButton deleteBookBtn;

	@FXML
	private JFXButton orderBookBtn;

	@FXML
	private JFXTextArea authorTextArea;

	@FXML
	private JFXTextArea categoriesTextArea;

	@FXML
	private JFXTextField catNumTextField;

	@FXML
	private JFXTextField publicationYearTextField;

	@FXML
	private JFXTextField editionNumTextField;

	@FXML
	private JFXTextField locationTextField;
	
	@FXML
	private JFXTextField returnDateTextField;

	private Book bookToShow;

	@FXML
	private TitledPane copiesTitlePane;

	@FXML
	private TextArea copiesTextArea;

	@FXML
	private Label availableLabel;
	
	@FXML
	private Label returnDateLabel;

	@FXML
	private Label locationLabel;
	
	 @FXML
	    private JFXCheckBox wantedBookCheckBox;

	  @FXML
	  	private JFXButton saveChanges_btn;

	  @FXML
	   private JFXButton viewTOC_btn;
    
	  @FXML
	  private JFXButton cancel_btn;
	  
	public void setBookInformation(Book book)
	{
		bookToShow = book;
		descreptionPane.setText(book.getDescription());
		bookNameTextArea.setText(book.getName());
		String authors = book.getAuthorNameList().toString().replace("[", "").replace("]", "");
		authorTextArea.setText(authors);
		String categories = book.getCategories().toString().replace("[", "").replace("]", "");
		categoriesTextArea.setText(categories);
		GuiManager.preventLettersTypeInTextField(editionNumTextField);
		GuiManager.preventLettersTypeInTextField(publicationYearTextField);
		catNumTextField.setText(book.getCatalogNumber());
		boolean isOrderExist = false;
		if (book.getClassification().equals("wanted"))
		{
			wantedBookLabel.setVisible(true);
			wantedLogo.setVisible(true);
		} else
		{
			wantedBookLabel.setVisible(false);
			wantedLogo.setVisible(false);
		}
		if (book.getCurrentNumOfBorrows() < book.getMaxCopies()) // book is available for borrow
		{
			availableLabel.setText("Available for borrow");
			availableLabel.setTextFill(Color.web("#12d318"));
			orderBookBtn.setDisable(true);
			returnDateLabel.setVisible(false);
			returnDateTextField.setVisible(false);
		} 
		else if (book.getCurrentNumOfBorrows() == book.getMaxCopies()) // book is not available for borrow
		{
			availableLabel.setText("Not available for borrow"); // means that the book is available for order
			availableLabel.setTextFill(Color.RED);
			locationLabel.setVisible(false);
			locationTextField.setVisible(false);
			if (book.getCurrentNumOfOrders() == book.getMaxCopies()) // if orders queue is full
			{
				if (subscriberLoggedIn != null) 
				{
					availableLabel.setText("Orders queue is full");
				} 
				else 
				{
					availableLabel.setText("Not available for order");
				}
				availableLabel.setTextFill(Color.RED);
				orderBookBtn.setDisable(true);
			}
			if (subscriberLoggedIn != null) 
			{
				/* if the subscriber is currently borrow one of the copies of this book,
				 * than we need to prevent the option to order this book */
				for (BorrowACopyOfBook borrow : book.getBorrows()) 
				{
					if (borrow.getSubscriberId().equals(subscriberLoggedIn.getId()))
					{
						availableLabel.setText("You cant order a book that\nyou are currently borrowing");
						availableLabel.setTextFill(Color.RED);
						orderBookBtn.setDisable(true);
					}
				}
				/* if the subscriber is currently order the book,
				 * than we need to show the location in orders queue*/
				int i = 1, positionInQueue = 0;
				for (BookOrder order : book.getOrders()) 
				{
					// check if the subscriber already ordered this book
					if (order.getSubscriberId().equals(subscriberLoggedIn.getId())) 
					{
						isOrderExist = true;
						positionInQueue = i;
					}
					i++;
				}
				if (isOrderExist) 
				{
					availableLabel.setText("Your position in orders queue: " + positionInQueue);
					availableLabel.setTextFill(Color.web("#12d318"));
					orderBookBtn.setDisable(true);
				}
			}
			// check what is the closest expected return date
			String closestReturnDate = book.getBorrows().get(1).getExpectedReturnDate();
			for (BorrowACopyOfBook borrow : book.getBorrows()) 
			{
				// if the current expected return date is before the previous date
				if (LocalDate.parse(borrow.getExpectedReturnDate()).isBefore((LocalDate.parse(closestReturnDate))))
				{
					closestReturnDate = borrow.getExpectedReturnDate();
				}
			}
			returnDateTextField.setText(closestReturnDate);
		}
		if (subscriberLoggedIn != null && !subscriberLoggedIn.getStatus().equals("active")) 
		{
			availableLabel.setText("Card status is not active");
			availableLabel.setTextFill(Color.RED);
			orderBookBtn.setDisable(true);
		}
		publicationYearTextField.setText(book.getPublicationYear());
		editionNumTextField.setText(book.getEditionNumber());
		locationTextField.setText(book.getLocation());
		if (book.getCopies() != null)
		{
			String copies = "";
			for (CopyOfBook copy : book.getCopies())
			{
				copies = copies + System.lineSeparator() + copy.getId() + "              " + copy.getStatus();
			}
			copiesTextArea.setText("The book's copies are:\n" + "Copy ID   " + "Status" + copies);
		}
	}

	@FXML
	void btn_orderBookClick(ActionEvent event)
	{
		BookOrder newOrder = new BookOrder(userLoggedIn.getId(), catNumTextField.getText());

		GuiManager.client.createNewOrder(newOrder);
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case CreateNewOrder:
		{
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Order executed Successfully!");
					orderBookBtn.setDisable(true);
				});
			}
			break;
		}
		default:
			break;
		}
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
		if (userLoggedIn == null)
		{
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
		}
		switch (userLoggedIn.getType())
		{
		case "subscriber":
			orderBookBtn.setVisible(true);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
			copiesTitlePane.setVisible(false);
			break;
		case "library manager":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(true);
			editDetailsBtn.setVisible(true);
			copiesTitlePane.setVisible(true);
			break;
		case "librarian":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(true);
			editDetailsBtn.setVisible(true);
			copiesTitlePane.setVisible(true);
			break;
		case "guest":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
			copiesTitlePane.setVisible(false);
			break;
		}

	}

	public void setSubscriber(Subscriber subscriberLogged)
	{
		subscriberLoggedIn = subscriberLogged;
		if (!subscriberLoggedIn.getStatus().equals("active"))
		{
			orderBookBtn.setDisable(true);
		} else
			orderBookBtn.setDisable(false);
	}
	@FXML
	void moveToArchiveClick(ActionEvent event) 
	{
		String bookID= catNumTextField.getText();   
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Warning");
		alert.setHeaderText("Are you sure you want to delete this book?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK)
		{
			GuiManager.client.moveBookToArchive(bookID);
			GuiManager.ShowMessagePopup("The book with catalog number :" + bookID + "  moved to the archive" );
				 
		} else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();
		}
		Stage stage = (Stage) deleteBookBtn.getScene().getWindow();//we want to close the stage where the delete button is
		    // do what you have to do
		 stage.close();
	    }
	   @FXML
	    void viewTableOfContentClick(ActionEvent event) 
	   {
		   Book bookToSend=new Book(catNumTextField.getText());
		   GuiManager.client.viewTableOfContent(bookToSend);
		   
	   }

	    @FXML
	    void editDetailsClick(ActionEvent event) {
	    	bookNameTextArea.setEditable(true);
	    	authorTextArea.setEditable(true);
	    	categoriesTextArea.setEditable(true);
	    	publicationYearTextField.setEditable(true);
	    	editionNumTextField.setEditable(true);
	    	locationTextField.setEditable(true);
	    	descreptionPane.setEditable(true);
	    	wantedBookLabel.setVisible(false);
	    	wantedLogo.setVisible(false);
	    	wantedBookCheckBox.setVisible(true);
	    	saveChanges_btn.setVisible(true);
	    	cancel_btn.setVisible(true);
	    	editDetailsBtn.setDisable(true);
	    	if(bookToShow.getClassification().equals("wanted"))
	    	{
	    		wantedBookCheckBox.setSelected(true);
	    	}
	    	
	    }
	    @FXML
	    void saveChangesClick(ActionEvent event) //need to add function to remove ' 
	    {
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Are you sure you want to change those detailes?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() == ButtonType.OK)
			{
				//ADD TEST TO PUBLICATION YEAR - 1901 to 2155.
				cancel_btn.setVisible(false);
				bookNameTextArea.setEditable(false);
				authorTextArea.setEditable(false);
				categoriesTextArea.setEditable(false);
				publicationYearTextField.setEditable(false);
				editionNumTextField.setEditable(false);
				locationTextField.setEditable(false);
				descreptionPane.setEditable(false);
				copiesTextArea.setEditable(false);
				wantedBookCheckBox.setVisible(true);
				ArrayList <String> authorsList=new ArrayList<String>(Arrays.asList(authorTextArea.getText().split(",")));
				ArrayList <String> categoriesList=new ArrayList<String>(Arrays.asList(categoriesTextArea.getText().split(",")));
				String bookName=removeTag(bookNameTextArea.getText());
				String publicationYear=removeTag(publicationYearTextField.getText());
				String location=removeTag(locationTextField.getText());
				String description=removeTag(descreptionPane.getText());
				String bookClassification;
				if(wantedBookCheckBox.isSelected())
					bookClassification = "wanted";
				else
					bookClassification = "ordinary";
				if(bookName.isEmpty()||publicationYear.isEmpty()||location.isEmpty()||description.isEmpty())
					GuiManager.ShowErrorPopup("please fill all fields!");
				else {
	    		 
					Book newBook = new Book(catNumTextField.getText(),bookName,authorsList,	categoriesList,publicationYear, editionNumTextField.getText(),
							location,description,bookClassification);	
	    		 GuiManager.client.editBookDetails(newBook);
	    		 GuiManager.ShowMessagePopup("This book has been edited successfully!");
	    		 saveChanges_btn.setVisible(false);
	    		 editDetailsBtn.setDisable(false);
	    	 }
			}
			else if (option.get() == ButtonType.CANCEL)
			{
				alert.close();
			}
	    }

	    @FXML
	    void cancelClick(ActionEvent event) {
	    	saveChanges_btn.setVisible(false);
	    	cancel_btn.setVisible(false);
	    	editDetailsBtn.setDisable(false);
	    	wantedBookCheckBox.setVisible(false);
	    	setBookInformation(bookToShow);
	    }
	    /*
	     * this function get a string and remove all tag's that can break the program
	     */
	    public static String removeTag(String str)
	    {
	    	String s= str.replace("'", " ");
	    	return s;
	    }
	 	    
}
