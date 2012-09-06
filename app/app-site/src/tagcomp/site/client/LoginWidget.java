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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWidget extends Composite
{
	private FlexTable loggedIn;
	private FlexTable loggedOff;
	private Menu parent;
	private StatusLabel loginStatus;
	
	private TextBox user;
	private PasswordTextBox pass;

	/**
	 * Creates a new session menu and adds it to the specified menu in the specified position.
	 * @param menu
	 */
	public LoginWidget(Menu menu, int position)
	{
		parent = menu;
		
		loginStatus = new StatusLabel();
		user = new TextBox();
		user.setWidth("100px");

		// Textbox para la clave. Lamentablemente ninguno de los dos eventos siguientes provee tanto keyCode como charCode, por lo que tenemos que colocar ambos.
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
		loggedOff = new FlexTable();
		loggedOff.setStyleName("subtable");
		//loggedOff.getRowFormatter().setStyleName(0, "error");	// For password errors.
		loggedOff.setWidget(0, 0, loginStatus);
		loggedOff.getFlexCellFormatter().setColSpan(0, 0, 3);
		
		loggedOff.setText(1, 0, "user:");
		loggedOff.setWidget(1, 1, user);
		loggedOff.setWidget(1, 2, go);
		loggedOff.getFlexCellFormatter().setRowSpan(1, 2, 2);

		loggedOff.setText(2, 0, "pass:");
		loggedOff.setWidget(2,1, pass);

//		HorizontalPanel hp = new HorizontalPanel();
//		hp.add(register);
//		hp.add(forgot);
//		loggedOff.setWidget(3,0, hp);
//		loggedOff.getFlexCellFormatter().setColSpan(3, 0, 3);

		loggedOff.setWidget(3,0, register);
		loggedOff.getFlexCellFormatter().setColSpan(3, 0, 3);
		loggedOff.setWidget(4,0, forgot);
		loggedOff.getFlexCellFormatter().setColSpan(4, 0, 3);

		
//		loggedOff.setWidget(1,1, login);
//		loggedOff.getFlexCellFormatter().setColSpan(0, 0, 2);
//		loggedOff.setWidget(1,0, pass);
//		loggedOff.setWidget(1,1, login);
//		loggedOff.setWidget(2,1, new Anchor("Cerrar Sessión", rootPath));
//		loggedOff.setVisible(false);
//		loggedOff.setText(0,0, "Sessión expirada.");

		menu.addSubMenu("login/register", loggedOff, 999);
		
/*		
		activeMenu = new FlexTable();
		activeMenu.setStyleName("subtable");
		outMenu
	   
		// Menu cuando la sesión está activa.
		activeMenu.getRowFormatter().setStyleName(0, "categories");
		activeMenu.setText(0,0, "Usuario");
		
		Anchor anchor = new Anchor("Cambiar Clave", "", "portal.passChange");
		anchor.addClickHandler(Portal.getInstance());
		activeMenu.setWidget(1,0, anchor);
		
		anchor = new Anchor("Mis datos", "", "portal.userInfo");
		anchor.addClickHandler(Portal.getInstance());
		activeMenu.setWidget(2,0, anchor);
		
		activeMenu.getRowFormatter().setStyleName(3, "categories");
		activeMenu.setText(3,0, "Sesión");
		activeMenu.setWidget(4,0, new Anchor("Cerrar Sessión", rootPath));
		
		// Menu cuando la sesión ha caducado.
*/		
		
		//this.initWidget(loggedOff);
	}
	

	private void submitLogin()
	{
		// TODO Auto-generated method stub
		loginStatus.setStatus("welcome!", StatusStyle.ERROR);
	}

}
