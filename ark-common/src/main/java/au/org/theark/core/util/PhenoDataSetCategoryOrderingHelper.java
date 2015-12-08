package au.org.theark.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;

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
	public  List<PhenoDataSetCategory> orderHierarchicalyphenoDatasetCategories(List<PhenoDataSetCategory> currentCustomCategorylist){
		List<PhenoDataSetCategory> mainCtLst=new ArrayList<PhenoDataSetCategory>();
		List<PhenoDataSetCategory> parentLst=getSortedAllParentList(currentCustomCategorylist);
		for (PhenoDataSetCategory ctCat : parentLst) {
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
	private    List<PhenoDataSetCategory> getSortedAllParentList(List<PhenoDataSetCategory> lst){
		List<PhenoDataSetCategory> parentList=new ArrayList<PhenoDataSetCategory>();
		PhenoDataSetCategory parentCat;
		for (PhenoDataSetCategory ctCat : lst) {
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
	private   List<PhenoDataSetCategory> addMainLstFromParentCategory(PhenoDataSetCategory parentCat,List<PhenoDataSetCategory> allCatLst,List<PhenoDataSetCategory> mainCatLst ){

		parentCat.setDisplayLevel(level);
		mainCatLst.add(parentCat);
		List<PhenoDataSetCategory> sortedchildLst=getSortedFirstLevelChildLstFromParent(allCatLst, parentCat);
		for (PhenoDataSetCategory phenoDatasetCat : sortedchildLst) {
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
	private  List<PhenoDataSetCategory> getSortedFirstLevelChildLstFromParent(List<PhenoDataSetCategory> phenoDatasetCatList,PhenoDataSetCategory ctCatP){
		List<PhenoDataSetCategory> phenoDatasetChildCatList=new ArrayList<PhenoDataSetCategory>();
		for (PhenoDataSetCategory customeFieldCat : phenoDatasetCatList) {
			if (customeFieldCat.getParentCategory()!=null) {
				if (customeFieldCat.getParentCategory().equals(ctCatP)) {
					phenoDatasetChildCatList.add(customeFieldCat);
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
	private   List<PhenoDataSetCategory> sortLst(List<PhenoDataSetCategory> phenoDatasetLst){
		//sort by order number.
		Collections.sort(phenoDatasetLst, new Comparator<PhenoDataSetCategory>(){
		    public int compare(PhenoDataSetCategory custFieldCategory1, PhenoDataSetCategory custFieldCatCategory2) {
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
	private  boolean isExsistOnLst(List<PhenoDataSetCategory> categoryLst,PhenoDataSetCategory category){
		for (PhenoDataSetCategory ctCatCurrent : categoryLst) {
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
	public String preTextDecider(PhenoDataSetCategory phenoDatasetCat){
		if(phenoDatasetCat.getDisplayLevel().intValue()==1){
			return "";
		}else{
			return "⊢"+StringUtils.repeat("⊶", phenoDatasetCat.getDisplayLevel().intValue()-1);
		}
	}


}
