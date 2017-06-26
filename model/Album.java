package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/*
 * Album object class
 */

public class Album implements Serializable  {


	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Photo> photoList;
	private int numberOfPhotos;
	private Calendar startDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
	
	/**
	 * Album constructor
	 * @param Name (string) of the album
	 * @param PhotoList (ArrayList) with all photos in album
	 */
	public Album(String Name, ArrayList<Photo> PhotoList) {
		name = Name;
		photoList = PhotoList;
		numberOfPhotos = 0;
		startDate.set(Calendar.MILLISECOND,0);
		endDate.set(Calendar.MILLISECOND,0);
	}
	
	public Album(String Name) {
		name = Name;
		photoList = new ArrayList<Photo>();
		startDate.set(Calendar.MILLISECOND,0);
		endDate.set(Calendar.MILLISECOND,0);
	}
	
	/**
	 * Get name of album
	 * return Name (String) of album
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get size of album
	 * return Size (int) of album
	 */
	public int getAlbumSize() {
		return numberOfPhotos;
	}
	
	/**
	 * Get list of photos
	 * return List (ArrayList) of photos (photo) from album object
	 */
	public ArrayList<Photo> getList() {
		return photoList;
	}
	
	/**
	 * Edit name of album
	 * @param Name (String) that is to replace present name of album
	 * return true if successful
	 */
	public boolean editName(String Name) {
		if(Name.equals(""))
		{
			return false;
		}
		name = Name;
		return true;
	}
	
	/**
	 * Add photo to album
	 * @param Photo (photo) that is to be added to album
	 * return true if the photo is successfully added (not duplicate, properly exists) 
	 */
	public boolean addPhoto(Photo Photo) {
		if(Photo.equals(null)) {
			System.out.println("File " + Photo.getName() + " does not exist.");
			return false;
		}else if(photoList.contains(Photo)) {
			System.out.println("Photo " + Photo.getName() +  "already exists in album " + getName());
			return false;
		}
		photoList.add(Photo);
		
		if(startDate.after(Photo.getDate()))
			startDate = Photo.getDate();
		if(endDate.before(Photo.getDate()))
			endDate = Photo.getDate();
		
		startDate.set(Calendar.MILLISECOND,0);
		endDate.set(Calendar.MILLISECOND,0);
		
		numberOfPhotos++;
		
		return true;
	}
	
	/**
	 * Delete a photo
	 */
	public boolean deletePhoto(String photoName) {
		Photo checkPhoto;
		Iterator<Photo> iterator = photoList.iterator();
		while(iterator.hasNext())
		{
			checkPhoto = iterator.next();
			if(checkPhoto.getName().equals(photoName))
			{
				iterator.remove();
				numberOfPhotos--;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * delete a caption
	 * @param Photo (photo) that has altered caption to replace photo
	 * 
	 */
	public void editCaption(Photo Photo) {
		
	}
	
	 @Override 
	 public String toString() {
		 return name;
	 }
	
}