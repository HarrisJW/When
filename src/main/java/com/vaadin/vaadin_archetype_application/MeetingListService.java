package com.vaadin.vaadin_archetype_application;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MeetingListService {

 
    
    private static MeetingListService instance;

    public static MeetingListService createService(String uid) {
        if (instance == null) {

            final MeetingListService meetingListService = new MeetingListService();
            ArrayList<MeetingShortDescription> userMeetings = Controllers.DatabaseConnector.GetMeetingsList(
            		Controllers.DatabaseConnector.GetUserID(uid)
            		);

            for (MeetingShortDescription meetingSD: userMeetings) {
            	MeetingShortDescription meeting = new MeetingShortDescription();
            	meeting.setName(meetingSD.getMeetingName());
            	meeting.setNumAttending(meetingSD.getAttendees());
            	meeting.setState(meetingSD.getState());
            	meeting.setID(meetingSD.getId());
            	System.out.println(meetingSD);
            	System.out.print(meeting);
                meetingListService.save(meeting);
            }
            instance = meetingListService;
        }

        return instance;
    }

    private HashMap<Long, MeetingShortDescription> meetings = new HashMap<>();

    public synchronized List<MeetingShortDescription> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (MeetingShortDescription meetingSD : meetings.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || meetingSD.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(meetingSD.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MeetingListService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<MeetingShortDescription>() {
        	@Override
            public int compare(MeetingShortDescription o1, MeetingShortDescription o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return meetings.size();
    }

    public synchronized void delete(MeetingShortDescription value) {
    	meetings.remove(value.getId());
    }

    public synchronized void save(MeetingShortDescription entry) {
        try {
            entry = (MeetingShortDescription) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        meetings.put((long)entry.getId(), entry);
        System.out.println(meetings.entrySet().toString());
    }

}