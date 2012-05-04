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

public class XlsExportLink<T> extends Link<Void> {

	private static final long	serialVersionUID	= 1L;
	private transient Logger	log					= LoggerFactory.getLogger(XlsExportLink.class);
	private final DataTable<T>	table;
	private List<String>			headers				= new ArrayList<String>(0);

	public XlsExportLink(String id, DataTable<T> table, List<String> headers) {
		super(id);
		this.table = table;
		this.headers = headers;
	}

	@Override
	public void onClick() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		final String fileName = "exportxls.xls";
		final java.io.File file = new File(tempDir, fileName);

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
			}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void writeTableToXls(OutputStream outputStream) {
		try {
			WritableWorkbook writableWorkBook = Workbook.createWorkbook(outputStream);
			jxl.write.WritableSheet wsheet = writableWorkBook.createSheet("Sheet", 0);

			int row = 0;
			int col = 0;

			// Column headers
			for (String cell : headers) {
				jxl.write.Label label = new jxl.write.Label(col++, row, cell);
				wsheet.addCell(label);
			}
			row++;

			Pager pager = new Pager(100, table.getDataProvider().size());
			for (int i = 0; i < pager.pages(); i++) {
				Iterator<? extends T> it = table.getDataProvider().iterator(pager.offset(i), pager.count(i));
				while (it.hasNext()) {
					ArrayList<T> rowOfValues = (ArrayList<T>) it.next();
					col = 0;
					for (T cell : rowOfValues) {
						String value = new String();
						if(cell != null) {
							value = cell.toString();
						}
						jxl.write.Label label = new jxl.write.Label(col++, row, value);
						wsheet.addCell(label);
					}
					row++;
				}
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
}
