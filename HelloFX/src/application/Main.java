package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Pane pane = new Pane();
        Label l = new Label("Enter member name..");
        l.setLayoutX(100);
        l.setLayoutY(200);

        TextField t = new TextField();
        t.setPromptText("Last Name, First Name");
        t.setLayoutX(100);
        t.setLayoutY(150);
        Button b = new Button();
        b.setText("Submit");
        b.setLayoutX(300);
        b.setLayoutY(150);
        pane.getChildren().add(l);
        pane.getChildren().add(t);
        pane.getChildren().add(b);

        Scene scene = new Scene(pane, 640, 480);
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}