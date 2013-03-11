package ketola.wicket.selenium.tester.wicket.selenium.tester.application.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SimplePanel extends Panel {

	private static final long serialVersionUID = 1L;

	public SimplePanel(String id, IModel<String> model) {
		super(id, model);
		add(new Label("label", model));
	}

}
