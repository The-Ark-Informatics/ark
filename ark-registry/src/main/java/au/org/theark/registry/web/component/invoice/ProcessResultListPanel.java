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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.registry.web.component.invoice.form.ContainerForm;
import au.org.theark.registry.web.component.invoice.form.ProcessContainerForm;

/**
 * 
 * @author cellis
 * @author elam
 * 
 */
public class ProcessResultListPanel extends Panel {

	private static final long	serialVersionUID	= 6150100976180421479L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;
	private ArkCrudContainerVO	 arkCrudContainerVO;
	private transient Logger	log					= LoggerFactory.getLogger(ProcessResultListPanel.class);
	private ContainerForm					containerForm;
	private  AbstractDetailModalWindow		modalWindow;
	private ProcessDetailPanel				processDetailPanel;
	private FeedbackPanel feedBackPanel;

	public ProcessResultListPanel(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.arkCrudContainerVO  = arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
		
		modalWindow = new AbstractDetailModalWindow("modalWindow") {
			
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
		};
		
		add(modalWindow);
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Upload
	 */
	@SuppressWarnings("unchecked")
	public PageableListView<Process> buildPageableListView(IModel iModel) {
		PageableListView<Process> pageableListView = new PageableListView<Process>(Constants.RESULT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Process> item) {
				Process p = item.getModelObject();

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
	public DataView<Process> buildDataView(ArkDataProvider<Process, IArkCommonService> subjectProvider) {

		DataView<Process> dataView = new DataView<Process>(Constants.RESULT_LIST, subjectProvider) {

			@Override
			protected void populateItem(final Item<Process> item) {
				Process process = item.getModelObject();
				
				if (process != null && process.getId() != null) {
					item.add(new Label("id", process.getId().toString() ));
				}
				else {
					item.add(new Label("id", ""));
				}
				
				item.add(buildLink(item.getModelObject()));
				//item.add(new Label("name", item.getModelObject().getName()));


				if (process != null && process.getDescription() != null) {
					item.add(new Label("description", process.getDescription()));
				}
				else {
					item.add(new Label("description", ""));
				}
				
				if (process != null && process.getStartTime() != null) {
					item.add(new Label("startTime", process.getStartTime().toString()));
				}
				else {
					item.add(new Label("startTime", ""));
				}
				
				if (process != null && process.getEndTime() != null) {
					item.add(new Label("endTime", process.getEndTime().toString()));
				}
				else {
					item.add(new Label("endTime", ""));
				}

				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		return dataView;
	}

	private AjaxLink buildLink(final Process process) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("processLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				//subject.getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));

				//ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				
				//POPUP FOR DETAILS
				target.appendJavaScript("alert('popup for process details');");
				
				ArkCrudContainerVO arkCrudContainerVO = new ArkCrudContainerVO();
				ProcessContainerForm processContainerForm = new ProcessContainerForm("content", new CompoundPropertyModel<Process>(process));
				
				add(processContainerForm);
				
				processDetailPanel = new ProcessDetailPanel("content", feedBackPanel, arkCrudContainerVO, processContainerForm);
				processDetailPanel.initialisePanel();
				//processContainerForm.add(processDetailPanel);
				DetailPanel dp = new DetailPanel("content", feedBackPanel, arkCrudContainerVO);
				dp.initialisePanel();
				
				// Set the modalWindow title and content
				modalWindow.setTitle("Edit Process Details");
				modalWindow.setContent(dp);
				modalWindow.show(target);
				
			}
		};
		Label nameLinkLabel = new Label("process", process.getName());
		link.add(nameLinkLabel);
		return link;
	}
}