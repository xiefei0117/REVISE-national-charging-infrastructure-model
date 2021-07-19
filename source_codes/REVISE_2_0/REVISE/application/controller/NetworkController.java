package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class NetworkController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXTextField txtNumNodes;

    @FXML
    private JFXTextField txtNumODPairs;

    @FXML
    private JFXTextField txtNumPaths;

    @FXML
    private JFXTextField txtStages;

    @FXML
    private JFXTextField fileODpair;

    @FXML
    private JFXButton openFileODpair;

    @FXML
    private JFXTextField fileDistance;

    @FXML
    private JFXButton openFileDistance;

    @FXML
    private JFXTextField filePathNodes;

    @FXML
    private JFXButton openFilePathNodes;
    
    @FXML
    private ImageView imageBack = new ImageView();

    @FXML
    void initialize() {
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	
    	fileODpair.setText(UIParameters.getfileODpairValue());
    	fileDistance.setText(UIParameters.getfileDistanceValue());
    	filePathNodes.setText(UIParameters.getfilePathNodesValue());
    	
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
    	
    	openFileODpair.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			File file = new File(fileODpair.getText());
    			try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
    		}
    	});	
    	
    	openFileDistance.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			File file = new File(fileDistance.getText());
    			try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
    		}
    	});	
    	
    	openFilePathNodes.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			File file = new File(filePathNodes.getText());
    			try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
    		}
    	});	
    }
}
