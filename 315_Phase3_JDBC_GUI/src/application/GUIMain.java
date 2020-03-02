package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

<<<<<<< HEAD:315_Phase3_JDBC_GUI/src/application/GUIMain.java
public class GUIMain extends Application {

=======
public class Main extends Application {
	
>>>>>>> 07be223e5fa6f2906dc201c92b92cae71ecc5d92:315_Phase3_JDBC_GUI/src/application/Main.java
	@Override
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
