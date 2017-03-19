package com.vaadin.vaadin_archetype_application;

import java.util.ArrayList;
import java.util.Date;

//Data container for meetings
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
	public int membersCount;
	public ArrayList<MeetingMember> members;
	
	public Meeting()
	{
		
	}

	public Meeting(long ID, String code, String name, Date startDate, Date endDate, Date duration){
		
		this.ID = ID;
		this.code = code;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
		
	}
	
	public String getMeetingName()
	{
		return name;
	}
	
	public String getStartTime(){
		
		return startDate.toString();
		
	}
	
	public String getEndTime(){
		
		return endDate.toString();
		
	}
	
	public String getMembersCountString()
	{
		return String.valueOf(membersCount);
	}
	
	public void SetState(int s)
	{
		if (s == 0)
			state = MeetingState.Setup;
		else if (s == 1)
			state = MeetingState.Voting;
		else
			state = MeetingState.Finalized;
	}
}
