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
import com.google.api.services.calendar.Calendar.CalendarList;
import com.google.api.services.calendar.Calendar.Freebusy;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.vaadin.ui.UI;

public class CalendarStuff {
	//test to calculate busy times for a single calendar
		public static void calendarTest() throws IOException, ParseException
		{
			HttpTransport httpTransport = new NetHttpTransport();
	        JsonFactory jsonFactory = new JacksonFactory();
			GoogleCredential credential = (GoogleCredential) UI.getCurrent().getSession().getAttribute("credential");
			
			HttpRequestInitializer initializer = credential;
			
			Calendar calendar = new Calendar(httpTransport,jsonFactory,initializer);
			
			//specify start and end time for busy query
			String dIn = "2017-03-19 00:00:00";
			String dIne = "2017-03-25 23:99:9";
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
			item.setId("whenapp3130@gmail.com");//specify calendar id
			List<FreeBusyRequestItem> list = new ArrayList<FreeBusyRequestItem>();
			list.add(item);
			req.setItems(list);
			
			//query the calendar
			Calendar.Freebusy.Query fbq = calendar.freebusy().query(req);
			FreeBusyResponse fbresponse = fbq.execute();
			
			//print response
			System.out.println(fbresponse.toString());
			
		}
}
