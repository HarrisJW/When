package com.vaadin.vaadin_archetype_application.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.vaadin.vaadin_archetype_application.Constants;
import com.vaadin.vaadin_archetype_application.DBProvider;
import com.vaadin.vaadin_archetype_application.DatabaseConnector;

public class DatabaseConnectorTest {

	@Test
	public void tryJoinMeeting() {
		DBProvider dbc = new DatabaseConnector();
		dbc.Initialize();
		assertEquals(0, dbc.TryJoinMeeting("nopass", ""));
		assertEquals(0, dbc.TryJoinMeeting("nopass", "asdf"));
		assertEquals(Constants.CODE_INVALID_MEETING_ID_PASSWORD, dbc.TryJoinMeeting("pass", ""));
		assertEquals(Constants.CODE_INVALID_MEETING_ID_PASSWORD, dbc.TryJoinMeeting("pass", "asdf"));
		assertEquals(0, dbc.TryJoinMeeting("pass", "guest"));
		assertEquals(Constants.CODE_MEETING_ID_EMPTY, dbc.TryJoinMeeting("", ""));
		assertEquals(Constants.CODE_MEETING_ID_EMPTY, dbc.TryJoinMeeting("", "asdf"));
		dbc.Dispose();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
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
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "DELETE FROM user;"));
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "INSERT INTO user VALUES (1, \"1234\", \"asd@asd.asd\", \"asd\", \"asd\");"));
		assertEquals(true, dbc.Execute(dbc.GetConnection(), "INSERT INTO user VALUES (2, \"21234\", \"2asd@asd.asd\", \"2asd\", \"2asd\");"));
		//ArrayList<Object[]> r = DatabaseConnector.ExecuteQuery(DatabaseConnector.OpenConnection(), "SELECT * FROM user;");
		ArrayList<Object[]> r = dbc.ExecuteStoredProcedure(dbc.GetConnection(), "selectUsers", new Object[] { 2 });
		for (int i = 0; i < r.size(); i++)
		{
			Object[] o = r.get(i);
			//for (int j = 0; j < o.length; j++)
			//System.out.print(o.toString() + " (" + o.getClass().getName() + "), ");
			System.out.print((int)o[0] + ", ");
			System.out.print((String)o[1] + ", ");
			System.out.println("");
		}
		assertEquals(2, dbc.GetUserID("2asd@asd.asd"));
		dbc.Dispose();
	}

}
