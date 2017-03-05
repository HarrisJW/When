package com.vaadin.vaadin_archetype_application;

import com.vaadin.ui.UI;

public final class UserManager {
	private UserManager() { } //Thank you, Java, for not having static classes
	
	public static boolean IsLoggedIn(String targetView)
	{
		if (UI.getCurrent().getSession().getAttribute("credential") == null)
		{
			System.out.println("User not logged in");
			UI.getCurrent().getSession().setAttribute("loginRedirectTarget", targetView);
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_LOGIN);
			return false;
		}
		else
			return true;
	}
}
