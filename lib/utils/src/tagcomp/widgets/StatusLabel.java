package tagcomp.widgets;

import tagcomp.resources.SharedResources;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Daniel Langdon
 * Clase que encapsula funcionalidad básica del portal en cuanto a:
 * - Título estándar para las aplicaciones
 * - Mensaje estándar en caso de error u otra notificación no bloqueante.
 * - Diálogo modal que deshabilita la página completa para mostrar que se está cargando algo.
 * TODO Hacer que el manejo de sesión (timer, etc) se realice directamente desde aquí.
 */
public class StatusLabel extends Label
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

	private static StatusLabel instance;
	public static StatusLabel getMain()
	{
		return instance;
	}
	
	private Timer deleteTimer;
	private PopupPanel loadingPanel;
	private int loading = 0;
	
	/**
	 * Agrega los elementos básicos al RootPanel e inicializa las variables y el LoadingPanel.
	 * @param title Título de la aplicación.
	 * @param loadingImgSrc Path de la imagen que se mostrará en el LoadingPanel.
	 */
	public StatusLabel()
	{
		// Coloca el timer de borrado.
		deleteTimer = new Timer() { public void run() { setStatus(" "); } };
		
		if(SharedResources.instance.loading() != null)
		{
			// Inicializo el panel (no auto-hide, modal)
			loadingPanel = new PopupPanel(false, true);
			loadingPanel.setWidget(new Image(SharedResources.instance.loading()));
			loadingPanel.setAnimationEnabled(false);
			loadingPanel.setGlassEnabled(true);
			loadingPanel.setStyleName("loadingIcon");
			loadingPanel.hide();
		}
	}
	
	public StatusLabel(boolean main)
	{
		this();
		if(main)
			instance = this;
	}
	
	/**
	 * Coloca el texto provisto, borrándolo automáticamente luego del tiempo definido.
	 * @param text Text to set in the status.
	 * @param style El estilo a aplicar.
	 * @param miliseconds Milisegundos a esperar para eliminar el mensaje, si el valor es menor o igual a cero, el texto no se borra
	 * @see #setStatus(String text)
	 */
	public void setStatus(String text, StatusStyle style, int miliseconds)
	{
		this.setStyleName(style.getStyle());
		this.setText(text);
		if(miliseconds > 0)
		{
			deleteTimer.schedule(miliseconds);
		}
		else
		{
			deleteTimer.cancel();
		}
	}
	
	/**
	 * Coloca el texto enviado con el estilo pedido y lo borra despues de 5 segundos.
	 * @param text Mensaje.
	 * @param style Estilo a aplicar.
	 */
	public void setStatus(String text, StatusStyle style)
	{
		setStatus(text, style, 5000);
	}
	
	/**
	 * Coloca el texto enviado cono una notificacion normal y lo borra despues de 5 segundos.
	 * @param text Mensaje.
	 */
	public void setStatus(String text)
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
	public void setLoading(boolean loading)
	{
		if(loadingPanel != null)
		{
			if(loading && this.loading++ == 0)
			{
				loadingPanel.show();
			}
			else if(--this.loading == 0)
			{
				loadingPanel.hide();
			}
		}
	}
}
