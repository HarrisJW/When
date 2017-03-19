package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.services.plus.model.Person;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
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
	MeetingForm meetingForm = new MeetingForm();
    Grid meetingList = new Grid();
    Button newTask = new Button("New task");
    MeetingListService meetingListService;
    



	@Override
	protected void InitUI()
	{

		// Get reference to active user so that we can get their meetings.
		Plus plus = null;
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
		try {
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Get a reference to current user's profile.
		Person profile = null;
		try {
			profile = plus.people().get("me").execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		meetingListService = MeetingListService.createService(profile.getEmails().get(0).getValue());
		
        configureComponents();
        buildLayout();
		
		
		// Controllers.DatabaseConnector.CreateUser("Jonathan", "Harris", "j.wilfred.harris@gmail.com", "j.wilfred.harris");
		
//		Controllers.DatabaseConnector.CreateMeeting("Cats", new Date((new java.util.Date()).getTime()), 
//				new Date((new java.util.Date()).getTime()), 
//				"Test Meeting",
//				new Date((new java.util.Date()).getTime()), 
//				Controllers.DatabaseConnector.GetUserID("j.wilfred.harris@gmail.com"));

      
			
	}
	
    private void configureComponents() {

        meetingList.setContainerDataSource(new BeanItemContainer<>(MeetingShortDescription.class));
        meetingList.setSelectionMode(Grid.SelectionMode.SINGLE);
        meetingList.addSelectionListener(
                e -> meetingForm.edit((MeetingShortDescription) meetingList.getSelectedRow()));
        refreshMeetings();
    }
    
    private void buildLayout() {
        VerticalLayout left = new VerticalLayout(meetingList);
        left.setSizeFull();
        meetingList.setSizeFull();
        left.setExpandRatio(meetingList, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, meetingForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        setContent(mainLayout);
    }
	
	@Override
	public String GetPageTitle() {
		
		return "All Meetings";
	}

	@Override
	public String GetPageURL() {
		
		return Constants.URL_ALL_MEETINGS;
	}
	
    void refreshMeetings() {
        meetingList.setContainerDataSource(new BeanItemContainer<>(
        		MeetingShortDescription.class, meetingListService.findAll(null))
        		);
        meetingForm.setVisible(false);
    }

}
