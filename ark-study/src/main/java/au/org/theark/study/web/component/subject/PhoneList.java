/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.component.subject.form.PhoneContainerForm;

/**
 * @author nivedann
 *
 */
public class PhoneList extends Panel{

	
	private PhoneContainerForm phoneContainerForm;
	private WebMarkupContainer phoneDetailPanelContainer;
	private WebMarkupContainer phoneListPanelContainer;
	/**
	 * @param id
	 */
	public PhoneList(String id,PhoneContainerForm containerForm,WebMarkupContainer listContainer,WebMarkupContainer detailPanelContainer) {
		super(id);
		this.phoneContainerForm = containerForm;
		this.phoneListPanelContainer = listContainer;
		this.phoneDetailPanelContainer = detailPanelContainer;
	}
	
	public PageableListView<Phone> buildPageableListView(IModel iModel){
		
		PageableListView<Phone> phonePageableListView = new PageableListView<Phone>("phoneNumberList",iModel,5) {

			@Override
			protected void populateItem(ListItem<Phone> item) {
				
				Phone phone = item.getModelObject();
				
				if(phone.getPhoneKey() != null){
					item.add(new Label("phoneKey",phone.getPhoneKey().toString()));
				}else{
					item.add(new Label("phoneKey",""));
				}
				
				if(phone.getAreaCode() != null){
					item.add(new Label("areaCode",phone.getAreaCode().toString()));	
				}else{
					item.add(new Label("areaCode",""));
				}
				item.add(buildLink(phone));
				if(phone.getPhoneType() != null && phone.getPhoneType().getName() != null){
					item.add(new Label("phoneType.name",phone.getPhoneType().getName()));	
				}else{
					item.add(new Label("phoneType.name",""));
				}				
				
			}
		};
		return phonePageableListView;
	}
	
	private AjaxLink buildLink(final Phone phone){
		
		AjaxLink link = new AjaxLink("phoneNumber") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SubjectVO subjectVO = phoneContainerForm.getModelObject();
				subjectVO.setPhone(phone);
				phoneDetailPanelContainer.setVisible(true);
				phoneListPanelContainer.setVisible(false);
				
				target.addComponent(phoneDetailPanelContainer);
				target.addComponent(phoneListPanelContainer);
				
			}
		};
		
		//Add the label for the link
		Label phoneNumberLabelLink = new Label("phonerNumberLblLink", phone.getPhoneNumber().toString());
		link.add(phoneNumberLabelLink);
		
		return link;
	}

}
