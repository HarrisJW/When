package com.vaadin.vaadin_archetype_application;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

//Interface for database provider
public interface DBProvider {
	//Internal functions
	public Connection OpenConnection(String connectionString);
	public ArrayList<Object[]> ExecuteQuery(Connection connection, String query);
	public boolean Execute(Connection connection, String query);
	public boolean ExecuteStoredProcedure(Connection connection, String name, Object[] params);
	public ArrayList<Object[]> ExecuteStoredProcedureRead(Connection connection, String name, Object[] params);
	
	
	
	//Public controller functions
	public void Initialize();
	public void Dispose();
	public boolean IsConnected();
	public Connection GetConnection();
	
	
	
	//DB stored procedures handlers
	public int TryJoinMeeting(String meetingCode, String meetingPassword, long userID);
	public long CreateMeeting(String password, Date startDate, Date endDate, String name, long duration, long userID);
	public boolean DeleteMeeting(long meetingID);//Don't call this in prod
	public Meeting GetMeetingDescription(long meetingID);
	public ArrayList<Meeting> GetMeetingsList(long userID);
	public ArrayList<MeetingMember> GetMeetingMembers(long meetingID);
	public long GetUserID(String email);
	public boolean UpdateMeetingTime(long meetingID, Date startDate, Date endDate, long duration);
	public long CreateUser(String firstName, String lastName, String email, String googleID);
	public int LeaveMeeting(long meetingID, long userID);
	public boolean SetMeetingState(long meetingID, int state);
	
	public TimeRange[] GetAvailableTimeRanges(long meetingID);
	public TimeRange[] GetUserTimeRanges(long meetingID, long userID);
	public TimeRange[] GetMeetingTimeRanges(long meetingID);
	public long AddUserTimeRange(long meetingID, long userID, Date start, Date end);
	public boolean DeleteUserTimeRange(long rid);
	public boolean ClearMeetingTimeRanges(long meetingID);
	public boolean AddAvailableTimeRange(long meetingID, Date startDate, Date endDate);
	
	public TimeSlot[] GetAvailableTimeSlots(long meetingID);
	public boolean AddTimeSlot(long meetingID, Date startDate, Date endDate);
	public boolean ClearMeetingTimeSlots(long meetingID);
	
	public TimeSlotVote[] GetTimeSlotVotes(long slotID);
	public boolean AddTimeSlotVote(long slotID, long userID, int vote);
	public boolean RemoveTimeSlotVote(long slotID, long userID);
	public boolean UpdateTimeSlotVote(long slotID, long userID, int vote);
	public boolean ClearTimeSlotVotes(long slotID);
}
