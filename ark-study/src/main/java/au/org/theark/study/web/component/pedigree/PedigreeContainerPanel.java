package au.org.theark.study.web.component.pedigree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.model.capsule.ArkRelativeCapsule;
import au.org.theark.study.model.capsule.RelativeCapsule;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.model.vo.RelationshipVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;

import com.x5.template.Chunk;
import com.x5.template.Theme;

public class PedigreeContainerPanel extends AbstractContainerPanel<PedigreeVo>{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	//Markups
	
	private WebMarkupContainer	 					arkContextMarkup;
	protected WebMarkupContainer 				studyNameMarkup;
	protected WebMarkupContainer 				studyLogoMarkup;
	
	// Panels
	private SearchPanel								searchComponentPanel;
	private SearchResultListPanel					searchResultPanel;

	private PageableListView<RelationshipVo>	pageableListView;

	private ContainerForm							containerForm;

	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService studyService;
	

	public PedigreeContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PedigreeVo>(new PedigreeVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());

		containerForm.add(initialiseSearchResults());

		containerForm.add(initialiseSearchPanel());

		add(containerForm);

	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm, arkContextMarkup,  studyNameMarkup,  studyLogoMarkup );
		searchResultPanel.setOutputMarkupId(true);
		
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				String subjectUID= (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				containerForm.getModelObject().setRelationshipList(studyService.generateSubjectPedigreeRelativeList(subjectUID,studyId));
				pageableListView.removeAll();
				return containerForm.getModelObject().getRelationshipList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		
		DownloadLink madelineDownloadLink = new DownloadLink("pedLink", new AbstractReadOnlyModel<File>() {

			@Override
			public File getObject() {
				File tempFile = null;
				String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				RelativeCapsule[] relatives = studyService.generateSubjectPedigreeExportList(subjectUID, studyId);

				try {
					Theme theme = new Theme();
					Chunk chunk = theme.makeChunk("pedigree_template", "txt");
					chunk.set("relatives", relatives);

					String tmpDir = System.getProperty("java.io.tmpdir");
					String pedFileName=(subjectUID!=null?subjectUID:"NO_SUBJECT")+"-Madeline.ped";
					tempFile = new File(tmpDir,pedFileName);
					FileWriter out = new FileWriter(tempFile);
					chunk.render(out);
					out.flush();
					out.close();
				}
				catch (IOException io) {
					io.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				return tempFile;
			}

		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);
		
		madelineDownloadLink.add(new Behavior(){
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to Madeline PED");
			}
		});

		
		DownloadLink arkDownloadLink = new DownloadLink("arkLink", new AbstractReadOnlyModel<File>() {

			@Override
			public File getObject() {
				File tempFile = null;
				String subjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				ArkRelativeCapsule[] arkrelatives = studyService.generateSubjectArkPedigreeExportList(subjectUID, studyId);

				try {
					Theme theme = new Theme();
					Chunk chunk = theme.makeChunk("ark_pedigree_template", "txt");
					chunk.set("arkrelatives", arkrelatives);

					String tmpDir = System.getProperty("java.io.tmpdir");
					String pedFileName=(subjectUID!=null?subjectUID:"NO_SUBJECT")+"-Ark.ped";
					tempFile = new File(tmpDir,pedFileName);
					FileWriter out = new FileWriter(tempFile);
					chunk.render(out);
					out.flush();
					out.close();
				}
				catch (IOException io) {
					io.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				return tempFile;
			}

		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);
		
		arkDownloadLink.add(new Behavior(){
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				tag.put("title", "Export to Ark PED");
			}
		});

		
		searchResultPanel.add(madelineDownloadLink);
		searchResultPanel.add(arkDownloadLink);
		
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		searchComponentPanel = new SearchPanel("searchComponentPanel",arkContextMarkup,studyNameMarkup,studyLogoMarkup ,arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchComponentPanel.setOutputMarkupId(true);
		searchComponentPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchComponentPanel);
		return arkCrudContainerVO.getSearchPanelContainer();

	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
