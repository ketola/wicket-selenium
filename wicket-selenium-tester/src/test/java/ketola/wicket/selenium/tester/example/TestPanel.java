package ketola.wicket.selenium.tester.example;

import static junit.framework.Assert.assertEquals;
import ketola.wicket.selenium.tester.PanelLoader;
import ketola.wicket.selenium.tester.WicketSeleniumTester;
import ketola.wicket.selenium.tester.example.application.WicketApplication;
import ketola.wicket.selenium.tester.example.panel.SimplePanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestPanel
{
    private WicketSeleniumTester tester;

    @Before
    public void setUp()
    {
        tester = new WicketSeleniumTester( new WicketApplication() );
    }

    @After
    public void cleanUp()
    {
        tester.quit();
    }

    @Test
    public void panelRendersSuccessfully()
    {
        WebDriver driver = tester.startPanel( new PanelLoader()
        {

            @Override
            public Panel getPanel( String id )
            {
                return new SimplePanel( id, Model.of( "a text to display" ) );
            }
        } );

        assertEquals( "a text to display", driver.findElement( By.xpath( "//span" ) ).getText() );
    }
}
