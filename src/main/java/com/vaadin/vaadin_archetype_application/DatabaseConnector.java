package com.vaadin.vaadin_archetype_application;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

//Class responsible for fetching and uploading data to database
public final class DatabaseConnector{
	
	private DatabaseConnector() { } //Thank you, Java, for not allowing static classes /s

	//Function that checks if meeting ID and password correspond to any existing ones and 
	//joins users to that meeting
	public static int TryJoinMeeting(String meetingID, String meetingPassword)
	{
		if (meetingID.length() == 0)
			return Constants.CODE_MEETING_ID_EMPTY;
		
		if (meetingID.equals("nopass"))
			return 0;
		if (meetingID.equals("pass") && meetingPassword.equals("guest"))
			return 0;
		
		return Constants.CODE_INVALID_MEETING_ID_PASSWORD;
	}
	
	public static int CreateMeeting(String meetingName, 
			String meetingPassword, 
			String confirmMeetingPassword, 
			String participantEmailAddresses){
		
		return 0;
	}
	
	public static List<Meeting> getMeetings(String username){
		
		Meeting meetingAM = new Meeting(0, "9:00AM", "10:00AM", null, null, null);
		Meeting meetingPM = new Meeting(0, "1:00PM", "2:00PM", null, null, null);
		
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		meetings.add(meetingAM);
		meetings.add(meetingPM);
		return meetings;
		
		
	}
}