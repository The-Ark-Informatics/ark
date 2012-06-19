package au.org.theark.core.web.component.export;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * A toolbar for exporting DataTable results to CSV, XLS, or PDF 
 * @author cellis
 *
 * @param <T>
 */
public class ExportToolbar<T> extends AbstractToolbar {

	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 * @param id
	 * @param table
	 * @param headers
	 * @param filename
	 */
	public ExportToolbar(final DataTable<T> table, List<String> headers, String filename) {
		super(table);
		WebMarkupContainer span = new WebMarkupContainer("span") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("colspan", table.getColumns().size());
			}
		};
		add(span);
		span.add(new CsvExportLink<T>("csv", table, headers, filename + ".csv"));
		span.add(new XlsExportLink<T>("xls", table, headers, filename + ".xls"));
		span.add(new PdfExportLink<T>("pdf", table, headers, filename + ".pdf"));
	}
}
