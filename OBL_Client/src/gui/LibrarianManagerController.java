package gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.DBMessage;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LibrarianManagerController implements Initializable ,IClientUI
{
	private User userLogedIn;
    @FXML
    private Label userWelcomLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Pane pane_home ,  pane_createNewSubscriberCard , pane_searchBook , pane_searchSubscriberCard, pane_employees,pane_reports;
    @FXML
    private ImageView btn_home, btn_createNewSubscriberCard , btn_books , btn_searchSubscriberCard,btn_employees,btn_reports;

    @FXML
    void btn_homeDisplay(MouseEvent event) 
    {
    	pane_home.setVisible(true);
    	pane_createNewSubscriberCard.setVisible(false);
    	pane_searchBook.setVisible(false);
    	pane_searchSubscriberCard.setVisible(false);
    	pane_employees.setVisible(false);
    	pane_reports.setVisible(false);
    	btn_home.setOpacity(0.5);
    	btn_createNewSubscriberCard.setOpacity(1);
    	btn_books.setOpacity(1);
    	btn_searchSubscriberCard.setOpacity(1);
    	btn_employees.setOpacity(1);
    	btn_reports.setOpacity(1);
    	
    	
    }
    @FXML
    void btn_createNewSubscriberCardDisplay(MouseEvent event) 
    {
    	pane_home.setVisible(false);
    	pane_createNewSubscriberCard.setVisible(true);
    	pane_searchBook.setVisible(false);
    	pane_searchSubscriberCard.setVisible(false);
      	pane_employees.setVisible(false);
    	pane_reports.setVisible(false);
    	btn_home.setOpacity(1);
    	btn_createNewSubscriberCard.setOpacity(0.5);
    	btn_books.setOpacity(1);
    	btn_searchSubscriberCard.setOpacity(1);
    	btn_employees.setOpacity(1);
    	btn_reports.setOpacity(1);
    }

	 @FXML
	 void btn_booksDisplay(MouseEvent event) 
	 {
	    	pane_home.setVisible(false);
	    	pane_createNewSubscriberCard.setVisible(false);
	    	pane_searchBook.setVisible(true);
	    	pane_searchSubscriberCard.setVisible(false);
	      	pane_employees.setVisible(false);
	    	pane_reports.setVisible(false);
	    	btn_home.setOpacity(1);
	    	btn_createNewSubscriberCard.setOpacity(1);
	    	btn_books.setOpacity(0.5);
	    	btn_searchSubscriberCard.setOpacity(1);
	    	btn_employees.setOpacity(1);
	    	btn_reports.setOpacity(1);
	}
	
	@FXML
	void btn_searchSubscriberCardDisplay(MouseEvent event) 
	{
	    	pane_home.setVisible(false);
	    	pane_createNewSubscriberCard.setVisible(false);
	    	pane_searchBook.setVisible(false);
	    	pane_searchSubscriberCard.setVisible(true);
	      	pane_employees.setVisible(false);
	    	pane_reports.setVisible(false);
	    	btn_home.setOpacity(1);
	    	btn_createNewSubscriberCard.setOpacity(1);
	    	btn_books.setOpacity(1);
	    	btn_searchSubscriberCard.setOpacity(0.5);
	    	btn_employees.setOpacity(1);
	    	btn_reports.setOpacity(1);
	   }
    
    @FXML
    void btn_employeesDisplay(MouseEvent event) 
    {
    	pane_home.setVisible(false);
    	pane_createNewSubscriberCard.setVisible(false);
    	pane_searchBook.setVisible(false);
    	pane_searchSubscriberCard.setVisible(false);
    	pane_employees.setVisible(true);
    	pane_reports.setVisible(false);
    	btn_home.setOpacity(1);
    	btn_createNewSubscriberCard.setOpacity(1);
    	btn_books.setOpacity(1);
    	btn_searchSubscriberCard.setOpacity(1);
    	btn_employees.setOpacity(0.5);
    	btn_reports.setOpacity(1);
    }

	 @FXML
	 void btn_reportsDisplay(MouseEvent event) 
	 {
		pane_home.setVisible(false);
    	pane_createNewSubscriberCard.setVisible(false);
    	pane_searchBook.setVisible(false);
    	pane_searchSubscriberCard.setVisible(false);
	 	pane_employees.setVisible(false);
	 	pane_reports.setVisible(true);
	 	btn_home.setOpacity(1);
	 	btn_createNewSubscriberCard.setOpacity(1);
    	btn_books.setOpacity(1);
    	btn_searchSubscriberCard.setOpacity(1);
	 	btn_employees.setOpacity(1);
	 	btn_reports.setOpacity(0.5);
	}
	 
	 public void initialize(URL arg0, ResourceBundle arg1) 
	   {
	    	pane_home.setVisible(true);
	    	pane_createNewSubscriberCard.setVisible(false);
	    	pane_searchBook.setVisible(false);
	    	pane_searchSubscriberCard.setVisible(false);
	    	pane_employees.setVisible(false);
	    	pane_reports.setVisible(false);
	    	btn_home.setOpacity(0.5);
	    	btn_createNewSubscriberCard.setOpacity(1);
	    	btn_books.setOpacity(1);
	    	btn_searchSubscriberCard.setOpacity(1);
	    	btn_employees.setOpacity(1);
	    	btn_reports.setOpacity(1);
		
		
			
	   }
	 
	    @FXML
	    void btn_createSubscriberCardDisplay(ActionEvent event) 
	    {
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("OBL Create new subscriber");
	    	alert.setHeaderText("The subscriber's card was created successfully");
	    	alert.setContentText("Subscriber Number: ______ ");
	    	 Optional<ButtonType> option = alert.showAndWait();
	     if (option.get() == ButtonType.OK) {
	    	 alert.close();
	     }
	    }
 
 

    @FXML
    void logOutDisplay(MouseEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 GuiManager.SwitchScene(SCREENS.login);			//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }	

    }
	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		// TODO Auto-generated method stub
		
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
	}
	@Override
	public User getUserLogedIn()
	{
		return userLogedIn;
	}

}
