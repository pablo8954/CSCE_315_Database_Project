package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeamController {
	
	@FXML
	RadioButton GeneralRadio;
	@FXML
	RadioButton GameRadio;
	@FXML
	RadioButton PlayRadio;
	@FXML 
	TextField TeamNameText;
	@FXML 
	MenuButton yearButton;
	@FXML 
	MenuItem item2005;
	@FXML 
	MenuItem item2006;
	@FXML 
	MenuItem item2007;
	@FXML 
	MenuItem item2008;
	@FXML 
	MenuItem item2009;
	@FXML 
	MenuItem item2010;
	@FXML 
	MenuItem item2011;
	@FXML 
	MenuItem item2012;
	@FXML 
	MenuItem item2013;
	@FXML 
	TextField OpposingTeamText;
	@FXML 
	Button OKButton;
	String teamName;
	String opposingTeam;
	int year;
	String type = "General";
	
	public void generalRadioSelected() {
		GeneralRadio.setSelected(true);
		GameRadio.setSelected(false);
		PlayRadio.setSelected(false);
		type = "General";
	}
	
	public void gameRadioSelected() {
		GeneralRadio.setSelected(false);
		GameRadio.setSelected(true);
		PlayRadio.setSelected(false);
		type = "Game";
	}
	
	public void playRadioSelected() {
		GeneralRadio.setSelected(false);
		GameRadio.setSelected(false);
		PlayRadio.setSelected(true);
		type = "Play";
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public int getYear() {
		return year;
	}
	public String getOpposingTeam() {
		return opposingTeam;
	}
	public void selected2005() {
		MenuButton m = yearButton;
		MenuItem mitem = item2005;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2006() {
		MenuButton m = yearButton;
		MenuItem mitem = item2006;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2007() {
		MenuButton m = yearButton;
		MenuItem mitem = item2007;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2008() {
		MenuButton m = yearButton;
		MenuItem mitem = item2008;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2009() {
		MenuButton m = yearButton;
		MenuItem mitem = item2009;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2010() {
		MenuButton m = yearButton;
		MenuItem mitem = item2010;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2011() {
		MenuButton m = yearButton;
		MenuItem mitem = item2011;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2012() {
		MenuButton m = yearButton;
		MenuItem mitem = item2012;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void selected2013() {
		MenuButton m = yearButton;
		MenuItem mitem = item2013;
		m.setText(mitem.getText());
		year = Integer.parseInt(m.getText());
	}
	
	public void OKPressed() {
		teamName = TeamNameText.getText();
		opposingTeam = OpposingTeamText.getText();
		Stage s = (Stage) OKButton.getScene().getWindow();
		s.close();
		
	}
	
	public String getTeamType() {
		if(GeneralRadio.isSelected()) {
			return "General";
		}
		else if(GameRadio.isSelected()) {
			return "Game";
		}
		else if(PlayRadio.isSelected()) {
			return "Play";
		}
		return "";
	}

}
