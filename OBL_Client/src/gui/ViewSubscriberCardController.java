package gui;

import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.ActivityLog;
import entities.DBMessage;
import entities.ObservableActivityLog;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewSubscriberCardController implements IClientUI
{

	private Subscriber subscriberToShow;

	private ObservableList<ObservableActivityLog> activitylist;// for table view...

	@FXML
	private TableView<ObservableActivityLog> activityTable;

	@FXML
	private TableColumn<ObservableActivityLog, String> activitycol;

	@FXML
	private TableColumn<ObservableActivityLog, String> booknamecol;

	@FXML
	private TableColumn<ObservableActivityLog, String> datecol;

	@FXML
	private TableColumn<ObservableActivityLog, String> commentscol;

	@FXML
	private JFXTextField subscriberNumberField;

	@FXML
	private JFXTextField idNumberField;

	@FXML
	private JFXTextField firstNameField;

	@FXML
	private JFXTextField lastNameField;

	@FXML
	private JFXTextField phoneNumberField;

	@FXML
	private JFXTextField emailField;

	@FXML
	private JFXTextField userNameField;

	@FXML
	private JFXButton btn_Edit;

	@FXML
	private JFXButton btn_Cancel;

	@FXML
	private JFXButton btn_Save;

	@FXML
	private Label warningLabel;

	@FXML
	private Label SuccessLabel;

	public void setSubscriberToShow(Subscriber sub)
	{
		subscriberToShow = sub;
		subscriberNumberField.setText(subscriberToShow.getSubscriberNumber());
		userNameField.setText(subscriberToShow.getUserName());
		idNumberField.setText(subscriberToShow.getId());
		firstNameField.setText(subscriberToShow.getFirstName());
		lastNameField.setText(subscriberToShow.getLastName());
		phoneNumberField.setText(subscriberToShow.getPhoneNumber());
		emailField.setText(subscriberToShow.getEmail());
		GuiManager.client.getActivityLogFromDB(subscriberToShow.getId());
		
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{


	}

	public void setUserLogedIn(User userLoged)
	{
	}

	@Override
	public User getUserLogedIn()
	{
		return null;
	}

	@FXML
	void btn_EditClick(ActionEvent event)
	{
		GuiManager.preventLettersTypeInTextField(phoneNumberField);

		btn_Edit.setVisible(false);
		btn_Cancel.setVisible(true);
		btn_Save.setVisible(true);

		firstNameField.setEditable(true);
		lastNameField.setEditable(true);
		phoneNumberField.setEditable(true);
		emailField.setEditable(true);

		firstNameField.setCursor(Cursor.TEXT);
		lastNameField.setCursor(Cursor.TEXT);
		phoneNumberField.setCursor(Cursor.TEXT);
		emailField.setCursor(Cursor.TEXT);
	}

	@FXML
	void btn_CancelClick(ActionEvent event)
	{
		//GuiManager.client.getSubscriberFromDB(subscriberToShow.getId());
		//GuiManager.client.getActivityLogFromDB(subscriberToShow.getId());
		
		setSubscriberToShow(subscriberToShow);

		btn_Edit.setVisible(true);
		btn_Cancel.setVisible(false);
		btn_Save.setVisible(false);

		firstNameField.setEditable(false);
		lastNameField.setEditable(false);
		phoneNumberField.setEditable(false);
		emailField.setEditable(false);

		firstNameField.setCursor(Cursor.DEFAULT);
		lastNameField.setCursor(Cursor.DEFAULT);
		phoneNumberField.setCursor(Cursor.DEFAULT);
		emailField.setCursor(Cursor.DEFAULT);

		SuccessLabel.setVisible(false);
		warningLabel.setVisible(false);
	}

	@FXML
	void btn_SaveClick(ActionEvent event)
	{
		if (firstNameField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter first name please");
		} else if (lastNameField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter last name please");
		} else if (phoneNumberField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter phone number please");
		} else if (emailField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter Email please");
		} else if (!GuiManager.isValidEmailAddress(emailField.getText()))
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("The Email is incorrect");
		} else
		{
			btn_Edit.setVisible(true);
			btn_Cancel.setVisible(false);
			btn_Save.setVisible(false);

			firstNameField.setEditable(false);
			lastNameField.setEditable(false);
			phoneNumberField.setEditable(false);
			emailField.setEditable(false);

			firstNameField.setCursor(Cursor.DEFAULT);
			lastNameField.setCursor(Cursor.DEFAULT);
			phoneNumberField.setCursor(Cursor.DEFAULT);
			emailField.setCursor(Cursor.DEFAULT);

			Subscriber subscriberToUpdate = new Subscriber(subscriberToShow.getId(), firstNameField.getText(),
					lastNameField.getText(), phoneNumberField.getText(), emailField.getText(),
					subscriberToShow.getLoginStatus());
			GuiManager.client.updateSubscriberDetails(subscriberToUpdate);
			subscriberToUpdate.setUserName(subscriberToShow.getUserName());
			String name = subscriberToUpdate.getFirstName().substring(0, 1).toUpperCase()
					+ subscriberToUpdate.getFirstName().substring(1);

			SuccessLabel.setVisible(true);
			warningLabel.setVisible(false);
			SuccessLabel.setText("Changes saved successfully");
		}
	}

	public JFXTextField getFirstNameField()
	{
		return firstNameField;
	}

	public void setActivityLogList(ArrayList<ActivityLog> activityList)
	{

		activitycol.setCellValueFactory(new PropertyValueFactory<>("activity"));
		booknamecol.setCellValueFactory(new PropertyValueFactory<>("bookname"));
		datecol.setCellValueFactory(new PropertyValueFactory<>("date"));
		commentscol.setCellValueFactory(new PropertyValueFactory<>("comments"));
		activitylist = FXCollections.observableArrayList();

		for (ActivityLog activity : activityList)
		{
			if (activity.getActivity().equals("Return") && activity.getDate() == null)
				continue;
			else if (activity.getActivity().equals("Return") && activity.getComments().equals("no"))
				activity.setComments("The book was returned late");
			else if (activity.getActivity().equals("Return") && activity.getComments().equals("yes"))
				activity.setComments("The book was returned on time");

			if (activity.getActivity().equals("Order"))
				activity.setComments("Order status: " + activity.getComments());

			ObservableActivityLog temp = new ObservableActivityLog(activity.getActivity(), activity.getBookname(),
					activity.getDate(), activity.getComments());
			activitylist.add(temp);
			activityTable.getSortOrder().add(datecol);
			activityTable.setItems(activitylist);
		}
	}

}
