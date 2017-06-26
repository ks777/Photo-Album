package guiview;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;


/**
 * MainPanel Controller class
 */
public class MainPanelController implements Initializable {
	
	boolean albumEntered = false;
	
	//ObservableLists for each ListView
	ObservableList<Album> observableAlbumList;
	ObservableList<Photo> observablePhotoList;
	ObservableList<Tag> observableTagList;
	
	//ListViews
	@FXML ListView<Album> albumList;
	@FXML ListView<Photo> photoList;
	@FXML ListView<Tag> tagList;
 	
	@FXML ImageView thumbnail;
	
	@FXML Label labelName; 
	@FXML Label labelDate;
	@FXML Label labelCaption;
	
	public static Album currentAlbum;
	public static Photo currentPhoto;

	@FXML //Create an album
	public void createAlbum() {

		boolean exists = false;
	
		Optional<Object> result = ViewerFunctions.openSimpleDialog("Create Album", "Create an album", "Album Name");
		if(result.isPresent()) {
			//Check if album is in the list
			for (Album a: observableAlbumList)
			{
				if(a.getName().equals((String)result.get())) {
					ViewerFunctions.albumPresentError();
					exists = true;
				}
			}
			//If album doesn't already exist
			if(!exists) {
				Album b = new Album((String)result.get());
				GuiView.c.createAlbum(b.getName());
				observableAlbumList.add(b);
				albumList.getSelectionModel().select(b);
			}
		}
	}
	
	@FXML
	private void openSearch() {
		Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("SearchPanel.fxml"));
				Stage stage = new Stage();
				stage.setTitle("Search");
				stage.setScene(new Scene(root, 528, 600));
				stage.showAndWait();
				observableAlbumList.clear();
				updateAlbumList();
			}catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@FXML //Logout and save
	private void logOut() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Login");
			stage.setScene(new Scene(root, 300, 150));
			stage.show();
			GuiView.c.model.writeUser(GuiView.c.model.active);
			albumList.getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	private void updateAlbumList() {
		observableAlbumList.addAll(GuiView.c.model.active.getAlbums());
	}
	
	private void updatePhotoList() {
		if(!observableAlbumList.isEmpty()) {
			observablePhotoList.addAll(GuiView.c.model.active.getAlbum(albumList.getSelectionModel().getSelectedItem().getName()).getList());
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
		observableAlbumList = albumList.getItems();
		observablePhotoList = photoList.getItems();
		observableTagList = tagList.getItems();
		updateAlbumList();
		
		if(!observableAlbumList.isEmpty()) {
			albumList.getSelectionModel().select(0);
			currentAlbum = albumList.getSelectionModel().getSelectedItem();
			updatePhotoList();
			if(!observablePhotoList.isEmpty()) {
				photoList.getSelectionModel().select(0);
				updateThumbnail(photoList.getSelectionModel().getSelectedItem());
			}
		}
		
		//Cell factory for the album list
		albumList.setCellFactory(lv -> {

            ListCell<Album> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteAlbum = new MenuItem();
            deleteAlbum.textProperty().bind(Bindings.format(" Delete \"%s\" ", cell.itemProperty()));
            deleteAlbum.setOnAction(event -> {
		        GuiView.c.deleteAlbum(albumList.getSelectionModel().getSelectedItem().getName());
		        observableAlbumList.clear();
		        updateAlbumList();
		        albumList.getSelectionModel().select(0);
		        observablePhotoList.clear();
		        if(!observableAlbumList.isEmpty()) {
		        	updatePhotoList();
		        }  
            });
            
            MenuItem renameAlbum = new MenuItem();
            renameAlbum.textProperty().bind(Bindings.format(" Rename \"%s\" ", cell.itemProperty()));
            renameAlbum.setOnAction(event -> {
		    	Optional<Object> ret = ViewerFunctions.openSimpleDialog("Rename Album", "Rename this album", "Album Name");
		    	if(ret.isPresent()) {
			    	albumList.getSelectionModel().getSelectedItem().editName((String)ret.get());
			    	albumList.refresh();
		    	}
            });
            
            MenuItem addPhotoToAlbum = new MenuItem();
            addPhotoToAlbum.textProperty().bind(Bindings.format(" Add Photo to \"%s\" ", cell.itemProperty()));
            addPhotoToAlbum.setOnAction(event -> {
		    	Stage stage = new Stage();
		    	Album a = albumList.getSelectionModel().getSelectedItem();
		    	FileChooser fileChooser = new FileChooser();
		    	fileChooser.setTitle("Add Photo");
		    	File f = fileChooser.showOpenDialog(stage);
		    	
		    	if(f != null) {
			    	Optional<Object> caption = ViewerFunctions.openSimpleDialog("Caption", "Enter a caption", "Caption");
			    	
			    	Photo p = new Photo(f.getName(), f.toURI().toString(), (String)caption.get());
					if(GuiView.c.addPhoto(f.getAbsolutePath(), p.getCaption(), a.getName())) {
						observablePhotoList.clear();
						updatePhotoList();
						photoList.getSelectionModel().select(p);
						try {
							GuiView.c.model.writeUser(GuiView.c.model.active);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		    	}
            });
            contextMenu.getItems().addAll(deleteAlbum, renameAlbum, addPhotoToAlbum);
          
            StringBinding sb = cell.itemProperty().asString();

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                    cell.textProperty().unbind();
                    cell.textProperty().set("");
                } else {
                    cell.setContextMenu(contextMenu);
                    cell.textProperty().bind(sb);
                }
            });
            return cell ;
        });
		
		//Photo cell factory
		photoList.setCellFactory(lv -> {

            ListCell<Photo> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deletePhoto = new MenuItem();
            deletePhoto.textProperty().bind(Bindings.format(" Delete \"%s\" ", cell.itemProperty()));
            deletePhoto.setOnAction(event -> {
		    	Photo p = photoList.getSelectionModel().getSelectedItem();
		    	Album a = albumList.getSelectionModel().getSelectedItem();
		    	a.deletePhoto(p.getName());
		    	observablePhotoList.remove(p);
		    	observablePhotoList.clear();
		    	updatePhotoList();
		    	photoList.refresh();
            });
            
            MenuItem editCaptionPhoto = new MenuItem();
            editCaptionPhoto.textProperty().bind(Bindings.format(" Edit Caption For \"%s\" ", cell.itemProperty()));
            editCaptionPhoto.setOnAction(event -> {
		    	Optional<Object> ret = ViewerFunctions.openSimpleDialog("Edit Caption", "Enter a caption", "Caption");
		    	if(ret.isPresent()) {
			    	Photo p = photoList.getSelectionModel().getSelectedItem();
			    	p.editCaption((String) ret.get());
			    	updateThumbnail(p);
		    	}
            });
            
            MenuItem addTagToPhoto = new MenuItem();
            addTagToPhoto.textProperty().bind(Bindings.format(" Add Tag To \"%s\" ", cell.itemProperty()));
            addTagToPhoto.setOnAction(event -> {
		    	Optional<Tag> ret = ViewerFunctions.openTagDialog();
		    	if(ret.isPresent()) {
			    	Photo p = photoList.getSelectionModel().getSelectedItem();
			    	p.addTag(ret.get());
			    	observableTagList.clear();
			    	updateTagList();
		    	}
            });
            
            MenuItem movePhoto = new MenuItem();
            movePhoto.textProperty().bind(Bindings.format(" Move \"%s\" ", cell.itemProperty()));
            movePhoto.setOnAction(event -> {
		    	Optional<Object> ret = ViewerFunctions.openSimpleDialog("Move Photo", "Enter the album to move the photo to", "New Album");
		    	if(ret.isPresent()) {
			    	String newAlbumName = (String)ret.get();
			    	Album newAlbum = GuiView.c.model.active.getAlbum(newAlbumName);
			    	Photo p = photoList.getSelectionModel().getSelectedItem();
			    	if(newAlbum != null) {
			    		if(GuiView.c.movePhoto(p, albumList.getSelectionModel().getSelectedItem().getName(), newAlbumName)) {
			    			albumList.getSelectionModel().getSelectedItem().deletePhoto(p.getName());
			    			updatePhotoList();
			    			albumList.getSelectionModel().select(newAlbum);
							photoList.getSelectionModel().select(p);
			    		}
			    	}
		    	}
            });
            
            MenuItem displayPhoto = new MenuItem();
            displayPhoto.textProperty().bind(Bindings.format(" Display \"%s\" ", cell.itemProperty()));
            displayPhoto.setOnAction(event -> {
            	Parent root;
        		try {
        			root = FXMLLoader.load(getClass().getResource("DisplayPanel.fxml"));
        			Stage stage = new Stage();
        			stage.setTitle("Display");
        			stage.setScene(new Scene(root, 800, 600));
        			stage.showAndWait();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            });
            
            contextMenu.getItems().addAll(deletePhoto, editCaptionPhoto, addTagToPhoto, movePhoto, displayPhoto);
          
            StringBinding sb = cell.itemProperty().asString();

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                    cell.textProperty().unbind();
                    cell.textProperty().set("");
                } else {
                    cell.setContextMenu(contextMenu);
                    cell.textProperty().bind(sb);
                }
            });
            return cell ;
        });
		
		//taglist cell factory
		tagList.setCellFactory(lv -> {

            ListCell<Tag> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteTag = new MenuItem();
            deleteTag.textProperty().bind(Bindings.format(" Delete \"%s\" ", cell.itemProperty()));
            deleteTag.setOnAction(event -> {
		    	Tag t = tagList.getSelectionModel().getSelectedItem();
		    	Photo p = photoList.getSelectionModel().getSelectedItem();
		    	p.deleteTag(t);
		    	observableTagList.clear();
		    	updateTagList();
            });
            
           
            contextMenu.getItems().addAll(deleteTag);
          
            StringBinding sb = cell.itemProperty().asString();
            
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                    cell.textProperty().unbind();
                    cell.textProperty().set("");
                } else {
                    cell.setContextMenu(contextMenu);
                    cell.textProperty().bind(sb);
                }
            });
            return cell ;
        });
		
		
		albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	observablePhotoList.clear();
	        	updatePhotoList();
		        if(!observablePhotoList.isEmpty()) {
		        	photoList.getSelectionModel().select(0);
		        	observableTagList.clear();
		    		updateThumbnail(photoList.getSelectionModel().getSelectedItem());
		    		updateTagList();
		    		currentAlbum = albumList.getSelectionModel().getSelectedItem();
		        }
	        }
	    });
		
		photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	Photo p = photoList.getSelectionModel().getSelectedItem();
		    	if(p != null) {
		    		observableTagList.clear();
		    		updateThumbnail(p);
		    		updateTagList();
		    		currentPhoto = photoList.getSelectionModel().getSelectedItem();
		    	}
	        }
	    });	
	}
}
