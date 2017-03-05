package com.vaadin.vaadin_archetype_application;

import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.buttons.GoogleButton;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
//import com.github.scribejava.core.model.OAuth1AccessToken;
//import com.github.scribejava.core.model.OAuth2AccessToken;
//import com.github.scribejava.core.model.Token;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends Panel implements View {

	protected VerticalLayout layout;
	protected OAuthPopupButton ob;
	protected Label lLoginRequired;

	protected GoogleCredential credential;


	@Override
	public void enter(ViewChangeEvent event) {
		if (UserManager.IsLoggedIn())
		{
			OnUserLoggedIn();
			return;
		}
		Page.getCurrent().setTitle("Login");
		setSizeFull();
		InitUI();
	}
	
	protected void InitUI() {
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
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
        
        ob = new GoogleButton(GGL_KEY, GGL_SECRET, "https://www.googleapis.com/auth/calendar "
        		+ "https://www.googleapis.com/auth/userinfo.email "
        		+ "https://www.googleapis.com/auth/userinfo.profile");
        
        ob.addOAuthListener(new OAuthListener() {
        	
        	@Override
        	public void authSuccessful(Token token, boolean isOAuth20) {
        		
        		System.out.println(token.toString());
        		
        		/*
        		    If authentication is successful, use token provided by Google to
        		    create a credential which allows When to access user's profile.
        		    Temporarily using Google+ to show that the authentication and
        		    Retrieval of information has been successful.
        		 */
        		
        		if (token instanceof OAuth2AccessToken) {
        			
        			String oa2 = ((OAuth2AccessToken) token).getAccessToken();
        			
        			// Get a new credential using access token.
        			credential = new GoogleCredential().setAccessToken(oa2);
        			
        			// Set the UI's credential attribute equal to the one we just created,
        			// so that it can be accessed later by LoggedOnView.
        			UI.getCurrent().getSession().setAttribute("credential", credential);
        			
        			// Navigate to LoggedOnView.
					//UI.getCurrent().getNavigator().navigateTo(Constants.URL_LOGGED_ON);
        		    //Actual navigation is handled in FocusListener as navigating here causes an error popup
        		} 
        		
        		else {
        			
        			((OAuth1AccessToken) token).getToken();
        			((OAuth1AccessToken) token).getTokenSecret();
        		}
        	}

        	@Override
        	public void authDenied(String reason) {
        		
        		System.out.println("DENIED");
        	}
        	
        });
        
        ob.addFocusListener(e -> {
				//Navigate to next page if user is logged in
				if (UserManager.IsLoggedIn())
					OnUserLoggedIn();
			});
        
        layout.addComponent(ob);

        //Show "login required" label when user is redirected from another page
        lLoginRequired = new Label("", ContentMode.HTML);
        layout.addComponent(lLoginRequired);
		if (UI.getCurrent().getSession().getAttribute("loginRedirectTarget") != null)
		{
			lLoginRequired.setValue("<font color=\"red\"/>You need to be logged in to access this page");
			ob.click();
		}

		setContent(layout);
	}
	
	//Used to navigate either to default page or to initial destination post-login
	private void OnUserLoggedIn()
	{
		if (UI.getCurrent().getSession().getAttribute("loginRedirectTarget") == null)
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_LOGGED_ON);
		else
		{
			UI.getCurrent().getNavigator().navigateTo((String)UI.getCurrent().getSession().getAttribute("loginRedirectTarget"));
			UI.getCurrent().getSession().setAttribute("loginRedirectTarget", null);
		}
	}
}

