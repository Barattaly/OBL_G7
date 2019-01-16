package gui;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import entities.DBMessage;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
	
public class ViewSubscriberCardController implements  IClientUI {
	
	
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



