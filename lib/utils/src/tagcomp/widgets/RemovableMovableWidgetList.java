package tagcomp.widgets;

import java.util.ArrayList;
import java.util.List;

import tagcomp.widgets.HasRemovableMovableWidgetList.Operation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO DL: Falta comentar. 
 */
public class RemovableMovableWidgetList extends VerticalPanel
{
	private enum Direction { UP, DOWN, BOTH, NONE };

	private class RemovableMovableWidget extends FlexTable implements MouseOverHandler, MouseOutHandler, HasMouseOverHandlers, HasMouseOutHandlers, ClickHandler
	{
		private Image removeImage, moveUpImage, moveDownImage;
		private Widget widget;
		private String value;
		private boolean removable;
		
		public RemovableMovableWidget(Widget widget, String value, boolean removable, boolean movable)
		{
			super();
			this.value = value;
			this.removable = removable;
			this.widget = widget;
			this.widget.setWidth(pixelSize + "px");
			setWidget(0, 1, this.widget);
			getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);

			getCellFormatter().setWidth(0, 0, "15px");
			if(removable)
			{
				removeImage = new Image("media/remove.png");
				removeImage.setSize("14px", "14px");
				removeImage.setVisible(false);
				removeImage.addClickHandler(this);
				removeImage.addStyleName("pointer");

				setWidget(0, 0, removeImage);
				getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
			}
			
			if(movable)
			{
				moveUpImage = new Image("media/up.png");
				moveUpImage.setSize("14px", "14px");
				moveUpImage.setVisible(false);
				moveUpImage.addClickHandler(this);
				moveUpImage.addStyleName("pointer");
				
				moveDownImage = new Image("media/down.png");
				moveDownImage.setSize("14px", "14px");
				moveDownImage.setVisible(false);
				moveDownImage.addClickHandler(this);
				moveDownImage.addStyleName("pointer");
				
				getCellFormatter().setWidth(0, 2, "15px");
				getCellFormatter().setWidth(0, 3, "15px");
				getCellFormatter().setAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
				getCellFormatter().setAlignment(0, 3, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
			}
			
			addMouseOverHandler(this);
			addMouseOutHandler(this);
		}
		
		public void setDirection(Direction dir)
		{
			if(movable)
			{
				switch(dir)
				{
				case UP:
					clearCell(0, 3);
					setWidget(0, 2, moveUpImage);
					break;
				case DOWN:
					clearCell(0, 2);
					setWidget(0, 3, moveDownImage);
					break;
				case BOTH:
					setWidget(0, 2, moveUpImage);
					setWidget(0, 3, moveDownImage);
					break;
				case NONE:
					clearCell(0, 2);
					clearCell(0, 3);
					break;
				}
			}
		}
		
		public void onMouseOver(MouseOverEvent event)
		{
			if(enabled)
			{
				if(removable)
				{
					removeImage.setVisible(true);
				}
				if(movable)
				{
					moveUpImage.setVisible(true);
					moveDownImage.setVisible(true);
				}
			}
		}

		public void onMouseOut(MouseOutEvent event)
		{
			if(removable)
			{
				removeImage.setVisible(false);
			}
			if(movable)
			{
				moveUpImage.setVisible(false);
				moveDownImage.setVisible(false);
			}
		}
		
		public String getValue()
		{
			return value;
		}

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler)
		{
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler)
		{
			return addDomHandler(handler, MouseOutEvent.getType());
		}
		
		public void onClick(ClickEvent event)
		{
			if(enabled)
			{
				if(((Image)event.getSource()) == removeImage)
				{
					removeWidget(((Image)event.getSource()).getParent(), true, true);
				}
				else if(((Image)event.getSource()) == moveUpImage)
				{
					moveWidget(this, Direction.UP);
				}
				else if(((Image)event.getSource()) == moveDownImage)
				{
					moveWidget(this, Direction.DOWN);
				}
	
				if(removable)
				{
					removeImage.setVisible(false);
				}
				if(movable)
				{
					moveUpImage.setVisible(false);
					moveDownImage.setVisible(false);
				}
			}
		}
	}
	
	private HasRemovableMovableWidgetList observer;
	private int pixelSize;
	private boolean movable = false, enabled = true;
	
	public RemovableMovableWidgetList(int pixelSize)
	{
		this(null, pixelSize);
	}
	
	public RemovableMovableWidgetList(int pixelSize, boolean movable)
	{
		this(null, pixelSize, movable);
	}
	
	public RemovableMovableWidgetList(HasRemovableMovableWidgetList parent, int pixelSize)
	{
		this(parent, pixelSize, true);
	}
	
	public RemovableMovableWidgetList(HasRemovableMovableWidgetList parent, int pixelSize, boolean movable)
	{
		super();
		this.observer = parent;
		this.pixelSize = pixelSize;
		this.movable = movable;
	}
	
	public void clear()
	{
		super.clear();
	}
	
	public void add(Widget widget, String value)
	{
		add(widget, value, true);
	}
	
	public void add(Widget widget, String value, boolean removable)
	{
		for(Widget w : getChildren())
		{
			if(((RemovableMovableWidget)w).getValue().equals(value))
			{
				return;
			}
		}
		RemovableMovableWidget rmw = new RemovableMovableWidget(widget, value, removable, movable);
		add(rmw);
		if(movable && getWidgetCount() > 1)
		{
			((RemovableMovableWidget)getWidget(0)).setDirection(Direction.DOWN);
			rmw.setDirection(Direction.UP);
			if(getWidgetCount() > 2)
			{
				((RemovableMovableWidget)getWidget(getWidgetCount() - 2)).setDirection(Direction.BOTH);
			}
		}
		
		if(observer != null && enabled)
			observer.listChanged(Operation.ADD, value);
	}
	
	public void removeWidget(String value)
	{
		for(Widget w : getChildren())
		{
			if(((RemovableMovableWidget)w).getValue().equals(value))
			{
				removeWidget(w, true, true);
				break;
			}
		}
	}
	
	public void removeWidget(Widget widget, boolean tellParent, boolean setDirections)
	{
		remove(widget);
		if(movable)
			setDirections();
		
		if(tellParent && observer != null && enabled)
			observer.listChanged(Operation.REMOVE, ((RemovableMovableWidget)widget).getValue());
	}
	
	private void setDirections()
	{
		switch(getWidgetCount())
		{
		case 0:
			break;
		case 1:
			((RemovableMovableWidget)getWidget(0)).setDirection(Direction.NONE);
			break;
		default:
			((RemovableMovableWidget)getWidget(0)).setDirection(Direction.DOWN);
			for(int i = 1; i < getWidgetCount() - 1; i++)
			{
				((RemovableMovableWidget)getWidget(i)).setDirection(Direction.BOTH);
			}
			((RemovableMovableWidget)getWidget(getWidgetCount() - 1)).setDirection(Direction.UP);
			break;
		}
	}
	
	private void moveWidget(RemovableMovableWidget rmw, Direction dir)
	{
		int index = getWidgetIndex(rmw);
		removeWidget(rmw, false, false);
		switch(dir)
		{
		case UP:
			insert(rmw, index - 1);
			break;
		case DOWN:
			insert(rmw, index + 1);
			break;
		}
		if(observer != null && enabled)
			observer.listChanged(Operation.MOVE, null);
		setDirections();
	}
	
	public int size()
	{
		return getChildren().size();
	}
	
	public List<String> getValues()
	{
		List<String> ret = new ArrayList<String>();
		
		for(Widget w : getChildren())
		{
			ret.add(((RemovableMovableWidget)w).getValue());
		}
		
		return ret;
	}

	public void setEnabled(boolean val)
	{
		enabled = val;
	}
}
