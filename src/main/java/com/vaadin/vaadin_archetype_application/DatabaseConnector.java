package com.vaadin.vaadin_archetype_application;

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
	public int TryJoinMeeting(String meetingID, String meetingPassword)//TODO
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
	public int CreateMeeting(String password, Date startDate, Date endDate, String name, Date duration) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean DeleteMeeting(int meetingID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MeetingDescription GetMeetingDescription(int meetingID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeetingShortDescription[] GetMeetingsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeetingMember[] GetMeetingMembers(int meetingID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int GetMeetingMembersCount(int meetingID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int GetUserID(String email) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean UpdateMeetingTime(int meetingID, Date startDate, Date endDate, Date duration) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CreateUser(String firstName, String lastName, String email, String googleID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}