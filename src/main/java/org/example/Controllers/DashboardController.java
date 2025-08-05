package org.example.Controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.example.Components.TransactionComponent;
import org.example.Dialogs.CreateNewCategoryDialog;
import org.example.Dialogs.CreateOrEditTransactionDialog;
import org.example.Dialogs.ViewTransactionCatDialog;
import org.example.Dialogs.ViewTransactionsDialog;
import org.example.Models.MonthlyFinance;
import org.example.Models.Transaction;
import org.example.Models.User;
import org.example.utils.SqlUtil;
import org.example.views.DashboardView;
import org.example.views.LoginView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class DashboardController {
    private final int recentTransactionSize= 10;

    private DashboardView dashboardView;
    private User user;
    private int currentPage;
    private int currentYear;
    private List<Transaction> recentTransactions, currentTransactionsByYear;

    public DashboardController(DashboardView dashboardView){
        this.dashboardView = dashboardView;
        currentYear = dashboardView.getYearComboBox().getValue();
        fetchUserData();
        initialize();
    }

    public void fetchUserData() {
        //load the loading animation
        dashboardView.getLoadingAnimations().setVisible(true);

        //remove all children from the dashboard view
        dashboardView.getRecentTransactionBox().getChildren().clear();

        user = SqlUtil.getUserByEmail(dashboardView.getEmail());

        currentTransactionsByYear = SqlUtil.getAllTransactionsByUserIdAndYear(user.getId(), currentYear, null);
        calculateDistinctYears();
        calculateBalanceAndIncomeAndExpense();

        dashboardView.getTransactionTable().setItems(calculateMonthlyFinance());
        createRecentTransactionsComponent();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    dashboardView.getLoadingAnimations().setVisible(false);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void calculateBalanceAndIncomeAndExpense() {
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        if(currentTransactionsByYear != null){
            for(Transaction transaction : currentTransactionsByYear){
                BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getTransactionAmount());
                if (transaction.getTransactionType().equalsIgnoreCase("income")){
                    totalIncome = totalIncome.add(transactionAmount);
                }else{
                    totalExpense = totalExpense.add(transactionAmount);
                }
            }
        }
        totalIncome.setScale(2, RoundingMode.HALF_UP);
        totalExpense.setScale(2, RoundingMode.HALF_UP);
        BigDecimal currentBalance = totalIncome.subtract(totalExpense);
        currentBalance = currentBalance.setScale(2, RoundingMode.HALF_UP);

        dashboardView.getTotalExpense().setText("£"+ totalExpense);
        dashboardView.getTotalIncome().setText("£"+ totalIncome);
        dashboardView.getCurrentBalance().setText("£"+ currentBalance);
    }

    private void calculateDistinctYears() {
        List<Integer> distinctYears = SqlUtil.getDistinctYears(user.getId());
        for(Integer integer : distinctYears){
            if(!dashboardView.getYearComboBox().getItems().contains(integer)){
                dashboardView.getYearComboBox().getItems().add(integer);
            }
        }
    }

    private void createRecentTransactionsComponent(){
        recentTransactions = SqlUtil.getAllTransactionsById(
                user.getId(), 0,
                currentPage,
                recentTransactionSize);
        if(recentTransactions == null) return;

        for (Transaction transaction : recentTransactions){
            dashboardView.getRecentTransactionBox().getChildren().add(
                    new TransactionComponent(this, transaction));

        }
    }

    private ObservableList<MonthlyFinance> calculateMonthlyFinance(){
        double[] incomeCounter = new double[12];
        double[] expenseCounter = new double[12];

        for(Transaction transaction : currentTransactionsByYear){
            LocalDate transactionDate = transaction.getTransactionDate();
            if(transaction.getTransactionType().equalsIgnoreCase("income")){
                incomeCounter[transactionDate.getMonth().getValue() - 1] += transaction.getTransactionAmount();
            }else{
                expenseCounter[transactionDate.getMonth().getValue() - 1] += transaction.getTransactionAmount();
            }
        }

        ObservableList<MonthlyFinance> monthlyFinances = FXCollections.observableArrayList();
        for(int i = 0; i < 12; i++){
            MonthlyFinance monthlyFinance = new MonthlyFinance(
                    Month.of(i + 1).name(),
                    new BigDecimal(String.valueOf(incomeCounter[i])),
                    new BigDecimal(String.valueOf(expenseCounter[i]))
            );

            monthlyFinances.add(monthlyFinance);
        }

        return monthlyFinances;
    }

    private void initialize(){
        addMenuActions();
        addTransactionAction();
        addComboBoxAction();
        addTableActions();
    }

    private void addComboBoxAction() {
        dashboardView.getYearComboBox().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                currentYear = dashboardView.getYearComboBox().getValue();

                fetchUserData();
            }
        });
    }

    private void addTableActions() {
        dashboardView.getTransactionTable().setRowFactory(new Callback<TableView<MonthlyFinance>, TableRow<MonthlyFinance>>() {
            @Override
            public TableRow<MonthlyFinance> call(TableView<MonthlyFinance> monthlyFinanceTableView) {
                TableRow<MonthlyFinance> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(!row.isEmpty() && mouseEvent.getClickCount() == 2){
                            MonthlyFinance monthlyFinance = row.getItem();
                            new ViewTransactionsDialog(DashboardController.this,
                                    monthlyFinance.getMonth()).showAndWait();
                        }
                    }
                });
                return row;
            }
        });
    }

    private void addMenuActions() {
        dashboardView.getCreateCategoryMi().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    new CreateNewCategoryDialog(user).showAndWait();
                }
            });
            dashboardView.getViewCategoriesMI().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    new ViewTransactionCatDialog(user, DashboardController.this).showAndWait();

                }
            });

            dashboardView.getLogoutMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    new LoginView().show();
                }
            });
    }

    private void addTransactionAction(){
        dashboardView.getAddTransactionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new CreateOrEditTransactionDialog(DashboardController.this, false ).showAndWait();
            }
        });
    }

    public User getUser(){
        return user;
    }

    public int getCurrentYear(){
        return currentYear;
    }
}