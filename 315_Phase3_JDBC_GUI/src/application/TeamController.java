package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeamController {
	
	@FXML 
	TextField TeamNameText;
	String teamName;
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
	int year;
	@FXML 
	TextField OpposingTeamText;
	String opposingTeam;
	@FXML 
	Button OKButton;
	
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

}
