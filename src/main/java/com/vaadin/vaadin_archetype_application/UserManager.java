package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

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
	
	public static String GetUserEmailAddress()
	{
		GoogleCredential credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		Plus plus = null;
		
		try {
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (plus!=null) {
			try {
				Person profile = plus.people().get("me").execute();
				return profile.getEmails().toString();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "";
	}
}
