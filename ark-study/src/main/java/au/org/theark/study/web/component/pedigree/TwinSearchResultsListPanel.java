package au.org.theark.study.web.component.pedigree;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;

public class TwinSearchResultsListPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private ArkCrudContainerVO	 arkCrudContainerVO;
	
	private PageableListView twinPageableListView;
	
	private ContainerForm containerForm;
	
	public TwinSearchResultsListPanel(String id, ArkCrudContainerVO	 arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		// TODO Auto-generated constructor stub
	}
	
public PageableListView<RelationshipVo> buildPageableListView(IModel iModel) {
		
	 twinPageableListView = new PageableListView<RelationshipVo>("relationshipList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<RelationshipVo> item) {

				RelationshipVo relationshipVo = item.getModelObject();

				if (relationshipVo.getIndividualId() != null) {
					item.add(new Label(Constants.PEDIGREE_INDIVIDUAL_ID, relationshipVo.getIndividualId()));
				}
				else {
					item.add(new Label(Constants.PEDIGREE_INDIVIDUAL_ID, ""));
				}
				
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

				item.add(addNotTwinButton(relationshipVo));
				item.add(addMZTwinButton(relationshipVo));
				item.add(addDZTwinButton(relationshipVo));
		
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}

			
		};
		return twinPageableListView;
	}

private AjaxButton addNotTwinButton(final RelationshipVo relationshipVo){
	AjaxButton ajaxButton = new AjaxButton("pedigree.ntButton") {

		private static final long	serialVersionUID	= 4494157023173040075L;

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			
			relationshipVo.setTwin(null);
//			twinPageableListView.removeAll();
//			updateTwinRelationship(null, relationshipVo.getIndividualId());
			target.add(arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults"));
		}

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
	
		};
	};

	ajaxButton.setDefaultFormProcessing(false);

	if (relationshipVo.getTwin() == null){
		ajaxButton.setEnabled(false);
	}
	else{
		ajaxButton.setEnabled(true);
	}

	return ajaxButton;
}

private AjaxButton addMZTwinButton(final RelationshipVo relationshipVo){
	AjaxButton ajaxButton = new AjaxButton("pedigree.mzButton") {

		private static final long	serialVersionUID	= 4494157023173040075L;

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//			twinPageableListView.removeAll();
			relationshipVo.setTwin("MZ");
//			updateTwinRelationship("MZ", relationshipVo.getIndividualId());
			target.add(arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults"));
		}

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			// TODO: log!
			
		};
	};

	ajaxButton.setEnabled(true);
	ajaxButton.setDefaultFormProcessing(false);

	if ("MZ".equalsIgnoreCase(relationshipVo.getTwin())){
		ajaxButton.setEnabled(false);
	}
	else{
		ajaxButton.setEnabled(true);
	}

	return ajaxButton;
}

private AjaxButton addDZTwinButton(final RelationshipVo relationshipVo){
	AjaxButton ajaxButton = new AjaxButton("pedigree.dzButton") {

		private static final long	serialVersionUID	= 4494157023173040075L;

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//			twinPageableListView.removeAll();
			relationshipVo.setTwin("DZ");
//			updateTwinRelationship("DZ", relationshipVo.getIndividualId());
			target.add(arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults"));
		}

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			// TODO: log!
			
		};
	};

	ajaxButton.setEnabled(true);
	ajaxButton.setDefaultFormProcessing(false);

	if ("DZ".equalsIgnoreCase(relationshipVo.getTwin())){
		ajaxButton.setEnabled(false);
	}else{
		ajaxButton.setEnabled(true);
	}

	return ajaxButton;
}

private void updateTwinRelationship(final String type, final String individualId){
	List<RelationshipVo> relationships = containerForm.getModel().getObject().getRelationshipList();
	
	for(RelationshipVo relationship : relationships){
		if(individualId.equals(relationship.getIndividualId())){
			relationship.setTwin(type);
			break;
		}
	}
} 

}
