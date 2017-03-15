package com.vaadin.vaadin_archetype_application;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

public interface DBProvider {
	public Connection OpenConnection(String connectionString);
	public ArrayList<Object[]> ExecuteQuery(Connection connection, String query);
	public boolean Execute(Connection connection, String query);
	public ArrayList<Object[]> ExecuteStoredProcedure(Connection connection, String name, Object[] params);
	
	
	
	public void Initialize();
	public void Dispose();
	public boolean IsConnected();
	
	
	
	public enum UserAccess
	{
		Member,
		Moderator,
		Creator
	}
	
	public enum MeetingState
	{
		Setup,
		Voting,
		Finalized
	}
	
	public class MeetingDescription
	{
		public int ID;
		public String name;
		public Date startDate, endDate, duration;
		public MeetingState state;
	}
	
	public class MeetingShortDescription
	{
		public int ID;
		public String name;
		public byte state;
		public int numberOfMembers;
	}
	
	public class MeetingMember
	{
		public int ID;
		public String firstName, lastName;
		public UserAccess access;
	}
	
	public int TryJoinMeeting(String meetingID, String meetingPassword);
	public int CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration);
	public boolean DeleteMeeting(int meetingID);//Don't call this in prod
	public MeetingDescription GetMeetingDescription(int meetingID);
	public MeetingShortDescription[] GetMeetingsList();
	public MeetingMember[] GetMeetingMembers(int meetingID);
	public int GetMeetingMembersCount(int meetingID);
	public int GetUserID(String email);
	public boolean UpdateMeetingTime(int meetingID, Date startDate, Date endDate, Date duration);
	public boolean CreateUser(String firstName, String lastName, String email, String googleID);
}
