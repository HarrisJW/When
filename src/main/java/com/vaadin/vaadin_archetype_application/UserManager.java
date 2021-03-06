package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.mysql.fabric.xmlrpc.base.Member;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.vaadin_archetype_application.MeetingMember.UserAccess;

public final class UserManager {
	private UserManager() { } //Thank you, Java, for not having static classes
	
	//Returns true if user is logged in.
	//Returns false and redirects user to login page if user's not logged in
	public static boolean AssureLogin(String targetView)
	{
		if (IsLoggedIn())
		//if (true)
			return true;
		else
		{
			System.out.println("User not logged in");
			UI.getCurrent().getSession().setAttribute("loginRedirectTarget", targetView);
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_LOGIN);
			return false;
		}
	}
	
	//Checks if user is logged in
	public static boolean IsLoggedIn()
	{
		return UI.getCurrent().getSession().getAttribute("credential") != null;
	}
	
	private static Person GetGooglePersonInfo()
	{
		GoogleCredential credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		Plus plus = null;
		
		try 
		{
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		
			if (plus != null) 
			{
				return plus.people().get("me").execute();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String GetUserEmailAddress()
	{
		return GetGooglePersonInfo().getEmails().get(0).getValue();
	}
	
	public static String GetUserDisplayName()
	{
		return GetGooglePersonInfo().getDisplayName().toString();
	}
	
	public static String GetUserID()
	{
		return GetGooglePersonInfo().getId().toString();
	}
	
	public static MeetingMember.UserAccess GetCurerntUserMeetingAccess(Meeting meeting)
	{
		for (MeetingMember member : meeting.members)
			if (member.ID == Controllers.UserID && !member.access.equals(UserAccess.Member))
				return member.access;
		return UserAccess.Member;
	}
}
