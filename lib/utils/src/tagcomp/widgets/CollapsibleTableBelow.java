package tagcomp.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CollapsibleTableBelow extends FlexTable 
{
	private class Title extends Composite implements ClickHandler, HasClickHandlers
	{
		private CollapsibleTableBelow parentTable;
		private int index;
		private Image arrow;
		
		private Title(int index, Widget w, CollapsibleTableBelow parent)
		{
			parentTable = parent;
			this.index = index;
			
			arrow = new Image(invisibleImageSrc);
			arrow.setStyleName("CollapsibleTableArrow");
			arrow.addClickHandler(this);
			
//			this.addClickHandler(this);
			
			FlowPanel fl = new FlowPanel();
			fl.add(arrow);
			fl.add(w);
			initWidget(fl);
		}
		
		public void setVisible(boolean open)
		{
			if(open)
			{
				arrow.setUrl(visibleImageSrc);
			}
			else
			{
				arrow.setUrl(invisibleImageSrc);
			}
		}
		
		public void onClick(ClickEvent event)
		{
			parentTable.toggle(index);
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler)
		{
			return addDomHandler(handler, ClickEvent.getType());
		}
	}
	
	private String invisibleImageSrc, visibleImageSrc;
	
	public CollapsibleTableBelow(String invisibleImageSrc, String visibleImageSrc)
	{
		super();
		this.invisibleImageSrc = invisibleImageSrc;
		this.visibleImageSrc = visibleImageSrc;
	}
	
	public void setTitle(int row, Widget w)
	{
		// creo el titulo a colocar
		Title t = new Title(row, w, this);
		
		// coloco el titulo y creo al menos un elemento para ocultar 
		super.setWidget(row * 2, 0, t);
		super.prepareRow((row * 2) + 1);
		
		getFlexCellFormatter().setColSpan(row * 2, 0, 100);
	}
	
	public void setTitle(int row, String text)
	{
		setTitle(row, new Label(text));
	}
	
	@Override
	public void setText(int row, int column, String text)
	{
		super.setText((row * 2) + 1, column, text);
		getRowFormatter().setVisible((row * 2) + 1, false);
	}
	
	@Override
	public void setWidget(int row, int column, Widget widget)
	{
		super.setWidget((row * 2) + 1, column, widget);
		getRowFormatter().setVisible((row * 2) + 1, false);
	}

	//TODO al parecer esta funcion no se llama nunca, en cuyo caso se debe eliminar
	public void onClick(ClickEvent event)
	{
		// obtenemos el titulo
		Title t = (Title)event.getSource();
		if(t != null)
		{
			toggle(t.index);
		}
	}
	
	public boolean toggle(int row)
	{
		boolean visible = !getRowFormatter().isVisible((row * 2) + 1);
		getRowFormatter().setVisible((row * 2) + 1, visible);
		((Title)getWidget(row * 2, 0)).setVisible(visible);
		return visible;
	}

	public void setColSpan(int row, int column, int colspan)
	{
		getFlexCellFormatter().setColSpan((row * 2) + 1, column, colspan);
	}
}
