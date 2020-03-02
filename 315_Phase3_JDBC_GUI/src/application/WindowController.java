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
	MenuButton mbutton2; //Attributes 1
	@FXML 
	MenuButton mbutton3; //Table 2
	@FXML 
	MenuButton mbutton4; //Attributes 2
	@FXML
	MenuItem confItem1;
	@FXML
	MenuItem confItem2;
	@FXML
	MenuItem driveItem1;
	@FXML
	MenuItem driveItem2;
	@FXML
	MenuItem gameItem1;
	@FXML
	MenuItem gameItem2;
	@FXML
	MenuItem playItem1;
	@FXML
	MenuItem playItem2;
	@FXML
	MenuItem playMetricsItem1;
	@FXML
	MenuItem playMetricsItem2;
	@FXML
	MenuItem playerItem1;
	@FXML
	MenuItem playerItem2;
	@FXML
	MenuItem playerMetricsGamewiseItem1;
	@FXML
	MenuItem playerMetricsGamewiseItem2;
	@FXML
	MenuItem playerYearwiseItem1;
	@FXML
	MenuItem playerYearwiseItem2;
	@FXML
	MenuItem stadiumYearwiseItem1;
	@FXML
	MenuItem stadiumYearwiseItem2;
	@FXML
	MenuItem teamItem1;
	@FXML
	MenuItem teamItem2;
	@FXML
	MenuItem teamMetricsGamewiseItem1;
	@FXML
	MenuItem teamMetricsGamewiseItem2;
	@FXML
	MenuItem teamYearwiseItem1;
	@FXML
	MenuItem teamYearwiseItem2;
	
	
	//All functions below are for updating menu text for teams
	public void confItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = confItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
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
	
	public void confItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = confItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void driveItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = driveItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
		
	}
	
	public void driveItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = driveItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	
	public void gameItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = gameItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
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
	
	public void gameItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = gameItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void playItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void playItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void playMetricsItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playMetricsItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void playMetricsItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playMetricsItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void playerItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = playerItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
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
	
	public void playerItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void playerMetricsGamewiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playerMetricsGamewiseItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void playerMetricsGamewiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerMetricsGamewiseItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void playerYearwiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playerYearwiseItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void playerYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerYearwiseItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void satdiumYearwiseItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = stadiumYearwiseItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
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
	
	public void satdiumYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = stadiumYearwiseItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void teamItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = teamItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
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
	
	public void teamItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void teamMetricsGamewiseItemSelected1() throws IOException {
		MenuButton m = mbutton1;
		MenuItem mitem = teamMetricsGamewiseItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
		
	}
	
	public void teamMetricsGamewiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamMetricsGamewiseItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	public void teamYearwiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = teamYearwiseItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void teamYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamYearwiseItem2;
		m.setText(mitem.getText());
		updateAttr2(m.getText());
	}
	
	//Update attribute items based on table selection
	public void updateAttr1(String table) {
		MenuButton m = mbutton3;
		updateAttr(m, table);
	}
	public void updateAttr2(String table) {
		MenuButton m = mbutton4;
		updateAttr(m, table);
	}
	
	public void updateAttr(MenuButton m, String table) {
		for(int i = m.getItems().size()-1; i > -1; i--) {
			m.getItems().remove(i);
		}
		if(table.equals("Conference Yearwise")) {
			for(int i = 0; i < Attributes.confAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.confAttr[i]));
			}
		}
		else if(table.equals("Drive")) {
			for(int i = 0; i < Attributes.driveAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.driveAttr[i]));
			}
		}
		else if(table.equals("Game")) {
			for(int i = 0; i < Attributes.gameAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.gameAttr[i]));
			}
		}
		else if(table.equals("Play")) {
			for(int i = 0; i < Attributes.playAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.playAttr[i]));
			}
		}
		else if(table.equals("Play Metrics")) {
			for(int i = 0; i < Attributes.playMetricsAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.playMetricsAttr[i]));
			}
		}
		else if(table.equals("Player")) {
			for(int i = 0; i < Attributes.playerAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.playerAttr[i]));
			}
		}
		else if(table.equals("Player Metrics Gamewise")) {
			for(int i = 0; i < Attributes.playerMetricsGamewiseAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.playerMetricsGamewiseAttr[i]));
			}
		}
		else if(table.equals("Player Yearwise")) {
			for(int i = 0; i < Attributes.playerYearwiseAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.playerYearwiseAttr[i]));
			}
		}
		else if(table.equals("Stadium Yearwise")) {
			for(int i = 0; i < Attributes.stadiumYearwiseAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.stadiumYearwiseAttr[i]));
			}
		}
		else if(table.equals("Team")) {
			for(int i = 0; i < Attributes.teamAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.teamAttr[i]));
			}
		}
		else if(table.equals("Team Metrics Gamewise")) {
			for(int i = 0; i < Attributes.teamMetricsGamewiseAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.teamMetricsGamewiseAttr[i]));
			}
		}
		else if(table.equals("Team Yearwise")) {
			for(int i = 0; i < Attributes.teamYearwiseAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.teamYearwiseAttr[i]));
			}
		}
	}
	

}
