package gui;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
	
public class ViewSubscriberCardController implements  IClientUI
{
	    
		private Subscriber subscriberToShow;

	    @FXML
	    private Pane pane_viewSubscriberCard;

	    @FXML
	    private JFXTextField subNumber;

	    @FXML
	    private JFXTextField userName;

	    @FXML
	    private JFXTextField idNumber;

	    @FXML
	    private JFXTextField firstName;

	    @FXML
	    private JFXTextField lastName;

	    @FXML
	    private JFXTextField phoneNumber;

	    @FXML
	    private JFXTextField email;
	    
	    public void setSubscriberToShow(Subscriber sub)
	    {
	    	subscriberToShow = sub;
	    	subNumber.setText(subscriberToShow.getSubscriberNumber());
	    	userName.setText(subscriberToShow.getUserName());
	    	idNumber.setText(subscriberToShow.getId());
	    	firstName.setText(subscriberToShow.getFirstName());
	    	lastName.setText(subscriberToShow.getLastName());
	    	phoneNumber.setText(subscriberToShow.getPhoneNumber());
	    	email.setText(subscriberToShow.getEmail());
	    }
	     
		
	    @Override
		public void getMessageFromServer(DBMessage msg)
		{
			// TODO Auto-generated method stub

		}
	  
	    public void setUserLogedIn(User userLoged)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public User getUserLogedIn()
		{
			// TODO Auto-generated method stub
			return null;
		}
		
		

	}



