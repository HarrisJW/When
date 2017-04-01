package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.Calendar.Acl;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.AclRule.Scope;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;
import com.vaadin.ui.UI;

public class CalendarStuff {
	
	private static GoogleCredential credential = 
			(GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
	private static HttpTransport httpTransport = new NetHttpTransport();
    private static JsonFactory jsonFactory = new JacksonFactory();
    
	private static DateTime startSearchTime;
	private static DateTime endSearchTime;
	private static FreeBusyRequest fbreq;
	private static FreeBusyResponse fbresp;
	private static ArrayList<Object> userIds;
	private static Map<String,FreeBusyCalendar> userCalendars;
	private static List<TimePeriod> availableTimePeriods = new ArrayList<TimePeriod>();
	
	//test to calculate busy times for a single calendar
		public static List<TimePeriod> calendarTest() throws IOException, ParseException
		{	
			HttpRequestInitializer initializer = credential;
			
			Calendar calendar = new Calendar(httpTransport,jsonFactory,initializer);
			
			// Another comment.
			// Fake start and end time for busy query
			// TODO: Replace with valid database calls.
			String dIn = "2017-04-3 00:00:00";
			String dIne = "2017-04-4 23:99:99";
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Date d = df.parse(dIn);
			startSearchTime = new DateTime(d, TimeZone.getTimeZone("America/Halifax"));
			
			Date de = df.parse(dIne);
			endSearchTime = new DateTime(de, TimeZone.getTimeZone("America/Halifax"));

			fbreq = new FreeBusyRequest();
			fbreq.setTimeMin(startSearchTime);
			fbreq.setTimeMax(endSearchTime);
			
			// TODO: Timezone should be provided by database.
			fbreq.setTimeZone("America/Halifax");
			
			// Fake user calendars to query
			// These addresses should result from valid calls to database.
			FreeBusyRequestItem item = new FreeBusyRequestItem();
			FreeBusyRequestItem item2 = new FreeBusyRequestItem();
			FreeBusyRequestItem item3 = new FreeBusyRequestItem();
			
			item.setId("whenapp3130@gmail.com");
			item2.setId("maxaaronparsons@gmail.com");
			//item3.setId("j.wilfred.harris@gmail.com");
			
			List<FreeBusyRequestItem> list = new ArrayList<FreeBusyRequestItem>();
			list.add(item);
			list.add(item2);
			//list.add(item3); //button clicker needs everybody's calendar before querying
			
			// TODO: list should result from valid call to database.
			fbreq.setItems(list);
			
			//query the calendar
			Calendar.Freebusy.Query fbq = calendar.freebusy().query(fbreq);
			fbresp = fbq.execute();
			
			//grab the Map containing user emails and calendars
			userCalendars = fbresp.getCalendars();
			
			//grab the user ids (their email addresses)
			userIds = new ArrayList<Object>(Arrays.asList(userCalendars.keySet().toArray()));

			//grab and print available time ranges
			getAvailableTimeRanges();
			
			return availableTimePeriods;
		}
		
		/**
		 * Finds available time ranges and adds them to a list
		 * @return said list
		 */
		
		public static void getAvailableTimeRanges()
		{	
			FreeBusyCalendar calendar;
			//there are still events 
			while (!userCalendars.isEmpty())
			{
				//find calendar with earliest event
				calendar = getCalendarWithEarliestEvent();
				
				//find if that calendar's earliest event conflicts with the startSearchTime
				TimePeriod timeRange = returnTimeRangeIfEarlier(calendar);
				
				//if no conflict then add time range to list
				if (timeRange != null)
				{
					availableTimePeriods.add(timeRange);
				}
				//check for and prune empty calendars
				pruneCalendarsAndIds();
			}
			//add a time range if there are no events left/to begin with
			availableTimePeriods.add(returnTimeRangeIfNoEvents());
		}
		
		/**
		 * Finds the calendar with the earliest event
		 * @return said calendar
		 */
		public static FreeBusyCalendar getCalendarWithEarliestEvent()
		{
			TimePeriod earliestEvent; 
			TimePeriod currentEarliestEvent;
			
			FreeBusyCalendar earliestEventUserCalendar;
			FreeBusyCalendar currentUserCalendar;
			
			earliestEventUserCalendar = userCalendars.get(userIds.get(0));
			earliestEvent = earliestEventUserCalendar.getBusy().get(0);
			
			for (int i = 1; i < userCalendars.size(); i++) 
			{
				currentUserCalendar = userCalendars.get(userIds.get(i));
				currentEarliestEvent = currentUserCalendar.getBusy().get(0);
				
				if (currentEarliestEvent.getStart().getValue() < earliestEvent.getStart().getValue()) 
				{
					earliestEvent = currentEarliestEvent;
					earliestEventUserCalendar = currentUserCalendar;
				}
			}
			
			return earliestEventUserCalendar;
		}
		
		/**
		 * Return an available time range if the earliest event starts after the 
		 * startSearchTime 
		 * @param startSearchTime the current start of the search period
		 * @param userCal the calendar containing the earliest event
		 * @return timeRange either the free time range or null
		 */
		public static TimePeriod returnTimeRangeIfEarlier(FreeBusyCalendar userCal)
		{
			//event starts after the start time
			if (startSearchTime.getValue() < userCal.getBusy().get(0).getStart().getValue())
			{
				//make the time period
				TimePeriod timeRange = new TimePeriod();
				timeRange.setStart(startSearchTime);
				timeRange.setEnd(userCal.getBusy().get(0).getStart());
				
				//move startSearchTime to END of earliest event and remove event from calendar
				startSearchTime = userCal.getBusy().get(0).getEnd();
				userCal.getBusy().remove(0);
				
				return timeRange; //this should be the available time period
			}
			//startSearchTime is inside the earliest event's time period
			else if (userCal.getBusy().get(0).getStart().getValue() <= startSearchTime.getValue()
				&& userCal.getBusy().get(0).getEnd().getValue() > startSearchTime.getValue())
			{
				//move startSearchTime to END of earliest event and remove event from calendar
				startSearchTime = userCal.getBusy().get(0).getEnd();
				userCal.getBusy().remove(0);
			}
			//the earliest event ends before the startSearchTime
			else
			{
				userCal.getBusy().remove(0); //remove event from calendar
			}
			
			return null;
		}
		
		/**
		 * Return a free time range if there are no events
		 * @param endSearchTime the end of the search period
		 * @param startSearchTime the current start of the search period
		 * @return timeRange 
		 */
		public static TimePeriod returnTimeRangeIfNoEvents()
		{
			TimePeriod timeRange = new TimePeriod();
			timeRange.setEnd(endSearchTime);
			timeRange.setStart(startSearchTime);
			
			return timeRange;
		}
		
		/**
		 * Cuts away empty calendars and their associated Ids
		 */
		public static void pruneCalendarsAndIds()
		{
			for (int i = 0; i < userCalendars.size(); i++)
			{	
				if (userCalendars.get(userIds.get(i)).getBusy().isEmpty())
				{
					userCalendars.remove(userIds.get(i));
					userIds.remove(i);
					i--;
				}
			}
		}
		
		/**
		 * Allows a logged in user to share their calendar with another user
		 * @param email email of the user to share with
		 * @throws IOException
		 */
		public static void shareCalendar(String email) throws IOException 
		{   
			HttpRequestInitializer initializer = credential;
			
			Calendar calendar = new Calendar(httpTransport,jsonFactory,initializer);
			
			AclRule rule = new AclRule();
			Scope scope = new Scope();
			scope.setType("user");
			scope.setValue(email);
			
			rule.setScope(scope);
			rule.setRole("freeBusyReader");
			AclRule inserted = calendar.acl().insert("primary", rule).execute();
		}
}
