package au.org.theark.core.web.component.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.util.Pager;

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

	public PdfExportLink(String id, DataTable<T> table, List<String> headers) {
		super(id);
		this.table = table;
		this.headers = headers;
	}

	@Override
	public void onClick() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		final String fileName = "exportpdf.pdf";
		final java.io.File file = new File(tempDir, fileName);

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
			}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void writeTableToPdf(OutputStream outputStream) {

		// step 1 open pdf document
		Document document = new Document(PageSize.A4.rotate());
		
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

			Pager pager = new Pager(100, table.getDataProvider().size());
			for (int i = 0; i < pager.pages(); i++) {
				Iterator<? extends T> it = table.getDataProvider().iterator(pager.offset(i), pager.count(i));
				while (it.hasNext()) {
					ArrayList<T> row = (ArrayList<T>) it.next();
					for (T cell : row) {
						String value = new String();
						if(cell != null) {
							value = cell.toString();
						}
						pdfTable.addCell(value);
					}
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
}
