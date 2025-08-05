package org.example.Components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.example.Controllers.DashboardController;
import org.example.Dialogs.CreateOrEditTransactionDialog;
import org.example.Models.Transaction;
import org.example.utils.SqlUtil;

public class TransactionComponent extends HBox {
    private Label transactionCatLabel, transactionNameLabel, transactionDateLabel, transactionAmountLabel;
    private Button editButton, deleteButton;

    private DashboardController dashboardController;
    private Transaction transaction;

    public TransactionComponent(DashboardController dashboardController, Transaction transaction)
    {
        this.dashboardController = dashboardController;
        this.transaction = transaction;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("main-background", "rounded-border", "padding-10px");


        VBox categoryNameDateSection = createCNDSection();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        transactionAmountLabel = new Label("£" + transaction.getTransactionAmount());
        transactionAmountLabel.getStyleClass().addAll("text-size-md");
        if(transaction.getTransactionType().equalsIgnoreCase("expense")){
            transactionAmountLabel.getStyleClass().add("text-light-red");
        }else{
            transactionAmountLabel.getStyleClass().add("text-light-green");
        }

        HBox actionButtonsSection = createActionButtons();
        getChildren().addAll(categoryNameDateSection, region, transactionAmountLabel, actionButtonsSection);

    }

    private HBox createActionButtons() {
        HBox actionButtonSection = new HBox(20);
        actionButtonSection.setAlignment(Pos.CENTER);

        editButton = new Button("Edit");
        editButton.getStyleClass().addAll("text-size-md", "rounded border");
        editButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new CreateOrEditTransactionDialog(dashboardController, TransactionComponent.this, true)
                        .showAndWait();
            }
        });
        deleteButton = new Button("Del");
        deleteButton.getStyleClass().addAll("text-size-md", "rounded border","bg-light-red", "text-white");
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!SqlUtil.deleteTransactionById(transaction.getId())){
                    return;
                }
                setVisible(false);
                setManaged(false);
                if(getParent() instanceof VBox){
                    ((VBox) getParent()).getChildren().remove(TransactionComponent.this);
                }
                dashboardController.fetchUserData();
            }
        });


        actionButtonSection.getChildren().addAll(editButton,deleteButton);

        return actionButtonSection;
    }

    private VBox createCNDSection() {
        VBox cNDSection = new VBox();

        if(transaction.getTransactionCategory() == null){
            transactionCatLabel = new Label("Undefined");
            transactionCatLabel.getStyleClass().addAll("text-light-gray");
        }else{
            transactionCatLabel = new Label(transaction.getTransactionCategory().getCategoryName());
            transactionCatLabel.setTextFill(Paint.valueOf("#" + transaction.getTransactionCategory().getCategoryColour()));
        }
        transactionNameLabel = new Label(transaction.getTransactionName());
        transactionNameLabel.getStyleClass().addAll("text-light-gray", "text-size-md");
        transactionDateLabel = new Label(transaction.getTransactionDate().toString());
        transactionDateLabel.getStyleClass().addAll("text-light-gray");

        cNDSection.getChildren().addAll(transactionCatLabel, transactionNameLabel, transactionDateLabel);
        return cNDSection;
    }

    public Transaction getTransaction(){
        return transaction;
    }
}
