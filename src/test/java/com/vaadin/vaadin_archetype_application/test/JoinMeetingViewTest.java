package com.vaadin.vaadin_archetype_application.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;

public class JoinMeetingViewTest extends TestBenchTestCase {
	
	// https://vaadin.com/forum/#!/thread/11023921/14736435
	// WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
	// StackExchange tells us to ignore the above warning.
	// @Rule
	// public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
	// Encountered classnotfound exception, and fixed it by adding to pom.xml
	// http://stackoverflow.com/questions/24741012/noclassdeffounderror-while-running-a-valid-jar-compiled-with-dependencies-desp
	
	@Before
	public void setUp() throws Exception {

			setDriver(new PhantomJSDriver());
			
	}

	private void openTestUrl(){
		
		getDriver().get("http://localhost:8080");

	}

	@Test
	public void joinMeetingButton() throws Exception{
		
		openTestUrl();
		
		ButtonElement joinMeetingButton = $(ButtonElement.class).caption("Join meeting").first();
		joinMeetingButton.click();
		
		assertEquals("You need to be logged in to access this page", $(LabelElement.class).first().getText());

	}
	
	@After
	public void tearDown() throws Exception {
		
		getDriver().quit();
		
	}

}