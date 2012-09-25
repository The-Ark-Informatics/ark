package au.org.theark.report.model.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import au.org.theark.report.model.vo.report.ResearcherDetailCostDataRow;

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
    public static Collection<ResearcherDetailCostDataRow> getBeans() {
        List<ResearcherDetailCostDataRow> costTypes = new  ArrayList<ResearcherDetailCostDataRow>();

        ResearcherDetailCostDataRow data1 = new  ResearcherDetailCostDataRow("1:1", new Date(),"Y","GST Inclusive",100d,103.42,3.42,"Mailouts",1L,"Letters",true,"");
        costTypes.add(data1);
        
        ResearcherDetailCostDataRow data2 = new  ResearcherDetailCostDataRow("1:2", new Date(),"Y","GST Inclusive",400d,148.23,8.23,"Mailouts",1L,"Letters",true,"");
        costTypes.add(data2);
        
        ResearcherDetailCostDataRow data3 = new  ResearcherDetailCostDataRow("1:3", new Date(),"Y","GST Inclusive",400d,148.23,8.23,"Mailouts",1L,"Letters",true,"");
        costTypes.add(data3);

        ResearcherDetailCostDataRow data4 = new  ResearcherDetailCostDataRow("1:4", new Date(),"Y","GST Inclusive",400d,148.23,8.23,"Mailouts",1L,"Letters",true,"");
        costTypes.add(data4);
        
        ResearcherDetailCostDataRow data5 = new  ResearcherDetailCostDataRow("1:1", new Date(),"Y","GST Inclusive",5.5,303.80,3.80,"Phone Follow-Up Charges",2L,"Hours",true,"");
        costTypes.add(data5);
        
        ResearcherDetailCostDataRow data6 = new  ResearcherDetailCostDataRow("Project & Data Management Costs ", new Date(),"Y","GST Inclusive",12.6,592.92,2.92,"Administration Charges",3L,"Hours",true,"");
        costTypes.add(data6);
            
        return  costTypes;
    }
}
