package au.org.theark.phenotypic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.org.theark.core.model.pheno.entity.PickedPhenoDataSetCategory;

public class PhenoDataSetCategoryOrderingHelper {
	
	private static Long level;
	
	private static PhenoDataSetCategoryOrderingHelper instance=new PhenoDataSetCategoryOrderingHelper();
	
	private PhenoDataSetCategoryOrderingHelper(){}
	
	public static PhenoDataSetCategoryOrderingHelper getInstance(){
		return instance;
	}
	
	/**
	 * make the catergory sub category order.
	 * 
	 * @param currentCustomCategorylist
	 * @return
	 */
	public  List<PickedPhenoDataSetCategory> orderHierarchicalyphenoDatasetCategories(List<PickedPhenoDataSetCategory> currentCustomCategorylist){
		List<PickedPhenoDataSetCategory> mainCtLst=new ArrayList<PickedPhenoDataSetCategory>();
		List<PickedPhenoDataSetCategory> parentLst=getSortedAllParentList(currentCustomCategorylist);
		for (PickedPhenoDataSetCategory ctCat : parentLst) {
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
	private    List<PickedPhenoDataSetCategory> getSortedAllParentList(List<PickedPhenoDataSetCategory> lst){
		List<PickedPhenoDataSetCategory> parentList=new ArrayList<PickedPhenoDataSetCategory>();
		PickedPhenoDataSetCategory parentCat;
		for (PickedPhenoDataSetCategory ctCat : lst) {
			parentCat=ctCat.getParentPickedPhenoDataSetCategory();
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
	private   List<PickedPhenoDataSetCategory> addMainLstFromParentCategory(PickedPhenoDataSetCategory parentCat,List<PickedPhenoDataSetCategory> allCatLst,List<PickedPhenoDataSetCategory> mainCatLst ){

		parentCat.setDisplayLevel(level);
		mainCatLst.add(parentCat);
		List<PickedPhenoDataSetCategory> sortedchildLst=getSortedFirstLevelChildLstFromParent(allCatLst, parentCat);
		for (PickedPhenoDataSetCategory phenoDatasetCat : sortedchildLst) {
			level++;
			addMainLstFromParentCategory(phenoDatasetCat, allCatLst, mainCatLst);
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
	private  List<PickedPhenoDataSetCategory> getSortedFirstLevelChildLstFromParent(List<PickedPhenoDataSetCategory> phenoDatasetCatList,PickedPhenoDataSetCategory ctCatP){
		List<PickedPhenoDataSetCategory> phenoDatasetChildCatList=new ArrayList<PickedPhenoDataSetCategory>();
		for (PickedPhenoDataSetCategory pickedPhenoDataSetCategory : phenoDatasetCatList) {
			if (pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory()!=null) {
				if (pickedPhenoDataSetCategory.getParentPickedPhenoDataSetCategory().equals(ctCatP)) {
					phenoDatasetChildCatList.add(pickedPhenoDataSetCategory);
				}
			}
		}
		return sortLst(phenoDatasetChildCatList);
	}
	/**
	 * Sort custom field list according to the order number.
	 * @param phenoDatasetLst
	 * @return
	 */
	private   List<PickedPhenoDataSetCategory> sortLst(List<PickedPhenoDataSetCategory> phenoDatasetLst){
		//sort by order number.
		Collections.sort(phenoDatasetLst, new Comparator<PickedPhenoDataSetCategory>(){
		    public int compare(PickedPhenoDataSetCategory custFieldCategory1, PickedPhenoDataSetCategory custFieldCatCategory2) {
		       return custFieldCategory1.getOrderNumber().compareTo(custFieldCatCategory2.getOrderNumber());
		    }
		});
				return phenoDatasetLst;
	}
	/**
	 * Check for the existence in the list particular category.
	 * @param catLst
	 * @param ctCat
	 * @return
	 */
	private  boolean isExsistOnLst(List<PickedPhenoDataSetCategory> categoryLst,PickedPhenoDataSetCategory category){
		for (PickedPhenoDataSetCategory ctCatCurrent : categoryLst) {
			if (ctCatCurrent.equals(category)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param phenoDatasetCat
	 * @return
	 */
	public String preTextDecider(PickedPhenoDataSetCategory phenoDatasetCat){
		
		if(phenoDatasetCat!=null && phenoDatasetCat.getDisplayLevel().intValue()==1){
			return "";
		}else{
			return "⊢"+StringUtils.repeat("⊶", phenoDatasetCat.getDisplayLevel().intValue()-1)+" ";
			//return Character.toString((char)315)+StringUtils.repeat(Character.toString((char)126), phenoDatasetCat.getDisplayLevel().intValue()-1);
			
		}
	}


}
