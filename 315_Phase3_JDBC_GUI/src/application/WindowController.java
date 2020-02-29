package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class WindowController {
	
	@FXML
	Button button;
	@FXML 
	MenuButton mbutton;
	
	
	public void buttonPressed(){
		System.out.println("Button pressed!");
		MenuButton mb = mbutton;
		MenuItem mitem = new MenuItem("Years");
		mb.getItems().add(mitem);
		
	}
	

}
