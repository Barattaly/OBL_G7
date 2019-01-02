package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.LibrarianModel;
import entities.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class PrototypeGUIController implements Initializable
{
	private LibrarianModel librarianDB;
	private String ServerIp = null;
	private String newStatus;
	private EventHandler<MouseEvent> DoubleClickOnTable = new EventHandler<MouseEvent>()
	{

		@Override
		public void handle(MouseEvent event)
		{
			if (librarianDB == null)
				return;
			if (event.getClickCount() == 2 && !studentList.isEmpty())
			{
				if(studentTable.getSelectionModel().getSelectedItem() == null) return;
				selectionPropmt();
				librarianDB.updateStudentStatus(studentTable.getSelectionModel().getSelectedItem(), newStatus);
				studentTable.getSelectionModel().getSelectedItem().StatusMembership = new SimpleStringProperty(
						newStatus);
				studentTable.refresh();
			}
		}
	};
	
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
	private Label msgLabel;

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
		options.getItems().addAll("Active", "Frozen", "Locked", "NotRegistered");
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
		dialogVbox.getChildren().addAll(headline, options, button);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			getServerIp();//Open window asking for the server IP
			librarianDB = new LibrarianModel(ServerIp, LibrarianModel.DEFAULT_PORT);
			librarianDB.updateStudentListFromDB(studentList);
			studentTable.setItems(studentList);

		} catch (Exception e)
		{
			msgLabel.setText("Error Connecting to Server");
			msgLabel.setTextFill(Paint.valueOf("Red"));
		}
		studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
		studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
		studentStatusMembership.setCellValueFactory(new PropertyValueFactory<>("StatusMembership"));
		studentTable.setOnMouseClicked(DoubleClickOnTable);
	}

	private void getServerIp()
	{
		final Stage dialog = new Stage();
		dialog.setOnCloseRequest(new EventHandler<WindowEvent>()
		{

			@Override
			public void handle(WindowEvent arg0)
			{
				System.exit(0);
			}
		});
		dialog.initStyle(StageStyle.DECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle("Connect to Server");
		VBox dialogVbox = new VBox(10);
		Label headline = new Label("Enter Server IP");
		headline.setFont(new Font(25));
		Label warning = new Label("(Make sure you are on the same lan)");
		warning.setFont(new Font(12));
		dialogVbox.setAlignment(Pos.CENTER);
		javafx.scene.control.TextField ipTextField = new javafx.scene.control.TextField("Example: 162.123.1.206");
		ipTextField.setMaxWidth(140);
		Button button = new Button("OK");
		button.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				if (ipTextField.getText().isEmpty() || ipTextField.getText().contains("Example: 162.123.1.206"))
					return;
				ServerIp = ipTextField.getText();
				dialog.close();
			}
		});
		dialogVbox.getChildren().addAll(headline, warning, ipTextField, button);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}

	public ObservableList<Student> getStudentList()
	{
		return studentList;
	}

	public void setStudentList(ObservableList<Student> studentList)
	{
		this.studentList = studentList;
	}

}
