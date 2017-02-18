package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.OAuthPopupConfig;
import org.vaadin.addon.oauthpopup.buttons.GoogleButton;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.plus.model.Person;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
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

	// init() is the new main()
	// Use this pattern for all UI for CSCI 3130.
	// Create layout --> create components --> put them together.
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        String GGL_KEY = "955701574186-f8mole07i7gdb6mevst2hdbrq857sool.apps.googleusercontent.com";
        String GGL_SECRET = "NbQmw6H9iTi7i8KmC5FudO4p";
   
        /* 
        It might be necessary in the future to use the below code.
        
        OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(GGL_KEY, GGL_SECRET);
        config.setScope("https://www.googleapis.com/auth/calendar "
        		+ "https://www.googleapis.com/auth/userinfo.email "
        		+ "https://www.googleapis.com/auth/userinfo.profile");
        OAuthPopupButton ob = new OAuthPopupButton(GoogleApi20.instance(), config);
        */
        
        OAuthPopupButton ob = new GoogleButton(GGL_KEY, GGL_SECRET, "https://www.googleapis.com/auth/calendar "
        		+ "https://www.googleapis.com/auth/userinfo.email "
        		+ "https://www.googleapis.com/auth/userinfo.profile");
        ob.addOAuthListener(new OAuthListener() {
        	@Override
        	public void authSuccessful(Token token, boolean isOAuth20) {
        		// TODO Auto-generated method stub
        		System.out.println(token.toString());
        		
        		/*
        		    If authentication is successful, use token provided by Google to
        		    create a credential which allows When to access user's profile.
        		    Temporarily using Google+ to show that the authentication and
        		    retrival of information has been successful.
        		 */
        		if (token instanceof OAuth2AccessToken) {
        			String oa2 = ((OAuth2AccessToken) token).getAccessToken();
        			GoogleCredential credential = new GoogleCredential().setAccessToken(oa2);
        			System.out.println(oa2);
        			((OAuth2AccessToken) token).getRefreshToken();
        			((OAuth2AccessToken) token).getExpiresIn();
        			Plus plus = null;
        			try {
						plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
					} catch (GeneralSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			if (plus!=null) {
        				try {
							Person profile = plus.people().get("me").execute();
							layout.addComponent(new Label(profile.getDisplayName() + " has logged in!"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				
        			}
        		} else {
        			((OAuth1AccessToken) token).getToken();
        			((OAuth1AccessToken) token).getTokenSecret();
        		}
        	}

        	@Override
        	public void authDenied(String reason) {
        		// TODO Auto-generated method stub
        		System.out.println("DENIED");
        	}
        });
        
        layout.addComponents(ob);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
