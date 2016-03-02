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

import java.util.Iterator;
import java.util.List;

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
import au.org.theark.core.model.geno.entity.Pipeline;
import au.org.theark.core.model.geno.entity.Process;
import au.org.theark.core.model.geno.entity.ProcessInput;
import au.org.theark.core.model.geno.entity.ProcessOutput;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkCRUDHelper;
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

	public ProcessResultListPanel(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO  = arkCrudContainerVO;
		
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
		PageableListView<Process> pageableListView = new PageableListView<Process>(Constants.RESULT_LIST, iModel, iArkCommonService.getUserConfig(Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {
			
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
					
				if(process != null && process.getCommand() != null) {
					item.add(new Label("command", process.getCommand().getName()));
				}
				else {
					item.add(new Label("command", ""));
				}
				
				
//				List<Command> commandList = iArkCommonService.getCommands();
//				ChoiceRenderer<Command> defaultChoiceRenderer = new ChoiceRenderer<Command>(Constants.NAME, Constants.ID);
//				DropDownChoice<Command> commandChoice = new DropDownChoice<Command>("command", commandList, defaultChoiceRenderer);
//				item.add(commandChoice);
				
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
				//target.appendJavaScript("alert('popup for process details');");
				
				arkCrudContainerVO = new ArkCrudContainerVO();
				ProcessContainerForm processContainerForm = new ProcessContainerForm("content", new CompoundPropertyModel<Process>(process));

				//add(processContainerForm);
				
				processDetailPanel = new ProcessDetailPanel("detailContainer", feedBackPanel, arkCrudContainerVO, processContainerForm);
				processDetailPanel.initialisePanel();
				//processContainerForm.add(processDetailPanel);
				
				DetailPanel dp = new DetailPanel("content", feedBackPanel, arkCrudContainerVO, containerForm);
				dp.initialisePanel();
				PageableListView<Pipeline> listView = null;
				SearchPanel sp = new SearchPanel("content", feedBackPanel, listView, containerForm, arkCrudContainerVO);
				sp.initialisePanel();
				
				//target.appendJavaScript("alert('Process : " + process.getName() + "');");
				List<ProcessInput> list = iArkCommonService.getProcessInputsForProcess(process);
				StringBuilder processInputDetails = new StringBuilder();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					ProcessInput processInput = (ProcessInput) iterator.next();
					processInputDetails.append("FileHash: ");
					processInputDetails.append(processInput.getInputFileHash());
					processInputDetails.append("<br>");
					processInputDetails.append("Input Location: ");
					processInputDetails.append(processInput.getinputFileLocation());
					processInputDetails.append("<br>");
					processInputDetails.append("File type: ");
					processInputDetails.append(processInput.getInputFileType());
					processInputDetails.append("<br>");
					processInputDetails.append("Input Kept: ");
					processInputDetails.append(processInput.getInputKept());
					processInputDetails.append("<br>");
					processInputDetails.append("Input Server: ");
					processInputDetails.append(processInput.getInputServer());
				}
				target.appendJavaScript("alert('ProcessInput Details : " + processInputDetails + "');");
				
				
				List<ProcessOutput> list2 = iArkCommonService.getProcessOutputsForProcess(process);
				StringBuilder processOutputDetails = new StringBuilder();
				for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
					ProcessOutput processOutput = (ProcessOutput) iterator.next();
					processOutputDetails.append("File Location: ");
					processOutputDetails.append(processOutput.getOutputFileLocation());
					processOutputDetails.append("<br>");
					processOutputDetails.append("File Hash: ");
					processOutputDetails.append(processOutput.getOutputFileHash());
					processOutputDetails.append("<br>");
					processOutputDetails.append("File type: ");
					processOutputDetails.append(processOutput.getOutputFileType());
					processOutputDetails.append("<br>");
					processOutputDetails.append("Output Kept: ");
					processOutputDetails.append(processOutput.getOutputKept());
					processOutputDetails.append("<br>");
					processOutputDetails.append("Output Server: ");
					processOutputDetails.append(processOutput.getOutputServer());
				}
				target.appendJavaScript("alert('ProcessOutput Details : " + processOutputDetails + "');");
				
				// Set the modalWindow title and content
				//modalWindow.setTitle("Edit Process Details");
				//modalWindow.setContent(new EmptyPanel("content"));
				
				modalWindow.show(target);
//				arkCrudContainerVO.getDetailPanelContainer().replaceWith(processDetailPanel);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}
		};
		Label nameLinkLabel = new Label("process", process.getName());
		link.add(nameLinkLabel);
		return link;
	}
}