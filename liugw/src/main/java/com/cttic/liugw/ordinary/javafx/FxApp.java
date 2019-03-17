package com.cttic.liugw.ordinary.javafx;

import java.time.Instant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FxApp extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Instant now = Instant.now();
        // TODO Auto-generated method stub
        Label label = new Label("Hello JavaFx");
        label.setFont(new Font(100));

        Button button = new Button("Red");
        button.setOnAction(event -> label.setTextFill(Color.RED));

        Slider slider = new Slider();
        slider.valueProperty().addListener(property -> label.setFont(new Font(slider.getValue())));

        stage.setScene(new Scene(label));
        //stage.setScene(new Scene(button));
        stage.setScene(new Scene(slider));
        stage.setTitle("hello");
        stage.show();
    }

}
