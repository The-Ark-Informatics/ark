package au.org.theark.core.web.component.export;

import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.util.CsvWriter;

import com.itextpdf.text.pdf.PdfPTable;

public class ExportableDateColumn<T> extends PropertyColumn<T> implements ExportableColumn<T> {
	private static final long	serialVersionUID	= 1L;
	private final String			format;

	public ExportableDateColumn(IModel<String> displayModel, String propertyExpression, String format) {
		super(displayModel, propertyExpression);
		this.format = format;
	}

	public ExportableDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, String format) {
		super(displayModel, sortProperty, propertyExpression);
		this.format = format;
	}
	
	@Override
   protected IModel<T> createLabelModel(IModel<T> iModel)
   {
       return new DateModel(super.createLabelModel(iModel));
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
			if (textModel.getObject() != null) {
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
		if (textModel.getObject() != null) {
			writer.addCell(textModel.getObject().toString());
		}
		else {
			writer.addCell("");
		}
		textModel.detach();
	}
	
	private class DateModel<T> implements IModel<String>
   {
       private final IModel<Date> inner;
       private static final long serialVersionUID = 190887916985140272L;

       private DateModel(IModel<Date> inner)
       {
           this.inner = inner;
       }

       public void detach()
       {
           inner.detach();
       }

       public String getObject()
       {
           Date date = (Date) inner.getObject();
           if(date == null)
           {
               return "";
           }
           SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
           return dateFormatter.format(date);
       }

       public void setObject(String s)
       {
           SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
           try
           {
               Date date = dateFormatter.parse(s);
               inner.setObject(date);
           }
           catch (java.text.ParseException e) {
				throw new WicketRuntimeException("Unable to parse date.", e );
           }
       }
   } 
}
