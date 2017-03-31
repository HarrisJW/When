package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	private static GoogleCredential credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
	//test to calculate busy times for a single calendar
		public static FreeBusyResponse calendarTest() throws IOException, ParseException
		{
			HttpTransport httpTransport = new NetHttpTransport();
	        JsonFactory jsonFactory = new JacksonFactory();
			
			HttpRequestInitializer initializer = credential;
			
			Calendar calendar = new Calendar(httpTransport,jsonFactory,initializer);
			
			//specify start and end time for busy query
			String dIn = "2017-03-27 00:00:00";
			String dIne = "2017-03-28 23:99:99";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date d = df.parse(dIn);
			DateTime startTime = new DateTime(d, TimeZone.getTimeZone("America/Halifax"));

			Date de = df.parse(dIne);
			DateTime endTime = new DateTime(de, TimeZone.getTimeZone("America/Halifax"));

			FreeBusyRequest req = new FreeBusyRequest();
			req.setTimeMin(startTime);
			req.setTimeMax(endTime);
			req.setTimeZone("America/Halifax");
			
			FreeBusyRequestItem item = new FreeBusyRequestItem();
			FreeBusyRequestItem item2 = new FreeBusyRequestItem();
			item2.setId("maxaaronparsons@gmail.com");
			item.setId("whenapp3130@gmail.com");//specify calendar id
			List<FreeBusyRequestItem> list = new ArrayList<FreeBusyRequestItem>();
			list.add(item);
			list.add(item2);
			req.setItems(list);
			
			//query the calendar
			Calendar.Freebusy.Query fbq = calendar.freebusy().query(req);
			FreeBusyResponse fbresponse = fbq.execute();
			
			//print response
			System.out.println(fbresponse.toString());
			getAvailableTimeRanges(fbresponse);
			
			return fbresponse;
		}
		
		/**
		 * Allows a logged in user to share their calendar with another user
		 * @param email email of the user to share with
		 * @throws IOException
		 */
		public static void shareCalendar(String email) throws IOException 
		{
			HttpTransport httpTransport = new NetHttpTransport();
	        JsonFactory jsonFactory = new JacksonFactory();
	        
			Calendar calendar = new Calendar(httpTransport,jsonFactory,credential);
			
			AclRule rule = new AclRule();
			Scope scope = new Scope();
			scope.setType("user");
			scope.setValue("email");
			
			rule.setScope(scope);
			rule.setRole("freeBusyReader");
			AclRule inserted = calendar.acl().insert("primary", rule).execute();
		}
		
		/**
		 * Finds available time ranges for all users
		 * @param fbr The response to the Calendar query i.e. a map that contains busy events
		 * 			  for all users
		 * @return a list of free time periods
		 */
		public static List<TimePeriod> getAvailableTimeRanges(FreeBusyResponse fbr)
		{
			List<TimePeriod> availableTimePeriods = new ArrayList<TimePeriod>();
			
			// For all users represented in the shared calendar...
			// Get an array of user IDs.
			// Note: User IDs are just email addresses.
			Set<String> users = fbr.getCalendars().keySet();
			Object userIds[] = users.toArray();
			
			//fake start and end times for meeting range
			Date start = new Date();
			Date end = new Date();
			
			DateTime startSearchTime = new DateTime(start);
			DateTime endSearchTime = new DateTime(end);
			
			FreeBusyCalendar calendar;
			Map<String,FreeBusyCalendar> userCalendars = fbr.getCalendars();
			while (userCalendars.size() != 0)
			{
				calendar = getCalendarWithEarliestEvent(fbr,userIds);
				TimePeriod timeRange = returnTimeRangeIfEarlier(startSearchTime,calendar);
				if (timeRange != null)
				{
					availableTimePeriods.add(timeRange);
				}
				
			}
			availableTimePeriods.add(returnTimeRangeIfNoEvents(endSearchTime,startSearchTime));
			
			return availableTimePeriods;
		}
		
		//finds the earliest starting event
		public static FreeBusyCalendar getCalendarWithEarliestEvent(FreeBusyResponse fbr,Object userIds[])
		{
			TimePeriod earliestEvent; 
			TimePeriod currentEarliestEvent = null;
			
			FreeBusyCalendar currentUserCalendar = null;
			
			currentUserCalendar = fbr.getCalendars().get(userIds[0]);
			FreeBusyCalendar earliestEventUserCalendar = null;
			earliestEvent = currentUserCalendar.getBusy().get(0);
			
			for (int i = 1; i < fbr.getCalendars().size(); i++) 
			{
				currentUserCalendar = fbr.getCalendars().get(userIds[i]);
				
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
		 * @param startSearchTime the current start of the 
		 * @param userCal the calendar containing the earliest event
		 * @return timeRange either the free time range or null
		 */
		public static TimePeriod returnTimeRangeIfEarlier(DateTime startSearchTime,FreeBusyCalendar userCal)
		{
			//event starts after the start time
			if (startSearchTime.getValue() < userCal.getBusy().get(0).getStart().getValue())
			{
				//make the time period
				TimePeriod timeRange = new TimePeriod();
				timeRange.setEnd(userCal.getBusy().get(0).getStart());
				timeRange.setStart(startSearchTime);
				
				
				//move startSearchTime to END of earliest event and remove event from calendar
				startSearchTime = userCal.getBusy().get(0).getEnd();
				userCal.getBusy().remove(0);
				
				
				//TODO
				//create time period with startSearchTime as the start and earliest event's start time as the end
				return timeRange; //this should be the available time period
			}
			//there is overlap
			else 
			{
				//move startSearchTime to END of earliest event and remove event from calendar
				startSearchTime = userCal.getBusy().get(0).getEnd();
				userCal.getBusy().remove(0);
			}
			
			return null;
		}
		
		/**
		 * Return the free time range once all events have been considered
		 * @param endSearchTime the end of the search period
		 * @param startSearchTime the current start of the search period
		 * @return timeRange the period between startSearchTime and endSearchTime
		 */
		public static TimePeriod returnTimeRangeIfNoEvents(DateTime endSearchTime, DateTime startSearchTime)
		{
			TimePeriod timeRange = new TimePeriod();
			timeRange.setEnd(endSearchTime);
			timeRange.setStart(startSearchTime);
			
			return timeRange;
		}
		
		/**
		 * Remove irrelevant events from the calendar map
		 * @param startSearchTime 
		 * @param userCalendars
		 * @param userIds
		 */
		public static void removeIrrelevantEvents(DateTime startSearchTime, Map<String,FreeBusyCalendar> userCalendars,Object userIds[])
		{
			for (int i = 0; i < userCalendars.size(); i++)
			{
				for (int j = 0; j < userCalendars.size(); i++)
				{
					if (startSearchTime.getValue() >= userCalendars.get(userIds[i]).getBusy().get(j).getEnd().getValue())
					{
						userCalendars.get(userIds[i]).getBusy().remove(j);
					}
				}
			}
		}
}
