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
package au.org.theark.registry.web.component.invoice;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;

/**
 * 
 * @author cellis
 * @author elam
 * 
 */
public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 6150100976180421479L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	private ArkCrudContainerVO	 arkCrudContainerVO;
	private transient Logger	log					= LoggerFactory.getLogger(SearchResultListPanel.class);
	private ContainerForm			containerForm;

	public SearchResultListPanel(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.arkCrudContainerVO  = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<Pipeline> buildPageableListView(IModel iModel) {
		PageableListView<Pipeline> pageableListView = new PageableListView<Pipeline>(Constants.RESULT_LIST, iModel, iArkCommonService.getRowsPerPage()) {
			
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Pipeline> item) {
				Pipeline p = item.getModelObject();

				// The ID
				if (p.getId() != null) {
					// Add the id component here
					item.add(new Label("id", p.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				// / The filename
				if (p.getName() != null) {
					// Add the id component here
					item.add(new Label("name", p.getName()));
				}
				else {
					item.add(new Label("name", ""));
				}

				// File Format
				if (p.getDescription() != null) {
					item.add(new Label("description", p.getDescription()));
				}
				else {
					item.add(new Label("description", ""));
				}


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
		return pageableListView;
	}

	@SuppressWarnings("serial")
	public DataView<Pipeline> buildDataView(ArkDataProvider<Pipeline, IArkCommonService> subjectProvider) {

		DataView<Pipeline> studyCompDataView = new DataView<Pipeline>(Constants.RESULT_LIST, subjectProvider) {

			@Override
			protected void populateItem(final Item<Pipeline> item) {
				Pipeline pipeline = item.getModelObject();
				
				if (pipeline != null && pipeline.getId() != null) {
					item.add(new Label("id", pipeline.getId().toString() ));
				}
				else {
					item.add(new Label("id", ""));
				}
				
				item.add(buildLink(item.getModelObject()));
				//item.add(new Label("name", item.getModelObject().getName()));

				

				if (pipeline != null && pipeline.getDescription() != null) {
					item.add(new Label("description", pipeline.getDescription()));
				}
				else {
					item.add(new Label("description", ""));
				}

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return studyCompDataView;
	}

	private AjaxLink buildLink(final Pipeline pipeline) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("pipelineLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				//subject.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

				containerForm.setModelObject(pipeline);
				
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		Label nameLinkLabel = new Label("pipeline", pipeline.getName());
		link.add(nameLinkLabel);
		return link;
	}
}