package au.org.theark.core.web.component.export;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSetDataProvider extends SortableDataProvider<List<? extends String>> {

	private static final long							serialVersionUID	= 1L;
	private transient Logger							log					= LoggerFactory.getLogger(ResultSetDataProvider.class);
	private String											dbName;
	private String											query;
	private IModel<List<List<? extends String>>>	dataModel;
	private int												size					= 0;
	private List<String>									colNames;

	public ResultSetDataProvider(final String dbName, final String query) {
		this.dbName = dbName;
		this.query = query;
		
		dataModel = new LoadableDetachableModel<List<List<? extends String>>>() {

			/**
			 *
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected List<List<? extends String>> load() {
				ArrayList<List<? extends String>> resultList = new ArrayList<List<? extends String>>();

				Connection conn = null;
				ResultSet rs = null;
				try {
					conn = DataSourceFactory.createDataSource(dbName).getConnection();
					log.info("query: " + query);
					PreparedStatement ps = conn.prepareStatement(query);
					rs = ps.executeQuery();

					ResultSetMetaData meta = rs.getMetaData();
					int totalColumns = meta.getColumnCount();

					colNames = new ArrayList<String>();
					for (int i = 1; i <= totalColumns; i++) {
						colNames.add(meta.getColumnName(i));
					}

					int rowsRead = 0;

					while (rs.next()) {
						List<String> row = new ArrayList<String>();

						rowsRead++;
						for (int i = 1; i <= totalColumns; i++) {
							//System.out.print(rs.getString(i));
							row.add(rs.getString(i));
							//System.out.print(" - ");
						}
						resultList.add(row);
						//System.out.print("\n");
					}

					log.info("Size:" + rowsRead);
					size = rowsRead;

				}
				catch (SQLException e) {
					size = 0;
					log.error(e.getMessage());
				}
				finally {
					if (conn != null) {
						try {
							conn.close();
						}
						catch (SQLException e) {
							log.error(e.getMessage());
						}
					}
				}

				return resultList;
			}
		};
	}
	
	public List<String> getColNames() {
		if (colNames == null) {
			dataModel.getObject();
		}
		return colNames;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName
	 *           the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @param q
	 *           the query to set
	 */
	public void setQuery(String q) {
		query = q;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	public Iterator<List<? extends String>> iterator(int first, int count) {

		int boundsSafeCount = count;

		if (first + count > size) {
			boundsSafeCount = first - size;
		}
		else {
			boundsSafeCount = count;
		}

		log.info("size:" + size + " - boundSafe:" + boundsSafeCount);

		return dataModel.getObject().subList(first, first + boundsSafeCount).iterator();
	}

	public int size() {
		return size;
	}

	public IModel<List<? extends String>> model(List<? extends String> object) {
		return Model.<String> ofList(object);
	}

	@Override
	public void detach() {
		dataModel.detach();
		super.detach();
	}

	public int getColumnCount() {
		return getColNames().size();
	}

}
