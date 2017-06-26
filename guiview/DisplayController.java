package guiview;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Album;
import model.Photo;
import model.Tag;

/**
 * DisplayController class
 */
public class DisplayController implements Initializable {
	
	Album album;
	Photo photo;
	
	ObservableList<Tag> observableTagList;
	
	@FXML ImageView display;
	
	@FXML ListView<Tag> tagList;
	
	@FXML Label labelName; 
	@FXML Label labelDate;
	@FXML Label labelCaption;
	
	@FXML
	public void nextButton() {
		int i = album.getList().indexOf(photo);
		if(i < album.getList().size()-1) {
			photo = album.getList().get(i+1);
		}
		updateDisplay(photo);
		observableTagList.clear();
		updateTagList();
	}
	
	@FXML
	public void previousButton() {
		int i = album.getList().indexOf(photo);
		if(i > 0) {
			photo = album.getList().get(i-1);
		}
		updateDisplay(photo);
		observableTagList.clear();
		updateTagList();
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	private void updateDisplay(Photo p) {
		display.setImage(new Image(p.getPath()));
		labelName.setText(p.getName());
		labelDate.setText(p.getDateToString());
		labelCaption.setText(p.getCaption());
	}
	
	private void updateTagList() {
		observableTagList.addAll(photo.getTags());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setAlbum(MainPanelController.currentAlbum);
		setPhoto(MainPanelController.currentPhoto);
		observableTagList = tagList.getItems();

		
		updateDisplay(photo);
		updateTagList();
	}
	

}
