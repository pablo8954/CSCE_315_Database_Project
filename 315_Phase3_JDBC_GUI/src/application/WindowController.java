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
	@FXML 
	Button questionOneButton;
	@FXML 
	Button questionTwoButton;
	@FXML 
	Button questionThreeButton;
	
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
	boolean questionOneResultsRequested = false;
	boolean questionTwoResultsRequested = false;
	boolean questionThreeResultsRequested = false;
	
	//Conference table selected
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
	}
	
	//Game table selected
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
	}
	
	
	//Player table selected
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
	}
	
	//Stadium table selected
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
	}

	//Team table selected
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
	}
	
	//Update boolean if user as pressed Get Results
	public void getResultsPressed() {
		resultsRequested = true;
	}
	
	//Open new window for question one
	public void questionOneButtonPressed() throws IOException {
		outputTextArea.setText("Given 2 teams, create a victory chain." + 
				"\n This may take up to 30 seconds...");
		//Ensure tables unselected
		mbutton.setText("Table Name");
		questionOneResultsRequested = true;
		//Boot window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("QuestionOne.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 175);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Question One");
		stage.showAndWait();
		//Get the info from conference window
		QuestionOneController controller = loader.getController();
		teamName = controller.getTeamName();
		opposingTeam = controller.getOpposingTeam();
	}
	
	//Open new window for question two
	public void questionTwoButtonPressed() throws IOException {
		outputTextArea.setText("Given a team, find the team with the most rushing yards vs. the given team.");
		//Ensure tables unselected
		mbutton.setText("Table Name");
		questionTwoResultsRequested = true;
		//Boot new window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("QuestionTwo.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 200);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Question Three");
		stage.showAndWait();
		//Get the info from conference window
		QuestionTwoController controller = loader.getController();
		teamName = controller.getTeamName();
		year = controller.getYear();
	}
	
	//Open new window for question two
	public void questionThreeButtonPressed() throws IOException {
		outputTextArea.setText("Given a team, is there a correlation between average attendance and team record.");
		//Ensure tables unselected
		mbutton.setText("Table Name");
		questionThreeResultsRequested = true; 
		//Boot new window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("QuestionThree.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 400, 200);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Question Five");
		stage.showAndWait();
		//Get the info from conference window
		QuestionThreeController controller = loader.getController();
		teamName = controller.getTeamName();
		year = controller.getYear();
	}
	
	public void resetResultsRequested() {
		resultsRequested = false;
	}
	
	public boolean getResultsRequested() {
		//Make sure right result sent using temp
		boolean result = resultsRequested;
		resultsRequested = false;
		return result;
	}
	
	public boolean getQuestionOneResultsRequested() {
		//Make sure right result sent using temp
		boolean result = questionOneResultsRequested;
		questionOneResultsRequested = false;
		return result;
	}
	
	public boolean getQuestionTwoResultsRequested() {
		//Make sure right result sent using temp
		boolean result = questionTwoResultsRequested;
		questionTwoResultsRequested = false;
		return result;
	}
	
	public boolean getQuestionThreeResultsRequested() {
		//Make sure right result sent using temp
		boolean result = questionThreeResultsRequested;
		questionThreeResultsRequested = false;
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
	
	//get year
	public int getYear() {
		return year;
	}
	
	//get team type
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
