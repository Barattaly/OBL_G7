package gui;

import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import entities.Book;
import entities.BorrowACopyOfBook;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import entities.DBMessage.DBAction;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibrarianScreenController implements Initializable, IClientUI
{
	private User userLogedIn;
	@FXML
	private Label userWelcomLabel;
	@FXML
	private Label userNameLabel;
	@FXML
	private Pane pane_home, pane_createNewSubscriberCard,pane_searchSubscriberCard;
	@FXML
	private AnchorPane pane_searchBook;
	@FXML
	private ImageView btn_home, btn_createNewSubscriberCard, btn_books, btn_searchSubscriberCard;

	@FXML
	private JFXTextField userNameTextfield;

	@FXML
	private JFXTextField idNumberTextfield;

	@FXML
	private JFXTextField firstNameTextfield;

	@FXML
	private JFXTextField lastNameTextfield;

	@FXML
	private JFXTextField phoneNumberTextfield;

	@FXML
	private JFXTextField emailTextfield;

	@FXML
	private JFXPasswordField passwordTextfield;
	
    @FXML
    private JFXTextField txt_subscriberID;

	@FXML
	private Label warningLabel;
    @FXML
    private JFXButton btn_viewSubscriberCard;
    
    private ViewSubscriberCardController controller;  //check
    
    public static IClientUI CurrentGuiController;//check
    
	private SearchBookController searchBookWindowController = null;

	private Stage borrowDialog = null;
	private Stage returnDialog = null;
	private JFXDatePicker returnDate = null;

	@FXML
	void btn_homeDisplay(MouseEvent event)
	{
		pane_home.setVisible(true);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		btn_home.setOpacity(0.5);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);

	}

	@FXML
	protected void btn_createNewSubscriberCardDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(true);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(0.5);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);
	}

	@FXML
	void btn_booksDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(true);
		pane_searchSubscriberCard.setVisible(false);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(0.5);
		btn_searchSubscriberCard.setOpacity(1);
	}

	@FXML
	void btn_searchSubscriberCardDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(true);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(0.5);
		txt_subscriberID.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(txt_subscriberID);
		GuiManager.limitTextFieldMaxCharacters(txt_subscriberID, 9);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		pane_home.setVisible(true);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		btn_home.setOpacity(0.5);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);

	}

	@FXML
	void logOutDisplay(MouseEvent event)
	{ // logout

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("OBL Log Out");
		alert.setHeaderText("Are you sure you want to log out?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK)
		{
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			GuiManager.SwitchScene(SCREENS.login);
		}

		else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();
		}

	}

	@FXML
	void btn_createSubscriberCardDisplay(ActionEvent event)
	{
		warningLabel.setText("");
		if (idNumberTextfield.getText().isEmpty() || userNameTextfield.getText().isEmpty()
				|| firstNameTextfield.getText().isEmpty() || lastNameTextfield.getText().isEmpty()
				|| passwordTextfield.getText().isEmpty())
		{
			warningLabel.setText("Please fill all the requierd field.");
			return;
		}
		Subscriber newSubscriberToCreate = createSubscriberFromTextFields();
		
		GuiManager.client.CreateSubscriber(newSubscriberToCreate);
	}

@FXML
	void btn_borrowClick(ActionEvent event)
	{
		borrowDialog = new Stage();
		borrowDialog.initModality(Modality.APPLICATION_MODAL);
		borrowDialog.setHeight(400);
		borrowDialog.setWidth(400);
		borrowDialog.setTitle("Borrow a copy of a Book");
		borrowDialog.getIcons().add(new Image("/resources/Braude.png"));
		Label headline = new Label("Enter borrow details");
		headline.setStyle("-fx-text-fill: #a0a2ab");
		headline.setFont(new Font(16));
		VBox borrowDialogVbox = new VBox(15);
		Label bookCatalogNumberLab = new Label("Book catalog number: ");
		bookCatalogNumberLab.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCatalogNumber = new JFXTextField();
		bookCatalogNumber.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCatalogNumber);
		Label bookCopyLab = new Label("Book copy ID: ");
		bookCopyLab.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCopyId = new JFXTextField();
		bookCopyId.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCopyId);
		Label subscriberIdLab = new Label("Subscriber ID: ");
		subscriberIdLab.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField subscriberID = new JFXTextField();
		subscriberID.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(subscriberID);
		GuiManager.limitTextFieldMaxCharacters(subscriberID, 9);
		Label returnDateLab = new Label("Return date: ");
		returnDateLab.setStyle("-fx-text-fill: #a0a2ab");
		returnDate = new JFXDatePicker();
		returnDate.setStyle("-fx-text-inner-color: #a0a2ab");
		returnDate.setPromptText("dd.mm.yyyy or dd.mm.yyyy");
		returnDate.setDayCellFactory(picker -> new DateCell() 
		{
	        public void updateItem(LocalDate date, boolean empty) 
	        {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();
	            setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.compareTo(today) < 0 || date.compareTo(today.plusDays(13)) > 0);
	        }
		});
		
		GridPane grid = new GridPane();
		grid.add(bookCatalogNumberLab, 1, 1);
		grid.add(bookCatalogNumber, 2, 1);
		grid.add(bookCopyLab, 1, 2);
		grid.add(bookCopyId, 2, 2);
		grid.add(subscriberIdLab, 1, 3);
		grid.add(subscriberID, 2, 3);
		grid.add(returnDateLab, 1, 4);
		grid.add(returnDate, 2, 4);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		borrowDialogVbox.setAlignment(Pos.CENTER);
		Label warningMessageLab = new Label("");
		warningMessageLab.setStyle("-fx-text-fill: RED; -fx-font-weight: BOLD");
		JFXButton borrowBtn = new JFXButton("Borrow");
		borrowBtn.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
		borrowDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;"); 
		
		borrowBtn.setOnMouseClicked(new EventHandler<Event>() 
		{
			@Override
			public void handle(Event e) 
			{
				String warningMessage = "";
				warningMessageLab.setText(warningMessage);
				if (bookCatalogNumber.getText().isEmpty() && bookCopyId.getText().isEmpty()
						&& subscriberID.getText().isEmpty() && (returnDate.getValue() == null)) 
				{
					warningMessage = "Please fill all of the fields";
				} 
				else if (bookCatalogNumber.getText().isEmpty()) 
				{
					warningMessage = "Please enter book catalog number";
				} 
				else if (bookCopyId.getText().isEmpty()) 
				{
					warningMessage = "Please enter book copy id";
				} 
				else if (subscriberID.getText().isEmpty()) 
				{
					warningMessage = "Please enter subscriber id";
				} 
				else if (returnDate.getValue() == null) 
				{
					warningMessage = "Please enter return date";
				}
				else 
				{
					try 
					{
						String retDate = returnDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						
						BorrowACopyOfBook newBorrow = new BorrowACopyOfBook(subscriberID.getText(), retDate,
								bookCatalogNumber.getText(), bookCopyId.getText());
						GuiManager.client.createNewBorrow(newBorrow);
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}
				}
				if (!warningMessage.isEmpty())
					warningMessageLab.setText(warningMessage);
			}
		});
		borrowDialogVbox.getChildren().addAll(headline, grid, warningMessageLab, borrowBtn);
		Scene borrowDialogScene = new Scene(borrowDialogVbox, 300, 200);
		borrowDialog.setScene(borrowDialogScene);
		borrowDialog.showAndWait();
	}

	@FXML
	void btn_ReturnClick(ActionEvent event) 
	{
		returnDialog = new Stage();
		returnDialog.initModality(Modality.APPLICATION_MODAL);
		returnDialog.setTitle("Return a copy of a Book");
		returnDialog.getIcons().add(new Image("/resources/Braude.png"));
		returnDialog.setHeight(250);
		returnDialog.setWidth(400);
		Label headline = new Label("Enter book catalog number and book copy id");
		headline.setStyle("-fx-text-fill: #a0a2ab");
		headline.setFont(new Font(16));
		VBox returnDialogVbox = new VBox(10);
		Label bookCatalogNumberlab = new Label("Book catalog number: ");
		bookCatalogNumberlab.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCatalogNumber = new JFXTextField();
		bookCatalogNumber.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCatalogNumber);
		Label bookCopylab = new Label("Book copy ID: ");
		bookCopylab.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCopy = new JFXTextField();
		bookCopy.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCopy);
		GridPane grid = new GridPane();
		grid.add(bookCatalogNumberlab, 1, 1);
		grid.add(bookCatalogNumber, 2, 1);
		grid.add(bookCopylab, 1, 2);
		grid.add(bookCopy, 2, 2);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		returnDialogVbox.setAlignment(Pos.CENTER);
		JFXButton button = new JFXButton("Return");
		button.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
		returnDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;");
		button.setOnMouseClicked(new EventHandler<Event>() 
		{
			@Override
			public void handle(Event e)
			{

				if (bookCopy.getText().isEmpty())
				{
					GuiManager.ShowErrorPopup("Enter book copy id please");
				} else 
				{
					returnDialog.close();
				}
			}
		});
		returnDialogVbox.getChildren().addAll(headline, grid, button);
		Scene returnDialogScene = new Scene(returnDialogVbox, 300, 200);
		returnDialog.setScene(returnDialogScene);
		returnDialog.showAndWait();
	}

	private Subscriber createSubscriberFromTextFields()
	{
		Subscriber subscriber = new Subscriber(userNameTextfield.getText(), passwordTextfield.getText(),
				idNumberTextfield.getText(), firstNameTextfield.getText(), lastNameTextfield.getText());
		String warningMessage = "";
		// input checks:
		if (!phoneNumberTextfield.getText().isEmpty())
		{
			try
			{
				double tryParse = Integer.valueOf(phoneNumberTextfield.getText());
				subscriber.setPhoneNumber(phoneNumberTextfield.getText());
			} catch (Exception e)
			{
				warningMessage = "Wrong phone number format.\n";
				subscriber.setPhoneNumber("0");
			}
		} else
			subscriber.setPhoneNumber("0");

		if (!emailTextfield.getText().isEmpty() && GuiManager.isValidEmailAddress(emailTextfield.getText()))
		{
			subscriber.setEmail(emailTextfield.getText());
		} else
			warningMessage += "Wrong email format. ";
		if (!warningMessage.isEmpty())
			warningLabel.setText(warningMessage);
		return subscriber;
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case CreateSubscriber:
		{
			Subscriber newSub = (Subscriber) msg.Data;
			if (newSub == null)
			{
				Platform.runLater(() -> {
					warningLabel.setText("Subscriber already exist!");
				});
			} else
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup(
							"Subscriber " + ((Subscriber) msg.Data).getSubscriberNumber() + " Added Successfully!");
				});
			}
			break;
		}
				case CreateNewBorrow: 
		{
			BorrowACopyOfBook newBorrow = (BorrowACopyOfBook) msg.Data;
			if (newBorrow.getSubscriberId().equals("0")) 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Subscriber ID doesn't exist!");
				});
			}
			else if (newBorrow.getSubscriberId().equals("1")) 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("The subscriber card status is not active,\nthis subscriber can't borrow new books!");
				});
			}
			 else if (newBorrow.getBookCatalogNumber().equals("0")) 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Book catalog number doesn't exist!");
				});
			} 
			else if (newBorrow.getBookCatalogNumber().equals("-1")) 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("All of this book's copies are unavailable,\nplease check you entered the right book catalog number");
				});
			} 
			else if (newBorrow.getCopyId().equals("0")) 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Copy ID doesn't exist!");
				});
			}
			else if (newBorrow.getExpectedReturnDate().equals("0")) // after press on "borrow button
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Wrong return date, please enter date up to 14 days from today");
				});
			} 
			else if (newBorrow.getExpectedReturnDate().equals("1")) // after press on "borrow button
			{
				Platform.runLater(() -> {
					GuiManager
							.ShowMessagePopup("This book is wanted, please enter return date up to 3 days from today");
				});
			} 
			else 
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Borrow executed Successfully!");
					borrowDialog.close();
				});
			}
			break;
		}
		case ViewSubscriberCard:
		{
			if (msg.Data == null)
			{
				Platform.runLater(() -> {
					GuiManager.ShowErrorPopup("This subscriber doesnt exist!");
				});
			}
			else
			{
				Subscriber newSub = (Subscriber) msg.Data;
				Platform.runLater(() -> {
					GuiManager.openSubscriberCard(newSub);
			});
			}
			break;
		}
			case GetAllBooksList:
			searchBookWindowController.setBookMap((Map<Integer, Book>)msg.Data);
			break;			
		}
	}


	@Override
	public void setUserLogedIn(User userLoged)
	{
		userLogedIn = userLoged;
		// make the userName start with upper case
		String name = userLoged.getFirstName().substring(0, 1).toUpperCase() + userLoged.getFirstName().substring(1);
		userWelcomLabel.setText("Hello " + name);
		String userName = userLoged.getUserName();
		userNameLabel.setText(userName);
		initialSearchWindow();
	}

	@Override
	public User getUserLogedIn()
	{
		return userLogedIn;
	}

    @FXML
    protected void btn_viewSubscriberCardClick(ActionEvent event)
    {
    	if (txt_subscriberID.getText().isEmpty())
		{
			GuiManager.ShowErrorPopup("Subscriber ID can't be empty");
		}
		
		else
		{
			GuiManager.client.getSubscriberFromDB(txt_subscriberID.getText());
		}  	
    }

	private void initialSearchWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/SearchBookScreen.fxml"));
			AnchorPane newLoadedPane = loader.load(); 
			searchBookWindowController = loader.getController();
			searchBookWindowController.setUserLogedIn(userLogedIn);
			searchBookWindowController.setPopUpMode(false);
			
			pane_searchBook.getChildren().add(newLoadedPane);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}
}
