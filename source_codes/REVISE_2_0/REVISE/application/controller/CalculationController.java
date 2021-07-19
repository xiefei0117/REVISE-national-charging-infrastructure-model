package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;

import application.model.GeneticAlgorithm.GA;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CalculationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label calculationLabel;

    @FXML
    public JFXTextArea txtFieldCalculation;

    @FXML
    private JFXProgressBar processbarCalculation;

    @FXML
    private JFXButton btnTerminate;
    
    @FXML
    private Label labelProcessCalculation;
    
    private boolean isCancelledVariable = false;
    

    @FXML
    void initialize() {
    	
    	//Setting tooltip
    	btnTerminate.setTooltip(
    			new Tooltip(
    					"Terminate algorithm and\n"
    					+ "save current results"
    					));
    	
    	//end setting tooltip
    	
    	txtFieldCalculation.setText("");
    	processbarCalculation.setProgress(0);
    	setProgressLabel(0);
    	
    	runGA(this);
    	

    	
    	btnTerminate.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			isCancelledVariable = true;
    		}
    	});	   	
    	/*
    	btnTerminate.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
    	    @Override
    	    public void handle(WindowEvent e) {
    	    	
    	    	Platform.exit();
    	    	System.exit(0);
    	    }
    	});
    	*/

    }
    
    public void JFXTextAreaAppendText (String text) {
    	txtFieldCalculation.setText(txtFieldCalculation.getText() + text);
    	txtFieldCalculation.setScrollTop(Double.MAX_VALUE);
    }
    
    public String JFXTextAreaGetText() {
    	return txtFieldCalculation.getText();
    }
    
    public void runGA(CalculationController controller1) {
    	Task task = new Task<Void>() {
    		@Override
    			public Void call() {
    				GA ga = new GA(UIParameters.gettxtNumGenesValue(), true, controller1);
    				ga.GARunWithTime(UIParameters.gettxtTimeLimitValue(),
    						UIParameters.gettoggleTournamentSelected(), 5, 1, 0, Double.MAX_VALUE, true);
    				return null;
    			}
    		
    	};
    	
    	Thread thread = new Thread(task);
    	thread.setDaemon(true);
    	thread.start();
    	
    }
    
    public boolean isCancelled() {
    	return isCancelledVariable;
    }
    
    public void setProgressBar(double value) {
    	processbarCalculation.setProgress(value);
    }
    
    public void setProgressLabel(double value) {
    	labelProcessCalculation.setText(String.format("%.2f", processbarCalculation.getProgress()*100)+"%");
    }
    
    
}