package control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.*;

@SuppressWarnings("unused")
public class Controller implements ModelController{
	
	public UserModel model = new UserModel();

	@Override
	public void listUsers() {
		// TODO Auto-generated method stub
		Iterator<User> iterator = model.all.iterator();
		while(iterator.hasNext())
		{
			System.out.println(iterator.next().getName());
		}
		return;
	}
	
	@Override
	public ArrayList<model.User> returnUsers() {
		return model.all;
	}
	


	@Override
	public boolean addUser(String u_id, String name) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		if(model.findUser(u_id) != null)
		{
			return false;
		}
		User newUser = new User(u_id, name);
		model.all.add(newUser);
		model.writeUser(newUser);
		return true;
	}

	@Override
	public boolean removeUser(String u_id) {
		// TODO Auto-generated method stub
		if(u_id.equals(""))
			return false;
		
		model.deleteUser(u_id);
		return true;
	}

	@Override
	public boolean userLogin(String u_id) {
		// TODO Auto-generated method stub
		User makeActive = model.findUser(u_id);
		if(makeActive == null)
		{
			System.out.println("Could not find user.");
			return false;
		}
		model.active = makeActive;
		return true;
	}

	@Override
	public void listAlbums() {
		
		int size;
		ArrayList<Album> albumList = model.active.getAlbums();
		Iterator<Album> iterator = albumList.iterator();
		
		//Has no albums
		if(!iterator.hasNext()) {
			System.out.println("No albums exist for user " + model.active.getID());
			return;
		}

		System.out.println("Albums for user " + model.active.getID() + ":");
		
		while(iterator.hasNext())
		{
			Album temp = iterator.next();
			System.out.println(temp);
		}
		return;
	}
	
	@Override
	public ArrayList<model.Album> returnAlbums() {
		return model.active.getAlbums();
	}

	@Override
	public boolean createAlbum(String albumName) {
		model.active.addAlbum(albumName);
		return false;
	}

	@Override
	public boolean deleteAlbum(String albumName) {
		model.active.deleteAlbum(albumName);
		return true;
	}

	@Override
	public void listPhotos(String albumName) {
		Photo temp;
		
		if(model.active.getAlbum(albumName) == null) {
			System.out.println("No photos to be displayed.");
			return;
		}
		Album activeAlbum = model.active.getAlbum(albumName);
		Iterator<Photo> iterator = activeAlbum.getList().iterator();
		while(iterator.hasNext())
		{
			temp = iterator.next();
			System.out.println(temp.getName() + " - " + temp.getDateToString());
		}
		return;
	}

	@Override
	public boolean addPhoto(String fileName, String caption, String albumName) {
		
		//Null string
		if(fileName.equals("")||albumName.equals("")) {
			System.out.println("Null file.");
			return false;
		}

		
		File newFile = new File(fileName);
		
		//Check if file exists
		if(!newFile.exists()) {
			System.out.println("File " + fileName + " does not exist.");
			return false;
		}

		Photo newPhoto = new Photo(newFile.getName(), newFile.toURI().toString(), caption);
		
		//Check if album exists
		if(model.active.getAlbum(albumName) == null) {
			System.out.println("Could not find album! Did not add photo.");
			return false;
		}
		
		model.active.getAlbum(albumName).addPhoto(newPhoto);
		System.out.println("Photo added to album " + albumName);
		return true;
	}

	@Override
	public boolean movePhoto(Photo p, String oldAlbumName, String newAlbumName) {
		
		//Check if old album exists
		if(model.active.getAlbum(oldAlbumName)== null) {
			System.out.println("Album " + oldAlbumName + " does not exist");
			return false;
		}

		//Check if new album exists
		if(model.active.getAlbum(newAlbumName) == null) {
			System.out.println("Album " + newAlbumName + " does not exist");
			return false;
		}
		removePhoto(p.getName(), oldAlbumName);

		model.active.getAlbum(newAlbumName).addPhoto(p);
		
		System.out.println("Moved photo " + p.getName() + ":");
		System.out.println(p.getName() + " - From album " + oldAlbumName + "  ");
		return true;
	

	}

	@Override
	public boolean removePhoto(String fileName, String albumName) {
		// TODO Auto-generated method stub
		if(fileName.equals("")||albumName.equals(""))
			return false;
		if(model.active.getAlbum(albumName)==null)
			return false;
		return model.active.getAlbum(albumName).deletePhoto(fileName);
	}

	@Override
	public boolean addTag(String fileName, String tagType, String tagValue) {
		if(fileName.equals("")||tagType.equals("")||tagValue.equals(""))
			return false;
		
		Photo daPhoto = null;
		Tag newtag = new Tag(tagType, tagValue);
		ArrayList<Album> allUA;
		allUA = model.active.getAlbums(); 
		Iterator<Album> iterator1 = allUA.iterator();
		Iterator<Photo> iterator2;
		while(iterator1.hasNext())
		{
			iterator2 = iterator1.next().getList().iterator();
			while(iterator2.hasNext()){
				daPhoto = iterator2.next();
				if(daPhoto.getName().equals(fileName))
				{
					if(!daPhoto.hasTag(newtag)) {
						daPhoto.addTag(newtag);
						System.out.println("Added tag:");
						System.out.println(daPhoto.getName() + newtag);
						return true;
					}else{
						System.out.println("Tag already exists for " + daPhoto.getName() + " " + newtag);
						return false;
					}
				}
			}
		}
		System.out.println("Photo " + daPhoto.getName() + " does not exist");
		
		return false;
	}
	
	@Override
	public boolean deleteTag(String fileName, String tagType, String tagValue) {
		if(fileName.equals("")||tagType.equals("")||tagValue.equals(""))
			return false;
		
		Photo daPhoto;
		Tag newtag = new Tag(tagType, tagValue);
		ArrayList<Album> allUA;
		allUA = model.active.getAlbums(); 
		Iterator<Album> iterator1 = allUA.iterator();
		Iterator<Photo> iterator2;
		while(iterator1.hasNext())
		{
			iterator2 = iterator1.next().getList().iterator();
			while(iterator2.hasNext()){
				daPhoto = iterator2.next();
				if(daPhoto.getName().equals(fileName))
				{
					if(!daPhoto.hasTag(newtag)) {
						System.out.println("Tag does not exist for " + daPhoto.getName() + " " + newtag);
						return false;
					}else{
						System.out.println("Deleted tag:");
						System.out.println(daPhoto.getName() + newtag);
						return true;
					}
				}
			}
		}
		return false;
		
	}

	@Override
	public boolean listPhotoInfo(String fileName) {

		Photo daPhoto;
		ArrayList<Album> allUA;
		allUA = model.active.getAlbums(); 
		Iterator<Album> iterator1 = allUA.iterator();
		Iterator<Photo> iterator2;
		while(iterator1.hasNext())
		{
			iterator2 = iterator1.next().getList().iterator();
			while(iterator2.hasNext()){
				daPhoto = iterator2.next();
				if(daPhoto.getName().equals(fileName))
				{
					System.out.println(daPhoto.getPhotoInfo());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void getPhotosByDate(Calendar startDate, Calendar endDate) {
		Photo daPhoto;
		ArrayList<Album> allUA;
		allUA = model.active.getAlbums(); 
		Iterator<Album> iterator1 = allUA.iterator();
		Iterator<Photo> iterator2;
		while(iterator1.hasNext())
		{
			iterator2 = iterator1.next().getList().iterator();
			while(iterator2.hasNext()){
				daPhoto = iterator2.next();
				if(daPhoto.getCalendar().after(startDate)&&daPhoto.getCalendar().before(endDate))
				{
					System.out.println(daPhoto.getName());
				}
			}
		}
		return;
	}

	@Override
	public void getPhotosByTag(String tagType, String tagValue) {
		// TODO Auto-generated method stub
		Photo daPhoto;
		ArrayList<Album> allUA;
		Tag newtag = new Tag(tagType, tagValue);
		allUA = model.active.getAlbums(); 
		Iterator<Album> iterator1 = allUA.iterator();
		Iterator<Photo> iterator2;
		while(iterator1.hasNext())
		{
			iterator2 = iterator1.next().getList().iterator();
			while(iterator2.hasNext()){
				daPhoto = iterator2.next();
				if(daPhoto.hasTag(newtag))
				{
					System.out.println(daPhoto.getName());
				}
			}
		}
		return;
	}

	@Override
	public boolean logout() throws IOException {
		model.writeUser(model.active);
		model.active = null;
		return true;
	}
	
	
	
}
