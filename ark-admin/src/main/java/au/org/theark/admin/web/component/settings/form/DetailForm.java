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
package au.org.theark.admin.web.component.settings.form;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.settings.form.ContainerForm;
import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailForm extends AbstractDetailForm<Setting> {


	private static final long				serialVersionUID	= -770595464410732350L;

	protected transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>			iAdminService;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param containerForm
	 * @param arkCrudContainerVo
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id, feedbackPanel, containerForm, arkCrudContainerVo);
		this.containerForm = containerForm;
		arkCrudContainerVO = arkCrudContainerVo;
		setMultiPart(true);
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {

		attachValidators();
		addDetailFormComponents();
	}


	@Override
	protected void attachValidators() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	protected void onSave(Form<Setting> containerForm, AjaxRequestTarget target) {

		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new Setting());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selectionO) {
		editCancelProcess(target);
	}

	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected boolean isNew() {
		if (containerForm.getModelObject().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
