package au.org.theark.study.web.component.site;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.service.IUserService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;


@SuppressWarnings("serial")
public class SearchResultList extends Panel{
	
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer searchPanelContainer;
	private ContainerForm containerForm;
	@SpringBean( name = "userService")
	private IUserService userService;
	
	/* A reference of the Model from the Container in this case Search Panel */
	private CompoundPropertyModel<SiteModel> cpm;
	
	
	public SearchResultList(String id, WebMarkupContainer  detailPanelContainer, WebMarkupContainer searchPanelContainer, ContainerForm siteContainerForm){
		super(id);
		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.containerForm = siteContainerForm;
	}
	public CompoundPropertyModel<SiteModel> getCpm() {
		return cpm;
	}

	public void setCpm(CompoundPropertyModel<SiteModel> cpm) {
		this.cpm = cpm;
	}
	
	
	public PageableListView<SiteVo> buildPageableListView(IModel iModel){
		
		PageableListView<SiteVo> sitePageableListView = new PageableListView<SiteVo>("siteVoList", iModel, 10) {
			@Override
			protected void populateItem(final ListItem<SiteVo> item) {
				
				SiteVo site = item.getModelObject();

				item.add(buildLink(site));
				
				if(site.getSiteDescription() != null){
					item.add(new Label("siteDescription", site.getSiteDescription()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("siteDescription", ""));//the ID here must match the ones in mark-up
				}

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				
			}
		};
		return sitePageableListView;
	}
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	private AjaxLink buildLink(final SiteVo site) {
		
		AjaxLink link = new AjaxLink("siteVo.siteName") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				SiteModel sitetudyModel  = cpm.getObject();
				sitetudyModel.setMode(Constants.MODE_EDIT);
				sitetudyModel.setSiteVo(site);//Sets the selected sitevo into the model
				containerForm.setModelObject(sitetudyModel);
				detailPanelContainer.setVisible(true);
				searchPanelContainer.setVisible(false);
		
				target.addComponent(detailPanelContainer);
				target.addComponent(searchPanelContainer);
			}
		};
		
		//Add the label for the link
		Label nameLinkLabel = new Label("siteNameLbl", site.getSiteName());
		link.add(nameLinkLabel);
		return link;

	}
	

}
