package gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
	private Pane pane_home, pane_createNewSubscriberCard, pane_searchBook, pane_searchSubscriberCard;
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
	private Label warningLabel;

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
	void btn_createNewSubscriberCardDisplay(MouseEvent event)
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
	}

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
		alert.setHeaderText("Are you sure you want to log out ?");
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
	    void btn_borrowClick(ActionEvent event) {
		   final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setTitle("Borrow Book");
			Label headline = new Label("Enter book copy id and subscriber id");
			headline.setFont(new Font(16));
			VBox dialogVbox = new VBox(30);
			Label bookCopylab = new Label("Book copy ID: ");
			TextField bookCopy = new TextField();
			Label subscriberIDlab = new Label("Subscriber ID: ");
			TextField subscriberID = new TextField();
			GridPane grid = new GridPane();
			grid.add(bookCopylab, 1, 1);
			grid.add(bookCopy, 2, 1);
			grid.add(subscriberIDlab, 1, 2);
			grid.add(subscriberID, 2, 2);
			grid.setHgap(10); 
			grid.setVgap(10); 
			grid.setAlignment(Pos.CENTER);
			dialogVbox.setAlignment(Pos.CENTER);
			Button button = new Button("Borrow");
			button.setOnMouseClicked(new EventHandler<Event>()
			{
				@Override
				public void handle(Event e)
				{
					if (bookCopy.getText().isEmpty() && subscriberID.getText().isEmpty())
					{
						GuiManager.ShowErrorPopup("Enter book copy id and subscriber id please");
					}
					else if (bookCopy.getText().isEmpty())
					{
						GuiManager.ShowErrorPopup("Enter book copy id please");
						
					}
					else if (subscriberID.getText().isEmpty())
					{
						GuiManager.ShowErrorPopup("Enter subscriber id please");
					}
					else
					{
						dialog.close();
					}
				}
			});
			dialogVbox.getChildren().addAll(headline ,grid, button);
			Scene dialogScene = new Scene(dialogVbox, 300, 200);
			dialog.setScene(dialogScene);
			dialog.showAndWait();

	    }
	   
	    @FXML
	    void btn_ReturnClick(ActionEvent event) {
			   final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.setTitle("Return Book");
				Label headline = new Label("Enter book copy id");
				headline.setFont(new Font(16));
				VBox dialogVbox = new VBox(30);
				Label bookCopylab = new Label("Book copy ID: ");
				TextField bookCopy = new TextField();
				GridPane grid = new GridPane();
				grid.add(bookCopylab, 1, 1);
				grid.add(bookCopy, 2, 1);
				grid.setAlignment(Pos.CENTER);
				dialogVbox.setAlignment(Pos.CENTER);
				Button button = new Button("Return");
				button.setOnMouseClicked(new EventHandler<Event>()
				{
					@Override
					public void handle(Event e)
					{
		
						if (bookCopy.getText().isEmpty())
						{
							GuiManager.ShowErrorPopup("Enter book copy id please");
						}
						else
						{
							dialog.close();
						}
					}
				});
				dialogVbox.getChildren().addAll(headline ,grid, button);
				Scene dialogScene = new Scene(dialogVbox, 300, 200);
				dialog.setScene(dialogScene);
				dialog.showAndWait();

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
		}
		else subscriber.setPhoneNumber("0");

		if (!emailTextfield.getText().isEmpty() && isValidEmailAddress(emailTextfield.getText()))
		{
			subscriber.setEmail(emailTextfield.getText());
		} else
			warningMessage += "Wrong email format. ";
		if(!warningMessage.isEmpty())
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
					GuiManager.ShowMessagePopup("Subscriber Added Successfully!");
				});

			}
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
	}

	@Override
	public User getUserLogedIn()
	{
		return userLogedIn;
	}

	private static boolean isValidEmailAddress(String email)
	{
		boolean result = true;
		try
		{
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex)
		{
			result = false;
		}
		return result;
	}

}
