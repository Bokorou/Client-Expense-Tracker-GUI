package org.example.Dialogs;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.Components.TransactionComponent;
import org.example.Controllers.DashboardController;
import org.example.Models.Transaction;
import org.example.utils.SqlUtil;

import java.time.Month;
import java.util.List;

public class ViewTransactionsDialog extends CustomDialog{

    private DashboardController dashboardController;
    String monthName;

    public ViewTransactionsDialog(DashboardController dashboardController, String monthName){
        super(dashboardController.getUser());
        this.dashboardController = dashboardController;
        this.monthName = monthName;

        setTitle("View Transactions");
        setWidth(815);
        setHeight(500);
        setResizable(true);

        ScrollPane transactionScrollPane = createTransactionScrollPane();
        getDialogPane().setContent(transactionScrollPane);
    }

    private ScrollPane createTransactionScrollPane() {
        VBox vbox = new VBox(20);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setMinHeight(getHeight() - 40);
        scrollPane.setFitToWidth(true);
        List<Transaction> monthTransactions = SqlUtil.getAllTransactionsByUserIdAndYear(
                dashboardController.getUser().getId(),
                dashboardController.getCurrentYear(),
                Month.valueOf(monthName).getValue());
        if ((monthTransactions != null)){
            for(Transaction transaction : monthTransactions){
                TransactionComponent transactionComponent = new TransactionComponent(
                        dashboardController, transaction
                );

                transactionComponent.getStyleClass().addAll("border-light-gray");

                vbox.getChildren().add(transactionComponent);
            }
        }

        return scrollPane;
    }
}
