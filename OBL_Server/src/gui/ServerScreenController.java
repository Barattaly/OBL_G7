package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import srvrDb.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class ServerScreenController implements Initializable
{
	private OBLServer server;
    @FXML
    private TitledPane _logTitledPane;
	@FXML
	private AnchorPane _startServerAnchorPane;
	@FXML
	private TextField _serverPortTextField;
	@FXML
	private AnchorPane _connectToDBAnchorPane;
	@FXML
	private TextField dbNameTextField;
	@FXML
	private TextField dbUserNameTextField;
	@FXML
	private Circle _serverLedIndicator;
	@FXML
	private Circle 	_dbLedIndicator;
	@FXML
	private TextField dbPassTextField;
	@FXML
	private Button _connectButton;
	@FXML
	private Button _startBtn;
	@FXML
	private TextArea _logTextArea;
	@FXML
	private AnchorPane _mainAnchorPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		dbNameTextField.setText("obl_db");
		dbUserNameTextField.setText("root");
		dbPassTextField.setText("Group7");
	}

	
	@FXML
	void startServerClicked(ActionEvent event)
	{
		if(_startBtn.getText().equals("Stop Server"))
		{
			server.stopListening();
			try {server.close();}
			catch(Exception e) 
			{ 			
				_logTextArea.setText(e.getMessage() + System.lineSeparator() + _logTextArea.getText());
			}
			_serverLedIndicator.setFill(javafx.scene.paint.Color.RED);
			_connectToDBAnchorPane.setVisible(false);
			_startServerAnchorPane.setVisible(true);
			_logTextArea.setText("Server Stopped" + System.lineSeparator() + _logTextArea.getText());
			_startBtn.setText("Start");
			_dbLedIndicator.setFill(javafx.scene.paint.Color.RED);
			_connectToDBAnchorPane.setDisable(false);
			return;
		}
		server = new OBLServer(Integer.parseInt(_serverPortTextField.getText()),_logTextArea);
		try 
		{
			server.listen();
			_startServerAnchorPane.setVisible(false);
			_connectToDBAnchorPane.setVisible(true);
			_logTextArea.setText("Server running on port "+_serverPortTextField.getText() + System.lineSeparator() + _logTextArea.getText());
			_serverLedIndicator.setFill(javafx.scene.paint.Color.GREEN);
			_startBtn.setText("Stop Server");
		}
		catch(Exception ex)
		{
			_logTextArea.setText(ex.getMessage() + System.lineSeparator() + _logTextArea.getText());
			_serverLedIndicator.setFill(javafx.scene.paint.Color.RED);
		}
	}

	@FXML
	void connectBtnClicked(ActionEvent event)
	{
		try
		{
			server.connectToDB( dbNameTextField.getText(), dbPassTextField.getText(),
					dbUserNameTextField.getText());

		} catch (Exception ex)
		{
			_logTextArea.setText(ex.getMessage() + System.lineSeparator() + _logTextArea.getText());
		}

		finally
		{
			if (server.isDBRunning())
			{
				_logTextArea.setText("Database connected successfully!" + System.lineSeparator() + _logTextArea.getText());
				_dbLedIndicator.setFill(javafx.scene.paint.Color.GREEN);
				_connectToDBAnchorPane.setDisable(true);
			}
		}

	}
	
	public void shutDown() 
	{
		try
		{
			if(server != null)
			{
				if(server.isListening())server.stopListening();
				server.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
