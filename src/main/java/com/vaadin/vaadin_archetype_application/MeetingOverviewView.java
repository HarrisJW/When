package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.ParseException;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

//Meeting overview page
//TODO
public class MeetingOverviewView extends ILoggedInView {

	@Override
	protected void InitUI()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		layout.addComponent(new Label("//TODO"));
		
		//test for busy calendar times
		try {
			CalendarStuff.calendarTest();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContent(layout);
	}

	@Override
	public String GetPageTitle() {
		return "Meeting overview";
	}

	@Override
	public String GetPageURL() {
		return Constants.URL_MEETING_OVERVIEW;
	}
}
