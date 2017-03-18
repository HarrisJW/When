package com.vaadin.vaadin_archetype_application;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

public interface DBProvider {
	public Connection OpenConnection(String connectionString);
	public ArrayList<Object[]> ExecuteQuery(Connection connection, String query);
	public boolean Execute(Connection connection, String query);
	public boolean ExecuteStoredProcedure(Connection connection, String name, Object[] params);
	public ArrayList<Object[]> ExecuteStoredProcedureRead(Connection connection, String name, Object[] params);
	
	
	
	public void Initialize();
	public void Dispose();
	public boolean IsConnected();
	public Connection GetConnection();
	
	
	
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
		public long ID;
		public String code;
		public String name;
		public Date startDate, endDate, duration;
		public MeetingState state;
	}
//	
//	public class MeetingShortDescription
//	{
//		public long ID;
//		public String name;
//		public byte state;
//		public int numberOfMembers;
//	}
	
	public class MeetingMember
	{
		public long ID;
		public String firstName, lastName;
		public UserAccess access;
	}
	
	public int TryJoinMeeting(String meetingCode, String meetingPassword, long userID);
	public long CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration, long userID);
	public boolean DeleteMeeting(long meetingID);//Don't call this in prod
	public MeetingDescription GetMeetingDescription(long meetingID);
	public ArrayList<MeetingShortDescription> GetMeetingsList(long userID);
	public ArrayList<MeetingMember> GetMeetingMembers(long meetingID);
	public int GetMeetingMembersCount(long meetingID);
	public long GetUserID(String email);
	public boolean UpdateMeetingTime(long meetingID, Date startDate, Date endDate, Date duration);
	public long CreateUser(String firstName, String lastName, String email, String googleID);
	public int LeaveMeeting(long meetingID, long userID);
}
