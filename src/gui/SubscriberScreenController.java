package gui;

import java.io.Serializable;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.*;
import defaultPackage.mainClient;
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

public class SubscriberScreenController implements Initializable
{

    @FXML
    private Button Search;

    @FXML
    private TextField Username12;

    @FXML
    private Button Order;

    @FXML
    private Label LogOut113;

    @FXML
    private ImageView logoutimage113;
    
    @FXML
    private TableView<Book> Table2;

    @FXML
    private TableColumn<Book, String> namecol;

    @FXML
    private TableColumn<Book, Integer> catalogynumbercol;

    @FXML
    private TableColumn<Book, String> authorcol;

    @FXML
    private TableColumn<Book, Integer> numbercol;

    @FXML
    private TableColumn<Book, String> subjectcol;

    @FXML
    private TableColumn<Book, String> locationcol;
    
    /////////////////////////////////////////////////
    @FXML
    private TableView<ActionHistory> Table1;

    @FXML
    private TableColumn<ActionHistory, String> borrowcol;

    @FXML
    private TableColumn<ActionHistory, String> returncol;

    @FXML
    private TableColumn<ActionHistory, String> librariancol;

    @FXML
    private TableColumn<ActionHistory, String> bookcol;

    @FXML
    private TableColumn<ActionHistory, Integer> copycol;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		   namecol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		   catalogynumbercol.setCellValueFactory(new PropertyValueFactory<>("Catalogynumber"));
		   authorcol.setCellValueFactory(new PropertyValueFactory<>("Author"));
		   numbercol.setCellValueFactory(new PropertyValueFactory<>("Numberofcopys"));
		   subjectcol.setCellValueFactory(new PropertyValueFactory<>("Subject"));
		   locationcol.setCellValueFactory(new PropertyValueFactory<>("Location"));
		   
		  Book book1 = new Book("Harry Potter", 123456, "J.K.Rolling", 7, "Adventure", "A6 313");
		  Book book2 = new Book("Kofiko", 456789, "Galila Ron Feder", 3, "Fun", "A8 949");
		  ObservableList<Book> list = FXCollections.observableArrayList(book1,book2);
		  
		   Table2.setItems(list);
		   
		   /////////////////////////////////////////////////////////////////////////////////////
		   
		   borrowcol.setCellValueFactory(new PropertyValueFactory<>("Borrowdate"));
		   returncol.setCellValueFactory(new PropertyValueFactory<>("Returndate"));
		   librariancol.setCellValueFactory(new PropertyValueFactory<>("Librarian"));
		   bookcol.setCellValueFactory(new PropertyValueFactory<>("Bookname"));
		   copycol.setCellValueFactory(new PropertyValueFactory<>("Copynumber"));
		  
		   ActionHistory action1 = new ActionHistory("14/10/2017", "30/10/2017", "Galina","Harry Poter", 14);
		   ActionHistory action2 = new ActionHistory("2/8/2012", "4/5/2014", "Talia","Kofiko", 4);
		  
		  ObservableList<ActionHistory> list2 = FXCollections.observableArrayList(action1,action2);
		  
		   Table1.setItems(list2);
    	
	}

    @FXML
    void LogOutDisplay(MouseEvent event) {
     	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 GuiManager.getLoginStage().show();				//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }

    }

    @FXML
    void LogOutImageDisplay(MouseEvent event) {
     	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 GuiManager.getLoginStage().show();				//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }

    }

}
