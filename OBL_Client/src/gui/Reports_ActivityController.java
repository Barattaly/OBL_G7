package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
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

	private Report_Activity reportData;
    @FXML
    private JFXButton saveBtn;


	public void setReportInformation(Report_Activity info)
	{
		reportData = info;
		dateTextField.setText(String.valueOf(info.getReportDate()));
		activeSubTextField.setText(String.valueOf(info.getActiveSubscribersNumber()));
		lockedSubTextField.setText(String.valueOf(info.getLockedSubscribersNumber()));
		frozenSubTextField.setText(String.valueOf(info.getFrozenSubscribersNumber()));
		totalSubTextField.setText(String.valueOf(info.getTotalNumberOfSubscribers()));
		currentBorrowsTextField.setText(String.valueOf(info.getCurrentNumOfBorrows()));
		lateSubTextField.setText(String.valueOf(info.getNumOfLateSubscribers()));
	}

	@FXML
	void saveReportClicked(ActionEvent event)
	{
		if (reportData == null)
			return;
		GuiManager.client.addReport(reportData);
	}
	
	public void setSaveVisible(boolean flag)
	{
		saveBtn.setVisible(flag);
	}

}
