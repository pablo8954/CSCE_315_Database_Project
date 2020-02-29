package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class WindowController {
	
	@FXML
	Button button;
	@FXML 
	MenuButton mbutton1;
	@FXML 
	MenuButton mbutton2;
	@FXML 
	MenuButton mbutton3;
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
	public void confItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = confItem1;
		m.setText(mitem.getText());
		updateAttr1(m.getText());
	}
	
	public void confItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = confItem2;
		m.setText(mitem.getText());
	}
	
	public void driveItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = driveItem1;
		m.setText(mitem.getText());
	}
	
	public void driveItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = driveItem2;
		m.setText(mitem.getText());
	}
	
	
	public void gameItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = gameItem1;
		m.setText(mitem.getText());
	}
	
	public void gameItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = gameItem2;
		m.setText(mitem.getText());
	}
	
	public void playItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playItem1;
		m.setText(mitem.getText());
	}
	
	public void playItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playItem2;
		m.setText(mitem.getText());
	}
	
	public void playMetricsItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playMetricsItem1;
		m.setText(mitem.getText());
	}
	
	public void playMetricsItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playMetricsItem2;
		m.setText(mitem.getText());
	}
	
	public void playerItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playerItem1;
		m.setText(mitem.getText());
	}
	
	public void playerItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerItem2;
		m.setText(mitem.getText());
	}
	
	public void playerMetricsGamewiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playerMetricsGamewiseItem1;
		m.setText(mitem.getText());
	}
	
	public void playerMetricsGamewiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerMetricsGamewiseItem2;
		m.setText(mitem.getText());
	}
	
	public void playerYearwiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = playerYearwiseItem1;
		m.setText(mitem.getText());
	}
	
	public void playerYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = playerYearwiseItem2;
		m.setText(mitem.getText());
	}
	
	public void satdiumYearwiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = stadiumYearwiseItem1;
		m.setText(mitem.getText());
	}
	
	public void satdiumYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = stadiumYearwiseItem2;
		m.setText(mitem.getText());
	}
	
	public void teamItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = teamItem1;
		m.setText(mitem.getText());
	}
	
	public void teamItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamItem2;
		m.setText(mitem.getText());
	}
	
	public void teamMetricsGamewiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = teamMetricsGamewiseItem1;
		m.setText(mitem.getText());
	}
	
	public void teamMetricsGamewiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamMetricsGamewiseItem2;
		m.setText(mitem.getText());
	}
	
	public void teamYearwiseItemSelected1() {
		MenuButton m = mbutton1;
		MenuItem mitem = teamYearwiseItem1;
		m.setText(mitem.getText());
	}
	
	public void teamYearwiseItemSelected2() {
		MenuButton m = mbutton2;
		MenuItem mitem = teamYearwiseItem2;
		m.setText(mitem.getText());
	}
	
	//Update attribute items based on table selection
	public void updateAttr1(String table) {
		MenuButton m = mbutton3;
		//remove all items from the menu
		for(int i = m.getItems().size()-1; i > -1; i--) {
			m.getItems().remove(i);
		}
		if(table == "Conference Yearwise") {
			for(int i = 0; i < Attributes.confAttr.length; i++) {
				m.getItems().add(new MenuItem(Attributes.confAttr[i]));
			}
		}
	}
	public void updateAttr2() {
		MenuButton m = mbutton2;
	}
	

}
