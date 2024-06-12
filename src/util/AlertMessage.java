package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMessage {
	    
	    private Alert alert;
	    
	    public void errorMessage(String message){
	        
	        alert = new Alert(AlertType.ERROR);
	        alert.setTitle("ERRO");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	        
	    }
	    
	    public void successMessage(String message){
	        
	        alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("INFORMAÇÃO");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	        
	    }
	    
	}
