package com.vaadin.vaadin_archetype_application;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public abstract class ILoggedInView extends Panel implements View {
	
	//Used to indicate if current page is redirecting user to the login screen
	private boolean IsForbidden = false;
	protected AbstractOrderedLayout layout = null;

	@Override
	public void enter(ViewChangeEvent event)
	{
		// Moved this logic from the constructor, to ensure that it is called when we transition views.
		// Including this logic in the constructor meant that it was called during instantiation
		// of the object when registering it with the navigator (i.e. before most of the information
		// about credentials had been collected).
		if (IsForbidden = !UserManager.AssureLogin(GetPageURL()))
			return;
		Page.getCurrent().setTitle(GetPageTitle());
		setSizeFull();
		layout = InitUI();
		if (layout != null)
			setContent(layout);
	}
	
	//Initialize UI here
	protected AbstractOrderedLayout InitUI()
	{
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		
		Button menu = new Button("Back to menu");
		menu.addClickListener(e -> OnMenuButtonClick());
		vl.addComponent(menu);
		vl.setComponentAlignment(menu, Alignment.TOP_RIGHT);
		
		return vl;
	}
	
	protected void OnMenuButtonClick()
	{
		UI.getCurrent().getNavigator().navigateTo("");	
	}

	//Used to indicate if current page is redirecting user to the login screen
	protected boolean IsForbidden()
	{
		return IsForbidden;
	}
	
	//Returns page title (Constant)
	public abstract String GetPageTitle();
	
	//Returns page URL (constant)
	public abstract String GetPageURL();
}
