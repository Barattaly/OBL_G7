package gui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import entities.Book;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class AddNewBookController implements Initializable
{

	@FXML
	private TextArea descreptionPane;

	@FXML
	private JFXTextArea bookNameTextArea;

	@FXML
	private JFXTextArea authorTextArea;

	@FXML
	private JFXTextArea categoriesTextArea;


	@FXML
	private JFXTextField publicationYearTextField;

	@FXML
	private JFXTextField editionNumTextField;

	@FXML
	private JFXTextField locationTextField;

	@FXML
	private JFXButton btnAddBook;
   
	@FXML
    private JFXCheckBox wantedBook;

	@FXML
	private Spinner<Integer> copiesSpinner;


	@FXML
	private Label warningLabel;

	protected static final String INITAL_VALUE = "0";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		

		copiesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,
	            Integer.parseInt(INITAL_VALUE)));
		copiesSpinner.setEditable(false);

	    EventHandler<KeyEvent> enterKeyEventHandler;

	    enterKeyEventHandler = new EventHandler<KeyEvent>() {

	        @Override
	        public void handle(KeyEvent event) {

	            // handle users "enter key event"
	            if (event.getCode() == KeyCode.ENTER) {

	                try {
	    // yes, using exception for control is a bad solution ;-)
	            Integer.parseInt(copiesSpinner.getEditor().textProperty().get());
	                }
	                catch (NumberFormatException e) {

	                    // show message to user: "only numbers allowed"
	                	GuiManager.ShowErrorPopup("only numbers allowed");
	                    // reset editor to INITAL_VALUE
	                	copiesSpinner.getEditor().textProperty().set(INITAL_VALUE);
	                }
	            }
	        }
	    };

	    // note: use KeyEvent.KEY_PRESSED, because KeyEvent.KEY_TYPED is to late, spinners
	    // SpinnerValueFactory reached new value before key released an SpinnerValueFactory will
	    // throw an exception
	    copiesSpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);

	}
	
	public static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}

	@FXML
	void btnAddBookClick(ActionEvent event)
	{
		warningLabel.setText("");
		if (bookNameTextArea.getText().isEmpty() || categoriesTextArea.getText().isEmpty()
				 || copiesSpinner.getValue().equals(0) || authorTextArea.getText().isEmpty() )

		{
			warningLabel.setTextFill(Color.RED);
			warningLabel.setText("Please fill all the requierd field.");
			return;
		}
		else
		{
			 Calendar now = Calendar.getInstance();
			 String purchaseDate = getCurrentDateAsString();
			 System.out.println(purchaseDate);
			 
			 List<String> authorNameList = new ArrayList<String>(Arrays.asList(authorTextArea.getText().split(",")));
			 
			 List<String> categories = new ArrayList<String>(Arrays.asList(categoriesTextArea.getText().split(",")));
			 
			 
			/*Book tempbook = New Book(bookNameTextArea.getText(),purchaseDate, authorNameList,
					categories, String publicationYear, String editionNumber ,
					String location,String description,
					int maxCopies,  String classification,String tableOfContenPath);*/
		}

	}

}
