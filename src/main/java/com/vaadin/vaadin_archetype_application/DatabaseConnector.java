package com.vaadin.vaadin_archetype_application;

import org.junit.Test;

//Class responsible for fetching and uploading data to database
public final class DatabaseConnector{
	private DatabaseConnector() { } //Thank you, Java, for not allowing static classes /s

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
}