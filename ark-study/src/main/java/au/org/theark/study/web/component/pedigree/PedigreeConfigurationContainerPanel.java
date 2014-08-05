package au.org.theark.study.web.component.pedigree;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.pedigree.form.ContainerForm;

public class PedigreeConfigurationContainerPanel extends AbstractContainerPanel<PedigreeVo> {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;

	protected WebMarkupContainer					savePanelContainer;
	
	protected WebMarkupContainer					configPanelContainer;

	protected AbstractDetailModalWindow			modalWindow;

	private SavePanel									savePanel;
	
	private PedigreeConfigurationPanel configPanel;

	private ContainerForm							containerForm;

	@SpringBean(name = Constants.STUDY_SERVICE)
	IStudyService										studyService;

	public PedigreeConfigurationContainerPanel(String id,AbstractDetailModalWindow modalWindow) {
		super(id);
		this.modalWindow = modalWindow;
		
		cpModel = new CompoundPropertyModel<PedigreeVo>(new PedigreeVo());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());
				
		containerForm.add(initialiseConfigPanel());

		add(containerForm);

	}
	
	protected WebMarkupContainer initialiseConfigPanel() {
		configPanelContainer = new WebMarkupContainer("configurationContainer");
		configPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		configPanel = new PedigreeConfigurationPanel("configurationPanel",feedBackPanel,arkCrudContainerVO, modalWindow);
		configPanel.setOutputMarkupId(true);
		configPanel.initialisePanel(cpModel);
		configPanelContainer.add(configPanel);
		return configPanelContainer;
	}
	
	protected WebMarkupContainer initialiseSavePanel() {
		savePanelContainer = new WebMarkupContainer("saveContainer");
		savePanelContainer.setOutputMarkupPlaceholderTag(true);
		
		savePanel = new SavePanel("saveComponentPanel",feedBackPanel,arkCrudContainerVO, modalWindow,true);
		savePanel.setOutputMarkupId(true);
		savePanel.initialisePanel(cpModel);
		savePanelContainer.add(savePanel);
		
		return savePanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void loadData(){
		
	}

}
