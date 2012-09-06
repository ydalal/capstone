package tagcomp.widgets;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Evento de cambio de estado. 
 * Esta clase no contine elementos externos adicionales, sino que actúa como una señal.
 * @author Daniel Langdon
 */
public class StateChangeEvent extends GwtEvent<StateChangeHandler>
{
	/**
	 * Tipo del evento (a ser usado para registrar un StateChangeHandler).
	 */
	public static final Type<StateChangeHandler> TYPE = new Type<StateChangeHandler>();

   @Override
   protected void dispatch(StateChangeHandler handler) 
   {
       handler.onStateChange(this);
   }

	@Override
	public Type<StateChangeHandler> getAssociatedType()
	{
		return TYPE;
	}
}
