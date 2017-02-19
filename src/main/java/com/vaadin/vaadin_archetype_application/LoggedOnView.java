package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class LoggedOnView extends Panel implements View{
	
	final VerticalLayout layout = new VerticalLayout();
	protected GoogleCredential credential;
	
	public LoggedOnView() {
		Page.getCurrent().setTitle("Login");
		setSizeFull();
		addLabel();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);
	}
	
	public void addLabel() {
		layout.addComponent(new Label("has logged in!"));
		Plus plus = null;
		try {
			plus = new Plus.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("When").build();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (plus!=null) {
			try {
				Person profile = plus.people().get("me").execute();
				layout.addComponent(new Label(profile.getDisplayName() + " has logged in!"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Page.getCurrent().setTitle("LOGGEDON"); 
		credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
	}

}
