package ketola.wicket.selenium.tester.example.wickettester.panel;

import ketola.wicket.selenium.tester.example.application.WicketApplication;
import ketola.wicket.selenium.tester.example.panel.SimplePanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestPanel
{
    private WicketTester tester;

    @Before
    public void setUp()
    {
        tester = new WicketTester( new WicketApplication() );
    }

    @Test
    public void homepageRendersSuccessfully()
    {
        tester.startPage( new DummyPanelPage()
        {
            @Override
            protected Panel getTestPanel( String id )
            {
                return new SimplePanel( id, Model.of( "a text in a panel" ) );
            }
        } );

        tester.assertLabel( "panel:label", "a text in a panel" );
    }
}
