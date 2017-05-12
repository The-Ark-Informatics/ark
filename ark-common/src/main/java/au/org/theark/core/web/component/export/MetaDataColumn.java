package au.org.theark.core.web.component.export;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.util.CsvWriter;

public class MetaDataColumn<T> extends AbstractColumn<List<String>> {

	/**
	*
	*/
	private static final long	serialVersionUID	= 1L;
	int								columnNumber;

	public MetaDataColumn(final ResultSetDataProvider prov, final int colNumber) {
		super(new AbstractReadOnlyModel<String>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public String getObject() {
				return prov.getColNames().get(colNumber);

			}
		});
		columnNumber = colNumber;
	}

	public void populateItem(Item<ICellPopulator<List<String>>> cellItem, String componentId, IModel<List<String>> rowModel) {
		cellItem.add(new Label(componentId, rowModel.getObject().get(columnNumber)));
	}
	
	public void exportCsv(final T object, CsvWriter writer) {
		IModel<?> model = new AbstractReadOnlyModel<T>(){


			private static final long	serialVersionUID	= 1L;

			@Override
			public T getObject() {
				return object;
			}
			
		};
		writer.write(model.getObject());
	}
	
	@Override
	public IModel<String> getDisplayModel() {
		return super.getDisplayModel();
	}
}
