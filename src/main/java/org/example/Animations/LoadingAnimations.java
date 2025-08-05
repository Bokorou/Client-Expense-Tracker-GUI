package org.example.Animations;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
//@8:00:07
public class LoadingAnimations extends Pane {
    private final Rectangle rectangle;
    private final Label loadingLabel;

    public LoadingAnimations(double screenWidth, double screenHeight) {
        rectangle =  new Rectangle(screenWidth, screenHeight, Color.BLACK);
        rectangle.setOpacity(0.5);
        loadingLabel = new Label("Loading...");
        loadingLabel.getStyleClass().addAll("text-size-lg", "text-white");
        loadingLabel.setLayoutX(screenWidth/2 - 150);
        loadingLabel.setLayoutY(screenHeight/2 - 10);

        setMinSize(screenWidth, screenHeight);
        getChildren().addAll(rectangle, loadingLabel);
        setVisible(false);
        setManaged(false);
    }

    public void resizeWidth(double newWidth){
        rectangle.setWidth(newWidth);
        loadingLabel.setLayoutX(newWidth/2 - 150);
        setMinWidth(newWidth);
    }

    public void resizeHeight(double newHeight){
        rectangle.setHeight(newHeight);
        loadingLabel.setLayoutY(newHeight/2 - 10);
        setMinHeight(newHeight);

    }
}
