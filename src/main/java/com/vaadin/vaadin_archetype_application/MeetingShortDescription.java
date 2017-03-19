package com.vaadin.vaadin_archetype_application;

import org.apache.commons.beanutils.BeanUtils;


public class MeetingShortDescription {

		public int ID;
		public String name;
		public byte state;
		public int numberOfMembers;
		
		public String getMeetingName() {
			return name;
		}
		public byte getState() {
			return state;
		}
		public int getAttendees() {
			return numberOfMembers;
		}
		public void setID(int ID) {
			this.ID = ID;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setState(byte state) {
			this.state = state;
		}
		public void setNumAttending(int num) {
			this.numberOfMembers = num;
		}
		public int getId() {
			return ID;
		}
	
	    @Override
	    public MeetingShortDescription clone() throws CloneNotSupportedException {
	        try {
	            return (MeetingShortDescription) BeanUtils.cloneBean(this);
	        } catch (Exception ex) {
	            throw new CloneNotSupportedException();
	        }
	    }
	    
	    public String toString() {
	    	return ID + " " + name + " " + state + " " + numberOfMembers;
	    }
}
