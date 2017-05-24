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
package au.org.theark.study.web.component.attachments;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkCheckSumNotSameException;
import au.org.theark.core.exception.ArkFileNotFoundException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.attachments.form.ContainerForm;

/**
 * 
 * @author nivedann
 * @author elam
 * @author cellis
 * 
 */
public class SearchResultListPanel extends Panel {
	private static final long	serialVersionUID	= 8147089460348057381L;
	private transient Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		arkCommonService;
	
	@SpringBean(name = au.org.theark.study.web.Constants.STUDY_SERVICE)
	private IStudyService		studyService;
	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;

	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of SubjectFile
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<SubjectFile> buildPageableListView(IModel iModel) {
		PageableListView<SubjectFile> sitePageableListView = new PageableListView<SubjectFile>(Constants.RESULT_LIST, iModel, arkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

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
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME, subjectFile.getFilename()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_FILENAME, ""));
				}

				// The study component
				if (subjectFile.getStudyComp() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_STUDY_COMP, subjectFile.getStudyComp().getName()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_STUDY_COMP, ""));
				}

				// UserId
				if (subjectFile.getUserId() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_USER_ID, subjectFile.getUserId()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_USER_ID, ""));
				}

				// Comments
				if (subjectFile.getComments() != null) {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS, subjectFile.getComments()));
				}
				else {
					item.add(new Label(au.org.theark.study.web.Constants.SUBJECT_FILE_COMMENTS, ""));
				}

				// Download file button
				AjaxButton downloadButton = buildDownloadButton(subjectFile);
				item.add(downloadButton);

				// Delete file button
				item.add(buildDeleteButton(subjectFile, downloadButton));

				// For the alternative stripes
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

		};
		return sitePageableListView;
	}

	private AjaxButton buildDownloadButton(final SubjectFile subjectFile) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.study.web.Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = null;
				try {
					
//					data = subjectFile.getPayload();//.getBytes(1, (int) subjectFile.getPayload().length());
					
					Long studyId =subjectFile.getLinkSubjectStudy().getStudy().getId();
					String subjectUID = subjectFile.getLinkSubjectStudy().getSubjectUID();
					String fileId = subjectFile.getFileId();
					String checksum = subjectFile.getChecksum();
					
					data = arkCommonService.retriveArkFileAttachmentByteArray(studyId,subjectUID,au.org.theark.study.web.Constants.ARK_SUBJECT_ATTACHEMENT_DIR,fileId,checksum);

					if (data != null) {
						InputStream inputStream = new ByteArrayInputStream(data);
						OutputStream outputStream;

						final String tempDir = System.getProperty("java.io.tmpdir");
						final java.io.File file = new File(tempDir, subjectFile.getFilename());
						final String fileName = subjectFile.getFilename();
						outputStream = new FileOutputStream(file);
						IOUtils.copy(inputStream, outputStream);

						IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
						getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
							@Override
							public void respond(IRequestCycle requestCycle) {
								super.respond(requestCycle);
								Files.remove(file);
							}
						}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
					}
				}
				catch(ArkSystemException e){
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
					log.error("ArkSystemException" + e.getMessage(), e);
				}catch (IOException e) {
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
					log.error("IOException" + e.getMessage(), e);
				} catch (ArkFileNotFoundException e) {
					this.error("An unexpected error occurred. Download request could not be fulfilled.");
					log.error("FileNotFoundException" + e.getMessage(), e);
				} catch (ArkCheckSumNotSameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(containerForm);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Download request could not be fulfilled.");
				log.error("An unexpected error occurred. Download request could not be fulfilled.");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		//if (subjectFile.getPayload() == null)
		//ajaxButton.setVisible(false);

		return ajaxButton;
	}

	private AjaxDeleteButton buildDeleteButton(final SubjectFile subjectFile, final AjaxButton downloadButton) {

		DeleteButton ajaxButton = new DeleteButton(subjectFile, SearchResultListPanel.this) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to delete upload
				
				boolean success=false;
				if (subjectFile.getId() != null) {
					try {
						studyService.delete(subjectFile,au.org.theark.study.web.Constants.ARK_SUBJECT_ATTACHEMENT_DIR);
						success=true;
					}
					catch (ArkSystemException e) {
						this.error("An unexpected error occurred. Delete request could not be fulfilled.");
						log.error("ArkSystemException" + e.getMessage(), e);
					}
					catch (EntityNotFoundException e) {
						this.error("An unexpected error occurred. Delete request could not be fulfilled.");
						log.error("Entity not found" + e.getMessage(), e);
					} catch (ArkFileNotFoundException e) {
						this.error("File not found:"+e.getMessage());
					}
				}

				if(success){
					containerForm.info("Attachment " + subjectFile.getFilename() + " was successfully deleted.");
				}	
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
				if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE)) {
					return flag = true;
				}

				return flag;
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Delete request could not be fulfilled.");
				log.error("An unexpected error occurred. Delete request could not be fulfilled.");
			}
		};

		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
	}
}
