package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LoginController {
	


	
    
    @FXML
    void LoginDisplay(ActionEvent event) throws IOException {           //press on login button
    
	   	/*((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
	    	Stage SeondStage = new Stage();
	    	AnchorPane root = FXMLLoader.load(getClass().getResource("/application/try.fxml"));
			Scene scene = new Scene(root);
			SeondStage.setTitle("Ort Braude Library");
			//SeondStage.getIcons().add(new Image("/resource/Braude.png"));
			SeondStage.setScene(scene);		
			SeondStage.show();*/
    		
    	
    }
    
  @FXML
    void OpenSearchScreen(MouseEvent event) throws IOException 
    {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	Stage SeondStage2 = new Stage();
    	AnchorPane root2 = FXMLLoader.load(getClass().getResource("/application/SearchBookScreen.fxml"));
		Scene scene2 = new Scene(root2);
		SeondStage2.setTitle("Ort Braude Library");
		//SeondStage2.getIcons().add(new Image("application/Braude.png"));
		SeondStage2.setScene(scene2);		
		SeondStage2.show();
  }

  
    

    
   

}
