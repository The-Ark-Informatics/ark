package au.org.theark.study.web.component.pedigree;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.web.Constants;

public class TwinSearchResultsListPanel extends Panel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public TwinSearchResultsListPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
public PageableListView<RelationshipVo> buildPageableListView(IModel iModel) {
		
	 PageableListView sitePageableListView = new PageableListView<RelationshipVo>("relationshipList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

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

}
