package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.services.plus.model.Person;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.vaadin_archetype_application.MeetingShortDescription;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import java.sql.*;

public class AllMeetingsView extends ILoggedInView {
	
	protected GoogleCredential credential;

	@Override
	protected void InitUI()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		// Get reference to active user so that we can get their meetings.
		Plus plus = null;
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		try {
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get a reference to current user's profile.
		Person profile = null;
		try {
			profile = plus.people().get("me").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// As per example at:
		// https://vaadin.com/framework
		
		// List of Meetings from database connector.
		//List<MeetingShortDescription> meetingList = Controllers.DatabaseConnector.GetMeetingsList(
		//		Controllers.DatabaseConnector.GetUserID(profile.getEmails().toString()));
		
		// Controllers.DatabaseConnector.CreateUser("Jonathan", "Harris", "j.wilfred.harris@gmail.com", "j.wilfred.harris");
		
		Controllers.DatabaseConnector.CreateMeeting("Cats", new Date((new java.util.Date()).getTime()), 
				new Date((new java.util.Date()).getTime()), 
				"Test Meeting",
				new Date((new java.util.Date()).getTime()), 
				Controllers.DatabaseConnector.GetUserID("j.wilfred.harris@gmail.com"));
			
		
		
		List<MeetingShortDescription> meetingList = Controllers.DatabaseConnector.GetMeetingsList(
				Controllers.DatabaseConnector.GetUserID("j.wilfred.harris@gmail.com"));
		

		Grid grid = new Grid();
		
		IndexedContainer indexedContainer = new IndexedContainer();
		// indexedContainer.addContainerProperty("ID", long.class, null);
		indexedContainer.addContainerProperty("name", String.class, null);
		indexedContainer.addContainerProperty("state", Byte.class, null);
		indexedContainer.addContainerProperty("numberOfMembers", Integer.class, null);
		
		for(MeetingShortDescription meeting : meetingList){
			
			indexedContainer.addItem(meeting);
			// indexedContainer.getContainerProperty(meeting, "ID").setValue(meeting.ID);
			indexedContainer.getContainerProperty(meeting, "name").setValue(meeting.name);
			indexedContainer.getContainerProperty(meeting, "state").setValue(meeting.state);
			indexedContainer.getContainerProperty(meeting, "numberOfMembers").setValue(meeting.numberOfMembers);
		}
		
		
		
		// Grid<MeetingShortDescription> grid = new Grid<>(MeetingShortDescription);
		
		// grid.setContainerDataSource(new BeanItemContainer<>(MeetingShortDescription.class, meetingList));
		
//		BeanItemContainer<MeetingShortDescription> container = 
//				new BeanItemContainer<MeetingShortDescription>(MeetingShortDescription.class);
//		
//		BeanItemContainer<MeetingShortDescription> container = 
//				new BeanItemContainer<>(MeetingShortDescription.class);
	
//		BeanItemContainer<MeetingShortDescription> container = 
//		new BeanItemContainer<>(MeetingShortDescription.class);
		
		// As per example at:
		// https://vaadin.com/forum#!/thread/9934386/9934385
//		for (MeetingShortDescription meeting : meetingList) {
//
//            // First we add the item
//            container.addItem(meeting);
//
//        }
		
		// container.addItem(meetingList);
		//grid.setContainerDataSource(container);
		
		// Create a Grid and bind the Meeting objects to it
		// grid.setItems(meetingList);
	
		// Define the columns to be displayed
		grid.setContainerDataSource(indexedContainer);
		
		// System.out.println(grid.getColumns().toString());
		// grid.setColumns("ID", "state");
		// grid.setColumnOrder("ID", "name");
		
		layout.addComponent(grid);
		
		setContent(layout);
	}
	
	@Override
	public String GetPageTitle() {
		
		return "All Meetings";
	}

	@Override
	public String GetPageURL() {
		
		return Constants.URL_ALL_MEETINGS;
	}

}
