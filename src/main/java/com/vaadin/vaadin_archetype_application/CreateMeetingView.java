package com.vaadin.vaadin_archetype_application;

import java.util.Date;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CreateMeetingView extends ILoggedInView {

	private TextField tbMeetingName;
	//private TextField tbMeetingCreatorEmailAddress;
	//private TextField tbMeetingParticipantEmailAddresses;
	private DateField tbMeetingStartDate;
	private DateField tbMeetingEndDate;
	private TextField tbMeetingDuration;
	private PasswordField tbMeetingPassword;
	private PasswordField tbConfirmMeetingPassword;
	private Label lErrorMessage;
	
	@Override
	public String GetPageTitle() {
		
		return "Create Meeting";
	}

	@Override
	public String GetPageURL() {
		
		return Constants.URL_CREATE_MEETING;
	}

	@Override
	protected AbstractOrderedLayout InitUI()
	{
		tbMeetingName = new TextField();
		tbMeetingStartDate = new DateField();
		tbMeetingStartDate.setResolution(DateField.RESOLUTION_MIN);
		tbMeetingEndDate = new DateField();
		tbMeetingEndDate.setResolution(DateField.RESOLUTION_MIN);
		tbMeetingDuration = new TextField();
		//tbMeetingCreatorEmailAddress = new TextField();
		//tbMeetingParticipantEmailAddresses = new TextField();
		tbMeetingPassword = new PasswordField();
		tbConfirmMeetingPassword = new PasswordField();
		
		tbMeetingPassword.setRequired(false);
		tbConfirmMeetingPassword.setRequired(false);
		
		lErrorMessage = new Label("", ContentMode.HTML);
		
		VerticalLayout layout = (VerticalLayout)super.InitUI();//Global layout
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2,6);//Grid layout for text fields
		gl.setSpacing(true);
		int row = 0;
		
		gl.addComponent(new Label("Meeting Name: "), 0, row);
		gl.addComponent(tbMeetingName, 1, row++);
		
		gl.addComponent(new Label("Meeting Password: "), 0, row);
		gl.addComponent(tbMeetingPassword, 1, row++);
		
		gl.addComponent(new Label("Confirm meeting Password: "), 0, row);
		gl.addComponent(tbConfirmMeetingPassword, 1, row++);
		
		gl.addComponent(new Label("Meeting start date: "), 0, row);
		gl.addComponent(tbMeetingStartDate, 1, row++);
		
		gl.addComponent(new Label("Meeting end date: "), 0, row);
		gl.addComponent(tbMeetingEndDate, 1, row++);
		
		gl.addComponent(new Label("Meeting duration (minutes): "), 0, row);
		gl.addComponent(tbMeetingDuration, 1, row++);
		
		layout.addComponent(gl);
		
		// Join meeting button
		Button bCreate = new Button("Create meeting");
		bCreate.addClickListener(e -> OnCreateMeetingButtonClicked());
		layout.addComponent(bCreate);
		
		// Error message label
		layout.addComponent(lErrorMessage);

		return layout;
	}

	//Join meeting button click handler. Communicates with DB and changes state according to the return code
	private void OnCreateMeetingButtonClicked()
	
	{
		
		// CreateMeeting() takes:
		// String password, Date startDate, Date endDate, String name, Date duration, long userID
		// Update text entry fields to reflect same.
		
		if(!tbMeetingPassword.getValue().equals(tbConfirmMeetingPassword.getValue()))
		{
			setErrorMessage("Passwords must match!");
			return;	
		}
		
		long duration;
		try
		{
			duration = new Long(tbMeetingDuration.getValue()) * 60;
		}
		catch (Exception e)
		{
			setErrorMessage("Duration must be a number");
			return;
		}
		if (duration <= 0)
		{
			setErrorMessage("Duration must be a positive number");
			return;
		}
		
		int code = (int) Controllers.DatabaseConnector.CreateMeeting(tbMeetingPassword.getValue(), 
				tbMeetingStartDate.getValue(), tbMeetingEndDate.getValue(),
				tbMeetingName.getValue(),
				duration,
				Controllers.UserID);
		
		if (code > 0)
		{
			Meeting m = Controllers.DatabaseConnector.GetMeetingDescription(code);
			UI.getCurrent().getSession().setAttribute("selectedMeeting", m);
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_MEETING_OVERVIEW);	
		}
		else
		{
			setErrorMessage("Error creating meeting");
			System.out.println("Can't create meeting. Error code " + code);
			return;
		}
	}
	
	//Display error message in red
	private void setErrorMessage(String msg)
	{
		lErrorMessage.setValue("<font color=\"red\"/>" + msg);
	}
	
}
