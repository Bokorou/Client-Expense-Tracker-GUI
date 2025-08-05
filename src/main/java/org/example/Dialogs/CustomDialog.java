package org.example.Dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.example.Models.User;

public class CustomDialog extends Dialog {

    protected User user; //Children class can still access this method

    public CustomDialog(User user){
        this.user = user;
        getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getDialogPane().getStyleClass().addAll("main-background");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

    }
}
