package tagcomp.widgets;

import java.util.ArrayList;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Clase que permite una navegación principal para una página.
 * El menu permite mostrar links directos a aplicaciones de primer nivel, así como agregar sub-menues genéricos para alcanzar otras opciones de navegación.
 * Los ítemes del menu son desplegados en forma horizontal, en un único nivel. La cantidad de elementos que caben horizontalmente puede alterarse meciante los siguientes estilos: 
 *  <ul>
 *  <li>.menu : Estilo para el menu principal.</li>
 *  <li>.menu td : Estilo asignado a cada uno de los ítemes del menú principal.</li>
 *  <li>.menu .selected : Estilo para el ítem del menú que se encuentra actualmente seleccionado (activo).</li>
 *  <li>.menu .hover : Estilo para el ítem del menú sobre el cual está puesto el mouse. </li>
 *  <li>.menu .nosub : Estilo (adicional a .menu td) para ítems de primer nivel, que no contienen un sub-menú. por ejemplo para dar bordes inferiores redondeados.</li>
 *  <li>.menu .space : Estilo para los espacioadores.</li>
 *  <li>.submenu : Estilo adicional para los sub-menús (se abren en un popup independiente).</li>
 *  <li>.bigger : Estilo para aquellos sub-menús que resultan más grandes que el encabezado de primer nivel.</li>
 *  <li>.menu-glass : Estilo para el panel secundario que se abre en caso de que se abra un sub-menú en forma modal.</li>
 *  </ul>
 * 
 * @author Daniel Langdon
 */
public class Menu extends Widget
{
	/** Estructura simple para almacenar la estructura interna del los ítems del menú. */
	private class Item
	{
		Element	td;						// Elemento de HTML que corresponde a este ítem.
		Widget	content;					// Contenido adicional en caso de un sub-menú.
		Anchor 	target;					// Link al que apunta el ítem en caso de ser de primer nivel.
	}

	private Element tr;					// Elemento TR de la tabla que incorpora todos los ítems. El menú contempla una sola fila.
	private ArrayList<Item>	items;	// Lista de ítems del menú principal. Los índices en esta lista no necesariamente guardan relación alguna con los de "tr".
	private PopupPanel popup;			// Pop-up único en el que mostrar submenús.
	private int	selected;				// Indicador del ítem que se encuentra seleccionado.
	private int	hover;					// Indicador del ítem sobre el que se encuentra el menú (que puede haber pasado al contenido del submenú).

	/**
	 * Constructor del menú vacío, sin ítems de ningun tipo.
	 */
	public Menu()
	{
		// Creo la estructura base del menu.
		Element table = DOM.createTable();
		Element body = DOM.createTBody();
		tr = DOM.createTR();
		setElement(table);

		DOM.appendChild(table, body);
		DOM.appendChild(body, tr);

		// Inicialización general.
		setStyleName("menu");
		items = new ArrayList<Item>();
		selected = -1;
		hover = -1;

		// Configuro el popup
		popup = new PopupPanel(false);
		popup.setStyleName("submenu");
		popup.setGlassStyleName("menu-glass");
		
		popup.addDomHandler(new MouseOutHandler()
		{
			/**
			 * Oculta el submenu y deselecciona el mouseOver.
			 */
			public void onMouseOut(MouseOutEvent event)
			{
				if(!popup.isModal())
					clear();
			}
		}, MouseOutEvent.getType());
		
		// Necesito hundir estos eventos para que pueda manejarlos.
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONFOCUS
				| Event.ONBLUR | Event.ONCLICK );
	}

	/**
	 * Agrega un ítem del primer nivel al menu, es decir uno que direcciona directamente a una aplicación o link.
	 * @param title El nombre del ítem.
	 * @param target El link a utilizar.
	 * @param index El índice en el que insertar el ítem. En caso de ser mayor que el el número de ítems existentes se agrega al final. 
	 */
	public void addItem(String title, Anchor target, int index)
	{
		Item item = new Item();
		item.target = target;
		item.td = DOM.createTD();
		item.td.setInnerText(title);
		item.td.addClassName("nosub");
		items.add(item);
		DOM.insertChild(tr, item.td, index);
	}
	
	/**
	 * Agrega un ítem del primer nivel al menu, que muestra un sub-menú flotante al pasar sobre él.
	 * @param title El nombre del ítem.
	 * @param widget El contenido a agregar al popup.
	 * @param index El índice en el que insertar el ítem. En caso de ser mayor que el el número de ítems existentes se agrega al final. 
	 */
	public void addSubMenu(String title, Widget content, int index)
	{
		Item item = new Item();
		item.content = content;
		item.td = DOM.createTD();
		item.td.setInnerText(title + "▾"); // Otros caracteres que pueden usarse para demarcar un menu: ‣ ▸ ▾ » › ↴ ⇩ ⤵ ◢ ‣  
		items.add(item);
		DOM.insertChild(tr, item.td, index);
	}

	/**
	 * Agrega un separador al menú, en el índice seleccionado.
	 * @param index Posición en la que agragar el separador. En caso de ser mayor se coloca al final.
	 * @param width Ancho del separador.
	 */
	public void addSeparator(int index, int width)
	{
		Element td = DOM.createTD();
		td.addClassName("space");
		td.setPropertyInt("width", width);
		DOM.insertChild(tr, td, index);
	}
	
	/**
	 * Escucha por eventos. Es público solo por necesidad de sobrecargar el método original, pero no debiera ser utilizado para usar el menú.
	 */
	@Override
	public void onBrowserEvent(Event event)
	{
		// Busco el elemento que se creó.
		int current = 0;
		Element target = DOM.eventGetTarget(event);

		for (Item item : items)
		{
			if (DOM.isOrHasChild(item.td, target))
				break;
			current++;
		}
		if (current < 0 || current == items.size())
			return;
		
		Item item = items.get(current);
		// Veo el tipo de evento.
		switch (DOM.eventGetType(event))
		{
			case Event.ONFOCUS:
			case Event.ONMOUSEOVER:
			{
				hover = current;
				showPopup(item);
				break;
			}
			case Event.ONMOUSEOUT:
			case Event.ONBLUR:
			{
				// Si salgo hacia el popup no hago nada, sino, limpio todo.
				EventTarget et = event.getRelatedEventTarget();	
				if (!Element.is(et) || !popup.getElement().isOrHasChild(Element.as(et))) 
					clear();
				break;
			}
			case Event.ONCLICK:
			{
				if(item.target != null)
					item.target.onBrowserEvent(event);
				break;
			}
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Muestra el popup basado en el ítem recibido.
	 * @param item Elemento a mostrar.
	 */
	private void showPopup(final Item item)
	{
		// Abro consistentemente el menu como si hubiera volado sobre él.
		hover = items.indexOf(item);
		item.td.addClassName("hover");
		
		// Si hay submenu, lo muestro.
		if(item.content != null)
		{
			// Coloco el nuevo menu. Reseteo el tamaño en caso de que lo haya dejado fijo, ya que puede haber cambios en el contenido mismo (ej: menu de sesión)
			popup.setWidget(item.content);
			item.content.setWidth("auto");
			
			popup.setPopupPositionAndShow(new PopupPanel.PositionCallback()
			{
				public void setPosition(int offsetWidth, int offsetHeight)
				{
					popup.setPopupPosition(item.td.getAbsoluteLeft(), item.td.getAbsoluteBottom());
				
					// Garantizo que el submenu nunca sea menor que el título.
					if(item.content.getOffsetWidth() <= item.td.getOffsetWidth())
					{
						item.content.setWidth(item.td.getOffsetWidth() + "px");
						popup.removeStyleName("bigger");
					}
					else
					{
						popup.addStyleName("bigger");
					}
				}
			});
		}
	}
	
	/**
	 * Limpia el item actualmente visible. Ocultando el popup de ser necesario.
	 * @note Esto no modifica en ninguna forma la selección.
	 */
	public void clear()
	{
		// Existen casos en que la variable "hover" ya se perdió.
		if(hover >= 0)
			items.get(hover).td.removeClassName("hover");
		popup.setModal(false);
		popup.setGlassEnabled(false);
		popup.hide();
	}
	
	/**
	 * @return el índice del elemento actualmente seleccionado.
	 */
	public int getSelected()
	{
		return selected;
	}

	/**
	 * @param selected el índice del elemento que se quiere seleccionar.
	 */
	public void setSelected(int selected)
	{
		if(this.selected != selected)
		{
			unselect();
			this.selected = selected;
			if(selected >=0 )
				items.get(selected).td.addClassName("selected");
		}
	}
	
	/**
	 * Muestra el item en cuestión en forma programática, bloqueandolo además si se requiere.
	 * @param index índice del ítem a mostrar.
	 * @param modal true si se quiere mostrar en forma modal, es decir, sin tomar en cuenta otros eventos de usuario a menos que se llame programáticamente a clear(). Los eventos en el popup, sin embargo, no son bloqueados. 
	 */
	public void showItem(int index, boolean modal)
	{
		// Olvido lo que estaba seleccionado y muestro lo nuevo.
		clear();
		popup.setModal(modal);
		popup.setGlassEnabled(modal);
		showPopup(items.get(index));
	}
	
	/**
	 * Esconde cualquier sub-menú abierto y selecciona el ítem sobre el que se encuentraba el mouse.
	 */
	public void clearAndSelect()
	{
		clear();
		setSelected(hover);
	}

	/**
	 * Desselecciona el ítem seleccionado.
	 */
	public void unselect()
	{
		if(selected >= 0)
		{
			items.get(selected).td.removeClassName("selected");
			selected = -1;
		}
	}
	
	/**
	 * @param includeSeparators true si se deben contabilizar los separadores en la cuenta.
	 * @return El número de ítems en el menú.
	 */
	public int size(boolean includeSeparators)
	{
		return (includeSeparators) ? tr.getChildCount() : items.size();
	}
}
