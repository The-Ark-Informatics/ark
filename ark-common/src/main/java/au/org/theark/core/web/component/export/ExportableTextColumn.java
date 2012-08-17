package au.org.theark.core.web.component.export;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.itextpdf.text.pdf.PdfPTable;

import au.org.theark.core.util.CsvWriter;

public class ExportableTextColumn<T> extends PropertyColumn<T> implements ExportableColumn<T> {
	private static final long	serialVersionUID	= 1L;

	public ExportableTextColumn(IModel<String> displayModel, String propertyExpression) {
		super(displayModel, propertyExpression);
	}

	public void exportCsv(final T object, CsvWriter writer) {
		IModel<?> textModel = createLabelModel(new AbstractReadOnlyModel<T>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public T getObject() {
				return object;
			}
		});
		writer.write(textModel.getObject());
		textModel.detach();
	}

	public void exportXls(final T object, WritableSheet writer, int col, int row) {
		IModel<?> textModel = createLabelModel(new AbstractReadOnlyModel<T>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public T getObject() {
				return object;
			}
		});
		
		try {
			if(textModel.getObject() != null) {
				jxl.write.Label label = new jxl.write.Label(col, row, textModel.getObject().toString());
				writer.addCell(label);
			}
			
		}
		catch (RowsExceededException e) {
		}
		catch (WriteException e) {
		}
		textModel.detach();
	}

	public void exportPdf(final T object, PdfPTable writer) {
		IModel<?> textModel = createLabelModel(new AbstractReadOnlyModel<T>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public T getObject() {
				return object;
			}
		});
		if(textModel.getObject() != null) {
			writer.addCell(textModel.getObject().toString());
		}
		else{
			writer.addCell("");
		}
		textModel.detach();
	}
}
