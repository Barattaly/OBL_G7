package gui;

import entities.Book;
import entities.BookOrder;
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

import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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

	private Book bookToShow;

	@FXML
	private TitledPane copiesTitlePane;

	@FXML
	private TextArea copiesTextArea;

	@FXML
	private Label availableLabel;

    @FXML
    private JFXButton viewTOC_btn;
    
	public void setBookInformation(Book book)
	{
		descreptionPane.setText(book.getDescription());
		bookToShow = book;
		bookNameTextArea.setText(book.getName());
		String authors = book.getAuthorNameList().toString().replace("[", "").replace("]", "");
		authorTextArea.setText(authors);
		String categories = book.getCategories().toString().replace("[", "").replace("]", "");
		categoriesTextArea.setText(categories);

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
		if (subscriberLoggedIn != null)
		{
			if (!subscriberLoggedIn.getStatus().equals("active"))
			{
				availableLabel.setText("Card status isn't active"); // book is available for order
				availableLabel.setTextFill(Color.RED);
				orderBookBtn.setDisable(true);
			} else
			{

				if (book.getCurrentNumOfBorrows() < book.getMaxCopies()) // book is available for borrow
				{
					availableLabel.setText("Available for borrow");
					availableLabel.setTextFill(Color.web("#12d318"));
					orderBookBtn.setDisable(true);
				} else if (book.getCurrentNumOfBorrows() == book.getMaxCopies()) // book is not available for borrow
				{
					availableLabel.setText("Not available for borrow"); // book is available for order
					availableLabel.setTextFill(Color.RED);
					// orderBookBtn.setDisable(true);

					if (book.getCurrentNumOfOrders() < book.getMaxCopies())
					{
						for (BookOrder order : book.getOrders())
						{
							// check if the subscriber already ordered this book
							if (order.getSubscriberId().equals(userLoggedIn.getId()))
							{
								isOrderExist = true;
							}
						}
						if (isOrderExist)
						{
							availableLabel.setText("Already ordered this book"); // book is available for order
							availableLabel.setTextFill(Color.RED);
							orderBookBtn.setDisable(true);
						}
					} else
					{
						orderBookBtn.setDisable(true);
					}
				}
			}
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
	    void moveToArchiveClick(ActionEvent event) {

		   String bookID= catNumTextField.getText();
		   
		   Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("OBL Log Out");
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
	    }
	   @FXML
	    void viewTableOfContentClick(ActionEvent event) {
		   Book bookToSend=new Book(catNumTextField.getText());
		   GuiManager.client.viewTableOfContent(bookToSend);
		   
	    }

	    }


