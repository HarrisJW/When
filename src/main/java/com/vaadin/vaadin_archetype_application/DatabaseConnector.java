package com.vaadin.vaadin_archetype_application;

import static org.junit.Assert.assertEquals;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.math.BigInteger;

import org.junit.Test;

//Class responsible for fetching and uploading data to database
public class DatabaseConnector extends MySQLProvider {
	
	private Connection connection = null;
	
	public void Initialize()
	{
		connection = OpenConnection(Constants.dbConnectionString);
	}
	
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

	
	
	@Override
	public long CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "createMeeting", new Object[] { password, startDate,
				endDate, name, duration, userID });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean DeleteMeeting(long meetingID) {
		return ExecuteStoredProcedure(connection, "deleteMeeting", new Object[] { meetingID });
	}

	@Override
	public MeetingDescription GetMeetingDescription(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getMeetingDescription", new Object[] { meetingID });
		if (r.size() == 0)
			return null;
		Object[] o = r.get(0);

		MeetingDescription meetingDescription = new MeetingDescription();
		meetingDescription.ID = meetingID;
		meetingDescription.name = (String)o[0];
		meetingDescription.startDate = (Date)o[1];
		meetingDescription.endDate = (Date)o[2];
		meetingDescription.duration = (Date)o[3];
		if ((int)o[4]==0)
			meetingDescription.state = MeetingState.Setup;
		else if ((int)o[4]==1)
			meetingDescription.state = MeetingState.Voting;
		else
			meetingDescription.state = MeetingState.Finalized;
		meetingDescription.code = (String)o[5];
		
		return meetingDescription;
	}

	@Override
	public ArrayList<MeetingShortDescription> GetMeetingsList(long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getMeetingList", new Object[] { userID });
		Object[] o;
		ArrayList<MeetingShortDescription> meetingDescriptions = new ArrayList<MeetingShortDescription>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingShortDescription msd = new MeetingShortDescription();
			o = r.get(i);
			msd.ID = (int)o[0];
			msd.name = (String)o[1];
			msd.state = (byte)(int)o[2];
			msd.numberOfMembers = (int)(long)o[3];
			meetingDescriptions.add(msd);
		}
		return meetingDescriptions;
	}

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
			if ((int)o[3]==0)
				mm.access = UserAccess.Member;
			else if ((int)o[3]==1)
				mm.access = UserAccess.Moderator;
			else
				mm.access = UserAccess.Creator;
			System.out.println("");
			meetingMembers.add(mm);
		}
		return meetingMembers;
	}

	@Override
	public int GetMeetingMembersCount(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getMeetingMembersCount", new Object[] { meetingID });
		Object[] o = r.get(0);
		return (int)(long)o[0];
	}

	@Override
	public long GetUserID(String email) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "getUserID", new Object[] { email });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean UpdateMeetingTime(long meetingID, Date startDate, Date endDate, Date duration) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "updateMeetingTime", new Object[] { meetingID, startDate,
				endDate, duration });
		return true;
	}

	@Override
	public long CreateUser(String firstName, String lastName, String email, String googleID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "createUser", new Object[] { email, firstName, lastName, googleID });
		Object[] o = r.get(0);
		return (int)o[0];
	}
	
	public int LeaveMeeting(long meetingID, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedureRead(connection, "leaveMeeting", new Object[] { userID, meetingID });
		Object[] o = r.get(0);
		return (int)(long)o[0];
		
	}
	
	
	
}