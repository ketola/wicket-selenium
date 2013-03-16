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
    }

    @Test
    public void openFormPageUsingApplicationPath()
    {
        WebDriver d = tester.startPath( "formPage" );

        assertEquals( "Form Page", d.getTitle() );
    }

    @Test
    public void successfulFormSubmissionLeadsToConfirmPage()
    {
        WebDriver d = tester.startPage( FormPage.class );

        d.findElement( By.name( "name" ) ).sendKeys( "John Smith" );
        d.findElement( By.name( "address" ) ).sendKeys( "Sunset Boulevard" );
        d.findElement( By.name( "zip" ) ).sendKeys( "000111" );
        d.findElement( By.name( "city" ) ).sendKeys( "Oslo" );
        d.findElement( By.name( "country" ) ).sendKeys( "Vietnam" );
        d.findElement( By.cssSelector( "input[type='submit']" ) ).click();

        // response page should be ConfirmationPage
        assertEquals( "Confirm Page", d.getTitle() );
        assertEquals( "John Smith", d.findElement( By.cssSelector( "span[wicket\\:id='name']" ) ).getText() );
        assertEquals( "Sunset Boulevard", d.findElement( By.cssSelector( "span[wicket\\:id='address']" ) ).getText() );
        assertEquals( "000111", d.findElement( By.cssSelector( "span[wicket\\:id='zip']" ) ).getText() );
        assertEquals( "Oslo", d.findElement( By.cssSelector( "span[wicket\\:id='city']" ) ).getText() );
        assertEquals( "Vietnam", d.findElement( By.cssSelector( "span[wicket\\:id='country']" ) ).getText() );
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
