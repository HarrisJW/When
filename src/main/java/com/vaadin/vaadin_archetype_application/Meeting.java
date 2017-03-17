package com.vaadin.vaadin_archetype_application;

import java.util.Date;

public class Meeting {
	
    public enum MeetingState

    {

        Setup,

        Voting,

        Finalized

    }
	
    public long ID;
    public String code;
    public String name;
    public Date startDate, endDate, duration;

    public MeetingState state;
	
	private String startTime;
	private String endTime;

	public Meeting(long ID, String code, String name, Date startDate, Date endDate, Date duration){
		
		this.ID = ID;
		this.code = code;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		
	}
	
	public String getStartTime(){
		
		return this.startTime;
		
	}
	
	public String getEndTime(){
		
		return this.endTime;
		
	}
}
