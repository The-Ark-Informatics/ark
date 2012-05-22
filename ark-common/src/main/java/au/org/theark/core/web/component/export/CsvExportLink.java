package au.org.theark.core.web.component.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.util.CsvWriter;

public class CsvExportLink<T> extends Link<Void> {

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(CsvExportLink.class);
	private final DataTable<T>	table;
	private List<String>			headers				= new ArrayList<String>(0);
	private String					filename				= "export.csv";

	/**
	 * 
	 * @param id
	 * @param table
	 * @param headers
	 * @param filename
	 */
	public CsvExportLink(String id, DataTable<T> table, List<String> headers, String filename) {
		super(id);
		this.table = table;
		this.headers = headers;
		this.filename = filename;
		
		add(new ContextImage("csvimage", new Model<String>("images/icons/page_white_text.png")));
		add(new Behavior(){
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to CSV");
			}
		});
	}

	@Override
	public void onClick() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		final java.io.File file = new File(tempDir, filename);
		if(filename == null || filename.isEmpty()) {
			filename = "exportcsv.csv";
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);

			writeTableToCsv(outputStream);

			IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
			getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
				@Override
				public void respond(IRequestCycle requestCycle) {
					super.respond(requestCycle);
					Files.remove(file);
				}
			}.setFileName(filename).setContentDisposition(ContentDisposition.ATTACHMENT));
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	private void writeTableToCsv(OutputStream outputStream) {
		CsvWriter writer = new CsvWriter(outputStream);
		List<ExportableColumn<T>> exportable = getExportableColumns();

		// Column headers
		for (String col : headers) {
			writer.write(col);
		}
		writer.endLine();

		Iterator<? extends T> it = table.getDataProvider().iterator(0, table.getDataProvider().size()); //(pager.offset(i), pager.count(i));
		while (it.hasNext()) {
			T object = it.next();
			for (ExportableColumn<T> col : exportable) {
				col.exportCsv(object, writer);
			}
			writer.endLine();
		}
		writer.close();

	}

	@SuppressWarnings("unchecked")
	private List<ExportableColumn<T>> getExportableColumns() {
		List<ExportableColumn<T>> exportable = new ArrayList<ExportableColumn<T>>(table.getColumns().size());
		for (IColumn<T> column : table.getColumns()) {
			if (column instanceof ExportableColumn<?>) {
				exportable.add((ExportableColumn<T>) column);
			}
		}
		return exportable;
	}
}
