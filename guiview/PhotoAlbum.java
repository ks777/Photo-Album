package guiview;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main PhotoAlbum class
 */
public class PhotoAlbum extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		
		primaryStage.setTitle("Song Library");
		primaryStage.setScene(new Scene(root, 300, 150));
		primaryStage.show();
	}
	
	@Override
	public void stop() {
	    try {
	    	for(int i = 0; i < GuiView.c.model.all.size(); i++) {
	    		GuiView.c.model.writeUser(GuiView.c.model.all.get(i));
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
