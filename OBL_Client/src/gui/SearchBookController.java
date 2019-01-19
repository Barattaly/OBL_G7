package gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import entities.*;
import gui.GuiManager.SCREENS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import com.jfoenix.controls.JFXRadioButton;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class SearchBookController implements Initializable, IClientUI
{
	@FXML
	private JFXTextField bookNameTextField;

	@FXML
	private JFXTextField authorNameTextfield;

	@FXML
	private JFXTextField bookSubjectTextfield;

	@FXML
	private JFXTextField freeSearchTextfield;

	@FXML
	private TableView<ObservableBook> BookTable;

	@FXML
	private TableColumn<ObservableBook, String> namecol;

	@FXML
	private TableColumn<ObservableBook, String> authorcol;

	@FXML
	private TableColumn<ObservableBook, Integer> catalognumbercol;

	@FXML
	private TableColumn<ObservableBook, String> locationcol;
	
    @FXML
    private JFXRadioButton bookNameRadioBtn;

    @FXML
    private JFXRadioButton authorNameRadioBtn;

    @FXML
    private JFXRadioButton bookSubjectRadioBtn;

    @FXML
    private JFXRadioButton freeTextRadioBtn;
    
    @FXML
    private ToggleGroup radioGroup;


	private ObservableList<ObservableBook> booklist;// for table view...

	private Map<Integer, Book> bookMap;// key = catalog number, value = the book!

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		GuiManager.client.getAllBooks();// fill in the table off books from the updated DB book list
		
		namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
		authorcol.setCellValueFactory(new PropertyValueFactory<>("author"));
		catalognumbercol.setCellValueFactory(new PropertyValueFactory<>("catalognumber"));
		locationcol.setCellValueFactory(new PropertyValueFactory<>("location"));
		booklist = FXCollections.observableArrayList();

		BookTable.setItems(booklist);

		BookTable.setRowFactory(tv -> { // press on row in book table to open book information
			TableRow<ObservableBook> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableBook rowData = row.getItem();
					int bookCatNum = rowData.getCatalognumber();
					GuiManager.openBookWindow(bookMap.get(bookCatNum));
				}
			});
			return row;
		});

	}
    @FXML
    void radioBtnClicked(ActionEvent event) 
    {
    	/*switch(radioGroup.getSelectedToggle().getUserData().toString())
    	{
    	case "Free text":
    		System.out.println("freeee");
    		break;
    	case "Author name":
    		break;
    		
    	}*/
    }

	@FXML
	void searchBookBtnClick(ActionEvent event)
	{
		
		// free
		JFXTextField txtField = freeSearchTextfield;
		ObservableList<ObservableBook> data = booklist;

		if (txtField.textProperty().get().isEmpty())
		{

			BookTable.setItems(data);

			return;
		}

		ObservableList<ObservableBook> itemsAfterFilter = FXCollections.observableArrayList();

		ObservableList<TableColumn<ObservableBook, ?>> cols = BookTable.getColumns();
		try
		{
			for (int i = 0; i < data.size(); i++)
			{
				for (int j = 0; j < cols.size(); j++)
				{

					TableColumn col = cols.get(j);
					String cellValue =	null;
					try
					{
						cellValue = col.getCellData(data.get(i)).toString();
					} catch (NullPointerException ex)
					{
						break;
					}
					cellValue = cellValue.toLowerCase();

					if (cellValue.contains(txtField.textProperty().get().toLowerCase()))
					{

						itemsAfterFilter.add(data.get(i));

						break;

					}

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		BookTable.setItems(itemsAfterFilter);
	}

	@FXML
	void backPreviewClick(MouseEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		GuiManager.SwitchScene(SCREENS.login);// show login screen

	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetAllBooksList:
		{
			bookMap = (Map<Integer, Book>) msg.Data;
			copyBookMapToBookList();
		}
		}

	}

	// this function is because of the fucking stupid table view of javaFx
	private void copyBookMapToBookList()
	{
		for (Integer key : bookMap.keySet())
		{
			String authors = "";
			for (String author : bookMap.get(key).getAuthorNameList())
			{
				if (authors.isEmpty())
					authors = author;
				else
					authors = authors + ", " + author;
			}
			ObservableBook temp = new ObservableBook(bookMap.get(key).getName(), authors,
					Integer.parseInt(bookMap.get(key).getCatalogNumber()), bookMap.get(key).getLocation());
			booklist.add(temp);
		}
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserLogedIn()
	{
		// TODO Auto-generated method stub
		return null;
	}
	


}
