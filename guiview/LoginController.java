package guiview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * LoginController class
 */
public class LoginController implements Initializable {
	
	@FXML private TextField userName;
	
	@FXML
	private void Login() {
		Parent root;
		if(userName.getText().equals("admin")) {
			try {
				root = FXMLLoader.load(getClass().getResource("AdminPanel.fxml"));
				Stage stage = new Stage();
				stage.setTitle("Admin");
				stage.setScene(new Scene(root, 550, 550));
				stage.show();
				
				userName.getScene().getWindow().hide();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else if(GuiView.c.userLogin(userName.getText())) {
			try {
				root = FXMLLoader.load(getClass().getResource("MainPanel.fxml"));
				Stage stage = new Stage();
				stage.setTitle("Browse");
				stage.setScene(new Scene(root, 800, 600));
				stage.show();
				
				userName.getScene().getWindow().hide();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Invalid username");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
