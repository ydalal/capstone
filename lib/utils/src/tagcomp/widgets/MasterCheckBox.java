package tagcomp.widgets;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;

public class MasterCheckBox extends CheckBox implements ClickHandler
{
	private List<CheckBox> children = new LinkedList<CheckBox>();

	public MasterCheckBox()
	{
		super();
		addClickHandler(this);
		setValue(false);
	}

	public MasterCheckBox(List<CheckBox> children)
	{
		this();
		addChildren(children);
	}

	public void addChild(CheckBox child)
	{
		if(child != null)
		{
			children.add(child);
			child.addClickHandler(this);
		}
	}

	public void addChildren(List<CheckBox> children)
	{
		for(CheckBox child : children)
		{
			addChild(child);
		}
	}

	public void clear()
	{
		children.clear();
	}

	public void remove(CheckBox child)
	{
		if(child != null)
		{
			children.remove(child);
		}
	}

	public void onClick(ClickEvent event)
	{
		if(event.getSource() instanceof MasterCheckBox)
		{
			setValue(getValue());
		}
		else if(event.getSource() instanceof CheckBox && !((CheckBox)(event.getSource())).getValue())
		{
			super.setValue(false);
		}
	}
	
	public void setValue(Boolean val)
	{
		super.setValue(val);
		for(CheckBox child : children)
		{
			child.setValue(val);
		}
	}
	
	public List<CheckBox> getChildren()
	{
		return children;
	}
}
