package ketola.wicket.selenium.tester.example.panel;

import static junit.framework.Assert.assertEquals;

import java.io.Serializable;

import ketola.wicket.selenium.tester.IPanelLoader;
import ketola.wicket.selenium.tester.WicketSeleniumTester;
import ketola.wicket.selenium.tester.example.application.WicketApplication;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestPanel
    implements Serializable
{
    private static final long serialVersionUID = 1L;

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
    public void panelRendersSuccessfullyAnonymousPanelLoader()
    {
        WebDriver driver = tester.startPanel( new IPanelLoader()
        {

            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel( String id )
            {
                return new SimplePanel( id, Model.of( "a text to display" ) );
            }
        } );

        assertEquals( "a text to display", driver.findElement( By.xpath( "//span" ) ).getText() );
    }

    @Test
    public void panelRendersSuccessfullyConcretePanelLoader()
    {
        WebDriver driver = tester.startPanel( new PanelLoader() );

        assertEquals( "another text to display", driver.findElement( By.xpath( "//span" ) ).getText() );
    }

    private static class PanelLoader
        implements IPanelLoader
    {

        private static final long serialVersionUID = 1L;

        public Panel getPanel( String id )
        {
            return new SimplePanel( id, Model.of( "another text to display" ) );
        };
    }

}
