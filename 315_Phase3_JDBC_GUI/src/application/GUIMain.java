package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMain extends Application {

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Window.fxml"));
		Scene scene = new Scene(root, 800, 800);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Gay");
		primaryStage.resizableProperty().setValue(Boolean.FALSE);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
