package tagcomp.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Paquete de recursos (css, imagenes) para uso en aplicaciones GWT.
 * @author Juan Peralta
 * 		<br>TODO  Completar proyectos que faltan con esta modalidad de obtencion de recursos.
 * 		<br>Para estos casos se puede modificar el css con la anotación @external para las clases que presentan conflicto
 * @note Si no se desea modificar el styles.css ni generar una interfaz con accessors se puede utilizar la anotacion
 * 		@NotStrict, la que permite ignorar los errores por concepto de clases no ofuscadas dentro del css.
 * 		<br> Se deja mientras tanto como NotStrict, para no ser invasivo con el codigo ya generado
 * @modo_de_uso Estilos: Para agregar estilos debe agregarse la siguiente linea en el onModuleLoad(): 
 * 				 <code>ResourceBundle.instance.css().ensureInjected()</code>.
 * 				 <br> Imagenes: debe reemplazarse el argumento path de una imagen por una llamada a ResourceBundle. Se debe reemplazar por 
 * 				 <code>ResourceBundle.instance.<imagen>.getURL()</code>. Recordar que se puede instanciar un widget Image como new Image(ImageResource).
 * 				 <br> Si se desea ver como se aplican estos cambios revisar proyecto WebFacturacion_v06102010.
 * @note versiones de GWT inferiores a la 2.1 generan una imágen usando Styles y Background (CSS), lo que las vuelve incompatibles con cambios de tamaño.
 */
public interface SharedResources extends ClientBundle
{
	public static final SharedResources instance = GWT.create(SharedResources.class);

	@Source("create.png")
	public ImageResource create();

	@Source("delete.png")
	public ImageResource delete();
	
	@Source("go.png")
	public ImageResource go();
	
	@Source("save.png")
	public ImageResource save();
	
	@Source("undo.png")
	public ImageResource undo();
	
	@Source("delete.png")
	public ImageResource loading();
	
	@Source("delete.png")
	public ImageResource ok();
}