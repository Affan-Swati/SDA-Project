package admin_package;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import BLL_package.Currency;
import BLL_package.FDCX;
import BLL_package.Stock;
import BLL_package.prediction;
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

public class AdminWelcomePageController
{
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
	
	@FXML private TableView ViewStocks;
	@FXML private TableView ViewCurrencies;
	
	@FXML private Button signOut;
	
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
		String name = null; String code = null; Double rate = null; String type = null;
		
        for (Currency item : selectedItems)
        {
            name = item.getCurrencyName();
            code = item.getCurrencyCode();
            rate = item.getRateAgainstUSD();
            type = item.getType();
        }
    
		FDCX fdcx = FDCX.getInstance();
		fdcx.addCurrencyToSystem(name, code, rate, type, Double.parseDouble(buyCurrencyUnit.getText()));	
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("CURRENCY ADDED!");
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
	
		FDCX fdcx = FDCX.getInstance();
		fdcx.addStockToSystem(selectedItems.get(0).getName(), selectedItems.get(0).getUnitPrice(), Integer.parseInt(buyStocksUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("STOCKS ADDED!");
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
    }
	
	public void fillSellCurrency() throws SQLException
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
        
        sellCurrencyTable.getColumns().clear();
        sellCurrencyTable.getColumns().addAll(nameColumn, codeCol, quantityColumn, typeCol, amountCol);
		
        sellCurrencyTable.setItems(list);
       
	}
	
	public void fillSellStocks() throws SQLException
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
        sellStocksTable.getColumns().clear();
        sellStocksTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn);
		
        sellStocksTable.setItems(list);
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
		String name = null; String code = null; Double rate = null; String type = null;
		double amount = 0;
		
        for (Currency item : selectedItems)
        {
            name = item.getCurrencyName();
            code = item.getCurrencyCode();
            rate = item.getRateAgainstUSD();
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
		fdcx.removeCurrencyFromSystem(code, Double.parseDouble(sellCurrencyUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("CURRENCY REMOVED!");
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
		fdcx.removeStockFromSystem(selectedItems.get(0).getName(), Integer.parseInt(sellStocksUnit.getText()));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);  // No header
        alert.setContentText("STOCKS REMOVED!");
        sellStocksUnit.setText("");
        alert.showAndWait();
	}
	
	public void updateCurrencyPredictions() throws Exception
	{
		FDCX fdcx = FDCX.getInstance();
		List<prediction> log = fdcx.getCurrencyPrediction();
		ObservableList<prediction> list = FXCollections.observableArrayList();
		
		for(prediction s : log)
		{
			list.add(s);
		}
		
        TableColumn<prediction, String> nameColumn = new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        TableColumn<prediction, Double> curr = new TableColumn<>("CURRENT VALUE");
        curr.setCellValueFactory(new PropertyValueFactory<>("current"));
        curr.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        TableColumn<prediction, Double> pred = new TableColumn<>("PREDICTED VALUE");
        pred.setCellValueFactory(new PropertyValueFactory<>("predicted"));
        pred.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        ViewCurrencies.getColumns().clear();
        
        ViewCurrencies.getColumns().addAll(nameColumn, curr, pred);
		
        ViewCurrencies.setItems(list);		
	}
	
	public void updateStockPredictions() throws Exception
	{
		FDCX fdcx = FDCX.getInstance();
		
		List<prediction> log = fdcx.getStockPrediction();
		ObservableList<prediction> list = FXCollections.observableArrayList();
		
		for(prediction s : log)
		{
			list.add(s);
		}
		
		TableColumn<prediction, String> nameColumn = new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        TableColumn<prediction, Double> curr = new TableColumn<>("CURRENT VALUE");
        curr.setCellValueFactory(new PropertyValueFactory<>("current"));
        curr.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        TableColumn<prediction, Double> pred = new TableColumn<>("PREDICTED VALUE");
        pred.setCellValueFactory(new PropertyValueFactory<>("predicted"));
        pred.setPrefWidth(ViewCurrencies.getWidth()/3);
        
        ViewStocks.getColumns().clear();
        
        ViewStocks.getColumns().addAll(nameColumn, curr, pred);
		
        ViewStocks.setItems(list);		
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
	
}
