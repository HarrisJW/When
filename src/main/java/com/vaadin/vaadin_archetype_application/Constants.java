package com.vaadin.vaadin_archetype_application;

//Class to store all of the constants
public final class Constants {
	private Constants() { } //Thank you, Java, for not allowing static classes /s

	//URL constants
	public static final String URL_LOGIN = "login";
	public static final String URL_LOGGED_ON = "logged-on";
	public static final String URL_JOIN_MEETING = "join-meeting";
	public static final String URL_MEETING_OVERVIEW = "meeting-overview";
	
	//Return codes
	public static final int CODE_INVALID_MEETING_ID_PASSWORD = 1;
	public static final int CODE_MEETING_ID_EMPTY = 2;
}
