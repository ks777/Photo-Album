package model;

import java.io.Serializable;
import java.util.Calendar;

public class Tag implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private String tagType;
	private String tagValue;
	private Calendar date;
	
	public Tag(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
		this.date = Calendar.getInstance();
		date.set(Calendar.MILLISECOND,0);
	}
	
	public String getTagType() {
		return tagType;
	}
	
	public String getTagValue() {
		return tagValue;
	}
	
	public Calendar getTagDate() {
		return date;
	}
	
	public void setTagType(String type) {
		tagType = type;
	}
	
	public void setTagValue(String value) {
		tagValue = value;
	}
	
	@Override
	public String toString() {
		return tagType + ":" + "\"" + tagValue + "\"";
	}

	public boolean equals(Tag tag) {
		Tag t = tag;
		return (t.tagType.equals(tagType) && t.tagValue.equals(tagValue));
	}
}
