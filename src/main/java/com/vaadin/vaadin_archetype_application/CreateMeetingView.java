package com.vaadin.vaadin_archetype_application;

import java.util.Date;

import com.vaadin.shared.ui.label.ContentMode;
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
	private TextField tbMeetingCreatorEmailAddress;
	private TextField tbMeetingParticipantEmailAddresses;
	private DateField tbMeetingStartDate;
	private DateField tbMeetingEndDate;
	private DateField tbMeetingDuration;
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
	protected void InitUI()
	{
		tbMeetingName = new TextField();
		tbMeetingStartDate = new DateField();
		tbMeetingEndDate = new DateField();
		tbMeetingDuration = new DateField();
		tbMeetingCreatorEmailAddress = new TextField();
		tbMeetingParticipantEmailAddresses = new TextField();
		tbMeetingPassword = new PasswordField();
		tbConfirmMeetingPassword = new PasswordField();
		
		lErrorMessage = new Label("", ContentMode.HTML);
		
		VerticalLayout layout = new VerticalLayout();//Global layout
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2,8);//Grid layout for text fields
		gl.setSpacing(true);
		
		gl.addComponent(new Label("Meeting Name: "), 0, 0);
		gl.addComponent(tbMeetingName, 1, 0);
		
		gl.addComponent(new Label("Meeting Password: "), 0, 1);
		gl.addComponent(tbMeetingPassword, 1, 1);
		
		gl.addComponent(new Label("Confirm meeting Password: "), 0, 2);
		gl.addComponent(tbConfirmMeetingPassword, 1, 2);
		
		gl.addComponent(new Label("Meeting creator email address: "), 0, 3);
		gl.addComponent(tbMeetingCreatorEmailAddress, 1, 3);
		
		gl.addComponent(new Label("Meeting participant email addresses: "), 0, 4);
		gl.addComponent(tbMeetingParticipantEmailAddresses, 1, 4);
		
		gl.addComponent(new Label("Meeting start date: "), 0, 5);
		gl.addComponent(tbMeetingStartDate, 1, 5);
		
		gl.addComponent(new Label("Meeting end date: "), 0, 6);
		gl.addComponent(tbMeetingEndDate, 1, 6);
		
		gl.addComponent(new Label("Meeting duration: "), 0, 7);
		gl.addComponent(tbMeetingDuration, 1, 7);
		
		layout.addComponent(gl);
		
		// Join meeting button
		Button bCreate = new Button("Create meeting");
		bCreate.addClickListener(e -> OnCreateMeetingButtonClicked());
		layout.addComponent(bCreate);
		
		// Error message label
		layout.addComponent(lErrorMessage);
		
		setContent(layout);
	}

	//Join meeting button click handler. Communicates with DB and changes state according to the return code
	private void OnCreateMeetingButtonClicked()
	
	{
		
		// CreateMeeting() takes:
		// String password, Date startDate, Date endDate, String name, Date duration, long userID
		// Update text entry fields to reflect same.
		
		int code = (int) Controllers.DatabaseConnector.CreateMeeting(tbMeetingPassword.getValue(), 
				tbMeetingStartDate.getValue(), tbMeetingEndDate.getValue(),
				tbMeetingName.getValue(),
				tbMeetingDuration.getValue(),
				Controllers.DatabaseConnector.GetUserID(tbMeetingCreatorEmailAddress.getValue()));
		
		switch (code) {
		
		case 0:
			
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_MEETING_OVERVIEW);	
			
			break;

/*		case Constants.CODE_INVALID_MEETING_ID_PASSWORD:
			setErrorMessage("Invalid ID / Password combination");
			break;

		case Constants.CODE_MEETING_ID_EMPTY:
			setErrorMessage("Meeting ID can't be empty");
			break;
			
		default:
			setErrorMessage("Unknown behavior");
			break;*/
		}	
		
		
	}
	
	//Display error message in red
	private void setErrorMessage(String msg)
	{
		lErrorMessage.setValue("<font color=\"red\"/>" + msg);
	}
	
}
