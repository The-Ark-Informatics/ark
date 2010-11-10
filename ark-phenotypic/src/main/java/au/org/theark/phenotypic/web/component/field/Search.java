/**
 * 
 */
package au.org.theark.phenotypic.web.component.field;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.field.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class Search extends Panel
{
	private FeedbackPanel				feedBackPanel;
	private WebMarkupContainer			searchMarkupContainer;
	private WebMarkupContainer			listContainer;
	private WebMarkupContainer			detailContainer;
	private PageableListView<Field>	listView;
	private ContainerForm				containerForm;
	private Detail							detail;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService			iArkCommonService;

	/* Constructor */
	public Search(	String id, 
						FeedbackPanel feedBackPanel, 
						WebMarkupContainer searchMarkupContainer, 
						PageableListView<Field> listView, 
						WebMarkupContainer resultListContainer,
						WebMarkupContainer detailPanelContainer, 
						Detail detail, 
						ContainerForm containerForm)
	{
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.detailContainer = detailPanelContainer;
		this.detail = detail;
		this.containerForm = containerForm;
		listContainer = resultListContainer;
	}

	public void processDetail(AjaxRequestTarget target)
	{
		searchMarkupContainer.setVisible(false);
		detail.getDetailForm().getFieldIdTxtFld().setEnabled(false);
		detailContainer.setVisible(true);
		listContainer.setVisible(false);
		target.addComponent(detailContainer);
		target.addComponent(searchMarkupContainer);
		target.addComponent(listContainer);
	}

	public void initialisePanel()
	{
		// Get the study id from the session and get the study
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, (CompoundPropertyModel<FieldVO>) containerForm.getModel())
		{
			protected void onSearch(AjaxRequestTarget target)
			{
				// Refresh the FB panel if there was an old message from previous search result
				target.addComponent(feedBackPanel);

				// Get a list of all Fields for the Study in context
				Study study = iArkCommonService.getStudy(sessionStudyId);
				Field searchField = containerForm.getModelObject().getField();
				searchField.setStudy(study);

				java.util.Collection<Field> fieldCollection = phenotypicService.searchField(searchField);

				if (fieldCollection != null && fieldCollection.size() == 0)
				{
					this.info("Fields with the specified criteria does not exist in the system.");
					target.addComponent(feedBackPanel);
				}
				containerForm.getModelObject().setFieldCollection(fieldCollection);
				listView.removeAll();
				listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
				target.addComponent(listContainer);// For ajax this is required so
			}

			protected void onNew(AjaxRequestTarget target)
			{
				// Show the details panel name and description
				FieldVO fieldVo = new FieldVO();
				fieldVo.setMode(au.org.theark.core.Constants.MODE_NEW);
				
				// Set study for the new field
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study	study = iArkCommonService.getStudy(studyId);
				fieldVo.getField().setStudy(study);
				
				containerForm.setModelObject(fieldVo);
				processDetail(target);
			}
		};
		add(searchForm);
	}
}