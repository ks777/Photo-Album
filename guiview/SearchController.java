package guiview;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Album;
import model.DateRange;
import model.Photo;
import model.Tag;

/**
 * Search Controller class
 */
public class SearchController implements Initializable {
	
	ObservableList<Photo> observablePhotoList;
	ObservableList<Tag> observableTagList;
	
	@FXML TextField startDateField;
	@FXML TextField endDateField;
	
	@FXML TextField tagField;
	@FXML TextField valueField;
	
	@FXML ListView<Photo> photoList;
	@FXML ListView<Tag> tagList;
	
	@FXML ImageView thumbnail;
	
	@FXML Label labelName; 
	@FXML Label labelDate;
	@FXML Label labelCaption;
	
	@FXML
	private void dateSearch() {
		Optional<DateRange> dr = ViewerFunctions.openDateDialog();
		DateRange d = dr.get();
		Calendar s = d.getStartDate();
		Calendar e = d.getEndDate();
		ArrayList<Album> all = GuiView.c.model.active.getAlbums();
		for(int i = 0; i < all.size(); i++) {
			ArrayList<Photo> p = all.get(i).getList();
			for(int j = 0; j < p.size(); j++) {
				Photo curr = p.get(j);
				if((curr.getCalendar().after(s) && curr.getCalendar().before(e)) || ((curr.getCalendar().equals(s)) || curr.getCalendar().equals(e))) {
					observablePhotoList.add(curr);
				}
			}
		}
	}
	
	@FXML
	private void tagSearch() {
		Optional<Tag> t = ViewerFunctions.openTagDialog();
		Tag tag = t.get();
		
		ArrayList<Album> all = GuiView.c.model.active.getAlbums();
		for(int i = 0; i < all.size(); i++) {
			ArrayList<Photo> p = all.get(i).getList();
			for(int j = 0; j < p.size(); j++) {
				Photo curr = p.get(j);
				if(curr.hasTag(tag)) {
					observablePhotoList.add(curr);
				}
			}
		}
	}
	
	@FXML
	private void createAlbumFromResults() {
		Optional<Object> ret = ViewerFunctions.openSimpleDialog("Album Name", "Name of New Album", "Album Name");
		GuiView.c.model.active.addAlbum((String)ret.get());
		Album a = GuiView.c.model.active.getAlbum((String) ret.get());
		
		for(int i = 0; i < observablePhotoList.size(); i++) {
			a.addPhoto(observablePhotoList.get(i));
		}
		
		try {
			GuiView.c.model.writeUser(GuiView.c.model.active);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateTagList() {
		observableTagList.addAll(photoList.getSelectionModel().getSelectedItem().getTags());
	}
	
	private void updateThumbnail(Photo p) {
		thumbnail.setImage(new Image(p.getPath()));
		labelName.setText(p.getName());
		labelDate.setText(p.getDateToString());
		labelCaption.setText(p.getCaption());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		observablePhotoList = photoList.getItems();
		observableTagList = tagList.getItems();

		photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	Photo p = photoList.getSelectionModel().getSelectedItem();
		    	if(p != null) {
		    		observableTagList.clear();
		    		updateThumbnail(p);
		    		updateTagList();
		    	}
	        }
	    });	
		
	}

}
