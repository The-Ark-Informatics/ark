package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

public class CustomFieldDisplayListPanel extends Panel {

	public CustomFieldDisplayListPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public DataView<CustomFieldDisplay> buildDataView(ArkDataProvider2<CustomFieldDisplay, CustomFieldDisplay> provider){
		
		
		DataView<CustomFieldDisplay> dataView = new DataView<CustomFieldDisplay>("customFieldDisplayList", provider){

			@Override
			protected void populateItem(Item<CustomFieldDisplay> item) {
				
				CustomFieldDisplay cfd  = item.getModelObject();
				
				item.add(buildLink(item));
				if(cfd.getSequence() != null){
					item.add( new Label("sequence",cfd.getSequence().toString()));
				}else{
					item.add(new Label("sequence",""));
				}
				
				if(cfd.getRequiredMessage() != null){
					item.add( new Label("requiredMessage",cfd.getRequiredMessage()));
				}else{
					item.add(new Label("requiredMessage",""));
				}
				if(cfd.getRequired()){
					item.add( new Label("required", "Yes"));
				}else{
					item.add( new Label("required", "No"));
				}
				
		
				
			}
		};
		
		return dataView;
	}
	
	
	public WebMarkupContainer buildLink(final Item<CustomFieldDisplay> item){
		
		WebMarkupContainer linkWmc = new WebMarkupContainer("cfdLinkWmc", item.getModel());
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("id") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				//TODO open a Modal window and allow that modal window to display the selected item and be able to edit and save it as a separate transaction
			}
		};
		
		
		CustomFieldDisplay customFieldDisplay = item.getModelObject();
		Label idLink = new Label("idLabel", customFieldDisplay.getId().toString());
		link.add(idLink);
		linkWmc.add(link);
		return linkWmc;
	}
	

}
