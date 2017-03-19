package com.vaadin.vaadin_archetype_application;

//Class for providing references to controller classes
public class Controllers {
	private Controllers() { } //Thank you, Java, for not having static classes /s
	
	public static DBProvider DatabaseConnector = (DBProvider) new DatabaseConnector();
	public static long UserID = -1;
}
