package org.example.utils;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Utility {
    public static final int APP_WIDTH = 1500;
    public static final int APP_HEIGHT = 800;

    //Successful login

    public static void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String getHexColourValue(ColorPicker colorPicker){
        String colour = colorPicker.getValue().toString();
        return colour.substring(2, colour.length() - 2);
    }

    /*public static void showToastAlert(Stage ownerStage, String toastMsg, int durationSecs){
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.setAlwaysOnTop(true);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(toastMsg);
        label.setStyle("-fx-background-color: rgba(500,500,500,0.8); -fx-text-fill: white; -fx-padding: 15px 25px;" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, black, 5, 0.5, 0, 1);");
        label.setOpacity(0.9);

        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-radius: 10; -fx-background-color: transparent;");
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);

        toastStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(durationSecs));
        delay.setOnFinished(e -> toastStage.close());
        delay.play();

    }

     */

}
