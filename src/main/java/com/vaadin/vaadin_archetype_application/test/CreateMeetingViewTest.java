package com.vaadin.vaadin_archetype_application.test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;

public class CreateMeetingViewTest extends TestBenchTestCase {
	
	// @Rule
	// public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
	
	@Before
	public void setUp() throws Exception {

			setDriver(new FirefoxDriver());
			
	}

	private void openTestUrl(){
		
		getDriver().get("http://localhost8080");

	}

	@Test
	public void testJoinMeetingButton() throws Exception{
		
		openTestUrl();
		
		//assertFalse($(LabelElement.class).exists());

		ButtonElement joinMeetingButton = $(ButtonElement.class).caption("Join meeting").first();
		joinMeetingButton.click();
		
		assertEquals("You need to be logged in to access this page", $(LabelElement.class).first().getText());

	}
	
	@After
	public void tearDown() throws Exception {
		getDriver().quit();
	}

}
