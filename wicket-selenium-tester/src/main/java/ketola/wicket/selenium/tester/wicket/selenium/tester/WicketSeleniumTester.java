package ketola.wicket.selenium.tester.wicket.selenium.tester;



import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WicketSeleniumTester {
	private Server server;
	private HtmlUnitDriver driver;
	private WebApplication application;
	
	
	public WicketSeleniumTester(WebApplication application) {
		this.application = application;
		setUp();
	}
	
	public void setUp(){
		this.server = new Server();
		
		driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_10);
		driver.setJavascriptEnabled(true);
		
		Connector con = new SelectChannelConnector();
		con.setPort( 8091 );
		server.addConnector( con );
		ServletContextHandler context = newServletContextHolder();
        context.setContextPath("/");
        server.setHandler(context);
        WicketServlet servlet = newWicketServlet();
		
		ServletHolder holder = new ServletHolder( servlet );
		holder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		
		holder.setName( "wicket.selenium.servlet" );
		context.addServlet( holder, "/*" );
		
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private WicketServlet newWicketServlet() {
		return new WicketServlet(){
			private static final long serialVersionUID = 1L;

			@Override
			protected WicketFilter newWicketFilter() {
				
				return new WicketFilter(){
					
					@Override
					protected IWebApplicationFactory getApplicationFactory() {
						return new IWebApplicationFactory() {
							
							@Override
							public void destroy(WicketFilter filter) {}
							
							@Override
							public WebApplication createApplication(WicketFilter filter) {
								return application;
							}
						};
					}
				};
			}
		};
	}

	private ServletContextHandler newServletContextHolder() {
		return new ServletContextHandler(ServletContextHandler.SESSIONS);
	}
	
	public void quit()
	{
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		driver.quit();
	}
	
	protected HtmlUnitDriver getWebDriver(){
		return driver;
	}
	
	public WebDriver startPath(String path){
		driver.get("http://localhost:8091/" + path);
		return driver;
	}
	
	public WebDriver startPage(Class<? extends WebPage> page){
		application.mountPage("startPage", page);
		driver.get("http://localhost:8091/startPage");
		return driver;
	}
}
