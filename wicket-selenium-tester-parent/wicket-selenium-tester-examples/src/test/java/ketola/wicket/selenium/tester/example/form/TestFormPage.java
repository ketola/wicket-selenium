package ketola.wicket.selenium.tester.example.form;

import static org.junit.Assert.assertEquals;

import java.util.List;

import ketola.wicket.selenium.tester.WicketSeleniumTester;
import ketola.wicket.selenium.tester.example.application.WicketApplication;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestFormPage
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
    public void openFormPageUsingPageClass()
    {
        WebDriver d = tester.startPage( FormPage.class );

        assertEquals( "Form Page", d.getTitle() );
        assertEquals( FormPage.class, tester.getLastRenderedPage().getClass() );
    }

    @Test
    public void openFormPageUsingApplicationPath()
    {
        WebDriver d = tester.startPath( "formPage" );

        assertEquals( "Form Page", d.getTitle() );
        assertEquals( FormPage.class, tester.getLastRenderedPage().getClass() );
    }

    @Test
    public void successfulFormSubmissionLeadsToConfirmPage()
    {
        WebDriver d = tester.startPage( FormPage.class );

        assertEquals( FormPage.class, tester.getLastRenderedPage().getClass() );

        d.findElement( By.name( "name" ) ).sendKeys( "John Smith" );
        d.findElement( By.name( "address" ) ).sendKeys( "Sunset Boulevard" );
        d.findElement( By.name( "zip" ) ).sendKeys( "000111" );
        d.findElement( By.name( "city" ) ).sendKeys( "Oslo" );
        d.findElement( By.name( "country" ) ).sendKeys( "Vietnam" );
        d.findElement( By.cssSelector( "input[type='submit']" ) ).click();

        // response page should be ConfirmationPage - check both Class and the rendered markup
        assertEquals( ConfirmPage.class, tester.getLastRenderedPage().getClass() );
        assertEquals( "Confirm Page", d.getTitle() );

        // Check values on the page
        assertEquals( "John Smith", d.findElement( By.cssSelector( "span[wicket\\:id='name']" ) ).getText() );
        assertEquals( "Sunset Boulevard", d.findElement( By.cssSelector( "span[wicket\\:id='address']" ) ).getText() );
        assertEquals( "000111", d.findElement( By.cssSelector( "span[wicket\\:id='zip']" ) ).getText() );
        assertEquals( "Oslo", d.findElement( By.cssSelector( "span[wicket\\:id='city']" ) ).getText() );
        assertEquals( "Vietnam", d.findElement( By.cssSelector( "span[wicket\\:id='country']" ) ).getText() );

        // It's also possible to check values in the model object
        Person person = (Person) tester.getLastRenderedPage().getDefaultModelObject();

        assertEquals( "John Smith", person.getName() );
        assertEquals( "Sunset Boulevard", person.getAddress() );
        assertEquals( "000111", person.getZip() );
        assertEquals( "Oslo", person.getCity() );
        assertEquals( "Vietnam", person.getCountry() );

    }

    @Test
    public void validations()
    {
        WebDriver d = tester.startPage( FormPage.class );

        d.findElement( By.cssSelector( "input[type='submit']" ) ).click();

        List<WebElement> feedbacks = d.findElements( By.cssSelector( "span.feedbackPanelERROR" ) );
        assertEquals( "'name' is required.", feedbacks.get( 0 ).getText() );
        assertEquals( "'address' is required.", feedbacks.get( 1 ).getText() );
        assertEquals( "'zip' is required.", feedbacks.get( 2 ).getText() );
        assertEquals( "'city' is required.", feedbacks.get( 3 ).getText() );
        assertEquals( "'country' is required.", feedbacks.get( 4 ).getText() );
    }
}
