package tagcomp.widgets;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Flextable que permite agrupar una o más filas en un agrupados colocado en una
 * fila superior. la clase introduce un nuevo tipo de mapeo a las filas de la
 * tabla, donde pueden ser vistas como un par (grupo,hijo).
 * 
 * @note Ojo con operaciones que eliminen filas directamente sin hacer uso de
 *       esta estructura, pueden causar problemas con los índices de los grupos.
 *       TODO Podrían ser necesario un método que remueva un índice o fila.
 * @author Daniel Langdon
 */
public class GroupedTable extends FlexTable implements ClickHandler
{
    // Statics
    // --------------------------------------------------------------------------------------------
    /**
     * Clase que agrupa un índice (grupo,hijo).
     */
    public class Index
    {
	public int group;
	public int child;

	public Index(int group, int child) {
	    super();
	    this.group = group;
	    this.child = child;
	}
    }

    // Commons
    // --------------------------------------------------------------------------------------------
    private String invisibleImageSrc, visibleImageSrc;
    private ArrayList<Integer> groups;

    /**
     * Constructor base.
     * 
     * @param invisibleImageSrc
     *            Imagen a mostrar para agrupadores colapsados.
     * @param visibleImageSrc
     *            Imagen a mostrar para agrupadores expandidos.
     */
    public GroupedTable(String invisibleImageSrc, String visibleImageSrc) {
	super();
	groups = new ArrayList<Integer>();
	this.invisibleImageSrc = invisibleImageSrc;
	this.visibleImageSrc = visibleImageSrc;
    }

    /**
     * Retorna si una fila corresponde a un agrupador.
     */
    public boolean isGroup(int row)
    {
	return groups.contains(row);
    }

    /**
     * @return El número de grupos actualmente exisntente.
     */
    public int getGroupCount()
    {
	return groups.size();
    }

    /**
     * Inserta un nuevo grupo antes que uno existente.
     * 
     * @param before
     *            Grupo antes del cual realizar la inserción. De no existir lo
     *            crea al final de la tabla.
     * @param widget
     *            Widget a insertar en la segunda columna (como título o
     *            agrupador).
     * @param span
     *            Spanning a usar para la segunda columna.
     * @return Fila de la tabla donde se agregó el grupo.
     */
    public int insertGroup(int before, Widget widget, int span)
    {
	// Detecto la fila a insertar.
	int row = 0;
	if (before >= groups.size() || before < 0)
	{
	    row = this.getRowCount();
	    groups.add(row);
	}
	else
	{
	    // Inserto al centro, moviendo todos los indices.
	    row = this.insertRow(groups.get(before));
	    groups.add(0);
	    for (int i = groups.size() - 1; i > before; i--)
		groups.set(i, groups.get(i - 1));
	    groups.set(before, row);
	}

	// Coloco la flecha de expansión.
	Image arrow = new Image(visibleImageSrc);
	arrow.setTitle("Ocultar");
	arrow.setStyleName("smallIcon");
	arrow.addClickHandler(this);
	this.setWidget(row, 0, arrow);

	// Coloco el widget de entrada.
	this.setWidget(row, 1, widget);
	getFlexCellFormatter().setColSpan(row, 1, span);
	getRowFormatter().setStyleName(row, "header2");
	return row;
    }

    /**
     * Inserta tantos hijos como sea necesario para dejar "child" hijos en el
     * grupo "group" ya existente, moviendo todo lo demás hacia abajo. Nótese
     * que insertar hijos al último grupo es gratuito y puede hacerse
     * directamente creando celdas en la tabla.
     * 
     * @param group
     *            El grupo ya existente al que se debe agregar hijos.
     * @param child
     *            El índice del hijo a insertar. De ya existir un hijo con este
     *            índice se mueve hacia abajo. De no existir son creados tantas
     *            filas como sean necesarias para situar al hijo.
     */
    public int insertChild(int group, int child)
    {
	// obtengo el numero de hijos y no hago nada si el grupo no existe.
	int children = getChildrenCount(group);
	if (children < 0)
	    return -1;

	// Determino cuantas filas agregar y donde.
	int toAdd = 1;
	int row = groups.get(group) + child + 1;

	if (children <= child)
	{
	    toAdd = child - children + 1;
	    row = groups.get(group) + children + 1;
	}

	// Actualizo los índices de los grupos arrastrados.
	for (int g = group + 1; g < groups.size(); g++)
	    groups.set(g, groups.get(g) + toAdd);

	// Agrego las filas correspondientes.
	// OJO que insertRow es inteligente si agregamos fuera de la tabla, no
	// se cae y agreag al final.
	for (int i = 0; i < toAdd; i++)
	    this.insertRow(row);
	return row + toAdd - 1;
    }

    /**
     * Retorna el número de hijos del grupo.
     */
    public int getChildrenCount(int group)
    {
	// Busco si el grupo existe.
	if (group >= groups.size())
	    return -1;

	// Si los hijos llegan hasta el grupo sigueinte.
	if (group < groups.size() - 1)
	    return groups.get(group + 1) - groups.get(group) - 1;

	// Sino los hijos son todos hasta el final.
	return this.getRowCount() - groups.get(group) - 1;
    }

    /**
     * Retorna la fila de la tabla donde se encuentra el hijo correspondiente al
     * grupo. Si el grupo no existe o tiene menos hijos, retorno -1.
     */
    public int groupToRow(int group, int child)
    {
	int children = getChildrenCount(group);
	if (children < 0 || children < child)
	    return -1;
	return groups.get(group) + child + 1;
    }

    /**
     * Retorna el índice correspondiente a una fila.
     * 
     * @param row
     *            Fila a consultar.
     * @return Un par grupo-hijo o null si la fila no corresponde a un grupo.
     */
    public Index rowToIndex(int row)
    {
	// Busco un grupo posterior a la fila.
	int i;
	for (i = 0; i < groups.size(); i++)
	{
	    if (groups.get(i) > row)
		break;
	}

	// Luego selecciono el grupo anterior (de existir)
	if (i == 0)
	    return null;
	return new Index(i - 1, row - groups.get(i - 1) - 1);
    }

    /**
     * Administra ocultar y mostrar elementos en la tabla de acuerdo a las
     * flechas de expandir/mostrar en cada grupo.
     */
    public void onClick(ClickEvent event)
    {
	// Obtengo la celda y la imagen
	HTMLTable.Cell cell = this.getCellForEvent(event);
	boolean visible = (((Image) event.getSource()).getTitle().compareTo("Expander")==0);
	this.setExpansion(cell.getRowIndex(), !visible);
    }

    /**
     * Expande o colapsa un grupo basado en una fila específica. El grupo
     * colapsado es el inmediatamente superior a la fila seleccionada. De no
     * existir grupos este método no hace nada. Se recibe la fila en vez del
     * grupo porque es más óptimo y potencialmente un caso de uso más común.
     * 
     * @param row
     *            Una fila cualquiera de la tabla.
     * @param visible
     *            Si el grupo debe estar expandido o no.
     */
    public void setExpansion(int row, boolean visible)
    {
	// Valido que la fila pertenezca a un grupo y detecto cual y que viene
	// después.
	int start = -1;
	int end = this.getRowCount();
	for (Integer g : groups)
	{
	    if (g > row)
	    {
		end = g;
		break;
	    }
	    start = g;
	}
	if (start == -1)
	{
	    return;
	}

	// Cambio la imagen
	Image arrow = (Image) this.getWidget(start, 0);
	arrow.setUrl(visible ? visibleImageSrc : invisibleImageSrc);
	arrow.setTitle(visible ? "Expander":"Ocultar");

	// Escondo todas las filas hijo hasta el sgte grupo o el final de la
	// tabla.
	for (int i = start + 1; i < end; i++)
	{
	    getRowFormatter().setVisible(i, visible);
	}
    }

    /**
     * Elimina todas las celdas de la tabla y todos los grupos asociados.
     */
    @Override
    public void removeAllRows()
    {
	super.removeAllRows();
	groups.clear();
    }
}
