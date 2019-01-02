package gui;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.LibrarianModel;
import defaultPackage.mainClient;
import entities.Book;
import entities.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;




public class LibrarianScreenController implements Initializable
{
    LibrarianModel librarianDB= new LibrarianModel("localhost",LibrarianModel.DEFAULT_PORT);
    private String newStatus; 
    @FXML
    private GridPane Grid;

    @FXML
    private Button Create;

    @FXML
    private GridPane Grid2;

    @FXML
    private Button Boorow;

    @FXML
    private GridPane Grid1;

    @FXML
    private Button Search;
    
    @FXML
    private TableView<Student> studentTable;
    
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, Integer> studentID;

    @FXML
    private TableColumn<Student, String> studentStatusMembership;
    
    @FXML
    private TableView<Book> Table1;

    @FXML
    private TableColumn<Book, String> namecol1;

    @FXML
    private TableColumn<Book, Integer> catalogynumbercol1;

    @FXML
    private TableColumn<Book, String> authorcol1;

    @FXML
    private TableColumn<Book, Integer> numbercol1;

    @FXML
    private TableColumn<Book, String> subjectcol1;

    @FXML
    private TableColumn<Book, String> locationcol1;
    
    /////////////////////////////////////////////////
    
    @FXML
    private TableView<Book> Table2;

    @FXML
    private TableColumn<Book, String> namecol2;

    @FXML
    private TableColumn<Book, Integer> catalogynumbercol2;

    @FXML
    private TableColumn<Book, String> authorcol2;

    @FXML
    private TableColumn<Book, Integer> numbercol2;

    @FXML
    private TableColumn<Book, String> subjectcol2;

    @FXML
    private TableColumn<Book, String> locationcol2;
    
    
	private void selectionPropmt() 
	{
		final Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UNDECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		VBox dialogVbox = new VBox(10);
		Label headline = new Label("Select Status");
		headline.setFont(new Font(30));		
		dialogVbox.setAlignment(Pos.CENTER);
        ComboBox<String> options = new ComboBox<String>();
        options.getItems().addAll("Active","Frozen","Locked","NotRegistered");
        options.getSelectionModel().selectFirst();
        Button button = new Button("Select");
        button.setOnMouseClicked(new EventHandler<Event>()
        {
			@Override
			public void handle(Event e) 
			{
	            newStatus = options.getSelectionModel().getSelectedItem().toString();
				dialog.close();
			}
		});
        dialogVbox.getChildren().addAll(headline,options,button);	
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
	}
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) 
   {																				//open screen table:
    	   namecol1.setCellValueFactory(new PropertyValueFactory<>("Name"));
		   catalogynumbercol1.setCellValueFactory(new PropertyValueFactory<>("Catalogynumber"));
		   authorcol1.setCellValueFactory(new PropertyValueFactory<>("Author"));
		   numbercol1.setCellValueFactory(new PropertyValueFactory<>("Numberofcopys"));
		   subjectcol1.setCellValueFactory(new PropertyValueFactory<>("Subject"));
		   locationcol1.setCellValueFactory(new PropertyValueFactory<>("Location"));
		   
		  Book book1 = new Book("Harry Potter", 123456, "J.K.Rolling", 7, "Adventure", "A6 313");
		  
		  ObservableList<Book> list = FXCollections.observableArrayList(book1);
		   Table1.setItems(list);
		   
		   //////////
		   studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
		   studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
		   studentStatusMembership.setCellValueFactory(new PropertyValueFactory<>("StatusMembership"));
		   studentTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) 
			{
				if(event.getClickCount()==2)
				{
					selectionPropmt();
					librarianDB.updateStudentStatus(studentTable.getSelectionModel().getSelectedItem(),newStatus);
					studentTable.getSelectionModel().getSelectedItem().StatusMembership = new SimpleStringProperty(newStatus);
					studentTable.refresh();
				}
			}
		});
		   /*studentStatusMembership.setOnEditStart(new EventHandler<TableColumn.CellEditEvent<Student,String>>() {
			
			@Override
			public void handle(CellEditEvent<Student, String> event) 
			{
				try
				{
					selectionPropmt();
					librarianDB.updateStudentStatus(event.getRowValue(),newStatus);
					event.getRowValue().StatusMembership = new SimpleStringProperty(newStatus);
				}
				catch(Exception e) 
				{
					e.printStackTrace();
				}
			}

		
		});*/

		   librarianDB.updateStudentListFromDB(studentList);
		   studentTable.setItems(studentList);
		   
		   
		   //////////////
		   																			//search book screen table:
    	   namecol2.setCellValueFactory(new PropertyValueFactory<>("Name"));
		   catalogynumbercol2.setCellValueFactory(new PropertyValueFactory<>("Catalogynumber"));
		   authorcol2.setCellValueFactory(new PropertyValueFactory<>("Author"));
		   numbercol2.setCellValueFactory(new PropertyValueFactory<>("Numberofcopys"));
		   subjectcol2.setCellValueFactory(new PropertyValueFactory<>("Subject"));
		   locationcol2.setCellValueFactory(new PropertyValueFactory<>("Location"));
		   
		  Book book2 = new Book("Harry Potter", 123456, "J.K.Rolling", 7, "Adventure", "A6 313");
		  
		  ObservableList<Book> list2 = FXCollections.observableArrayList(book2);
		  
		   Table2.setItems(list2);
		   
    	
   }
    @FXML
    void CreateSubscriberCardDisplay(ActionEvent event) 
    {   //press on create subscriber card
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("OBL Create new subscriber");
    	alert.setHeaderText("The subscriber's card create succefully");
    	alert.showAndWait();
    }
    
    @FXML
    void SearchBookDisplay(ActionEvent event) 
    {							//press on search in search book screen
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
    void LogOutDisplay(MouseEvent event) {			//logout
    	
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 mainClient.getLoginStage().show();				//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }
    	

    }

    @FXML
    void LogOutImageDisplay(MouseEvent event) {					//logout
      	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("OBL Log Out");
    	alert.setHeaderText("Are you sure you want to log out ?");
    	 Optional<ButtonType> option = alert.showAndWait();
     if (option.get() == ButtonType.OK) {
    	 ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	 mainClient.getLoginStage().show();				//show login screen
     }
        
      else if (option.get() == ButtonType.CANCEL) {
    	  alert.close();
    	
      }

    }
	public ObservableList<Student> getStudentList() {
		return studentList;
	}
	public void setStudentList(ObservableList<Student> studentList) {
		this.studentList = studentList;
	}


}
