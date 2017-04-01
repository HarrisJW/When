package com.vaadin.vaadin_archetype_application;

import com.google.api.client.util.DateTime;

public class TimeSlot {
	public long id;
	public DateTime startTime, endTime;
	public TimeSlotVote[] votes;
}
