package model;

import java.util.Calendar;

public class DateRange {
	
	private Calendar startDate;
	private Calendar endDate;
	
	public DateRange(Calendar s, Calendar e) {
		startDate = s;
		endDate = e;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	
	public Calendar getEndDate() {
		return endDate;
	}

}
