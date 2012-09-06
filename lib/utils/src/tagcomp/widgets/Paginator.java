package tagcomp.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Paginator extends Composite implements ClickHandler
{
	private HasPaginator parent;
	private FlexTable paginationTable = new FlexTable();

	private int selected; // Pagina actualmente seleccionada.
	private int chapter; // Capitulo que se esta mostrando (podria o no incluir
								// la seleccion)
	private int pagesPerChapter; // Numero de paginas por capitulo.
	private int numPages; // Numero de paginas totales.

	public int getNumPages()
	{
		return numPages;
	}

	public Paginator(HasPaginator parent)
	{
		this.parent = parent;
		this.pagesPerChapter = 1;
		paginationTable.addClickHandler(this);
		numPages = 1;
		chapter = 0;
		setPage(0, true);
		initWidget(paginationTable);
		setStyleName("paginador");
	}

	public void setNumPages(int numPages, int pagesPerChapter)
	{
		this.numPages = numPages;
		this.pagesPerChapter = pagesPerChapter > numPages ? numPages : pagesPerChapter;
		paginationTable.removeAllRows();
		setPage(0, true);
	}

	public void setNumPages(int numRecords, int recordsPerPage, int pagesPerChapter)
	{
		setNumPages(numRecords % recordsPerPage == 0 ? (numRecords / recordsPerPage) : (numRecords / recordsPerPage) + 1, pagesPerChapter);
	}

	public void setNumPages(int numRecords, int recordsPerPage, int pagesPerChapter, int lastPageSelected)
	{
		this.numPages = numRecords % recordsPerPage == 0 ? (numRecords / recordsPerPage) : (numRecords / recordsPerPage) + 1;
		this.pagesPerChapter = pagesPerChapter > numPages ? numPages : pagesPerChapter;
		paginationTable.removeAllRows();
		setPage(lastPageSelected, true);
	}

	public void setPage(int page, boolean select)
	{
		pagesPerChapter = pagesPerChapter == 0 ? 1 : pagesPerChapter;
		// Coloco la pagina y la selecciono si tiene sentido.
		chapter = page - (page % pagesPerChapter);
		if (select)
		{
			// si cambio la pagina seleccionada se le avisa al padre
			if (selected != page && parent != null)
			{
				parent.pageChanged(page);
			}
			selected = page;
		}

		// Coloco el menu de acuerdo a la pagina actual.
		for (int i = 1; i <= pagesPerChapter; i++)
		{
			if (chapter + i > numPages)
			{
				setTextNStyle(0, i, "", "void");
			} else if (chapter + i - 1 == selected)
			{
				setTextNStyle(0, i, "" + (chapter + i), "selected");
			} else
			{
				setTextNStyle(0, i, "" + (chapter + i), "");
			}
		}

		// Reviso si las flechas laterales debieran estar habilitadas o
		// desabilitadas.
		if (chapter == 0)
		{
			setTextNStyle(0, 0, "", "arrows void");
		} else
		{
			setTextNStyle(0, 0, "<", "arrows");
		}

		if (chapter + pagesPerChapter >= numPages)
		{
			setTextNStyle(0, pagesPerChapter + 1, "", "arrows void");
		} else
		{
			setTextNStyle(0, pagesPerChapter + 1, ">", "arrows");
		}
	}

	private void setTextNStyle(int i, int j, String text, String style)
	{
		paginationTable.setText(i, j, text);
		paginationTable.getCellFormatter().setStyleName(i, j, style);
	}

	public void onClick(ClickEvent event)
	{
		Cell clickedCell = paginationTable.getCellForEvent(event);
		if (clickedCell != null)
		{
			int cellIndex = clickedCell.getCellIndex();

			// Decido si tengo que retroceder un capitulo, avanzar al siguiente o
			// seleccionar una pagina del capitulo.
			if (cellIndex == 0)
			{
				if (chapter != 0)
				{
					setPage(chapter - pagesPerChapter, false);
				}
			} else if (cellIndex == pagesPerChapter + 1)
			{
				if (chapter + pagesPerChapter <= numPages)
				{
					setPage(chapter + pagesPerChapter, false);
				}
			} else
			{
				setPage(chapter + cellIndex - 1, true);
			}
		}
	}

	public void clearCaption()
	{
		paginationTable.setText(1, 0, "");
		paginationTable.getCellFormatter().removeStyleName(1, 0, "caption");
	}

	public void setCaption(String text)
	{
		setTextNStyle(1, 0, text, "caption");
		paginationTable.getFlexCellFormatter().setColSpan(1, 0, pagesPerChapter + 2);
	}

	public int getSelectedPage()
	{
		return selected;
	}
}