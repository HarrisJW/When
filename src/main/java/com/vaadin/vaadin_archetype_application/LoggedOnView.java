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

public class LoggedOnView extends Panel implements View{
	
	final VerticalLayout layout;
	
	protected GoogleCredential credential;
	
	public LoggedOnView(){
		layout = new VerticalLayout();
		addLogoutButton();
	}
	
	public void addLogoutButton(){
		Button logoutButton = new Button("Logout",
                new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().getNavigator().navigateTo("");
						credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("");
					}
		});
		
		layout.addComponent(logoutButton);
		layout.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);
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
