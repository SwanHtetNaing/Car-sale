import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class Main extends Application
{
	int menuIndex = 0, no = 1, saleNo = 1;
	Font mmFont = new Font("Zawgyi-One",15);
	String addBorderColor = "green", success = "green", fail = "firebrick";
	NumberFormat numberFormat;
	Date date ;
	DropShadow shadowEffect = new DropShadow();
	
	Button toBuyBtn, afterBuyBtn, newBtn, updateBtn, deleteBtn, monthlyBtn, miniBtn,exitBtn;
	HBox menuHbox;
	int paneHeight = 53;
	
	TextField searchField;
	Button btnSearch, btnShowAll, btnLack, btnMonth;
	ComboBox<String> cbSearch, cbLack, cbMonth, cbYear;
	FlowPane searchPane, monthPane;
	StackPane stackSearch;
	
	ObservableList<CarData> carDataList, searchCarDataList;
	ObservableList<SaleData> saleDataList;
	ObservableList<MonthData> monthDataList;
	
	// car table
	TableView<CarData> carTable ;
	TableColumn<CarData, String> noCol,codeCol, nameCol, saleCol;
	TableColumn<CarData, String> quantityCol;
	TableColumn<CarData, String> priceCol;
	
	// sold table
	TableView<SaleData> soldTable;
	TableColumn<SaleData, String> soldNoCol, soldCodeCol, soldNameCol, soldQuantityCol, soldPriceCol, soldTotalCol, dateCol, dayCol, monthCol, yearCol, unSaleCol;
	
	// monthly table
	TableView<MonthData> monthTable;
	TableColumn<MonthData, String> monthNoCol, monthCodeCol, monthNameCol, monthQuantityCol, monthTotalCol;
 	
	// panes for table
	FlowPane soldPane, carPane, monthTablePane;
	
	// table stack pane
	StackPane stackTable;
	
	// add new data
	TextField addCodeField;
	TextField addNameField;
	TextField addQuantityField;
	TextField addPriceField;
	Button addBtn;
	Label chooseUpdateLbl;
	ComboBox<String> cbUpdate;
	FlowPane addNewPane, updatePane;
	Label operationLbl =new Label();
	
	// data base
	Connection con ;
	Statement stmt;
	ResultSet rs;
	
	// alert
	TextInputDialog dialog ;
	
	// base pane
	VBox basePane;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void start(Stage stage)
	{
		String backColor = "lightgreen", btnColor = "lightgreen";
		date = new Date();
		numberFormat = NumberFormat.getNumberInstance(Locale.UK);
		
		// data base connection
		con = getConnection();
		
		// ObservableList
		carDataList = FXCollections.observableArrayList();
		searchCarDataList = FXCollections.observableArrayList();
		saleDataList = FXCollections.observableArrayList();
		monthDataList = FXCollections.observableArrayList();
		
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from CarTable");
			while(rs.next())
			{
				carDataList.add(new CarData(no, rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3)), formatPrice(rs.getString(4)) ) );
				++no;
			}
			rs = stmt.executeQuery("select * from SaleTable");
			while(rs.next())
			{
				saleDataList.add(new SaleData(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), formatPrice(rs.getString(5)), formatPrice(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9)) );
				++saleNo;
			}
		} catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		// Controls
		toBuyBtn = new Button("ေရာင္​းရန္");
		afterBuyBtn = new Button("ေရာင္​းၿပီးပစၥည္း");
		newBtn = new Button("အသစ္​ထည့္");
		updateBtn = new Button("ျပင္​မည္");
		deleteBtn = new Button("ဖ်က္​မည္");
		monthlyBtn = new Button("လခ်ဳပ္​စာရင္​း");
		miniBtn = new Button("_");
		exitBtn = new Button("X");
		
		// set button properties
		toBuyBtn.setPrefSize(150, 50);
		afterBuyBtn.setPrefSize(150, 50);
		newBtn.setPrefSize(150, 50);
		updateBtn.setPrefSize(150, 50);
		deleteBtn.setPrefSize(150, 50);
		monthlyBtn.setPrefSize(150, 50);
		miniBtn.setPrefSize(70, 50);
		exitBtn.setPrefSize(70, 50);
		
		// setting font
		toBuyBtn.setFont(mmFont);
		afterBuyBtn.setFont(mmFont);
		newBtn.setFont(mmFont);
		updateBtn.setFont(mmFont);
		deleteBtn.setFont(mmFont);
		monthlyBtn.setFont(mmFont);
		miniBtn.setFont(new Font("Arial", 20));
		exitBtn.setFont(new Font("Arial", 20));
		
		toBuyBtn.setStyle("-fx-background-color:orange;");
		toBuyBtn.setEffect(shadowEffect);
		afterBuyBtn.setStyle("-fx-background-color:" + btnColor + ";");
		newBtn.setStyle("-fx-background-color:" + btnColor + ";");
		updateBtn.setStyle("-fx-background-color:" + btnColor + ";");
		deleteBtn.setStyle("-fx-background-color:" + btnColor + ";");
		monthlyBtn.setStyle("-fx-background-color:" + btnColor + ";");
		miniBtn.setStyle("-fx-background-color:" + btnColor);
		exitBtn.setStyle("-fx-background-color:" + btnColor);
		
		// Mneu Btn pane
		FlowPane menuBtnPane = new FlowPane(toBuyBtn, afterBuyBtn, newBtn, updateBtn,deleteBtn,monthlyBtn);
		menuBtnPane.setStyle("-fx-background-color:"+backColor+";");
		menuBtnPane.setHgap(1);
		menuBtnPane.setPrefHeight(paneHeight);
		
		menuHbox = new HBox();
		HBox.setHgrow(menuBtnPane, Priority.ALWAYS);
		menuHbox.getChildren().addAll(menuBtnPane, miniBtn,exitBtn);
		menuHbox.setStyle("-fx-background-color: " + backColor);
		
		// search pane
		searchField = new TextField();
		searchField.setPrefSize(300, 40);
		searchField.setFont(mmFont);
		searchField.setPromptText("အမ်ိဳးအမည္"+ "ျဖင့္  ရွာ​ေဖြမည္ . . .");
		
		btnSearch = new Button("ရွာပါ");
		btnSearch.setPrefHeight(40);
		btnSearch.setPrefWidth(70);
		btnSearch.setFont(mmFont);
		
		cbSearch = new ComboBox<String>();
		cbSearch.setPrefHeight(40);
		cbSearch.getItems().addAll("ကုဒ္","အမ်ိဳးအမည္");
		cbSearch.getSelectionModel().selectLast();
		cbSearch.setPadding(new Insets(0,10,0,10));
		cbSearch.setStyle("-fx-font: normal normal 15px 'Zawgyi-One'");
		
		btnShowAll = new Button("အကုန္​ျပ");
		btnShowAll.setPadding(new Insets(5,30,5,25));
		btnShowAll.setPrefHeight(40);
		btnShowAll.setFont(mmFont);
		
		btnLack = new Button("4 ခု ေအာက္ နည္းေသာ ပစၥည္းမ်ား");
		btnLack.setPadding(new Insets(0,50,0,50));
		btnLack.setFont(mmFont);
		btnLack.setPrefHeight(40);
		
		cbLack = new ComboBox<String>();
		cbLack.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
		cbLack.setPrefHeight(40);
		cbLack.setPadding(new Insets(0,0,0,10));
		cbLack.setStyle("-fx-font: normal normal 15px Ariel");
		cbLack.getSelectionModel().select(3);
		
		searchPane = new FlowPane(searchField, btnSearch, cbSearch,btnShowAll,btnLack, cbLack);
		FlowPane.setMargin(searchField, new Insets(20,0,10,20));
		FlowPane.setMargin(btnSearch, new Insets(20,0,10,0));
		FlowPane.setMargin(cbSearch, new Insets(20,40,10,20));
		FlowPane.setMargin(btnLack, new Insets(20,10,10,180));
		FlowPane.setMargin(btnShowAll, new Insets(20,0,10,0));
		FlowPane.setMargin(cbLack, new Insets(20,0,10,0));
		searchPane.setStyle("-fx-background-color: lightblue");
		searchPane.setPrefHeight(40);
		
		// search for monthly
		btnMonth = new Button((date.getMonth()+1) +" လပိုင္း စာရင္းခ်ဳပ္");
		btnMonth.setPadding(new Insets(15,100,15,100));
		btnMonth.setPrefHeight(40);
		btnMonth.setFont(mmFont);
		
		cbMonth = new ComboBox<String>();
		cbMonth.setPrefHeight(40);
		cbMonth.setStyle("-fx-font: normal normal 15px 'Zawgyi-One'");
		cbMonth.setPadding(new Insets(10,10,10,10));
		cbMonth.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
		cbMonth.getSelectionModel().select(date.getMonth());
		
		cbYear = new ComboBox<String>();
		cbYear.setPrefHeight(40);
		cbYear.setStyle("-fx-font: normal normal 15px 'Zawgyi-One'");
		cbYear.setPadding(new Insets(10,10,10,10));
		cbYear.getItems().addAll("2017","2018","2019");
		cbYear.getSelectionModel().selectFirst();
		
		monthPane = new FlowPane(btnMonth, cbMonth, cbYear);
		FlowPane.setMargin(btnMonth, new Insets(30,30,10,20));
		FlowPane.setMargin(cbMonth, new Insets(30,10,10,0));
		FlowPane.setMargin(cbYear, new Insets(30,0,10,0));
		monthPane.setStyle("-fx-background-color: lightblue");
		monthPane.setPrefHeight(40);
		monthPane.setVisible(false);
		
		// stack for searching 
		stackSearch = new StackPane(searchPane, monthPane);
		stackSearch.setPrefHeight(50);
		
		// car table pane
		carTable =  new TableView<CarData>();
		noCol = new TableColumn<>("စဥ္");
		codeCol = new TableColumn<>("ကုဒ္");
		nameCol = new TableColumn<>("အမ်ိဳးအမည္");
		quantityCol = new TableColumn<>("အ​ေရအတြက္");
		priceCol = new TableColumn<>("တန္​ဖိုး");
		saleCol = new TableColumn<>("");
		
		noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("maxQuantity"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		saleCol.setCellValueFactory(new PropertyValueFactory<>("saleCol"));
		
		noCol.setPrefWidth(50);
		codeCol.setPrefWidth(250);
		nameCol.setPrefWidth(500);
		quantityCol.setPrefWidth(120);
		priceCol.setPrefWidth(180);
		saleCol.setPrefWidth(160);

		quantityCol.setStyle("-fx-padding: 0 20 0 0;");
		priceCol.setStyle("-fx-padding: 0 20 0 0;");
		
		// to sale column
		Callback< TableColumn<CarData, String>, TableCell<CarData, String> > saleColCellFactory = 
				new Callback< TableColumn<CarData,String>, TableCell<CarData,String> >()
		{
			@Override
			public TableCell<CarData, String> call(final TableColumn<CarData, String> param)
			{
				final TableCell<CarData, String> tableCell = new TableCell<CarData, String>()
				{
					Button btnSale = new Button("ေရာင္းမည္");
					String oldStyle = null ;
					@Override
					public void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						if(empty)
						{
							setGraphic(null);
							setText(null);
						}
						else
						{
							// sale Button Action 
							btnSale.setStyle("-fx-background-color: lime");
							btnSale.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
								btnSale.setEffect(shadowEffect);
								oldStyle = getTableRow().getStyle();
							});
							btnSale.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
								btnSale.setEffect(null);
								getTableRow().setStyle(oldStyle);
							});
							btnSale.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
								btnSale.setStyle("-fx-background-color: greenyellow");
								getTableRow().setStyle("-fx-background-color: orange;"); // lightpink -> lightcoral
							});
							btnSale.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
								btnSale.setStyle("-fx-background-color: lime");
							});
							btnSale.setOnAction(e -> saleBtnClick( getTableView().getItems().get( getIndex() ) ) );
							setGraphic(btnSale);
							setText(null);
						}
					}
				};
				return tableCell;
			}
		};
		saleCol.setCellFactory(saleColCellFactory);
		
		noCol.setStyle("-fx-alignment: center;");
		codeCol.setStyle("-fx-alignment: center;");
		nameCol.setStyle("-fx-alignment: center;");
		saleCol.setStyle("-fx-alignment: center");
		
		carTable.setItems(carDataList);
		carTable.getStylesheets().add("tableStyle.css");
		carTable.getColumns().addAll(noCol,codeCol,nameCol,quantityCol,priceCol, saleCol);
		carTable.setPadding(new Insets(20,20,10,20));
		carTable.setPrefSize(1300,450);
		carTable.setPlaceholder(new Label("ေရာင္းစရာ ပစၥည္း   မရွိေသးပါ"));
		carTable.setEditable(true);
		
		carTable.setRowFactory(tv -> new TableRow<CarData>() {
		    @Override
		    public void updateItem(CarData item, boolean empty) {
		        super.updateItem(item, empty) ;
		        if (item == null) {
		            setStyle("");
		        } else if (item.getMaxQuantity() < Integer.parseInt(cbLack.getValue())) {
		            setStyle("-fx-background-color: red; -fx-border-color: gainsboro; -fx-table-cell-border-color: transparent;");
		        } else {
		            setStyle("");
		        }
		    }
		    
		});
		
		carPane = new FlowPane(carTable);
		carTable.setStyle("-fx-background-color: lightblue");
		carPane.setStyle("-fx-background-color: lightblue");
		
		// sold table
		soldTable = new TableView<>();
		soldNoCol = new TableColumn<SaleData, String>("စဥ္");
		soldCodeCol = new TableColumn<>("ကုဒ္");
		soldNameCol = new TableColumn<>("အမ်ိဳးအမည္");
		soldQuantityCol = new TableColumn<>("အ​ေရအတြက္");
		soldPriceCol = new TableColumn<>("တန္​ဖိုး");
		soldTotalCol = new TableColumn<SaleData, String>("စုစုေပါင္း တန္ဖိုး");
		dateCol = new TableColumn<SaleData, String>("ရက္စြဲ");
		dayCol = new TableColumn<SaleData, String>("ရက္");
		monthCol = new TableColumn<SaleData, String>("လ");
		yearCol = new TableColumn<SaleData, String>("ႏွစ္");
		unSaleCol = new TableColumn<>("Unsold");
		
		dateCol.getColumns().addAll(dayCol, monthCol, yearCol);
		
		soldNoCol.setStyle("-fx-alignment: center");
		soldCodeCol.setStyle("-fx-alignment: center");
		soldNameCol.setStyle("-fx-alignment: center");
		soldQuantityCol.setStyle("-fx-alignment: center");
		dayCol.setStyle("-fx-alignment: center");
		monthCol.setStyle("-fx-alignment: center");
		yearCol.setStyle("-fx-alignment: center");
		unSaleCol.setStyle("-fx-alignment: center");
		
		soldNoCol.setCellValueFactory(new PropertyValueFactory<>("no"));
		soldCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		soldNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		soldQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		soldPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		soldTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
		dayCol.setCellValueFactory(new PropertyValueFactory<>("day"));
		monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		unSaleCol.setCellValueFactory(new PropertyValueFactory<>("unSaleCol"));
		
		soldNoCol.setPrefWidth(50);
		soldCodeCol.setPrefWidth(250);
		soldNameCol.setPrefWidth(310);
		soldQuantityCol.setPrefWidth(120);
		soldPriceCol.setPrefWidth(120);
		soldTotalCol.setPrefWidth(150);
		dateCol.setPrefWidth(190);
		dayCol.setPrefWidth(50);
		monthCol.setPrefWidth(50);
		yearCol.setPrefWidth(90);
		unSaleCol.setPrefWidth(120);
		
		soldQuantityCol.setStyle("-fx-padding: 0 20 0 0;");
		soldPriceCol.setStyle("-fx-padding: 0 20 0 0;");
		soldTotalCol.setStyle("-fx-padding: 0 40 0 0;");
		
		// to unsale action
		Callback< TableColumn<SaleData, String>, TableCell<SaleData, String> > unSaleColCellFactory = 
				new Callback< TableColumn<SaleData,String>, TableCell<SaleData,String> >()
		{
			@Override
			public TableCell<SaleData, String> call(final TableColumn<SaleData, String> param)
			{
				final TableCell<SaleData, String> tableCell = new TableCell<SaleData, String>()
				{
					Button btnUnSale = new Button("ျပန္သိမ္းမည္");
					String oldStyle = null ;
					@Override
					public void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						if(empty)
						{
							setGraphic(null);
							setText(null);
						}
						else
						{
							// sale Button Action 
							btnUnSale.setStyle("-fx-background-color: aquamarine");
							btnUnSale.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
								btnUnSale.setEffect(shadowEffect);
								oldStyle = getTableRow().getStyle();
							});
							btnUnSale.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
								btnUnSale.setEffect(null);
								getTableRow().setStyle(oldStyle);
							});
							btnUnSale.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
								btnUnSale.setStyle("-fx-background-color: deepskyblue;");
								getTableRow().setStyle("-fx-background-color: orange;"); // lightpink -> lightcoral
							});
							btnUnSale.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
								btnUnSale.setStyle("-fx-background-color: aquamarine");
							});
							btnUnSale.setOnAction(e -> unSaleBtnClick( getTableView().getItems().get( getIndex() ) ) );
							setGraphic(btnUnSale);
							setText(null);
						}
					}
				};
				return tableCell;
			}
		};
		unSaleCol.setCellFactory(unSaleColCellFactory);
		
		soldTable.setItems(saleDataList);
		soldTable.getStylesheets().add("soldTableStyle.css");
		soldTable.getColumns().addAll(soldNoCol, soldCodeCol, soldNameCol, soldQuantityCol, soldPriceCol, soldTotalCol, dateCol, unSaleCol);
		soldTable.setPlaceholder(new Label("ေရာင္းထားေသာ ပစၥည္း   မရွိေသးပါ"));
		soldTable.setPadding(new Insets(20,20,20,20));
		soldTable.setPrefSize(1350, 470);
		soldTable.setEditable(true);
		soldTable.setStyle("-fx-background-color: lightblue");
		
		soldPane = new FlowPane(soldTable);
		soldPane.setStyle("-fx-background-color: lightblue");
		soldPane.setVisible(false);
		
		// monthly table
		monthNoCol = new TableColumn<>("စဥ္");
		monthCodeCol = new TableColumn<>("ကုဒ္");
		monthNameCol = new TableColumn<>("အမ်ိဳးအမည္");
		monthQuantityCol = new TableColumn<>("အ​ေရအတြက္");
		monthTotalCol = new TableColumn<>("စုစုေပါင္း တန္ဖိုး");
		
		monthNoCol.setCellValueFactory(new PropertyValueFactory<>("no"));
		monthCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		monthNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		monthQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		monthTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
		
		monthNoCol.setStyle("-fx-alignment: center");
		monthCodeCol.setStyle("-fx-alignment: center");
		monthNameCol.setStyle("-fx-alignment: center");
		monthQuantityCol.setStyle("-fx-alignment: center");
		monthTotalCol.setStyle("-fx-padding: 0 30 0 0;");
		
		monthNoCol.setPrefWidth(50);
		monthCodeCol.setPrefWidth(250);
		monthNameCol.setPrefWidth(350);
		monthQuantityCol.setPrefWidth(120);
		monthTotalCol.setPrefWidth(200);
		
		monthTable = new TableView<MonthData>();
		monthTable.setItems(monthDataList);
		monthTable.getColumns().addAll(monthNoCol, monthCodeCol, monthNameCol, monthQuantityCol, monthTotalCol );
		monthTable.setPadding(new Insets(20,20,20,20));
		monthTable.setPrefSize(1000, 470);
		monthTable.setEditable(false);
		monthTable.setStyle("-fx-background-color: lightblue");
		monthTable.getStylesheets().add("monthTableStyle.css");
		
		monthTablePane = new FlowPane(monthTable);
		monthTablePane.setStyle("-fx-background-color: lightblue");
		monthTablePane.setVisible(false);
		monthTablePane.setAlignment(Pos.CENTER);
		
		// stack table Pane
		stackTable = new StackPane(carPane,soldPane,monthTablePane);
		stackTable.setPadding(new Insets(0,0,0,0));
		StackPane.setMargin(carPane, new Insets(0,0,10,0));
		
		// new data adding
		addCodeField = new TextField();
		addNameField = new TextField();
		addQuantityField = new TextField();
		addPriceField = new TextField();
		addBtn = new Button("အသစ္​ထည့္");
		
		addCodeField.setTooltip(new Tooltip("ကုဒ္ ျဖည္​့ပါ"));
		addNameField.setTooltip(new Tooltip("အမ်ိဳးအမည္ ျဖည္​့ပါ"));
		addQuantityField.setTooltip(new Tooltip("အေရအတြက္ ျဖည္​့ပါ"));
		addPriceField.setTooltip(new Tooltip("တန္​ဖိုး ျဖည္​့ပါ"));
		
		addCodeField.setPrefSize(150, 45);
		addNameField.setPrefSize(150, 45);
		addQuantityField.setPrefSize(150, 45);
		addPriceField.setPrefSize(150, 45);
		addBtn.setPrefSize(150, 45);
		
		addCodeField.setPromptText("ကုဒ္");
		addNameField.setPromptText("အမ်ိဳးအမည္");
		addQuantityField.setPromptText("အ​ေရအတြက္");
		addPriceField.setPromptText("တန္​ဖိုး");
		
		addCodeField.setStyle("-fx-border-color: black;");
		addNameField.setStyle("-fx-border-color: black;");
		addQuantityField.setStyle("-fx-border-color: black;");
		addPriceField.setStyle("-fx-border-color: black;");
		
		addCodeField.setFont(mmFont);
		addNameField.setFont(mmFont);
		addQuantityField.setFont(mmFont);
		addPriceField.setFont(mmFont);
		addBtn.setFont(mmFont);
		addBtn.setTextFill(Color.WHITE);
		
		// choose to update
		chooseUpdateLbl = new Label("ျပင္​လိုသည္​ကို ​ေ႐ြးပါ");
		chooseUpdateLbl.setFont(mmFont);
		
		cbUpdate = new ComboBox<String>();
		cbUpdate.getItems().addAll("ကုဒ္","အမ်ိဳးအမည္","အ​ေရအတြက္","တန္​ဖိုး");
		cbUpdate.getSelectionModel().selectFirst();
		cbUpdate.setPrefHeight(40);
		cbUpdate.setStyle("-fx-background-color: white; -fx-font: normal normal 15px 'Zawgyi-One'; -fx-border-color: cornflowerblue;");
		cbUpdate.setPadding(new Insets(5,10,5,10));
		
		updatePane = new FlowPane(chooseUpdateLbl, cbUpdate);
		FlowPane.setMargin(cbUpdate, new Insets(0,0,0,20));
		updatePane.setPadding(new Insets(10,10,10,10));
		updatePane.setStyle("-fx-background-color: lightblue;");
		updatePane.setVisible(false);
		
		addBtn.setOnAction(e -> {
			String query = "";
			// to insert new car data
			if(menuIndex == 2)
			{
				if(isValid() && !isExistingCode())
				{
					query = "insert into CarTable values('" + addCodeField.getText() + "', '" + addNameField.getText() + "', " + addQuantityField.getText() + ", " + addPriceField.getText() + ")";
					try
					{
						stmt.execute(query);
						showOperation("အသစ္​ထည့္​ၿပီးပါၿပီ",success);
						carDataList.add( new CarData(no++, addCodeField.getText() , addNameField.getText(), Integer.parseInt(addQuantityField.getText()), formatPrice(addPriceField.getText()) ) );
						addCodeField.clear();
						addNameField.clear();
						addQuantityField.clear();
						addPriceField.clear();
					} catch (SQLException e1)
					{
						e1.printStackTrace();
						showOperation("အသစ္​ထည့္ျခင္း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
					}
				}
			}
			
			// to update existing car data
			else if(menuIndex == 3)
			{
				query = ""; 
				String selectStr = cbUpdate.getValue();
				if(selectStr.equals("ကုဒ္"))
				{
					if(addCodeField.getText().equals("") || addNameField.getText().equals(""))
						showOperation("Box အားလုံး​ကို ျဖည္​့ပါ", fail);
					else
					{
						boolean flag = true;
						for(CarData cd : carDataList)
						{
							if(!cd.getName().equals(addNameField.getText()) && cd.getCode().equals(addCodeField.getText()) )
							{
								showOperation("ရွိၿပီးသား ကုဒ္​ျဖစ္​လို႔ မရပါ", fail);
								flag = false;
							}
						}
						if(flag)
						{
							query = "update CarTable set CODE = '" + addCodeField.getText() + "' where NAME like '" + addNameField.getText() + "'";
							try
							{
								stmt.execute(query);
								int index = 0;
								for(CarData cd : carDataList)
									if(cd.getName().equals(addNameField.getText()))
									{
										index = cd.getNo();
										carDataList.set(index-1, new CarData(index,addCodeField.getText(),cd.getName(), cd.getMaxQuantity(), cd.getPrice()));
										break;
									}
								showOperation("ျပင္​ၿပီးပါၿပီ", success);
							}catch(Exception e1)
							{
								e1.printStackTrace();
								showOperation("ျပင္​ျခင္​း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
							}
						}
					}
				}
				
				else if(selectStr.equals("အမ်ိဳးအမည္"))
				{
					if(addCodeField.getText().equals("") || addNameField.getText().equals(""))
						showOperation("Box အားလုံး​ကို ျဖည္​့ပါ", fail);
					else
					{
						boolean flag = false;
						for(CarData cd: carDataList)
							if(cd.getCode().equals(addCodeField.getText()))
							{
								flag = true;
								break;
							}
						if(flag)
						{
							query = "update CarTable set NAME = '" + addNameField.getText() + "' where CODE like '"+ addCodeField.getText() + "'";
							try
							{
								stmt.execute(query);
								for(CarData cd : carDataList)
									if(cd.getCode().equals(addCodeField.getText()))
									{
										carDataList.set(cd.getNo()-1, new CarData(cd.getNo(), cd.getCode(), addNameField.getText(), cd.getMaxQuantity(), cd.getPrice()));
										break;
									}
								showOperation("ျပင္​ၿပီးပါၿပီ", success);
							} catch (Exception e2)
							{
								e2.printStackTrace();
								showOperation("ျပင္​ျခင္​း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
							}
						}
						else
						{
							showOperation("ျပင္​လို​ေသာ အမ်ိဳးအမည္၏ ကုဒ္​ ရွာမ​ေတြ႕ပါ", fail);
						}
					}
				}
				else if(selectStr.equals("အ​ေရအတြက္"))
				{
					if(addCodeField.getText().equals("") || addQuantityField.getText().equals(""))
						showOperation("Box အားလုံး​ကို ျဖည္​့ပါ", fail);
					else
					{
						boolean flag = false;
						for(CarData cd: carDataList)
							if(cd.getCode().equals(addCodeField.getText()))
							{
								flag = true;
								break;
							}
						if(flag)
						{
							try
							{
								Integer.parseInt(addQuantityField.getText());
								query = "update CarTable set QUANTITY = " + addQuantityField.getText() + " where CODE like '" + addCodeField.getText() + "'";
								stmt.execute(query);
								for(CarData cd : carDataList)
									if(cd.getCode().equals(addCodeField.getText()))
									{
										carDataList.set(cd.getNo()-1, new CarData(cd.getNo(), cd.getCode(), cd.getName(), Integer.parseInt(addQuantityField.getText()), cd.getPrice()));
										break;
									}
								showOperation("ျပင္​ၿပီးပါၿပီ", success);
							}catch(NumberFormatException e4)
							{
								showOperation("အ​ေရအတြက္မွာ  "+"ကိန္​းဂဏန္​း​ေတြသာ  ျဖည္​့လိုရပါမယ္​", fail);
								e4.printStackTrace();
							}catch(Exception e3)
							{
								e3.printStackTrace();
								showOperation("ျပင္​ျခင္​း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
							}
						}
						else
						{
							showOperation("ျပင္လို​ေသာ အ​ေရအတြက္၏ ကုဒ္​ ရွာမ​ေတြ႕ပါ", fail);
						}
					}
				}
				else // price
				{
					if(addPriceField.getText().equals("") || addCodeField.getText().equals(""))
						showOperation("Box အားလုံး​ကို ျဖည္​့ပါ", fail);
					else
					{
						boolean flag = false;
						for(CarData cd: carDataList)
							if(cd.getCode().equals(addCodeField.getText()))
							{
								flag = true;
								break;
							}
						if(flag)
						{
							try
							{
								Double.parseDouble(addPriceField.getText());
								query = "update CarTable set PRICE = " + addPriceField.getText() + " where CODE like '" + addCodeField.getText() + "'";
								stmt.execute(query);
								for(CarData cd : carDataList)
									if(cd.getCode().equals(addCodeField.getText()))
									{
										carDataList.set(cd.getNo()-1, new CarData(cd.getNo(), cd.getCode(), cd.getName(), cd.getMaxQuantity(), formatPrice(addPriceField.getText())));
										break;
									}
								showOperation("ျပင္​ၿပီးပါၿပီ", success);
							}catch(NumberFormatException e5)
							{
								showOperation("တန္​ဖိုးမွာ" + "ကိန္​းဂဏန္​း​ေတြသာ  ျဖည္​့လိုရပါမယ္​", fail);
								e5.printStackTrace();
							}catch(Exception e3)
							{
								e3.printStackTrace();
								showOperation("ျပင္​ျခင္​း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
							}
						}
						else
						{
							showOperation("ျပင္လို​ေသာ တန္​ဖိုး၏ ကုဒ္​ ရွာမ​ေတြ႕ပါ", fail);
						}
					}
				}
			}
			// delete action
			else if(menuIndex == 4)
			{
				if(addCodeField.getText().equals(""))
					showOperation("ဖ်က္လိုေသာ ပစၥည္​း၏ ကုဒ္​ကို ရိုက္​ထည္​့ပါ", fail);
				else
				{
					boolean flag = false;
					for(CarData cd: carDataList)
						if(cd.getCode().equals(addCodeField.getText()))
						{
							flag = true;
							break;
						}
					if(flag)
					{
						query = "delete from CarTable where CODE like '" + addCodeField.getText() + "'";
						try
						{
							stmt.execute(query);
							int foundIndex = 0;
							for(CarData cd: carDataList)
							{
								if(cd.getCode().equals(addCodeField.getText()))
								{
									carDataList.remove(cd.getNo()-1);
									foundIndex = cd.getNo()-1;
									break;
								}
							}
							for(int i = foundIndex; i < carDataList.size(); i++)
							{
								CarData cd = carDataList.get(i);
								carDataList.set(i, new CarData(i+1,cd.getCode(), cd.getName(), cd.getMaxQuantity(), cd.getPrice()));
							}
							--no;
							showOperation("ဖ်က္​ၿပီးပါၿပီ", success);
						}catch(Exception e1)
						{
							e1.printStackTrace();
							showOperation("ဖ်က္ျခင္း မ​ေအာင္​ျမင္​ပါ ( Database error )", fail);
						}
					}
					else
					{
						showOperation("ဖ်က္လိုေသာ  ပစၥည္​း၏ ကုဒ္​ ရွာမ​ေတြ႕", fail);
					}
				}
			}
		});
		
		addNewPane = new FlowPane(addCodeField, addNameField, addQuantityField, addPriceField, addBtn, updatePane);
		addNewPane.setStyle("-fx-background-color: lightblue;");
		addNewPane.setPadding(new Insets(10,10, 10,10));
		FlowPane.setMargin(updatePane, new Insets(0,20,0,0));
		addNewPane.setHgap(5);
		addNewPane.setVisible(false);
		
		// get screen size
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();
		
		// database operation status
		operationLbl.setPrefSize(screen.getWidth(),20);
		operationLbl.setPadding(new Insets(10,0,10,20));
		operationLbl.setAlignment(Pos.CENTER);
		operationLbl.setFont(mmFont);
		operationLbl.setVisible(false);

		// all event
		setEvent();
		
		miniBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			miniBtn.setStyle("-fx-background-color : cornflowerblue;" );
		});
		miniBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			miniBtn.setStyle("-fx-background-color: lightgreen;" );
		});
		miniBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			miniBtn.setStyle("-fx-background-color: blue;");
		});
		miniBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			miniBtn.setStyle("-fx-background-color: cornflowerblue;");
			( (Stage) ((Node)e.getSource()).getScene().getWindow() ).setIconified(true);
		});
		
		exitBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			exitBtn.setStyle("-fx-background-color : red;" );
		});
		exitBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			exitBtn.setStyle("-fx-background-color: lightgreen;" );
		});
		exitBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			exitBtn.setStyle("-fx-background-color: darkred;");
		});
		exitBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			exitBtn.setStyle("-fx-background-color: red;");
			stage.close();
		});
		
		btnSearch.setOnAction(e -> searchAction());
		
		btnShowAll.setOnAction(e -> {
			carTable.setItems(carDataList);
		});
		
		btnLack.setOnAction(e -> {
			int lackInt = Integer.parseInt(cbLack.getValue());
			if(searchCarDataList.size() != 0)
				searchCarDataList.removeAll(searchCarDataList);
			for(CarData cd: carDataList)
			{
				if(cd.getMaxQuantity() < lackInt)
					searchCarDataList.add(cd);
			}
			carTable.setItems(searchCarDataList);
		});
		
		// Base pane
		basePane = new VBox(menuHbox, stackSearch, stackTable, addNewPane, operationLbl);
		basePane.setStyle("-fx-background-color: lightblue");
		VBox.setMargin(menuBtnPane, new Insets(2,0,5,0));
		VBox.setMargin(addNewPane, new Insets(0,80,5,20));
		VBox.setVgrow(stackTable, Priority.ALWAYS);
		
		// Scene
		Scene scene = new Scene(basePane,screen.getWidth(), screen.getHeight());
		
		stage.setResizable(false);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle("နီလာစိန္​ ကားပစၥည္​းဆိုင္");
		stage.show();
		
		// alert
		dialog = new TextInputDialog();
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
	}
	
	public void setEvent()
	{
		// toBuyBtn
		toBuyBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			toBuyBtn.setEffect(shadowEffect);
			toBuyBtn.setStyle("-fx-background-color: orange");
		});
		toBuyBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			toBuyBtn.setStyle("-fx-background-color: lightgreen;");
			if(menuIndex == 0)
			{
				toBuyBtn.setEffect(shadowEffect);
				toBuyBtn.setStyle("-fx-background-color: orange");
			}
			else
				toBuyBtn.setEffect(null);
		});
		toBuyBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(monthPane.isVisible())
				monthPane.setVisible(false);
			if(!searchPane.isVisible())
				searchPane.setVisible(true);
			toBuyBtn.setStyle("-fx-background-color: orange");
			if(!carPane.isVisible())
				carPane.setVisible(true);
			if(soldPane.isVisible())
				soldPane.setVisible(false);
			if(monthTablePane.isVisible())
				monthTablePane.setVisible(false);
			addNewPane.setVisible(false);
			switch(menuIndex)
			{
			case 1: afterBuyBtn.setStyle("-fx-background-color: lightgreen;"); afterBuyBtn.setEffect(null); break;
			case 2: newBtn.setStyle("-fx-background-color: lightgreen;"); newBtn.setEffect(null);break;
			case 3: updateBtn.setStyle("-fx-background-color: lightgreen;"); updateBtn.setEffect(null); break;
			case 4: deleteBtn.setStyle("-fx-background-color: lightgreen;"); deleteBtn.setEffect(null); break;
			case 5: monthlyBtn.setStyle("-fx-background-color: lightgreen;"); monthlyBtn.setEffect(null); break;
			}
			menuIndex = 0;
		});
		
		// afterBuyBtn
		afterBuyBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			afterBuyBtn.setEffect(shadowEffect);
			afterBuyBtn.setStyle("-fx-background-color: orange");
		});
		afterBuyBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			afterBuyBtn.setStyle("-fx-background-color: lightgreen");
			if( menuIndex == 1)
			{
				afterBuyBtn.setEffect(shadowEffect);
				afterBuyBtn.setStyle("-fx-background-color: orange");
			}
			else
				afterBuyBtn.setEffect(null);
		});
		afterBuyBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(searchPane.isVisible())
				searchPane.setVisible(false);
			if(!soldPane.isVisible())
				soldPane.setVisible(true);
			if(monthTablePane.isVisible())
				monthTablePane.setVisible(false);
			afterBuyBtn.setStyle("-fx-background-color: orange");
			addNewPane.setVisible(false);
			switch(menuIndex)
			{
			case 0: toBuyBtn.setStyle("-fx-background-color: lightgreen;"); toBuyBtn.setEffect(null); break;
			case 2: newBtn.setStyle("-fx-background-color: lightgreen;"); newBtn.setEffect(null);break;
			case 3: updateBtn.setStyle("-fx-background-color: lightgreen;"); updateBtn.setEffect(null); break;
			case 4: deleteBtn.setStyle("-fx-background-color: lightgreen;"); deleteBtn.setEffect(null); break;
			case 5: monthlyBtn.setStyle("-fx-background-color: lightgreen;"); monthlyBtn.setEffect(null); break;
			}
			menuIndex = 1;
		});
		
		// newBtn
		newBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			newBtn.setEffect(shadowEffect);
			newBtn.setStyle("-fx-background-color: orange");
		});
		newBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			newBtn.setStyle("-fx-background-color: lightgreen");
			if(menuIndex == 2)
			{
				newBtn.setEffect(shadowEffect);
				newBtn.setStyle("-fx-background-color: orange");
			}
			else
				newBtn.setEffect(null);
		});
		newBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(monthPane.isVisible())
				monthPane.setVisible(false);
			if(!searchPane.isVisible())
				searchPane.setVisible(true);
			if(!carPane.isVisible())
				carPane.setVisible(true);
			if(soldPane.isVisible())
				soldPane.setVisible(false);
			if(monthTablePane.isVisible())
				monthTablePane.setVisible(false);
			
			newBtn.setStyle("-fx-background-color: orange");
			
			updatePane.setVisible(false);
			
			addBtn.setText("အသစ္​ထည့္ပါ");
			addBtn.setStyle("-fx-background-color : forestgreen;");
			
			addCodeField.clear();
			addNameField.clear();
			addQuantityField.clear();
			addPriceField.clear();
			
			addCodeField.setVisible(true);
			addNameField.setVisible(true);
			addQuantityField.setVisible(true);
			addPriceField.setVisible(true);
			
			addNewPane.setVisible(true);
			addNewPane.setStyle("-fx-background-color: lightblue; -fx-border-color: green;");
			
			if(carTable.getItems() == searchCarDataList)
				carTable.setItems(carDataList);
			
			switch(menuIndex)
			{
			case 0: toBuyBtn.setStyle("-fx-background-color: lightgreen;"); toBuyBtn.setEffect(null); break;
			case 1: afterBuyBtn.setStyle("-fx-background-color: lightgreen;"); afterBuyBtn.setEffect(null); break;
			case 3: updateBtn.setStyle("-fx-background-color: lightgreen;"); updateBtn.setEffect(null); break;
			case 4: deleteBtn.setStyle("-fx-background-color: lightgreen;"); deleteBtn.setEffect(null); break;
			case 5: monthlyBtn.setStyle("-fx-background-color: lightgreen;"); monthlyBtn.setEffect(null); break;
			}
			menuIndex = 2;
		});
		
		// updateBtn
		updateBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			updateBtn.setEffect(shadowEffect);
			updateBtn.setStyle("-fx-background-color: orange");
		});
		updateBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			updateBtn.setStyle("-fx-background-color: lightgreen");
			if(menuIndex == 3)
			{
				updateBtn.setEffect(shadowEffect);
				updateBtn.setStyle("-fx-background-color: orange");
			}
			else
				updateBtn.setEffect(null);
		});
		updateBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(monthPane.isVisible())
				monthPane.setVisible(false);
			if(!searchPane.isVisible())
				searchPane.setVisible(true);
			if(!carPane.isVisible())
				carPane.setVisible(true);
			if(soldPane.isVisible())
				soldPane.setVisible(false);
			if(monthTablePane.isVisible())
				monthTablePane.setVisible(false);
			
			updateBtn.setStyle("-fx-background-color: orange");
			addBtn.setText("ျပင္ပါ");
			addBtn.setStyle("-fx-background-color : darkslateblue;");
			
			updatePane.setVisible(true);
			
			addCodeField.clear();
			addNameField.clear();
			addQuantityField.clear();
			addPriceField.clear();
			
			addQuantityField.setVisible(false);
			addPriceField.setVisible(false);
			addNameField.setVisible(true);
			
			addNewPane.setVisible(true);
			addNewPane.setStyle("-fx-background-color: lightblue; -fx-border-color: blue");
			
			if(carTable.getItems() == searchCarDataList)
				carTable.setItems(carDataList);
			
			switch(menuIndex)
			{
			case 0: toBuyBtn.setStyle("-fx-background-color: lightgreen;"); toBuyBtn.setEffect(null); break;
			case 1: afterBuyBtn.setStyle("-fx-background-color: lightgreen;"); afterBuyBtn.setEffect(null); break;
			case 2: newBtn.setStyle("-fx-background-color: lightgreen;"); newBtn.setEffect(null);break;
			case 4: deleteBtn.setStyle("-fx-background-color: lightgreen;"); deleteBtn.setEffect(null); break;
			case 5: monthlyBtn.setStyle("-fx-background-color: lightgreen;"); monthlyBtn.setEffect(null); break;
			}
			menuIndex = 3;
		});
		
		// deleteBtn
		deleteBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			deleteBtn.setEffect(shadowEffect);
			deleteBtn.setStyle("-fx-background-color: orange");
		});
		deleteBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			deleteBtn.setStyle("-fx-background-color: lightgreen");
			if(menuIndex == 4)
			{
				deleteBtn.setEffect(shadowEffect);
				deleteBtn.setStyle("-fx-background-color: orange");
			}
			else
				deleteBtn.setEffect(null);
		});
		deleteBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(monthPane.isVisible())
				monthPane.setVisible(false);
			if(!searchPane.isVisible())
				searchPane.setVisible(true);
			if(!carPane.isVisible())
				carPane.setVisible(true);
			if(soldPane.isVisible())
				soldPane.setVisible(false);
			if(monthTablePane.isVisible())
				monthTablePane.setVisible(false);
			
			deleteBtn.setStyle("-fx-background-color: orange");
			addBtn.setText("ဖ်က္ပါ");
			addBtn.setStyle("-fx-background-color: firebrick;");
			
			updatePane.setVisible(false);
			
			addCodeField.clear();
			addNameField.clear();
			addQuantityField.clear();
			addPriceField.clear();
			
			addCodeField.setVisible(true);
			addNameField.setVisible(false);
			addQuantityField.setVisible(false);
			addPriceField.setVisible(false);
			
			addNewPane.setVisible(true);
			addNewPane.setStyle("-fx-background-color: lightblue; -fx-border-color: red");
			
			if(carTable.getItems() == searchCarDataList)
				carTable.setItems(carDataList);
			
			switch(menuIndex)
			{
			case 0: toBuyBtn.setStyle("-fx-background-color: lightgreen;"); toBuyBtn.setEffect(null); break;
			case 1: afterBuyBtn.setStyle("-fx-background-color: lightgreen;"); afterBuyBtn.setEffect(null); break;
			case 2: newBtn.setStyle("-fx-background-color: lightgreen;"); newBtn.setEffect(null);break;
			case 3: updateBtn.setStyle("-fx-background-color: lightgreen;"); updateBtn.setEffect(null); break;
			case 5: monthlyBtn.setStyle("-fx-background-color: lightgreen;"); monthlyBtn.setEffect(null); break;
			}
			menuIndex = 4;
		});
		
		// monthlyBtn
		monthlyBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			monthlyBtn.setEffect(shadowEffect);
			monthlyBtn.setStyle("-fx-background-color: orange");
		});
		monthlyBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			monthlyBtn.setStyle("-fx-background-color: lightgreen");
			if(menuIndex == 5)
			{
				monthlyBtn.setEffect(shadowEffect);
				monthlyBtn.setStyle("-fx-background-color: orange");
			}
			else
				monthlyBtn.setEffect(null);
		});
		monthlyBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			monthPane.setVisible(true);
			monthTablePane.setVisible(false);
			if(carPane.isVisible())
				carPane.setVisible(false);
			if(soldPane.isVisible())
				soldPane.setVisible(false);
			
			monthlyBtn.setStyle("-fx-background-color: orange");
			addNewPane.setVisible(false);
			switch(menuIndex)
			{
			case 0: toBuyBtn.setStyle("-fx-background-color: lightgreen;"); toBuyBtn.setEffect(null); break;
			case 1: afterBuyBtn.setStyle("-fx-background-color: lightgreen;"); afterBuyBtn.setEffect(null); break;
			case 2: newBtn.setStyle("-fx-background-color: lightgreen;"); newBtn.setEffect(null);break;
			case 3: updateBtn.setStyle("-fx-background-color: lightgreen;"); updateBtn.setEffect(null); break;
			case 4: deleteBtn.setStyle("-fx-background-color: lightgreen;"); deleteBtn.setEffect(null); break;
			}
			menuIndex = 5;
		});
			
		// cbSearch event
		cbSearch.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue)
			{
				searchField.setPromptText(newValue + "ျဖင့္  ရွာ​ေဖြမည္ . . .");
			}
		});
		
		// cb Lack
		cbLack.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue)
			{
				btnLack.setText(newValue + " ခု ေအာက္ နည္းေသာ ပစၥည္းမ်ား");
			}
		});
		
		// cb Month
		cbMonth.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue)
			{
				btnMonth.setText(newValue + " လပိုင္း စာရင္းခ်ဳပ္");
			}
		});
		
		// add button event
		addBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			addBtn.setEffect(shadowEffect);
		});
		addBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			addBtn.setEffect(null);
		});
		addBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if(menuIndex == 2)
				addBtn.setStyle("-fx-background-color: green");
			else if(menuIndex == 3)
				addBtn.setStyle("-fx-background-color: blue");
			else if(menuIndex == 4)
				addBtn.setStyle("-fx-background-color: red");
		});
		addBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			if(menuIndex == 2)
				addBtn.setStyle("-fx-background-color: forestgreen");
			else if(menuIndex == 3)
				addBtn.setStyle("-fx-background-color: darkslateblue");
			else if(menuIndex == 4)
				addBtn.setStyle("-fx-background-color: firebrick");
		});
		
		// update combo box event
		cbUpdate.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue)
			{
				addCodeField.setVisible(true);
				addNameField.setVisible(true);
				addQuantityField.setVisible(true);
				addPriceField.setVisible(true);
				
				if(newValue.equals("ကုဒ္") || newValue.equals("အမ်ိဳးအမည္")) // code update
				{
					addQuantityField.setVisible(false);
					addPriceField.setVisible(false);
				}
				else if(newValue.equals("အ​ေရအတြက္"))
				{
					addNameField.setVisible(false);
					addPriceField.setVisible(false);
				}
				else
				{
					addNameField.setVisible(false);
					addQuantityField.setVisible(false);
				}
			}
		});
		
		btnMonth.setOnAction(e -> {
			monthTablePane.setVisible(true);
			String month = cbMonth.getValue();
			String year = cbYear.getValue();
			monthDataList.removeAll(monthDataList);
			int monthNo = 1, sum = 0;
			boolean found = false;
			for(SaleData sd: saleDataList)
			{
				if(month.equals(sd.getMonth()) && year.equals(sd.getYear()))
				{
					found = true;
					String total = sd.getTotal();
					try
					{
						int price = numberFormat.parse(total).intValue();
						sum += price;
					} catch (ParseException e1)
					{
						e1.printStackTrace();
					}
					monthDataList.add(new MonthData(monthNo++,sd.getCode(), sd.getName(), Integer.parseInt(sd.getQuantity()), total));
				}
			}
			if(found)
			{
				monthTable.setItems(monthDataList);
				showOperation("စုစုေပါင္း ေရာင္းရေငြ  - " + sum + " က်ပ္", success);
			}
			else
			{
				monthTable.setPlaceholder(new Label( cbMonth.getValue()+ " လပိုင္းအတြက္​ စာရင္းခ်ဳပ္​မရွိေသးပါ"));
				showOperation(cbYear.getValue() + ", " + cbMonth.getValue()+ " လပိုင္းအတြက္​ စာရင္းခ်ဳပ္​မရွိေသးပါ", fail);
			}
		});
	}
	
	// search Action
	public void searchAction()
	{
		String searchString = cbSearch.getValue();
		boolean found = false;
		
		if(searchString.equals("ကုဒ္"))
		{
			if(searchCarDataList.size() != 0)
				searchCarDataList.removeAll(searchCarDataList);
			for(CarData cd: carDataList)
				if(cd.getCode().equals(searchField.getText()))
				{
					searchCarDataList.add(new CarData(1,cd.getCode(), cd.getName(), cd.getMaxQuantity(), cd.getPrice()));
					carTable.setItems(searchCarDataList);
					found = true;
					break;
				}
			if(!found)
				showOperation(searchField.getText() + " ဆိုေသာကုဒ္​ ရွာမ​ေတြ႕ပါ", fail);
			else
				operationLbl.setVisible(false);
		}
		else
		{
			if(searchCarDataList.size() != 0)
				searchCarDataList.removeAll(searchCarDataList);
			for(CarData cd: carDataList)
				if(cd.getName().equals(searchField.getText()))
				{
					searchCarDataList.add(new CarData(1,cd.getCode(), cd.getName(), cd.getMaxQuantity(), cd.getPrice()));
					carTable.setItems(searchCarDataList);
					found = true;
				}
			if(!found)
				showOperation(searchField.getText() + "ဆိုေသာ အမ်ိဳးအမည္ ရွာမေတြ႕ပါ", fail);
			else
				operationLbl.setVisible(false);
		}
	}
	
	// sale button action
	public void saleBtnClick(CarData cd)
	{
		dialog.setTitle("ေရာင္းမည္");
		dialog.setContentText("ေရာင္းမည့္ ​အေရအတြက္");
		
		Optional<String> result = dialog.showAndWait();
		System.out.println(result + ", " + result.empty());
		if(result.isPresent() )
		{
			try
			{
				int qty = Integer.parseInt(result.get());
				int maxQty = cd.getMaxQuantity();
				if(qty <= maxQty)
				{
					int index = 0;
					@SuppressWarnings("deprecation")
					int day = date.getDate(), month = date.getMonth()+1, year = date.getYear()+1900;
					for(CarData data : carDataList)
						if( data.getCode() == cd.getCode() )
						{
							index = data.getNo() - 1;
							break;
						}
					cd.setMaxQuantity(maxQty - qty); 
					carDataList.set(index, cd);
					stmt.execute("update CarTable set QUANTITY = " + (maxQty - qty) + " where CODE like '" + cd.getCode() + "'");
					double price = numberFormat.parse(cd.getPrice()).doubleValue();
					double totalPrice = price * qty ;
					stmt.execute("insert into SaleTable values(" + saleNo +",'"+ cd.getCode() + "','" + cd.getName() + "',"+ qty + "," + price + "," + totalPrice + "," + day + "," + month + "," + year + ");");
					saleDataList.add(new SaleData(saleNo++, cd.getCode(), cd.getName(), qty+"", formatPrice(price+""),  formatPrice(totalPrice+""), day+"", month+"", year+""));
					showOperation(cd.getName() + "  " + qty + " ခု  " + "ေရာင္းလိုက္သည္", success);
				}
				else
					showOperation("ေရာင္းဖို႔ မလုံ​ေလာက္​ပါ", fail);
				
			}catch(NumberFormatException e)
			{
				showOperation("အေရအတြက္​  မွားရိုက္သည္", fail);
			}catch(Exception e)
			{
				showOperation("မေအာင္ျမင္ပါ ( Database error )", fail);
				e.printStackTrace();
			}
		}
	}
	
	// unsale btn click
	public void unSaleBtnClick(SaleData sd)
	{
		dialog.setTitle("ျပန္သိမ္းမည္");
		dialog.setContentText("ျပန္သိမ္းမည့္ ​အေရအတြက္ကို ရိုက္ထည္​့ပါ");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
		{
			try
			{
				int qty = Integer.parseInt(result.get());
				int maxQty = Integer.parseInt(sd.getQuantity());
				if(qty <= maxQty)
				{
					boolean flag = false;
					int index = 0;
					CarData cd = null;
					for(int i = 0; i < carDataList.size(); i++)
					{
						CarData cdd = carDataList.get(i);
						if(cdd.getCode().equals(sd.getCode()))
						{
							flag = true;
							cd = cdd;
							cd.setMaxQuantity(cd.getMaxQuantity()+qty);
							index = i;
							break;
						}
					}
					if(flag)
					{
						stmt.execute("update CarTable set QUANTITY = QUANTITY+" + qty + " where CODE like '" + sd.getCode() + "'");
						carDataList.set(index, cd);
					}
					else
					{
						double price = numberFormat.parse(sd.getPrice()).doubleValue();
						stmt.execute("insert into CarTable values('"+ sd.getCode() + "','" + sd.getName() + "'," + qty + "," + price + ")");
						carDataList.add(new CarData(no++, sd.getCode(), sd.getName(), qty, sd.getPrice()));
					}
					
					if(qty == maxQty)
					{
						saleDataList.remove(sd.getNo()-1);
						stmt.execute("delete from SaleTable where NO = " + sd.getNo());
						stmt.execute("update SaleTable set NO = NO-1 where NO > " + sd.getNo());
						--saleNo;
						for(int j  = 0; j < saleDataList.size(); j++)
						{
							SaleData sdd = saleDataList.get(j);
							if(j >= sd.getNo()-1)
							{
								sdd.setNo(sdd.getNo()-1);
								saleDataList.set(j, sdd);
							}
						}
					}
					else
					{
						sd.setQuantity((maxQty - qty) + "");
						saleDataList.set(sd.getNo()-1, sd);
						stmt.execute("update SaleTable set QUANTITY = " + (maxQty - qty) + " where NO = " + sd.getNo() );
					}
					showOperation(sd.getName() + "  " + qty + "  ခု        " + "ျပန္သိမ္းလိုက္ပါသည္", success);
				}
				else
					showOperation("ေရာင္းဖို႔ မလုံ​ေလာက္​ပါ", fail);
				
			}catch(NumberFormatException e)
			{
				showOperation("အေရအတြက္​  မွားရိုက္သည္", fail);
			}catch(Exception e)
			{
				showOperation("မေအာင္ျမင္ပါ ( Database error )", fail);
				e.printStackTrace();
			}
		}
	}
	
	public void showOperation(String status, String color)
	{
		operationLbl.setVisible(true);
		operationLbl.setText(status);
		operationLbl.setTextFill(Color.WHITESMOKE);
		operationLbl.setStyle("-fx-background-color: " + color);
	}
	
	// data base connection
	public Connection getConnection()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:CarSaleShopDB.sqlite");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isValid()
	{
		if(addCodeField.getText().equals("") || addNameField.getText().equals("") || addQuantityField.getText().equals("") || addPriceField.getText().equals(""))
		{
			showOperation("Box အားလုံး​ကို ျဖည္​့ပါ", fail);
			return false;
		}
		else
		{
			try
			{
				Integer.parseInt(addQuantityField.getText());
			}catch(NumberFormatException e)
			{
				showOperation("အ​ေရအတြက္မွာ  "+"ကိန္​းဂဏန္​း​ေတြသာ  ျဖည္​့လိုရပါမယ္​", fail);
				e.printStackTrace();
				return false;
			}
			try
			{
				Double.parseDouble(addPriceField.getText());
			}catch(NumberFormatException e)
			{
				showOperation("တန္​ဖိုးမွာ" + "ကိန္​းဂဏန္​း​ေတြသာ  ျဖည္​့လိုရပါမယ္​", fail);
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean isExistingCode()
	{
		for(CarData cd : carDataList)
			if(cd.getCode().equals(addCodeField.getText()))
			{
				showOperation("ရွိၿပီးသား ကုဒ္​ျဖစ္​လို႔ မရပါ", fail);
				return true;
			}
		return false;
	}
	
	public boolean isExistingName()
	{
		for(CarData cd : carDataList)
			if(cd.getName().equals(addNameField.getText()))
				return true;
		
		return false;
	}
	
	public String formatPrice(String price)
	{
		return String.format("%,.0f", Double.parseDouble(price) );
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
