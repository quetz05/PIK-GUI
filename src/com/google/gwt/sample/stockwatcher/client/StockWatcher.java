package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class StockWatcher implements EntryPoint {

	private static final int REFRESH_INTERWAL = 5000; //ms
	private VerticalPanel mainPanel = new VerticalPanel();  
	private FlexTable stocksFlexTable = new FlexTable();  
	private HorizontalPanel addPanel = new HorizontalPanel();  
	private TextBox newSymbolTextBox = new TextBox();  
	private Button addStockButton = new Button("Add");  
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();

	/**  
	 *  Entry point method.  
	 */  
	public void onModuleLoad() 
	{  
		// Create table for stock data.
		stocksFlexTable.setText(0, 0, "Name");
		stocksFlexTable.setText(0, 1, "Year");
		stocksFlexTable.setText(0, 2, "Category");
		stocksFlexTable.setText(0, 3, "Remove");
		
		// Assemble Add Stock panel.  
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		
		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		
		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);
		
		// Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);
		
		addStockButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addStock();	
			}
		});
		
		addStockButton.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					addStock();
			}
		});
		
		Timer refreshTimer = new Timer() {

			@Override
			public void run() {
				refreshWatchList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERWAL);
	}
	
	protected void refreshWatchList() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Add stock to FlexTable.
	 */
	private void addStock()
	{
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		
		if(!symbol.matches("^[0-9A-Z\\.]{1,10}$"))
		{
			Window.alert("'" + symbol + "' is not walid symbol.");
			newSymbolTextBox.selectAll();
			return;
		}
		
		if( stocks.contains(symbol) )
			return;
		
		int row = stocksFlexTable.getRowCount();
		stocks.add(symbol);
		stocksFlexTable.setText(row, 0 , symbol);
		
		Button removeStockButton = new Button("x");
		removeStockButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int removedIndex = stocks.indexOf(symbol);
				stocks.remove(removedIndex);
				stocksFlexTable.removeRow(removedIndex + 1);
			}
		});
		stocksFlexTable.setWidget(row, 3, removeStockButton);
		
		refreshWatchList();
	}
}