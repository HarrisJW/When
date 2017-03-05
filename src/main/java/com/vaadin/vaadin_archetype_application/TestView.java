package com.vaadin.vaadin_archetype_application;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

//This will not be in final release
//This view is used to quickly navigate to different parts of app during development
public class TestView extends Panel implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("Debug view");
		setSizeFull();
		InitUI();
	}

	//Create all UI elements and set button click handler
	private void InitUI()
	{
		
		VerticalLayout layout = new VerticalLayout();//Global layout
		layout.setSpacing(true);
		layout.setMargin(true);
		
		Button bLogin = new Button("Login");
		bLogin.addClickListener(new ClickListener() {
			
			//Join meeting button click handler. Communicates with DB and changes state according to the return code
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(Constants.URL_LOGIN);
			}
		});
		layout.addComponent(bLogin);

		Button bJoinMeeting = new Button("Join meeting");
		bJoinMeeting.addClickListener(new ClickListener() {
			
			//Join meeting button click handler. Communicates with DB and changes state according to the return code
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(Constants.URL_JOIN_MEETING);
			}
		});
		layout.addComponent(bJoinMeeting);
		
		setContent(layout);
	}

}
