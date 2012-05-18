package au.org.theark.core.web.component.export;

import jxl.write.WritableSheet;
import au.org.theark.core.util.CsvWriter;

import com.itextpdf.text.pdf.PdfPTable;

public interface ExportableColumn<T> {
		void exportCsv(T object, CsvWriter writer);
		void exportXls(T object, WritableSheet writer, int col, int row);
		void exportPdf(T object, PdfPTable writer);
}
