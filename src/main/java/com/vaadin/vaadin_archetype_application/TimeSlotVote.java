package com.vaadin.vaadin_archetype_application;

public class TimeSlotVote {
	public enum Vote
	{
		Upvote,
		Downvote,
		Veto
	}
	
	public long userID;
	public Vote vote;
	
	public void SetVote(int v)
	{
		Vote[] va = Vote.values();
		for (int i = 0; i < va.length; i++)
			if (va[i].ordinal() == v)
			{
				vote = va[i];
				return;
			}
	}
}
