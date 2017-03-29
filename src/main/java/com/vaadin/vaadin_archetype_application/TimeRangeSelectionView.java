package com.vaadin.vaadin_archetype_application;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.VerticalLayout;

public class TimeRangeSelectionView extends ILoggedInView {
	
	PopupView popup;

	@Override
	public String GetPageTitle() {
		return "Select time ranges";
	}

	@Override
	public String GetPageURL() {
		return Constants.URL_TIME_RANGE_SELECT;
	}
	
	@Override
	protected void InitUI() {
			VerticalLayout vl = new VerticalLayout();
			
			Grid dg = new Grid();
			vl.addComponent(dg);
			
			Button b = new Button("asdf");
			b.addClickListener(e->{ popup.setVisible(true); });
			vl.addComponent(b);
			
			setContent(vl);

			CreatePopup();
			vl.addComponent(popup);
	}
	
	protected void CreatePopup()
	{
		VerticalLayout vl = new VerticalLayout();
		
		vl.addComponent(new Label("Asdf"));
		
		popup = new PopupView("asf", vl);
		
	}

}
