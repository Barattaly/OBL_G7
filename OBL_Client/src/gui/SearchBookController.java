package gui;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXTextField;

import client.IClientUI;
import entities.Book;
import entities.BookOrder;
import entities.BorrowACopyOfBook;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import entities.DBMessage.DBAction;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observableEntities.*;

public class SearchBookController implements Initializable, IClientUI
{
	private User userLogged = null;
	
	private Subscriber subscriberLogged = null;
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
	private ImageView refreshBtn;

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
	
	@FXML
	private AnchorPane spinnerAnchorPane;

	@FXML
	private JFXSpinner spinner;
	
	private BookInformationController bookInformationController;
	
	Stage newBookStage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		GuiManager.client.getAllBooks();// fill in the table of books from the updated DB book list
		
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
					openBookWindow(bookMap.get(bookCatNum), getUserLogedIn());
				}
			});
			return row;
		});

	}

	private void openBookWindow(Book book, User userLoged)
	{
		try
		{
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(
					GuiManager.class.getResource("/gui/BookInformationScreen.fxml"));
			Parent root = loader.load();
			bookInformationController = loader.getController();
			bookInformationController.setUserLogedIn(userLoged);
			if(subscriberLogged != null)
			{
				bookInformationController.setSubscriber(subscriberLogged);
			}
			bookInformationController.setBookInformation(book);
			Scene scene = new Scene(root);
			SeondStage.setTitle("Book Page");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.showAndWait();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
				} catch (NullPointerException ex)
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

	@SuppressWarnings("unchecked")
	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetAllBooksList:
		{
			bookMap = (Map<Integer, Book>) msg.Data;
			copyBookMapToBookList();
			spinnerAnchorPane.setVisible(false);
			spinner.setVisible(false);
			break;
		}
		case CreateNewOrder:
		{
			Platform.runLater(() -> {
				bookInformationController.getMessageFromServer(msg);
				refreshBtnClicked(null);
			});
			break;
		}
		case AddBook:
		{
			Platform.runLater(() -> {
				if(((DBMessage)msg).Data == null)
					GuiManager.ShowErrorPopup("Book was not added.\nPlease check the inforamtion you enterd (unique book name and edition , correct year etc...)");
				else
				{
					GuiManager.client.getAllBooks();// fill in the table of books from the updated DB book list
					GuiManager.ShowMessagePopup("The book was added successfully");
					newBookStage.close();
				}
			});
			break;
		}
		case EditBookDetails://data is not null
		{
			Platform.runLater(() -> {
				GuiManager.ShowMessagePopup("This book has been edited successfully!");
				bookInformationController.getStage().close();
				refreshBtnClicked(null);
				//openUpdatedBook(((Book)msg.Data).getCatalogNumber());
			});
			break;
		}
		case CancelOrder:
		{
			Platform.runLater(() -> {
				bookInformationController.getMessageFromServer(msg);
				refreshBtnClicked(null);
			});
			break;
		}
		default:
			break;
		}

	}

	private void openUpdatedBook(String catalogNumber)
	{
		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openBookWindow(bookMap.get(catalogNumber), getUserLogedIn());
	}

	// this function is because of the fucking stupid table view of javaFx
	private void copyBookMapToBookList()//moving from bookMap of"Book" to list of "ObservableBook"
	{
		booklist.clear();//if we dont clear it will just add more and more books. we want to refresh it.
		for (Integer key : bookMap.keySet())
		{
			if (bookMap.get(key).getIsArchived().equals("no"))
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
				boolean isAvailable = bookMap.get(key).getMaxCopies() - bookMap.get(key).getCurrentNumOfBorrows() > 0;
				ObservableBook temp = new ObservableBook(bookMap.get(key).getName(), authors,
						Integer.parseInt(bookMap.get(key).getCatalogNumber()), bookMap.get(key).getLocation(),
						catagories, isAvailable);
				booklist.add(temp);
			}
		}
		BookTable.refresh();
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		this.userLogged = userLoged;
		if (userLoged.getType().equals("librarian") || userLoged.getType().equals("library manager"))
		{
			addNewBookBtn.setVisible(true);
			headlineLabel.setText("Book Inventory");
		} else
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
		spinnerAnchorPane.setVisible(false);
		spinner.setVisible(false);
	}

	// The default is pop up
	public void setPopUpMode(boolean isPopUp)
	{
		if (isPopUp)
		{
			backToLabel.setVisible(true);
			goBackArrowImg.setVisible(true);
			oblLogoImg.setVisible(true);
			oblLogoLabel.setVisible(true);
		} else
		{
			backToLabel.setVisible(false);
			goBackArrowImg.setVisible(false);
			oblLogoImg.setVisible(false);
			oblLogoLabel.setVisible(false);
		}
	}
	public void setSubscriber(Subscriber subscriberLoggedIn)
	{
		subscriberLogged = subscriberLoggedIn;
	}
	
    @FXML
    void refreshBtnClicked(MouseEvent event) 
    {
		GuiManager.client.getAllBooks();// fill in the table of books from the updated DB book list
    }

	@FXML
	void pressRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(0.5);
	}

	@FXML
	void releasedRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(1);
	}
	
    @FXML
    void addNewBookBtnClick(ActionEvent event) 
    {
    	openAddNewBook();
    }
    
    private void openAddNewBook()
	{
		try
		{
			newBookStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(GuiManager.availableFXML.get(SCREENS.addNewBook)));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			newBookStage.setResizable(false);
			newBookStage.setTitle("Add new book");
			newBookStage.getIcons().add(new Image("/resources/Braude.png"));
			newBookStage.setScene(scene);
			newBookStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
