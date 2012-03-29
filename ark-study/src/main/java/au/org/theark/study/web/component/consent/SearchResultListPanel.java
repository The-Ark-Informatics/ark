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
package au.org.theark.study.web.component.consent;

import java.text.SimpleDateFormat;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.form.ContainerForm;
import au.org.theark.study.web.component.consent.form.FormHelper;

/**
 * @author nivedann
 * 
 */
public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService		studyService;

	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;

	/**
	 * @param id
	 * @param arkCrudContainerVO 
	 */
	public SearchResultListPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {

		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	@SuppressWarnings("unchecked")
	public PageableListView<Consent> buildPageableListView(IModel iModel) {

		PageableListView<Consent> pageableListView = new PageableListView<Consent>(Constants.CONSENT_LIST, iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Consent> item) {
				Consent consent = item.getModelObject();

				item.add(buildLink(consent));
				if (consent.getStudyComponentStatus() != null) {
					item.add(new Label("studyComponentStatus.name", consent.getStudyComponentStatus().getName()));
				}
				else {
					item.add(new Label("studyComponentStatus.name", " "));
				}

				if (consent.getConsentStatus() != null) {
					item.add(new Label("consentStatus.name", consent.getConsentStatus().getName()));
				}
				else {
					item.add(new Label("consentStatus.name", " "));
				}

				if (consent.getConsentType() != null) {
					item.add(new Label("consentType.name", consent.getConsentType().getName()));
				}
				else {
					item.add(new Label("consentType.name", ""));
				}

				if (consent.getConsentedBy() != null) {
					item.add(new Label("consentedBy", consent.getConsentedBy()));
				}
				else {
					item.add(new Label("consentedBy", ""));
				}

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				String consentDate = "";

				if (consent.getConsentDate() != null) {
					item.add(new Label("consentDate", simpleDateFormat.format(consent.getConsentDate())));
				}
				else {
					item.add(new Label("consentDate", consentDate));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return pageableListView;

	}

	private AjaxLink<String> buildLink(final Consent consent) {

		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("studyComp.name") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = consent.getId();

				try {
					Consent consentFromBackend = studyService.getConsent(id);
					containerForm.getModelObject().setConsent(consentFromBackend);
					// Add consentId into context (for use with consentFile(s))
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID, consentFromBackend.getId());

					WebMarkupContainer wmcPlain = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.WMC_PLAIN);
					WebMarkupContainer wmcRequested = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.WMC_REQUESTED);
					WebMarkupContainer wmcRecieved = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.WMC_RECIEVED);
					WebMarkupContainer wmcCompleted = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.WMC_COMPLETED);

					new FormHelper().updateStudyCompStatusDates(target, consentFromBackend.getStudyComponentStatus().getName(), wmcPlain, wmcRequested, wmcRecieved, wmcCompleted);

					ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
					
				}
				catch (ArkSystemException e) {
					containerForm.error("A System Error has occured please contact Support");
				}
			}
		};
		Label nameLinkLabel = new Label(Constants.CONSENT_COMPONENT_LABEL, consent.getStudyComp().getName());
		link.add(nameLinkLabel);
		return link;
	}

}
