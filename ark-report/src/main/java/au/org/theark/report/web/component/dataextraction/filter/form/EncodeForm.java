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
package au.org.theark.report.web.component.dataextraction.filter.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.QueryFilterVO;
import au.org.theark.core.web.component.customfield.dataentry.EncodedValueVO;

/**
 * @author sanjayam
 * 
 */
@SuppressWarnings( { "unchecked" })
public class EncodeForm extends Form<QueryFilterVO> {
	private static final long		serialVersionUID	= 1L;
	private static final Logger		log					= LoggerFactory.getLogger(EncodeForm.class);
	private DropDownChoice<EncodedValueVO>				encodeDdc;
	protected ModalWindow 			modalWindow;
	protected FeedbackPanel			feedbackPanel;
	private String encodedValues; 
	private QueryFilterVO queryFilterVO;

	public EncodeForm(String id, IModel<QueryFilterVO> model, ModalWindow modalWindow,ArkCrudContainerVO arkCrudContainerVO) {
		super(id, model);
		queryFilterVO=((QueryFilterVO) model.getObject());
		encodedValues=(queryFilterVO.getQueryFilter().getCustomFieldDisplay()!=null) ? queryFilterVO.getQueryFilter().getCustomFieldDisplay().getCustomField().getEncodedValues() 
				: queryFilterVO.getQueryFilter().getPhenoDataSetFieldDisplay().getPhenoDataSetField().getEncodedValues() ;
		this.modalWindow = modalWindow;
		this.feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		setMultiPart(true);
		add(feedbackPanel);
	}

	public void initialiseForm() {
		
		initEncodedValues();
		
		add(new AjaxButton(Constants.SETANDCLOSE) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if(onSave(target)) {
					modalWindow.close(target);
				}
				else{
					log.info("failed validation so don't permit save and close");
				}
				target.add(feedbackPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		});
	}

	
	private boolean onSave(AjaxRequestTarget target) {
		
		return true;
	}
	
	private void initEncodedValues() {
		List<String> encodeKeyValueList = Arrays.asList(encodedValues.split(";"));
		ArrayList<EncodedValueVO> choiceList = new ArrayList<EncodedValueVO>();
		for (String keyValue : encodeKeyValueList) {
			// Only split for the first instance of the '=' (allows the '=' character in the actual value)
			String[] keyValueArray = keyValue.split("=", 2);
			EncodedValueVO encodedValueVo = new EncodedValueVO();
			encodedValueVo.setKey(keyValueArray[0]);
			encodedValueVo.setValue(keyValueArray[1]);
			choiceList.add(encodedValueVo);
		}				
		ChoiceRenderer<EncodedValueVO> choiceRenderer = new ChoiceRenderer<EncodedValueVO>("value", "key");
		encodeDdc = new DropDownChoice<EncodedValueVO>(Constants.ENCODED_VALUES, choiceList, choiceRenderer);
		encodeDdc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				EncodedValueVO encodedValueVO=(EncodedValueVO)encodeDdc.getDefaultModelObject();
				queryFilterVO.getQueryFilter().setValue(encodedValueVO.getKey());
			}
		});
		add(encodeDdc);
	}

	
 
}