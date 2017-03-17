package com.vaadin.vaadin_archetype_application.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.vaadin.vaadin_archetype_application.Constants;
import com.vaadin.vaadin_archetype_application.DBProvider;
import com.vaadin.vaadin_archetype_application.DBProvider.MeetingDescription;
import com.vaadin.vaadin_archetype_application.DBProvider.MeetingMember;
import com.vaadin.vaadin_archetype_application.DBProvider.MeetingShortDescription;
import com.vaadin.vaadin_archetype_application.DatabaseConnector;

public class DatabaseConnectorTest {
	
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
		Date d = new Date();
		long mid1 = dbc.CreateMeeting("", d, d, "m1", new Date(1000*60*60*2), uid1);
		assertNotSame(-1, mid1);
		
		long mid2 = dbc.CreateMeeting("asdf", d, d, "m2", new Date(1000*60*60*3), uid2);
		assertNotSame(-1, mid2);
		
		//Get meeting description
		MeetingDescription md1 = dbc.GetMeetingDescription(mid1);
		MeetingDescription md2 = dbc.GetMeetingDescription(mid2);
		//assertEquals(1000*60*60*2, md1.duration.getTime());
		//assertEquals(d, md1.endDate);
		//assertEquals(d, md1.endDate);
		assertEquals(mid1, md1.ID);
		assertEquals("m1", md1.name);
		assertEquals(DBProvider.MeetingState.Setup, md1.state);
		
		//TODO
		//Join meetings
		//assertEquals(0, dbc.TryJoinMeeting(md2.code, "", uid1));
		//assertNotSame(0, dbc.TryJoinMeeting(md2.code, "", uid1));
		//assertNotSame(0, dbc.TryJoinMeeting(md1.code, "", uid1));
		
		//Get meetings list
		ArrayList<MeetingShortDescription> msd = dbc.GetMeetingsList(uid1);
		assertEquals(2, msd.size());
		assertEquals(mid1, msd.get(0).ID);
		assertEquals(mid2, msd.get(1).ID);
		assertEquals("m1", msd.get(0).name);
		assertEquals("m2", msd.get(1).name);
		assertEquals(0, msd.get(0).state);
		assertEquals(0, msd.get(1).state);
		assertEquals(1, msd.get(0).numberOfMembers);
		assertEquals(2, msd.get(1).numberOfMembers);
		
		//Get meeting members
		ArrayList<MeetingMember> mem = dbc.GetMeetingMembers(mid2);
		assertEquals(2, mem.size());
		assertEquals(2, mem.get(0).access);
		assertEquals(0, mem.get(1).access);
		assertEquals("asdf", mem.get(0).firstName);
		assertEquals("2asdf", mem.get(1).firstName);
		assertEquals(uid1, mem.get(0).ID);
		assertEquals(uid2, mem.get(1).ID);
		assertEquals("qwer", mem.get(0).lastName);
		assertEquals("2qwer", mem.get(1).lastName);
		
		//Get meeting members count
		assertEquals(1, dbc.GetMeetingMembersCount(mid1));
		assertEquals(2, dbc.GetMeetingMembersCount(mid2));
		
		//Update meeting information
		Date d2 = new Date();
		assertEquals(true, dbc.UpdateMeetingTime(mid1, d2, d2, new Date(1000)));
		md1 = dbc.GetMeetingDescription(mid1);
		assertEquals(1000*60*60*2, md1.duration);
		assertEquals(d, md1.endDate);
		assertEquals(d, md1.endDate);
		assertEquals(mid1, md1.ID);
		assertEquals("m1", md1.name);
		assertEquals(DBProvider.MeetingState.Setup, md1.state);
		
		
		//TODO check meeting state change, voting
		
		//Leave meetings
		assertEquals(0, dbc.LeaveMeeting(mid2, uid1));
		assertEquals(1, dbc.GetMeetingMembersCount(mid2));
		assertNotSame(0, dbc.LeaveMeeting(mid2, uid2));
		assertEquals(1, dbc.GetMeetingMembersCount(mid2));

		//Delete meetings
		assertEquals(0, dbc.TryJoinMeeting(md2.code, "", uid1));
		assertEquals(0, dbc.DeleteMeeting(mid1));
		assertEquals(0, dbc.DeleteMeeting(mid2));
		assertEquals(null, dbc.GetMeetingDescription(mid1));
		
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE uid = " + String.valueOf(uid1) + ";"));
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user WHERE uid = " + String.valueOf(uid2) + ";"));
		
		dbc.Dispose();
	}

}
