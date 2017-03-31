package com.vaadin.vaadin_archetype_application;

import java.util.Date;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.vaadin_archetype_application.MeetingMember.UserAccess;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;

// Meeting overview page
public class MeetingOverviewView extends ILoggedInView {
	
	@Override
	protected void InitUI()
	{
		Meeting meeting = (Meeting)UI.getCurrent().getSession().getAttribute("selectedMeeting");
		if (meeting == null)
		{
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_ALL_MEETINGS);
			return;
		}
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		GridLayout gl = new GridLayout(2, 7);//Grid layout for text fields
		gl.setSpacing(true);
		int row = 0;
		
		TextField emailField = new TextField();
		emailField.setInputPrompt("Enter users email to invite them...");

		Button emailButton = new Button("Send Email",
				new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Email sent");
				EmailNotifier.sendCreationMail(emailField.getValue(), meeting.name, 
						meeting.code, meeting.password);
				emailField.clear();
			}

		});

		gl.addComponent(new Label("Title"), 0, row);
		gl.addComponent(new Label(meeting.name), 1, row++);
		
		gl.addComponent(new Label("ID"), 0, row);
		gl.addComponent(new Label(meeting.code), 1, row++);
		
		gl.addComponent(new Label("Password"), 0, row);
		gl.addComponent(new Label(meeting.password), 1, row++);
		
		gl.addComponent(new Label("Meeting start date"), 0, row);
		String s = new Date(meeting.startDate.getValue()).toString();
		gl.addComponent(new Label(s.substring(0, s.indexOf(" "))), 1, row++);
		
		gl.addComponent(new Label("Meeting end date"), 0, row);
		s = new Date(meeting.endDate.getValue()).toString();
		gl.addComponent(new Label(s.substring(0, s.indexOf(" "))), 1, row++);
		
		gl.addComponent(new Label("Meeting duration"), 0, row);
		gl.addComponent(new Label(String.valueOf(meeting.duration / 60) + " minutes"), 1, row++);
		
		gl.addComponent(new Label("Status"), 0, row);
		gl.addComponent(new Label(meeting.getStatus()), 1, row++);
		
		for (MeetingMember member : meeting.members) {
			if (member.ID == Controllers.UserID && member.access.equals(UserAccess.Creator)) {
				layout.addComponent(emailField);
				layout.addComponent(emailButton);
			}
		}
		
		layout.addComponent(gl);
		
		layout.addComponent(new Label("Members:"));

		Grid grid = new Grid();
		BeanItemContainer<MeetingMember> container = new BeanItemContainer<MeetingMember>(MeetingMember.class);
		for (MeetingMember member : meeting.members) 
            container.addItem(member);
		grid.setContainerDataSource(container);
		grid.setSelectionMode(SelectionMode.NONE);
		layout.addComponent(grid);
		
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
