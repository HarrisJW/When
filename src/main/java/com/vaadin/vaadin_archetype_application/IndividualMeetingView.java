package com.vaadin.vaadin_archetype_application;

public class IndividualMeetingView extends ILoggedInView {

	@Override
	public String GetPageTitle() {
		
		return "Individual Meeting";
	}

	@Override
	public String GetPageURL() {
		
		return Constants.URL_INDIVIDUAL_MEETING;
	}

}
