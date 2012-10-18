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
package au.org.theark.study.web.component.correspondence;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.correspondence.form.ContainerForm;

public class SearchResultListPanel extends Panel {
	private static final long	serialVersionUID	= 6424424894090501973L;
	private transient Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService		studyService;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;

	public SearchResultListPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<Correspondences> buildPageableListView(IModel iModel) {

		PageableListView<Correspondences> pageableListView = new PageableListView<Correspondences>("correspondenceList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 9076367524574951367L;

			@Override
			protected void populateItem(final ListItem<Correspondences> item) {
				Correspondences correspondence = item.getModelObject();

				// set the date to be the link to details
				item.add(buildLink(correspondence));

				if (correspondence.getTime() != null) {
					item.add(new Label("time", correspondence.getTime()));
				}
				else {
					item.add(new Label("time", ""));
				}

				if (correspondence.getOperator() != null) {
					item.add(new Label("operator.ldapUserName", correspondence.getOperator().getLdapUserName()));
				}
				else {
					item.add(new Label("operator.ldapUserName", ""));
				}

				if (correspondence.getCorrespondenceModeType() != null) {
					item.add(new Label("correspondenceModeType.name", correspondence.getCorrespondenceModeType().getName()));
				}
				else {
					item.add(new Label("correspondenceModeType.name", ""));
				}

				if (correspondence.getCorrespondenceDirectionType() != null) {
					item.add(new Label("correspondenceDirectionType.name", correspondence.getCorrespondenceDirectionType().getName()));
				}
				else {
					item.add(new Label("correspondenceDirectionType.name", ""));
				}

				if (correspondence.getCorrespondenceOutcomeType() != null) {
					item.add(new Label("correspondenceOutcomeType.name", correspondence.getCorrespondenceOutcomeType().getName()));
				}
				else {
					item.add(new Label("correspondenceOutcomeType.name", ""));
				}

				if (correspondence.getReason() != null) {
					item.add(new Label("reason", correspondence.getReason()));
				}
				else {
					item.add(new Label("reason", ""));
				}

				// Download file button
				item.add(buildDownloadButton(correspondence));
				
				// Delete file button
				item.add(buildDeleteButton(correspondence));

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= -1588380616547616236L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

			
		};

		return pageableListView;
	}

	@SuppressWarnings( { "unchecked" })
	private AjaxLink buildLink(final Correspondences correspondence) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("correspondence") {

			private static final long	serialVersionUID	= 826367436671619720L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				containerForm.getModelObject().setCorrespondence(correspondence);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				target.add(containerForm);
				
				arkCrudContainerVO.getDetailPanelFormContainer().get("worktrackingcontainer").setVisible(false);
				AjaxButton ajaxButton = (AjaxButton)arkCrudContainerVO.getEditButtonContainer().get("delete");
				
				if(correspondence.getBillableItem()!=null){
					ajaxButton.setEnabled(false);
				}
				else{
					ajaxButton.setEnabled(true);
				}
				
			}
		};

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
		String dateOfCorrespondence = "";

		if (correspondence.getDate() != null)
			dateOfCorrespondence = simpleDateFormat.format(correspondence.getDate());

		Label nameLinkLabel = new Label("correspondenceLabel", dateOfCorrespondence);
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final Correspondences correspondences) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.study.web.Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 4494157023173040075L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				byte[] data = correspondences.getAttachmentPayload();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, correspondences.getAttachmentFilename()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error Downloading File: " + correspondences.getAttachmentFilename());
				this.error("There was an error while downloading file. Please contact Administrator");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (correspondences.getAttachmentFilename() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}
	
	private AjaxButton buildDeleteButton(final Correspondences correspondences) {
		DeleteButton ajaxButton = new DeleteButton(correspondences, SearchResultListPanel.this) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				String filename = correspondences.getAttachmentFilename();
				// Attempt to delete attachment
				if (correspondences.getId() != null) {
					try {
						correspondences.setAttachmentFilename(null);
						correspondences.setAttachmentPayload(null);
						studyService.update(correspondences);
					}
					catch (ArkSystemException e) {
						log.error(e.getMessage());
					}
					catch (EntityNotFoundException e) {
						log.error(e.getMessage());
					}
				}

				containerForm.info("Correspondence attachment " + filename + " was deleted successfully.");

				// Update the result panel
				// target.add(searchResultContainer);
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());

				target.add(containerForm);
			}

			@Override
			public boolean isVisible() {
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();
				boolean flag = false;
				flag = (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE) && (correspondences.getAttachmentFilename() != null)); 
				return flag;
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Delete request could not be fulfilled.");
			}
		};

		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
	}
}
