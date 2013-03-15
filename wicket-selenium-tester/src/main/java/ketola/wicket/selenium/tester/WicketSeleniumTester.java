package ketola.wicket.selenium.tester;

import java.io.File;

import ketola.wicket.selenium.tester.requesthandler.DummyPanelPageProvider;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
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
        createAndStartServer();
        createWebDriver();
    }

    private void createAndStartServer()
    {
        this.server = new Server( 0 );

        ServletContextHandler context = newServletContextHolder();
        context.setContextPath( "/" );
        context.setBaseResource( getWebAppRoot() );

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

    protected Resource getWebAppRoot()
    {
        try
        {
            return Resource.newResource( new File( new File( "" ).getAbsolutePath() + "/src/main/webapp" ) );
        }
        catch ( Exception e )
        {
            throw new RuntimeException();
        }
    }

    private void createWebDriver()
    {
        driver = new HtmlUnitDriver( BrowserVersion.FIREFOX_10 );
        driver.setJavascriptEnabled( true );
    }

    protected WicketServlet newWicketServlet()
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

    protected ServletContextHandler newServletContextHolder()
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
        driver.get( createUrl( path ) );
        return driver;
    }

    private String createUrl( String path )
    {
        return String.format( "http://localhost:%d/%s", server.getConnectors()[0].getLocalPort(), path );
    }

    public WebDriver startPage( Class<? extends WebPage> page )
    {
        String path = randomString();
        application.mountPage( path, page );
        driver.get( createUrl( path ) );
        return driver;
    }

    private String randomString()
    {
        return "" + System.currentTimeMillis();
    }

    public WebDriver startPage( final IPageLoader loader )
    {
        final String path = randomString();
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
                return Url.parse( path );
            }

            @Override
            public int getCompatibilityScore( Request request )
            {
                return Integer.MAX_VALUE;
            }
        } );

        driver.get( createUrl( path ) );
        return driver;
    }

    public WebDriver startPanel( final IPanelLoader loader )
    {
        final String path = randomString();
        application.mount( new IRequestMapper()
        {

            @Override
            public IRequestHandler mapRequest( Request request )
            {
                return new RenderPageRequestHandler( new DummyPanelPageProvider( loader ) );
            }

            @Override
            public Url mapHandler( IRequestHandler requestHandler )
            {
                return Url.parse( path );
            }

            @Override
            public int getCompatibilityScore( Request request )
            {
                return Integer.MAX_VALUE;
            }
        } );

        driver.get( createUrl( path ) );
        return driver;
    }
}
