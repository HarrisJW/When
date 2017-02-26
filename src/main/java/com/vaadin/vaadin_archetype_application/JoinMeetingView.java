package com.vaadin.vaadin_archetype_application;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

//Join meeting page
public class JoinMeetingView extends Panel implements View {
	private TextField tbMeetingID;
	private PasswordField tbMeetingPassword;
	private Label lErrorMessage;
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		Page.getCurrent().setTitle("Join meeting");
		setSizeFull();
		initUI();
	}
	
	
	
	//Create all UI elements and set button click handler
	private void initUI()
	{
		tbMeetingID = new TextField();
		tbMeetingPassword = new PasswordField();
		lErrorMessage = new Label("", ContentMode.HTML);
		
		VerticalLayout layout = new VerticalLayout();//Global layout
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2, 2);//Grid layout for text fields
		gl.setSpacing(true);
		
		gl.addComponent(new Label("Meeting ID: "), 0, 0);
		gl.addComponent(tbMeetingID, 1, 0);
		gl.addComponent(new Label("Meeting Password: "), 0, 1);
		gl.addComponent(tbMeetingPassword, 1, 1);
		layout.addComponent(gl);
		
		Button bJoin = new Button("Join meeting");
		bJoin.addClickListener(new ClickListener() {
			
			//Join meeting button click handler. Communicates with DB and changes state according to the return code
			@Override
			public void buttonClick(ClickEvent event) {
				int code = DatabaseConnector.TryJoinMeeting(tbMeetingID.getValue(), tbMeetingPassword.getValue());
				System.out.println("TryJoinMeeting returned code " + String.valueOf(code));
				switch (code) {
				case 0:
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
		});
		layout.addComponent(bJoin);
		
		layout.addComponent(lErrorMessage);
		
		setContent(layout);
	}
	
	//Display error message in red
	private void setErrorMessage(String msg)
	{
		lErrorMessage.setValue("<font color=\"red\"/>" + msg);
	}
}
