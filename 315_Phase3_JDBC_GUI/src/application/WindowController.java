package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowController {
	
	@FXML 
	MenuButton mbutton1; //Table 1
	@FXML
	MenuItem confItem1;
	@FXML
	MenuItem gameItem1;
	@FXML
	MenuItem playItem1;
	@FXML
	MenuItem playerItem1;
	@FXML
	MenuItem stadiumYearwiseItem1;
	@FXML
	MenuItem teamItem1;
	
	
	//All functions below are for updating menu text for teams
	public void confItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = confItem1;
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
		String h = controller.getConferenceName();
		System.out.println(h);
		int year = controller.getYear();
		System.out.println(year);
	}
	
	public void gameItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = gameItem1;
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
		String h = controller.getTeamName();
		System.out.println(h);
		int year = controller.getYear();
		System.out.println(year);
	}
	
	public void playItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playItem1;
		m.setText(mitem.getText());
	}
	
	
	public void playerItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = playerItem1;
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
		String h = controller.getPlayerName();
		System.out.println(h);
		String t = controller.getOpposingTeam();
		System.out.println(t);
		int year = controller.getYear();
		System.out.println(year);
	}
	
	public void satdiumYearwiseItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = stadiumYearwiseItem1;
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
		String h = controller.getStadiumName();
		System.out.println(h);
	}

	public void teamItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = teamItem1;
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
		String h = controller.getTeamName();
		System.out.println(h);
		String t = controller.getOpposingTeam();
		System.out.println(t);
		int year = controller.getYear();
		System.out.println(year);
	}

}
