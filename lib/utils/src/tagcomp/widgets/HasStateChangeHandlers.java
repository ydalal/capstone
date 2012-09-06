package tagcomp.widgets;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Interfaz que indica que se permite registrar handlers de cambio de estado.
 * @author Daniel Langdon
 */
public interface HasStateChangeHandlers
{
	/**
	 * Registra un nuevo handler de cambio de estado.
	 */
	HandlerRegistration addStateChangeHandlers(StateChangeHandler handler);
}
