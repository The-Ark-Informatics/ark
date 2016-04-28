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
package au.org.theark.study.web.component.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.StudyCalendar;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.calendar.form.ContainerForm;

public class SearchResultListPanel extends Panel {


	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService		iStudyService;
	
	/**
	 * 
	 * @param id
	 * @param crudContainerVO
	 * @param studyCompContainerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @param searchContainer
	 * @return
	 */
	public PageableListView<StudyCalendar> buildPageableListView(IModel iModel) {

		PageableListView<StudyCalendar> sitePageableListView = new PageableListView<StudyCalendar>("studyCalenderList", iModel, iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<StudyCalendar> item) {

				StudyCalendar studyCalendar = item.getModelObject();

				/* The Component ID */
				if (studyCalendar.getId() != null) {
					// Add the study Component Key here
					item.add(new Label("studyCalendar.id", studyCalendar.getId().toString()));
				}
				else {
					item.add(new Label("studyCalendar.id", ""));
				}
				/* Component Name Link */
				item.add(buildLink(studyCalendar));

				// TODO when displaying text escape any special characters
				/* Description */
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String startDate = "";
				if (studyCalendar.getStartDate() != null) {
					startDate = simpleDateFormat.format(studyCalendar.getStartDate()); 
					item.add(new Label("studyCalendar.startDate", startDate));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("studyCalendar.startDate", startDate));// the ID here must match the ones in mark-up
				}
				
				String endDate = "";
				if (studyCalendar.getStartDate() != null) {
					endDate = simpleDateFormat.format(studyCalendar.getEndDate()); 
					item.add(new Label("studyCalendar.endDate", endDate));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("studyCalendar.endDate", endDate));// the ID here must match the ones in mark-up
				}

				/* For the alternative stripes */
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

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final StudyCalendar studyCalendar) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("studyCalendar.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				StudyCalendarVo studyCalendarVo = containerForm.getModelObject();
				studyCalendarVo.setMode(Constants.MODE_EDIT);
				studyCalendarVo.setStudyCalendar(studyCalendar);// Sets the selected object into the model
				// Render the UI
				
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Collection<CustomField> availableCustomFields = iStudyService.getStudySubjectCustomFieldList(studyId);				
				studyCalendarVo.setAvailableCustomFields(availableCustomFields);
				
				Collection<CustomField> selectedustomFields = iStudyService.getSelectedCalendarCustomFieldList(studyCalendar);				
				studyCalendarVo.setSelectedCustomFields((ArrayList<CustomField>)selectedustomFields);
				
				
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}
		};

		// Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", studyCalendar.getName());
		link.add(nameLinkLabel);
		return link;

	}

}
