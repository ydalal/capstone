package tagcomp.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * Widget que encapsula una secuencia de imágenes, pudiendo rotan entre ellas con un click.
 * Cada vez que la imagen mostrada cambia se emite un StateChangeEvent
 * @author Daniel Langdon
 */
public class StateIcon extends Composite implements ClickHandler, HasStateChangeHandlers
{
	private ImageResource[] images;		// Lista de imágenes a mostrar.
	private String[] descriptions;		// Lista de descripciones.
	private int current;						// Indice de la imagen actual.
	private Image image;						// Imagen actualmente mostrada.

	/**
	 * Inicializa el boton de estados con iconos sin tooltips.
	 * @param images los iconos a mostrar, en orden.
	 */
	public StateIcon(ImageResource[] images)
	{
		this(images, null);
	}
	
	/**
	 * Inicializa el boton de estados con iconos sin tooltips.
	 * @param images los iconos a mostrar, en orden.
	 * @param descriptions Descripciones para los estados. Por contrato, debe tener el mismo número de elementos que la lista de imágenes.
	 */
	public StateIcon(ImageResource[] images, String[] descriptions)
	{
		this.images = images;
		this.descriptions = descriptions;

		image = new Image();
		image.addClickHandler(this);
		
		setCurrentState(0);
		initWidget(image);
	}
	
	/**
	 * Método que se ejecuta al hacer click en la imágen.
	 */
	public void onClick(ClickEvent event)
	{
		setCurrentState((current+1)%images.length);
		this.fireEvent(new StateChangeEvent());
	}
	
	/** 
	 * Coloca la imágen seleccionada.
	 * Este cambio no emite un StateChangeEvent
	 * @param current Indice a mostrar.
	 */
	public void setCurrentState(int current)
	{
		this.current = current;
		image.setResource(images[current]);
		if(descriptions != null)
			image.setTitle(descriptions[current]);
	}
	
	/**
	 * @return El índice del icono actualmente mostrado.
	 */
	public int getCurrentState()
	{
		return current;
	}

	public HandlerRegistration addStateChangeHandlers(StateChangeHandler handler)
	{
		return this.addHandler(handler, StateChangeEvent.TYPE);
	}
}