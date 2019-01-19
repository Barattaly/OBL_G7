package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;

public class ViewSubscriberCardController implements IClientUI
{

	private Subscriber subscriberToShow;

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

}
