package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GASetupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXTextField txtTimeLimit;

    @FXML
    private JFXComboBox<?> comboNumThreads;

    @FXML
    private JFXToggleButton toggleTournament;
    
    @FXML
    private JFXTextField txtRandomSeed;

    @FXML
    private JFXCheckBox checkRandomSeedChoose;

    @FXML
    void initialize() {
    	btnReturn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
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
}
