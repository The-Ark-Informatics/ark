/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.customfield.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class SearchResultListPanel extends Panel{
	
	protected ArkCrudContainerVO arkCrudContainerVO;
	protected ContainerForm containerForm;
	/**
	 * @param id
	 */
	public SearchResultListPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		
		// TODO Auto-generated constructor stub
	}

	public PageableListView<SubjectCustmFld> buildPageableListView(IModel iModel) {
		
		PageableListView<SubjectCustmFld> pageableListView = new PageableListView<SubjectCustmFld>(Constants.SEARCH_RESULT_LIST,iModel,au.org.theark.core.Constants.ROWS_PER_PAGE){

			@Override
			protected void populateItem(ListItem<SubjectCustmFld> item) {
				
				SubjectCustmFld customField = item.getModelObject();
				
				item.add(buildLink(customField));
				
				if(customField.getName() != null){
					item.add( new Label("name",customField.getName()));
				}else{
					item.add( new Label("name",""));
				}
			}
		};
		
		return pageableListView;
	}
	
	private AjaxLink buildLink(final SubjectCustmFld customField){
		
		Label nameLinkLabel = new Label(Constants.CUSTOM_FIELD_LABEL, customField.getFieldTitle());
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("fieldTitle"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				CustomFieldVO customFieldVO = containerForm.getModelObject();
			
			}
		};
		
		link.add(nameLinkLabel);
		return link;
	}

}
