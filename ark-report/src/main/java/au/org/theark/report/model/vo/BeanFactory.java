package au.org.theark.report.model.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.org.theark.report.model.vo.report.BiospecimenSummaryDataRow;

/**
 * 
 * This class only used in the iReport design tool. 
 * <p>
 * Can change the {@link BeanFactory#getBeans()} method to required data type.
 *
 */
public class BeanFactory {
	
	public  BeanFactory() {
    }
	
	/**
	 * Sample data collection population method.
	 * @return data beans collection.
	 */
    public static Collection<BiospecimenSummaryDataRow> getBeans() {
        List<BiospecimenSummaryDataRow> specimens = new  ArrayList<BiospecimenSummaryDataRow>();

        BiospecimenSummaryDataRow data1 = new  BiospecimenSummaryDataRow("test1","0001",1111111L,"A00001","Blood/plasma",18.5d,"Initial Quantity");
        specimens.add(data1);
        
        BiospecimenSummaryDataRow data2 = new  BiospecimenSummaryDataRow("test2","0002",1111112L,"A00002","Blood/plasma",18.6d,"Initial Quantity");
        specimens.add(data2);
        
        BiospecimenSummaryDataRow data3 = new  BiospecimenSummaryDataRow("test3","0003",1111113L,"A00003","Blood/plasma",18.7d,"Initial Quantity");
        specimens.add(data3);

        BiospecimenSummaryDataRow data4 = new  BiospecimenSummaryDataRow("test4","0004",1111114L,"A00004","Blood/plasma",18.8d,"Initial Quantity");
        specimens.add(data4);
        
        BiospecimenSummaryDataRow data5 = new  BiospecimenSummaryDataRow("test5","0005",1111115L,"A00005","Blood/plasma",18.9d,"Initial Quantity");
        specimens.add(data5);
        
        BiospecimenSummaryDataRow data6 = new  BiospecimenSummaryDataRow("test6","0006",1111116L,"A00006","Blood/plasma",19.0d,"Initial Quantity");
        specimens.add(data6);
            
        return  specimens;
    }
}
