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
package au.org.theark.study.web.component.subjectFile;

import java.sql.SQLException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectFile.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked", "unused" })
public class SearchResultListPanel extends Panel {
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService		studyService;

	private transient Logger	log	= LoggerFactory.getLogger(SearchResultListPanel.class);

	private WebMarkupContainer	detailPanelContainer;
	private WebMarkupContainer	detailPanelFormContainer;
	private WebMarkupContainer	searchPanelContainer;
	private WebMarkupContainer	searchResultContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private ContainerForm		containerForm;

	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer searchPanelContainer,
			WebMarkupContainer searchResultContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, ContainerForm containerForm) {

		super(id);
		// TODO Auto-generated constructor stub
		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.searchResultContainer = searchResultContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of SubjectFile
	 */
	public PageableListView<SubjectFile> buildPageableListView(IModel iModel) {
		PageableListView<SubjectFile> sitePageableListView = new PageableListView<SubjectFile>(Constants.RESULT_LIST, iModel, Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<SubjectFile> item) {
				SubjectFile subjectFile = item.getModelObject();
				// The ID
				if (subjectFile.getId() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_ID, subjectFile.getId().toString()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_ID, ""));
				}

				// The filename
				if (subjectFile.getFilename() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME, subjectFile.getFilename()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME, ""));
				}

				// The study component
				if (subjectFile.getStudyComp() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_STUDY_COMP, subjectFile.getStudyComp().getName()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_STUDY_COMP, ""));
				}

				// TODO when displaying text escape any special characters
				// UserId
				if (subjectFile.getUserId() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_USER_ID, subjectFile.getUserId()));// the ID here must match the
					// ones in
					// mark-up
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_USER_ID, ""));// the ID here must match the ones in mark-up
				}

				// Comments
				if (subjectFile.getComments() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS, subjectFile.getComments()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS, ""));
				}

				// Download file link button
				AjaxButton downloadButton = buildDownloadButton(subjectFile);
				item.add(downloadButton);

				// Delete the upload file
				item.add(buildDeleteButton(subjectFile, downloadButton));

				// For the alternative stripes
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return sitePageableListView;
	}

	private Link buildDownloadLink(final SubjectFile subjectFile) {
		Link link = new Link(au.org.theark.study.web.Constants.DOWNLOAD_FILE) {
			@Override
			public void onClick() {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = subjectFile.getPayload().getBytes(1, (int) subjectFile.getPayload().length());
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("", data, subjectFile.getFilename()));

			};
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("downloadFileLbl", "Download File");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final SubjectFile subjectFile) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.study.web.Constants.DOWNLOAD_FILE, new StringResourceModel("downloadKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					data = subjectFile.getPayload().getBytes(1, (int) subjectFile.getPayload().length());
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("", data, subjectFile.getFilename()));
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (subjectFile.getPayload() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}

	private AjaxDeleteButton buildDeleteButton(final SubjectFile subjectFile, final AjaxButton downloadButton) {

		DeleteButton ajaxButton = new DeleteButton(subjectFile, SearchResultListPanel.this) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to delete upload
				if (subjectFile.getId() != null) {
					try {
						// TODO: implement disabling of other buttons on row when buttons clicked once
						downloadButton.setEnabled(false);
						target.addComponent(downloadButton);
						studyService.delete(subjectFile);
					}
					catch (ArkSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				containerForm.info("Attachment " + subjectFile.getFilename() + " was deleted successfully.");

				// Update the result panel
				target.addComponent(searchResultContainer);
				target.addComponent(containerForm);
			}

			@Override
			public boolean isVisible() {
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();
				boolean flag = false;
				if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE)) {
					return flag = true;
				}

				return flag;
			}
		};

		// TODO: Check permissions for delete
		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		return ajaxButton;
	}
}
