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
	
public class ViewSubscriberCardController implements  IClientUI, Initializable{
	    private Subscriber sub;
		private Node thisNode; //check if needed
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
	    
	  
	     public ViewSubscriberCardController(Subscriber sub) {
			// TODO Auto-generated constructor stub
	    	 this.sub =sub;
		}
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1)
		{
	    	subNumber.setText(sub.getSubscriberNumber());
	    	userName.setText(sub.getUserName());
	    	idNumber.setText(sub.getId());
	    	firstName.setText(sub.getFirstName());
	    	lastName.setText(sub.getLastName());
	    	phoneNumber.setText(sub.getPhoneNumber());
	    	email.setText(sub.getEmail());
	    	
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



