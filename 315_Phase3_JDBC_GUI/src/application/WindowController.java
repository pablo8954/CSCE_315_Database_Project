package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowController {
	
	@FXML 
	MenuButton mbutton; //Table 1
	@FXML 
	Button getResultsButton;
	@FXML
	MenuItem confItem;
	@FXML
	MenuItem gameItem;
	@FXML
	MenuItem playerItem;
	@FXML
	MenuItem stadiumYearwiseItem;
	@FXML
	MenuItem teamItem;
	@FXML 
	TextArea outputTextArea;
	@FXML
	CheckBox generateTextFileCheckBox;
	@FXML 
	TextArea userInputLabel;
	
	String dataSelection = "";
	String teamName = "";
	String teamType = "";
	String conferenceName = "";
	String playerFirstName = "";
	String playerLastName = "";
	String opposingTeam = "";
	String stadiumName = "";
	int year = 0; 
	boolean resultsRequested = false;
	
	//All functions below are for updating menu text for teams
	public void confItemSelected() throws IOException {
		MenuButton m = mbutton;
		MenuItem mitem = confItem;
		m.setText(mitem.getText());
		//Boot the conference info window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Conference.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 150);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Conference Data");
		stage.showAndWait();
		//Get the info from conference window
		ConferenceController controller = loader.getController();
		conferenceName = controller.getConferenceName();
		year = controller.getYear();
		//Update the userInputLabel
		String partOne = "Requesting conference data for\n" + conferenceName;
		String partTwo = " in " + year;
		if(year == 0) {
			userInputLabel.setText(partOne);
		}
		else {
			userInputLabel.setText(partOne + partTwo);
		}
	}
	
	public void gameItemSelected() throws IOException {
		MenuButton m = mbutton;
		MenuItem mitem = gameItem;
		m.setText(mitem.getText());
		//Boot the conference info window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Game.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 150);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Game Data");
		stage.showAndWait();
		//Get the info from conference window
		GameController controller = loader.getController();
		teamName = controller.getTeamName();
		year = controller.getYear();
		//Update userInputLabel
		String partOne = "Requesting game data for\n" + teamName;
		String partTwo = " in " + year;
		if(year == 0) {
			userInputLabel.setText(partOne);
		}
		else {
			userInputLabel.setText(partOne + partTwo);
		}
	}
	
	
	public void playerItemSelected() throws IOException {
		MenuButton m = mbutton;
		MenuItem mitem = playerItem;
		m.setText(mitem.getText());
		//Boot the conference info window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Player.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 250);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Player Data");
		stage.showAndWait();
		//Get the info from conference window
		PlayerController controller = loader.getController();
		playerFirstName = controller.getFirstName();
		playerLastName = controller.getLastName();
		opposingTeam = controller.getOpposingTeam();
		year = controller.getYear();
		//Update userInputLabel
		String partOne = "Requesting player data for\n" + playerFirstName + " " + playerLastName;
		String partTwo = " in " + year;
		if(year == 0) {
			userInputLabel.setText(partOne);
		}
		else {
			userInputLabel.setText(partOne + partTwo);
		}
	}
	
	public void satdiumYearwiseItemSelected() throws IOException {
		MenuButton m = mbutton;
		MenuItem mitem = stadiumYearwiseItem;
		m.setText(mitem.getText());
		//Boot the conference info window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Stadium.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 100);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Stadium Data");
		stage.showAndWait();
		//Get the info from conference window
		StadiumController controller = loader.getController();
		stadiumName = controller.getStadiumName();
		//Update userInputLabel
		userInputLabel.setText("Requesting stadium data for\n" + stadiumName);
		
	}

	public void teamItemSelected() throws IOException {
		MenuButton m = mbutton;
		MenuItem mitem = teamItem;
		m.setText(mitem.getText());
		//Boot the conference info window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Team.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 250);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Team Data");
		stage.showAndWait();
		//Get the info from conference window
		TeamController controller = loader.getController();
		teamName = controller.getTeamName();
		opposingTeam = controller.getOpposingTeam();
		year = controller.getYear();
		teamType = controller.getTeamType();
		//Update userInputLabel
		if(teamType.equals("General")) {
			userInputLabel.setText("Requesting general data for\n" + teamName);
		}
		else if(teamType.equals("Game")) {
			userInputLabel.setText("Requesting game data for\n" + teamName + " against "
					+ opposingTeam + " in year");
		}
		else if(teamType.equals("Play")) {
			userInputLabel.setText("Requesting play data for\n" + teamName + " against "
					+ opposingTeam + " in year");
		}
	}
	
	public void getResultsPressed() {
		resultsRequested = true;
	}
	
	public void resetResultsRequested() {
		resultsRequested = false;
	}
	
	public boolean getResultsRequested() {
		boolean result = resultsRequested;
		resultsRequested = false;
		return result;
	}
	
	public boolean generateTextFile() {
		return generateTextFileCheckBox.isSelected();
	}
	
	//get data selection
	public String getDataSelection() {
		dataSelection = mbutton.getText();
		return dataSelection;
	}
	
	//get team name
	public String getTeamName() {
		return teamName;
	}
	
	//get conference name
	public String getConferenceName() {
		return conferenceName;
	}
	
	//get player name
	public String getPlayerFirstName() {
		return playerFirstName;
	}
	
	//get player name
	public String getPlayerLastName() {
		return playerLastName;
	}
	
	//get opposing team name
	public String getOpposingTeamName() {
		return opposingTeam;
	}
	
	//get stadium name
	public String getStadiumName() {
		return stadiumName;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getTeamType() {
		return teamType;
	}
	
	public void updateOutputTextArea(String result) {
		outputTextArea.setEditable(true);
		outputTextArea.setText(result);
		outputTextArea.setEditable(false);
	    dataSelection = "";
		teamName = "";
		teamType = "";
		conferenceName = "";
		playerFirstName = "";
		playerLastName = "";
		opposingTeam = "";
		stadiumName = "";
		year = 0; 
		
	}

}
