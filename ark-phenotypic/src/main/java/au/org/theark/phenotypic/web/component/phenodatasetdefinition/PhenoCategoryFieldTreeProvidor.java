package au.org.theark.phenotypic.web.component.phenodatasetdefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.LinkPhenoDataSetCategoryField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhenoDataSetFieldGroupVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import wickettree.ITreeProvider;

public class PhenoCategoryFieldTreeProvidor implements ITreeProvider<Object> {

	private static final long	serialVersionUID	= 1L;
	
	private static List<PickedPhenoDataSetCategory> roots=new ArrayList<PickedPhenoDataSetCategory>() ;
	
	private IArkCommonService				iArkCommonService;
	private IPhenotypicService				iPhenotypicService;
	
	private CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpModel;
	private Study study;
	private ArkFunction arkFunction;
	private ArkUser arkUser;
	
	public PhenoCategoryFieldTreeProvidor(IArkCommonService iArkCommonService, IPhenotypicService iPhenotypicService, CompoundPropertyModel<PhenoDataSetFieldGroupVO> cpModel)
	{
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService = iPhenotypicService;
		this.cpModel = cpModel;
		roots.clear();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		try {
			arkUser=iArkCommonService.getArkUser((String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_USERID));
		} catch (InvalidSessionException | EntityNotFoundException e) {
			e.printStackTrace();
		}
		arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_PHENO_COLLECTION);
		roots.addAll(iPhenotypicService.getAllParentPickedPhenoDataSetCategories(study, arkFunction, arkUser));
	}

	public Iterator getChildren(Object obj) {
		List<Object> categoryAndField =new ArrayList<Object>();
		if(obj instanceof PickedPhenoDataSetCategory){
			categoryAndField.addAll(iPhenotypicService.
					getLinkPhenoDataSetCategoryFieldsForPickedPhenoDataSetCategory((PickedPhenoDataSetCategory)obj));
			categoryAndField.addAll(iPhenotypicService.
					getChildrenOfPickedPhenoDataSetCategory((PickedPhenoDataSetCategory)obj));
			return categoryAndField.iterator();
		}else if(obj instanceof LinkPhenoDataSetCategoryField){
			return new Iterator() {	
				@Override
				public boolean hasNext() {
					return false;
				}
				@Override
				public Object next() {
					return null;
				}
			};
		}
		return null;
	}

	public Iterator getRoots() {
		return roots.iterator();
	}

	public boolean hasChildren(Object obj) {
		if(obj instanceof PickedPhenoDataSetCategory){
			PickedPhenoDataSetCategory pickedPhenoDataSetCategory=(PickedPhenoDataSetCategory)obj;
			List<PhenoDataSetCategory> phenoDataSetCategories=new ArrayList<PhenoDataSetCategory>();
			phenoDataSetCategories.add(pickedPhenoDataSetCategory.getPhenoDataSetCategory());
			if(iPhenotypicService.isPickedPhenoDataSetCategoryIsAParentOfAnotherCategory(pickedPhenoDataSetCategory)
					|| iPhenotypicService.isSelectedCategoriesAlreadyAssignedToFields(study, arkFunction, arkUser, phenoDataSetCategories)){
				return true;
			}else{
				return false;
			}
		}else if(obj instanceof LinkPhenoDataSetCategoryField){
			return false;
		}
		return false;
		
	}

	public IModel model(Object obj) {
		if(obj instanceof PickedPhenoDataSetCategory) {
			return new Model((PickedPhenoDataSetCategory) obj);
		}else if(obj instanceof LinkPhenoDataSetCategoryField) {
			return new Model((LinkPhenoDataSetCategoryField) obj);
		}
		return null;
		
	}

	public void detach() {
		
	}

}
