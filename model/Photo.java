package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

/*
 * The photo class (containing Name, Caption, Tags, and Date)
 */
public class Photo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private String caption;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	private Calendar date;
	private DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
	
	
	/**
	 * The constructor
	 */
	public Photo(String Name, String path, String Caption, Tag t, Calendar Date)
	{
		name = Name;
		this.path = path;
		caption = Caption;
		tags.add(t);
		date = Date;
	} 
	
	public Photo(String Name, String path, String Caption)
	{
		name = Name;
		this.path = path;
		caption = Caption;
		date = Calendar.getInstance();
		date.set(Calendar.MILLISECOND,0);
	}

	/**
	 * Edit the name value of a photo object
	 * @param Name (String) to replace the present name of the photo object
	 * return true if successful 
	 */
	public boolean editName(String Name) {
		if(Name.equals(""))
			return false;
		name = Name;
		return true;
	}
	
	/**
	 * Edit the caption value of a photo object
	 * return true if caption edit is successful (Caption is now caption)
	 */
	public boolean editCaption(String Caption) {
		if(Caption.equals(""))
			return false;
		caption = Caption;
		return true;
	}
	
	/**
	 * Add tags to the tag list of a photo object
	 */
	public boolean addTag(Tag t) {
		if(t == null) {
			return false;
		}else if(tags.contains(t)) {
			return false;
		}
		
		tags.add(t);
		return true;
	}
	
	/**
	 * delete a tag
	 * return true if successfully deleted the tag (tag was in list and was deleted)
	 */
	public boolean deleteTag(Tag t) {
		if(t == null)
			return false;
		Iterator<Tag> iterator = tags.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next() == t){
				iterator.remove();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * get the name
	 * return the name (String) of the photo object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * get the path
	 * return the path(String) of the photo object
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * get the caption
	 * return the caption (String) of the photo object
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * get all Tags in a list
	 * return a list (ArrayList) of the tags (Strings) of the photo object
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	
	/**
	 * Display all tags in string
	 * return String of all tags
	 */
	public String displayTags() {
		Iterator<Tag> iterator = tags.iterator();
		StringBuilder sb = new StringBuilder();
		while(iterator.hasNext()) {
			sb.append(iterator.next().toString());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	
	/**
	 * Check if tag exists
	 */
	public boolean hasTag(Tag t){
		Iterator<Tag> iterator = tags.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().equals(t))
				return true;
		}
		return false;
	}
	
	/**
	 * get calendar date of photo
	 * author Regan Shaner
	 */
	public Calendar getCalendar() {
		return date;
	}

	public Calendar getDate() {
		return date;
	}
	
	
	/**
	 * Returns the date as a string
	 * author Regan Shaner
	 */
	public String getDateToString() {
		return dateFormat.format(date.getTime());
	}
	
	
	/**
	 * Returns photo info as a string
	 * author Regan Shaner
	 */
	public String getPhotoInfo() {
		return name + " " + getDateToString() + " " + displayTags();
	}
	
	@Override
	public String toString() {
		return name;
	}
	

}
