package com.vaadin.vaadin_archetype_application;

import javax.servlet.annotation.WebServlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
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
	private GoogleCredential credential;
	// init() is the new main()
	// Use this pattern for all UI for CSCI 3130.
	// Create layout --> create components --> put them together.
	Navigator navigator;
	protected static final String MAINVIEW = "main";
	protected static final String LOGGEDON = "loggedon";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	getPage().setTitle("Navigation Example");
    	navigator = new Navigator(this, this);
    	navigator.addView("", new LoginView());
    	navigator.addView(LOGGEDON, new LoggedOnView());   
    	
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

    }

}
