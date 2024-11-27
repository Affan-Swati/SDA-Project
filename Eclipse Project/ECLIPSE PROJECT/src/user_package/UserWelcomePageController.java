package user_package;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import BLL_package.Currency;
import BLL_package.FDCX;
import BLL_package.Stock;
import BLL_package.TransactionLog;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserWelcomePageController
{
	@FXML private Text username;
	
	@FXML private Button delButton;
	
	@FXML private TableView sellStocksTable;
	@FXML private javafx.scene.control.TextField sellStocksUnit;
	@FXML private Button sellStocksButton;
	
	@FXML private TableView buyStocksTable;
	@FXML private javafx.scene.control.TextField buyStocksUnit;
	@FXML private Button buyStocksButton;
	
	@FXML private TableView<Currency> sellCurrencyTable;
	@FXML private javafx.scene.control.TextField sellCurrencyUnit;
	@FXML private Button sellCurrencyButton;
	
	@FXML private TableView buyCurrencyTable;
	@FXML private javafx.scene.control.TextField buyCurrencyUnit;
	@FXML private Button buyCurrencyButton;
	
	@FXML private Button NewMonthlySubs;
	@FXML private Button NewQuarterlySubs;
	@FXML private Button NewYearlySubs;
	@FXML private Button RenewSubs;
	@FXML private Button CancelSubs;
	
	@FXML private TableView taxReport;
	
	@FXML private Button signOut;
	
	@FXML private Text balance_buyStocks;
	@FXML private Text balance_sellStocks;
	@FXML private Text balance_buyCurrency;
	@FXML private Text balance_sellCurrency;
	
	@FXML private Text currentSubs;
	@FXML private Text currentSubs_;
	
	public class IntegerChecker
	{
	    public static boolean isInteger(TextField textField) {
	        try {
	            Integer.parseInt(textField.getText());
	            return true;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
	}
	
	public class DoubleChecker
	{
	    public static boolean isDouble(TextField textField) {
	        try {
	            Double.parseDouble(textField.getText());
	            return true;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
	}
	
	public void buyCurrency() throws Exception
	{
		if (!DoubleChecker.isDouble(buyCurrencyUnit) || (DoubleChecker.isDouble(buyCurrencyUnit) && Double.parseDouble(buyCurrencyUnit.getText()) <= 0))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
		}
				
		ObservableList<Currency> selectedItems = buyCurrencyTable.getSelectionModel().getSelectedItems();
		String code = null; String type = null; Double amount = null;
		
        for (Currency item : selectedItems)
        {
            code = item.getCurrencyCode();
            type = item.getType();
            amount = item.getAmount();
        }
        
        if(Double.parseDouble(buyCurrencyUnit.getText()) > amount)
        {
        	Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
        }
                
		FDCX fdcx = FDCX.getInstance();
		fdcx.depositFunds(type, fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), code, Double.parseDouble(buyCurrencyUnit.getText()));	
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("CURRENCY BOUGHT!");
        buyCurrencyUnit.setText("");
        alert.showAndWait();
		
	}
	
	public void buyStock() throws Exception
	{
		if (!IntegerChecker.isInteger(buyStocksUnit) || (IntegerChecker.isInteger(buyStocksUnit) && Integer.parseInt(buyStocksUnit.getText()) <= 0))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
		} 
		
		ObservableList<Stock> selectedItems = buyStocksTable.getSelectionModel().getSelectedItems();
	        
		if(Integer.parseInt(buyStocksUnit.getText()) > selectedItems.get(0).getQuantity())
        {
        	Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
        }
		
		FDCX fdcx = FDCX.getInstance();
		fdcx.assignStockToUser(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), selectedItems.get(0), Integer.parseInt(buyStocksUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("STOCKS BOUGHT!");
        buyStocksUnit.setText("");
        alert.showAndWait();
	}
	
	public void fillBuyStocks() throws SQLException
	{
		FDCX fdcx = FDCX.getInstance();
		List<Stock> stocks = fdcx.getStockDB();
		ObservableList<Stock> list = FXCollections.observableArrayList();
		
		for(Stock s : stocks)
		{
			list.add(s);
		}
		
        TableColumn<Stock, String> nameColumn = new TableColumn<>("Stock Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Stock, Double> priceColumn = new TableColumn<>("Unit Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        TableColumn<Stock, Integer> quantityColumn = new TableColumn<>("Available Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyStocksTable.getColumns().clear();
        buyStocksTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn);
		
		buyStocksTable.setItems(list);
		
		if(username != null)
			balance_buyStocks.setText("BALANCE: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getWallet().getCurrencyBalance("USD"));
	}			  
	
	public void fillBuyCurrency() throws SQLException
	{
		FDCX fdcx = FDCX.getInstance();
		List<Currency> currency = fdcx.getCurrencyDB();
		ObservableList<Currency> list = FXCollections.observableArrayList();
		
		for(Currency s : currency)
		{
			list.add(s);
		}
		
        TableColumn<Currency, String> nameColumn = new TableColumn<>("Currency Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        TableColumn<Currency, String> codeCol = new TableColumn<>("Currency Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("CurrencyCode"));

        TableColumn<Currency, Double> quantityColumn = new TableColumn<>("RATE/USD");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("rateAgainstUSD"));
        
        TableColumn<Currency, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Currency, Double> amountCol = new TableColumn<>("Quantity");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        buyCurrencyTable.getColumns().clear();
        buyCurrencyTable.getColumns().addAll(nameColumn, codeCol, quantityColumn, typeCol, amountCol);
		
        buyCurrencyTable.setItems(list);
        
        if(username != null)
        	balance_buyCurrency.setText("BALANCE: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getWallet().getCurrencyBalance("USD"));
	}
	
	public void fillSellCurrency() throws SQLException
	{
		FDCX fdcx = FDCX.getInstance();
		List<Currency> currency = fdcx.getUserCurrencyDB();
		ObservableList<Currency> list = FXCollections.observableArrayList();
		
		for(Currency s : currency)
		{
			list.add(s);
		}
		
        TableColumn<Currency, String> nameColumn = new TableColumn<>("Currency Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        TableColumn<Currency, String> codeCol = new TableColumn<>("Currency Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("CurrencyCode"));

        TableColumn<Currency, Double> quantityColumn = new TableColumn<>("RATE/USD");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("rateAgainstUSD"));
        
        TableColumn<Currency, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Currency, Double> amountCol = new TableColumn<>("Quantity");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        sellCurrencyTable.getColumns().clear();
        sellCurrencyTable.getColumns().addAll(nameColumn, codeCol, quantityColumn, typeCol, amountCol);
		
        sellCurrencyTable.setItems(list);
        
        if(username != null)
        	balance_sellCurrency.setText("BALANCE: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getWallet().getCurrencyBalance("USD"));
	
	}
	
	public void fillSellStocks() throws SQLException
	{
		FDCX fdcx = FDCX.getInstance();
		List<Stock> stocks = fdcx.getUserStockDB();
		ObservableList<Stock> list = FXCollections.observableArrayList();
		
		for(Stock s : stocks)
		{
			list.add(s);
		}
		
        TableColumn<Stock, String> nameColumn = new TableColumn<>("Stock Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Stock, Double> priceColumn = new TableColumn<>("Unit Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        TableColumn<Stock, Integer> quantityColumn = new TableColumn<>("Available Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellStocksTable.getColumns().clear();
        sellStocksTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn);
		
        sellStocksTable.setItems(list);
		
        if(username != null)
        	balance_sellStocks.setText("BALANCE: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getWallet().getCurrencyBalance("USD"));

	}
	
	public void sellCurrency()
	{
		if (!DoubleChecker.isDouble(sellCurrencyUnit) || (DoubleChecker.isDouble(sellCurrencyUnit) && Double.parseDouble(sellCurrencyUnit.getText()) <= 0))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
		}
				
		ObservableList<Currency> selectedItems = sellCurrencyTable.getSelectionModel().getSelectedItems();
		String code = null; String type = null; Double amount = null;
		
        for (Currency item : selectedItems)
        {
            code = item.getCurrencyCode();
            type = item.getType();
            amount = item.getAmount();
        }
        
        if(Double.parseDouble(sellCurrencyUnit.getText()) > amount)
        {
        	Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
        }
                
		FDCX fdcx = FDCX.getInstance();
		fdcx.withdrawFunds(type, fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), code,  Double.parseDouble(sellCurrencyUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("CURRENCY SOLD!");
        sellCurrencyUnit.setText("");
        alert.showAndWait();
	}
	
	public void sellStocks()
	{
		if (!IntegerChecker.isInteger(sellStocksUnit) || (IntegerChecker.isInteger(sellStocksUnit) && Integer.parseInt(sellStocksUnit.getText()) <= 0))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
		} 
		
		ObservableList<Stock> selectedItems = sellStocksTable.getSelectionModel().getSelectedItems();
	        
		if(Integer.parseInt(sellStocksUnit.getText()) > selectedItems.get(0).getQuantity())
        {
        	Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INVALID UNITS");
	
	         alert.showAndWait();
	         return;
        }
		
		FDCX fdcx = FDCX.getInstance();
		fdcx.removeStockFromUser(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), selectedItems.get(0), Integer.parseInt(sellStocksUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("STOCKS SOLD!");
        sellStocksUnit.setText("");
        alert.showAndWait();
	}
	
	public void updateTaxReports() throws Exception
	{
		FDCX fdcx = FDCX.getInstance();
		List<TransactionLog> log = fdcx.getTaxReport(fdcx.getCNICfromUsername(username.getText()));
		ObservableList<TransactionLog> list = FXCollections.observableArrayList();
		
		for(TransactionLog s : log)
		{
			list.add(s);
		}
		
        TableColumn<Stock, String> nameColumn = new TableColumn<>("TAX DETAILS");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        nameColumn.prefWidthProperty().bind(taxReport.widthProperty());
        taxReport.getColumns().addAll(nameColumn);
		
        taxReport.setItems(list);		
	}
	
	public void newMonthly()
	{
		FDCX fdcx = FDCX.getInstance();
		
		if(!fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().equals("cancelled"))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: SUBSCRIPTION ALREADY ACTIVE");
	
	         alert.showAndWait();
		}
		
		else
		{
			fdcx.subscribe(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), "monthly");
			currentSubs.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
			
			 Alert alert = new Alert(AlertType.CONFIRMATION);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("SUCCESS: SUBSCRIPTION APPLIED!");
	
	         alert.showAndWait();
		}
	}
	
	public void newQuarterly()
	{
		FDCX fdcx = FDCX.getInstance();
		
		if(!fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().equals("cancelled"))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: SUBSCRIPTION ALREADY ACTIVE");
	
	         alert.showAndWait();
		}
		
		else
		{
			fdcx.subscribe(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), "quarterly");
			currentSubs.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
		
			Alert alert = new Alert(AlertType.CONFIRMATION);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("SUCCESS: SUBSCRIPTION APPLIED!");
	
	         alert.showAndWait();
		}
	}
	
	public void newYearly()
	{
		FDCX fdcx = FDCX.getInstance();
		
		if(!fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().equals("cancelled"))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: SUBSCRIPTION ALREADY ACTIVE");
	
	         alert.showAndWait();
		}
		
		else
		{
			fdcx.subscribe(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())), "yearly");
			currentSubs.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
			
			Alert alert = new Alert(AlertType.CONFIRMATION);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("SUCCESS: SUBSCRIPTION APPLIED!");
	
	         alert.showAndWait();
		}
	}
	
	public void RenewSubs()
	{
		FDCX fdcx = FDCX.getInstance();
		
		if(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().equals("cancelled"))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: NO SUBSCRIPTION ACTIVE!");
	
	         alert.showAndWait();
	         return;
		}
		
		fdcx.renewSubscription(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())));
		currentSubs.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
	
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("ERROR");
        alert.setHeaderText(null);  // No header
        alert.setContentText("SUCCESS: SUBSCRIPTION RENEWED!");

        alert.showAndWait();
	}
	
	public void CancelSubs_()
	{
		FDCX fdcx = FDCX.getInstance();
		
		if(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().equals("cancelled"))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: NO SUBSCRIPTION ACTIVE!");
	
	         alert.showAndWait();
	         return;
		}
		
		fdcx.cancelSubscription(fdcx.getUser(fdcx.getCNICfromUsername(username.getText())));
		updateSubsType_();
	
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("ERROR");
        alert.setHeaderText(null);  // No header
        alert.setContentText("SUCCESS: UNSUBSCRIBED!");

        alert.showAndWait();
	}
	
	public void updateSubsType_()
	{
		FDCX fdcx = FDCX.getInstance();
		currentSubs_.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
	}
	
	public void updateSubsType()
	{
		FDCX fdcx = FDCX.getInstance();
		currentSubs.setText("Current Plan: " + fdcx.getUser(fdcx.getCNICfromUsername(username.getText())).getAccount().getSubscription().getType().toUpperCase());
	}
	
	public void signOut()
	{
		Parent MainPage = null;
		try {
			MainPage = FXMLLoader.load(getClass().getResource("/main_package/MainPage.fxml"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 	    Scene MainPageScene = new Scene(MainPage);
 	    Stage stage = (Stage) signOut.getScene().getWindow(); // Get current stage
 	    stage.setScene(MainPageScene);
	}
	
	public void setUsername(String c)
	{
		username.setText(c);
	}
	
	public void DeleteButtonPressed()
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("WARNING!");
	    alert.setHeaderText("");
	    alert.setContentText("THIS ACTION WILL DELETE YOUR ACCOUNT PERMANENTLY!");

	    // Add Yes and No buttons
	    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
	    alert.getButtonTypes().setAll(yesButton, noButton);

	    // Show the alert and wait for user input
	    Optional<ButtonType> result = alert.showAndWait();

	    if (result.isPresent() && result.get() == yesButton) 
	    {
	        FDCX fdcx = FDCX.getInstance();
	        String cnic = fdcx.getCNICfromUsername(username.getText());
	        fdcx.removeAccount(username.getText());
	        fdcx.removeUser(cnic);
	        
	        Parent mainPage = null;
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_package/MainPage.fxml"));
	        try {
	            mainPage = loader.load();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

            Scene mainPageScene = new Scene(mainPage);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(mainPageScene);	        
	    } 	
	}
	
}
