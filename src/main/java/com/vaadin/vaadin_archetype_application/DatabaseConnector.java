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
	public int TryJoinMeeting(String meetingID, String meetingPassword, int userID)//TODO
	{
		if (meetingID.length() == 0)
			return Constants.CODE_MEETING_ID_EMPTY;
		
		if (meetingID.equals("nopass"))
			return 0;
		if (meetingID.equals("pass") && meetingPassword.equals("guest"))
			return 0;
		
		return Constants.CODE_INVALID_MEETING_ID_PASSWORD;
	}

	
	
	@Override
	public int CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration, int userID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "createMeeting", new Object[] { password, startDate,
				endDate, name, duration, userID});
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean DeleteMeeting(int meetingID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "deleteMeeting", new Object[] { meetingID });
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		return false;
	}

	@Override
	public MeetingDescription GetMeetingDescription(int meetingID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "getMeetingDescription", new Object[] { meetingID });
		Object[] o = null;
		for (int i = 0; i < r.size(); i++)
		{
			o = r.get(i);
			System.out.print((String)o[0] + ", ");
			System.out.print((Date)o[1] + ", ");
			System.out.print((Date)o[2] + ", ");
			System.out.print((Date)o[3] + ", ");
			System.out.print((int)o[4] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		MeetingDescription meetingDescription = new MeetingDescription();
		if (o == null) {
			meetingDescription.name = (String)o[0];
			meetingDescription.startDate = (Date)o[1];
			meetingDescription.endDate = (Date)o[2];
			meetingDescription.duration = (Date)o[3];
			meetingDescription.state = MeetingState.Setup;
		} else {
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
		}
		return meetingDescription;
	}

	@Override
	public ArrayList<MeetingShortDescription> GetMeetingsList(int userID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "getMeetingList", new Object[] { userID });
		Object[] o;
		ArrayList<MeetingShortDescription> meetingDescriptions = new ArrayList<MeetingShortDescription>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingShortDescription msd = new MeetingShortDescription();
			o = r.get(i);
			System.out.print((int)o[0] + ", ");
			msd.ID = (int)o[0];
			System.out.print((String)o[1] + ", ");
			msd.name = (String)o[1];
			System.out.print((int)o[2] + ", ");
			msd.ID = (int)o[2];
			System.out.print((int)o[3] + ", ");
			msd.ID = (int)o[3];
			System.out.println("");
			meetingDescriptions.add(msd);
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		return meetingDescriptions;
	}

	@Override
	public ArrayList<MeetingMember> GetMeetingMembers(int meetingID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "getMeetingMembers", new Object[] { meetingID });
		Object[] o;
		ArrayList<MeetingMember> meetingMembers = new ArrayList<MeetingMember>();
		for (int i = 0; i < r.size(); i++)
		{
			MeetingMember mm = new MeetingMember();
			o = r.get(i);
			System.out.print((int)o[0] + ", ");
			mm.ID = (int)o[0];
			System.out.print((String)o[1] + ", ");
			mm.firstName = (String)o[1];
			System.out.print((String)o[2] + ", ");
			mm.lastName = (String)o[2];
			System.out.print((int)o[3] + ", ");
			if ((int)o[3]==0)
				mm.access = UserAccess.Member;
			else if ((int)o[3]==1)
				mm.access = UserAccess.Moderator;
			else
				mm.access = UserAccess.Creator;
			System.out.println("");
			meetingMembers.add(mm);
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		return meetingMembers;
	}

	@Override
	public int GetMeetingMembersCount(int meetingID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "getMeetingMembersCount", new Object[] { meetingID });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public int GetUserID(String email) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "getUserID", new Object[] { email });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		Object[] o = r.get(0);
		return (int)o[0];
	}

	@Override
	public boolean UpdateMeetingTime(int meetingID, Date startDate, Date endDate, Date duration) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "updateMeetingTime", new Object[] { meetingID, startDate,
				endDate, duration });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		return false;
	}

	@Override
	public int CreateUser(String firstName, String lastName, String email, String googleID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "createUser", new Object[] { firstName, lastName, email, googleID });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		Object[] o = r.get(0);
		return (int)o[0];
	}
	
	public int LeaveMeeting(int meetingID, int userID) {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "leaveMeeting", new Object[] { meetingID, userID });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			System.out.print((int)o[0] + ", ");
			System.out.println("");
		}
		//assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
		Object[] o = r.get(0);
		return (int)o[0];
		
	}
	
	
	
}