package com.codecrunchcorner.stockquote;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends Activity {

	public final static String STOCK_SYMBOL = "com.example.stockquote.STOCK";

	public SharedPreferences stockSymbolsEntered;
	private TableLayout stocksTableScrollView;
	private EditText symbolsEditText;
	Button enterSymbolButton;
	Button deleteStocksButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stockSymbolsEntered = getSharedPreferences("stockList", MODE_PRIVATE);

		//stocksScrollView = (TableLayout) findViewById(R.id.stockScrollView);
		
		/*
		 * BUG FIX 1/31/14
		 * stocksScrollView and stocksTableScrollView are two distinct, decoupled elements within the .xml that needed to be separated.
		 * I tightly coupled the two together when I erroneously thought that the naming wouldn't matter in this case. stocksTableScroll 
		 * is a tableLayout that MUST be separate from the component its nested beneath.
		 * */
		stocksTableScrollView = (TableLayout) findViewById(R.id.stockTableScrollView);
		symbolsEditText = (EditText) findViewById(R.id.symbolEditText);

		enterSymbolButton = (Button) findViewById(R.id.enterSymbolButton);
		deleteStocksButton = (Button) findViewById(R.id.deleteStocksButton);

		enterSymbolButton.setOnClickListener(enterSymbolButtonListener);
		deleteStocksButton.setOnClickListener(deleteStocksButtonListener);

		updateSavedStockList(null);
	}

	private void updateSavedStockList(String newStockSymbol) {
		String[] stocks = stockSymbolsEntered.getAll().keySet()
				.toArray(new String[0]);

		Arrays.sort(stocks, String.CASE_INSENSITIVE_ORDER);

		if (newStockSymbol != null) {
			insertStockInScrollView(newStockSymbol,
					Arrays.binarySearch(stocks, newStockSymbol));

		} else {
			for (int i = 0; i < stocks.length; i++)
				insertStockInScrollView(stocks[i], i);
		}

	}

	private void saveStockSymbol(String newStock) {

		String isTheStockNew = stockSymbolsEntered.getString(newStock, null);
		SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();

		preferencesEditor.putString(newStock, newStock);
		preferencesEditor.apply();

		if (isTheStockNew == null)
			updateSavedStockList(newStock);

	}

	private void insertStockInScrollView(String stock, int arrayIndex) {

		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//call in the stock_quote_view.xml
		View newStockRow = inflator.inflate(R.layout.stock_quote_row, null);

		
		TextView newStockTextView = (TextView) newStockRow
				.findViewById(R.id.stockSymbolTextView);

		//fill in with the inputed text
		newStockTextView.setText(stock);

		//make the buttons....
		Button stockQuoteButton = (Button) newStockRow
				.findViewById(R.id.stockQuoteButton);

		Button quoteFromWebButton = (Button) newStockRow
				.findViewById(R.id.quoteFromWebButton);

		//give the buttons listeners
		stockQuoteButton.setOnClickListener(getStockActivityListener);
		quoteFromWebButton.setOnClickListener(getStockFromWebsiteListener);

		//make the StockTableLayout that's nested under Row3 show this.
		stocksTableScrollView.addView(newStockRow, arrayIndex);
	}

	
	public OnClickListener getStockActivityListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			TableRow tableRow = (TableRow) v.getParent();
			TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);

			String stockSymbol = stockTextView.getText().toString();
			System.out.println(stockSymbol + " Making intent...");
			Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
			System.out.println(intent.toString());
			intent.putExtra(STOCK_SYMBOL, stockSymbol);
			
			startActivity(intent);
		}
	};
	
	
	public OnClickListener getStockFromWebsiteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//getting the text from TableRow3
			TableRow tableRow = (TableRow) v.getParent();
			TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);

			String stockSymbol = stockTextView.getText().toString();
			
			String stockURL = getString(R.string.yahoo_stock_url)+stockSymbol;
			System.out.println(stockURL);
			
			Intent getStockWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
			
			System.out.println(getStockWebPage.toString());
			startActivity(getStockWebPage);
		}
		
		
	};
	
	public OnClickListener enterSymbolButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (symbolsEditText.getText().length() > 0) {
				saveStockSymbol(symbolsEditText.getText().toString());

				// clears after clicking
				symbolsEditText.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(symbolsEditText.getWindowToken(), 0);

			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);

				builder.setTitle(R.string.invalid_stock_symbol);
				builder.setPositiveButton(R.string.ok, null);
				builder.setMessage(R.string.missing_stock_symbol);

				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();

			}
		}
	};

	private void deleteAllStocks() {

		stocksTableScrollView.removeAllViews();
	}

	public OnClickListener deleteStocksButtonListener = new OnClickListener() {

		
		@Override
		public void onClick(View v) {
			deleteAllStocks();

			SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();

			preferencesEditor.clear();
			preferencesEditor.apply();
		}
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		
		return true;
	}
}
