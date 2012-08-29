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
package au.org.theark.lims.web.component.barcodelabel.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.lims.service.ILimsAdminService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.barcodelabeldata.BarcodeLabelDataPanel;
import au.org.theark.lims.web.component.button.PrinterListPanel;

/**
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<BarcodeLabel> {


	private static final long					serialVersionUID	= 6808980290575067265L;

	protected static final Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService					iLimsAdminService;

	private TextField<Long>						idTxtFld;
	private DropDownChoice<Study>				studyDdc;
	private DropDownChoice<BarcodePrinter>	barcodePrinterDdc;
	private TextField<String>					nameTxtFld;
	private TextArea<String>					descriptionTxtArea;
	private TextField<Number>					versionTxtFld;
	
	private Panel 									barcodeLabelDataPanel;
	private Label									barcodeLabelTemplateLbl;
	private DropDownChoice<BarcodeLabel>	barcodeLabelTemplateDdc;
	private TextArea<String> 					exampleBarcodeDataFile;
	StringValue barcodePrinterName;
	
	private PrinterListPanel printerList;

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
		idTxtFld = new TextField<Long>("id");
		nameTxtFld = new TextField<String>("name");
		nameTxtFld.setEnabled(false);
		descriptionTxtArea = new TextArea<String>("description");
		versionTxtFld = new TextField<Number>("version");
		
		barcodeLabelTemplateLbl = new Label("barcodeLabelTemplateLbl", "Clone from Barcode Label Template:"){

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.setVisible(isNew());
			}
		};

		initStudyDdc();
		initBarcodePrinterDdc();
		initBarcodeLabelTemplateDdc();
		
		barcodeLabelDataPanel = new EmptyPanel("barcodeLabelDataPanel");
		exampleBarcodeDataFile = new TextArea<String>("exampleBarcodeDataFile", new Model<String>(""));
		exampleBarcodeDataFile.setEnabled(false);
		exampleBarcodeDataFile.setVisible(isNew());

		addDetailFormComponents();
		attachValidators();
	}
	
	@Override
	public void onBeforeRender() {
		if(!isNew()) {
			BarcodeLabelDataPanel barcodeLabelDataPanel = new BarcodeLabelDataPanel("barcodeLabelDataPanel", containerForm.getModelObject(), feedBackPanel);
			barcodeLabelDataPanel.initialisePanel();
			arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(barcodeLabelDataPanel);
			exampleBarcodeDataFile.setModelObject(iLimsAdminService.getBarcodeLabelTemplate(containerForm.getModelObject()));
		}
		
		String selected = new String();
		if(containerForm.getModelObject().getBarcodePrinterName() != null) {
			selected = containerForm.getModelObject().getBarcodePrinterName();
		}
		printerList = new PrinterListPanel("printerList", selected, isNew());
		printerList.add(new AbstractDefaultAjaxBehavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.renderOnLoadJavaScript("findPrinters()");
				String js = "function callWicket(selectedPrinter) { var wcall = wicketAjaxGet ('"
				    + getCallbackUrl() + "&selectedPrinter='+selectedPrinter, function() { }, function() { } ) }";
				response.renderJavaScript(js, "selectPrinter");
			}

			@Override
			protected void respond(AjaxRequestTarget arg0) {
				barcodePrinterName = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("selectedPrinter");
			}
		});
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(printerList);
		super.onBeforeRender();
	}

	private void initStudyDdc() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		studyListForUser = getStudyListForUser();
		ChoiceRenderer<Study> choiceRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyListForUser, choiceRenderer) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				studyDdc.setEnabled(isNew());
				studyDdc.setChoices(getStudyListForUser());
			}
		};

		studyDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(barcodeLabelTemplateDdc);
			}
		});
	}

	private void initBarcodePrinterDdc() {
		ChoiceRenderer<BarcodePrinter> choiceRenderer = new ChoiceRenderer<BarcodePrinter>("uniqueName", Constants.ID);
		barcodePrinterDdc = new DropDownChoice<BarcodePrinter>("barcodePrinter") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				List<BarcodePrinter> choices = iLimsAdminService.getBarcodePrintersByStudyList(getStudyListForUser());
				this.setChoices(choices);
			}
		};
		barcodePrinterDdc.setChoiceRenderer(choiceRenderer);
		barcodePrinterDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				containerForm.getModelObject().setStudy(barcodePrinterDdc.getModelObject().getStudy());
			}
		});
	}
	
	private void initBarcodeLabelTemplateDdc() {
		ChoiceRenderer<BarcodeLabel> choiceRenderer = new ChoiceRenderer<BarcodeLabel>("nameAndVersion", Constants.ID);
		barcodeLabelTemplateDdc = new DropDownChoice<BarcodeLabel>("barcodeLabelTemplate") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				setVisible(isNew());
				List<BarcodeLabel> choices = iLimsAdminService.getBarcodeLabelTemplates();
				this.setChoices(choices);
			}
		};
		barcodeLabelTemplateDdc.setOutputMarkupPlaceholderTag(true);
		barcodeLabelTemplateDdc.setChoiceRenderer(choiceRenderer);
		
		barcodeLabelTemplateDdc.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Clone data from other BarcodeLabel
				if (barcodeLabelTemplateDdc.getModelObject() != null) {
					String labelPrefix = barcodeLabelTemplateDdc.getModelObject().getLabelPrefix();
					String labelSuffix = barcodeLabelTemplateDdc.getModelObject().getLabelSuffix();
					Long version = barcodeLabelTemplateDdc.getModelObject().getVersion();
					
					// Set default/required values
					containerForm.getModelObject().setLabelPrefix(labelPrefix);
					containerForm.getModelObject().setLabelSuffix(labelSuffix);
					containerForm.getModelObject().setVersion(version);
					
					exampleBarcodeDataFile.setModelObject(iLimsAdminService.getBarcodeLabelTemplate(barcodeLabelTemplateDdc.getModelObject()));
					exampleBarcodeDataFile.setVisible(true);
					target.add(exampleBarcodeDataFile);
					
					nameTxtFld.setModelObject(barcodeLabelTemplateDdc.getModelObject().getName());
					target.add(nameTxtFld);

					versionTxtFld.setModelObject(version);
					target.add(versionTxtFld);
				}
			}
		});
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(studyDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(versionTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(barcodeLabelDataPanel);
		arkCrudContainerVO.getDetailPanelFormContainer().add(barcodeLabelTemplateLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(barcodeLabelTemplateDdc);
		arkCrudContainerVO.getDetailPanelFormContainer().add(exampleBarcodeDataFile);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.name.required", this, new Model<String>("Name")));
		studyDdc.setRequired(true).setLabel(new StringResourceModel("error.study.required", this, new Model<String>("Study")));
		barcodePrinterDdc.setRequired(true).setLabel(new StringResourceModel("error.barcodePrinter.required", this, new Model<String>("BarcodePrinter")));
		versionTxtFld.setRequired(true).setLabel(new StringResourceModel("error.version.required", this, new Model<String>("Version")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new BarcodeLabel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		iLimsAdminService.deleteBarcodeLabel(containerForm.getModelObject());
		containerForm.info("The Barcode label record was deleted successfully.");
		editCancelProcess(target);
		onCancel(target);
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

	@Override
	protected void onSave(Form<BarcodeLabel> containerForm, AjaxRequestTarget target) {
		if(barcodePrinterName == null) {
			this.error("Barcode Printer is required");
		}
		else {
			containerForm.getModelObject().setBarcodePrinterName(barcodePrinterName.toString());
		
			if (isNew()) {
				if (barcodeLabelTemplateDdc.getModelObject() != null) {
					List<BarcodeLabelData> cloneBarcodeLabelDataList = iLimsAdminService.getBarcodeLabelDataByBarcodeLabel(barcodeLabelTemplateDdc.getModelObject());
					List<BarcodeLabelData> barcodeLabelDataList = new ArrayList<BarcodeLabelData>(0);
					for (Iterator<BarcodeLabelData> iterator = cloneBarcodeLabelDataList.iterator(); iterator.hasNext();) {
						BarcodeLabelData clonebarcodeLabelData = (BarcodeLabelData) iterator.next();
						BarcodeLabelData barcodeLabelData = new BarcodeLabelData();
						// Copy parent details to new barcodeLabelData
						try {
							PropertyUtils.copyProperties(barcodeLabelData, clonebarcodeLabelData);
						}
						catch (IllegalAccessException e) {
							log.error(e.getMessage());
						}
						catch (InvocationTargetException e) {
							log.error(e.getMessage());
						}
						catch (NoSuchMethodException e) {
							log.error(e.getMessage());
						}
						barcodeLabelData.setId(null);
						barcodeLabelDataList.add(barcodeLabelData);
					}
					containerForm.getModelObject().setBarcodeLabelData(barcodeLabelDataList);
				}
				
				iLimsAdminService.createBarcodeLabel(containerForm.getModelObject());
			}
			else {
				iLimsAdminService.updateBarcodeLabel(containerForm.getModelObject());
			}
			this.info("Barcode label: " + containerForm.getModelObject().getName() + " was created/updated successfully.");
		}
		target.add(feedBackPanel);
		onSavePostProcess(target);
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

	/**
	 * Returns a list of Studies the user is permitted to access
	 * 
	 * @return
	 */
	private List<Study> getStudyListForUser() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);

			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return studyListForUser;
	}
}
