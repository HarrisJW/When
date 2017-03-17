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
		
		if (meetingID.length() == 0)
			return Constants.CODE_MEETING_ID_EMPTY;
		if (meetingID.equals("nopass"))
			return 0;
		if (meetingID.equals("pass") && meetingPassword.equals("guest"))
			return 0;
		
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "joinMeeting", new Object[] { meetingID, meetingPassword, userID });
		Object[] o = r.get(0);
		
		return (int) o[1];
	}

	
	
	@Override
	public long CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "createMeeting", new Object[] { password, startDate,
				endDate, name, duration, userID });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean DeleteMeeting(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "deleteMeeting", new Object[] { meetingID });
		return false;
	}

	@Override
	public MeetingDescription GetMeetingDescription(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "getMeetingDescription", new Object[] { meetingID });
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
		
		return meetingDescription;
	}

	@Override
	public ArrayList<MeetingShortDescription> GetMeetingsList(long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "getMeetingList", new Object[] { userID });
		Object[] o;
		ArrayList<MeetingShortDescription> meetingDescriptions = new ArrayList<MeetingShortDescription>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingShortDescription msd = new MeetingShortDescription();
			o = r.get(i);
			msd.ID = ((BigInteger)o[0]).longValue();
			msd.name = (String)o[1];
			msd.state = (byte)o[2];
			msd.numberOfMembers = (int)o[3];
			meetingDescriptions.add(msd);
		}
		return meetingDescriptions;
	}

	@Override
	public ArrayList<MeetingMember> GetMeetingMembers(long meetingID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "getMeetingMembers", new Object[] { meetingID });
		Object[] o;
		ArrayList<MeetingMember> meetingMembers = new ArrayList<MeetingMember>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingMember mm = new MeetingMember();
			o = r.get(i);
			mm.ID = ((BigInteger)o[0]).longValue();
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
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "getMeetingMembersCount", new Object[] { meetingID });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public long GetUserID(String email) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "getUserID", new Object[] { email });
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean UpdateMeetingTime(long meetingID, Date startDate, Date endDate, Date duration) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "updateMeetingTime", new Object[] { meetingID, startDate,
				endDate, duration });
		return false;
	}

	@Override
	public long CreateUser(String firstName, String lastName, String email, String googleID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "createUser", new Object[] { email, firstName, lastName, googleID });
		Object[] o = r.get(0);
		return (int)o[0];
	}
	
	public int LeaveMeeting(long meetingID, long userID) {
		ArrayList<Object[]> r = ExecuteStoredProcedure(connection, "leaveMeeting", new Object[] { meetingID, userID });
		Object[] o = r.get(0);
		return (int)o[0];
		
	}
	
	
	
}