package ketola.wicket.selenium.tester.requesthandler;

import ketola.wicket.selenium.tester.PanelLoader;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.DummyPanelPage;

public class DummyPanelPageProvider
    extends PageProvider
{

    public DummyPanelPageProvider( final PanelLoader loader )
    {
        super( new DummyPanelPage()
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Panel getTestPanel( String id )
            {
                return loader.getPanel( id );
            }
        } );
    }

}
