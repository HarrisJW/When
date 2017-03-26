package com.vaadin.vaadin_archetype_application.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.vaadin.vaadin_archetype_application.DBProvider;
import com.vaadin.vaadin_archetype_application.DatabaseConnector;
import com.vaadin.vaadin_archetype_application.Meeting;
import com.vaadin.vaadin_archetype_application.Meeting.MeetingState;
import com.vaadin.vaadin_archetype_application.MeetingMember;
import com.vaadin.vaadin_archetype_application.MeetingMember.UserAccess;
import com.vaadin.vaadin_archetype_application.TimeRange;
import com.vaadin.vaadin_archetype_application.TimeSlot;
import com.vaadin.vaadin_archetype_application.TimeSlotVote;

public class DatabaseConnectorTest {
	
	//*
	@Test
	public void tryOpenConnection()
	{
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		assertEquals(true, dbc.IsConnected());
		dbc.Dispose();
	}
	
	@Test
	public void UselessAnnoyingTest()
	{
		//TODO delete everything if a test fails
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE cid = 'asdf@qwer.ty';"));
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE cid = '2asdf@qwer.ty';"));
		
		//Create and get users
		long uid1 = dbc.CreateUser("asdf", "qwer", "asdf@qwer.ty", "asdfafgdfg");
		assertNotSame(-1, uid1);
		assertEquals(uid1, dbc.GetUserID("asdf@qwer.ty"));

		long uid2 = dbc.CreateUser("2asdf", "2qwer", "2asdf@qwer.ty", "2asdfafgdfg");
		assertNotSame(-1, uid2);
		assertEquals(uid2, dbc.GetUserID("2asdf@qwer.ty"));
		
		//Create meetings
		Date d1 = new Date();
		long mid1 = dbc.CreateMeeting("", d1, d1, "m1", 60*60*2, uid1);
		assertNotSame(-1, mid1);
		
		long mid2 = dbc.CreateMeeting("asdf", d1, d1, "m2", 60*60*3, uid2);
		assertNotSame(-1, mid2);
		
		System.out.println(uid1);
		System.out.println(mid1);
		
		//Get meeting description
		Meeting md1 = dbc.GetMeetingDescription(mid1);
		Meeting md2 = dbc.GetMeetingDescription(mid2);
		assertEquals(mid1, md1.ID);
		assertEquals("m1", md1.name);
		assertEquals(MeetingState.Setup, md1.state);
		assertEquals(1, md1.membersCount);
		assertEquals(1, md1.members.size());
		assertEquals(60*60*2, md1.duration);
		assertEquals("", md1.password);
		
		System.out.println(md1.code);
		System.out.println(md2.code);
		
		//Join meetings
		assertNotSame(0, dbc.TryJoinMeeting(md2.code, "", uid1));
		assertEquals(0, dbc.TryJoinMeeting(md2.code, "asdf", uid1));
		assertNotSame(0, dbc.TryJoinMeeting(md2.code, "asdf", uid1));
		assertNotSame(0, dbc.TryJoinMeeting(md1.code, "", uid1));
		
		//Get meetings list
		ArrayList<Meeting> msd = dbc.GetMeetingsList(uid1);
		assertEquals(2, msd.size());
		assertEquals(mid1, msd.get(0).ID);
		assertEquals(mid2, msd.get(1).ID);
		assertEquals("m1", msd.get(0).name);
		assertEquals("m2", msd.get(1).name);
		assertEquals(MeetingState.Setup, msd.get(0).state);
		assertEquals(MeetingState.Setup, msd.get(1).state);
		assertEquals(1, msd.get(0).membersCount);
		assertEquals(2, msd.get(1).membersCount);
		assertEquals(1, msd.get(0).members.size());
		assertEquals(2, msd.get(1).members.size());
		
		//Get meeting members
		ArrayList<MeetingMember> mem = dbc.GetMeetingMembers(mid2);
		assertEquals(2, mem.size());
		assertEquals(UserAccess.Member, mem.get(0).access);
		assertEquals(UserAccess.Creator, mem.get(1).access);
		assertEquals("asdf", mem.get(0).firstName);
		assertEquals("2asdf", mem.get(1).firstName);
		assertEquals(uid1, mem.get(0).ID);
		assertEquals(uid2, mem.get(1).ID);
		assertEquals("qwer", mem.get(0).lastName);
		assertEquals("2qwer", mem.get(1).lastName);
		
		//Update meeting information
		Date d2 = new Date();
		assertEquals(true, dbc.UpdateMeetingTime(mid1, d2, d2, 1000));
		md1 = dbc.GetMeetingDescription(mid1);
		assertEquals(mid1, md1.ID);
		assertEquals("m1", md1.name);
		assertEquals(MeetingState.Setup, md1.state);
		
		//Meeting state update
		assertEquals(true, dbc.SetMeetingState(mid1, Meeting.MeetingState.TimeRangeSelection.ordinal()));
		md1 = dbc.GetMeetingDescription(mid1);
		assertEquals(Meeting.MeetingState.TimeRangeSelection, md1.state);
		
		
		
		//Add time ranges
		assertEquals(true, dbc.AddAvailableTimeRange(mid1, d1, d2));
		assertEquals(true, dbc.AddAvailableTimeRange(mid1, d2, d1));
		
		//Get available time ranges
		TimeRange[] tra = dbc.GetAvailableTimeRanges(mid1);
		assertEquals(2, tra.length);
		
		//Add user time range
		long rid;
		assertNotSame(0, rid = dbc.AddUserTimeRange(mid1, uid1, d1, d2));
		
		//Get user time ranges
		tra = dbc.GetUserTimeRanges(mid1, uid1);
		assertEquals(1, tra.length);
		assertEquals(rid, tra[0].id);
		
		//Get meeting time ranges
		tra = dbc.GetMeetingTimeRanges(mid1);
		assertEquals(1, tra.length);
		assertEquals(rid, tra[0].id);
		
		//Delete user time range
		dbc.DeleteUserTimeRange(rid);
		tra = dbc.GetUserTimeRanges(mid1, uid1);
		assertEquals(0, tra.length);
		
		//Clear meeting time ranges
		assertEquals(true, dbc.ClearMeetingTimeRanges(mid1));
		tra = dbc.GetAvailableTimeRanges(mid1);
		assertEquals(0, tra.length);
		
		

		assertEquals(true, dbc.SetMeetingState(mid1, Meeting.MeetingState.TimeSlotVoting.ordinal()));
		md1 = dbc.GetMeetingDescription(mid1);
		assertEquals(Meeting.MeetingState.TimeSlotVoting, md1.state);
		
		//Add time slot
		assertEquals(true, dbc.AddTimeSlot(mid1, d1, d2));
		assertEquals(true, dbc.AddTimeSlot(mid1, d2, d1));
		
		//Get available time slots
		TimeSlot[] tsa = dbc.GetAvailableTimeSlots(mid1);
		assertEquals(2, tsa.length);
		assertEquals(0, tsa[0].votes.length);
		assertEquals(0, tsa[1].votes.length);
		
		//Add time slot vote
		assertEquals(true, dbc.AddTimeSlotVote(tsa[0].id, uid1, TimeSlotVote.Vote.Upvote.ordinal()));
		assertEquals(true, dbc.AddTimeSlotVote(tsa[0].id, uid2, TimeSlotVote.Vote.Downvote.ordinal()));
		
		//Get time slot votes
		TimeSlotVote[] tsva = dbc.GetTimeSlotVotes(tsa[0].id);
		assertEquals(2, tsva.length);
		assertEquals(tsva[0].userID, uid1);
		assertEquals(tsva[1].userID, uid2);
		assertEquals(tsva[0].vote, TimeSlotVote.Vote.Upvote);
		assertEquals(tsva[1].vote, TimeSlotVote.Vote.Downvote);
		
		//Update time slot vote
		assertEquals(true, dbc.UpdateTimeSlotVote(tsa[0].id, uid1, TimeSlotVote.Vote.Veto.ordinal()));
		tsva = dbc.GetTimeSlotVotes(tsa[0].id);
		assertEquals(2, tsva.length);
		assertEquals(tsva[0].userID, uid1);
		assertEquals(tsva[1].userID, uid2);
		assertEquals(tsva[0].vote, TimeSlotVote.Vote.Veto);
		assertEquals(tsva[1].vote, TimeSlotVote.Vote.Downvote);
		
		//Remove time slot vote
		assertEquals(true, dbc.RemoveTimeSlotVote(tsa[0].id, uid2));
		tsva = dbc.GetTimeSlotVotes(tsa[0].id);
		assertEquals(1, tsva.length);
		assertEquals(tsva[0].userID, uid1);
		assertEquals(tsva[0].vote, TimeSlotVote.Vote.Veto);
		
		//Clear time slot votes
		assertEquals(true, dbc.ClearTimeSlotVotes(tsa[0].id));
		tsva = dbc.GetTimeSlotVotes(tsa[0].id);
		assertEquals(0, tsva.length);
		
		//Clear meeting time slots
		assertEquals(true, dbc.ClearMeetingTimeSlots(mid1));
		tsa = dbc.GetAvailableTimeSlots(mid1);
		assertEquals(0, tsa.length);

		
		
		assertEquals(true, dbc.SetMeetingState(mid1, Meeting.MeetingState.Finalized.ordinal()));
		md1 = dbc.GetMeetingDescription(mid1);
		assertEquals(Meeting.MeetingState.Finalized, md1.state);

		
		
		//Leave meetings
		System.out.println("asdf");
		assertEquals(0, dbc.LeaveMeeting(mid2, uid1));
		assertNotSame(0, dbc.LeaveMeeting(mid2, uid2));

		//Delete meetings
		assertEquals(0, dbc.TryJoinMeeting(md2.code, "asdf", uid1));
		assertEquals(true, dbc.DeleteMeeting(mid1));
		assertEquals(true, dbc.DeleteMeeting(mid2));
		assertEquals(null, dbc.GetMeetingDescription(mid1));
		
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE uid = " + String.valueOf(uid1) + ";"));
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE uid = " + String.valueOf(uid2) + ";"));
		
		dbc.Dispose();
	}
//*/

}
