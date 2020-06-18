package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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

public class OutputController {

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXToggleButton toggleInfrastructureOutput;

    @FXML
    private JFXToggleButton toggleSummaryOutput;

    @FXML
    private JFXTextField txtTimeLimit;

    @FXML
    private JFXComboBox<String> comboNumThreads;

    @FXML
    private JFXToggleButton toggleTournament;
    
    @FXML
    private JFXTextField txtNumGenes;

    @FXML
    private JFXTextField txtRandomSeed;

    @FXML
    private JFXCheckBox checkRandomSeedChoose;
    
    @FXML
    private ImageView imageBack = new ImageView();

    @FXML
    void initialize() {
    	readParameters();
    	
    	imageBack.setImage(new Image("/application/asset/Left_Arrow.png"));
    	
    	//Setting tooltip
    	btnReturn.setTooltip(new Tooltip("Return to main page"));
    	txtTimeLimit.setTooltip(
    			new Tooltip(
    					"Time limit (secs) for algorithm run;\n"
    					+ "positive real value."
    					));
    	comboNumThreads.setTooltip(
    			new Tooltip(
    					"Number of threads to be used for calculation;\n"
    					+ "solution quality improves with more threads."
    					));
    	txtNumGenes.setTooltip(
    			new Tooltip(
    					"Number of genes are included in the population\n"
    					+ "pool for GA optimization."
    					));
    	txtRandomSeed.setTooltip(
    			new Tooltip(
    					"Mannually set random seed for simulation;\n"
    					+ "integer value."
    					));
    	checkRandomSeedChoose.setTooltip(
    			new Tooltip(
    					"If checked, computer clock time is used as\n"
    					+ "random seed; otherwise, set random seed\n"
    					+ "mannually."
    					));
    	toggleTournament.setTooltip(
    			new Tooltip(
    					"If true, allow tournament between candidate\n"
    					+ "genes; gene with the best fitness is allowed\n"
    					+ "to join the population pool."
    					));
    	toggleInfrastructureOutput.setTooltip(
    			new Tooltip(
    					"If true, output infrastructure results\n"
    					+ "(i.e., when and where stations are opened\n"
    					+ "and station capacity)."
    					));
    	toggleSummaryOutput.setTooltip(
    			new Tooltip(
    					"If true, output summary results\n"
    					+ "(i.e., total cost, VMT, and energy)."
    					));
    	
    	//end setting tooltip
    	
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
    	
    	checkRandomSeedChoose.selectedProperty().addListener(new ChangeListener<Boolean>() {
    		@Override
    	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    	        if (newValue) {
    	        	txtRandomSeed.setDisable(true);
    	    		txtRandomSeed.setText("");
    	        }
    	        else {
    	        	txtRandomSeed.setDisable(false);
    	        	txtRandomSeed.setText("2020");
    	        }
    	    }
    	});
    	
    	
    }
    
    private void readParameters() {
    	txtTimeLimit.setText(String.format("%.0f",UIParameters.gettxtTimeLimitValue()));
    	
    	int[] range = IntStream.rangeClosed(1, UIParameters.getprocessors()).toArray();
    	String strRange[] = Arrays.stream(range).mapToObj(String::valueOf).toArray(String[]::new);
    	comboNumThreads.setItems(FXCollections.observableArrayList(strRange));
    	comboNumThreads.setValue(String.valueOf(UIParameters.getprocessorsValue()));
    	
    	toggleTournament.setSelected(UIParameters.gettoggleTournamentSelected());
    	toggleInfrastructureOutput.setSelected(UIParameters.gettoggleInfrastructureOutputSelected());
    	toggleSummaryOutput.setSelected(UIParameters.gettoggleSummaryOutputSelected());
    	
    	txtNumGenes.setText(Integer.toString(UIParameters.gettxtNumGenesValue()));
    	checkRandomSeedChoose.setSelected(UIParameters.getcheckRandomSeedChooseValue());
    	if(checkRandomSeedChoose.isSelected()) {
    		txtRandomSeed.setDisable(true);
    		txtRandomSeed.setText("");
    	}
    	else {
    		txtRandomSeed.setDisable(false);
    		txtRandomSeed.setText(Long.toString(UIParameters.gettxtRandomSeedValue()));
    	}
	}
    
    private void saveParameters() {
    	UIParameters.settxtTimeLimitValue(Double.parseDouble(txtTimeLimit.getText()));
    	UIParameters.setprocessorsValue(Integer.parseInt(comboNumThreads.getValue()));
    	
    	UIParameters.settoggleTournamentSelected(toggleTournament.isSelected());
    	UIParameters.settoggleInfrastructureOutputSelected(toggleInfrastructureOutput.isSelected());
    	UIParameters.settoggleSummaryOutputSelected(toggleSummaryOutput.isSelected());
    	
    	UIParameters.settxtNumGenesValue(Integer.parseInt(txtNumGenes.getText()));
    	UIParameters.setcheckRandomSeedChooseValue(checkRandomSeedChoose.isSelected());
    	if(checkRandomSeedChoose.isSelected()) {
    		UIParameters.settxtRandomSeedValue(System.currentTimeMillis());
    	}
    	else {
    		UIParameters.settxtRandomSeedValue(Long.parseLong(txtRandomSeed.getText()));
    	}
    }
}
