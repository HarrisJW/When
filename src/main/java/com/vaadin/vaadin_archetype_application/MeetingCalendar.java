package com.vaadin.vaadin_archetype_application;

import java.util.Date;
import java.util.List;

import com.google.api.services.calendar.model.TimePeriod;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;

public class MeetingCalendar {

	private static Calendar cal;
	private static Meeting meeting;
	private Date startDate;
	private Date endDate;
	public MeetingCalendar(Meeting meeting)
	{
		cal = new Calendar();
		this.meeting = meeting;
	}
	
	public Calendar getCalendar()
	{
		return cal;
	}
	
	public void setStartDate()
	{
		long timeMillis = meeting.startDate.getValue();
		startDate = new Date(timeMillis);
		cal.setStartDate(startDate);
	}
	public void setEndDate()
	{
		long timeMillis = meeting.endDate.getValue();
		endDate = new Date(timeMillis);
		cal.setEndDate(endDate);
	}
	
	public void setVisibleHours(int firstHour, int lastHour)
	{
		cal.setFirstVisibleHourOfDay(firstHour);
		cal.setLastVisibleHourOfDay(lastHour);
	}
	
	public void addTimeRange(TimePeriod tp, int i)
	{
		Date start = new Date(tp.getStart().getValue());
		Date end = new Date(tp.getEnd().getValue());
		EditableCalendarEvent event = new BasicEvent();
		event.setStart(start);
		event.setEnd(end);
		event.setCaption("Free time range " + i);
		cal.addEvent(event);
		//this.addTimeSlot(tp);
	}
	
	public void addTimeRanges(List<TimePeriod> tps) 
	{
		int i = 1;
		for (TimePeriod tp : tps)
		{
			this.addTimeRange(tp, i);
			i++;
		}
	}
	/*
	public void addTimeSlot(TimePeriod tp)
	{
		int j = 1;
		for (long i = tp.getStart().getValue(); 
				 i < tp.getEnd().getValue(); 
				 i += meeting.duration * 60000)	
		{
			Date start = new Date(i);
			Date end = new Date(i + meeting.duration * 60000);
			EditableCalendarEvent event = new BasicEvent();
			event.setStart(start);
			event.setEnd(end);
			event.setCaption("Time slot " + j++);
			cal.addEvent(event);
		}
	}*/
}
