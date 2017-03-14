package com.vaadin.vaadin_archetype_application;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class CreateMeetingView extends ILoggedInView {

	private TextField tbMeetingName;
	private TextField tbMeetingParticipantEmailAddresses;
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
		tbMeetingParticipantEmailAddresses = new TextField();
		tbMeetingPassword = new PasswordField();
		tbConfirmMeetingPassword = new PasswordField();
		
		lErrorMessage = new Label("", ContentMode.HTML);
		
		VerticalLayout layout = new VerticalLayout();//Global layout
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2,4);//Grid layout for text fields
		gl.setSpacing(true);
		
		gl.addComponent(new Label("Meeting Name: "), 0, 0);
		gl.addComponent(tbMeetingName, 1, 0);
		
		gl.addComponent(new Label("Meeting Password: "), 0, 1);
		gl.addComponent(tbMeetingPassword, 1, 1);
		
		gl.addComponent(new Label("Confirm meeting Password: "), 0, 2);
		gl.addComponent(tbConfirmMeetingPassword, 1, 2);
		
		gl.addComponent(new Label("Meeting participant email addresses: "), 0, 3);
		gl.addComponent(tbMeetingParticipantEmailAddresses, 1, 3);
		
		layout.addComponent(gl);
		
		// Join meeting button
		Button bCreate = new Button("Create meeting");
		bCreate.addClickListener(e -> OnJoinMeetingButtonClicked());
		layout.addComponent(bCreate);
		
		// Error message label
		layout.addComponent(lErrorMessage);
		
		setContent(layout);
	}

	//Join meeting button click handler. Communicates with DB and changes state according to the return code
	private void OnJoinMeetingButtonClicked()
	
	{
		int code = DatabaseConnector.tryCreateMeeting(tbMeetingName.getValue(), 
				tbMeetingPassword.getValue(),
				tbConfirmMeetingPassword.getValue(),
				tbMeetingParticipantEmailAddresses.getValue());
		
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
