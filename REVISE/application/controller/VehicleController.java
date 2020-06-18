package application.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VehicleController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXSlider sliderBEVStock2020;

    @FXML
    private JFXSlider sliderBEVStock2025;

    @FXML
    private JFXSlider sliderBEVStock2030;

    @FXML
    private JFXSlider sliderBEVStock2035;

    @FXML
    private JFXSlider sliderBEVStock2040;

    @FXML
    private Label labelBEVStockText2020;

    @FXML
    private Label labelBEVStockText2025;

    @FXML
    private Label labelBEVStockText2030;

    @FXML
    private Label labelBEVStockText2035;

    @FXML
    private Label labelBEVStockText2040;

    @FXML
    private Label labelBEVStock2020;

    @FXML
    private Label labelBEVStock2025;

    @FXML
    private Label labelBEVStock2030;

    @FXML
    private Label labelBEVStock2035;

    @FXML
    private Label labelBEVStock2040;

    @FXML
    private JFXTextField fileBEVStockState;

    @FXML
    private JFXButton chooseFileBEVStockState;

    @FXML
    private JFXRadioButton radioBEVStockNational;

    @FXML
    private ToggleGroup toggleBEVStock;

    @FXML
    private JFXRadioButton radioBEVStockState;

    @FXML
    private JFXTextField txtBEVFuelConsumption;

    @FXML
    private JFXTextField txtRentalCarFuelEconomy;
    
    @FXML
    private JFXTextField txtBEVRange;
    
    @FXML
    private ImageView imageBack = new ImageView();
    
    @FXML
    void initialize() {
    	
    	readParameters();
    	stateOnToggleBEVStock();
    	
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	
    	//Setting tooltip
    	btnReturn.setTooltip(new Tooltip("Return to main page"));
    	sliderBEVStock2020.setTooltip(
    			new Tooltip(
    					"Change 2020 BEV stock (%)\n"
    					+ "at national scale."));
    	sliderBEVStock2025.setTooltip(
    			new Tooltip(
    					"Change 2025 BEV stock (%)\n"
    					+ "at national scale."));
    	sliderBEVStock2030.setTooltip(
    			new Tooltip(
    					"Change 2030 BEV stock (%)\n"
    					+ "at national scale."));
    	sliderBEVStock2035.setTooltip(
    			new Tooltip(
    					"Change 2035 BEV stock (%)\n"
    					+ "at national scale."));
    	sliderBEVStock2040.setTooltip(
    			new Tooltip(
    					"Change 2040 BEV stock (%)\n"
    					+ "at national scale."));
    	fileBEVStockState.setTooltip(
    			new Tooltip(
    					"Current file address for state\n"
    					+ "BEV stock in all years."
    					));
    	chooseFileBEVStockState.setTooltip(
    			new Tooltip(
    					"Import file"
    					));
    	txtBEVRange.setTooltip(
    			new Tooltip(
    					"Design BEV range (miles);\n"
    					+ "maximum BEV range for all BEVs;\n"
    					+ "positive real value"
    					));
    	txtBEVFuelConsumption.setTooltip(
    			new Tooltip(
    					"Design BEV electricity consumption (Wh/mile);\n"
    					+ "positive real value"
    					));
    	txtRentalCarFuelEconomy.setTooltip(
    			new Tooltip(
    					"Design alternative rental car \n"
    					+ "gasoline consumption (Wh/mile);\n"
    					+ "positive real value"
    					));
    	
    	//end setting tooltip
    	
    	sliderBEVStock2020.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelBEVStock2020.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderBEVStock2025.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelBEVStock2025.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderBEVStock2030.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelBEVStock2030.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderBEVStock2035.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelBEVStock2035.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	sliderBEVStock2040.valueProperty().addListener(
    			new ChangeListener<Number>() {
    				public void changed(ObservableValue <? extends Number>  
                    observable, Number oldValue, Number newValue) 
    					{    						
    						labelBEVStock2040.setText(String.format("%.1f", newValue) +"%"); 
    					} 
    			});
    	
    	
    	btnReturn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			btnReturn.getScene().getWindow().hide();
    	    	   
    	    	Stage newStage = new Stage();
    	    		
    			try {
    				saveParameters();
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
    	
    	//radioBEVStockNational.setUserData("National");
    	//radioBEVStockState.setUserData("State");
    	
    	toggleBEVStock.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
    	    public void changed(ObservableValue<? extends Toggle> ov,
    	            Toggle old_toggle, Toggle new_toggle) {
    	    	if (toggleBEVStock.getSelectedToggle() != null) {
    	    		stateOnToggleBEVStock(); 
    	    	}
    	    }});
    
    	FileChooser fileChooser = new FileChooser();
    	
    	chooseFileBEVStockState.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(null);
                        if (file != null) {
                        	fileBEVStockState.setText(file.getAbsolutePath());
                        	resetfileBEVStockState();
                        	sliderBEVStock2020.setValue(UIParameters.getsliderBEVStock2020Value());
                    		sliderBEVStock2025.setValue(UIParameters.getsliderBEVStock2025Value());
                    		sliderBEVStock2030.setValue(UIParameters.getsliderBEVStock2030Value());
                    		sliderBEVStock2035.setValue(UIParameters.getsliderBEVStock2035Value());
                    		sliderBEVStock2040.setValue(UIParameters.getsliderBEVStock2040Value());
                    		
                    		labelBEVStock2020.setText(String.format("%.1f", sliderBEVStock2020.getValue()) +"%");
                    		labelBEVStock2025.setText(String.format("%.1f", sliderBEVStock2025.getValue()) +"%");
                    		labelBEVStock2030.setText(String.format("%.1f", sliderBEVStock2030.getValue()) +"%");
                    		labelBEVStock2035.setText(String.format("%.1f", sliderBEVStock2035.getValue()) +"%");
                    		labelBEVStock2040.setText(String.format("%.1f", sliderBEVStock2040.getValue()) +"%");
                        }
                    }
                });
    }
    
    //change active national or state
    private void stateOnToggleBEVStock() {
    	boolean isNationalSelected = radioBEVStockNational.isSelected();
    	
    	//enable or disable national
		labelBEVStockText2020.setDisable(!isNationalSelected);
		labelBEVStockText2025.setDisable(!isNationalSelected);
		labelBEVStockText2030.setDisable(!isNationalSelected);
		labelBEVStockText2035.setDisable(!isNationalSelected);
		labelBEVStockText2040.setDisable(!isNationalSelected);
		
		labelBEVStockText2020.setDisable(!isNationalSelected);
		labelBEVStockText2025.setDisable(!isNationalSelected);
		labelBEVStockText2030.setDisable(!isNationalSelected);
		labelBEVStockText2035.setDisable(!isNationalSelected);
		labelBEVStockText2040.setDisable(!isNationalSelected);
		
		labelBEVStock2020.setDisable(!isNationalSelected);
		labelBEVStock2025.setDisable(!isNationalSelected);
		labelBEVStock2030.setDisable(!isNationalSelected);
		labelBEVStock2035.setDisable(!isNationalSelected);
		labelBEVStock2040.setDisable(!isNationalSelected);
		
		sliderBEVStock2020.setDisable(!isNationalSelected);
		sliderBEVStock2025.setDisable(!isNationalSelected);
		sliderBEVStock2030.setDisable(!isNationalSelected);
		sliderBEVStock2035.setDisable(!isNationalSelected);
		sliderBEVStock2040.setDisable(!isNationalSelected);
		
		//disable or enable national
		fileBEVStockState.setDisable(isNationalSelected);
		chooseFileBEVStockState.setDisable(isNationalSelected);
    }
    
    
    private void readParameters() {
		
		radioBEVStockNational.setSelected(UIParameters.getradioBEVStockNationalSelected());
		sliderBEVStock2020.setValue(UIParameters.getsliderBEVStock2020Value());
		sliderBEVStock2025.setValue(UIParameters.getsliderBEVStock2025Value());
		sliderBEVStock2030.setValue(UIParameters.getsliderBEVStock2030Value());
		sliderBEVStock2035.setValue(UIParameters.getsliderBEVStock2035Value());
		sliderBEVStock2040.setValue(UIParameters.getsliderBEVStock2040Value());
		
		fileBEVStockState.setText(UIParameters.getfileBEVStockStateValue());
		
		txtBEVRange.setText(Double.toString(UIParameters.gettxtBEVRangeValue()));
		txtBEVFuelConsumption.setText(Double.toString(UIParameters.gettxtBEVFuelConsumptionValue()));
		txtRentalCarFuelEconomy.setText(Double.toString(UIParameters.gettxtRentalCarFuelEconomyValue()));
		
		labelBEVStock2020.setText(String.format("%.1f", sliderBEVStock2020.getValue()) +"%");
		labelBEVStock2025.setText(String.format("%.1f", sliderBEVStock2025.getValue()) +"%");
		labelBEVStock2030.setText(String.format("%.1f", sliderBEVStock2030.getValue()) +"%");
		labelBEVStock2035.setText(String.format("%.1f", sliderBEVStock2035.getValue()) +"%");
		labelBEVStock2040.setText(String.format("%.1f", sliderBEVStock2040.getValue()) +"%");
	}
	
	private void saveParameters() {
		UIParameters.setradioBEVStockNationalSelected(radioBEVStockNational.isSelected());
		UIParameters.setsliderBEVStock2020Value(sliderBEVStock2020.getValue());
		UIParameters.setsliderBEVStock2025Value(sliderBEVStock2025.getValue());
		UIParameters.setsliderBEVStock2030Value(sliderBEVStock2030.getValue());
		UIParameters.setsliderBEVStock2035Value(sliderBEVStock2035.getValue());
		UIParameters.setsliderBEVStock2040Value(sliderBEVStock2040.getValue());
		
		UIParameters.setfileBEVStockStateValue(fileBEVStockState.getText());
		UIParameters.settxtBEVRangeValue(Double.parseDouble(txtBEVRange.getText()));
		UIParameters.settxtBEVFuelConsumptionValue(Double.parseDouble(txtBEVFuelConsumption.getText()));
		UIParameters.settxtRentalCarFuelEconomyValue(Double.parseDouble(txtRentalCarFuelEconomy.getText()));
	}
	
	private void resetfileBEVStockState() {
		double [][] statePenetration = new double[5][51];
		;
		try {
			Scanner input = new Scanner (new File(fileBEVStockState.getText()));
			for (int j=0; j<51; j++) {
				input.next();
				for (int i=0; i<5; i++) {
					statePenetration[i][j] = input.nextDouble();
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UIParameters.setoldsliderBEVStock2020Value(
				UIParameters.calculateStateWeightedAverage(statePenetration[0]));
		UIParameters.setoldsliderBEVStock2025Value(
				UIParameters.calculateStateWeightedAverage(statePenetration[1]));
		UIParameters.setoldsliderBEVStock2030Value(
				UIParameters.calculateStateWeightedAverage(statePenetration[2]));
		UIParameters.setoldsliderBEVStock2035Value(
				UIParameters.calculateStateWeightedAverage(statePenetration[3]));
		UIParameters.setoldsliderBEVStock2040Value(
				UIParameters.calculateStateWeightedAverage(statePenetration[4]));	
		
	}
}

	
