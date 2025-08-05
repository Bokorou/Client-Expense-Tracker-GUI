package org.example.Controllers;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.example.utils.SqlUtil;
import org.example.utils.Utility;
import org.example.views.SignUpView;
import org.example.views.LoginView;


public class SignupController {
    private SignUpView signUpView;

    public SignupController(SignUpView signUpView){
        this.signUpView = signUpView;
        initialize();

    }

    private void initialize() {
        signUpView.getLoginLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new LoginView().show();
            }
        });

        signUpView.getRegisterButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!validateInput()){
                    Utility.showAlert(Alert.AlertType.ERROR, "Invalid input");
                    return;
                }

                //Extract the data in the fields
                String name = signUpView.getNameField().getText();
                String email = signUpView.getEmailField().getText();
                String password = signUpView.getPasswordField().getText();

                //Created the Json Data to send to our post request
                JsonObject jsonData = new JsonObject();
                jsonData.addProperty("name", name);
                jsonData.addProperty("email", email);
                jsonData.addProperty("password", password);

                // Send in our post request
                boolean postCreatAccountStatus = SqlUtil.postCreateUser(jsonData);
                //Depending onn the result we will display corresponding message

                if(postCreatAccountStatus){
                    Utility.showAlert(Alert.AlertType.INFORMATION, "Succesfully created a new account!");
                    new LoginView().show();
                }else{
                    Utility.showAlert(Alert.AlertType.WARNING, "Failed to create account!");
                }

            }
        });
    }

    private boolean validateInput(){
        if(signUpView.getNameField().getText().isEmpty()) return false;
        if(signUpView.getEmailField().getText().isEmpty()|| !signUpView.getEmailField().getText().contains("@")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Email");
            alert.setContentText("Please enter valid email ");
            alert.showAndWait();
            return false;
        }
        //Check if entered email already exists
        if(SqlUtil.getUserByEmail(signUpView.getEmailField().getText()) != null)return false;



        if(signUpView.getPasswordField().getText().isEmpty()) return false;
        if(signUpView.getRePasswordField().getText().isEmpty()) return false;

        if(!signUpView.getPasswordField().getText().equals(signUpView.getRePasswordField().getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Registration");
            alert.setContentText("Passwords are not equal");
            alert.showAndWait();
            return false;
        }else {
            return true;
        }
    }

}
