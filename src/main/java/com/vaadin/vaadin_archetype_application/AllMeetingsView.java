package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.services.plus.model.Person;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import java.sql.*;

public class AllMeetingsView extends ILoggedInView {
	
	protected GoogleCredential credential;

	@Override
	protected AbstractOrderedLayout InitUI()
	{
		VerticalLayout layout = (VerticalLayout)super.InitUI();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		// As per example at:
		// https://vaadin.com/framework
		List<Meeting> meetingList = Controllers.DatabaseConnector.GetMeetingsList(Controllers.UserID);

		Grid grid = new Grid();
		BeanItemContainer<Meeting> container = new BeanItemContainer<Meeting>(Meeting.class);
		
		// As per example at:
		// https://vaadin.com/forum#!/thread/9934386/9934385
		for (Meeting meeting : meetingList) 
		{
            // First we add the item
            container.addItem(meeting);
        }
		grid.setContainerDataSource(container);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(e -> ItemSelected(e));
		layout.addComponent(grid);

		return layout;
	}
	
	private void ItemSelected(SelectionEvent e)
	{
		Object[] o = e.getSelected().toArray();
		if (o.length == 0)
			return;

		UI.getCurrent().getSession().setAttribute("selectedMeeting", (Meeting)o[0]);
		UI.getCurrent().getNavigator().navigateTo(Constants.URL_MEETING_OVERVIEW);
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
