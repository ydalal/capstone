package tagcomp.widgets;

import tagcomp.text.Formatter;
import tagcomp.text.Validator;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;


/**
 * IMPORTANTE Requiere las librerias eft-utils-text y eft-utils-gwt-text
 */
public class LabelTextBox extends Composite implements HasValueChangeHandlers<String>, ClickHandler, BlurHandler, KeyPressHandler
{
	private Label l = new Label();
	private TextBox tb = new TextBox();
	
	private Validator validator;
	private Formatter formatter;
	boolean acceptsEmpty, enable = true, password = false;
	
	public LabelTextBox()
	{
		this.acceptsEmpty = true;
		
		FlowPanel wrapper = new FlowPanel();
		initWidget(wrapper);

		l.addClickHandler(this);
		l.addStyleName("pointer");
		wrapper.add(l);
		
		tb.setVisible(false);
		tb.addKeyPressHandler(this);
		tb.addBlurHandler(this);
		wrapper.add(tb);
	}

	public void onClick(ClickEvent event)
	{
		if(enable)
		{
			// Cambio el widget visible.
			l.setVisible(false);
			tb.setVisible(true);
			tb.setFocus(true);
		}
	}
	
	private void attemptSave()
	{
		// Evito doble llamada
		if(l.isVisible())
			return;
		
		// Valido vacíos o validadores (si existen)
		if(validator != null && !validator.validate(tb.getText()) || tb.getText().isEmpty() && !acceptsEmpty)
		   tb.addStyleName("tb_error");

		// Cambio el texto e invierto los widgets.
		else
		{
			tb.removeStyleName("tb_error");
			
			if(tb.getText().isEmpty())
			{
				l.setText("Click para editar");
				l.addStyleName("clickEdit");
			}
			else
			{
				if(formatter != null)
					l.setText(formatter.format(tb.getText()));
				else
					l.setText(tb.getText());
				l.removeStyleName("clickEdit");				
			}
			
			l.setVisible(true);
			tb.setVisible(false);
			ValueChangeEvent.fire(this, tb.getText());
		}
	}
	
	public void onBlur(BlurEvent event)
	{
		attemptSave();
	}
	
	public void onKeyPress(KeyPressEvent event)
	{
		if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
			attemptSave();
	}
	
	/**
	 * Método que permite setear la validación que se desea realizar antes de realizar cambios
	 * @param validator Objeto de tipo InterfaceValidator
	 */
	public void setValidator(Validator v, boolean acceptsEmpty)
	{
		this.acceptsEmpty = acceptsEmpty;
		this.validator = v;
	}
	
	/**
	 * Método que setea que tipo de formato se desea aplicar al LabelTextBox
	 * @param f Formatter a utilizar. 
	 */
	public void setFormatter(Formatter f)
	{
		this.formatter = f;
	}
	
	/**
	 * @return El texto actual, sin el formato mostrado por el label.
	 * @note El texto retornado podría no ser válido, si el widget se encuentra abierto para edición.
	 */
	public String getText()
	{
		return tb.getText();
	}
	
	/** Coloca el texto enviado.
	 * Si el texto es inválido, el widget mostrado será editable y quedará marcado con un error. 
	 * @param str El texto a colocar.
	 */
	public void setText(String str)
	{
		l.setVisible(false);
		tb.setVisible(true);
		tb.setText(str);
		attemptSave();
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler)
	{
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	public boolean isEditing()
	{
		return tb.isVisible();
	}
	
	public void setEnabled(boolean enable)
	{
		this.enable = enable;
	}
	
	public boolean isEnabled()
	{
		return enable;
	}
	
	public void setMaxLength(int length)
	{
		tb.setMaxLength(length);
	}
}