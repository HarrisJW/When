package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class LoggedOnView extends ILoggedInView {
	
	//protected VerticalLayout layout;
	protected GoogleCredential credential;
	private GridLayout grid;
	


	@Override
	public String GetPageTitle() {
		return "Logged on";
	}

	@Override
	public String GetPageURL() {
		return Constants.URL_LOGGED_ON;
	}
	
	@Override
	protected AbstractOrderedLayout InitUI()
	{
		// Get a local reference to the UI's credential, which was set after successful login.
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		
		layout = (VerticalLayout)super.InitUI();
		grid = new GridLayout(5, 5);
		grid.setWidth(400, Sizeable.UNITS_PIXELS);
		grid.setHeight(200, Sizeable.UNITS_PIXELS);
		layout.setSpacing(true);
		layout.setMargin(true);
		
		addLogoutButton();
		addLabel();
		addMeetingButton();
		addCreateMeetingButton();
		addJoinMeetingButton();
		
		layout.addComponent(grid);
		layout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
		return layout;

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
		layout.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
	}
	public void addMeetingButton(){
		Button meetingButton = new Button("My Meetings",
                new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().getNavigator().navigateTo(Constants.URL_ALL_MEETINGS);
						
					}
		});
		
		grid.addComponent(meetingButton, 2, 2);
		grid.setComponentAlignment(meetingButton, Alignment.MIDDLE_CENTER);
	}
	
	//*****************************************************************************
	// Create meeting button.
	//*****************************************************************************
	
	public void addCreateMeetingButton() {
		Button bCreateMeeting = new Button("Create New Meeting");
		bCreateMeeting.setId("CreateMeeting");
		bCreateMeeting.addClickListener(new ClickListener() {
			
			// Join meeting button click handler. Communicates with DB and changes state according to the return code
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(Constants.URL_CREATE_MEETING);
			}
		});
		
		layout.addComponent(bCreateMeeting);
		grid.addComponent(bCreateMeeting, 2, 3);
		grid.setComponentAlignment(bCreateMeeting, Alignment.MIDDLE_CENTER);
	}
	
	//*****************************************************************************
	// Join meeting button.
	//*****************************************************************************
	
	public void addJoinMeetingButton() {
		Button bJoinMeeting = new Button("Join meeting");
		bJoinMeeting.setId("JoinMeeting");
		bJoinMeeting.addClickListener(new ClickListener() {

			// Join meeting button click handler. Communicates with DB and changes state according to the return code
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(Constants.URL_JOIN_MEETING);
			}
		});

		grid.addComponent(bJoinMeeting, 2, 4);
		grid.setComponentAlignment(bJoinMeeting, Alignment.MIDDLE_CENTER);
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
				Label welcomeMsg = new Label("Welcome, " + profile.getDisplayName() + "!");
				grid.addComponent(welcomeMsg, 2, 1);
				grid.setComponentAlignment(welcomeMsg, Alignment.MIDDLE_CENTER);
				
			} 
			
			catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	}

}
