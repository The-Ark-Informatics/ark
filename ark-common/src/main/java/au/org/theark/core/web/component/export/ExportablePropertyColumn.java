package au.org.theark.core.web.component.export;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.util.CsvWriter;

public class ExportablePropertyColumn<T> extends PropertyColumn<T> implements ExportableColumn<T> {
	private static final long	serialVersionUID	= 1L;

	public ExportablePropertyColumn(IModel<String> displayModel, String propertyExpression) {
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
}
