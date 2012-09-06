package tagcomp.widgets;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Provee in IFrame de invisible que hace un llamado a un servlet
 * con parametros pasados por get. Una vez que se ha cargado, el IFrame se elimina.
 * @author Francisco Perez
 */
public class ServletIFrame extends Frame
{
	private void init(String servletName, String context, String url, boolean isUrl, Map<String, String> params)
	{
		setSize("0px", "0px");
		setVisible(false);
		sinkEvents(Event.ONLOAD);
		RootPanel.get().add(this);
		
		StringBuilder sb = new StringBuilder();		
		if(isUrl)
		{
			sb.append(url);
		}
		else
		{
			sb.append(context).append(servletName);
		}
		if(params != null && params.size() > 0)
		{
			sb.append("?");
			for(Map.Entry<String, String> param : params.entrySet())
			{
				sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
			}
		}
		setUrl(sb.toString());
	}
	
	public ServletIFrame(String url, boolean isUrl) throws Exception
	{
		this(url, null, isUrl);
	}

	
	public ServletIFrame(String url, Map<String, String> params, boolean isUrl) throws Exception
	{
		if(!isUrl)
		{
			throw new Exception("Parameter isUrl must be true!");
		}
		init(null, null, url, true, params);
	}
	
	public ServletIFrame(String servletName)
	{
		this(servletName, GWT.getModuleBaseURL(), null);
	}
	
	public ServletIFrame(String servletName, String context)
	{
		this(servletName, context + "/", null);
	}
	
	public ServletIFrame(String servletName, Map<String, String> params)
	{
		this(servletName, GWT.getModuleBaseURL(), params);
	}
	
	public ServletIFrame(String servletName, String context, Map<String, String> params)
	{
		super();
		init(servletName, context, null, false, params);
	}

	public void onBrowserEvent(Event event)
	{
		if(DOM.eventGetType(event) == Event.ONLOAD)
		{
			unsinkEvents(Event.ONLOAD);
			DOM.eventCancelBubble(event, true);
			RootPanel.get().remove(this);
		}
		else
		{
			super.onBrowserEvent(event);
		}
	}

} 