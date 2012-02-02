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
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public ExportToolbar(final DataTable<T> table, List<String> headers) {
		super(table);
		WebMarkupContainer span = new WebMarkupContainer("span") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("colspan", table.getColumns().size());
			}
		};
		add(span);
		span.add(new CsvExportLink<T>("csv", table, headers));
		span.add(new XlsExportLink<T>("xls", table, headers));
		span.add(new PdfExportLink<T>("pdf", table, headers));
	}
}
