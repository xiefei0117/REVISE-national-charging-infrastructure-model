package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import application.model.GeneticAlgorithm.StringMisMatchException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TravelerController {

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private Label labelNationalHomeChargingAvailability;

    @FXML
    private JFXTextField fileStateHomeChargingAvailability;

    @FXML
    private JFXButton chooseFileStateHomeChargingAvailability;

    @FXML
    private JFXSlider sliderNationalHomeChargingAvailability;

    @FXML
    private JFXSlider sliderInitialSOCNoHomeCharger;

    @FXML
    private Label labelInitialSOCNoHomeCharger;

    @FXML
    private JFXRadioButton radioHomeChargingNational;

    @FXML
    private ToggleGroup toggleHomeCharging;

    @FXML
    private JFXRadioButton radioHomeChargingState;

    @FXML
    private Label labelSOCWhenCharge;

    @FXML
    private JFXTextField txtVehicleOccupancy;

    @FXML
    private JFXSlider sliderSOCWhenCharge;

    @FXML
    private JFXTextField txtTimeValue;

    @FXML
    private JFXTextField txtMaximumDailyRange;

    @FXML
    private JFXTextField txtRentalCarCost;
       
    @FXML
    private ImageView imageBack = new ImageView();

    @FXML
    void initialize() {
    	
    	readParameters();
    	stateOntoggleHomeCharging();
    	
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	
    	//Setting tooltip
    	btnReturn.setTooltip(new Tooltip("Return to main page"));
    	sliderNationalHomeChargingAvailability.setTooltip(
    			new Tooltip(
    					"Change home charging availability (%) at national\n"
    				  + "scale. State level home charging availabilities are\n"
    	  			  + "different between states and change proportionally \n"
    	  			  + "accordingly."));
    	fileStateHomeChargingAvailability.setTooltip(
    			new Tooltip(
    					"Current file address for state\n"
    					+ "home charging availability."
    					));
    	chooseFileStateHomeChargingAvailability.setTooltip(
    			new Tooltip(
    					"Import file"
    					));
    	sliderInitialSOCNoHomeCharger.setTooltip(
    			new Tooltip(
    					"Set initial SOC(%) for travelers without home chargers;\n"
    					+ "travelers without home chargers are assumed to have\n"
    					+ "this SOC level at beggining of long distance trips."
    					));
    	txtVehicleOccupancy.setTooltip(
    			new Tooltip(
    					"Number of passengers in one vehicle;\n"
    					+ "positive real value"
    					));
    	txtTimeValue.setTooltip(
    			new Tooltip(
    					"Time value (e.g., charging time);\n"
    					+ "positive real value"
    					));
    	txtMaximumDailyRange.setTooltip(
    			new Tooltip(
    					"Assumed maximum daily range; a factor\n"
    					+ "to determine number of days for travel;\n"
    					+ "positive real value"
    					));
    	txtRentalCarCost.setTooltip(
    			new Tooltip(
    					"Daily rental cost for renting alternative\n"
    					+ "gasoline rental car if BEVs are not used;\n"
    					+ "positive real value"
    					));
    	sliderSOCWhenCharge.setTooltip(
    			new Tooltip(
    					"Average SOC(%) when BEVs start recharging\n"
    					+ "at charging stations."
    					));
    	
    	//end setting tooltip
    	
    	toggleHomeCharging.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
    	    public void changed(ObservableValue<? extends Toggle> ov,
    	            Toggle old_toggle, Toggle new_toggle) {
    	    	if (toggleHomeCharging.getSelectedToggle() != null) {
    	    		stateOntoggleHomeCharging(); 
    	    	}
    	    }});
    	
    	sliderNationalHomeChargingAvailability.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelNationalHomeChargingAvailability.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderInitialSOCNoHomeCharger.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    					labelInitialSOCNoHomeCharger.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderSOCWhenCharge.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    					labelSOCWhenCharge.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	FileChooser fileChooser = new FileChooser();
    	
    	chooseFileStateHomeChargingAvailability.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(null);
                        if (file != null) {
                        	fileStateHomeChargingAvailability.setText(file.getAbsolutePath());
                        	resetfileStateHomeChargingAvailability();
                        	sliderNationalHomeChargingAvailability.setValue(UIParameters.getsliderNationalHomeChargingAvailabilityValue());	
                        	labelNationalHomeChargingAvailability.setText(String.format("%.1f", sliderNationalHomeChargingAvailability.getValue()) +"%");
                        }
                    }
                });
    	
    	btnReturn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			saveParameters();
    			btnReturn.getScene().getWindow().hide();
    	    	   
    	    	Stage newStage = new Stage();
    	    		
    			try {
    				Parent root = FXMLLoader.load(getClass().getResource("/application/view/MainView.fxml"));
    				Scene scene = new Scene(root);
    				scene.getStylesheets().clear();
    				scene.getStylesheets().add(getClass().getResource("/application/css/toolTipStyle.css").toExternalForm());
    				newStage.setScene(scene);
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
    
  //change active national or state
    private void stateOntoggleHomeCharging() {
    	//enable or disable national
    	boolean isNationalSelected = radioHomeChargingNational.isSelected();
    	labelNationalHomeChargingAvailability.setDisable(!isNationalSelected);
    	sliderNationalHomeChargingAvailability.setDisable(!isNationalSelected);
    	fileStateHomeChargingAvailability.setDisable(isNationalSelected);
    	chooseFileStateHomeChargingAvailability.setDisable(isNationalSelected);
    }
    
    private void readParameters() {
    	radioHomeChargingNational.setSelected(UIParameters.getradioHomeChargingNationalSelected());
    	sliderNationalHomeChargingAvailability.setValue(UIParameters.getsliderNationalHomeChargingAvailabilityValue());	
    	labelNationalHomeChargingAvailability.setText(String.format("%.1f", sliderNationalHomeChargingAvailability.getValue()) +"%");
    	
    	fileStateHomeChargingAvailability.setText(UIParameters.getfileStateHomeChargingAvailabilityValue());
    	
    	sliderInitialSOCNoHomeCharger.setValue(UIParameters.getsliderInitialSOCNoHomeChargerValue());
    	labelInitialSOCNoHomeCharger.setText(String.format("%.1f", sliderInitialSOCNoHomeCharger.getValue()) +"%");
    	
    	txtVehicleOccupancy.setText(String.format("%.2f", UIParameters.gettxtVehicleOccupancyValue()));
    	txtTimeValue.setText(String.format("%.2f", UIParameters.gettxtTimeValueValue()));
    	txtMaximumDailyRange.setText(String.format("%.1f", UIParameters.gettxtMaximumDailyRangeValue()));
    	txtRentalCarCost.setText(String.format("%.2f", UIParameters.gettxtRentalCarCostValue()));
    	
    	sliderSOCWhenCharge.setValue(UIParameters.getsliderSOCWhenChargeValue());
    	labelSOCWhenCharge.setText(String.format("%.1f", sliderSOCWhenCharge.getValue()));
    	
    }
    
    private void saveParameters() {
    	UIParameters.setradioHomeChargingNationalSelected(radioHomeChargingNational.isSelected());
    	UIParameters.setsliderNationalHomeChargingAvailabilityValue(sliderNationalHomeChargingAvailability.getValue());

    	UIParameters.setfileStateHomeChargingAvailabilityValue(fileStateHomeChargingAvailability.getText());
    	
    	UIParameters.setsliderInitialSOCNoHomeChargerValue(sliderInitialSOCNoHomeCharger.getValue());
    	   	
    	UIParameters.settxtVehicleOccupancyValue(Double.parseDouble(txtVehicleOccupancy.getText()));
    	UIParameters.settxtTimeValueValue(Double.parseDouble(txtTimeValue.getText()));
    	UIParameters.settxtMaximumDailyRangeValue(Double.parseDouble(txtMaximumDailyRange.getText()));
    	UIParameters.settxtRentalCarCostValue(Double.parseDouble(txtRentalCarCost.getText()));
    	UIParameters.setsliderSOCWhenChargeValue(sliderSOCWhenCharge.getValue());
    }
    
    private void resetfileStateHomeChargingAvailability() {
    	double [] stateHomeChargingAvail = new double[51];
    	try {
			Scanner input = new Scanner (new File(fileStateHomeChargingAvailability.getText()));
			
			for (int i=0; i<51; i++) {
				input.next();
				stateHomeChargingAvail[i] = input.nextDouble();
			}
			
			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
    	UIParameters.setoldsliderNationalHomeChargingAvailabilityValue(
				UIParameters.calculateStateWeightedAverage(stateHomeChargingAvail));
    }
}
