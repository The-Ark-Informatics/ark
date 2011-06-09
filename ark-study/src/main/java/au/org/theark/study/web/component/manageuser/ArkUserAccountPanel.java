package au.org.theark.study.web.component.manageuser;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.RoleVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.manageuser.form.ContainerForm;
/**
 * 
 * @author nivedan
 *
 */
@SuppressWarnings("serial")
public class ArkUserAccountPanel extends Panel{

	
	@SpringBean( name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private ContainerForm containerForm;
	
	/**
	 * Constructor
	 * @param id
	 * @param containerForm
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArkUserAccountPanel(String id,ContainerForm containerForm) {
		super(id);
		
		this.containerForm = containerForm;
		
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		//For the Given study get the linked Modules and the associated Roles for each Module as a List of ArkModuleVO 
		//Set this list into the ArkUserVO
		final Collection<ArkModuleVO> listArkModuleVO = iArkCommonService.getArkModulesLinkedToStudy(study);
		containerForm.getModelObject().setArkModuleVOList((List)listArkModuleVO);
		
		
		ListView  listView = new ListView("moduleRoleList",containerForm.getModelObject().getArkModuleVOList()) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem item) {
				
				
				//Each item will be ArkModuleVO use that to build the Module name and the drop down
				ArkModuleVO arkModuleVO = (ArkModuleVO)item.getModelObject();
				item.add(new Label("moduleName", arkModuleVO.getArkModule().getName()));
				Model model = new Model(arkModuleVO);
				PropertyModel<List<ArkRole>> rolespm = new PropertyModel<List<ArkRole>>(model,"arkModuleRoles");
				ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>(Constants.NAME, "id");
				DropDownChoice<ArkRole> ddc = new DropDownChoice<ArkRole>("arkRole",new Model(),rolespm,defaultChoiceRenderer);
				item.add(ddc);
			}
		};
		
		this.add(listView);
	}

}
