package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import entities.ActivityLog;
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
import javafx.scene.Parent;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibrarianScreenController implements Initializable, IClientUI
{
	private User userLogedIn;
	@FXML
	protected Label userWelcomLabel;
	@FXML
	protected Label userNameLabel;
	@FXML
	protected Pane pane_createNewSubscriberCard, pane_searchSubscriberCard;
	@FXML
	protected AnchorPane pane_searchBook,pane_home;
	@FXML
	protected ImageView btn_home, btn_createNewSubscriberCard, btn_books, btn_searchSubscriberCard;

	@FXML
	protected JFXTextField userNameTextfield;

	@FXML
	protected JFXTextField idNumberTextfield;

	@FXML
	protected JFXTextField firstNameTextfield;

	@FXML
	protected JFXTextField lastNameTextfield;

	@FXML
	protected JFXTextField phoneNumberTextfield;

	@FXML
	protected JFXTextField emailTextfield;

	@FXML
	protected JFXPasswordField passwordTextfield;

	@FXML
	protected JFXTextField txt_subscriberID;

	@FXML
	protected Label warningLabel;
	@FXML
	protected JFXButton btn_viewSubscriberCard;

	 protected ViewSubscriberCardController controller; 

	protected SearchBookController searchBookWindowController = null;

	protected BorrowsScreenController borrowsWindowController = null;

	@FXML
	protected AnchorPane borrowsPane;

	protected Stage borrowDialog = null;

	protected Stage returnDialog = null;
	
	protected JFXProgressBar returnDialogProgressBar = null;
	
	protected JFXProgressBar borrowDialogProgressBar = null;

	@FXML
	protected void btn_homeDisplay(MouseEvent event)
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
	protected void btn_booksDisplay(MouseEvent event)
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
	protected void btn_searchSubscriberCardDisplay(MouseEvent event)
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
		GuiManager.preventLettersTypeInTextField(idNumberTextfield);
		GuiManager.preventLettersTypeInTextField(phoneNumberTextfield);
		emailTextfield.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent e)
			{
				if (!GuiManager.isValidEmailAddress(emailTextfield.getText()))
				{
					warningLabel.setText("Wrong email format");
				}
				else
					warningLabel.setText("");
			}
		});
	}

	@FXML
	protected void logOutDisplay(MouseEvent event)
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
	protected void btn_createSubscriberCardDisplay(ActionEvent event)
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
	protected void btn_borrowClick(ActionEvent event)
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
		Label bookCatalogNumberLabel = new Label("Book catalog number: ");
		bookCatalogNumberLabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCatalogNumber = new JFXTextField();
		bookCatalogNumber.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCatalogNumber);
		Label bookCopyIdLabel = new Label("Book copy ID: ");
		bookCopyIdLabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCopyId = new JFXTextField();
		bookCopyId.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCopyId);
		Label subscriberIdLabel = new Label("Subscriber ID: ");
		subscriberIdLabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField subscriberID = new JFXTextField();
		subscriberID.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(subscriberID);
		GuiManager.limitTextFieldMaxCharacters(subscriberID, 9);
		Label returnDateLabel = new Label("Return date: ");
		returnDateLabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXDatePicker returnDate = new JFXDatePicker();
		returnDate.setStyle("-fx-text-inner-color: #a0a2ab");
		returnDate.setPromptText("dd.mm.yyyy or dd.mm.yyyy");
		returnDate.setDayCellFactory(picker -> new DateCell()
		{
			public void updateItem(LocalDate date, boolean empty)
			{
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.compareTo(today) < 0
						|| date.compareTo(today.plusDays(13)) > 0);
			}
		});

		GridPane grid = new GridPane();
		grid.add(bookCatalogNumberLabel, 1, 1);
		grid.add(bookCatalogNumber, 2, 1);
		grid.add(bookCopyIdLabel, 1, 2);
		grid.add(bookCopyId, 2, 2);
		grid.add(subscriberIdLabel, 1, 3);
		grid.add(subscriberID, 2, 3);
		grid.add(returnDateLabel, 1, 4);
		grid.add(returnDate, 2, 4);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		borrowDialogVbox.setAlignment(Pos.CENTER);
		Label warningMessageLabel = new Label("");
		warningMessageLabel.setStyle("-fx-text-fill: RED; -fx-font-weight: BOLD");
		JFXButton borrowBtn = new JFXButton("Borrow");
		borrowBtn.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
		borrowDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;");
		borrowDialogProgressBar = new JFXProgressBar();
		borrowDialogProgressBar.setVisible(false);
		borrowBtn.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				String warningMessage = "";
				warningMessageLabel.setText(warningMessage);
				if (bookCatalogNumber.getText().isEmpty() && bookCopyId.getText().isEmpty()
						&& subscriberID.getText().isEmpty() && (returnDate.getValue() == null))
				{
					warningMessage = "Please fill all of the fields";
				} else if (bookCatalogNumber.getText().isEmpty())
				{
					warningMessage = "Please enter book catalog number";
				} else if (bookCopyId.getText().isEmpty())
				{
					warningMessage = "Please enter book copy id";
				} else if (subscriberID.getText().isEmpty())
				{
					warningMessage = "Please enter subscriber id";
				} else if (returnDate.getValue() == null)
				{
					warningMessage = "Please enter return date";
				} else
				{
					try
					{
						String retDate = returnDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

						BorrowACopyOfBook newBorrow = new BorrowACopyOfBook(subscriberID.getText(), retDate,
								bookCatalogNumber.getText(), bookCopyId.getText());
						borrowDialogProgressBar.setVisible(true);
						GuiManager.client.createNewBorrow(newBorrow);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				if (!warningMessage.isEmpty())
					warningMessageLabel.setText(warningMessage);
			}
		});
		borrowDialogVbox.getChildren().addAll(headline, grid, warningMessageLabel, borrowBtn, borrowDialogProgressBar);
		Scene borrowDialogScene = new Scene(borrowDialogVbox, 300, 200);
		borrowDialog.setScene(borrowDialogScene);
		borrowDialog.showAndWait();
	}

	@FXML
	protected void btn_ReturnClick(ActionEvent event)
	{
		returnDialog = new Stage();
		returnDialog.initModality(Modality.APPLICATION_MODAL);
		returnDialog.setTitle("Return a copy of a Book");
		returnDialog.getIcons().add(new Image("/resources/Braude.png"));
		returnDialog.setHeight(300);
		returnDialog.setWidth(400);
		Label headline = new Label("Enter return details");
		headline.setStyle("-fx-text-fill: #a0a2ab");
		headline.setFont(new Font(16));
		VBox returnDialogVbox = new VBox(10);
		Label bookCatalogNumberlabel = new Label("Book catalog number: ");
		bookCatalogNumberlabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCatalogNumber = new JFXTextField();
		bookCatalogNumber.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCatalogNumber);
		Label bookCopyIdLabel = new Label("Book copy ID: ");
		bookCopyIdLabel.setStyle("-fx-text-fill: #a0a2ab");
		JFXTextField bookCopyId = new JFXTextField();
		bookCopyId.setStyle("-fx-text-fill: #a0a2ab");
		GuiManager.preventLettersTypeInTextField(bookCopyId);
		GridPane grid = new GridPane();
		grid.add(bookCatalogNumberlabel, 1, 1);
		grid.add(bookCatalogNumber, 2, 1);
		grid.add(bookCopyIdLabel, 1, 2);
		grid.add(bookCopyId, 2, 2);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		returnDialogVbox.setAlignment(Pos.CENTER);
		Label warningMessageLabel = new Label("");
		warningMessageLabel.setStyle("-fx-text-fill: RED; -fx-font-weight: BOLD");
		JFXButton returnButton = new JFXButton("Return");
		returnButton.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
		returnDialogProgressBar = new JFXProgressBar();
		returnDialogProgressBar.setVisible(false);
		returnDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;");
		returnButton.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				String warningMessage = "";
				warningMessageLabel.setText(warningMessage);
				if (bookCatalogNumber.getText().isEmpty() && bookCopyId.getText().isEmpty())
				{
					warningMessage = "Please fill all of the fields";
				} else if (bookCatalogNumber.getText().isEmpty())
				{
					warningMessage = "Please enter book catalog number";
				} else if (bookCopyId.getText().isEmpty())
				{
					warningMessage = "Please enter book copy id";
				} else
				{
					try
					{
						String returnDateTime = getCurrentDateTimeAsString();

						BorrowACopyOfBook borrowToClose = new BorrowACopyOfBook(returnDateTime,
								bookCatalogNumber.getText(), bookCopyId.getText(), true);
						returnDialogProgressBar.setVisible(true);
						GuiManager.client.returnBook(borrowToClose);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				if (!warningMessage.isEmpty())
					warningMessageLabel.setText(warningMessage);
			}
		});

		returnDialogVbox.getChildren().addAll(headline, grid, warningMessageLabel, returnButton, returnDialogProgressBar);
		Scene returnDialogScene = new Scene(returnDialogVbox, 300, 200);
		returnDialog.setScene(returnDialogScene);
		returnDialog.showAndWait();
	}

	protected Subscriber createSubscriberFromTextFields()
	{
		/*
		 * public Subscriber (String idNumber,String firstName,String lastName,String
		 * phoneNumber,String email,String status)
		 */

		Subscriber subscriber = new Subscriber(idNumberTextfield.getText(), firstNameTextfield.getText(),
				lastNameTextfield.getText(), "0", "", "active");// phone and email default values at first
		subscriber.setUserName(userNameTextfield.getText());
		subscriber.setPassword(passwordTextfield.getText());
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

	@SuppressWarnings("unchecked")
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
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Subscriber ID doesn't exist!");
				});
			} else if (newBorrow.getSubscriberId().equals("1"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This book has been ordered by another subscriber,\nthis subscriber can't borrow this book.");
				});
			} else if (newBorrow.getSubscriberId().equals("2"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("The subscriber card status is not active,\nthis subscriber can't borrow new books!");
				});
			} else if (newBorrow.getBookCatalogNumber().equals("0"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Book catalog number doesn't exist!");
				});
			} else if (newBorrow.getBookCatalogNumber().equals("-1"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("The book is archived,\nsubscriber can't borrow copies of it.");
				});
			} else if (newBorrow.getBookCatalogNumber().equals("-2"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("All of this book's copies are unavailable,\nplease check you entered the correct book catalog number");
				});
			} else if (newBorrow.getBookCatalogNumber().equals("-3"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This subscriber is currently borrowing a copy of this book");
				});
			} else if (newBorrow.getCopyId().equals("0"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Copy ID doesn't exist!");
				});
			} else if (newBorrow.getCopyId().equals("-1"))
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This copy is already borrowed,\nplease check you entered the correct copy ID");
				});
			} else if (newBorrow.getExpectedReturnDate().equals("0")) // after press on "borrow button
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Wrong return date, please enter date up to 14 days from today");
				});
			} else if (newBorrow.getExpectedReturnDate().equals("1")) // after press on "borrow button
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This book is wanted, please enter return date up to 3 days from today");
				});
			} else
			{
				Platform.runLater(() -> {
					borrowDialogProgressBar.setVisible(false);
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
			} else
			{
				Subscriber newSub = (Subscriber) msg.Data;
				Platform.runLater(() -> {
					GuiManager.openSubscriberCard(newSub);
				});
			}
			break;
		}
		case GetActivityLog:
		{
			ArrayList<ActivityLog> activityList = (ArrayList<ActivityLog>) msg.Data;
			if (activityList == null)
				break;
			else
			{
				Platform.runLater(() -> {
					GuiManager.openActvityLog(activityList);
				});
			}
			break;
		}

	
		case GetAllBooksList:
		{
			searchBookWindowController.getMessageFromServer(msg);
			break;
		}
		case GetCurrentBorrows:
			borrowsWindowController.getMessageFromServer(msg);
			break;
		case ReturnBook:
		{
			BorrowACopyOfBook newBorrow = (BorrowACopyOfBook) msg.Data;
			if (newBorrow.getBookCatalogNumber().equals("0"))
			{
				Platform.runLater(() -> {
					returnDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Book catalog number doesn't exist!");
				});
			} else if (newBorrow.getBookCatalogNumber().equals("-1"))
			{
				Platform.runLater(() -> {
					returnDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("None of this book's copies are currently borrow,\nplease check you entered the correct book catalog number");
				});
			} else if (newBorrow.getCopyId().equals("0"))
			{
				Platform.runLater(() -> {
					returnDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Copy ID doesn't exist!");
				});
			} else if (newBorrow.getCopyId().equals("-1"))
			{
				Platform.runLater(() -> {
					returnDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This copy is not currently borrow,\nplease check you entered the correct copy ID");
				});
			} else
			{
				Platform.runLater(() -> {
					returnDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Return executed Successfully!");
					returnDialog.close();
				});
			}
			break;
		}
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
		initialBorrowsWindow();
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


	protected void initialSearchWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/SearchBookScreen.fxml"));
			AnchorPane newLoadedPane = loader.load();
			searchBookWindowController = loader.getController();
			searchBookWindowController.setUserLogedIn(userLogedIn);
			searchBookWindowController.setPopUpMode(false);
			AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
			AnchorPane.setTopAnchor(newLoadedPane, 0.0);
			AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
			AnchorPane.setRightAnchor(newLoadedPane, 0.0);

			pane_searchBook.getChildren().add(newLoadedPane);
		} catch (Exception e)
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

	public static String getCurrentDateTimeAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(calendar.getTime());
		return string;
	}

	protected void initialBorrowsWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/BorrowsScreen.fxml"));
			AnchorPane newLoadedPane = loader.load();
			borrowsWindowController = loader.getController();
			borrowsWindowController.setUserLogedIn(userLogedIn);
			
			AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
			AnchorPane.setRightAnchor(newLoadedPane, 0.0);
			AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
			AnchorPane.setTopAnchor(newLoadedPane, 0.0);
			
			borrowsPane.getChildren().add(newLoadedPane);


		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
