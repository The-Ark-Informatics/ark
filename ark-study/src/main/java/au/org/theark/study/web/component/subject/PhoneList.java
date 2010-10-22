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

import au.org.theark.study.model.entity.Phone;
import au.org.theark.study.model.vo.SubjectVO;
import au.org.theark.study.web.component.subject.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class PhoneList extends Panel{

	
	private ContainerForm subjectContainerForm;
	private WebMarkupContainer phoneDetailPanelContainer;
	private WebMarkupContainer phoneListPanelContainer;
	/**
	 * @param id
	 */
	public PhoneList(String id,ContainerForm containerForm,WebMarkupContainer listContainer,WebMarkupContainer detailPanelContainer) {
		super(id);
		this.subjectContainerForm = containerForm;
		this.phoneListPanelContainer = listContainer;
		this.phoneDetailPanelContainer = detailPanelContainer;
	}
	
	public PageableListView<Phone> buildPageableListView(IModel iModel){
		
		PageableListView<Phone> phonePageableListView = new PageableListView<Phone>("phoneList",iModel,5) {

			@Override
			protected void populateItem(ListItem<Phone> item) {
				
				Phone phone = item.getModelObject();
				
				if(phone.getPhoneKey() != null){
					item.add(new Label("phone.phoneKey",phone.getPhoneKey().toString()));
				}else{
					item.add(new Label("phone.phoneKey",""));
				}
				
				if(phone.getAreaCode() != null){
					item.add(new Label("phone.areaCode",phone.getAreaCode().toString()));	
				}else{
					item.add(new Label("phone.areaCode",""));
				}
				item.add(buildLink(phone));
				//item.add(new Label("phone.phoneNumber",phone.getPhoneNumber().toString()));
				
				//TODO get the Name given the PhoneType Id
				//item.add(new Label("phone.phoneType",phone.getPhoneNumber().toString()));
				
			}
		};
		return phonePageableListView;
	}
	
	private AjaxLink buildLink(final Phone phone){
		
		AjaxLink link = new AjaxLink("phone.number") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				SubjectVO subjectVO = subjectContainerForm.getModelObject();
				subjectVO.setPhone(phone);
				phoneDetailPanelContainer.setVisible(true);
				phoneListPanelContainer.setVisible(false);
				
				target.addComponent(phoneDetailPanelContainer);
				target.addComponent(phoneListPanelContainer);
				
			}
		};
		return link;
	}

}
