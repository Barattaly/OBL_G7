package gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;




public class LibrarianScreenControllerNew implements Initializable 
{
	
	
	public void initialize(URL arg0, ResourceBundle arg1) 
	   {
		
				
			
	   }
	
    @FXML
    void logOutDisplay(MouseEvent event) {			//logout
    	
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 defaultPackage.mainClient.getLoginStage().show();				//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }
     
     
  
    	

    }

}
