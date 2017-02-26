package com.vaadin.vaadin_archetype_application;

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
public class MeetingOverviewView extends Panel implements View {

	@Override
	public void enter(ViewChangeEvent event)
	{
		Page.getCurrent().setTitle("Join meeting");
		setSizeFull();
		initUI();
	}

	private void initUI()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		layout.addComponent(new Label("//TODO"));
		
		setContent(layout);
	}
}
