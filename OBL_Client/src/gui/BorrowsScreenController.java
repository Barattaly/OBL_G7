package gui;

import java.util.List;
import java.util.Map;

import entities.Book;
import entities.BorrowACopyOfBook;
import entities.DBMessage;
import entities.ObservableBorrow;
import entities.ObservableEmployee;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BorrowsScreenController implements IClientUI
{
	//NEED TO ADD REFRESH FUNCTION
	
	User userLoggedIn;
	@FXML
	private TableView<ObservableBorrow> borrowsTable;

	@FXML
	private TableColumn<ObservableBorrow, String> borrowNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> borrowDateColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> returnDateColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> copyNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> catalogNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> subscriberIDColumn;

	private ObservableList<ObservableBorrow> observableBorrowsList;// for table view...

	
	@Override
	public User getUserLogedIn()
	{
		return userLoggedIn;
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		userLoggedIn = userLoged;
		switch (userLoggedIn.getType())
		{
		case "subscriber":
			initialSubscriberView();
			break;
		case "librarian":
		case "library manager":
			 initialLibrarianView();
			break;
		}
	}

	private void initialLibrarianView()
	{
		borrowNumberColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
		borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		copyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		catalogNumberColumn.setCellValueFactory(new PropertyValueFactory<>("catalogNumber"));
		subscriberIDColumn.setCellValueFactory(new PropertyValueFactory<>("subscriberId"));
		
		observableBorrowsList = FXCollections.observableArrayList();
		GuiManager.client.getAllCurrentBorrows();		
	}

	private void initialSubscriberView()
	{
		borrowNumberColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
		borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		copyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		catalogNumberColumn.setCellValueFactory(new PropertyValueFactory<>("catalogNumber"));
		subscriberIDColumn.setVisible(false);
		
		observableBorrowsList = FXCollections.observableArrayList();
		GuiManager.client.getCurrentBorrowsForSubscriberID(userLoggedIn.getId());
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetCurrentBorrowsForSubID:case GetCurrentBorrows:
		{
			updateBorrowTable((List<BorrowACopyOfBook>)msg.Data);
		}
		default:
			break;
		}
	}

	private void updateBorrowTable(List<BorrowACopyOfBook> borrowList)
	{
		for(BorrowACopyOfBook borrow: borrowList)
		{
			ObservableBorrow temp = new ObservableBorrow(borrow.getId(), borrow.getBorrowDate(),
					borrow.getExpectedReturnDate(), borrow.getCopyId(), borrow.getBookCatalogNumber(),
					borrow.getSubscriberId());
			observableBorrowsList.add(temp);
		}
		borrowsTable.setItems(observableBorrowsList);
	}

}
