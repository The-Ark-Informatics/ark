package au.org.theark.core.web.component.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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

public class XlsExportLink<T> extends Link<Void> {

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(XlsExportLink.class);
	private final DataTable<T>	table;
	private List<String>			headers				= new ArrayList<String>(0);
	private String filename = "export.xls";

	/**
	 * 
	 * @param id
	 * @param table
	 * @param headers
	 * @param filename
	 */
	public XlsExportLink(String id, DataTable<T> table, List<String> headers, String filename) {
		super(id);
		this.table = table;
		this.headers = headers;
		this.filename = filename;
		
		add(new ContextImage("xlsimage", new Model<String>("images/icons/page_white_excel.png")));
		add(new Behavior(){
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to XLS");
			}
		});
	}

	@Override
	public void onClick() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		if(filename == null || filename.isEmpty()) {
			filename = "export.xls";
		}
		final java.io.File file = new File(tempDir, filename);

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);

			writeTableToXls(outputStream);
			// throw new AbortException();

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

	private void writeTableToXls(OutputStream outputStream) {
		try {
			WritableWorkbook writableWorkBook = Workbook.createWorkbook(outputStream);
			jxl.write.WritableSheet wsheet = writableWorkBook.createSheet("Sheet", 0);
			List<ExportableColumn<T>> exportable = getExportableColumns();

			int row = 0;
			int col = 0;

			// Column headers
			for (String cell : headers) {
				jxl.write.Label label = new jxl.write.Label(col++, row, cell);
				wsheet.addCell(label);
			}
			row++;

			Iterator<? extends T> it = table.getDataProvider().iterator(0, table.getDataProvider().size());
			while (it.hasNext()) {
				col = 0;
				T object = it.next();
				for (ExportableColumn<T> column : exportable) {
					column.exportXls(object, wsheet, col++, row);
				}
				row++;
			}

			// All sheets and cells added. Now write out the workbook
			writableWorkBook.write();
			writableWorkBook.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (RowsExceededException e) {
			e.printStackTrace();
		}
		catch (WriteException e) {
			e.printStackTrace();
		}
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
