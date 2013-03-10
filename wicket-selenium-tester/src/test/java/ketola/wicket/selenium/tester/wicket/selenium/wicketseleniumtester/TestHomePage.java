package ketola.wicket.selenium.tester.wicket.selenium.wicketseleniumtester;


import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ketola.wicket.selenium.tester.wicket.selenium.tester.WicketSeleniumTester;
import ketola.wicket.selenium.tester.wicket.selenium.tester.application.HomePage;
import ketola.wicket.selenium.tester.wicket.selenium.tester.application.WicketApplication;

public class TestHomePage {

	private WicketSeleniumTester tester;
	
	@Before
	public void setUp()
	{
		tester = new WicketSeleniumTester(new WicketApplication());
	}
	
	@Test
	public void homepageRendersSuccessfully()
	{
		WebDriver driver = tester.startPage(HomePage.class);
		
		//System.out.println(driver.getPageSource());

		assertEquals("Apache Wicket", driver.findElement(By.xpath("//div[@id='logo']/h1")).getText());
		assertEquals("Congratulations!", driver.findElement(By.xpath("//div[@id='bd']/h2")).getText());
	}
}
