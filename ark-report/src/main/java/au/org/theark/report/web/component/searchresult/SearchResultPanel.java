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
package au.org.theark.report.web.component.searchresult;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.report.entity.SearchPayload;
import au.org.theark.core.model.report.entity.SearchResult;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ByteDataResourceRequestHandler;

@SuppressWarnings( { "unchecked" })
public class SearchResultPanel extends Panel {


	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private static final long	serialVersionUID	= 6069001768176246767L;
 	private transient Logger	log					= LoggerFactory.getLogger(SearchResultPanel.class);
	protected IModel<Object>				iModel;
	private PageableListView<SearchResult>	pageableListView;

	/**
	 * 
	 * @param id
	 */
	public SearchResultPanel(String id, final Long searchResultId) {
		super(id);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				pageableListView.removeAll();
				return iArkCommonService.getSearchResultList(searchResultId);
			}
		};

		pageableListView = buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
		add(pageNavigator);
		add(pageableListView);
	}
	
	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	public PageableListView<SearchResult> buildPageableListView(IModel iModel) {
		PageableListView<SearchResult> sitePageableListView = new PageableListView<SearchResult>(Constants.RESULT_LIST, iModel, iArkCommonService.getCustomFieldsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<SearchResult> item) {
				SearchResult searchResult = item.getModelObject();

				// The ID
				if (searchResult.getId() != null) {
					// Add the id component here
					item.add(new Label("searchResult.id", searchResult.getId().toString()));
				}
				else {
					item.add(new Label("searchResult.id", ""));
				}

				// / The filename
				if (searchResult.getFilename() != null) {
					// Add the id component here
					item.add(new Label("searchResult.fileName", searchResult.getFilename()));
				}
				else {
					item.add(new Label("searchResult.fileName", ""));
				}
				
				// File Format
				if (searchResult.getFileFormat() != null) {
					item.add(new Label("searchResult.fileFormat", searchResult.getFileFormat().getName()));
				}
				else {
					item.add(new Label("searchResult.fileFormat", ""));
				}

				// UserId
				if (searchResult.getUserId() != null) {
					item.add(new Label("searchResult.userId", searchResult.getUserId()));
				}
				else {
					item.add(new Label("searchResult.userId", ""));
				}

				// Start time
				if (searchResult.getStartTime() != null) {
					item.add(new Label("searchResult.startTime", searchResult.getStartTime().toString()));
				}
				else {
					item.add(new Label("searchResult.startTime", ""));
				}

				// Finish time
				if (searchResult.getFinishTime() != null) {
					item.add(new Label("searchResult.finishTime", searchResult.getFinishTime().toString()));
				}
				else {
					item.add(new Label("searchResult.finishTime", ""));
				}
				/*
				if (searchResult.getUploadStatus() != null && searchResult.getUploadStatus().getShortMessage()!=null) {
					item.add(new Label(Constants.UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME, searchResult.getUploadStatus().getShortMessage()));
				}
				else {
					item.add(new Label(Constants.UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME, ""));
				}
				*/


				// Download file link button
				item.add(buildDownloadButton(searchResult));

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

	private AjaxButton buildDownloadButton(final SearchResult searchResult) {
		AjaxButton ajaxButton = new AjaxButton(au.org.theark.core.Constants.DOWNLOAD_FILE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SearchPayload payload  = iArkCommonService.getSearchPayloadForSearchResult(searchResult);
				byte[] data = payload.getPayload();
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new ByteDataResourceRequestHandler("text/csv", data, searchResult.getFilename()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("onError called when buildDownloadButton pressed");
			};
		};

		ajaxButton.setDefaultFormProcessing(false);

		//TODO TEST...payload should never be nulll...but test.
		//if (searchResult.getPayload() == null) {
		//ajaxButton.setVisible(false);
		//}

		return ajaxButton;
	}

}