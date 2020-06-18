package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InfrastructureController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private JFXTextField txtStationCost;

    @FXML
    private JFXTextField txtPlugCost;

    @FXML
    private JFXTextField txtChargingPower;

    @FXML
    private JFXTextField txtPlugLifespan;

    @FXML
    private JFXTextField txtDiscountRate;

    @FXML
    private JFXTextField txtChargingCost;

    @FXML
    private JFXTextField txtGasolinePrice;

    @FXML
    private JFXTextField txtWaitingTime;

    @FXML
    private JFXTextField txtProbabilityThreshold;

    @FXML
    private Label labelLOSExplanation;

    @FXML
    private JFXButton btnReturn;
    
    @FXML
    private ImageView imageBack = new ImageView();

    @FXML
    void initialize() {
    	readParameters();
    	
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	
    	//Setting tooltip
    	btnReturn.setTooltip(new Tooltip("Return to main page"));
    	txtStationCost.setTooltip(
    			new Tooltip(
    					"Station annual capital cost ($/yr);\n"
    				  + "non-negative real value"
    							));
    	txtPlugCost.setTooltip(
    			new Tooltip(
    					"Upfront capital cost ($) per plug;\n"
    				  + "positive real value"
    							));
    	txtChargingPower.setTooltip(
    			new Tooltip(
    					"Design charging power of (KW) each plug;\n"
    				  + "positive real value"
    							));
    	txtPlugLifespan.setTooltip(
    			new Tooltip(
    					"Design lifespan (years) of each plug;\n"
    				  + "positive real value"
    							));
    	txtDiscountRate.setTooltip(
    			new Tooltip(
    					"Discount rate (%) to calculate annuity ($/year);\n"
    				  + "[0, 100] real value"
    							));
    	txtChargingCost.setTooltip(
    			new Tooltip(
    					"Charging cost ($/KWh);\n"
    				  + "non-negative real value"
    							));
    	txtGasolinePrice.setTooltip(
    			new Tooltip(
    					"Gasoline price ($/gallon);\n"
    				  + "non-negative real value"
    							));
    	txtWaitingTime.setTooltip(
    			new Tooltip(
    					"Design waiting time (mins) criteria;\n"
    					+ "one of the two factors for level of service;\n"
    					+ "for determining station capacity;\n"  					
    					+ "non-negative real value"
    							));
    	txtProbabilityThreshold.setTooltip(
    			new Tooltip(
    					"Design probability (%) criteria;\n"
    					+ "one of the two factors for level of service;\n"
    					+ "for determining station capacity;\n"  					
    					+ "[0, 100) real value"
    							));
    	//end setting tooltip
    	
    	updatelabelLOSExplanation(txtWaitingTime.getText(),txtProbabilityThreshold.getText());
    	
    	txtWaitingTime.textProperty().addListener((observable, oldValue, newValue) -> {
    		updatelabelLOSExplanation(newValue, txtProbabilityThreshold.getText());
    	});
    	
    	txtProbabilityThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
    		updatelabelLOSExplanation(txtWaitingTime.getText(), newValue);
    	});
    	
    	btnReturn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			saveParameters();
    			btnReturn.getScene().getWindow().hide();
    	    	   
    	    	Stage newStage = new Stage();
    	    		
    			try {
    				Parent root = FXMLLoader.load(getClass().getResource("/application/view/MainView.fxml"));
    				Scene scene = new Scene(root);
    				newStage.setScene(scene);
    				scene.getStylesheets().clear();
    				scene.getStylesheets().add(getClass().getResource("/application/css/toolTipStyle.css").toExternalForm());
    				newStage.sizeToScene();
    				newStage.show();
    				newStage.setResizable(false);
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}       	
    		}
    	});	

    }
    
    private void updatelabelLOSExplanation(String time, String prob) {
    	labelLOSExplanation.setText("LOS Requirement: the probability of finding available charger"
    			+ " within " + time + " mins is at least " + prob + "%");
    }
    
    private void readParameters() {
    	txtStationCost.setText(String.format("%.0f", UIParameters.gettxtStationCostValue()));
    	txtPlugCost.setText(String.format("%.0f", UIParameters.gettxtPlugCostValue()));
    	txtChargingPower.setText(String.format("%.0f", UIParameters.gettxtChargingPowerValue()));
    	txtPlugLifespan.setText(String.format("%.1f", UIParameters.gettxtPlugLifespanValue()));
    	txtDiscountRate.setText(String.format("%.1f", UIParameters.gettxtDiscountRateValue()));
    	txtChargingCost.setText(String.format("%.2f", UIParameters.gettxtChargingCostValue()));
    	txtGasolinePrice.setText(String.format("%.2f", UIParameters.gettxtGasolinePriceValue()));
    	txtWaitingTime.setText(String.format("%.1f", UIParameters.gettxtWaitingTimeValue()));
    	txtProbabilityThreshold.setText(String.format("%.1f", UIParameters.gettxtProbabilityThresholdValue()));
    }
    
    private void saveParameters() {
    	UIParameters.settxtStationCostValue(Double.parseDouble(txtStationCost.getText()));
    	UIParameters.settxtPlugCostValue(Double.parseDouble(txtPlugCost.getText()));
    	UIParameters.settxtChargingPowerValue(Double.parseDouble(txtChargingPower.getText()));
    	UIParameters.settxtPlugLifespanValue(Double.parseDouble(txtPlugLifespan.getText()));
    	UIParameters.settxtDiscountRateValue(Double.parseDouble(txtDiscountRate.getText()));
    	UIParameters.settxtChargingCostValue(Double.parseDouble(txtChargingCost.getText()));
    	UIParameters.settxtGasolinePriceValue(Double.parseDouble(txtGasolinePrice.getText()));
    	UIParameters.settxtWaitingTimeValue(Double.parseDouble(txtWaitingTime.getText()));
    	UIParameters.settxtProbabilityThresholdValue(Double.parseDouble(txtProbabilityThreshold.getText()));
    }
}
