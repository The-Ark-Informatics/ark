package au.org.theark.report.model.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.org.theark.report.model.vo.report.ResearcherCostDataRow;

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
    public static Collection<ResearcherCostDataRow> getBeans() {
        List<ResearcherCostDataRow> costTypes = new  ArrayList<ResearcherCostDataRow>();

        ResearcherCostDataRow data1 = new  ResearcherCostDataRow("Mail out", 10,2.5);
        costTypes.add(data1);
        
        ResearcherCostDataRow data2 = new  ResearcherCostDataRow("post", 9,1.5);
        costTypes.add(data2);
        
        ResearcherCostDataRow data3 = new  ResearcherCostDataRow("call back", 8,0.5);
        costTypes.add(data3);

        return  costTypes;
    }
}
