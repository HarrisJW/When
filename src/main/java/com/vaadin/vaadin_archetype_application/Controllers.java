package com.vaadin.vaadin_archetype_application;

public class Controllers {
	private Controllers() { } //Thank you, Java, for not having static classes /s
	
	public static DBProvider DatabaseConnector = (DBProvider) new DatabaseConnector();
	public static int UserID = -1;
}
