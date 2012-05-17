package au.org.theark.core.web.component.export;

import au.org.theark.core.util.CsvWriter;

public interface ExportableColumn<T> {
		void exportCsv(T object, CsvWriter writer);
}
