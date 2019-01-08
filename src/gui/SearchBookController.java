package gui;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import entities.*;
import defaultPackage.mainClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class SearchBookController implements Initializable{
	
	    @FXML
	    private TableView<Book> BookTable;
	 
	    @FXML
	    private TableColumn<Book, String> namecol;

	    @FXML
	    private TableColumn<Book, Integer> catalognumbercol;

	    @FXML
	    private TableColumn<Book, String> authorcol;

	    @FXML
	    private TableColumn<Book, Integer> numbercol;

	    @FXML
	    private TableColumn<Book, String> categoriescol;

	    @FXML
	    private TableColumn<Book, String> locationcol;

	
	   @Override
		public void initialize(URL arg0, ResourceBundle arg1)
	   {
		   namecol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		   catalognumbercol.setCellValueFactory(new PropertyValueFactory<>("Catalognumber"));
		   authorcol.setCellValueFactory(new PropertyValueFactory<>("Author"));
		   numbercol.setCellValueFactory(new PropertyValueFactory<>("Numberofcopys"));
		   categoriescol.setCellValueFactory(new PropertyValueFactory<>("Categories"));
		   locationcol.setCellValueFactory(new PropertyValueFactory<>("Location"));
		   
		  Book book1 = new Book("Harry Potter", 123456, "J.K.Rolling", 7, "Adventure", "A6 313");
		  Book book2 = new Book("Kofiko", 456789, "Galila Ron Feder", 3, "Fun", "A8 949");
		  ObservableList<Book> list = FXCollections.observableArrayList(book1,book2);
		  
		   BookTable.setItems(list);
		   
		   
	   }

	  /* @FXML
	    void searchBookDisplay(ActionEvent event) {							//press on search in search book screen
	    	


	    }*/

    @FXML
    void logOutDisplay(MouseEvent event) {				//press on back to preview
   
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	defaultPackage.mainClient.getLoginStage().show();				//show login screen

    }

 

}
