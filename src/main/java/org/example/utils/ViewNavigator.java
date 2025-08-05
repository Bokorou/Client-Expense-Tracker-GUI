package org.example.utils;

/*
    * ViewNavigator is a utility class responsible for managing the navigation
    * Between different scenes within the same primary stage(window) of a javaFX application.
    * It provides methods to set the main stage and switch between different views(scenes)
 */

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewNavigator {
    private static Stage mainStage;

    public static void setMainStage(Stage stage){
        mainStage = stage; //like creating a JFrame
    }

    public static void switchViews(Scene scene){
        if(mainStage != null){
            mainStage.setScene(scene);
            mainStage.show();
        }
    }
}
