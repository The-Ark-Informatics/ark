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
import org.apache.wicket.model.IModel;

import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.site.form.ContainerForm;


@SuppressWarnings("serial")
public class SearchResultList extends Panel{
	
	
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer resultPanelContainer;
	private ContainerForm containerForm;
	
	public SearchResultList(String id, WebMarkupContainer  detailPanelContainer, WebMarkupContainer searchPanelContainer, WebMarkupContainer resultPanelContainer, ContainerForm siteContainerForm){
		super(id);
		this.detailPanelContainer = detailPanelContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.containerForm = siteContainerForm;
		this.resultPanelContainer = resultPanelContainer;
	}

	public PageableListView<SiteVO> buildPageableListView(IModel iModel){
		
		PageableListView<SiteVO> sitePageableListView = new PageableListView<SiteVO>("siteVoList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<SiteVO> item) {
				
				SiteVO site = item.getModelObject();

				item.add(buildLink(site));
				
				if(site.getSiteDescription() != null){
					item.add(new Label("siteDescription", site.getSiteDescription()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("siteDescription", ""));//the ID here must match the ones in mark-up
				}
				
				/*
				 * TODO: Implement getSiteContact()
				if(site.getSiteContactPerson() != null){
					item.add(new Label("siteContact", site.getSiteContact()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("siteContact", ""));//the ID here must match the ones in mark-up
				}
				*/
				
				/*
				 * TODO: Implement getSiteAddress()
				if(site.getSiteAddress() != null){
					item.add(new Label("siteAddress", site.getSiteAddress()));//the ID here must match the ones in mark-up	
				}else{
					item.add(new Label("siteAddress", ""));//the ID here must match the ones in mark-up
				}
				*/

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
	private AjaxLink buildLink(final SiteVO site) {
		
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("siteVo.siteName") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				containerForm.getModelObject().setMode(Constants.MODE_EDIT);
				containerForm.getModelObject().setSiteVo(site);
				detailPanelContainer.setVisible(true);
				searchPanelContainer.setVisible(false);
				resultPanelContainer.setVisible(false);
		
				target.addComponent(detailPanelContainer);
				target.addComponent(searchPanelContainer);
				target.addComponent(resultPanelContainer);
			}
		};
		
		//Add the label for the link
		Label nameLinkLabel = new Label("siteNameLbl", site.getSiteName());
		link.add(nameLinkLabel);
		return link;
	}
}