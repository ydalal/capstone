package tagcomp.widgets;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interfaz para aquellas clases que sean capaces de manejar un cambio de estado.
 * @author Daniel Langdon
 */
public interface StateChangeHandler extends EventHandler 
{
	/**
	 * Efectúa las acciones necesarias en caso que se produzca un cambio de estado.
	 * @param event Evento del cambio.
	 */
    void onStateChange(StateChangeEvent event);
}
