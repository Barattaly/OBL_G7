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

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SearchBookController implements Initializable, IClientUI
{
	private User userLogged;
	@FXML
	private JFXTextField bookNameTextField;

	@FXML
	private JFXTextField authorNameTextfield;

	@FXML
	private JFXTextField bookCatagoriesTextfield;

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
	private TableColumn<ObservableBook, String> catagoriesCol;
	@FXML
	private TableColumn<ObservableBook, String> availableCol;

	@FXML
	private JFXRadioButton bookNameRadioBtn;

	@FXML
	private JFXRadioButton authorNameRadioBtn;

	@FXML
	private JFXRadioButton bookCatagoriesRadioBtn;

	@FXML
	private JFXRadioButton freeTextRadioBtn;

	@FXML
	private ToggleGroup radioGroup;

	private ObservableList<ObservableBook> booklist;// for table view...

	private Map<Integer, Book> bookMap;// key = catalog number, value = the book!

	@FXML
	private Label backToLabel;

	@FXML
	private ImageView goBackArrowImg;

	@FXML
	private ImageView oblLogoImg;

	@FXML
	private Label oblLogoLabel;

    @FXML
    private JFXButton addNewBookBtn;
    
	@FXML
	private Label headlineLabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		GuiManager.client.getAllBooks();// fill in the table off books from the updated DB book list
		
		namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
		authorcol.setCellValueFactory(new PropertyValueFactory<>("author"));
		catalognumbercol.setCellValueFactory(new PropertyValueFactory<>("catalognumber"));
		locationcol.setCellValueFactory(new PropertyValueFactory<>("location"));
		catagoriesCol.setCellValueFactory(new PropertyValueFactory<>("catagories"));
		availableCol.setCellValueFactory(new PropertyValueFactory<>("isAvailableToBorrow"));
		
		booklist = FXCollections.observableArrayList();

		BookTable.setItems(booklist);

		BookTable.setRowFactory(tv -> { // press on row in book table to open book information
			TableRow<ObservableBook> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableBook rowData = row.getItem();
					int bookCatNum = rowData.getCatalognumber();
					GuiManager.openBookWindow(bookMap.get(bookCatNum), getUserLogedIn());
				}
			});
			return row;
		});

	}

	@FXML
	void radioBtnClicked(ActionEvent event)
	{
		switch (((JFXRadioButton) radioGroup.getSelectedToggle()).getText())
		{
		case "Free text:":
			bookNameTextField.setDisable(true);
			bookCatagoriesTextfield.setDisable(true);
			authorNameTextfield.setDisable(true);
			freeSearchTextfield.setDisable(false);
			break;
		case "Book name:":
			bookNameTextField.setDisable(false);
			bookCatagoriesTextfield.setDisable(true);
			authorNameTextfield.setDisable(true);
			freeSearchTextfield.setDisable(true);
			break;
		case "Author name:":
			bookNameTextField.setDisable(true);
			bookCatagoriesTextfield.setDisable(true);
			authorNameTextfield.setDisable(false);
			freeSearchTextfield.setDisable(true);
			break;
		case "Book catagory:":
			bookNameTextField.setDisable(true);
			bookCatagoriesTextfield.setDisable(false);
			authorNameTextfield.setDisable(true);
			freeSearchTextfield.setDisable(true);
			break;
		}
	}

	@FXML
	void searchBookBtnClick(ActionEvent event)
	{
		if (!bookNameTextField.isDisable())
		{
			searchBook(namecol, bookNameTextField);

		} else if (!bookCatagoriesTextfield.isDisable())
		{
			searchBook(catagoriesCol, bookCatagoriesTextfield);

		} else if (!authorNameTextfield.isDisable())
		{
			searchBook(authorcol, authorNameTextfield);

		} else
		{
			searchByFreeText();
		}

	}

	private void searchBook(TableColumn<ObservableBook, String> col, JFXTextField txtField)
	{
		ObservableList<ObservableBook> data = booklist;

		if (txtField.textProperty().get().isEmpty())
		{

			BookTable.setItems(data);

			return;
		}

		ObservableList<ObservableBook> itemsAfterFilter = FXCollections.observableArrayList();

		try
		{
			for (int i = 0; i < data.size(); i++)
			{

				String cellValue = null;
				try
				{
					cellValue = col.getCellData(data.get(i)).toString();
				} 
				catch (NullPointerException ex)
				{
					break;
				}
				cellValue = cellValue.toLowerCase();

				if (cellValue.contains(txtField.textProperty().get().toLowerCase()))
				{
					itemsAfterFilter.add(data.get(i));
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		BookTable.setItems(itemsAfterFilter);

	}

	private void searchByFreeText()
	{
		ObservableList<ObservableBook> data = booklist;

		if (freeSearchTextfield.textProperty().get().isEmpty())
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
					String cellValue = null;
					try
					{
						cellValue = col.getCellData(data.get(i)).toString();
					} catch (NullPointerException ex)
					{
						break;
					}
					cellValue = cellValue.toLowerCase();

					if (cellValue.contains(freeSearchTextfield.textProperty().get().toLowerCase()))
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
		default:
			break;
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
			String catagories = "";
			for (String catagory : bookMap.get(key).getCategories())
			{
				if (catagories.isEmpty())
					catagories = catagory;
				else
					catagories = catagories + ", " + catagory;
			}
			boolean isAvailable=  bookMap.get(key).getMaxCopies() - bookMap.get(key).getCurrentNumOfBorrows() >0;
			ObservableBook temp = new ObservableBook(bookMap.get(key).getName(), authors,
					Integer.parseInt(bookMap.get(key).getCatalogNumber()), 
					bookMap.get(key).getLocation(), catagories,isAvailable);
			booklist.add(temp);
		}
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		this.userLogged = userLoged;
		if(userLoged.getType().equals("librarian")||userLoged.getType().equals("library manager"))
		{
			addNewBookBtn.setVisible(true);
			headlineLabel.setText("Book Inventory");
		}
		else
		{
			addNewBookBtn.setVisible(false);
			headlineLabel.setText("Search Book");
		}
	}

	@Override
	public User getUserLogedIn()
	{
		return userLogged;
	}

	public Map<Integer, Book> getBookMap()
	{
		return bookMap;
	}

	public void setBookMap(Map<Integer, Book> bookMap)
	{
		this.bookMap = bookMap;
		copyBookMapToBookList();
		BookTable.refresh();
	}

	// The default is pop up
	public void setPopUpMode(boolean isPopUp)
	{
		if(isPopUp)
		{
			backToLabel.setVisible(true);
		    goBackArrowImg.setVisible(true);
		    oblLogoImg.setVisible(true);
		    oblLogoLabel.setVisible(true);
		}
		else
		{
			backToLabel.setVisible(false);
		    goBackArrowImg.setVisible(false);
		    oblLogoImg.setVisible(false);
		    oblLogoLabel.setVisible(false);
		}
	}
}
