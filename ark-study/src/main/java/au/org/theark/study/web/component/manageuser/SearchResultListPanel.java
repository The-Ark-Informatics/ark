package au.org.theark.study.web.component.manageuser;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;

public class SearchResultListPanel extends Panel{

	private ArkCrudContainerVO arkCrudContainerVO;
	private ContainerForm containerForm;
	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	/**
	 * Constructor
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}
	
	public PageableListView<ArkUserVO> buildPageableListView(IModel iModel,  final WebMarkupContainer searchResultsContainer){
		//This has to be populated earlier
		//final List<ArkUsecase> arkUsecaseList = (List)iArkCommonService.getEntityList(ArkUsecase.class);
		PageableListView<ArkUserVO>  pageableListView = new PageableListView<ArkUserVO>("userList",  iModel, au.org.theark.core.Constants.ROWS_PER_PAGE)
		{
			@Override
			protected void populateItem(final ListItem<ArkUserVO> item) {
				
				ArkUserVO arkUserVO =  item.getModelObject();
				//arkUserVO.setArkUsecaseList(arkUsecaseList);
				//ChoiceRenderer<ArkUsecase> defaultChoiceRenderer = new ChoiceRenderer<ArkUsecase>(Constants.NAME, "id");
				//DropDownChoice<ArkUsecase> ddc = new DropDownChoice<ArkUsecase>("arkUsecase",arkUsecaseList,defaultChoiceRenderer);	
				item.add(buildLink(arkUserVO, searchResultsContainer));
				item.add(new Label("lastName", arkUserVO.getLastName()));//the ID here must match the ones in mark-up
				item.add(new Label("firstName", arkUserVO.getFirstName()));
				//item.add(ddc);
			}
		};
		
		return pageableListView;
	}
	
	private AjaxLink buildLink(final ArkUserVO arkUserVo, final WebMarkupContainer searchResultsContainer){

		AjaxLink link = new AjaxLink("userName") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					//TODO
				}
		};
		//Add the label for the link
		Label userNameLinkLabel = new Label("userNameLink", arkUserVo.getUserName());
		link.add(userNameLinkLabel);
		return link;
	}
}
