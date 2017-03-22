/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.registry.web.component.invoice.form;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.registry.web.component.invoice.ProcessResultListPanel;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<Pipeline> {

	private static final long					serialVersionUID	= 1423759632793367263L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;
/*
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_GENO_SERVICE)
	@Autowired
	protected IGenoService					iGenoService;
*/
	//@SpringBean(name = Constants.STUDY_SERVICE)
	//private IStudyService						iStudyService;

	private TextField<String>					name;
	private TextField<String>					description;

	private ProcessResultListPanel processResults;

	private ArkDataProvider<au.org.theark.core.model.geno.entity.Process, IArkCommonService> processProvider;

	private DataView<au.org.theark.core.model.geno.entity.Process>									dataView;
	protected WebMarkupContainer resultsWmc = new WebMarkupContainer("resultsWmc");

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void initialiseDetailForm() {
		name = new TextField<String>("name");
		name.add(new ArkDefaultFormFocusBehavior());
		description = new TextField<String>("description");
		
		processResults = new ProcessResultListPanel("processResults", feedBackPanel, (ContainerForm) containerForm, arkCrudContainerVO);
		
		// Data providor to paginate resultList
		processProvider = new ArkDataProvider<au.org.theark.core.model.geno.entity.Process, IArkCommonService>(iArkCommonService) {

			private static final long	serialVersionUID	= 1L;

			public long size() {
				Study study = iArkCommonService.getStudy((Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID));
				Pipeline p = containerForm.getModelObject();
				au.org.theark.core.model.geno.entity.Process proc = model.getObject();
				proc.setPipeline(p);
				return service.getProcessCount(model.getObject());
			}

			public Iterator<au.org.theark.core.model.geno.entity.Process> iterator(long first, long count) {
				List<au.org.theark.core.model.geno.entity.Process> list = new ArrayList<au.org.theark.core.model.geno.entity.Process>();
				list = iArkCommonService.searchPageableProcesses(model.getObject(), first, count);
				return list.iterator();
			}
		};
		
		processProvider.setModel(new Model<au.org.theark.core.model.geno.entity.Process>(new au.org.theark.core.model.geno.entity.Process()));

		
		dataView = processResults.buildDataView(processProvider);
		dataView.setItemsPerPage(iArkCommonService.getRowsPerPage());

		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		
		processResults.add(pageNavigator);
		processResults.buildDataView(processProvider);
		
		resultsWmc.add(pageNavigator);
		resultsWmc.add(dataView);
		processResults.add(resultsWmc);
		
		attachValidators();
		addDetailFormComponents();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(name);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(description);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(processResults);
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		//AddressVO addressVO = new AddressVO();
		//containerForm.setModelObject(addressVO);
		containerForm.setModelObject(new Pipeline());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
//		try {
//			//iStudyService.delete(containerForm.getModelObject().getAddress());
//			//iGenoService.de
//			this.info("The Pipeline has been deleted successfully.");
//			editCancelProcess(target);
//		}
//		catch (ArkSystemException e) {
//			this.error("An error occured while processing your delete. Please contact Support");
//			// TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to
//			// admin
//			e.printStackTrace();
//		}
		iArkCommonService.deletePipeline(containerForm.getModelObject());
		this.info("Pipeline deleted");
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override	
	protected void onSave(Form<Pipeline> containerForm, AjaxRequestTarget target) {
	//	Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//StringBuffer feedBackMessageStr = new StringBuffer();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(studyId);
		containerForm.getModelObject().setStudy(study);
		if(containerForm.getModelObject()!=null && containerForm.getModelObject().getId()==null){
			
			iArkCommonService.createPipeline(containerForm.getModelObject());
			this.info("Pipeline saved");
		}
		else {
			iArkCommonService.updatePipeline(containerForm.getModelObject());
			this.info("Pipeline updated");
		}
		
		target.add(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}

	@Override
	protected void attachValidators() {
		name.setRequired(true).setLabel((new StringResourceModel("name.required", this, new Model<String>("Name"))));
	}
}
