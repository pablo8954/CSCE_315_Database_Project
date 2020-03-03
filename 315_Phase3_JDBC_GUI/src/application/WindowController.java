package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	
	String dataSelection = "";
	String teamName = "";
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
		stage.showAndWait();
		//Get the info from conference window
		ConferenceController controller = loader.getController();
		conferenceName = controller.getConferenceName();
		year = controller.getYear();
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
		Scene scene = new Scene(root, 400, 200);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		//Get the info from conference window
		GameController controller = loader.getController();
		teamName = controller.getTeamName();
		year = controller.getYear();
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
		Scene scene = new Scene(root, 400, 200);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		//Get the info from conference window
		PlayerController controller = loader.getController();
		playerFirstName = controller.getFirstName();
		playerLastName = controller.getLastName();
		opposingTeam = controller.getOpposingTeam();
		year = controller.getYear();
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
		stage.showAndWait();
		//Get the info from conference window
		StadiumController controller = loader.getController();
		stadiumName = controller.getStadiumName();
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
		Scene scene = new Scene(root, 400, 200);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		//Get the info from conference window
		TeamController controller = loader.getController();
		teamName = controller.getTeamName();
		opposingTeam = controller.getOpposingTeam();
		year = controller.getYear();
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
	
	public void updateOutputTextArea(String result) {
		outputTextArea.setEditable(true);
		outputTextArea.setText(result);
		outputTextArea.setEditable(false);
	}

}
