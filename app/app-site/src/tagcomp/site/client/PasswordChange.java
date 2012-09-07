package tagcomp.site.client;

import tagcomp.text.CommonValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Aplicación para cambiar de clave en el portal.
 * En general es bastante simple. Acepta claves con letras y números.
 * @author Daniel Langdon
 * @note No hay permisos ni configurador asociados a esta aplicación, la única restricción es tener acceso a la aplicación en primer lugar.
 * @serial portal.passChange
 */
public class PasswordChange extends Composite
{
	private static PasswordChangeUiBinder uiBinder	= GWT.create(PasswordChangeUiBinder.class);
	interface PasswordChangeUiBinder extends UiBinder<Widget, PasswordChange>{}

	@UiField PasswordTextBox actual;		// Para colocar la clave actual.
	@UiField PasswordTextBox newPass;	// Para colocar la nueva clave.
	@UiField PasswordTextBox confirm;	// Para confirmar la nueva clave.
	@UiField Button submit;					// Para enviar el formulario con los datos.
	
	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public PasswordChange()
	{
		initWidget(uiBinder.createAndBindUi(this));
		actual.setFocus(true);
//		Portal.getStatus().setStatus("La clave debe contener entre 3 y 12 letras o dígitos", StatusStyle.NORMAL, 0);
	}

	/**
	 * Valida las claves ingresadas y de estar correctas envía la actualización al servidor.
	 * @param e Evento original de envío.
	 */
	@UiHandler("submit")
	void onClick(ClickEvent e)
	{
		if(actual.getValue().isEmpty() || !CommonValidator.ALPHA.validate(actual.getValue()))
		{
//			Portal.showError("Clave actual incorrecta.");
		}
		else if(newPass.getValue().isEmpty() || !CommonValidator.ALPHA.validate(newPass.getValue()))
		{
//			Portal.showError("Clave nueva incorrecta.");
		}
		else if(!confirm.getValue().equals(newPass.getValue()))
		{
//			Portal.showError("La nueva clave no coincide.");
		}
		else
		{
//			Portal.getService().changePassword(newPass.getValue(), new AsyncCallback<Void>()
//			{
//				public void onSuccess(Void result)
//				{
//					Portal.showMessage("Clave cambiada exitosamente.");
//				}
//				
//				public void onFailure(Throwable caught)
//				{
//					Portal.showError(caught.getMessage());
//				}
//			});
		}
	}
}
