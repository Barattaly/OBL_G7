package gui;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import entities.Report_Activity;

public class Reports_ActivityController
{
    @FXML
    private JFXTextField activeSubTextField;

    @FXML
    private JFXTextField lockedSubTextField;

    @FXML
    private JFXTextField frozenSubTextField;

    @FXML
    private JFXTextField totalSubTextField;

    @FXML
    private JFXTextField currentBorrowsTextField;

    @FXML
    private JFXTextField lateSubTextField;

    @FXML
    private JFXTextField dateTextField;

	public void setReportInformation(Report_Activity info)
	{
		dateTextField.setText(String.valueOf(info.getReportDate()));
		activeSubTextField.setText(String.valueOf(info.getActiveSubscribersNumber()));
		lockedSubTextField.setText(String.valueOf(info.getLockedSubscribersNumber()));
		frozenSubTextField.setText(String.valueOf(info.getFrozenSubscribersNumber()));
		totalSubTextField.setText(String.valueOf(info.getTotalNumberOfSubscribers()));
		currentBorrowsTextField.setText(String.valueOf(info.getCurrentNumOfBorrows()));
		lateSubTextField.setText(String.valueOf(info.getNumOfLateSubscribers()));
	}

}

