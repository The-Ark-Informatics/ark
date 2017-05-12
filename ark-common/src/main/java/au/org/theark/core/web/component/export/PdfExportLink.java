package au.org.theark.core.web.component.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfExportLink<T> extends Link<Void> {

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(PdfExportLink.class);
	private final DataTable<T>	table;
	private List<String>			headers				= new ArrayList<String>(0);
	private String filename = "export.pdf";

	/**
	 * 
	 * @param id
	 * @param table
	 * @param headers
	 * @param filename
	 */
	public PdfExportLink(String id, DataTable<T> table, List<String> headers, String filename) {
		super(id);
		this.table = table;
		this.headers = headers;
		this.filename = filename;
		
		add(new ContextImage("pdfimage", new Model<String>("images/icons/page_white_acrobat.png")));
		add(new Behavior(){
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to PDF");
			}
		});
	}

	@Override
	public void onClick() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		if(filename == null || filename.isEmpty()) {
			filename = "export.pdf";
		}
		final java.io.File file = new File(tempDir, filename);

		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);

			writeTableToPdf(outputStream);
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

	private void writeTableToPdf(OutputStream outputStream) {

		// step 1 open pdf document
		Document document = new Document(PageSize.A4.rotate());
		List<ExportableColumn<T>> exportable = getExportableColumns();
		
		// Create a pdf table
      PdfPTable pdfTable = new PdfPTable(table.getColumns().size());
      pdfTable.setWidthPercentage(100f);
      		
		// step 2
		try {
			PdfWriter.getInstance(document, outputStream).setInitialLeading(16);
			// step 3
			document.open();

			// Add header row 
			pdfTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

			// Column headers
			for (String col : headers) {
				pdfTable.addCell(col);
			}
			
			pdfTable.getDefaultCell().setBackgroundColor(null);
			pdfTable.setHeaderRows(1);

			Iterator<? extends T> it = table.getDataProvider().iterator(0, table.getDataProvider().size());
			while (it.hasNext()) {
				T object = it.next();
				for (ExportableColumn<T> column : exportable) {
					column.exportPdf(object, pdfTable);
				}
			}
			
			document.add(pdfTable);

			// step 5 close pdf / streams
			document.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (DocumentException e) {
			log.error(e.getMessage());
		}
		catch (IOException e) {
			log.error(e.getMessage());
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
