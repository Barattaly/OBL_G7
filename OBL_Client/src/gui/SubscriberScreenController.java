package gui;

import java.io.Serializable;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import entities.*;
import gui.GuiManager.SCREENS;
import defaultPackage.mainClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class SubscriberScreenController implements Initializable, IClientUI
{
	private User userLogedIn;
    @FXML
    private Label userWelcomLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label statusLabel;
    
    @FXML
    private JFXTextField subscriberNumberField;

    @FXML
    private JFXTextField idNumberField;

    @FXML
    private JFXTextField firstNameField;

    @FXML
    private JFXTextField lastNameField;

    @FXML
    private JFXTextField phoneNumberField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField userNameField;
    
	@FXML
	private Pane pane_home, pane_books, pane_viewSubscriberCard, pane_searchBook;

	@FXML
	private ImageView btn_home, btn_books, btn_viewSubscriberCard, btn_searchBook;

	@FXML
	void btn_homeDisplay(MouseEvent event)
	{
		pane_home.setVisible(true);
		pane_books.setVisible(false);
		pane_viewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		btn_home.setOpacity(0.5);
		btn_books.setOpacity(1);
		btn_viewSubscriberCard.setOpacity(1);
		btn_searchBook.setOpacity(1);

	}

	@FXML
	void btn_booksDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_books.setVisible(true);
		pane_viewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		btn_home.setOpacity(1);
		btn_books.setOpacity(0.5);
		btn_viewSubscriberCard.setOpacity(1);
		btn_searchBook.setOpacity(1);
	}

	@FXML
	void btn_viewSubscriberCardDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_books.setVisible(false);
		pane_viewSubscriberCard.setVisible(true);
		pane_searchBook.setVisible(false);
		btn_home.setOpacity(1);
		btn_books.setOpacity(1);
		btn_viewSubscriberCard.setOpacity(0.5);
		btn_searchBook.setOpacity(1);
		
	}

	@FXML
	void btn_searchBookDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_books.setVisible(false);
		pane_viewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(true);
		btn_home.setOpacity(1);
		btn_books.setOpacity(1);
		btn_viewSubscriberCard.setOpacity(1);
		btn_searchBook.setOpacity(0.5);

	}

	/*
	 * @FXML private TableView<Book> Table2;
	 * 
	 * @FXML private TableColumn<Book, String> namecol;
	 * 
	 * @FXML private TableColumn<Book, Integer> catalogynumbercol;
	 * 
	 * @FXML private TableColumn<Book, String> authorcol;
	 * 
	 * @FXML private TableColumn<Book, Integer> numbercol;
	 * 
	 * @FXML private TableColumn<Book, String> subjectcol;
	 * 
	 * @FXML private TableColumn<Book, String> locationcol;
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		/*
		 * namecol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		 * catalogynumbercol.setCellValueFactory(new
		 * PropertyValueFactory<>("Catalogynumber")); authorcol.setCellValueFactory(new
		 * PropertyValueFactory<>("Author")); numbercol.setCellValueFactory(new
		 * PropertyValueFactory<>("Numberofcopys")); subjectcol.setCellValueFactory(new
		 * PropertyValueFactory<>("Subject")); locationcol.setCellValueFactory(new
		 * PropertyValueFactory<>("Location"));
		 * 
		 * Book book1 = new Book("Harry Potter" ,"J.K.Rolling" ,123456, 7, "Adventure",
		 * "A6 313"); Book book2 = new Book("Kofiko", "Galila Ron Feder" ,456789, 3,
		 * "Fun", "A8 949"); ObservableList<Book> list =
		 * FXCollections.observableArrayList(book1,book2);
		 * 
		 * Table2.setItems(list);
		 */

		pane_home.setVisible(true);
		pane_searchBook.setVisible(false);
		pane_viewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		btn_home.setOpacity(0.5);
		btn_books.setOpacity(1);
		btn_viewSubscriberCard.setOpacity(1);
		btn_searchBook.setOpacity(1);
		
		

	

	}

	@FXML
	void logOutDisplay(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("OBL Log Out");
		alert.setHeaderText("Are you sure you want to log out?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK)
		{
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			GuiManager.SwitchScene(SCREENS.login); // show login screen
		}

		else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();

		}

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		// TODO Auto-generated method stub
		switch(msg.Action)
		{
			case ViewSubscriberCard:
			{
				Subscriber newSub = (Subscriber) msg.Data;
				if (newSub == null)
				{
					Platform.runLater(() -> {
						GuiManager.ShowErrorPopup("Something went wrong\nPlease restart the program.");
					});
				} else
				{
					Platform.runLater(() -> {
						setSubscriberCardDisplay(newSub);
					});

				}
				break;
			}
		}

	}
	
	private void setSubscriberCardDisplay(Subscriber newSub)
	{
		String status = newSub.getStatus().substring(0,1).toUpperCase() + newSub.getStatus().substring(1);
		statusLabel.setText("Subscriber card status: "+ status);
	    subscriberNumberField.setText(newSub.getSubscriberNumber());
	    idNumberField.setText(newSub.getId());
	    firstNameField.setText(newSub.getFirstName());
	    lastNameField.setText(newSub.getLastName());
	    phoneNumberField.setText(newSub.getPhoneNumber());
	    emailField.setText(newSub.getEmail());
	    userNameField.setText(newSub.getUserName());
		
	}



	@Override
	public void setUserLogedIn(User userLoged)
	{
		userLogedIn = userLoged;
		//make the userName start with upper case
		String name = userLoged.getFirstName().substring(0, 1).toUpperCase() + userLoged.getFirstName().substring(1);		
		userWelcomLabel.setText("Hello "+ name);
		String userName = userLoged.getUserName();
		userNameLabel.setText(userName);
		GuiManager.client.getSubscriberFromDB(userLogedIn.getId());

		
	}
	


	@Override
	public User getUserLogedIn()
	{
		// TODO Auto-generated method stub
		return userLogedIn;
	}

}
