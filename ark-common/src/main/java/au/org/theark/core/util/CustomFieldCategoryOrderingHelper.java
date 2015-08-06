package au.org.theark.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.org.theark.core.model.study.entity.CustomFieldCategory;

public class CustomFieldCategoryOrderingHelper {
	
	private static Long level;
	
	private static CustomFieldCategoryOrderingHelper instance=new CustomFieldCategoryOrderingHelper();
	
	private CustomFieldCategoryOrderingHelper(){}
	
	public static CustomFieldCategoryOrderingHelper getInstance(){
		return instance;
	}
	
	/**
	 * make the catergory sub category order.
	 * 
	 * @param currentCustomCategorylist
	 * @return
	 */
	public  List<CustomFieldCategory> orderHierarchicalyCustomFieldCategories(List<CustomFieldCategory> currentCustomCategorylist){
		List<CustomFieldCategory> mainCtLst=new ArrayList<CustomFieldCategory>();
		List<CustomFieldCategory> parentLst=getSortedAllParentList(currentCustomCategorylist);
		for (CustomFieldCategory ctCat : parentLst) {
				level=1L;
				addMainLstFromParentCategory(ctCat, currentCustomCategorylist, mainCtLst);
		}
		return mainCtLst;

		
	}
	/**
	 * Get sorted parent list.
	 * @param lst
	 * @return
	 */
	private    List<CustomFieldCategory> getSortedAllParentList(List<CustomFieldCategory> lst){
		List<CustomFieldCategory> parentList=new ArrayList<CustomFieldCategory>();
		CustomFieldCategory parentCat;
		for (CustomFieldCategory ctCat : lst) {
			parentCat=ctCat.getParentCategory();
			if(parentCat==null){
				parentList.add(ctCat);
			}else{
				if(!isExsistOnLst(lst, parentCat)){
					parentList.add(ctCat);
				}
			}
		}
		return sortLst(parentList);
	}
	/**
	 * Add to main list only selected from parent categoty from all category. 
	 * 
	 * @param parentCat
	 * @param allCat
	 * @param mainCat
	 * @return
	 */
	private   List<CustomFieldCategory> addMainLstFromParentCategory(CustomFieldCategory parentCat,List<CustomFieldCategory> allCatLst,List<CustomFieldCategory> mainCatLst ){

		parentCat.setDisplayLevel(level);
		mainCatLst.add(parentCat);
		List<CustomFieldCategory> sortedchildLst=getSortedFirstLevelChildLstFromParent(allCatLst, parentCat);
		for (CustomFieldCategory customFieldCat : sortedchildLst) {
			level++;
			addMainLstFromParentCategory(customFieldCat, allCatLst, mainCatLst);
			level--;
		}
		return mainCatLst;
	}
	/**
	 *  Get sorted first level child list from parent.
	 * @param ctLst
	 * @param ctCatP
	 * @return
	 */
	private  List<CustomFieldCategory> getSortedFirstLevelChildLstFromParent(List<CustomFieldCategory> customFieldCatList,CustomFieldCategory ctCatP){
		List<CustomFieldCategory> customFieldChildCatList=new ArrayList<CustomFieldCategory>();
		for (CustomFieldCategory customeFieldCat : customFieldCatList) {
			if (customeFieldCat.getParentCategory()!=null) {
				if (customeFieldCat.getParentCategory().equals(ctCatP)) {
					customFieldChildCatList.add(customeFieldCat);
				}
			}
		}
		return sortLst(customFieldChildCatList);
	}
	/**
	 * Sort custom field list according to the order number.
	 * @param customFieldLst
	 * @return
	 */
	private   List<CustomFieldCategory> sortLst(List<CustomFieldCategory> customFieldLst){
		//sort by order number.
		Collections.sort(customFieldLst, new Comparator<CustomFieldCategory>(){
		    public int compare(CustomFieldCategory custFieldCategory1, CustomFieldCategory custFieldCatCategory2) {
		        return custFieldCategory1.getOrderNumber().compareTo(custFieldCatCategory2.getOrderNumber());
		    }
		});
				return customFieldLst;
	}
	/**
	 * Check for the exsistancy in the list particular category.
	 * @param catLst
	 * @param ctCat
	 * @return
	 */
	private  boolean isExsistOnLst(List<CustomFieldCategory> categoryLst,CustomFieldCategory category){
		for (CustomFieldCategory ctCatCurrent : categoryLst) {
			if (ctCatCurrent.equals(category)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param customFieldCat
	 * @return
	 */
	public String preTextDecider(CustomFieldCategory customFieldCat){
		if(customFieldCat.getDisplayLevel().intValue()==1){
			return "";
		}else{
			return "⊢"+StringUtils.repeat("⊶", customFieldCat.getDisplayLevel().intValue()-1);
		}
	}


}
