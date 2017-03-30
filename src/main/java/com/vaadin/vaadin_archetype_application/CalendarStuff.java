package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.google.api.services.calendar.Calendar.Acl;
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
			getTimeRanges(fbresponse);
			
			return fbresponse;
		}
		
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
		
		public static TimePeriod getTimeRanges(FreeBusyResponse fbr)
		{
			//first time range = db.getmeetingstarttime to getStart()
			//DateTime start = getStart(fbr);
			//System.out.println(start.toString());
			
			return null;
		}
		
		public static FreeBusyCalendar getStart(FreeBusyResponse fbr)
		{
			TimePeriod earliestEvent; // Return this.
			TimePeriod currentEarliestEvent = null;
						
			// For all users represented in the shared calendar...
			// Get an array of user IDs.
			// Note: User IDs are just email addresses.
			Set<String> users = fbr.getCalendars().keySet();
			Object userIds[] = users.toArray();
			
			FreeBusyCalendar currentUserCalendar = null;
			
			currentUserCalendar = fbr.getCalendars().get(userIds[0]);
			FreeBusyCalendar earliestEventUserCalendar = null;
			earliestEvent = currentUserCalendar.getBusy().get(0);
			
			//TODO need to track the calendar with the eariest event and pass to overlap method
			for (int i = 1; i < fbr.getCalendars().size(); i++) 
			{
				currentUserCalendar = fbr.getCalendars().get(userIds[i]);
				
				currentEarliestEvent = currentUserCalendar.getBusy().get(0);
				
				if (currentEarliestEvent.getStart().getValue() < earliestEvent.getStart().getValue()) 
				{
					earliestEventUserCalendar = currentUserCalendar;
					//earliestEvent = currentEarliestEvent;
				}
			}
			
			return earliestEventUserCalendar;
		}
		
		/**
		 * Return an available time range if there is no overlap between startSearchTime and 
		 * the earliest event's start time
		 * @param startSearchTime
		 * @param userCal
		 * @return available Time Range
		 */
		public static TimePeriod ReturnTimeRangeIfNoOverlap(DateTime startSearchTime,FreeBusyCalendar userCal)
		{
			//no overlap
			if (startSearchTime.getValue() < userCal.getBusy().get(0).getStart().getValue())
			{
				//move startSearchTime to START of earliest event
				startSearchTime = userCal.getBusy().get(0).getEnd();
				userCal.getBusy().remove(0);
				//TODO
				//create time period with startSearchTime as the start and earliest event's start time as the end
				return null; //this should be the available time period
			}
			//there is overlap
			else 
			{
				//move startSearchTime to END of earliest event
				startSearchTime = userCal.getBusy().get(0).getEnd();
			}
			
			return null;
		}
}
