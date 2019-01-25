package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import entities.Book;
import entities.DBMessage;
import entities.Employee;
import entities.ObservableBook;
import entities.ObservableBorrow;
import entities.ObservableEmployee;
import entities.Subscriber;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LibrarianManagerController extends LibrarianScreenController
{
	@FXML
	private TableView<ObservableEmployee> emplyeeTableView;

	@FXML
	private TableColumn<ObservableEmployee, String> empNumColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empIDColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empFirstNameColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empLastNameColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empEmailColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empRoleColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empDepartmentColumn;

	private ObservableList<ObservableEmployee> empList;// for table view...

	@FXML
	private Pane pane_employees,pane_reports;

	@FXML
	private ImageView btn_employees,	btn_reports;

	@FXML
	private JFXTextField searchTextField;//Search employee
	
	@FXML
	private ImageView refreshBtn;
	
	// this is the search function, it is in listener for text inside the textfield
	private InvalidationListener onSearchStart = new InvalidationListener()
	{

		@Override
		public void invalidated(Observable arg0)
		{

			if (searchTextField.textProperty().get().isEmpty())
			{

				emplyeeTableView.setItems(empList);

				return;

			}

			ObservableList<ObservableEmployee> tableItems = FXCollections.observableArrayList();

			ObservableList<TableColumn<ObservableEmployee, ?>> cols = emplyeeTableView.getColumns();

			for (int i = 0; i < empList.size(); i++)
			{

				for (int j = 0; j < cols.size(); j++)
				{

					TableColumn col = cols.get(j);
					String cellValue = null;
					try
					{
						cellValue = col.getCellData(empList.get(i)).toString();
					} catch (NullPointerException ex)
					{
						break;
					}

					cellValue = cellValue.toLowerCase();

					if (cellValue.contains(searchTextField.textProperty().get().toLowerCase()))
					{

						tableItems.add(empList.get(i));

						break;

					}

				}

			}

			emplyeeTableView.setItems(tableItems);
		}
	};
	// private SearchBookController searchBookWindowController = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		super.initialize(arg0, arg1);
		GuiManager.client.getEmployeeList();
		searchTextField.textProperty().addListener(onSearchStart);
		
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);

		empNumColumn.setCellValueFactory(new PropertyValueFactory<>("empNumber"));
		empIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		empFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		empLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		empEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		empRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
		empDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

		empList = FXCollections.observableArrayList();

	}

	@FXML
	protected void btn_homeDisplay(MouseEvent event)
	{
		super.btn_homeDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_createNewSubscriberCardDisplay(MouseEvent event)
	{
		super.btn_createNewSubscriberCardDisplay(event);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_booksDisplay(MouseEvent event)
	{
		super.btn_booksDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_searchSubscriberCardDisplay(MouseEvent event)
	{
		super.btn_searchSubscriberCardDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	void btn_employeesDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		pane_employees.setVisible(true);
		pane_reports.setVisible(false);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);
		btn_employees.setOpacity(0.5);
		btn_reports.setOpacity(1);
	}

	@FXML
	void btn_reportsDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		pane_employees.setVisible(false);
		pane_reports.setVisible(true);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(0.5);
	}


	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetEmployeeList:
		{
			updateEmpList((ArrayList<Employee>) msg.Data);
			break;
		}
		default:
			super.getMessageFromServer(msg);
		}
	}

	private void updateEmpList(ArrayList<Employee> data)
	{
		empList.clear();
		for (Employee employee : data)
		{
			empList.add(new ObservableEmployee(employee.getEmpNumber(), employee.getId(), employee.getFirstName(),
					employee.getLastName(), employee.getEmail(), employee.getRole(), employee.getDepartment()));
		}
		emplyeeTableView.setItems(empList);
	}
	
    @FXML
    void refreshBtnClicked(MouseEvent event) 
    {
		GuiManager.client.getEmployeeList();
    }

	@FXML
	void pressRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(0.5);
	}

	@FXML
	void releasedRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(1);
	}

}
