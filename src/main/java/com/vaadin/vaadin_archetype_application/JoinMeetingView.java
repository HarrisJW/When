package com.vaadin.vaadin_archetype_application;

import java.io.IOException;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

//Join meeting page
public class JoinMeetingView extends ILoggedInView {
	private TextField tbMeetingID;
	private PasswordField tbMeetingPassword;
	private Label lErrorMessage;

	@Override
	public String GetPageTitle() {
		return "Join meeting";
	}
	
	@Override
	public String GetPageURL() {
		return Constants.URL_JOIN_MEETING;
	}
	
	
	
	//Create all UI elements and set button click handler
	@Override
	protected AbstractOrderedLayout InitUI()
	{
		tbMeetingID = new TextField();
		tbMeetingPassword = new PasswordField();
		lErrorMessage = new Label("", ContentMode.HTML);
		
		VerticalLayout layout = (VerticalLayout)super.InitUI();//Global layout
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2, 2);//Grid layout for text fields
		gl.setSpacing(true);
		
		gl.addComponent(new Label("Meeting ID: "), 0, 0);
		gl.addComponent(tbMeetingID, 1, 0);
		gl.addComponent(new Label("Meeting Password: "), 0, 1);
		gl.addComponent(tbMeetingPassword, 1, 1);
		layout.addComponent(gl);
		
		//Join meeting button
		Button bJoin = new Button("Join meeting");
		bJoin.addClickListener(e -> OnJoinMeetingButtonClicked());
		layout.addComponent(bJoin);
		
		//Error message label
		layout.addComponent(lErrorMessage);

		return layout;
	}

	//Join meeting button click handler. Communicates with DB and changes state according to the return code
	private void OnJoinMeetingButtonClicked()
	{
		int code = Controllers.DatabaseConnector.TryJoinMeeting(tbMeetingID.getValue(), tbMeetingPassword.getValue(), Controllers.UserID);
		System.out.println("TryJoinMeeting returned code " + String.valueOf(code));
		switch (code) {
		case 0:
			//TODO Database needs to store member EMAILS!
			try {
				CalendarStuff.shareCalendar("j.wilfred.harris@gmail.com");
			} catch (IOException e) {
				e.printStackTrace();
			}
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_MEETING_OVERVIEW);	
			break;

		case Constants.CODE_INVALID_MEETING_ID_PASSWORD:
			setErrorMessage("Invalid ID / Password combination");
			break;

		case Constants.CODE_MEETING_ID_EMPTY:
			setErrorMessage("Meeting ID can't be empty");
			break;
			
		default:
			setErrorMessage("Unknown behavior");
			break;
		}
	}
	
	//Display error message in red
	private void setErrorMessage(String msg)
	{
		lErrorMessage.setValue("<font color=\"red\"/>" + msg);
	}
}
