package tagcomp.widgets;

public interface HasRemovableMovableWidgetList
{
	public enum Operation { ADD, REMOVE, MOVE };
	public void listChanged(Operation operation, String value);
}
