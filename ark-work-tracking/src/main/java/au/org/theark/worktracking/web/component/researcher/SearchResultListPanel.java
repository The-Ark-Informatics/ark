package au.org.theark.worktracking.web.component.researcher;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.worktracking.model.vo.ResearcherVo;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.researcher.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;


	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
	}


	public PageableListView<Researcher> buildPageableListView(IModel iModel) {

		PageableListView<Researcher> sitePageableListView = new PageableListView<Researcher>("researcherList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Researcher> item) {

				Researcher researcher = item.getModelObject();

				if (researcher.getId() != null) {
					item.add(new Label(Constants.RESEARCHER_ID, researcher.getId().toString()));
				}
				else {
					item.add(new Label(Constants.RESEARCHER_ID, ""));
				}

				if (researcher.getInstitute() != null) {
					item.add(new Label(Constants.RESEARCHER_INSTITUTE, researcher.getInstitute()));
				}
				else {
					item.add(new Label(Constants.RESEARCHER_INSTITUTE, ""));
				}
				item.add(buildLink(researcher));				
				if (researcher.getLastActiveDate() != null) {
					item.add(new Label(Constants.RESEARCHER_LAST_ACTIVE_DATE, researcher.getLastActiveDate().toString()));
				}
				else {
					item.add(new Label(Constants.RESEARCHER_LAST_ACTIVE_DATE, ""));
				}
				if (researcher.getResearcherRole() != null) {
					item.add(new Label(Constants.RESEARCHER_ROLE, researcher.getResearcherRole().getName()));
				}
				else {
					item.add(new Label(Constants.RESEARCHER_ROLE, ""));
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
		return sitePageableListView;
	}

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final Researcher researcher) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.RESEARCHER_FULL_NAME) {

			@Override
			public void onClick(AjaxRequestTarget target) {

				ResearcherVo researcherVo = containerForm.getModelObject();
				researcherVo.setMode(Constants.MODE_EDIT);
				researcherVo.setResearcher(researcher);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}
		};
		
		String fullName=researcher.getFirstName() + " " + researcher.getLastName();
		Label nameLinkLabel = new Label("nameLbl", fullName);
		link.add(nameLinkLabel);
		return link;

	}

}
