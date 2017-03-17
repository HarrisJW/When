package com.vaadin.vaadin_archetype_application;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AllMeetingsView extends ILoggedInView {

	@Override
	protected void InitUI()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		// As per example at:
		// https://vaadin.com/framework
		
		// List of entities from any Java back end (EJB/JPA/Spring/etc)
		// List<Meeting> meetingList = DatabaseConnector.getMeetings("j.wilfred.harris@gmail.com");
		// Grid<Meeting> grid = new Grid<>(Meeting.class);
		// Create a Grid and bind the Person objects to it
		
		// grid.setItems(meetingList);

		// Define the columns to be displayed
		// grid.setColumns("Start Time", "End Time");
		
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
