package au.org.spark.service;

import java.util.List;

import org.molgenis.genotype.Sample;

import au.org.spark.util.Constants.DATA_CENTERS;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

public interface CassandraService {
	
	public String getRingMembers();
	
	public void insert();
	
	public void createDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC);
	
	public void dropDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC);
	
	public void createDataSourceMapTable(DataSourceVo dataSource);
	
	public void createDataSourcePedTable(DataSourceVo dataSource);
	
	public void dropDataSourceTable(DataSourceVo dataSource);
	
	public void insertMap(String tableName, int id, int chormosome, String snp, double bp, int distance);
	
	public void createPlinkDataCenterTables(DataCenterVo dataCenterVo);
	
	public void insertPlinkSampleData(DataCenterVo dataCenterVo,Sample sample);
	
	public void insertPlinkGenomicData(DataCenterVo dataCenterVo,int id,String snp, String chromosome, List<String> alleles);
	
	public void dropPlinkDataCenterTables(DataCenterVo dataCenterVo);
	
	public void createPlinkBedTable(int[] data, DataCenterVo dataCenterVo );
	
	public void createPlinkFamTable(DataCenterVo dataCenterVo);
	
	public void createPlinkBimTable(DataCenterVo dataCenterVo);
	
	public void createPlinkIndividualResultTable(DataCenterVo dataCenterVo, int colCount);
	
	public void insertPlinkBedTable(int snpId,int[] data,DataCenterVo dataCenterVo);
	
	public void insertPlinkFamTable(int id, String[] famArray, DataCenterVo dataCenterVo);

	public void insertPlinkBimTable(int id, String[] bimArray, DataCenterVo dataCenterVo);
	
	public int queryResultFamilyTablePosition(DataCenterVo dataCenterVo);
	
	public long tableSnpCount(DataCenterVo dataCenterVo);
	
	public int getSnpNumber(DataCenterVo dataCenterVo,String column, int id);
	
	public String[] getSnpAlleles(int snpId, DataCenterVo dataCenterVo);
	
	public void insertSnpIndividualResult(int[] data, DataCenterVo dataCenterVo);
	
	public boolean isResultExists(DataCenterVo dataCenterVo);
	
	public void dropIndividualResult(DataCenterVo dataCenterVo);
	
	public int[] getIndividualResult(DataCenterVo dataCenterVo, int colCount);
	
	public String[] getIndividualFamilyDetails(DataCenterVo dataCenterVo);

}
