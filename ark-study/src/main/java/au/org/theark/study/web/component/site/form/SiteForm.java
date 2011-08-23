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
package au.org.theark.study.web.component.site.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.vo.SiteModelVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.Details;

public class SiteForm extends Form<SiteModelVO> {

	@SpringBean(name = "userService")
	private IUserService			userService;
	private WebMarkupContainer	resultListContainer;

	private AjaxButton			saveButton;
	private AjaxButton			cancelButton;

	private TextField<String>	siteNameTxtFld;
	private TextArea<String>	siteDescription;
	private ContainerForm		containerForm;
	private Details				detailsPanel;

	public SiteForm(String id, Details details, WebMarkupContainer listContainer, final WebMarkupContainer detailsContainer, ContainerForm siteContainerForm) {

		super(id);

		this.resultListContainer = listContainer;
		this.detailsPanel = details;
		this.containerForm = siteContainerForm;

		/* Action buttons */

		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				resultListContainer.setVisible(false);
				detailsContainer.setVisible(false);
				target.addComponent(detailsContainer);
				target.addComponent(resultListContainer);
				containerForm.getModelObject().setSiteVo(new SiteVO());
				onCancel(target);
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// CompoundPropertyModel<SiteModelVO> cpm = detailsPanel.getCpm();

				// SiteModelVO siteModel = cpm.getObject();
				target.addComponent(detailsContainer);
				onSave(containerForm.getModelObject(), target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processFeedback(target);
			}
		};
	}

	public void initialiseForm() {

		siteNameTxtFld = new TextField<String>("siteVo.siteName");
		siteNameTxtFld.add(new ArkDefaultFormFocusBehavior());
		siteDescription = new TextArea<String>("siteVo.siteDescription");
		attachValidators();
		addComponents();
	}

	private void addComponents() {
		add(siteNameTxtFld);
		add(siteDescription);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));

	}

	private void attachValidators() {

		siteNameTxtFld.setRequired(true);
		siteDescription.add(StringValidator.lengthBetween(1, 255));
	}

	protected void onSave(SiteModelVO siteModel, AjaxRequestTarget target) {

	}

	protected void onCancel(AjaxRequestTarget target) {

	}

	protected void onArchive(StudyModelVO studyModel, AjaxRequestTarget target) {

	}

	protected void processFeedback(AjaxRequestTarget target) {

	}

}
