package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
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
	
	Meeting meeting;
	
	@Override
	protected AbstractOrderedLayout InitUI()
	{
		meeting = (Meeting)UI.getCurrent().getSession().getAttribute("selectedMeeting");
		if (meeting == null)
		{
			UI.getCurrent().getNavigator().navigateTo(Constants.URL_ALL_MEETINGS);
			return null;
		}
		
		/*//TODO move this to JoinMeetingView and share calendar with all members rather than
		 *       only the creator -> to avoid errors
		try {
			CalendarStuff.shareCalendar(meeting.members.get(meeting.members.size()-1).getEmail());
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		VerticalLayout layout = (VerticalLayout)super.InitUI();
		
		GridLayout gl = new GridLayout(2, 7);//Grid layout for text fields
		gl.setSpacing(true);
		int row = 0;

		gl.addComponent(new Label("Title"), 0, row);
		gl.addComponent(new Label(meeting.name), 1, row++);
		
		gl.addComponent(new Label("ID"), 0, row);
		gl.addComponent(new Label(meeting.code), 1, row++);
		
		gl.addComponent(new Label("Password"), 0, row);
		gl.addComponent(new Label(meeting.password), 1, row++);
		
		gl.addComponent(new Label("Meeting start date"), 0, row);
		//String s = new Date(meeting.startDate.getValue()).toString();
		gl.addComponent(new Label(DateTimeToString(meeting.startDate)), 1, row++);
		
		gl.addComponent(new Label("Meeting end date"), 0, row);
		//s = new Date(meeting.endDate.getValue()).toString();
		gl.addComponent(new Label(DateTimeToString(meeting.endDate)), 1, row++);
		
		gl.addComponent(new Label("Meeting duration"), 0, row);
		gl.addComponent(new Label(String.valueOf(meeting.duration / 60) + " minutes"), 1, row++);
		
		gl.addComponent(new Label("Status"), 0, row);
		gl.addComponent(new Label(meeting.getStatus()), 1, row++);
		
		layout.addComponent(gl);
		
		layout.addComponent(new Label("Members:"));

		Grid grid = new Grid();
		BeanItemContainer<MeetingMember> container = new BeanItemContainer<MeetingMember>(MeetingMember.class);
		for (MeetingMember member : meeting.members) 
            container.addItem(member);
		grid.setContainerDataSource(container);
		grid.setSelectionMode(SelectionMode.NONE);
		layout.addComponent(grid);
		
		if (!UserManager.GetCurerntUserMeetingAccess(meeting).equals(UserAccess.Member)) {
			TextField emailField = new TextField();
			emailField.setInputPrompt("Enter users email to invite them...");

			Button emailButton = new Button("Send Email", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					Notification.show("Email sent");
					EmailNotifier.sendCreationMail(emailField.getValue(), meeting.name, meeting.code, meeting.password);
					emailField.clear();
				}

			});
				
			layout.addComponent(emailField);
			layout.addComponent(emailButton);
		}
		
		Button finalize = new Button("Start vote");
		finalize.addClickListener(e -> StartMeetingVote());
		layout.addComponent(finalize);
		
		MeetingCalendar mCal = new MeetingCalendar(meeting);
		mCal.setStartDate();
		mCal.setEndDate();
		mCal.setVisibleHours(6, 20);
		mCal.clear();
		try {
			mCal.addTimeRanges(CalendarStuff.freeBusyQuery(meeting));
		} catch (ParseException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(Exception e1) {e1.printStackTrace();}
		layout.addComponent(mCal.getCalendar());
		
		if (UserManager.GetCurerntUserMeetingAccess(meeting).equals(UserAccess.Creator))
		{
			Button delete = new Button("Delete meeting");
			delete.addClickListener(e -> DeleteMeeting());
			layout.addComponent(delete);
		}
		else
		{
			Button delete = new Button("Leave meeting");
			delete.addClickListener(e -> LeaveMeeting());
			layout.addComponent(delete);
		}

		return layout;
	}
	
	private String DateTimeToString(DateTime dt)
	{
		String s = new Date(dt.getValue()).toString();
		return s.substring(0, s.indexOf(" ", 8)) + " " + s.substring(s.lastIndexOf(" "));
	}
	
	private void StartMeetingVote()//TODO
	{
		
	}
	
	private void DeleteMeeting()//TODO confirmation
	{
		Controllers.DatabaseConnector.DeleteMeeting(meeting.ID);
		UI.getCurrent().getNavigator().navigateTo(Constants.URL_ALL_MEETINGS);
	}
	
	private void LeaveMeeting()//TODO confirmation
	{
		Controllers.DatabaseConnector.LeaveMeeting(meeting.ID, Controllers.UserID);
		UI.getCurrent().getNavigator().navigateTo(Constants.URL_ALL_MEETINGS);
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
