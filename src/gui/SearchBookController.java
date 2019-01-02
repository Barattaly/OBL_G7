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
	    private TableColumn<Book, Integer> catalogynumbercol;

	    @FXML
	    private TableColumn<Book, String> authorcol;

	    @FXML
	    private TableColumn<Book, Integer> numbercol;

	    @FXML
	    private TableColumn<Book, String> subjectcol;

	    @FXML
	    private TableColumn<Book, String> locationcol;

	
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
		  
		   BookTable.setItems(list);
		   
		   
	   }

	   @FXML
	    void SearchBookDisplay(ActionEvent event) {							//press on search in search book screen
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("OBL Search Book");
	    	alert.setHeaderText("The book is exist in A4 383");
	    	ButtonType show = new ButtonType("show Table of Contents");
	    	ButtonType OK = new ButtonType("OK");
	    	
	    	
	        alert.getButtonTypes().clear();
	        alert.getButtonTypes().addAll(OK, show);
	        
	        Optional<ButtonType> option = alert.showAndWait();
	        if (option.get() == OK) {
	            alert.close();
	        } else if (option.get() == show) {
	        	Alert alert2 = new Alert(AlertType.INFORMATION);
	        	alert2.setTitle("Table of Contents");
	        	alert2.setHeaderText("The book is pinokio ....");
	        	alert2.showAndWait();


	        } 


	    }

    @FXML
    void LogOutDisplay(MouseEvent event) {				//press on back to preview
   
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	mainClient.getLoginStage().show();				//show login screen

    }

    @FXML
    void LogOutImageDisplay(MouseEvent event) {         //press on back to preview image
    	
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	mainClient.getLoginStage().show();				//show login screen

    }

}
