package tagcomp.site.client;

import tagcomp.widgets.Menu;
import tagcomp.widgets.StatusLabel;
import tagcomp.widgets.StatusLabel.StatusStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App_site implements EntryPoint
{
	private Menu menu;
	private StatusLabel status;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		// SharedResources.instance.css().ensureInjected();

		// Coloco el label de estado para mensajes.
		status = new StatusLabel();
		RootPanel.get("statusContainer").add(status);
		status.setStatus("Utilice el menu principal para navegar", StatusStyle.NORMAL, 0);
		
		// Create the site menu.
		this.createMenu();

		// RootPanel.get("contentContainer").add(new SessionController(menu, 999));
		RootPanel.get("contentContainer").add(new Label("Here goes the main content..."));
		
		// Me conecto a la historia.
		//History.addValueChangeHandler(this);
		//History.fireCurrentHistoryState();

	}

	/**
	 * Create the session menu item at the end. This item handles login and logout for the user.
	 * Only alpha-numerical passwords are allowed.
	 */
	private void createMenu()
	{
		Anchor link = new Anchor("- Not registered?", "");
		link.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				event.preventDefault();
				status.setStatus("Click!", StatusStyle.ERROR);
			}
		});
		
		menu = new Menu();
		menu.addItem("Option 1", link, 0);
		menu.addItem("Option 2", link, 1);
		new LoginWidget(menu, 999);
		RootPanel.get("menuContainer").add(menu);
	}
}
