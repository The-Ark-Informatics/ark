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
package au.org.theark.study.web.component.contact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhoneSubjectVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.contact.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class PhoneListPanel extends Panel {
	private static final long											serialVersionUID	= 1L;
	private ContainerForm												containerForm;
	protected ArkCrudContainerVO										arkCrudContainerVO;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService											iArkCommonService;
	private ArkDataProvider<Phone, IStudyService>						phoneProvider;				// Display and navigate purposes only.
	private ArkDataProvider<PhoneSubjectVO, IStudyService>				subjectPhoneProvider;	// Export purposes only.
	private DataView<Phone>												dataViewPhone;
	private DataView<PhoneSubjectVO>									dataViewPhoneSubject;
	private Long														sessionPersonId;
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService												studyService;
	private Person														person;
	private WebMarkupContainer											dataContainer;
	private FeedbackPanel 					feedBackPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public PhoneListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm, FeedbackPanel feedBackPanel) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel=feedBackPanel;
		initialiseDataview();

	}

	/**
	 * Initialize the data view relevant to phone list.
	 */
	private void initialiseDataview() {
		dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		/**
		 * phone Provider for the pageable list.
		 */
		phoneProvider = new ArkDataProvider<Phone, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<Phone>			listPhoneForSize;
			private List<Phone>			listPhone;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getPhoneVo().getPhone().setPerson(person);
						listPhoneForSize = studyService.getPersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhoneVo().getPhone());
						return listPhoneForSize.size();
					}else{
						return 0;
					}
					
				}
				catch (ArkSystemException e) {
					e.printStackTrace();
					return 0;
				}
				catch (EntityNotFoundException e) {
					e.printStackTrace();
					return 0;
				}
			}

			public Iterator<Phone> iterator(int first, int count) {
				listPhone = studyService.pageablePersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhoneVo().getPhone(), first, count);
				return listPhone.iterator();
			}
		};
		/**
		 * Subject phone provider for to include the subject id.
		 */
		subjectPhoneProvider = new ArkDataProvider<PhoneSubjectVO, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<Phone>			listPhoneForSize;
			private List<Phone>			listPhone;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getPhoneVo().getPhone().setPerson(person);
						listPhoneForSize = studyService.getPersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhoneVo().getPhone());
						return listPhoneForSize.size();
					}else{
						return 0;
					}
					
				}
				catch (ArkSystemException e) {
					e.printStackTrace();
					return 0;
				}
				catch (EntityNotFoundException e) {
					e.printStackTrace();
					return 0;
				}
			}

			public Iterator<PhoneSubjectVO> iterator(int first, int count) {
				listPhone = studyService.pageablePersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhoneVo().getPhone(), first, count);
				List<PhoneSubjectVO> phoneVoList = new ArrayList<PhoneSubjectVO>();
				for (Phone phone : listPhone) {
					String sessionSubjectUId = SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID).toString();
					phoneVoList.add(new PhoneSubjectVO(phone, sessionSubjectUId));
				}
				return phoneVoList.iterator();
			}
		};

		phoneProvider.setModel(Model.of(containerForm.getModelObject().getPhoneVo().getPhone()));
		dataViewPhone = buildDataView(phoneProvider);
		dataViewPhoneSubject = buildDataViewWithStudySubjectID(subjectPhoneProvider);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("phoneNavigator", dataViewPhone) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(dataContainer);
			}
		};
		dataContainer.add(pageNavigator);

		List<IColumn<PhoneSubjectVO>> exportColumns = new ArrayList<IColumn<PhoneSubjectVO>>();
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Subject UID"), "subjectUID"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("ID"), "id"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Area Code"), "areaCode"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Phone Number"), "phoneNumber"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Phone Type"), "phone.phoneType.name"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Phone Status"), "phone.phoneStatus.name"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Phone Valid From"), "validFrom"));
		exportColumns.add(new ExportableTextColumn<PhoneSubjectVO>(Model.of("Phone Valid To"), "validTo"));
		
		DataTable exportTable = new DataTable("datatable", exportColumns, dataViewPhoneSubject.getDataProvider(), iArkCommonService.getRowsPerPage());
		List<String> headers = new ArrayList<String>(0);
		headers.add("Subject UID:");
		headers.add("ID:");
		headers.add("Area Code:");
		headers.add("Phone Number:");
		headers.add("Phone Type:");
		headers.add("Phone Status:");
		headers.add("Phone Valid From:");
		headers.add("Phone Valid To:");
	
		String filename = sessionPersonId != null ? String.valueOf(sessionPersonId) + "_phoneNumberList" : "unknown" + "_phoneNumberList";
		RepeatingView toolbars = new RepeatingView("toolbars");
		//Disable the tool bar if session person not exsists.
		if(sessionPersonId==null){toolbars.setEnabled(false);}else{toolbars.setEnabled(true);}
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(exportTable, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		dataContainer.add(toolbars);
		dataContainer.add(dataViewPhone);
		add(dataContainer);
	}

	/**
	 * Create a dataview for display and navigate purposes.
	 * 
	 * @param phoneProvider
	 * @return
	 */
	private DataView<Phone> buildDataView(ArkDataProvider<Phone, IStudyService> phoneProvider) {

		DataView<Phone> phoneListDataView = new DataView<Phone>("phoneList", phoneProvider, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Phone> item) {

				Phone phone = item.getModelObject();
				item.add(buildLink(phone));
				if (phone.getId() != null) {
					item.add(new Label("id", phone.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				if (phone.getAreaCode() != null) {
					item.add(new Label("areaCode", phone.getAreaCode()));
				}
				else {
					item.add(new Label("areaCode", ""));
				}
				if (phone.getPhoneType() != null && phone.getPhoneType().getName() != null) {
					item.add(new Label("phoneType.name", phone.getPhoneType().getName()));
				}
				else {
					item.add(new Label("phoneType.name", ""));
				}
				if (phone.getPhoneStatus() != null && phone.getPhoneStatus().getName() != null) {
					item.add(new Label("phoneStatus.name", phone.getPhoneStatus().getName()));
				}
				else {
					item.add(new Label("phoneStatus.name", ""));
				}
				if (phone.getValidFrom() != null  ) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateValidFrom = "";
					dateValidFrom = simpleDateFormat.format(phone.getValidFrom());
					item.add(new Label("validFrom", dateValidFrom));
				}
				else {
					item.add(new Label("validFrom", ""));
				}
				if (phone.getValidTo() != null ) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateValidTo = "";
					dateValidTo = simpleDateFormat.format(phone.getValidTo());
					item.add(new Label("validTo", dateValidTo));
				}
				else {
					item.add(new Label("validTo", ""));
				}
				if (phone.getPreferredPhoneNumber() != null && phone.getPreferredPhoneNumber() == true) {
					item.add(new ContextImage("phone.preferredPhoneNumber", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new Label("phone.preferredPhoneNumber", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return phoneListDataView;
	}

	/**
	 * Create a dataview for only export purposes.
	 * 
	 * @param phoneProvider
	 * @return
	 */
	private DataView<PhoneSubjectVO> buildDataViewWithStudySubjectID(ArkDataProvider<PhoneSubjectVO, IStudyService> phoneProvider) {

		DataView<PhoneSubjectVO> phoneListDataView = new DataView<PhoneSubjectVO>("phoneListWithSubjectID", subjectPhoneProvider, iArkCommonService.getRowsPerPage()) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<PhoneSubjectVO> item) {

				PhoneSubjectVO phoneSubjectVO = item.getModelObject();
				if (phoneSubjectVO.getSubjectUID() != null) {
					item.add(new Label("subjectUId", phoneSubjectVO.getSubjectUID()));
				}
				else {
					item.add(new Label("subjectUId", ""));
				}
				if (phoneSubjectVO.getId() != null) {
					item.add(new Label("id", phoneSubjectVO.getId().toString()));
				}
				else {
					item.add(new Label("id", ""));
				}

				if (phoneSubjectVO.getAreaCode() != null) {
					item.add(new Label("areaCode", phoneSubjectVO.getAreaCode()));
				}
				else {
					item.add(new Label("areaCode", ""));
				}
				if (phoneSubjectVO.getPhoneType() != null && phoneSubjectVO.getPhoneType().getName() != null) {
					item.add(new Label("phoneType.name", phoneSubjectVO.getPhoneType().getName()));
				}
				else {
					item.add(new Label("phoneType.name", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return phoneListDataView;
	}

	/**
	 * Build the link.
	 * 
	 * @param phone
	 * @return
	 */
	private AjaxLink<String> buildLink(final Phone phone) {
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("phoneNumberLink") {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				//Clean up the feedback messages before show about the phone details.
				Session.get().cleanupFeedbackMessages();
				target.add(feedBackPanel);
				containerForm.getModelObject().getPhoneVo().setPhone(phone);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResultsForMultiplePanels(target, arkCrudContainerVO, au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL,
						au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL,au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL);
			}
		};
		Label nameLinkLabel = new Label(Constants.PHONE_NUMBER_VALUE, phone.getPhoneNumber());
		link.add(nameLinkLabel);
		return link;
	}

}
