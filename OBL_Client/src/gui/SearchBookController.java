package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import entities.*;
import gui.GuiManager.SCREENS;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class SearchBookController implements Initializable, IClientUI
{

	@FXML
	private JFXTextField bookNameTextField;

	@FXML
	private JFXTextField authorNameTextfield;

	@FXML
	private JFXTextField bookSubjectTextfield;

	@FXML
	private JFXTextField freeSearchTextfield;

	@FXML
	private TableView<ObservableBook> BookTable;

	@FXML
	private TableColumn<ObservableBook, String> namecol;

	@FXML
	private TableColumn<ObservableBook, String> authorcol;

	@FXML
	private TableColumn<ObservableBook, Integer> catalognumbercol;

	@FXML
	private TableColumn<ObservableBook, String> locationcol;

	@FXML
	private TableColumn<ObservableBook, String> returndatecol;

	private ObservableList<ObservableBook> booklist;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
		namecol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		authorcol.setCellValueFactory(new PropertyValueFactory<>("Author"));
		catalognumbercol.setCellValueFactory(new PropertyValueFactory<>("Catalognumber"));
		locationcol.setCellValueFactory(new PropertyValueFactory<>("Location"));
		returndatecol.setCellValueFactory(new PropertyValueFactory<>("Returndate"));

		ObservableBook book1 = new ObservableBook("Harry Potter", "J.K.Rolling", 12, "A6 313", "14/10/2018");
		ObservableBook book2 = new ObservableBook("Kofiko", "Galila Ron Feder", 456789, "A8 949", "12/3/2019");
		booklist = FXCollections.observableArrayList(book1, book2);

		BookTable.setItems(booklist);

		BookTable.setRowFactory(tv -> { // press on row in book table to open book information
			TableRow<ObservableBook> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableBook rowData = row.getItem();
					System.out.println("Double click on: " + rowData.getCatalognumber());
				}
			});
			return row;
		});

	}

	@FXML
	void searchBookBtnClick(ActionEvent event)
	{
		
	}

	@FXML
	void backPreviewClick(MouseEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.login);// show login screen

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch(msg.Action)
		{
			case GetAllBooksList:
			{
				
			}
		}

	}

	@Override
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
