package ketola.wicket.selenium.tester.example.form;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ConfirmPage
    extends WebPage
{

    private static final long serialVersionUID = 1L;

    public ConfirmPage( IModel<Person> person )
    {
        add( label( "name", person ) );
        add( label( "address", person ) );
        add( label( "zip", person ) );
        add( label( "city", person ) );
        add( label( "country", person ) );
    }

    private static Label label( String id, IModel<Person> model )
    {
        return new Label( id, new PropertyModel<String>( model, id ) );
    }

}
