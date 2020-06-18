package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.controller.UIParameters;
import application.model.GeneticAlgorithm.GA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;



public class MainController {

	@FXML
    private JFXButton btnInfrastructureMain;

    @FXML
    private JFXButton btnTravelerMain;

    @FXML
    private JFXButton btnVehicleMain;

    @FXML
    private JFXButton btnHelpMain;

    @FXML
    private JFXButton btnObjectivesMain;

    @FXML
    private JFXButton btnNetworkMain;

    @FXML
    private JFXButton btnGASetupMain;

    @FXML
    private JFXButton btnOutputMain;

    @FXML
    private JFXButton btnRunMain;
    
    @FXML
    private ImageView imageRun = new ImageView();

    @FXML
    private JFXTextField txtScenarioNameMain;
    
    
    //UIParameters parameters = new UIParameters();

    @FXML
    void initialize() {
    	//setStageTitle("REVISE 2.0");

    	imageRun.setImage(new Image("/application/asset/proceedlarge.png"));
    	
    	//Setting tooltip
    	btnInfrastructureMain.setTooltip(
    			new Tooltip(
    					"Station related parameters\n"
    				  + "(e.g., station cost)\n"));
    	btnTravelerMain.setTooltip(
    			new Tooltip(
    					"Traveler related parameters\n"
    				  + "(e.g., home charging availability)\n"));
    	btnObjectivesMain.setTooltip(
    			new Tooltip(
    					"Objective functions for optimization\n"
    				  + "(e.g., weighting factors for objectives)\n"));
    	btnVehicleMain.setTooltip(
    			new Tooltip(
    					"Vehicle related parameters\n"
    				  + "(e.g., BEV market penetration)\n"));
    	btnNetworkMain.setTooltip(
    			new Tooltip(
    					"Network related parameters\n"
    				  + "(e.g., Node, arcs, and paths)\n"));
    	btnOutputMain.setTooltip(
    			new Tooltip(
    					"Model setup on GA and output\n"
    				  + "(e.g., multithread computing)\n"));
    	btnHelpMain.setTooltip(
    			new Tooltip(
    					"For additional information on\n"
    					+ "REVISE...\n"));
    	txtScenarioNameMain.setTooltip(
    			new Tooltip(
    					"Define scenario name\n"));
    	btnRunMain.setTooltip(
    			new Tooltip(
    					"Run scenario\n"));
    	
    	//end setting tooltip
    	
    	if (!UIParameters.getTxtScenarioNameMainValue().equals("")) {
    		txtScenarioNameMain.setText(UIParameters.getTxtScenarioNameMainValue());
    	}
    	
    	
    	btnInfrastructureMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/InfrastructureView.fxml");
    		}
    	});	
    	
    	btnVehicleMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/VehicleView.fxml");
    		}
    	});	
    	
    	btnTravelerMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/TravelerView.fxml");
    		}
    	});	
    	
    	btnObjectivesMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/ObjectivesView.fxml");
    		}
    	});	
    	
    	btnNetworkMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/NetworkView.fxml");
    		}
    	});	
    	
    	btnGASetupMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/GASetupView.fxml");
    		}
    	});	
    	
    	btnOutputMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/OutputView.fxml");
    		}
    	});	
    	
    	btnHelpMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/HelpView.fxml");
    		}
    	});	
    	
    	txtScenarioNameMain.textProperty().addListener((observable, oldValue, newValue) -> {
    		UIParameters.setTxtScenarioNameMainValue(newValue);
    	});
    	
    	
    	btnRunMain.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			accessWindow("/application/view/CalculationView.fxml");
    		}
    	    		
    	});
    	

    }
    
    private void accessWindow(String url) {
    	
    	//get the main window
    	btnRunMain.getScene().getWindow().hide();
    	   
    	Stage newStage = new Stage();
    		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(url));
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
    
}
