package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * User object 
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private ArrayList<Album> albums;
	
	String storeDir = "data";
	String storeFile = name;
	
	
	/**
	 * Constructor for the user
	 * @param The ID (String) for the new user
	 * @param The full name (String) for the new user
	 * @param The albums (album) in a list (ArrayList<album>)
	 * return the created object file (user). 
	 */
	public User(String ID, String Name, ArrayList<Album> Albums) {
		id = ID;
		name = Name;
		albums = Albums;
	}
	/**
	 * Overload constructor for the user
	 * @param The ID (String) for the new user and the name (String) for the new user
	 * return the created object file (user).
	 */
	public User(String ID,String Name) {
		id = ID;
		name = Name;
		albums = new ArrayList<Album>();
		
	}
	
	/**
	 * Add an album to the user
	 * @param The album (album) that should be added
	 * return true if successfully added
	 */
	public boolean addAlbum(String name) {
		Iterator<Album> iterator = albums.iterator();
		Album activeAlbum;
		while(iterator.hasNext())
		{
			activeAlbum = iterator.next();
			if(activeAlbum.getName().equals(name)) {
				return false;
			}
		}
		activeAlbum = new Album(name);
		albums.add(activeAlbum);
		System.out.println("Created album for user " + id + ":");
		System.out.println(name);
		return true;
	}
	/**
	 * Delete an album for a user
	 * @param the album (album) that should be deleted
	 * return true if the album is successfully deleted
	 */
	public boolean deleteAlbum(String name) {
		Iterator<Album> iterator = albums.iterator();
		Album activeAlbum;
		while(iterator.hasNext())
		{
			activeAlbum = iterator.next();
			if(activeAlbum.getName().equals(name)) {
				iterator.remove();
				System.out.println("Deleted album from user " + id + ":");
				System.out.println(name);
				return true;
			}
		}
		return false;
	}
	
	public boolean renameAlbum(String oldName, String newName) {
		Iterator<Album> iterator = albums.iterator();
		Album activeAlbum;
		while(iterator.hasNext())
		{
			activeAlbum = iterator.next();
			if(activeAlbum.getName().equals(oldName)) {
				activeAlbum.editName(newName);
				System.out.println("Changed name of album from user " + id + ":");
				System.out.println(newName);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get all the albums
	 * return returns all the albums in an ArrayList<album>
	 */	
	public ArrayList<Album> getAlbums()
	{
		return albums;
	}
	
	/**
	 * Get all an album
	 * @param Name of the album (String)
	 * return returns the album
	 */	
	public Album getAlbum(String Name)
	{
		Album activeAlbum;
		Iterator<Album> iterator = albums.iterator();
		while(iterator.hasNext())
		{
			activeAlbum = iterator.next();
			if(activeAlbum.getName().equals(Name))
				return activeAlbum;
		}
		return null;
	}
	
	/**
	 * Rename an album that a user has
	 * @param the Name (String) that the album should have
	 * return true if the album is successfully renamed. 
	 */
	public boolean renameAlbum(String Name,Album Album) {
		Album.editName(Name);
		return true;
	}
	
	/**
	 * Gets the name of a user
	 * return returns the name of the user (String)
	 */	
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the ID of a user
	 * return returns the id of the user (String)
	 */	
	public String getID()
	{
		return id;
	}
	
	@Override
	public String toString() {
		return getID();
	}
	


}