package ketola.wicket.selenium.tester.example.page;

import static junit.framework.Assert.assertEquals;
import ketola.wicket.selenium.tester.IPageLoader;
import ketola.wicket.selenium.tester.WicketSeleniumTester;
import ketola.wicket.selenium.tester.example.application.HomePage;
import ketola.wicket.selenium.tester.example.application.WicketApplication;

import org.apache.wicket.Page;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestHomePage
{

    private static WicketSeleniumTester tester;

    @BeforeClass
    public static void setUp()
    {
        tester = new WicketSeleniumTester( new WicketApplication() );
    }

    @AfterClass
    public static void cleanUp()
    {
        tester.quit();
    }

    @Test
    public void homepageRendersSuccessfullyClass()
    {
        WebDriver driver = tester.startPage( HomePage.class );

        assertEquals( HomePage.class, tester.getLastRenderedPage().getClass() );
        assertEquals( "Apache Wicket", driver.findElement( By.xpath( "//div[@id='logo']/h1" ) ).getText() );
        assertEquals( "Congratulations!", driver.findElement( By.xpath( "//div[@id='bd']/h2" ) ).getText() );
    }

    @Test
    public void homepageRendersSuccessfullyObject()
    {
        WebDriver driver = tester.startPage( new IPageLoader()
        {
            @Override
            public Page getPage()
            {
                return new HomePage( null );
            }
        } );

        assertEquals( HomePage.class, tester.getLastRenderedPage().getClass() );
        assertEquals( "Apache Wicket", driver.findElement( By.xpath( "//div[@id='logo']/h1" ) ).getText() );
        assertEquals( "Congratulations!", driver.findElement( By.xpath( "//div[@id='bd']/h2" ) ).getText() );
    }

}
