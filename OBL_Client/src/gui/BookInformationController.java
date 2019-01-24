package gui;

import entities.Book;
import entities.BookOrder;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BookInformationController implements IClientUI
{
	private User userLoggedIn;
	
	private Subscriber subscriberLoggedIn;
	@FXML
	private Label bookNameLabel;

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
	private Label authorLabel;

	@FXML
	private Label categoriesLabel;

	@FXML
	private Label catNumLabel;

	private Book bookToShow;

    @FXML
    private Label availableLabel;

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
		if(!subscriberLoggedIn.getStatus().equals("active"))
		{
			availableLabel.setText("Card status isn't active"); // book is available for order
			availableLabel.setTextFill(Color.RED);
			orderBookBtn.setDisable(true);
		}
		else
		{

			if (book.getCurrentNumOfBorrows() < book.getMaxCopies()) // book is available for borrow
			{
				availableLabel.setText("Available for borrow");
				availableLabel.setTextFill(Color.web("#12d318"));
				orderBookBtn.setDisable(true);
			} 
			else if (book.getCurrentNumOfBorrows() == book.getMaxCopies()) // book is not available for borrow
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
				} 
				else 
				{
					orderBookBtn.setDisable(true);
				}
			}
		}
		
		/*moreInformationTextField.setText(""
				+ "Purchase Date: " +book.getPurchaseDate() 
				+ "\nCurrent Borrows: " +book.getCurrentNumOfBorrows() 
				+ "\nMax Copies: " +book.getMaxCopies() 

				);*/
	}

	
	@FXML
    void btn_orderBookClick(ActionEvent event) 
	{
		BookOrder newOrder = new BookOrder(userLoggedIn.getId(), catNumLabel.getText());
		
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


	public void setSubscriber(Subscriber subscriberLogged)
	{
		subscriberLoggedIn = subscriberLogged;
		
	}
}
