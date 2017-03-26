package com.vaadin.vaadin_archetype_application;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.vaadin_archetype_application.TimeSlotVote.Vote;

//Data container for meetings
public class Meeting {
	
	public enum MeetingState
	{
		Setup,
		TimeRangeSelection,
		TimeSlotVoting,
		Finalized
	}
	
	
	
	public long ID;
	public String code;
	public String password;
	public String name;
	public Date startDate, endDate;
	public long duration;
	public MeetingState state;
	public int membersCount;
	public ArrayList<MeetingMember> members;
	
	public Meeting()
	{
		
	}

	public Meeting(long ID, String code, String name, Date startDate, Date endDate, long duration){
		
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
	
	public String getMembers()
	{
		return String.valueOf(membersCount);
	}
	
	public String getStatus()
	{
		return state.toString();
	}
	
	public void SetState(int s)
	{
		MeetingState[] sa = MeetingState.values();
		for (int i = 0; i < sa.length; i++)
			if (sa[i].ordinal() == s)
			{
				state = sa[i];
				return;
			}
	}
}
