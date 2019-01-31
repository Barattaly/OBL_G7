package gui;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import entities.SendEmail;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import srvrDb.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
    @FXML
    private ProgressIndicator loadingSpinner;
    @FXML
    private TextField serverIPLabel;

	
	private AutomaticExecutors executer = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			try(final DatagramSocket socket = new DatagramSocket())
			{
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				serverIPLabel.setText(socket.getLocalAddress().getHostAddress());
			}
			loadingSpinner.setVisible(false);
			java.util.List<String> password = Files.readAllLines(Paths.get("dbPass.txt"));
			dbPassTextField.setText(password.get(0));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dbPassTextField.setText("Group7");

		}
		dbNameTextField.setText("obl_db");
		dbUserNameTextField.setText("root");
	}

	@FXML
	void startServerClicked(ActionEvent event)
	{
		loadingSpinner.setVisible(true);
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
			_logTextArea.setText("All clients disconnected" + System.lineSeparator() + _logTextArea.getText());
			_startBtn.setText("Start");
			_dbLedIndicator.setFill(javafx.scene.paint.Color.RED);
			_connectToDBAnchorPane.setDisable(false);
			loadingSpinner.setVisible(false);
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
			connectBtnClicked(event);//NEED TO BE CHANGED
		}
		catch(Exception ex)
		{
			_logTextArea.setText(ex.getMessage() + System.lineSeparator() + _logTextArea.getText());
			_serverLedIndicator.setFill(javafx.scene.paint.Color.RED);
		}
		finally
		{
			loadingSpinner.setVisible(false);
		}

	}

	@FXML
	void connectBtnClicked(ActionEvent event)
	{
		try
		{
			loadingSpinner.setVisible(true);
			server.connectToDB( dbNameTextField.getText(), dbPassTextField.getText(),
					dbUserNameTextField.getText());
			executer = new AutomaticExecutors(server.getConnection());
		
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
			loadingSpinner.setVisible(false);
		}

	}
	
	public void shutDown() 
	{
		try
		{
			loadingSpinner.setVisible(true);
			if(server != null)
			{
				
				if(server.isListening())server.stopListening();
				server.close();		
			}
			if(executer!=null) executer.shutDown();
			loadingSpinner.setVisible(false);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
