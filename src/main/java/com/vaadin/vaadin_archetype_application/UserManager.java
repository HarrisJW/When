package com.vaadin.vaadin_archetype_application;

import com.vaadin.ui.UI;

public final class UserManager {
	private UserManager() { } //Thank you, Java, for not having static classes
	
	//Returns true if user is logged in.
	//Returns false and redirects user to login page if user's not logged in
	public static boolean AssureLogin(String targetView)
	{
		if (IsLoggedIn())
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
}
