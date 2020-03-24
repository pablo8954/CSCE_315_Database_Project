package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class QuestionOneController {
	
	@FXML 
	TextField TeamNameText;
	String teamName;
	@FXML 
	TextField OpposingTeamNameText;
	String opposingTeamName;
	@FXML 
	Button OKButton;
	
	public String getTeamName() {
		return teamName;
		
	}
	
	public String getOpposingTeam() {
		return opposingTeamName;
		
	}
	
	
	public void OKPressed() {
		//Extract team names and close window
		teamName = TeamNameText.getText();
		opposingTeamName = OpposingTeamNameText.getText();
		Stage s = (Stage) OKButton.getScene().getWindow();
		s.close();
		
	}

}
