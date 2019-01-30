package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import entities.*;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SubscriberScreenController implements Initializable, IClientUI
{
	private User userLogedIn;

	private Subscriber subscriberLoggedIn;
	@FXML
	private Label userWelcomLabel;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label statusLabel;

	@FXML
	private Pane pane_home;
	@FXML
	private AnchorPane pane_viewSubscriberCard,pane_books;
	@FXML
	private AnchorPane pane_searchBook;

	@FXML
	private ImageView btn_home, btn_books, btn_viewSubscriberCard, btn_searchBook;

	private SearchBookController searchBookWindowController = null; 
	
	private BorrowsScreenController borrowsWindowController = null; 
	
	private ViewSubscriberCardController viewSubscriberCardController = null;

	@FXML
	void btn_homeDisplay(MouseEvent event)
	{
		if (!viewSubscriberCardController.getFirstNameField().getText().isEmpty())// in case user first name changed by him
		{
			String name = viewSubscriberCardController.getFirstNameField().getText().substring(0, 1).toUpperCase()
					+ viewSubscriberCardController.getFirstNameField().getText().substring(1);
			userWelcomLabel.setText("Hello " + name);
		}
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		pane_home.setVisible(true);
		pane_searchBook.setVisible(false);
		pane_viewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		btn_home.setOpacity(0.5);
		btn_books.setOpacity(1);
		btn_viewSubscriberCard.setOpacity(1);
		btn_searchBook.setOpacity(1);
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
			AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
			AnchorPane.setRightAnchor(newLoadedPane, 0.0);
			AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
			AnchorPane.setTopAnchor(newLoadedPane, 0.0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void initialBorrowsWindow()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/BorrowsScreen.fxml"));
			AnchorPane newLoadedPane = loader.load();
			borrowsWindowController = loader.getController();
			borrowsWindowController.setUserLogedIn(userLogedIn);
			pane_books.getChildren().add(newLoadedPane);
			AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
			AnchorPane.setRightAnchor(newLoadedPane, 0.0);
			AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
			AnchorPane.setTopAnchor(newLoadedPane, 0.0);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
		} else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();
		}
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetAllBooksList:
			searchBookWindowController.getMessageFromServer(msg);
			break;
		case GetCurrentBorrowsForSubID:
			borrowsWindowController.getMessageFromServer(msg);
			break;
		case CreateNewOrder:
			searchBookWindowController.getMessageFromServer(msg);
			break;
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
					subscriberLoggedIn = newSub;
					if(searchBookWindowController!=null)searchBookWindowController.setSubscriber(subscriberLoggedIn);
					initialSubscriberCard(newSub);
					String status = newSub.getStatus().substring(0, 1).toUpperCase() + newSub.getStatus().substring(1);
					statusLabel.setText("Subscriber card status: " + status);
				});

			}
			break;
		}
		case GetActivityLog:
		{
			ArrayList<ActivityLog> activityList= (ArrayList<ActivityLog>) msg.Data;
			if(activityList == null)
				break;
			else
			{
				Platform.runLater(() -> {
					viewSubscriberCardController.setActivityLogList(activityList);
				});
			}

			break;  
		}
		case BorrowExtension:
		{
			Platform.runLater(() -> {
				borrowsWindowController.getMessageFromServer(msg);
			});
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
		GuiManager.client.getSubscriberFromDB(userLogedIn.getId());
		initialSearchWindow();
		initialBorrowsWindow();
	}

	@Override
	public User getUserLogedIn()
	{
		return userLogedIn;
	}

	private void initialSubscriberCard(Subscriber subscriber)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/ViewSubscriberCardScreen.fxml"));
			AnchorPane newLoadedPane = loader.load();
			viewSubscriberCardController = loader.getController();
			viewSubscriberCardController.setSubscriberToShow(subscriber);
			// viewSubscriberCardController.setPopUpMode(false);
			

			pane_viewSubscriberCard.getChildren().add(newLoadedPane);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
