package ketola.wicket.selenium.tester.wicket.selenium.tester;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WicketSeleniumTester
{
    private Server server;

    private HtmlUnitDriver driver;

    private WebApplication application;

    public WicketSeleniumTester( WebApplication application )
    {
        this.application = application;
        setUp();
    }

    public void setUp()
    {
        this.server = new Server();

        driver = new HtmlUnitDriver( BrowserVersion.FIREFOX_10 );
        driver.setJavascriptEnabled( true );

        Connector con = new SelectChannelConnector();
        con.setPort( 8091 );
        server.addConnector( con );
        ServletContextHandler context = newServletContextHolder();
        context.setContextPath( "/" );
        server.setHandler( context );
        WicketServlet servlet = newWicketServlet();

        ServletHolder holder = new ServletHolder( servlet );
        holder.setInitParameter( WicketFilter.FILTER_MAPPING_PARAM, "/*" );

        holder.setName( "wicket.selenium.servlet" );
        context.addServlet( holder, "/*" );

        try
        {
            server.start();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }

    }

    private WicketServlet newWicketServlet()
    {
        return new WicketServlet()
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected WicketFilter newWicketFilter()
            {

                return new WicketFilter()
                {

                    @Override
                    protected IWebApplicationFactory getApplicationFactory()
                    {
                        return new IWebApplicationFactory()
                        {

                            @Override
                            public void destroy( WicketFilter filter )
                            {
                            }

                            @Override
                            public WebApplication createApplication( WicketFilter filter )
                            {
                                return application;
                            }
                        };
                    }
                };
            }
        };
    }

    private ServletContextHandler newServletContextHolder()
    {
        return new ServletContextHandler( ServletContextHandler.SESSIONS );
    }

    public void quit()
    {
        try
        {
            server.stop();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }

        driver.quit();
    }

    protected HtmlUnitDriver getWebDriver()
    {
        return driver;
    }

    public WebDriver startPath( String path )
    {
        driver.get( "http://localhost:8091/" + path );
        return driver;
    }

    public WebDriver startPage( Class<? extends WebPage> page )
    {
        application.mountPage( "startPage", page );
        driver.get( "http://localhost:8091/startPage" );
        return driver;
    }

    public WebDriver startPage( final PageLoader loader )
    {
        application.mount( new IRequestMapper()
        {

            @Override
            public IRequestHandler mapRequest( Request request )
            {
                return new RenderPageRequestHandler( new PageProvider( loader.getPage() ) );
            }

            @Override
            public Url mapHandler( IRequestHandler requestHandler )
            {
                return Url.parse( "page" );
            }

            @Override
            public int getCompatibilityScore( Request request )
            {
                return Integer.MAX_VALUE;
            }
        } );

        driver.get( "http://localhost:8091/page" );
        return driver;
    }

    public WebDriver startPanel( final PanelLoader loader )
    {
        application.mount( new IRequestMapper()
        {

            @Override
            public IRequestHandler mapRequest( Request request )
            {
                return new RenderPageRequestHandler( new PageProvider( new DummyPanelPage()
                {
                    @Override
                    protected Panel getTestPanel( String id )
                    {
                        return loader.getPanel( id );
                    }
                } ) );
            }

            @Override
            public Url mapHandler( IRequestHandler requestHandler )
            {
                return new Url();
            }

            @Override
            public int getCompatibilityScore( Request request )
            {
                return Integer.MAX_VALUE;
            }
        } );

        driver.get( "http://localhost:8091/panel" );
        return driver;
    }
}
