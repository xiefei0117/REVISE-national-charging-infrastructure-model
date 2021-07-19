package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	//private Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//stage = primaryStage;
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/view/MainView.fxml"));
			Scene scene = new Scene(root,700,400);
			
	    	scene.getStylesheets().clear();
	    	scene.getStylesheets().add(getClass().getResource("/application/css/toolTipStyle.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.show();
			/*
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent e) {
			    	Platform.exit();
			    	System.exit(0);
			    }
			});
			*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//public void setStageTitle(String newTitle) {
		//stage.setTitle(newTitle);
	//}
}
