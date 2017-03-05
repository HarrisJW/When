package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class LoggedOnView extends ILoggedInView {
	
	protected VerticalLayout layout;
	protected GoogleCredential credential;
	


	@Override
	public String GetPageTitle() {
		return "Logged on";
	}

	@Override
	public String GetPageURL() {
		return Constants.URL_LOGGED_ON;
	}
	
	@Override
	protected void InitUI()
	{
		// Get a local reference to the UI's credential, which was set after successful login.
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		addLogoutButton();
		addMeetingButton();
		addLabel();
		
		setContent(layout);
	}
	
	public void addLogoutButton(){
		Button logoutButton = new Button("Logout",
                new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
						getUI().getSession().close();
						getUI().getPage().setLocation("");//Use this instead of the line below to avoid "Session expired" message
						//UI.getCurrent().getNavigator().navigateTo("");
						
					}
		});
		
		layout.addComponent(logoutButton);
		layout.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);
	}
	public void addMeetingButton(){
		Button meetingButton = new Button("Meeting",
                new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().getNavigator().navigateTo(Constants.URL_MEETING_OVERVIEW);
						
					}
		});
		
		layout.addComponent(meetingButton);
		layout.setComponentAlignment(meetingButton, Alignment.MIDDLE_CENTER);
	}
	

	public void addLabel() {
		
		Plus plus = null;
		
		try {
			
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		} 
		
		catch (GeneralSecurityException e) {
			
			e.printStackTrace();
		} 
		
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		if (plus!=null) {
			
			try {
				
				// Get a reference to current user's profile.
				Person profile = plus.people().get("me").execute();
				
				//Display user's name...
				layout.addComponent(new Label(profile.getDisplayName() + " has logged in!"));
				
				// ... and email information.
				layout.addComponent(new Label(profile.getEmails().toString()));
				
			} 
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	}

}
