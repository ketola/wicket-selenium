package ketola.wicket.selenium.tester.example.form;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class FormPage
    extends WebPage
{
    private static final long serialVersionUID = 1L;

    public FormPage()
    {
        super();
        add( new PersonForm( "form", Model.of( new Person() ) ) );
    }

    public static class PersonForm
        extends Form<Person>
    {
        private static final long serialVersionUID = 1L;

        private TextField<String> name;

        private TextField<String> address;

        private TextField<String> zip;

        private TextField<String> city;

        private TextField<String> country;

        public PersonForm( String id, IModel<Person> model )
        {
            super( id, model );

            add( new FeedbackPanel( "feedback" ) );

            add( name = createTextField( "name", model ) );
            add( address = createTextField( "address", model ) );
            add( zip = createTextField( "zip", model ) );
            add( city = createTextField( "city", model ) );
            add( country = createTextField( "country", model ) );

            add( new Button( "submit" ) );
        }

        private TextField<String> createTextField( String field, IModel<Person> model )
        {
            TextField<String> textField = new TextField<String>( field, new PropertyModel<String>( model, field ) );
            textField.setRequired( true );
            return textField;
        }

        @Override
        protected void onSubmit()
        {
            setResponsePage( new ConfirmPage( getModel() ) );
        }
    }

}
