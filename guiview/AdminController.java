package guiview;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;

/**
 * AdminController class
 */
public class AdminController implements Initializable {
	
	ObservableList<User> observableUserList;
	
	boolean idEntered = false;
	boolean nameEntered = false;
	
	@FXML
	ListView<User> userList;
	
	@FXML
	private void createUser() {
		
		boolean exists = false;
		
		// Create the custom dialog.
		Dialog<User> dialog = new Dialog<>();
		dialog.setTitle("Create User");
		dialog.setHeaderText("Create a new user.");
		
		// Set the button types.
		ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

		//Setting up the layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField u_id = new TextField(); u_id.setPromptText("username");
		TextField name = new TextField(); name.setPromptText("full Name");
		
		grid.add(new Label("Username"), 0, 0); grid.add(u_id, 1, 0);
		grid.add(new Label("Full name"), 0, 1); grid.add(name, 1, 1);
		
		Node loginButton = dialog.getDialogPane().lookupButton(confirmButtonType);
		loginButton.setDisable(true);

		//Listeners to disable/enable the confirm button
		u_id.textProperty().addListener((observable, oldValue, newValue) -> {
			idEntered = !newValue.trim().isEmpty();
			loginButton.setDisable(!(idEntered && nameEntered));
		});
		
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			nameEntered = !newValue.trim().isEmpty();
			loginButton.setDisable(!(idEntered && nameEntered));
		});
		
		dialog.getDialogPane().setContent(grid);
		
		// Convert the result to a Song object
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == confirmButtonType) {
		    	User u = new User(u_id.getText(), name.getText());
		    	try {
					GuiView.c.addUser(u.getID(), u.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
		        return u;
		    }
		    return null;
		});

		Optional<User> result = dialog.showAndWait();
		if(result.isPresent()) {
			

			for (User u: observableUserList)
			{
				if(u.getID().equals(result.get().getID())) {
					userPresentError();
					exists = true;
				}
			}
			
			if(!exists) {
				observableUserList.add(result.get());
				userList.getSelectionModel().select(result.get());
			}
		}
	}
	
	@FXML
	private void deleteUser() {
		if(!observableUserList.isEmpty()) {
			if(GuiView.c.removeUser(userList.getSelectionModel().getSelectedItem().getID())) {
				userList.getItems().remove(userList.getSelectionModel().getSelectedItem());
				userList.getSelectionModel().selectNext();
				if(userList.getSelectionModel().isEmpty())
					userList.getSelectionModel().selectLast();
			}
		}
	}
	
	@FXML
	private void logOut() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Login");
			stage.setScene(new Scene(root, 300, 150));
			stage.show();
			userList.getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void userPresentError() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText("User already exists. Please try again.");
		alert.showAndWait();	
	}
	
	private void updateList() {
		observableUserList.addAll(GuiView.c.returnUsers());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		observableUserList = userList.getItems();
		updateList();
	}

}
