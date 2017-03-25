package com.vaadin.vaadin_archetype_application;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//Class responsible for fetching and uploading data to database
public class DatabaseConnector extends MySQLProvider {
	
	private Connection connection = null;
	
	//Opens db connection
	public void Initialize()
	{
		OpenConnection(Constants.dbConnectionString);
	}
	
	@Override
	public Connection OpenConnection(String conStr)
	{
		try
		{
			if (connection == null || connection.isClosed())
				connection = super.OpenConnection(conStr);
			return connection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//Closes connection
	public void Dispose()
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		connection = null;
	}
	
	@Override
	public boolean IsConnected()
	{
		return connection != null;
	}
	
	@Override
	public Connection GetConnection()
	{
		return connection;
	}
	

	
	//Function that checks if meeting ID and password correspond to any existing ones and 
	//joins users to that meeting
	@Override
	public int TryJoinMeeting(String meetingID, String meetingPassword, long userID)
	{
		if (meetingID == null || meetingID.length() == 0)
			return Constants.CODE_MEETING_ID_EMPTY;
		
		try
		{
			ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "joinMeeting", new Object[] { userID, meetingID, meetingPassword });
			Object[] o = r.get(0);
			return (int)(long)o[0];
		}
		catch (Exception e) 
		{
			return Constants.CODE_INVALID_MEETING_ID_PASSWORD;
		}
	}

	//Creates meeting. Returns SQL return code
	@Override
	public long CreateMeeting(String password, Date startDate, Date endDate, String name, long duration, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "createMeeting", new Object[] { password, startDate,
				endDate, name, duration, userID });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	//Deletes meeting. Returns true or throws an exception(???)
	@Override
	public boolean DeleteMeeting(long meetingID) {
		return ExecuteStoredProcedure(connection, "deleteMeeting", new Object[] { meetingID });
	}

	//Returns populated Meeting instance along with meeting members
	@Override
	public Meeting GetMeetingDescription(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getMeetingDescription", new Object[] { meetingID });
		if (r.size() == 0 || r.get(0) == null || r.get(0)[0] == null)
			return null;
		Object[] o = r.get(0);

		Meeting meeting = new Meeting();
		meeting.ID = meetingID;
		meeting.name = (String)o[0];
		meeting.startDate = (Date)o[1];
		meeting.endDate = (Date)o[2];
		meeting.duration = (long)o[3];
		meeting.SetState((int)o[4]);
		meeting.code = (String)o[5];
		meeting.password = (String)o[6];
		meeting.members = GetMeetingMembers(meetingID);
		meeting.membersCount = meeting.members.size();
		
		return meeting;
	}

	//Returns populated list of Meeting instances along with meeting members
	@Override
	public ArrayList<Meeting> GetMeetingsList(long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getFullMeetingsDescription", new Object[] { userID });
		Object[] o;
		ArrayList<Meeting> meetingDescriptions = new ArrayList<Meeting>();
		for (int i = 0; i < r.size(); i++)
		{
			o = r.get(i);
			Meeting meeting = new Meeting();
			meeting.ID = (int)o[0];
			meeting.name = (String)o[1];
			meeting.startDate = (Date)o[2];
			meeting.endDate = (Date)o[3];
			meeting.duration = (long)o[4];
			meeting.SetState((int)o[5]);
			meeting.code = (String)o[6];
			meeting.membersCount = (int)(long)o[7];
			meeting.password = (String)o[8];
			meeting.members = GetMeetingMembers(meeting.ID);
			meetingDescriptions.add(meeting);
		}
		return meetingDescriptions;
	}

	//Returns list of meeting members
	@Override
	public ArrayList<MeetingMember> GetMeetingMembers(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getMeetingMembers", new Object[] { meetingID });
		Object[] o;
		ArrayList<MeetingMember> meetingMembers = new ArrayList<MeetingMember>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingMember mm = new MeetingMember();
			o = r.get(i);
			mm.ID = (int)o[0];
			mm.firstName = (String)o[1];
			mm.lastName = (String)o[2];
			mm.SetAccess((int)o[3]);
			System.out.println("");
			meetingMembers.add(mm);
		}
		return meetingMembers;
	}

	//Returns user ID based on email address. Can be used to login
	@Override
	public long GetUserID(String email) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getUserID", new Object[] { email });
		if (r.size() == 0)
			return -1;
		Object[] o = r.get(0);
		return (int)o[0];
	}

	//Updates meeting parameters
	@Override
	public boolean UpdateMeetingTime(long meetingID, Date startDate, Date endDate, long duration) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "updateMeetingTime", new Object[] { meetingID, startDate,
				endDate, duration });
		return true;
	}

	//Creates a user and returns uid
	@Override
	public long CreateUser(String firstName, String lastName, String email, String googleID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "createUser", new Object[] { email, firstName, lastName, googleID });
		Object[] o = r.get(0);
		return (int)o[0];
	}
	
	//Leaves user from a meeting
	public int LeaveMeeting(long meetingID, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "leaveMeeting", new Object[] { userID, meetingID });
		Object[] o = r.get(0);
		return (int)(long)o[0];
		
	}
	
	
}