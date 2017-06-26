package guiview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.DateRange;
import model.Tag;


/**
 * Viewerfunctions
 */
public class ViewerFunctions {
	
	static boolean albumEntered = false;
	
	/*
	 * Simple dialog with one TextField for entry
	 * 
	 * Returns an Optional<Object>
	 * Use the Optional<Object>.get() command to return the stored value.
	 */
	
	/**
	 * Simple Diaglog
	 * @param Title
	 * @param Header
	 * @param Label
	 * @param Date (Calendar) of when the photo was generated. 
	 * @return An Optional<Object>
	 */
	public static Optional<Object> openSimpleDialog(String title, String header, String label) {
		
		// Create the custom dialog.
		Dialog<Object> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		
		// Set the button types.
		ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

		//Setting up the layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField field1 = new TextField(); field1.setPromptText(label.toLowerCase());
		
		grid.add(new Label(label), 0, 0); grid.add(field1, 1, 0);
		
		Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
		confirmButton.setDisable(true);

		//Listeners to disable/enable the confirm button
		field1.textProperty().addListener((observable, oldValue, newValue) -> {
			albumEntered = !newValue.trim().isEmpty();
			confirmButton.setDisable(!albumEntered);
		});
		
		dialog.getDialogPane().setContent(grid);
		
		// Convert the result to a Song object
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == confirmButtonType) {
		    	String s = new String(field1.getText());
		        return s;
		    }
		    return null;
		});
		
		return dialog.showAndWait();		
	}
	
	/**
	 * Simple Diaglog
	 */
	public static Optional<Tag> openTagDialog() {
		
		Dialog<Tag> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Add a new tag to this photo");
		dialog.setResizable(true);

		Label label1 = new Label("Tag type: ");
		Label label2 = new Label("Tag value: ");
		TextField text1 = new TextField();
		TextField text2 = new TextField();
				
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(text1, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(text2, 2, 2);
		dialog.getDialogPane().setContent(grid);
				
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		dialog.setResultConverter(new Callback<ButtonType, Tag>() {
		    @Override
		    public Tag call(ButtonType b) {

		        if (b == buttonTypeOk) {

		            return new Tag(text1.getText(), text2.getText());
		        }
		        return null;
		    }
		});	
		return dialog.showAndWait();		
	}
	
	/**
	 * Simple Diaglog
	 */
	public static Optional<DateRange> openDateDialog() {
			
		Dialog<DateRange> dialog = new Dialog<>();
		dialog.setTitle("Enter Date");
		dialog.setHeaderText("Enter a date");
		dialog.setResizable(true);
	
		Label label1 = new Label("Start Date: ");
		Label label2 = new Label("End Date: ");
		TextField text1 = new TextField();
		TextField text2 = new TextField();
		
		text1.setPromptText("Format: 01-Jan-16");
		text2.setPromptText("Format: 01-Jan-16");
				
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(text1, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(text2, 2, 2);
		dialog.getDialogPane().setContent(grid);
				
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
	
		dialog.setResultConverter(new Callback<ButtonType, DateRange>() {
		    @Override
		    public DateRange call(ButtonType b) {
	
		        if (b == buttonTypeOk) {
		        	
		    		String start = text1.getText();
		    		String end = text2.getText();
		    		DateFormat format = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
		    		Date startDate = null;
		    		Date endDate = null;
		    		Calendar sd = Calendar.getInstance();
		    		Calendar ed = Calendar.getInstance();
		    		
		    		if(start != null && end != null) {
		    			try {
		    				startDate = format.parse(start);
		    				endDate = format.parse(end);
		    				sd.set(Calendar.MILLISECOND,0);
		    				ed.set(Calendar.MILLISECOND,0);
		    				sd.setTime(startDate);
		    				ed.setTime(endDate);
		    			} catch (ParseException e) {
		    				e.printStackTrace();
		    			}
		    			
		    		}
	
		            return new DateRange(sd, ed);
		        }
		        return null;
		    }
		});	
		return dialog.showAndWait();		
	}
	
	public static void albumPresentError() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText("Album already exists. Please try again.");
		alert.showAndWait();	
	}

}
