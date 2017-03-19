package com.vaadin.vaadin_archetype_application;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;


public class MeetingForm extends FormLayout {

    TextField meetingName = new TextField("Meeting name");
    TextField state = new TextField("State");
    TextField numMembers = new TextField("Num. Invited");

    MeetingShortDescription meetingSD;

    BeanFieldGroup<MeetingShortDescription> formFieldBindings;

    public MeetingForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);
		addComponents(meetingName, state, numMembers);
    }


    void edit(MeetingShortDescription meetingSD) {
        this.meetingSD = meetingSD;
        if(meetingSD != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(meetingSD, this);
            meetingName.focus();
        }
        setVisible(meetingSD != null);
    }

    @Override
    public MyUI getUI() {
        return (MyUI) super.getUI();
    }

}