/**
 * 
 */
package au.org.theark.phenotypic.web.component.summaryModule;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.phenotypic.web.component.summaryModule.form.ContainerForm;
import au.org.theark.phenotypic.web.component.summaryModule.form.SummaryForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {
	private FeedbackPanel							feedBackPanel;
	private WebMarkupContainer						searchMarkupContainer;
	private WebMarkupContainer						resultListContainer;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<PhenoCollection> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer, DetailPanel detail, ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer) {
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.resultListContainer = resultListContainer;
	}

	public void initialisePanel() {
		SummaryForm searchForm = new SummaryForm("summaryForm", feedBackPanel, searchMarkupContainer, resultListContainer);
		add(searchForm);
	}
}