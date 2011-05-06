package au.org.theark.core.web.component;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.util.ArkSheetMetaData;

public class ArkDownloadTemplateButton extends AjaxButton{

	private String templateFilename = null;
	private String[] templateHeader = null;
	private transient ArkSheetMetaData		sheetMetaData;
	
	public ArkDownloadTemplateButton(String id, String templateFilename, String[] templateHeader) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.templateFilename = templateFilename;
		this.setTemplateHeader(templateHeader);
		this.sheetMetaData.setRows(1);
		this.sheetMetaData.setCols(templateHeader.length);
		
		// Do not submit parent form
		this.setDefaultFormProcessing(false);
		
		// Only show button if filename or templateHeader != null
		setVisible(templateFilename != null && templateHeader.length > 0);
	}

	public byte[] writeOutXlsFileToBytes()
	{
		byte[] bytes = null;
		try
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook w = Workbook.createWorkbook(output);
			WritableSheet writableSheet = w.createSheet("Sheet", 0);

			for (int row = 0; row < sheetMetaData.getRows(); row++)
			{
				for (int col = 0; col < sheetMetaData.getCols(); col++)
				{
					String cellData = getTemplateHeader()[col];
					jxl.write.Label label = new jxl.write.Label(col, row, cellData);
					writableSheet.addCell(label);
				}
			}

			w.write();
			w.close();
			bytes = output.toByteArray();
			output.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bytes;
	}

	public void setTemplateHeader(String[] templateHeader) {
		this.templateHeader = templateHeader;
	}

	public String[] getTemplateHeader() {
		return templateHeader;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		byte[] data = writeOutXlsFileToBytes();
		if(data != null)
		{
			getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("application/vnd.ms-excel", data, templateFilename + ".xls"));
		}
	}
}