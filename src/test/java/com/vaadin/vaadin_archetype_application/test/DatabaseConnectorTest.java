package com.vaadin.vaadin_archetype_application.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vaadin.vaadin_archetype_application.Constants;
import com.vaadin.vaadin_archetype_application.DatabaseConnector;

public class DatabaseConnectorTest {

	@Test
	public void tryJoinMeetingNoPasswordRequiredNoPasswordProvided() {
		assertEquals(0, DatabaseConnector.TryJoinMeeting("nopass", ""));
	}

	@Test
	public void tryJoinMeetingNoPasswordRequiredPasswordProvided() {
		assertEquals(0, DatabaseConnector.TryJoinMeeting("nopass", "asdf"));
	}

	@Test
	public void tryJoinMeetingPasswordRequiredNoPasswordProvided() {
		assertEquals(Constants.CODE_INVALID_MEETING_ID_PASSWORD, DatabaseConnector.TryJoinMeeting("pass", ""));
	}

	@Test
	public void tryJoinMeetingPasswordRequiredWrongPasswordProvided() {
		assertEquals(Constants.CODE_INVALID_MEETING_ID_PASSWORD, DatabaseConnector.TryJoinMeeting("pass", "asdf"));
	}

	@Test
	public void tryJoinMeetingPasswordRequiredRightPasswordProvided() {
		assertEquals(0, DatabaseConnector.TryJoinMeeting("pass", "guest"));
	}

	@Test
	public void tryJoinMeetingNoIDNoPassword() {
		assertEquals(Constants.CODE_MEETING_ID_EMPTY, DatabaseConnector.TryJoinMeeting("", ""));
	}

	@Test
	public void tryJoinMeetingNoIDPassword() {
		assertEquals(Constants.CODE_MEETING_ID_EMPTY, DatabaseConnector.TryJoinMeeting("", "asdf"));
	}

}
