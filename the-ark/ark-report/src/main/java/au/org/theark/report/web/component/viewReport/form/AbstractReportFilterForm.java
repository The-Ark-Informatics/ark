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
package au.org.theark.report.web.component.viewReport.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.report.entity.ReportOutputFormat;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.ArkFormVisitor;
import au.org.theark.report.model.vo.GenericReportViewVO;
import au.org.theark.report.service.IReportService;
import au.org.theark.report.web.Constants;
import au.org.theark.report.web.component.viewReport.ReportOutputPanel;

/**
 * @author elam
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractReportFilterForm<T extends GenericReportViewVO> extends AbstractContainerForm<T> {

	@SpringBean(name = au.org.theark.report.service.Constants.REPORT_SERVICE)
	protected IReportService							reportService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService						iArkCommonService;

	protected DropDownChoice<ReportOutputFormat>	outputFormatChoices;
	protected AjaxButton									generateButton;

	protected CompoundPropertyModel<T>				cpModel;
	protected ReportOutputPanel						reportOutputPanel;
	protected FeedbackPanel								feedbackPanel;

	// Add a visitor class for required field marking/validation/highlighting
	ArkFormVisitor											formVisitor	= new ArkFormVisitor();

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	public AbstractReportFilterForm(String id, CompoundPropertyModel<T> model) {
		super(id, model);
		this.cpModel = model;
	}

	public void initialiseFilterForm(FeedbackPanel feedbackPanel, ReportOutputPanel reportOutputPanel) {
		this.reportOutputPanel = reportOutputPanel;
		this.feedbackPanel = feedbackPanel;
		initialiseComponents();
	}

	private void initialiseComponents() {
		generateButton = new ArkBusyAjaxButton(Constants.GENERATE_BUTTON, new StringResourceModel("generateKey", this, null)) {
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(feedbackPanel);
				onGenerateProcess(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				onErrorProcess(target);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
					private static final long	serialVersionUID	= 1L;

					@Override
					public CharSequence postDecorateScript(CharSequence script) {
						return script + "wicketHide('" + reportOutputPanel.getMarkupId() + "');";
					}
				};
			}
		};

		initialiseOutputFormatChoice();
		initialiseCustomFilterComponents();
		this.add(outputFormatChoices);
		this.add(generateButton);
	}

	private void initialiseOutputFormatChoice() {

		T grvVO = cpModel.getObject();
		grvVO.setListReportOutputFormats(reportService.getOutputFormats());

		PropertyModel<ReportOutputFormat> outputChoicePM = new PropertyModel<ReportOutputFormat>(cpModel, "selectedOutputFormat");
		ChoiceRenderer<ReportOutputFormat> defaultChoiceRenderer = new ChoiceRenderer<ReportOutputFormat>(Constants.REPORT_OUTPUT_NAME);
		outputFormatChoices = new DropDownChoice<ReportOutputFormat>(Constants.REPORT_OUTPUT_DROP_DOWN_CHOICE, outputChoicePM, grvVO.getListReportOutputFormats(), defaultChoiceRenderer);
		outputFormatChoices.setRequired(true);

	}

	/**
	 * *DO NOT* call manually! This method will automatically be called as part of initialiseFiterForm(..)
	 */
	protected abstract void initialiseCustomFilterComponents();

	/**
	 * Called when the Generate button is clicked
	 */
	protected abstract void onGenerateProcess(AjaxRequestTarget target);

	protected void onErrorProcess(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}
}
