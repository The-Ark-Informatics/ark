package au.org.theark.core.web.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.Constants;
import au.org.theark.core.util.ArkSheetMetaData;

import com.csvreader.CsvReader;
import com.visural.common.datastruct.datagrid.DataException;
import com.visural.common.datastruct.datagrid.DataGrid;
import com.visural.common.datastruct.datagrid.io.CSVGridGenerator;

public class ArkExcelWorkSheetAsGrid extends Panel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2950851261474110946L;
	private transient Sheet		sheet;												// an instance of an Excel WorkSheet
	private ArkSheetMetaData	meta;
	private char					delimiterType;

	public ArkExcelWorkSheetAsGrid(String id)
	{
		super(id);
		meta = new ArkSheetMetaData();
		add(createMainGrid());
		add(createHeadings());
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, char delimChar)
	{
		super(id);
		meta = new ArkSheetMetaData();
		initWorkbook(inputStream, delimChar);
		add(createMainGrid());
		add(createHeadings());
	}

	@SuppressWarnings("unused")
	public void initDataTable(InputStream inputStream)
	{
		InputStream csvInputStream = convertXlsToCsv();
		CSVGridGenerator csvGridGenerator;
		try
		{
			csvGridGenerator = new CSVGridGenerator("csvGrid", csvInputStream);
			DataGrid dataGrid = new DataGrid(csvGridGenerator, false);
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}
	}

	public InputStream convertXlsToCsv()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = sheet;

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++)
			{
				row = s.getRow(i);

				if (row.length > 0)
				{
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++)
					{
						osw.write(",");
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.err.println(e.toString());
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/*
	 * generating rows using the Loop class and the PropertyModel with SheetMetaData instance works magicWe bound the numbers of rows stored in
	 * SheetMetaData instance to the Loop using PropertyModel. No table will be displayed before an upload.
	 */
	@SuppressWarnings( { "serial", "unchecked" })
	private Loop createMainGrid()
	{
		// We create a Loop instance and uses PropertyModel to bind the Loop iteration to ExcelMetaData "rows" value
		return new Loop("rows", new PropertyModel(meta, "rows"))
		{
			public void populateItem(LoopItem item)
			{

				final int row = item.getIteration();

				if(row > 0)
				{
					// creates the row numbers
					item.add(new Label("rowNo", new Model(String.valueOf(row))));
	
					// We create an inner Loop instance and uses PropertyModel to bind the Loop iteration to ExcelMetaData "cols" value
					item.add(new Loop("cols", new PropertyModel(meta, "cols"))
					{
						public void populateItem(LoopItem item)
						{
	
							final int col = item.getIteration();
							/*
							 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance
							 * which gets updated each time we upload a new Excel File, the value for each cell is automatically updated
							 */
							IModel<Object> model = new Model()
							{
								/**
								 * 
								 */
								private static final long	serialVersionUID	= 1144128566137457199L;
	
								@Override
								public Serializable getObject()
								{
									Cell cell = sheet.getCell(col, row);
									return cell.getContents();
								}
							};
							Label cellData = new Label("cellData", model);	
							item.add(cellData);	
						}
					});
				}
				else
				{
					item.add(new Label("rowNo", new Model("")));
					item.setVisible(false);
					item.add(new Loop("cols", new PropertyModel(meta, "cols"))
					{
						public void populateItem(LoopItem item)
						{
							Label cellData = new Label("cellData", new Model(""));
							item.add(cellData);
						}
					});
					item.setVisible(false);
				}
			}
		};
	}

	@SuppressWarnings( { "unchecked" })
	private Loop createHeadings()
	{

		return new Loop("heading", new PropertyModel(meta, "cols"))
		{

			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7027878243061138904L;
			
			public void populateItem(LoopItem item)
			{

				final int col = item.getIteration();

				/*
				 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance
				 * which gets updated each time we upload a new Excel File, the value for each cell is automatically updated
				 */
				IModel<Object> model = new Model()
				{
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1144128566137457199L;

					@Override
					public Serializable getObject()
					{
						Cell cell = sheet.getCell(col, 0);
						return cell.getContents();
					}
				};
				Label cellData = new Label("cellHead", model);
				item.add(cellData);
			}
		};
	}

	public void initWorkbook(InputStream inputStream, char delimChar)
	{
		delimiterType = delimChar;
		try
		{
			// By default try to get the XLS workbook
			// Streams directly from inputStream into Workbook.getWorkbook(Inputstream)
			Workbook wkb = Workbook.getWorkbook(inputStream);
			sheet = wkb.getSheet(0); // get First Work Sheet
		}
		catch (BiffException bex)
		{
			// Error when reading XLS file type, so must be CSV or TXT
			// Thus attempt a convert from csv or text to xls format
			try
			{
				inputStream.reset();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				CsvReader csvReader = new CsvReader(inputStreamReader, delimiterType);
				WritableWorkbook wwkb = Workbook.createWorkbook(output);
				jxl.write.WritableSheet wsheet = wwkb.createSheet("Sheet", 0);
				int row = 0;
				// Loop through all rows in file
				while (csvReader.readRecord())
				{
					String[] stringArray = csvReader.getValues();
					for (int col = 0; col < stringArray.length; col++)
					{
						jxl.write.Label label = new jxl.write.Label(col, row, stringArray[col]);
						wsheet.addCell(label);
					}
					row++;
				}

				// All sheets and cells added. Now write out the workbook
				wwkb.write();

				sheet = wwkb.getSheet(0); // get First Work Sheet to display in webpage

				wwkb.close();
				output.flush();
				inputStream.close();
				output.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (RowsExceededException e)
			{
				e.printStackTrace();
			}
			catch (WriteException e)
			{
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Sets Sheet meta data. The HTML table creation needs this object to know about the rows and columns
		 */
		meta.setRows(Constants.ROWS_PER_PAGE+1);
		meta.setCols(sheet.getColumns());
	}
}
