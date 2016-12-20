package au.org.theark.genomics.web.component.datacenter.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class QueryForm extends AbstractDetailForm<DataCenterVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private TextField<String> individualIdTxtFld;
	
	private AjaxButton queryButton;
	
	private AjaxButton downloadBtn;
	
	private DropDownChoice<String>	outputDDL;
	
	private AbstractDetailModalWindow modalWindow;

	private CompoundPropertyModel<DataCenterVo> cpmModel;
	
	private String selectedOutput;
	
	public QueryForm(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<DataCenterVo> cpModel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id, feedBackPanel, cpModel, arkCrudContainerVO);
		setOutputMarkupId(true);
		this.modalWindow = modalWindow;
		this.cpmModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.selectedOutput="PED/MAP";
	}
	
	public void initialiseQueryForm() {
			this.individualIdTxtFld = new TextField<String>("individualId");
			this.queryButton = new AjaxButton("queryButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					//super.onSubmit(target, form);
					iGenomicService.executeQueryAnalysis(cpmModel.getObject());
					downloadBtn.setEnabled(true);
					target.add(downloadBtn);
				}
			};
			
			this.downloadBtn =new AjaxButton("downloadButton") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					DataCenterVo dataCenter= cpmModel.getObject();
					byte[] data = iGenomicService.getQueryResult(dataCenter);
					String output= ""+dataCenter.getIndividualId()+"_SNPs.ped";
					getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, output));
					
				}
			};
			downloadBtn.setEnabled(false);
			
			List<String> outputTypeList = new ArrayList<String>();
			outputTypeList.add(selectedOutput);
//			outputTypeList.add("BIN");
			
			this.outputDDL = new DropDownChoice<String>("outputType",new PropertyModel<String>(this, "selectedOutput"),outputTypeList);
			
			
			arkCrudContainerVO.getEditButtonContainer().get("save").setVisible(false);
			arkCrudContainerVO.getEditButtonContainer().get("delete").setVisible(false);
			
			addDetailFormComponents();
	}
	

	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		modalWindow.close(target);
		
	}

	@Override
	protected void onSave(Form<DataCenterVo> containerForm, AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isNew() {
		return false;
	}
	
	

	public String getSelectedOutput() {
		return selectedOutput;
	}

	public void setSelectedOutput(String selectedOutput) {
		this.selectedOutput = selectedOutput;
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(individualIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(queryButton);		
		arkCrudContainerVO.getDetailPanelFormContainer().add(downloadBtn);		
		arkCrudContainerVO.getDetailPanelFormContainer().add(outputDDL);		
	}

}
