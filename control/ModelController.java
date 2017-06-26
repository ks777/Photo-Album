package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import model.Photo;

/**
 * Interface for controlling a photo viewer model. Handles the control and data manipulation of a model.
 */
public interface ModelController {
	
	/**
	 * List all users in model
	 */
	public void listUsers();
	
	/**
	 * return List of users
	 */
	public ArrayList<model.User> returnUsers();
	
	/**
	 * Add a new user to the user list.
	 */
	public boolean addUser(String u_id, String name) throws IOException, ClassNotFoundException;
	
	/**
	 * Remove a user from the user list.
	 */
	public boolean removeUser(String u_id);
	
	/**
	 * Login as user that matches u_id. Also switches view mode to interactive.
	 */
	public boolean userLogin(String u_id);
	
	/**
	 * List all albums in model
	 * */
	public void listAlbums();
	
	/**
	 * Create a new album.
	 */
	
	/**
	 * return List of albums
	 */
	public ArrayList<model.Album> returnAlbums();
	
	public boolean createAlbum(String albumName);
	
	/**
	 * Delete an album from the album list.
	 */
	public boolean deleteAlbum(String albumName);
	
	/**
	 * Lists all photos in the given album.
	 */
	public void listPhotos(String albumName);
	
	/**
	 * Adds a photo to the given album name with a new file name and caption.
	 */
	public boolean addPhoto(String fileName, String caption, String albumName);
	
	/**
	 * Moves a photo with the given filename from one album to another.
	 */
	public boolean movePhoto(Photo p, String oldAlbumName, String newAlbumName);
	
	/**
	 * Removes a photo from the given album name.
	 */
	public boolean removePhoto(String fileName, String albumName);
	
	/**
	 * Add a tag.
	 */
	public boolean addTag(String fileName, String tagType, String tagValue);
	
	/**
	 * List information about the given photo including file name, album, date, caption, and tags.
	 */
	public boolean listPhotoInfo(String fileName);
	
	/**
	 * Retrieve all photos taken within a given range of dates, in chronological order
	 */
	public void getPhotosByDate(Calendar startDate, Calendar endDate);
	
	/**
	 * Retrieve all photos that have all the given tags, in chronological order.
	 */
	public void getPhotosByTag(String tagType, String tagValue);
	
	/**
	 * Logout of current user and end interactive mode
	 */
	public boolean logout() throws IOException;
	
	/**
	 * Delete a tag.
	 */
	boolean deleteTag(String fileName, String tagType, String tagValue);
	
}