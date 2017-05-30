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
package au.org.theark.phenotypic.web.component.phenodataupload;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ByteDataResourceRequestHandler;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.panel.ConfirmationAnswer;
import au.org.theark.core.web.component.panel.YesNoPanel;
import au.org.theark.phenotypic.web.component.phenodataupload.form.ContainerForm;

/**
 * 
 * @author cellis
 * @author elam
 * 
 */
public class SearchResultListPanel extends Panel {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	
	private static final long	serialVersionUID	= 6150100976180421479L;

	private transient Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);
	private ModalWindow 			confirmModal;
	private ConfirmationAnswer		confirmationAnswer;
	private final String modalText = "<p>You are about to delete this record of</p><p><b>*.</b></p></p><p> But it will never delete the data imported to the Ark from this file previously.</p>";
	private SearchResultListPanel me;

	public SearchResultListPanel(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.setOutputMarkupId(true);
		me=this;
		initConfirmModel();
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<Upload> buildPageableListView(IModel iModel) {
		PageableListView<Upload> sitePageableListView = new PageableListView<Upload>(Constants.RESULT_LIST, iModel, iArkCommonService.getRowsPerPage()) {
			
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Upload> item) {
				Upload upload = item.getModelObject();

				// The ID
				if (upload.getId() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID, upload.getId().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_ID, ""));
				}

				// / The filename
				if (upload.getFilename() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME, upload.getFilename()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILENAME, ""));
				}

				// File Format
				if (upload.getFileFormat() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, upload.getFileFormat().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, ""));
				}

				// UserId
				if (upload.getUserId() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, upload.getUserId()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, ""));
				}

				// Start time
				if (upload.getStartTime() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_START_TIME, upload.getStartTime().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_START_TIME, ""));
				}

				// Finish time
				if (upload.getFinishTime() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FINISH_TIME, upload.getFinishTime().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FINISH_TIME, ""));
				}
				if (upload.getUploadStatus() != null && upload.getUploadStatus().getShortMessage()!=null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME, upload.getUploadStatus().getShortMessage()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME, ""));
				}

				// Download file link button
				item.add(buildDownloadButton(upload));

				// Download upload report button
				item.add(buildDownloadReportButton(upload));

				item.add(buildDeleteUploadButton(upload));
				// Delete the upload file
				// item.add(buildDeleteButton(upload));

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

	protected Link<Upload> buildDownloadLink(final Upload upload) {
		Link<Upload> link = new Link<Upload>(au.org.theark.phenotypic.web.Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick() {
				byte[] data = upload.getPayload().getPayload();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, upload.getFilename()));

			};
		};

		Label nameLinkLabel = new Label("downloadFileLbl", "Download File");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final Upload upload) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.phenotypic.web.Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Payload payload  = iArkCommonService.getPayloadForUpload(upload);
				byte[] data = payload.getPayload();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, upload.getFilename()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected Error: Could not process download request");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		//if (upload.getPayload() == null)
		//ajaxButton.setVisible(false);

		return ajaxButton;
	}

	protected Link<Upload> buildDownloadReportLink(final Upload upload) {
		Link<Upload> link = new Link<Upload>(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_REPORT) {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick() {
				byte[] data = upload.getUploadReport();//.getBytes(1, (int) upload.getUploadReport().length());
				log.warn("buildDownloadReportLink onclick get blob");
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, "uploadReport" + upload.getId() + ".txt"));
			};
		};

		Label nameLinkLabel = new Label("downloadReportLbl", "Download Report");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadReportButton(final Upload upload) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_REPORT) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Attempt to download the Blob as an array of bytes
				byte[] data = upload.getUploadReport();
				log.warn("buildDownloadReportButton onsubmit get blob");
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, "uploadReport" + upload.getId() + ".txt"));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected Error: Could not process download upload report request");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (upload.getUploadReport() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}
	/**
	 * 
	 * @param upload
	 * @return
	 */
	private AjaxButton buildDeleteUploadButton(Upload upload){
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.core.Constants.DELETE_UPLOAD){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				updateModelAndVarifyForDeleteUpload(upload);
				confirmModal.show(target);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when buildDeleteUploadButton pressed");
			};
		};
		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
	}
	/**
	 * 
	 * @param upload
	 */
	private void updateModelAndVarifyForDeleteUpload(Upload upload) {
		confirmModal.setContent(new YesNoPanel(confirmModal.getContentId(), modalText.replace("*"," ["+upload.getId()+"] "+upload.getFilename()),"Delete upload record.", confirmModal, confirmationAnswer));
		confirmModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
		private static final long serialVersionUID = 1L;
			public void onClose(AjaxRequestTarget target) {
				if (confirmationAnswer.isAnswer() ) {
					iArkCommonService.deleteUpload(upload);
					target.add(me);
				} else {//if no nothing be done.Just close I guess
				}
			}
		});
		addOrReplace(confirmModal);
	}
	
	private void initConfirmModel(){
		confirmationAnswer = new ConfirmationAnswer(false);
		confirmModal = new ModalWindow("confirmModal");
		confirmModal.setCookieName("yesNoPanel");
		confirmModal.setContent(new YesNoPanel(confirmModal.getContentId(), modalText,"Delete upload record.", confirmModal, confirmationAnswer));
		addOrReplace(confirmModal);
	}

	/*
	 * TO DO: DELETE of uploaded file is not supported till we can verify whether all subjects within the upload have also been deleted. At present,
	 * there is no linking table clearly indicating which subjects came from which upload (i.e. will need to be looked at 1st).
	 */
	// private AjaxDeleteButton buildDeleteButton(final Upload upload) {
	// DeleteButton ajaxButton = new DeleteButton(upload, SearchResultListPanel.this) {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	// // Attempt to delete upload
	// if (upload.getId() != null) {
	// iArkCommonService.deleteUpload(upload);
	// }
	//				
	// containerForm.info("Data Upload file " + upload.getFilename() + " was deleted successfully.");
	//
	// // Update the result panel and contianerForm (for feedBack message)
	// target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	// target.add(containerForm);
	// }
	//
	// @Override
	// public boolean isVisible() {
	// SecurityManager securityManager = ThreadContext.getSecurityManager();
	// Subject currentUser = SecurityUtils.getSubject();
	// boolean flag = false;
	// if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE)) {
	// return flag = true;
	// }
	//
	// return flag;
	// }
	//
	// @Override
	// protected void onError(AjaxRequestTarget target, Form<?> form) {
	// this.error("Unexpected Error: Could not process delete request");
	// }
	//
	// };
	//
	// ajaxButton.setDefaultFormProcessing(false);
	//
	// return ajaxButton;
	// }
}
