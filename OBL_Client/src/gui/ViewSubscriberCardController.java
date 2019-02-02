package gui;

import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import client.IClientUI;
import entities.ActivityLog;
import entities.DBMessage;
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
import observableEntities.ObservableActivityLog;

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

	@FXML
	private JFXTextField subscriberStatusField;

	@FXML
	private JFXToggleButton freezeSubscriberToggleButton;

	private User userLogged;

	public void setSubscriberToShow(Subscriber sub)
	{
		subscriberToShow = sub;
		subscriberStatusField.setText(subscriberToShow.getStatus());
		subscriberNumberField.setText(subscriberToShow.getSubscriberNumber());
		userNameField.setText(subscriberToShow.getUserName());
		idNumberField.setText(subscriberToShow.getId());
		firstNameField.setText(subscriberToShow.getFirstName());
		lastNameField.setText(subscriberToShow.getLastName());
		phoneNumberField.setText(subscriberToShow.getPhoneNumber());
		emailField.setText(subscriberToShow.getEmail());
		GuiManager.client.getActivityLogFromDB(subscriberToShow.getId());
		if (sub.getStatus().equals("deep freeze"))
		{
			freezeSubscriberToggleButton.setSelected(true);
		} else
			freezeSubscriberToggleButton.setSelected(false);

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{

	}

	public void setUserLogedIn(User userLoged)
	{
		this.userLogged = userLoged;
	}

	@Override
	public User getUserLogedIn()
	{
		return userLogged;
	}

	@FXML
	void btn_EditClick(ActionEvent event)
	{
		GuiManager.preventLettersTypeInTextField(phoneNumberField);
		GuiManager.limitTextFieldMaxCharacters(phoneNumberField, 10);

		btn_Edit.setVisible(false);
		btn_Cancel.setVisible(true);
		btn_Save.setVisible(true);
		if (getUserLogedIn() != null)
		{
			if (getUserLogedIn().getType().equals("library manager"))
			{
				freezeSubscriberToggleButton.setVisible(true);
				if (subscriberToShow.getStatus().equals("locked"))
				{
					freezeSubscriberToggleButton.setDisable(true);
				} else
					freezeSubscriberToggleButton.setDisable(false);
			}
		} else
		{
			freezeSubscriberToggleButton.setVisible(false);
		}

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

		freezeSubscriberToggleButton.setVisible(false);
	}

	@FXML
	void btn_SaveClick(ActionEvent event)
	{
		if (firstNameField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter first name please");
			return;
		}
		if (lastNameField.getText().isEmpty())
		{
			SuccessLabel.setVisible(false);
			warningLabel.setVisible(true);
			warningLabel.setText("Enter last name please");
		}
		if (!emailField.getText().isEmpty())
		{
			if (!GuiManager.isValidEmailAddress(emailField.getText()))
			{
				SuccessLabel.setVisible(false);
				warningLabel.setVisible(true);
				warningLabel.setText("The Email is incorrect");
				return;
			}

		}
		if(!phoneNumberField.getText().isEmpty())
		{
			if(phoneNumberField.getText().length()<10)
			{
				SuccessLabel.setVisible(false);
				warningLabel.setVisible(true);
				warningLabel.setText("The phone number is too short");
				return;
			}		
		}
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

		String status;
		String oldStatus = subscriberToShow.getStatus();
		if (getUserLogedIn().getType().equals("library manager"))
		{

			if (freezeSubscriberToggleButton.isSelected())
			{
				status = "deep freeze";
			} else
			{
				status = "active";
			}

		} else
			status = oldStatus;

		Subscriber subscriberToUpdate = new Subscriber(subscriberToShow.getId(), firstNameField.getText(),
				lastNameField.getText(), phoneNumberField.getText(), emailField.getText(), status);
		GuiManager.client.updateSubscriberDetails(subscriberToUpdate);
		subscriberToUpdate.setUserName(subscriberToShow.getUserName());
		String name = subscriberToUpdate.getFirstName().substring(0, 1).toUpperCase()
				+ subscriberToUpdate.getFirstName().substring(1);

		SuccessLabel.setVisible(true);
		warningLabel.setVisible(false);
		SuccessLabel.setText("Changes saved successfully");
		freezeSubscriberToggleButton.setVisible(false);
		GuiManager.client.getSubscriberFromDB(subscriberToShow.getId());

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

	public void setEditMode(boolean bool)
	{
		if (bool)
		{
			btn_EditClick(null);
		}

	}

}
