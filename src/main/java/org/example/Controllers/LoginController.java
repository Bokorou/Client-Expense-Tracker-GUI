package org.example.Controllers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.utils.ApiUtil;
import org.example.utils.SqlUtil;
import org.example.utils.Utility;
import org.example.views.DashboardView;
import org.example.views.LoginView;
import org.example.views.SignUpView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView){
        this.loginView = loginView;
        initialize();
    }

    private void initialize(){
        loginView.getLoginButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
               if (!validateUser()){
                   return;
               }

               String email = loginView.getUsernameField().getText();
               String password =loginView.getPasswordField().getText();

                if(SqlUtil.postLoginUSer(email, password)){
                    /*perhaps a toast pup up instead?
                      Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                      Utility.showToastAlert(stage, "Login successful", 5);
                     */
                    Utility.showAlert(Alert.AlertType.CONFIRMATION,"Login Successful!");
                    new DashboardView(email).show();

            }else{
                Utility.showAlert(Alert.AlertType.ERROR, "Failed to authenticate!");
                }
            }
        });

        loginView.getSignupLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new SignUpView().show();
            }
        });
    }

    private boolean validateUser(){
        if(loginView.getUsernameField().getText().isEmpty()||
           loginView.getPasswordField().getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Login");
            alert.setContentText("Please fill in both fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
