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
package au.org.theark.disease.web.component.disease.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.DiseaseVO;
import au.org.theark.disease.web.component.disease.AssociatedGenePalettePanel;
import au.org.theark.disease.web.component.disease.DiseaseCustomFieldsPalettePanel;

public class DetailForm extends AbstractDetailForm<DiseaseVO> {

	private static final long								serialVersionUID	= -9196914684971413116L;

	private WebMarkupContainer	arkContextMarkupContainer;

	private TextField<String> name;
	protected AssociatedGenePalettePanel<DiseaseVO> associatedGenesPalettePanel;
	protected DiseaseCustomFieldsPalettePanel<DiseaseVO> diseaseCustomFieldPalettePanel;
	
	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
	}

	@Override
	public void onBeforeRender() {		
		associatedGenesPalettePanel = new AssociatedGenePalettePanel<DiseaseVO>("associatedGenePalette", containerForm.getModel());
		associatedGenesPalettePanel.setOutputMarkupId(true);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(associatedGenesPalettePanel);
		
		diseaseCustomFieldPalettePanel = new DiseaseCustomFieldsPalettePanel<DiseaseVO>("diseaseCustomFieldsPalette", containerForm.getModel());
		diseaseCustomFieldPalettePanel.setOutputMarkupId(true);
		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(diseaseCustomFieldPalettePanel);
		
		if(!isNew()) {
			deleteButton.setVisible(true);
			associatedGenesPalettePanel.setVisible(true);
			diseaseCustomFieldPalettePanel.setVisible(true);
		} else {
			associatedGenesPalettePanel.setVisible(false);
			diseaseCustomFieldPalettePanel.setVisible(false);
		}
		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {

		name = new TextField<String>("disease.name");
		
		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(name);
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

	protected void onCancel(AjaxRequestTarget target) {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		DiseaseVO diseaseVO = new DiseaseVO();
		diseaseVO.getDisease().setStudy(study);
		containerForm.setModelObject(diseaseVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		name.setRequired(true).setLabel(new StringResourceModel("disease.name.required", this, null));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<DiseaseVO> containerForm, AjaxRequestTarget target) {
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId == null) {
			// No study in context
			this.error("There is no study selected. Please select a study to manage diseases.");
			processErrors(target);
		}
		else {			
			Disease disease = containerForm.getModelObject().getDisease();
			disease.setStudy(iArkCommonService.getStudy(studyId));
			
			disease.setGenes(new HashSet<Gene>(containerForm.getModelObject().getSelectedGenes()));
			disease.setCustomFields(new HashSet<CustomField>(containerForm.getModelObject().getSelectedCustomFields()));
			
			if(isNew()) {
				iArkDiseaseService.save(disease);
			} else {
				iArkDiseaseService.update(disease);
			}
			
			//Populating containerForm's Model Object for next render.
			List<Gene> availableGenes = iArkDiseaseService.getAvailableGenesForStudy(disease.getStudy());
			List<Gene> selectedGenes = new ArrayList<Gene>(disease.getGenes());		

			CustomField criteria = new CustomField();
			criteria.setStudy(disease.getStudy());
			criteria.setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS));
			List<CustomField> selectedCustomFields = new ArrayList<CustomField>(disease.getCustomFields());
			List<CustomField> availableCustomFields = iArkCommonService.getCustomFieldList(criteria);
							
			containerForm.getModelObject().setAvailableGenes(availableGenes);
			containerForm.getModelObject().setSelectedGenes(selectedGenes);
			containerForm.getModelObject().setAvailableCustomFields(availableCustomFields);
			containerForm.getModelObject().setSelectedCustomFields(selectedCustomFields);
			
			
			target.add(arkCrudContainerVO.getDetailPanelContainer());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		Disease disease = containerForm.getModelObject().getDisease();
		if(disease != null) {
			iArkDiseaseService.delete(disease);			
		}
		editCancelProcess(target);
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		return containerForm.getModelObject().getDisease().getId() == null;
	}
}
