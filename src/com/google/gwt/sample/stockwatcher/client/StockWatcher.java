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
	private TextBox nameTextBox = new TextBox();
	private TextBox yearTextBox = new TextBox();
	private TextBox categoryTextBox = new TextBox();
	private Button addStockButton = new Button("Add");  
	private Label lastUpdatedLabel = new Label();
	private ArrayList<BaseRow> stocks = new ArrayList<BaseRow>();
	private int rowType = 0;

	/**  
	 *  Entry point method.  
	 */  
	public void onModuleLoad() 
	{  
		// Create table for stock data.
		stocksFlexTable.setHTML(0, 0, "<header>Name</header>");
		stocksFlexTable.setHTML(0, 1, "<header>Year</header>");
		stocksFlexTable.setHTML(0, 2, "<header>Category</header>");
		stocksFlexTable.setHTML(0, 3, "<header>Remove</header>");
		
		nameTextBox.setTitle("name");
		yearTextBox.setTitle("year");
		categoryTextBox.setTitle("category");
		
		nameTextBox.setText("name");
		yearTextBox.setText("year");
		categoryTextBox.setText("category");
		
		nameTextBox.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
					nameTextBox.setText("");			
			}
		});
		
		yearTextBox.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
					yearTextBox.setText("");			
			}
		});
		
		categoryTextBox.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
					categoryTextBox.setText("");			
			}
		});
		
		// Assemble Add Stock panel.  
		addPanel.add(nameTextBox);
		addPanel.add(yearTextBox);
		addPanel.add(categoryTextBox);
		addPanel.add(addStockButton);
		
		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		
		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);

		
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
		final String newName = nameTextBox.getText().toUpperCase().trim();
		final String newYear = yearTextBox.getText().toUpperCase().trim();
		final String newCategory = categoryTextBox.getText().toUpperCase().trim();
		
		if(!newName.matches("^[0-9A-Z \\.]{1,20}$"))
		{
			Window.alert("'" + newName + "' is not walid symbol.");
			nameTextBox.selectAll();
			return;
		}
		else if(!newYear.matches("^[0-9\\.]{1,4}$"))
		{
			Window.alert("'" + newYear + "' is not walid symbol.");
			yearTextBox.selectAll();
			return;
		}
		else if(!newCategory.matches("^[0-9A-Z \\.]{1,10}$"))
		{
			Window.alert("'" + newCategory + "' is not walid symbol.");
			categoryTextBox.selectAll();
			return;
		}
		

		
		int row = stocksFlexTable.getRowCount();
		final BaseRow newRow = new BaseRow(newName, newYear, newCategory);

		stocks.add(newRow);
		String nameHTML;
		String yearHTML;
		String categoryHTML;
		if(rowType == 0)
		{
			nameHTML = "<td1>" + newName + "</td1>";
			yearHTML = "<td1>" + newYear + "</td1>";
			categoryHTML = "<td1>" + newCategory + "</td1>";
			rowType = 1;
		}
		else
		{
			nameHTML = "<td2>" + newName + "</td2>";
			yearHTML = "<td2>" + newYear + "</td2>";
			categoryHTML = "<td2>" + newCategory + "</td2>";
			rowType = 0;
		}
		
		stocksFlexTable.setHTML(row, 0 , nameHTML);
		stocksFlexTable.setHTML(row, 1 , yearHTML);
		stocksFlexTable.setHTML(row, 2 , categoryHTML);
		
		Button removeStockButton = new Button("x");
		removeStockButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int removedIndex = stocks.indexOf(newRow);
				stocks.remove(removedIndex);
				stocksFlexTable.removeRow(removedIndex + 1);
			}
		});
		stocksFlexTable.setWidget(row, 3, removeStockButton);
		
		refreshWatchList();
	}
}