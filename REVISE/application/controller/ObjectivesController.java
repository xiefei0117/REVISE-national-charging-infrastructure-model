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
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ObjectivesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXTextField txtWeightObj1;

    @FXML
    private JFXTextField txtWeightObj2;
    
    @FXML
    private ImageView imageBack = new ImageView();
    
    @FXML
    private ImageView imageObjectives = new ImageView();

    @FXML
    void initialize() {
    	readParameters();
    	
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	imageObjectives.setImage(new Image("/application/asset/objectives.png"));
    	
    	//Setting tooltip
    	btnReturn.setTooltip(new Tooltip("Return to main page"));
    	txtWeightObj1.setTooltip(
    			new Tooltip(
    					"Weighting factor for objective 1 - \n"
    					+ "infrastructure cost."));
    	txtWeightObj2.setTooltip(
    			new Tooltip(
    					"Weighting factor for objective 2 - \n"
    					+ "Traveler cost."));
    	//end setting tooltip
    	
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
    
    private void readParameters() {
    	txtWeightObj1.setText(String.format("%.2f", UIParameters.gettxtWeightObj1Value()));
    	txtWeightObj2.setText(String.format("%.2f", UIParameters.gettxtWeightObj2Value()));
    }
    
    private void saveParameters() {
    	UIParameters.settxtWeightObj1Value(Double.parseDouble(txtWeightObj1.getText()));
    	UIParameters.settxtWeightObj2Value(Double.parseDouble(txtWeightObj2.getText()));
    }
}
