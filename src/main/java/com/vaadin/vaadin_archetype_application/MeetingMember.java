package com.vaadin.vaadin_archetype_application;

//Data container for meeting member
public class MeetingMember {
	
	public enum UserAccess
	{
		Member,
		Moderator,
		Creator
	}

	public long ID;
	public String firstName, lastName;
	public UserAccess access;
	
	public void SetAccess(int a)
	{
		if (a == 0)
			access = UserAccess.Member;
		else if (a == 1)
			access = UserAccess.Moderator;
		else
			access = UserAccess.Creator;
	}
}
