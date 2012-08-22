package au.org.theark.lims.web.component.inventory.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;

public class InventoryContainerPanel extends AbstractInventoryPanel<LimsVO> {
	private static final long			serialVersionUID	= -8575670114976786294L;
	private static Logger				log	= LoggerFactory.getLogger(InventoryContainerPanel.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	
	private ContainerForm				containerForm;
	private InventoryTreePanel			treePanel;
	private EmptyPanel					detailPanel;

	public InventoryContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		setUserStudyList();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedbackPanel());
		containerForm.add(initialiseDetailContainer());
		
		/* Tree not in the containrForm (avoid lag of ArkFormVisitor) */ 
		add(initialiseTreeContainer());
		//TODO: This was added to overcome Wicket 1.5.0's strange multipart bug...shouldn't really be needed!
		containerForm.setMultiPart(true);
		add(containerForm);
	}
	
	private void setUserStudyList() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
			cpModel.getObject().setStudyList(studyListForUser);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Initialise the tree
	 * @return
	 */
	protected WebMarkupContainer initialiseTreeContainer() {
		treePanel = new InventoryTreePanel("treePanel", feedbackPanel, detailContainer, containerForm);
		treeContainer.addOrReplace(treePanel);
		return treeContainer;
	}

	/**
	 * Initialise empty Panel for placeholder
	 * @return
	 */
	protected WebMarkupContainer initialiseDetailContainer() {
		detailPanel = new EmptyPanel("detailPanel");
		detailPanel.setOutputMarkupPlaceholderTag(true);
		detailContainer.addOrReplace(detailPanel);
		return detailContainer;
	}
}