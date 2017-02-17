package com.vaadin.vaadin_archetype_application;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.buttons.GoogleButton;

import com.github.scribejava.core.model.Token;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push
@Theme("mytheme")
public class MyUI extends UI {

	// init() is the new main()
	// Use this pattern for all UI for CSCI 3130.
	// Create layout --> create components --> put them together.
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        // Add a new button to the interface. 
        Button button = new Button("Click Me");
        String GGL_KEY = "CLIENT KEY HERE";
        String GGL_SECRET = "CLIENT SECRET HERE";
        OAuthPopupButton ob = new GoogleButton(GGL_KEY, GGL_SECRET, "https://www.googleapis.com/auth/calendar");
        ob.addOAuthListener(new OAuthListener() {
        	@Override
        	public void authSuccessful(Token token, boolean isOAuth20) {
        		// TODO Auto-generated method stub
        		System.out.println(token.toString());
        	}

        	@Override
        	public void authDenied(String reason) {
        		// TODO Auto-generated method stub
        		System.out.println("DENIED");
        	}
        });

        
        // Event object e. Add new component to layout.
        // lambda function. Read up.
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });
        
        layout.addComponents(name, button, ob);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
