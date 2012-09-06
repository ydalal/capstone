package tagcomp.site.client;

import tagcomp.resources.SharedResources;
import tagcomp.widgets.Menu;
import tagcomp.widgets.StatusLabel;
import tagcomp.widgets.StatusLabel.StatusStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginMenuOptions
{
	private VerticalPanel loggedIn;
	private FlexTable loggedOut;

	private Menu parent;
	int position;								// Index of this submenu on the main menu. Required to alter it's name on a login/logout.
//	private StatusLabel loginStatus;
	
	private TextBox user;
	private PasswordTextBox pass;

	/**
	 * Creates a new session menu and adds it to the specified menu in the specified position.
	 * @param menu
	 */
	public LoginMenuOptions(Menu menu, int position)
	{
		parent = menu;
		this.position = position;

		// Initialize all menus.
		initLoggedOutMenu();
		initLoggedInMenu();
		
		// Establish the menu that goes first.
		FlowPanel fp = new FlowPanel();
		fp.addStyleName("test");
		fp.add(loggedIn);
		fp.add(loggedOut);
		position = parent.addSubMenu("Login/Register", fp, position);
	}
	
	void initLoggedOutMenu()
	{
		final StatusLabel loginStatus = new StatusLabel();
		user = new TextBox();
		user.setWidth("100px");

		// Prepare links and other interactive elements and their events.
		pass = new PasswordTextBox();
		pass.setWidth("100px");
		pass.addKeyDownHandler(new KeyDownHandler()
		{
			public void onKeyDown(KeyDownEvent event)
			{
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					submitLogin();
				}
			}
		});
		pass.addKeyPressHandler(new KeyPressHandler() 
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (!Character.isLetterOrDigit(event.getCharCode())) 
					((PasswordTextBox) event.getSource()).cancelKey();
			}
		});
		
		Anchor register = new Anchor("- Not registered?", "");
		register.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				event.preventDefault();
				loginStatus.setStatus("then you are screwed...", StatusStyle.ERROR);
				// TODO Registration
			}
		});
		
		Anchor forgot = new Anchor("- Forgot your password?", "");
		forgot.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				event.preventDefault();
				loginStatus.setStatus("idiot...", StatusStyle.ERROR);
				// TODO Recuperacion de clave...
			}
		});
		
		Image go = new Image(SharedResources.instance.go());
		go.addStyleName("handCursor");
		go.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				//event.preventDefault();
				submitLogin();
			}
		});

		// Initialize the menu allowing a user to log in.
		loggedOut = new FlexTable();
		loggedOut.setStyleName("subtable");

		//loggedOff.getRowFormatter().setStyleName(0, "error");	// For password errors.
		loggedOut.setWidget(0, 0, loginStatus);
		loggedOut.getFlexCellFormatter().setColSpan(0, 0, 3);
		loggedOut.setText(1, 0, "user:");
		loggedOut.setWidget(1, 1, user);
		loggedOut.setWidget(1, 2, go);
		loggedOut.getFlexCellFormatter().setRowSpan(1, 2, 2);
		loggedOut.setText(2, 0, "pass:");
		loggedOut.setWidget(2,1, pass);
		loggedOut.setWidget(3,0, register);
		loggedOut.getFlexCellFormatter().setColSpan(3, 0, 3);
		loggedOut.setWidget(4,0, forgot);
		loggedOut.getFlexCellFormatter().setColSpan(4, 0, 3);
	}

	void initLoggedInMenu()
	{
		// Initialize interactive links.
		Anchor profile = new Anchor("My profile", "");
		profile.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				event.preventDefault();
				// TODO Profile
			}
		});
		
		Anchor compares = new Anchor("My stored comparissons", "");
		compares.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				// Ojo que si no eliminamos el comportamiento del click por defecto se va a redireccionar la página, causando errores con "statusCode: 0".
				event.preventDefault();
				// TODO stored compares.
			}
		});
		
		Anchor logoff = new Anchor("Log off", "");
		logoff.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				event.preventDefault();
				logoff();
			}
		});

		Anchor passchange = new Anchor("Change password", "", "portal.passChange");
//		passchange.addClickHandler(Portal.getInstance());
		
		// Set the menu options for users logged in.
		loggedIn = new VerticalPanel();
		loggedIn.setStyleName("subtable");
		loggedIn.add(profile);
		loggedIn.add(compares);
		loggedIn.add(passchange);
		loggedIn.add(new HTML("<hr>"));
		loggedIn.add(logoff);
		
		// This menu is initially hidden.
		loggedIn.setVisible(false);
	}
	
	private void submitLogin()
	{
		String name = user.getText();
		if(name.length() == 0)
			name = "Anomymous";
		
		// TODO Go to the server.

		parent.renameItem(position, name);
		loggedOut.setVisible(false);
		loggedIn.setVisible(true);
		parent.clear();
	}

	void logoff()
	{
		parent.renameItem(position, "Login/Register");
		loggedOut.setVisible(true);
		loggedIn.setVisible(false);
		parent.clear();
	}
	
}
