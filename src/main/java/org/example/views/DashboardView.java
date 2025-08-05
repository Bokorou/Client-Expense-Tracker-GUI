package org.example.views;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.Animations.LoadingAnimations;
import org.example.Controllers.DashboardController;
import org.example.Controllers.LoginController;
import org.example.Models.MonthlyFinance;
import org.example.utils.Utility;
import org.example.utils.ViewNavigator;

import java.math.BigDecimal;
import java.time.Year;

public class DashboardView {
    private String email;
    private LoadingAnimations loadingAnimations;
    private Label currentBalanceLabel, currentBalance;
    private Label totalIncomeLabel, totalIncome;
    private Label totalExpenseLabel, totalExpense;
    private ComboBox<Integer> yearComboBox;
    private MenuItem  createCategoryMi, viewCategoriesMI, logoutMI;
    private VBox recentTransactionBox;
    private ScrollPane recentTransactionScrollpane;
    private TableView<MonthlyFinance> transactionTable;
    private TableColumn<MonthlyFinance, String> monthColumn;
    private TableColumn<MonthlyFinance, BigDecimal> expenseColumn;
    private TableColumn<MonthlyFinance, BigDecimal> incomeColumn;


    private Button addTransactionButton, viewChartButton;
    public DashboardView(String email){
        this.email = email;
        loadingAnimations = new LoadingAnimations(Utility.APP_WIDTH, Utility.APP_HEIGHT);

        currentBalanceLabel = new Label("Current Balance:");
        totalIncomeLabel = new Label("Total Income:");
        totalExpenseLabel = new Label("Total Expense:");

        addTransactionButton = new Button("+");

        currentBalance = new Label("£0.00");
        totalIncome = new Label("£0.00");
        totalExpense = new Label("£0.00");

    }

    public void show(){
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        new DashboardController(this);
        ViewNavigator.switchViews(scene);

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                loadingAnimations.resizeWidth(t1.doubleValue());
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                loadingAnimations.resizeHeight(t1.doubleValue());
                resizeTableWidthColumns();
            }
        });

    }

    private Scene createScene()  {
        MenuBar menuBar = createMenuBar();
        VBox mainContainer = new VBox(10);
        mainContainer.getStyleClass().addAll("main-background");
        mainContainer.setAlignment(Pos.TOP_CENTER);

        VBox mainContainerWrapper = new VBox();
        mainContainerWrapper.getStyleClass().addAll("dashboard-padding");
        VBox.setVgrow(mainContainerWrapper,Priority.ALWAYS);

        HBox balanceSummaryBox = createBalanceSummaryBox();
        GridPane contentGridPane = createContentGridPane();
        VBox.setVgrow(contentGridPane, Priority.ALWAYS);

        mainContainerWrapper.getChildren().addAll(balanceSummaryBox, contentGridPane);
        mainContainer.getChildren().addAll(menuBar, mainContainerWrapper,loadingAnimations);
        return new Scene(mainContainer, Utility.APP_WIDTH, Utility.APP_HEIGHT);
    }

    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        createCategoryMi = new MenuItem("Create Category");
        viewCategoriesMI = new MenuItem("View Categories");
        logoutMI = new MenuItem("Logout");
        fileMenu.getItems().addAll(createCategoryMi, viewCategoriesMI, logoutMI);
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;

    }

    private HBox createBalanceSummaryBox() {
        HBox balanceSummaryBox = new HBox();
        VBox currentBalanceBox = new VBox();
        currentBalanceLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        currentBalance.getStyleClass().addAll("text-size-lg", "text-white");
        currentBalanceBox.getChildren().addAll(currentBalanceLabel, currentBalance);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        VBox totalIncomeBox = new VBox();
        totalIncomeLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalIncome.getStyleClass().addAll("text-size-lg", "text-white");
        totalIncomeBox.getChildren().addAll(totalIncomeLabel, totalIncome);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        VBox totalExpenseBox = new VBox();
        totalExpenseLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalExpense.getStyleClass().addAll("text-size-lg", "text-white");
        totalExpenseBox.getChildren().addAll(totalExpenseLabel, totalExpense);

        balanceSummaryBox.getChildren().addAll(currentBalanceBox, region1, totalIncomeBox, region2, totalExpenseBox);
        return balanceSummaryBox;
    }

    private GridPane createContentGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        //set constraints to the cells in the gridpane
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints);

        //transaction table summary
        VBox summaryVbox = new VBox(20);
        HBox yearComboBoxAndChartButtonBox = yearComboBoxAndChartButtonBox();
        VBox tableContentBox = tableContentBox();
        VBox.setVgrow(tableContentBox, Priority.ALWAYS);
        summaryVbox.getChildren().addAll(yearComboBoxAndChartButtonBox, tableContentBox);


        //recent transactions
        VBox recentTransactionsVbox = recentTransactionsVBox();
        recentTransactionsVbox.getStyleClass().addAll("field-background",
                "rounded-border", "padding-10px");

        gridPane.add(summaryVbox, 0, 0);
        GridPane.setVgrow(recentTransactionsVbox, Priority.ALWAYS);
        gridPane.add(recentTransactionsVbox, 1,0);
        return gridPane;
    }

    private VBox tableContentBox() {
        VBox vbox = new VBox();
        transactionTable = new TableView<>();
        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        incomeColumn = new TableColumn<>("Income");
        incomeColumn.setCellValueFactory(new PropertyValueFactory<>("income"));
        incomeColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        expenseColumn = new TableColumn<>("Expense");
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("expense"));
        expenseColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        transactionTable.getColumns().addAll(monthColumn,incomeColumn,expenseColumn);
        vbox.getChildren().addAll(transactionTable);
        resizeTableWidthColumns();
        return vbox;
    }

    private HBox yearComboBoxAndChartButtonBox() {
        HBox hBox = new HBox(10);

        yearComboBox = new ComboBox<>();
        yearComboBox.getStyleClass().addAll("text-size-md");
        yearComboBox.setValue(Year.now().getValue());
        viewChartButton = new Button("View Chart");
        viewChartButton.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md");

        hBox.getChildren().addAll(yearComboBox, viewChartButton);
        return hBox;
    }

    private VBox recentTransactionsVBox() {
        VBox recentTransactionsVBox = new VBox();

        //label and add button
        HBox transactionLabelHBox = new HBox();
        Label recentTransactionLabel = new Label("Recent Transactions");
        Region labelButtonRegion = new Region();
        recentTransactionLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        labelButtonRegion.getStyleClass().addAll("field-background", "text-size-md", "text-light-gray", "rounded-border");

        ScrollPane recentTransactionScroll = new ScrollPane();
        recentTransactionScroll.setFitToWidth(true);
        recentTransactionScroll.setFitToHeight(true);

        HBox.setHgrow(labelButtonRegion, Priority.ALWAYS);
        transactionLabelHBox.getChildren().addAll(recentTransactionLabel, labelButtonRegion, addTransactionButton);
        //recent transactions box
        recentTransactionBox = new VBox(10);
        recentTransactionScrollpane = new ScrollPane(recentTransactionBox);
        recentTransactionScrollpane.setFitToWidth(true);
        recentTransactionScrollpane.setFitToHeight(true);
        recentTransactionsVBox.getChildren().addAll(transactionLabelHBox, recentTransactionScrollpane);
        return recentTransactionsVBox;
    }

    private void resizeTableWidthColumns(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double colsWidth = transactionTable.getWidth()*(0.335);
                monthColumn.setPrefWidth(colsWidth);
                incomeColumn.setPrefWidth(colsWidth);
                expenseColumn.setPrefWidth(colsWidth);
            }
        });
    }

    public MenuItem getCreateCategoryMi() {
        return createCategoryMi;
    }

    public void setCreateCategoryMi(MenuItem createCategoryMi) {
        this.createCategoryMi = createCategoryMi;
    }

    public MenuItem getViewCategoriesMI() {
        return viewCategoriesMI;
    }


    public String getEmail() {
        return email;
    }

    public Button getAddTransactionButton() {
        return addTransactionButton;
    }

    public VBox getRecentTransactionBox() {
        return recentTransactionBox;
    }

    public LoadingAnimations getLoadingAnimations(){
        return loadingAnimations;
    }

    public MenuItem getLogoutMenuItem(){
        return logoutMI;
    }

    public TableView<MonthlyFinance> getTransactionTable() {
        return transactionTable;
    }

    public TableColumn<MonthlyFinance, String> getMonthColumn() {
        return monthColumn;
    }

    public TableColumn<MonthlyFinance, BigDecimal> getExpenseColumn() {
        return expenseColumn;
    }

    public TableColumn<MonthlyFinance, BigDecimal> getIncomeColumn() {
        return incomeColumn;
    }

    public ComboBox<Integer> getYearComboBox(){
        return yearComboBox;
    }

    public Label getCurrentBalance() {
        return currentBalance;
    }

    public Label getTotalIncome() {
        return totalIncome;
    }

    public Label getTotalExpense() {
        return totalExpense;
    }
}
