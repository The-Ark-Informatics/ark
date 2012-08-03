package au.org.theark.worktracking.web.component.workrequest;

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

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.worktracking.model.vo.WorkRequestVo;
import au.org.theark.worktracking.util.Constants;
import au.org.theark.worktracking.web.component.workrequest.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm workRequestContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = workRequestContainerForm;
	}

	public PageableListView<WorkRequest> buildPageableListView(IModel iModel) {

		PageableListView<WorkRequest> sitePageableListView = new PageableListView<WorkRequest>("workRequestList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<WorkRequest> item) {

				WorkRequest workRequest = item.getModelObject();

				if (workRequest.getId() != null) {
					item.add(new Label(Constants.WORK_REQUEST_ID, workRequest.getId().toString()));
				}
				else {
					item.add(new Label(Constants.WORK_REQUEST_ID, ""));
				}

				item.add(buildLink(workRequest));				
				if (workRequest.getRequestStatus() != null) {
					item.add(new Label(Constants.WORK_REQUEST_REQUEST_STATUS, workRequest.getRequestStatus().getName()));
				}
				else {
					item.add(new Label(Constants.WORK_REQUEST_REQUEST_STATUS, ""));
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				
				String requestedDate = "";
				if (workRequest.getRequestedDate() != null) {
					requestedDate= simpleDateFormat.format(workRequest.getRequestedDate());
					item.add(new Label(Constants.WORK_REQUEST_REQUESTED_DATE, requestedDate));
				}
				else {
					item.add(new Label(Constants.WORK_REQUEST_REQUESTED_DATE, requestedDate));
				}
				
				String commencedDate="";
				if (workRequest.getCommencedDate() != null) {
					commencedDate =  simpleDateFormat.format(workRequest.getCommencedDate());
					item.add(new Label(Constants.WORK_REQUEST_COMMENCED_DATE, commencedDate));
				}
				else {
					item.add(new Label(Constants.WORK_REQUEST_COMMENCED_DATE, commencedDate));
				}

				String completedDate="";
				if (workRequest.getCompletedDate() != null) {
					completedDate=simpleDateFormat.format(workRequest.getCompletedDate());
					item.add(new Label(Constants.WORK_REQUEST_COMPLETED_DATE, completedDate));
				}
				else {
					item.add(new Label(Constants.WORK_REQUEST_COMPLETED_DATE, completedDate));
				}
				
				if(workRequest.getResearcher() != null){
					Researcher researcher = workRequest.getResearcher();
					item.add(new Label(Constants.WORK_REQUEST_RESEARCHER, researcher.getFirstName()+" "+researcher.getLastName()));
				}
				else{
					item.add(new Label(Constants.WORK_REQUEST_RESEARCHER,""));
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

	@SuppressWarnings( { "serial" })
	private AjaxLink buildLink(final WorkRequest workRequest) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.WORK_REQUEST_ITEM_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {

				WorkRequestVo workRequestVo = containerForm.getModelObject();
				workRequestVo.setMode(Constants.MODE_EDIT);
				workRequestVo.setWorkRequest(workRequest);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		Label nameLinkLabel = new Label("nameLbl", workRequest.getName());
		link.add(nameLinkLabel);
		return link;

	}

}