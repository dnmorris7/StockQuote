package com.codecrunchcorner.stockquote;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class StockInfoActivity extends Activity {
	private static final String TAG = "STOCKQUOTE";

	// define the Text Views to be used here
	TextView companyNameTextView;
	TextView yearLowTextView;
	TextView yearHighTextView;
	TextView daysLowTextView;
	TextView daysHighTextView;
	TextView lastTradePriceOnlyTextView;
	TextView changeTextView;
	TextView daysRangeTextView;

	// xml Node Key Strings are here.
	static final String KEY_ITEM = "quote";
	static final String KEY_NAME = "Name";
	static final String KEY_YEAR_LOW = "YearLow";
	static final String KEY_YEAR_HIGH = "YearHigh";
	static final String KEY_DAYS_LOW = "DaysLow";
	static final String KEY_DAYS_HIGH = "DaysHigh";
	static final String KEY_LAST_TRADE_PRICE = "LastTradePriceOnly";
	static final String KEY_CHANGE = "Change";
	static final String KEY_DAYS_RANGE = "DaysRange";

	// xml data to retrieve
	String name = "";
	String yearLow = "";
	String yearHigh = "";
	String daysLow = "";
	String daysHigh = "";
	String lastTradePriceOnly = "";
	String change = "";
	String daysRange = "";

	//Holds values from document using xml pullparser
	String[][] xmlPullParserArray = { { "AverageDailyVolume", "0" },
			{ "Change", "0" }, { "DaysLow", "0" },

			{ "DaysHigh", "0" }, { "YearLow", "0" }, { "YearHigh", "0" },

			{ "MarketCapitalization", "0" }, { "LastTradePriceOnly", "0" },
			{ "DaysRange", "0" },

			{ "Name", "0" }, { "Symbol", "0" }, { "Volume", "0" },

			{ "StockExchange", "0" } };

	int parserArrayIncrement = 0;

	// the XML call for the data goes here.
	String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22";
	String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stockinfo);

		Intent intent = getIntent();
		System.out.println("From within the intent we return: " + intent.toString());
		String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);

		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
		yearLowTextView = (TextView) findViewById(R.id.yearLowTextView);
		yearHighTextView = (TextView) findViewById(R.id.yearHighTextView);
		daysLowTextView = (TextView) findViewById(R.id.daysLowTextView);
		daysHighTextView = (TextView) findViewById(R.id.daysHighTextView);
		lastTradePriceOnlyTextView = (TextView) findViewById(R.id.lastTradePriceOnlyTextView);
		changeTextView = (TextView) findViewById(R.id.changeTextView);
		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);

		// sends a message to logCat
		Log.d(TAG, "Before URL Creation " + stockSymbol);

		final String yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
		new MyAsyncTask().execute(yqlURL);

	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			try {
				// get the URL passed in
				URL url = new URL(args[0]);

				// connection is the communications link b/twn the application
				// and the URL we'll read from
				URLConnection connection;
				connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;

				int responseCode = httpConnection.getResponseCode();

				// tests if response code = 200. That's a good connections
				if (responseCode == HttpURLConnection.HTTP_OK) {

					// reads data from connection
					InputStream in = httpConnection.getInputStream();

					// parses into dom object trees
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();

					// provides a DOM document from an xml page
					DocumentBuilder db = dbf.newDocumentBuilder();

					// parse the yahoo finance yql stock xml file
					Document dom = db.parse(in);

					// the root element is queried
					Element docEle = dom.getDocumentElement();

					// Get a list of quote nodes
					NodeList nl = docEle.getElementsByTagName("quote");

					// check to ensure a quote tag
					if (nl != null && nl.getLength() > 0) {

						for (int i = 0; i < nl.getLength(); i++) {
							// passes the root element of the xml page, so that
							// the function below can search for the info needed
							StockInfo theStock = getStockInformation(docEle);

							// gets the values stored in the StockInfo Object
							daysLow = theStock.getDaysLow();
							daysHigh = theStock.getDaysHigh();
							yearLow = theStock.getYearLow();
							yearHigh = theStock.getYearHigh();
							name = theStock.getName();
							lastTradePriceOnly = theStock
									.getLastTradePriceonly();
							change = theStock.getChange();
							daysRange = theStock.getDaysRange();

						}

					}

				}

			} catch (MalformedURLException e) {
				Log.d(TAG, "MalformedURLException", e);
			} catch (IOException e) {
				Log.d(TAG, "IOException", e);
			} catch (ParserConfigurationException e) {
				Log.d(TAG, "Parser Configurations Exception", e);

			} catch (SAXException e) {
				Log.d(TAG, "SAX Exception", e);
			}

			finally {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			companyNameTextView.setText(name);
			yearLowTextView.setText("Year Low: " + yearLow);
			yearHighTextView.setText("Year High: " + yearHigh);
			daysLowTextView.setText("Days Low: " + daysLow);
			daysLowTextView.setText("Days High: " + daysHigh);
			lastTradePriceOnlyTextView.setText("Last Price: "
					+ lastTradePriceOnly);

			changeTextView.setText("Change: " + change);
			daysRangeTextView.setText("Daily Price Range: " + daysRange);
		}

		private StockInfo getStockInformation(Element entry) {

			String stockName = getTextValue(entry, "Name");
			String stockYearLow = getTextValue(entry, "YearLow");
			String stockYearHigh = getTextValue(entry, "YearHigh");
			String stockDaysLow = getTextValue(entry, "DaysLow");
			String stockDaysHigh = getTextValue(entry, "DaysHigh");
			String stockLastTradePriceOnly = getTextValue(entry,
					"LastTradePriceOnly");
			String stockChange = getTextValue(entry, "Change");
			String stockDaysRange = getTextValue(entry, "DaysRange");

			StockInfo theStock = new StockInfo(stockDaysLow, stockDaysHigh,
					stockYearLow, stockYearHigh, stockName,
					stockLastTradePriceOnly, stockChange, stockDaysRange);

			return theStock;
		}

		private String getTextValue(Element entry, String tagName) {
			String tagValueToReturn = null;

			NodeList nl = entry.getElementsByTagName(tagName);

			if (nl != null && nl.getLength() > 0) {
				Element element = (Element) nl.item(0);
				tagValueToReturn = element.getFirstChild().getNodeValue();

			}
			return tagValueToReturn;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;

	}

}
