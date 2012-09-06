package tagcomp.widgets;


import tagcomp.resources.SharedResources;
import tagcomp.text.Validator;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Clase equivalente a un TextBox normal, pero que es capaz de validar el input a medida que se coloca texto.
 * Si el texto es válido en un momento determnado se muestra un check al lado derecho.
 * @author XXX
 * @note DL: Este código fue creado por alguien más y no documentado. Es posible que fuera más conveniente heredar de TextBox directamente pero no exploré la opción, limitándome a reparar lo que estaba hecho.
 */
public class ValidatedTextBox extends Composite
{
	private TextBox txb;						// El input de texto
	private Image okImage;					// La imagen de validación OK.
	private boolean acceptWhitespace;	// Si que no haya input está OK.
	private Validator validator;			// El validador a usar.

	/**
	 * Crea un nuevo textbox validado. 
	 * @param validator Validador a usar.
	 * @param acceptWhitespace Si que no haya input está OK.
	 */
	public ValidatedTextBox(Validator validator, boolean acceptWhitespace)
	{
		this.validator = validator;
		this.acceptWhitespace = acceptWhitespace;
		
		okImage = new Image(SharedResources.instance.ok());
		okImage.setVisible(false);
		okImage.setStyleName("mediumIcon");

		VerticalPanel vp = new VerticalPanel();
		vp.add(txb);
		vp.add(okImage);
		initWidget(vp);
		setStyleName("validatedTextBox");

		txb = new TextBox();
		txb.addChangeHandler(new ChangeHandler()
		{
			public void onChange(ChangeEvent event)
			{
				okImage.setVisible(isValid());
			}
		});
	}

	public String getText()
	{
		return txb.getText();
	}

	public void setText(String text)
	{
		txb.setText(text);
		okImage.setVisible(isValid());
	}

	public void setEnabled(boolean enabled)
	{
		txb.setEnabled(enabled);
	}

	public void setMaxLength(Integer length)
	{
		txb.setMaxLength(length);
	}

	public void setTexboxStyleName(String style)
	{
		txb.setStyleName(style);
	}
	
	public boolean isValid()
	{
		if(acceptWhitespace && txb.getText().isEmpty())
			return true;
		return validator.validate(txb.getText());
	}
}
