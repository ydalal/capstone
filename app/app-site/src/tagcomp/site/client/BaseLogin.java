package tagcomp.site.client;

import tagcomp.text.CommonFormatter;
import tagcomp.text.CommonValidator;
import tagcomp.widgets.StatusLabel;
import tagcomp.widgets.StatusLabel.StatusStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Daniel Langdon
 */
public class BaseLogin implements EntryPoint
{
	private PasswordTextBox pwr;
	private TextBox tb;
	private StatusLabel status;
//	private PortalServiceAsync service = GWT.create(PortalService.class);
	
	public void onModuleLoad()
	{
//		ResourceBundle.instance.css().ensureInjected();
		status = new StatusLabel();
		RootPanel.get("statusContainer").add(status);

		pwr = PasswordTextBox.wrap(Document.get().getElementById("password"));
		pwr.addKeyDownHandler(new KeyDownHandler()
		{
			public void onKeyDown(KeyDownEvent event)
			{
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					submitLogin();
			}
		});
		pwr.addKeyPressHandler(new KeyPressHandler() 
		{
			public void onKeyPress(KeyPressEvent event) 
			{
				if (!Character.isLetterOrDigit(event.getCharCode())) 
					((PasswordTextBox) event.getSource()).cancelKey();
			}
		});
		
		// Valido y doy formato al rut de ingreso. Esto podría ser customizad para otros tipos de ngreso.
		tb = TextBox.wrap(Document.get().getElementById("user"));
		tb.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				if(CommonValidator.RUT.validate(tb.getValue()))
				{
					tb.setValue(CommonFormatter.RUT.format(tb.getValue()));
				}
			}
		});
		tb.setFocus(true);
		
		// Valido que el formulario solo sea enviado con una clave razonable.
		Button submit = Button.wrap(Document.get().getElementById("submit"));
		submit.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				submitLogin();
				event.getNativeEvent().preventDefault();
			}
		});
	}
	
	void submitLogin()
	{
		String error = null;
		if(!CommonValidator.RUT.validate(tb.getValue()))
			error = "Debe ingresar un rut válido.";
		else if(pwr.getValue().isEmpty() || !CommonValidator.ALPHA.validate(pwr.getValue()))
			error = "Debe ingresar una clave válida (Números o letras)";

		if(error != null)
		{
			status.setStatus(error, StatusStyle.ERROR);
			return;
		}
		
		// Extraigo del rut el cógigo numérico.
		String id = CommonFormatter.CLEAN_ALPHA.format(tb.getValue()); 
		id = id.substring(0, id.length()-1); 
		
		// Enviamos los datos al servidor, para validar el login antes de redireccionar.
//		service.login(id, pwr.getValue(), new AsyncCallback<Integer>()
//		{
//			public void onSuccess(Integer result)
//			{
//				Window.open("../portal.html", "_self", ""); 
//			}
//			
//			public void onFailure(Throwable caught)
//			{
//				status.setStatus(caught.getMessage(), StatusStyle.ERROR);
//			}
//		});
	}
}
