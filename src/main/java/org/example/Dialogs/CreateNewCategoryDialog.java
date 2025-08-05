package org.example.Dialogs;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import jdk.jshell.execution.Util;
import org.example.Models.User;
import org.example.utils.SqlUtil;
import org.example.utils.Utility;

public class CreateNewCategoryDialog extends CustomDialog{

    private TextField newCategoryTextField;
    private ColorPicker colorPicker;
    private Button createCategoryBtn;

    public CreateNewCategoryDialog(User user){
        super(user);
        setTitle("Create new Category");
        getDialogPane().setContent(createDialogContentBox());
    }

    private VBox createDialogContentBox(){
        VBox dialogContentBox = new VBox(20);

        newCategoryTextField = new TextField();
        newCategoryTextField.setPromptText("Enter category name");
        newCategoryTextField.getStyleClass().addAll("text-size-md", "field-background", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.getStyleClass().add("text-size-md");
        colorPicker.setMaxWidth(Double.MAX_VALUE);

        createCategoryBtn = new Button("Create");
        createCategoryBtn.getStyleClass().addAll("bg-light-blue", "text-size-md", "text-white");
        createCategoryBtn.setMaxWidth(Double.MAX_VALUE);
        createCategoryBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Extract data

                String categoryName = newCategoryTextField.getText();
                String colour = Utility.getHexColourValue(colorPicker);
                System.out.println(colour);

                JsonObject userData = new JsonObject();
                userData.addProperty("id",user.getId());

                JsonObject transactionCategoryData = new JsonObject();
                transactionCategoryData.add("user", userData);
                transactionCategoryData.addProperty("categoryName", categoryName);
                transactionCategoryData.addProperty("categoryColour", colour);

                boolean postTransactionCategoryStatus = SqlUtil.postTransActionCategory(transactionCategoryData);

                if(postTransactionCategoryStatus){
                    Utility.showAlert(Alert.AlertType.INFORMATION, "Success: transaction category created!");
                }else{
                    Utility.showAlert(Alert.AlertType.ERROR, "Error: Something went wrong!");
                }
            }
        });


        dialogContentBox.getChildren().addAll(newCategoryTextField, colorPicker, createCategoryBtn);

        return dialogContentBox;
    }
}
