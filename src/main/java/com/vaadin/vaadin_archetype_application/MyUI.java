package com.vaadin.vaadin_archetype_application;

import javax.servlet.annotation.WebServlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push
@Theme("mytheme")

public class MyUI extends UI {

	// Tracks all views available to the user interface.
	Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	//Create a new Navigator instance, attached to this user interface.
    	navigator = new Navigator(this, this);
    	
    	// Create a new instance of each of our view classes,
    	// and register them with the navigator.
    	navigator.addView(Constants.URL_LOGIN, new LoginView());
    	navigator.addView(Constants.URL_LOGGED_ON, new LoggedOnView());
    	navigator.addView(Constants.URL_JOIN_MEETING, new JoinMeetingView());
    	navigator.addView(Constants.URL_MEETING_OVERVIEW, new MeetingOverviewView());
    	navigator.addView(Constants.URL_CREATE_MEETING, new CreateMeetingView());
    	navigator.addView(Constants.URL_ALL_MEETINGS, new AllMeetingsView());
    	
    	Controllers.DatabaseConnector.Initialize();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

    }

}
