package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.Controllers.LoginController;
import org.example.Controllers.SignupController;
import org.example.utils.Utility;
import org.example.utils.ViewNavigator;

public class SignUpView {

    private Label expenseTrackerLabel =  new Label("Expense Tracker");
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private PasswordField rePasswordField = new PasswordField();
    private Button registerButton = new Button("Register");
    private Label loginLabel = new Label("Already have an account? Login Here");

    public void show(){
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); //Linking CSS style sheet

        new SignupController(this);
        ViewNavigator.switchViews(scene);
    }

    private Scene createScene(){
        VBox mainContainer = new VBox(44);
        mainContainer.getStyleClass().addAll("main-background");
        mainContainer.setAlignment(Pos.TOP_CENTER);

        expenseTrackerLabel.getStyleClass().addAll("header", "text-white");
        VBox signUpFormBox = createSignUpForm();

        mainContainer.getChildren().addAll(expenseTrackerLabel, signUpFormBox);
        return new Scene(mainContainer, Utility.APP_WIDTH, Utility.APP_HEIGHT);

    }

    private VBox createSignUpForm(){
        VBox signUpForm = new VBox(44);
        signUpForm.setAlignment(Pos.CENTER);

        nameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg", "rounded-border");
        nameField.setMaxWidth(473);
        nameField.setPromptText("Enter name");

        emailField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg", "rounded-border");
        emailField.setMaxWidth(473);
        emailField.setPromptText("Enter email");

        passwordField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg", "rounded-border");
        passwordField.setMaxWidth(473);
        passwordField.setPromptText("Enter Password");

        rePasswordField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg", "rounded-border");
        rePasswordField.setMaxWidth(473);
        rePasswordField.setPromptText("Re-Enter Password");

        registerButton.getStyleClass().addAll("text-size-lg", "bg-light-blue", "text-white", "text-weight-700", "rounded-border");
        registerButton.setMaxWidth(473);
        loginLabel.getStyleClass().addAll("text-size-md", "text-light-gray", "text-underline", "link-text");

        signUpForm.getChildren().addAll(nameField,emailField,passwordField,rePasswordField,registerButton,loginLabel);

        return signUpForm;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
    }

    public Label getExpenseTrackerLabel() {
        return expenseTrackerLabel;
    }

    public void setExpenseTrackerLabel(Label expenseTrackerLabel) {
        this.expenseTrackerLabel = expenseTrackerLabel;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public PasswordField getRePasswordField() {
        return rePasswordField;
    }

    public void setRePasswordField(PasswordField rePasswordField) {
        this.rePasswordField = rePasswordField;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }

    public Label getLoginLabel() {
        return loginLabel;
    }

    public void setLoginLabel(Label loginLabel) {
        this.loginLabel = loginLabel;
    }
}
