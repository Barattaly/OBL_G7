package gui;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import entities.*;
import gui.GuiManager.SCREENS;
import defaultPackage.mainClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
	    private TableColumn<Book, String> authorcol;
	    

	    @FXML
	    private TableColumn<Book, Integer> catalognumbercol;

	  
	    @FXML
	    private TableColumn<Book, String> locationcol;
	    
	    @FXML
	    private TableColumn<Book, String> returndatecol;
	    
	    private ObservableList<Book> booklist;

	
	   @Override
		public void initialize(URL arg0, ResourceBundle arg1)
	   {
		   namecol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		   authorcol.setCellValueFactory(new PropertyValueFactory<>("Author"));
		   catalognumbercol.setCellValueFactory(new PropertyValueFactory<>("Catalognumber"));
		   locationcol.setCellValueFactory(new PropertyValueFactory<>("Location"));
		   returndatecol.setCellValueFactory(new PropertyValueFactory<>("Returndate"));
		   
		  Book book1 = new Book("Harry Potter" , "J.K.Rolling", 12, "A6 313","14/10/2018");
		  Book book2 = new Book("Kofiko" ,"Galila Ron Feder", 456789, "A8 949","12/3/2019");
		  booklist = FXCollections.observableArrayList(book1,book2);
		  
		   BookTable.setItems(booklist);
		   
		   BookTable.setRowFactory(tv -> {					//press on row in book table to open book information
	           TableRow<Book> row = new TableRow<>();
	           row.setOnMouseClicked(event -> {
	               if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                   Book rowData = row.getItem();
	                   System.out.println("Double click on: "+rowData.getCatalognumber());
	               }
	           });
	           return row ;
	       });
		   
		  
		   
	   }
	   
	  
	  
	

		
		
	  /* @FXML
	    void searchBookDisplay(ActionEvent event) {							//press on search in search book screen
	    	


	    }*/


    

    @FXML
    void backPreviewClick(MouseEvent event) 
    {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	GuiManager.getLoginStage().show();//show login screen

    }

 

}
