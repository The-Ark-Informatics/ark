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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressSubjectVO;
import au.org.theark.core.vo.ArkCrudContainerVO;
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
public class AddressListPanel extends Panel {

	private static final long											serialVersionUID	= 1L;
	private ContainerForm												containerForm;
	private ArkCrudContainerVO											arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService											iArkCommonService;
	private ArkDataProvider<Address, IStudyService>				addressProvider;				// For display and navigate purpose only  the page.
	private ArkDataProvider<AddressSubjectVO, IStudyService>	addressSubjectProvider;		// For export purposes only with subject UID.
	private DataView<Address>											dataViewAddress;
	private DataView<AddressSubjectVO>								dataViewAddressSubjectVO;
	private Long															sessionPersonId;
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService												studyService;
	private Person															person;

	public AddressListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		initialiseDataview();
	}

	/**
	 * Initialize the data view relevant to phone list.
	 */
	private void initialiseDataview() {
		sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		/**
		 * This is the address provider to the page able view.
		 */
		addressProvider = new ArkDataProvider<Address, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<Address>		listAddress;
			private List<Address>		listAddressForSize;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getAddressVo().getAddress().setPerson(person);
						listAddressForSize = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddressVo().getAddress());
						return listAddressForSize.size();
						
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

			public Iterator<? extends Address> iterator(int first, int count) {
				listAddress = studyService.pageablePersonAddressList(sessionPersonId, containerForm.getModelObject().getAddressVo().getAddress(), first, count);
				return listAddress.iterator();
			}
		};
		/**
		 * This is the address provider with subject uid. for export report.
		 */
		addressSubjectProvider = new ArkDataProvider<AddressSubjectVO, IStudyService>(studyService) {
			private static final long	serialVersionUID	= 1L;
			private List<Address>		listAddress;
			private List<Address>		listAddressForSize;

			public int size() {
				try {
					if (sessionPersonId != null) {
						person = studyService.getPerson(sessionPersonId);
						containerForm.getModelObject().getAddressVo().getAddress().setPerson(person);
						listAddressForSize = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddressVo().getAddress());
						return listAddressForSize.size();
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

			public Iterator<? extends AddressSubjectVO> iterator(int first, int count) {
				listAddress = studyService.pageablePersonAddressList(sessionPersonId, containerForm.getModelObject().getAddressVo().getAddress(), first, count);
				List<AddressSubjectVO> addressVoList = new ArrayList<AddressSubjectVO>();
				for (Address address : listAddress) {
					String sessionSubjectUId = SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID).toString();
					addressVoList.add(new AddressSubjectVO(address, sessionSubjectUId));
				}
				return addressVoList.iterator();
			}
		};

		addressProvider.setModel(Model.of(containerForm.getModelObject().getAddressVo().getAddress()));
		dataViewAddress = buildDataView(addressProvider);
		dataViewAddress.setItemsPerPage(iArkCommonService.getRowsPerPage());
		dataViewAddressSubjectVO = buildDataViewWithSubjectStudyID(addressSubjectProvider);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("addressNavigator", dataViewAddress);
		add(pageNavigator);

		List<IColumn<AddressSubjectVO>> columns = new ArrayList<IColumn<AddressSubjectVO>>();
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Subjetc UID"), "subjectUID"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Street Address"), "streetAddress"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("City"), "city"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("State"), "state.name"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Post Code"), "postCode"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Country"), "country.name"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Address Type"), "addressType.name"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("DateReceived"), "dateReceived"));
		columns.add(new ExportableTextColumn<AddressSubjectVO>(Model.of("Preferred Mailing Address"), "preferredMailingAddress"));

		DataTable table = new DataTable("datatable", columns, dataViewAddressSubjectVO.getDataProvider(), iArkCommonService.getRowsPerPage());
		List<String> headers = new ArrayList<String>(0);
		headers.add("Subject UID:");
		headers.add("Street Address:");
		headers.add("City:");
		headers.add("State:");
		headers.add("Post Code:");
		headers.add("Country:");
		headers.add("Address Type:");
		headers.add("DateReceived:");
		headers.add("Preferred Mailing Address:");

		String filename = sessionPersonId != null ? String.valueOf(sessionPersonId) + "_addressList" : "unknown" + "_addressList";
		RepeatingView toolbars = new RepeatingView("toolbars");
		//Disable the tool bar if session person not exsists.
		if(sessionPersonId==null){toolbars.setEnabled(false);}else{toolbars.setEnabled(true);}
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		add(toolbars);
		add(dataViewAddress);

	}

	/**
	 * Build the data view for display and navigate purposes.
	 * 
	 * @param addressProvider
	 * @return
	 */
	private DataView<Address> buildDataView(ArkDataProvider<Address, IStudyService> addressProvider) {

		DataView<Address> adressListDataView = new DataView<Address>("addressList", addressProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<Address> item) {

				Address address = item.getModelObject();
				item.add(buildLink(address));

				if (address.getCity() != null) {
					item.add(new Label("city", address.getCity()));
				}
				else {
					item.add(new Label("city", ""));
				}

				if (address.getState() != null && address.getState().getName() != null) {
					item.add(new Label("state.name", address.getState().getName()));// TODO things like this might almost need to be constants
				}
				else {
					item.add(new Label("state.name", (address.getOtherState() != null && !address.getOtherState().isEmpty()) ? ("other: " + address.getOtherState()) : "not defined"));
				}

				if (address.getPostCode() != null) {
					item.add(new Label("postCode", address.getPostCode()));
				}
				else {
					item.add(new Label("postCode", ""));
				}

				if (address.getCountry() != null && address.getCountry().getName() != null) {
					item.add(new Label("country.name", address.getCountry().getName()));
				}
				else {
					item.add(new Label("country.name", ""));
				}

				if (address.getAddressType() != null && address.getAddressType().getName() != null) {
					item.add(new Label("addressType.name", address.getAddressType().getName()));
				}
				else {
					item.add(new Label("addressType.name", ""));
				}

				if (address.getDateReceived() != null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateReceived = "";
					dateReceived = simpleDateFormat.format(address.getDateReceived());
					item.add(new Label("address.dateReceived", dateReceived));
				}
				else {
					item.add(new Label("address.dateReceived", ""));
				}

				if (address.getPreferredMailingAddress() != null && address.getPreferredMailingAddress() == true) {
					item.add(new ContextImage("address.preferredMailingAddress", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new Label("address.preferredMailingAddress", ""));
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

		return adressListDataView;
	}

	/**
	 * Build the data view for export purposes with subject id.
	 * 
	 * @param addressProvider
	 * @return
	 */
	private DataView<AddressSubjectVO> buildDataViewWithSubjectStudyID(ArkDataProvider<AddressSubjectVO, IStudyService> addressProvider) {

		DataView<AddressSubjectVO> addressListDataView = new DataView<AddressSubjectVO>("addressList", addressProvider) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<AddressSubjectVO> item) {

				AddressSubjectVO addressSubjectVO = item.getModelObject();
				// item.add(buildLink(address));

				if (addressSubjectVO.getSubjectUID() != null) {
					item.add(new Label("subjectUID", addressSubjectVO.getSubjectUID()));
				}
				else {
					item.add(new Label("subjectUID", ""));
				}
				if (addressSubjectVO.getStreetAddress() != null) {
					item.add(new Label("streetAddress", addressSubjectVO.getStreetAddress()));
				}
				else {
					item.add(new Label("streetAddress", ""));
				}
				if (addressSubjectVO.getCity() != null) {
					item.add(new Label("city", addressSubjectVO.getCity()));
				}
				else {
					item.add(new Label("city", ""));
				}

				if (addressSubjectVO.getState() != null && addressSubjectVO.getState().getName() != null) {
					item.add(new Label("state.name", addressSubjectVO.getState().getName()));// TODO things like this might almost need to be constants
				}
				else {
					item.add(new Label("state.name", (addressSubjectVO.getOtherState() != null && !addressSubjectVO.getOtherState().isEmpty()) ? ("other: " + addressSubjectVO.getOtherState())
							: "not defined"));
				}

				if (addressSubjectVO.getPostCode() != null) {
					item.add(new Label("postCode", addressSubjectVO.getPostCode()));
				}
				else {
					item.add(new Label("postCode", ""));
				}

				if (addressSubjectVO.getCountry() != null && addressSubjectVO.getCountry().getName() != null) {
					item.add(new Label("country.name", addressSubjectVO.getCountry().getName()));
				}
				else {
					item.add(new Label("country.name", ""));
				}

				if (addressSubjectVO.getAddressType() != null && addressSubjectVO.getAddressType().getName() != null) {
					item.add(new Label("addressType.name", addressSubjectVO.getAddressType().getName()));
				}
				else {
					item.add(new Label("addressType.name", ""));
				}

				if (addressSubjectVO.getDateReceived() != null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					String dateReceived = "";
					dateReceived = simpleDateFormat.format(addressSubjectVO.getDateReceived());
					item.add(new Label("address.dateReceived", dateReceived));
				}
				else {
					item.add(new Label("address.dateReceived", ""));
				}

				if (addressSubjectVO.getPreferredMailingAddress() != null && addressSubjectVO.getPreferredMailingAddress() == true) {
					item.add(new ContextImage("address.preferredMailingAddress", new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new Label("address.preferredMailingAddress", ""));
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
		return addressListDataView;
	}
	/**
	 * Build the link to the address.
	 * @param address
	 * @return
	 */
	private AjaxLink<String> buildLink(final Address address) {

		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("address") {

			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				containerForm.getModelObject().getAddressVo().setAddress(address);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResultsForMultiplePanels(target, arkCrudContainerVO, au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL,
						au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL,au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL);
			}
		};
		String add1 = address.getAddressLineOne() != null ? address.getAddressLineOne() : "";
		String streetAdd = address.getStreetAddress() == null ? "" : address.getStreetAddress();
		String concat = null;
		if (add1.isEmpty() && streetAdd.isEmpty()) {
			concat = "no street address specified";
		}
		else {
			concat = (add1.isEmpty() ? streetAdd : (add1 + " " + streetAdd));
		}
		// could be neatened up a little more...but that will do for now. presentation later
		Label nameLinkLabel = new Label(Constants.ADDRESS_LABEL, concat);
		link.add(nameLinkLabel);
		return link;
	}
}
