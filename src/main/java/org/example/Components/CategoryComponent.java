package org.example.Components;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.Controllers.DashboardController;
import org.example.Models.TransactionCategory;
import org.example.utils.SqlUtil;
import org.example.utils.Utility;

public class CategoryComponent extends HBox {

    private DashboardController dashboardController;
    private TransactionCategory transactionCategory;

    private TextField categoryTextfield;
    private ColorPicker colorPicker;
    private Button editButton, saveButton, deleteButton;

    private String updatedCategoryText;
    private String updatedColorPicker;
    private boolean isEditing;

    public CategoryComponent(DashboardController dashboardController, TransactionCategory transactionCategory){
        this.dashboardController = dashboardController;
        this.transactionCategory = transactionCategory;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("rounded-border", "field-background", "padding-10px");

        categoryTextfield = new TextField();
        categoryTextfield.setText(transactionCategory.getCategoryName());
        categoryTextfield.setMaxWidth(Double.MAX_VALUE);
        categoryTextfield.setEditable(false);
        categoryTextfield.getStyleClass().addAll("field-background", "text-size-md", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.setDisable(true);
        colorPicker.setValue(Color.valueOf(transactionCategory.getCategoryColour()));
        colorPicker.getStyleClass().addAll("text-size-sm");

        editButton = new Button("Edit");
        editButton.setMinWidth(50);
        editButton.getStyleClass().addAll("text-size-sm");
        editButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleToggle();
            }
        });

        saveButton = new Button("Save");
        saveButton.setMinWidth(50);
        saveButton.getStyleClass().addAll("text-size-sm");
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleToggle();
                updatedCategoryText = categoryTextfield.getText();
                System.out.println(updatedCategoryText);
                updatedColorPicker = Utility.getHexColourValue(colorPicker);
                System.out.println(updatedColorPicker);

                boolean putTransactionCategoryStatus = SqlUtil.putTransActionCategory(transactionCategory.getId(), updatedCategoryText,updatedColorPicker);

                if(putTransactionCategoryStatus){
                    Utility.showAlert(Alert.AlertType.INFORMATION, "Success: transaction category created!");
                }else{
                    Utility.showAlert(Alert.AlertType.ERROR, "Error: Something went wrong!");
                }

            }
        });

        deleteButton = new Button("Delete");
        deleteButton.setMinWidth(50);
        deleteButton.getStyleClass().addAll("text-size-sm", "bg-light-red", "text-white");
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean deleteTransactionStatus = SqlUtil.deleteTransactionCategory(transactionCategory.getId());

                if(getParent() instanceof VBox){
                    ((VBox)getParent()).getChildren().remove(CategoryComponent.this);
                }

                if(deleteTransactionStatus){
                    Utility.showAlert(Alert.AlertType.INFORMATION, "Success: transaction category created!");
                }else{
                    Utility.showAlert(Alert.AlertType.ERROR, "Error: Something went wrong!");
                }
            }
        });

        getChildren().addAll(categoryTextfield, colorPicker,editButton,saveButton,deleteButton);
    }

    private void handleToggle(){
        if(!isEditing){
            isEditing = true;

            categoryTextfield.setEditable(true);
            categoryTextfield.setStyle("-fx-background-color: #fff; -fx-text-fill: #000");
            colorPicker.setDisable(false);
            editButton.setVisible(false);
            editButton.setManaged(false);
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        }else if(isEditing){
            isEditing = false;

            categoryTextfield.setEditable(false);
            categoryTextfield.setStyle("-fx-background-color: #515050; -fx-text-fill: #BEB9B9");
            colorPicker.setDisable(true);
            saveButton.setVisible(false);
            saveButton.setManaged(false);
            editButton.setVisible(true);
            editButton.setManaged(true);

           // updateCategory();
        }
    }

//    private TransactionCategory updateCategory(TransactionCategory transactionCategory) {
//
//        //create transactioncategory here
//
//        TransactionCategory updatedTransactionCategory = new TransactionCategory()
//
//    }
}
