package com.vaadin.vaadin_archetype_application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
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
		
		
}
