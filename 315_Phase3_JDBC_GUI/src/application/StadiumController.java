package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StadiumController {
	
	@FXML 
	TextField StadiumNameText;
	String stadiumName;
	
	@FXML 
	Button OKButton;
	
	
	public String getStadiumName() {
		return stadiumName;
	}
	
	public void OKPressed() {
		stadiumName = StadiumNameText.getText();
		Stage s = (Stage) OKButton.getScene().getWindow();
		s.close();
		
	}

}
