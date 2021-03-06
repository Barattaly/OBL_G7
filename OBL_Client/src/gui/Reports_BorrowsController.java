package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.spi.InitialContextFactory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import entities.Report_BorrowDurationInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;

public class Reports_BorrowsController
{
	@FXML
	private JFXTextField regularBooksAvarageTextField;

	@FXML
	private JFXTextField regularBooksMedianTextField;

	@FXML
	private BarChart<CategoryAxis, NumberAxis> regularBooksGraph;

	@FXML
	private BarChart<CategoryAxis, NumberAxis> wantedBookGraph;

	@FXML
	private JFXTextField wantedBooksAvarageTextField;
	@FXML
	private JFXTextField wantedBooksMedianTextField;

	private Report_BorrowDurationInfo data = null;

	public void setReportInformation(Report_BorrowDurationInfo info)
	{
		data = info;
		initilReport();
	}

	private void initilReport()
	{
		setGraphs();
		setAvrages();
		setMedian();
	}

	private void setMedian()
	{
		List<Integer> daysArray = new ArrayList();// int[data.getRegularBooks().keySet().size()];
		double median;
		String medianString;
		// regulat books:
		for (String catNum : data.getRegularBooks().keySet())
		{
			if (data.getRegularBooks().get(catNum) != 0)
			{
				daysArray.add(data.getRegularBooks().get(catNum));
			}
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
			regularBooksMedianTextField.setText(medianString);
		} else
			regularBooksMedianTextField.setText("0");
		// wanted books:
		daysArray.clear();
		for (String catNum : data.getWantedBooks().keySet())
		{
			if (data.getWantedBooks().get(catNum) != 0)
			{
				daysArray.add(data.getWantedBooks().get(catNum));
			}
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
			wantedBooksMedianTextField.setText(medianString);
		} else
			wantedBooksMedianTextField.setText("0");
	}

	private void setAvrages()
	{
		float average = 0;
		int count = 0;
		String averageString;
		for (String catNum : data.getRegularBooks().keySet())
		{
			if (data.getRegularBooks().get(catNum) != 0)
			{
				average += data.getRegularBooks().get(catNum);
				count++;
			}
		}
		if (count != 0)
			average = average / count;
		else
			average = 0;
		averageString = String.valueOf(average);
		if (averageString.length() > 4)
			averageString = averageString.substring(0, 4);
		regularBooksAvarageTextField.setText(averageString);
		average = 0;
		count = 0;
		for (String catNum : data.getWantedBooks().keySet())
		{
			if (data.getWantedBooks().get(catNum) != 0)
			{
				average += data.getWantedBooks().get(catNum);
				count++;
			}
		}
		if (count != 0)
			average = average / count;
		else
			average = 0;
		averageString = String.valueOf(average);
		if (averageString.length() > 4)
			averageString = averageString.substring(0, 4);
		wantedBooksAvarageTextField.setText(averageString);
	}

	private void setGraphs()
	{
		String[] ranges;
		XYChart.Series regular = new XYChart.Series();
		XYChart.Series wanted = new XYChart.Series();
		int[] histogram;

		int[] regularDurations = Arrays.stream(data.getRegularBooks().values().toArray()).mapToInt(o -> (int) o)
				.toArray();
		int[] wantedDurations = Arrays.stream(data.getWantedBooks().values().toArray()).mapToInt(o -> (int) o)
				.toArray();
		regularBooksGraph.getXAxis().setLabel("Borrow duration [Days]");
		regularBooksGraph.getYAxis().setLabel("Amount of books");

		wantedBookGraph.getXAxis().setLabel("Borrow duration [Days]");
		wantedBookGraph.getYAxis().setLabel("Amount of books");
		regular.setName("Regular Books");
		wanted.setName("Wanted Books");

		Arrays.sort(regularDurations);
		Arrays.sort(wantedDurations);
		int max = regularDurations[regularDurations.length - 1];

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

		histogram = calcHistogram(regularDurations, 0, max , 10.0);
		for (int i = 0; i < amount; i++)
		{
			regular.getData().add(new XYChart.Data(ranges[i], histogram[i]));
		}

		regularBooksGraph.getData().add(regular);

		max = wantedDurations[wantedDurations.length - 1];

		ranges = new String[]
		{ "1", "2", "3" };

		histogram = calcHistogram(wantedDurations, 1, 4, 3);
		for (int i = 0; i < 3; i++)
		{
			wanted.getData().add(new XYChart.Data(ranges[i], histogram[i]));
		}

		wantedBookGraph.getData().add(wanted);

	}

	private String[] getDaysRange(int max)
	{
		if (max < 10)
		{
			return new String[]
			{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		}
		String[] ranges = new String[10];
		int start = 0;
		double step = Math.ceil(max/10.0);
		ranges[0]= "[" +String.valueOf(start) +" - " +String.valueOf(step) +"]";

		for(int i=1;i<10;i++)
		{
			ranges[i]= "[" +String.valueOf((i)*step) +" - " +String.valueOf((i+1)*step) +"]";

		}
		return ranges;
		/*
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
		return ranges;*/
	}

	public static int[] calcHistogram(int[] data, int min, int max, double numBins)
	{
		final int[] result = new int[10];
		
		double binSize = Math.ceil(max/numBins);


		for (int d : data)
		{
			int bin;
			if (binSize == 0)
				bin = 0;
			else
				bin = (int) ((d - min) / binSize);
			if (bin < 0)
			{
				/* this data is smaller than min */
			} else if (bin >= 10)
			{
				/* this data point is bigger than max */
			} else
			{
				result[bin] += 1;
			}
		}
		return result;
	}

}
