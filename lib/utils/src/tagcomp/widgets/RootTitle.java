package tagcomp.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Daniel Langdon
 * Clase que encapsula funcionalidad básica del portal en cuanto a:
 * - Título estándar para las aplicaciones
 * - Mensaje estándar en caso de error u otra notificación no bloqueante.
 * - Diálogo modal que deshabilita la página completa para mostrar que se está cargando algo.
 * TODO Hacer que el manejo de sesión (timer, etc) se realice directamente desde aquí.
 */
public class RootTitle
{
	//TODO seria una buena idea agregar warnings y otros
	public enum StatusStyle
	{
		NORMAL("status"), ERROR("error");
		
		private String style;
		
		private StatusStyle(String style)
		{
			this.style = style;
		}
		
		public String getStyle()
		{
			return style;
		}
	};
	
	private static Label status;
	private static Timer deleteTimer;
	private static PopupPanel loadingPanel;
	private static int loading = 0;
	
	/**
	 * Agrega los elementos básicos al RootPanel e inicializa las variables.
	 * @param title Título de la aplicación.
	 */
	public static void init(String title)
	{
		init(title, null);
	}
	
	/**
	 * Agrega los elementos básicos al RootPanel e inicializa las variables y el LoadingPanel.
	 * @param title Título de la aplicación.
	 * @param loadingImgSrc Path de la imagen que se mostrará en el LoadingPanel.
	 */
	public static void init(String title, String loadingImgSrc)
	{
		// Coloca el timer de borrado.
		deleteTimer = new Timer() { public void run() { setStatus(" "); } };
		
		// Colocar título inicial.
		Label t = new Label(title);
		t.setStyleName("title");
		RootPanel.get().add(t);
		
		// Ingreso label de estado.
		status = new Label();		
		RootPanel.get().add(status);
		
		if(loadingImgSrc != null)
		{
			// Inicializo el panel (no auto-hide, modal)
			loadingPanel = new PopupPanel(false, true);
			loadingPanel.setWidget(new Image(loadingImgSrc));
			loadingPanel.setAnimationEnabled(false);
			loadingPanel.setGlassEnabled(true);
			loadingPanel.setStyleName("loadingIcon");
			loadingPanel.hide();
		}
	}
	
	/**
	 * Coloca el texto provisto, borrándolo automáticamente luego del tiempo definido.
	 * @param text Text to set in the status.
	 * @param style El estilo a aplicar.
	 * @param miliseconds Milisegundos a esperar para eliminar el mensaje, si el valor es menor o igual a cero, el texto no se borra
	 * @see #setStatus(String text)
	 */
	public static void setStatus(String text, StatusStyle style, int miliseconds)
	{
		status.setStyleName(style.getStyle());
		status.setText(text);
		if(miliseconds > 0)
		{
			deleteTimer.schedule(miliseconds);
		}
	}
	
	/**
	 * Coloca el texto enviado con el estilo pedido y lo borra despues de 5 segundos.
	 * @param text Mensaje.
	 * @param style Estilo a aplicar.
	 */
	public static void setStatus(String text, StatusStyle style)
	{
		setStatus(text, style, 5000);
	}
	
	/**
	 * Coloca el texto enviado cono una notificacion normal y lo borra despues de 5 segundos.
	 * @param text Mensaje.
	 */
	public static void setStatus(String text)
	{
		setStatus(text, StatusStyle.NORMAL, 5000);
	}
	
	/**
	 * Tapa la aplicación completa con un dialogo modal de manera de bloquear la página completa
	 * El diálogo desaparecerá cuando el contador de llamadas con el parámetro en false sea igual
	 * a la cantidad de llamadas con el parámetro en true.<br>
	 * El contador puede llegar a valores negativos, es responsabilidad de quen llama a esta función
	 * el sincronizar la cantidad de llamados correcta.  
	 * @param loading
	 */
	public static void setLoading(boolean loading)
	{
		if(loadingPanel != null)
		{
			if(loading && RootTitle.loading++ == 0)
			{
				loadingPanel.show();
			}
			else if(--RootTitle.loading == 0)
			{
				loadingPanel.hide();
			}
		}
	}
}
