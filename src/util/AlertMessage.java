package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMessage {
	    
	    private Alert alert;
	    
	    public void errorMessage(String message){
	        
	        alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Mensagem de erro");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	        
	    }
	    
	    public void successMessage(String message){
	        
	        alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Mensagem informativa");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	        
	    }
	    
	}
