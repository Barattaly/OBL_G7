package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jfoenix.controls.JFXTextField;

import entities.ObservableBook;
import entities.Report_BorrowDurationInfo;
import entities.Report_LateReturns;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Reports_LateReturnsController
{
	@FXML
	private ScrollPane mainScrollPane;
	@FXML
	private JFXTextField avrgNumberLateTextField;

	@FXML
	private JFXTextField medianNumberLateTextField;

	@FXML
	private BarChart<?, ?> numberOfLateChart;

	@FXML
	private JFXTextField avrgDurationLateTextField;

	@FXML
	private JFXTextField medianDurationLateTextField;

	@FXML
	private BarChart<?, ?> durationOfLateChart;

	@FXML
	private TableView<ObservableReportRow> booksReportInfoTableview;

	@FXML
	private TableColumn<ObservableReportRow, String> catalogColumn;

	@FXML
	private TableColumn<ObservableReportRow, Integer> durationColumn;

	@FXML
	private TableColumn<ObservableReportRow, Float> numberColumn;

	private ObservableList<ObservableReportRow> reportRows;// for table view

	private Report_LateReturns data = null;

	public void setReportInformation(Report_LateReturns info)
	{
		data = info;
		initilReport();
		mainScrollPane.setVvalue(0);
	}

	private void initilReport()
	{
		setGraphs();
		setAvrages();
		setMedian();
		setTable();
		System.out.println(mainScrollPane.getVvalue());
	}

	private void setTable()
	{
		catalogColumn.setCellValueFactory(new PropertyValueFactory<>("catalogNumber"));
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("avgNumOfLate"));
		durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
		reportRows = FXCollections.observableArrayList();
		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			ObservableReportRow row = new ObservableReportRow(key,
					data.getBookToNumberAndDurationOfLates().get(key).getDurationOfLates(),
					data.getBookToNumberAndDurationOfLates().get(key).getAvarageNumberOfLates());
			reportRows.add(row);
		}

		booksReportInfoTableview.setItems(reportRows);
	}

	private void setMedian()
	{
		List<Integer> daysArray = new ArrayList();
		double median;
		String medianString;
		// number of lates:
		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			daysArray.add(data.getBookToNumberAndDurationOfLates().get(key).getNumberOfLates());
		}
		if (daysArray.size() != 0)
		{
			Collections.sort(daysArray);
			if (daysArray.size() % 2 == 0)
				median = ((double) daysArray.get(daysArray.size() / 2)
						+ (double) daysArray.get(daysArray.size() / 2 - 1)) / 2;
			else
				median = (double) daysArray.get(daysArray.size() / 2);
			medianString = String.valueOf(median);
			if (medianString.length() > 4)
				medianString = medianString.substring(0, 4);
			medianNumberLateTextField.setText(medianString);
		} else
			medianNumberLateTextField.setText("0.0");

		// Duration of lates:
		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			daysArray.add(data.getBookToNumberAndDurationOfLates().get(key).getDurationOfLates());
		}
		if (daysArray.size() != 0)
		{
			Collections.sort(daysArray);
			if (daysArray.size() % 2 == 0)
				median = ((double) daysArray.get(daysArray.size() / 2)
						+ (double) daysArray.get(daysArray.size() / 2 - 1)) / 2;
			else
				median = (double) daysArray.get(daysArray.size() / 2);
			medianString = String.valueOf(median);
			if (medianString.length() > 4)
				medianString = medianString.substring(0, 4);
			medianDurationLateTextField.setText(medianString);
		} else
			medianDurationLateTextField.setText("0.0");
	}

	private void setAvrages()
	{
		float sum = 0;
		float size = data.getBookToNumberAndDurationOfLates().keySet().size();
		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			sum += data.getBookToNumberAndDurationOfLates().get(key).getNumberOfLates();
		}
		avrgNumberLateTextField.setText(String.valueOf(sum / size));

		sum = 0;
		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			sum += data.getBookToNumberAndDurationOfLates().get(key).getDurationOfLates();
		}
		avrgDurationLateTextField.setText(String.valueOf(sum / size));
	}

	private void setGraphs()
	{
		String[] ranges;
		XYChart.Series numberOflates = new XYChart.Series();
		XYChart.Series durationOflates = new XYChart.Series();
		int size = data.getBookToNumberAndDurationOfLates().keySet().size();
		int[] histogram;
		int[] numberOfLatesArray = new int[size];
		int[] durationOfLatesArray = new int[size];
		int j = 0;

		numberOfLateChart.getXAxis().setLabel("Amount of lates");
		numberOfLateChart.getYAxis().setLabel("Amount of books");

		durationOfLateChart.getXAxis().setLabel("Duration of lates");
		durationOfLateChart.getYAxis().setLabel("Amount of books");

		for (String key : data.getBookToNumberAndDurationOfLates().keySet())
		{
			numberOfLatesArray[j] = data.getBookToNumberAndDurationOfLates().get(key).getNumberOfLates();
			durationOfLatesArray[j] = data.getBookToNumberAndDurationOfLates().get(key).getDurationOfLates();
			j++;
		}

		Arrays.sort(numberOfLatesArray);
		Arrays.sort(durationOfLatesArray);
		// Number of late:
		int max = numberOfLatesArray[numberOfLatesArray.length - 1];
		int amount;
		ranges = getDaysRange(max);

		if (max > 10)
		{
			amount = 10;

		} else
		{
			max = 10;
			amount = max - 1;
		}
		histogram = calcHistogram(numberOfLatesArray, 1, max, amount);
		for (int i = 0; i < amount; i++)
		{
			numberOflates.getData().add(new XYChart.Data(ranges[i], histogram[i]));
		}
		// Duration:
		max = durationOfLatesArray[durationOfLatesArray.length - 1];
		ranges = getDaysRange(max);

		if (max > 10)
		{
			amount = 10;

		} else
		{
			max = 10;
			amount = max - 1;
		}
		histogram = calcHistogram(durationOfLatesArray, 1, max, amount);
		for (int i = 0; i < amount; i++)
		{
			durationOflates.getData().add(new XYChart.Data(ranges[i], histogram[i]));
		}

		numberOfLateChart.getData().add(numberOflates);
		durationOfLateChart.getData().add(durationOflates);

	}

	public class ObservableReportRow
	{
		public SimpleStringProperty catalogNumber;
		public SimpleIntegerProperty duration;
		public SimpleFloatProperty avgNumOfLate;

		public ObservableReportRow(String catNum, int dur, float numOfLate)
		{
			catalogNumber = new SimpleStringProperty(catNum);
			duration = new SimpleIntegerProperty(dur);
			avgNumOfLate = new SimpleFloatProperty(numOfLate);
		}

		public String getCatalogNumber()
		{
			return catalogNumber.get();
		}

		public void setCatalogNumber(String catalogNumber)
		{
			this.catalogNumber = new SimpleStringProperty(catalogNumber);
		}

		public int getDuration()
		{
			return duration.get();
		}

		public void setDuration(int duration)
		{
			this.duration = new SimpleIntegerProperty(duration);
		}

		public float getAvgNumOfLate()
		{
			return avgNumOfLate.get();
		}

		public void setAvgNumOfLate(float avgNumOfLate)
		{
			this.avgNumOfLate = new SimpleFloatProperty(avgNumOfLate);
		}

	}

	private String[] getDaysRange(int amount)
	{
		if (amount < 10)
		{
			return new String[]
			{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		}
		String[] ranges = new String[amount];
		int maxDaysLate = amount, daysCount = 1;
		String daysRanges = "";
		for (int i = 0; i < (10 - (maxDaysLate % 10)); i++) // amount of columns
		{
			daysRanges += daysCount;
			for (int j = 0; j < ((maxDaysLate / 10) - 1); j++) // range of days
			{
				daysCount++;
			}
			daysRanges += "-" + daysCount + " ";
			daysCount++;
		}
		for (int i = 0; i < (maxDaysLate % 10); i++) // amount of columns
		{
			daysRanges += daysCount;
			for (int j = 0; j < ((maxDaysLate / 10)); j++) // range of days
			{
				daysCount++;
			}
			daysRanges += "-" + daysCount + " ";
			daysCount++;
		}
		ranges = daysRanges.split(" ");
		return ranges;
	}

	public static int[] calcHistogram(int[] data, int min, int max, int numBins)
	{
		final int[] result = new int[numBins];
		final double binSize = (max - min) / numBins;

		for (int d : data)
		{
			int bin = (int) ((d - min) / binSize);
			if (bin < 0)
			{
				/* this data is smaller than min */ } else if (bin >= numBins)
			{
				/* this data point is bigger than max */ } else
			{
				result[bin] += 1;
			}
		}
		return result;
	}

}
