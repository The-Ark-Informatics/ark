package au.org.theark.study.web.component.pedigree;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;


public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm workRequestContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = workRequestContainerForm;
	}

	public PageableListView<RelationshipVo> buildPageableListView(IModel iModel) {

		PageableListView<RelationshipVo> sitePageableListView = new PageableListView<RelationshipVo>("relationshipList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<RelationshipVo> item) {

				RelationshipVo relationshipVo = item.getModelObject();

//				if (relationshipVo.getIndividualId() != null) {
//					item.add(new Label(Constants.PEDIGREE_INDIVIDUAL_ID, relationshipVo.getIndividualId()));
//				}
//				else {
//					item.add(new Label(Constants.PEDIGREE_INDIVIDUAL_ID, ""));
//				}
				
				item.add(buildUidLink(relationshipVo));
				
				if (relationshipVo.getFirstName() != null) {
					item.add(new Label(Constants.PEDIGREE_FIRST_NAME, relationshipVo.getFirstName()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_FIRST_NAME, ""));
				}
				
				if (relationshipVo.getLastName() != null) {
					item.add(new Label(Constants.PEDIGREE_LAST_NAME, relationshipVo.getLastName()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_LAST_NAME, ""));
				}
				
				if (relationshipVo.getRelationship() != null) {
					item.add(new Label(Constants.PEDIGREE_RELATIONSHIP, relationshipVo.getRelationship()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_RELATIONSHIP, ""));
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);	
				String dob = "";
				if (relationshipVo.getDob() != null) {
					dob = simpleDateFormat.format(relationshipVo.getDob());
					item.add(new Label(Constants.PEDIGREE_DOB	, dob));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_DOB, dob));
				}
				if (relationshipVo.getTwin() != null) {
					item.add(new Label(Constants.PEDIGREE_TWIN, relationshipVo.getTwin()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_TWIN, ""));
				}
				
				item.add(buildUnsetLink(relationshipVo));
				
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
	private AjaxLink buildUidLink(final RelationshipVo relationshipVo) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.PEDIGREE_INDIVIDUAL_ID) {
			@Override
			public void onClick(AjaxRequestTarget target) {

				
			}
		};
		Label nameLinkLabel = new Label("uidLbl", relationshipVo.getIndividualId());
		link.add(nameLinkLabel);
		return link;
	}
	

	@SuppressWarnings( { "serial" })
	private AjaxLink buildUnsetLink(final RelationshipVo relationshipVo) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.PEDIGREE_RELATIONSHIP_DELETE) {
			@Override
			public void onClick(AjaxRequestTarget target) {

				
			}
		};
		Label nameLinkLabel = new Label("unsetLbl", "Unset");
		link.add(nameLinkLabel);
		return link;
	}

}
