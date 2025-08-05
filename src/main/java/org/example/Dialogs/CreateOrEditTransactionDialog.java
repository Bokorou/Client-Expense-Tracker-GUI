package org.example.Dialogs;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.Components.TransactionComponent;
import org.example.Controllers.DashboardController;
import org.example.Models.Transaction;
import org.example.Models.TransactionCategory;
import org.example.Models.User;
import org.example.utils.SqlUtil;
import org.example.utils.Utility;

import java.time.LocalDate;
import java.util.List;

public class CreateOrEditTransactionDialog extends CustomDialog{

    List<TransactionCategory> transactionCategories;
    private TransactionComponent transactionComponent;
    private DashboardController dashboardController;
    private TextField transactionNameField, transactionAmountField;
    private DatePicker transactionDate;
    private ComboBox<String> categoryBox;
    private ToggleGroup togggleGroup;
    private Button createButton, cancelButton;

    private boolean isEditing;

    //Used for editing
    public CreateOrEditTransactionDialog(DashboardController dashboardController, TransactionComponent transactionComponent,
                                         boolean isEditing){
        super(dashboardController.getUser());
        this.isEditing = isEditing;
        this.transactionComponent = transactionComponent;
        this.dashboardController = dashboardController;

        setTitle(isEditing ? "Edit Transaction" : "Create Transaction");
        setWidth(900);
        setHeight(595);
        transactionCategories = SqlUtil.getAllTransactionCategoriesByUser(user);

        VBox mainContentBox = createMainContentBox();
        getDialogPane().setContent(mainContentBox);
    }
    //Used for creating transactions
    public CreateOrEditTransactionDialog(DashboardController dashboardController, boolean isEditing){
        this(dashboardController, null, isEditing);
    }

    private VBox createMainContentBox() {
        VBox mainContentBox = new VBox(30);
        mainContentBox.setAlignment(Pos.CENTER);
        transactionNameField = new TextField();
        transactionNameField.setPromptText("Enter transaction name");
        transactionNameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md", "rounded-border");
        transactionAmountField = new TextField();
        transactionAmountField.setPromptText("Enter transaction amount");
        transactionAmountField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md", "rounded-border");
        transactionDate = new DatePicker();
        transactionDate.setPromptText("Enter transaction date");
        transactionDate.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md", "rounded-border");
        transactionDate.setMaxWidth(Double.MAX_VALUE);
        categoryBox = new ComboBox<>();
        categoryBox.setPromptText("Choose category");
        categoryBox.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md", "rounded-border");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        for(TransactionCategory transactionCategory : transactionCategories) {
            categoryBox.getItems().add(transactionCategory.getCategoryName());
        }

        if(isEditing){
            Transaction transaction = transactionComponent.getTransaction();
            transactionNameField.setText(transaction.getTransactionName());
            transactionAmountField.setText(String.valueOf(transaction.getTransactionAmount()));
            transactionDate.setValue(transaction.getTransactionDate());
            categoryBox.setValue(
                    transaction.getTransactionCategory()
                            ==  null ? "" : transaction.getTransactionCategory().getCategoryName()
            );
        }
        mainContentBox.getChildren().addAll(transactionNameField, transactionAmountField, transactionDate, categoryBox,transactionTypeRadioButton(), createAndConfirmButtonBox());
        return mainContentBox;
    }

    private HBox transactionTypeRadioButton(){
         HBox radioButtonBox = new HBox(50);
         radioButtonBox.setAlignment(Pos.CENTER);

         togggleGroup = new ToggleGroup();

        RadioButton incomeRadioButton = new RadioButton("Income");
        incomeRadioButton.getStyleClass().addAll("text-size-md", "text-light-gray");
        incomeRadioButton.setToggleGroup(togggleGroup);

        RadioButton expenseRadioButton = new RadioButton("Expense");
        expenseRadioButton.getStyleClass().addAll("text-size-md", "text-light-gray");
        expenseRadioButton.setToggleGroup(togggleGroup);

        if(isEditing){
            Transaction transaction = transactionComponent.getTransaction();
            if(transaction.getTransactionType().equalsIgnoreCase("income")){
                incomeRadioButton.setSelected(true);
            }else{
                expenseRadioButton.setSelected(true);
            }
        }
        radioButtonBox.getChildren().addAll(incomeRadioButton, expenseRadioButton);

        return radioButtonBox;
    }

    private HBox createAndConfirmButtonBox(){
        HBox buttonBox = new HBox(50);
        buttonBox.setAlignment(Pos.CENTER);


        createButton = new Button("Save");
        createButton.setPrefWidth(150);
        createButton.getStyleClass().addAll("bg-light-blue", "text-size-md", "text-white", "rounded-border");
        saveTransaction();

        cancelButton = new Button("Cancel");
        createButton.setPrefWidth(150);
        cancelButton.getStyleClass().addAll( "text-size-md","rounded-border");
        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CreateOrEditTransactionDialog.this.close();
                dashboardController.fetchUserData();
            }
        });

        buttonBox.getChildren().addAll(cancelButton, createButton);
        return buttonBox;
    }

    private void saveTransaction() {

        createButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String transactionNameText = transactionNameField.getText();
                double transactionAmountText = Double.parseDouble(transactionAmountField.getText());
                LocalDate transactionDateText = transactionDate.getValue();
                String transactionType = ((RadioButton)togggleGroup.getSelectedToggle()).getText();
                String categoryBoxText = categoryBox.getValue();

                TransactionCategory transactionCategory = findTransactionCategoryByName(transactionCategories, categoryBoxText);

                JsonObject userData = new JsonObject();

                userData.addProperty("id", user.getId());
                JsonObject categoryData = new JsonObject();
                categoryData.addProperty("id", transactionCategory.getId());

                JsonObject transactionData = new JsonObject();
                if (isEditing){
                    transactionData.addProperty("id", transactionComponent.getTransaction().getId());
                }
                transactionData.add("transactionCategory", categoryData);
                transactionData.add("user",userData);
                transactionData.addProperty("transactionName", transactionNameText);
                transactionData.addProperty("transactionAmount", transactionAmountText);
                transactionData.addProperty("transactionDate", String.valueOf(transactionDateText));
                transactionData.addProperty("transactionType", transactionType);

                if(!isEditing ?
                     SqlUtil.postTransactionData(transactionData) : SqlUtil.putTransAction(transactionData)) {


                    Utility.showAlert(Alert.AlertType.CONFIRMATION,
                            isEditing ? "Successfully saved transaction" : "Successfully created transaction!");
                    CreateOrEditTransactionDialog.this.close();
                    dashboardController.fetchUserData();
                }

            }

        });
    }

    private TransactionCategory findTransactionCategoryByName(List<TransactionCategory> transactionCategories,
                                                              String categoryBoxText){

        for(TransactionCategory transactionCategory : transactionCategories){
            if(transactionCategory.getCategoryName().equals(categoryBoxText)){
                return transactionCategory;
            }
        }
        return null;
    }
}
