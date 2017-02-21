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

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class LoggedOnView extends Panel implements View{
	
	final VerticalLayout layout = new VerticalLayout();
	
	protected GoogleCredential credential;
	
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

	@Override
	public void enter(ViewChangeEvent event) {
		
		// Moved this logic from the constructor, to ensure that it is called when we transition views.
		// Including this logic in the constructor meant that it was called during instantiation
		// of the object when registering it with the navigator (i.e. before most of the information
		// about credentials had been collected).
		
		Page.getCurrent().setTitle("LOGGEDON"); 
		
		setSizeFull();
	
		// Get a local reference to the UI's credential, which was set after successful login.
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		
		addLabel();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
		
	}

}
