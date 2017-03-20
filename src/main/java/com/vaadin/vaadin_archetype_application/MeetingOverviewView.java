package com.vaadin.vaadin_archetype_application;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
		
		GridLayout gl = new GridLayout(2, 5);//Grid layout for text fields
		gl.setSpacing(true);
		int row = 0;
		
		gl.addComponent(new Label("Title"), 0, row);
		gl.addComponent(new Label(meeting.name), 1, row++);
		
		gl.addComponent(new Label("Meeting start date"), 0, row);
		String s = meeting.startDate.toString();
		gl.addComponent(new Label(s.substring(0, s.indexOf(" "))), 1, row++);
		
		gl.addComponent(new Label("Meeting end date"), 0, row);
		s = meeting.endDate.toString();
		gl.addComponent(new Label(s.substring(0, s.indexOf(" "))), 1, row++);
		
		gl.addComponent(new Label("Meeting duration"), 0, row);
		gl.addComponent(new Label(String.valueOf(meeting.duration.getTime() / 1000 / 60 / (-20)) + " minutes"), 1, row++);
		
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
