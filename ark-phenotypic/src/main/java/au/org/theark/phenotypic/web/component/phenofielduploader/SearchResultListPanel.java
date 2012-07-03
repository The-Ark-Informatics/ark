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
package au.org.theark.phenotypic.web.component.phenofielduploader;

import java.sql.SQLException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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

import au.org.theark.core.model.study.entity.Payload;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ByteDataResourceRequestHandler;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.ArkDownloadTemplateButton;
import au.org.theark.phenotypic.web.component.phenofielduploader.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked", "unused" })
public class SearchResultListPanel extends Panel {
	
	private transient Logger	log	= LoggerFactory.getLogger(SearchResultListPanel.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;
	private PageableListView<Upload>	listView;

	/**
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, PageableListView<Upload> listView,ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.listView = listView;

		ArkDownloadTemplateButton downloadTemplateButton = new ArkDownloadTemplateButton("downloadTemplate", "DataDictionaryUpload", au.org.theark.core.Constants.DATA_DICTIONARY_HEADER) {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected Error: Download request could not be processed");
			}
			
		};
		add(downloadTemplateButton);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	public PageableListView<Upload> buildPageableListView(IModel iModel) {
		PageableListView<Upload> sitePageableListView = new PageableListView<Upload>(Constants.RESULT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
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
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, upload.getFileFormat().getName()));// the name
					// here
					// must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FILE_FORMAT, ""));// the ID here must match the ones in
					// mark-up
				}

				// UserId
				if (upload.getUserId() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, upload.getUserId()));// the ID here must match the
					// ones in
					// mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_USER_ID, ""));// the ID here must match the ones in mark-up
				}

				// Start time
				if (upload.getStartTime() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_START_TIME, upload.getStartTime().toString()));// the ID here
					// must
					// match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_START_TIME, ""));// the ID here must match the ones in
					// mark-up
				}

				// Finish time
				if (upload.getFinishTime() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FINISH_TIME, upload.getFinishTime().toString()));// the ID
					// here must
					// match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_FINISH_TIME, ""));
					// the ID here must match the ones in mark-up
				}

				// Download file link button
				item.add(buildDownloadButton(upload));

				// Download upload report button
				item.add(buildDownloadReportButton(upload));

				// For the alternative stripes
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return sitePageableListView;
	}

	private Link buildDownloadLink(final Upload upload) {
		Link link = new Link(au.org.theark.phenotypic.web.Constants.DOWNLOAD_FILE) {
			@Override
			public void onClick() {

				Payload payload  = iArkCommonService.getPayloadForUpload(upload);
				byte[] data = payload.getPayload();
				
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/csv", data, upload.getFilename()));

			};
		};

		// Add the label for the link
		Label nameLinkLabel = new Label("downloadFileLbl", "Download File");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadButton(final Upload upload) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.phenotypic.web.Constants.DOWNLOAD_FILE, new StringResourceModel("downloadKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Payload payload  = iArkCommonService.getPayloadForUpload(upload);
				byte[] data = payload.getPayload();
				
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/csv", data, upload.getFilename()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when buildDownloadButton pressed");
			};
		};

		ajaxButton.setDefaultFormProcessing(false);

		//log.warn("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n how many times is this run?");

		//TODO remove
		//Payload payload  = iArkCommonService.getPayloadForUpload(upload);
		//byte[] data = payload.getPayload();

		
		
		//TODO move back
		//if (upload.getPayload() == null)
		//if (data == null)
		//	ajaxButton.setVisible(false);

		return ajaxButton;
	}

	private Link buildDownloadReportLink(final Upload upload) {
		Link link = new Link(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_REPORT) {
			@Override
			public void onClick() {
				byte[] data  = upload.getUploadReport();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, "uploadReport" + upload.getId()));
			};
		};

		// Add the label for the link
		Label nameLinkLabel = new Label("downloadReportLbl", "Download Report");
		link.add(nameLinkLabel);
		return link;
	}

	private AjaxButton buildDownloadReportButton(final Upload upload) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.phenotypic.web.Constants.UPLOADVO_UPLOAD_UPLOAD_REPORT, new StringResourceModel("downloadReportKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				byte[] data = upload.getUploadReport();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/plain", data, "uploadReport" + upload.getId()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when buildDownloadReportButton pressed");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (upload.getUploadReport() == null)
			ajaxButton.setVisible(false);

		return ajaxButton;
	}
	
}