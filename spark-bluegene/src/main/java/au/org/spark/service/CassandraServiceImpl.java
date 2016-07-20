package au.org.spark.service;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.List;

import org.molgenis.genotype.GenotypeData;
import org.molgenis.genotype.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RingMember;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import au.org.spark.util.Constants;
import au.org.spark.util.Constants.DATA_CENTERS;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;


@Service
public class CassandraServiceImpl implements CassandraService {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public String getRingMembers() {

		String result = "";
		List<RingMember> members = cassandraOperations.describeRing();

		for (RingMember mem : members) {
			result += mem.DC;
		}
		return result;
	}

	public void insert() {
		Insert insert = QueryBuilder.insertInto("geno_map");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", 1);
		insert.value("chromosome", 10);
		insert.value("snp", "Dd");
		insert.value("bp", 1.12);
		insert.value("distance", 2);
		cassandraOperations.execute(insert);
	}

	public void createDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC) {
		cassandraOperations.execute("CREATE TABLE IF NOT EXISTS TEST_TABLE (id int PRIMARY KEY,chromosome int,snp text,bp double,distance int)");
	}

	public void createDataSourceMapTable(DataSourceVo dataSource) {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataSourceTableName(dataSource));
		sb.append(" (id int PRIMARY KEY,chromosome int,snp text,bp double,distance int)");
		cassandraOperations.execute(sb.toString());
	}

	public void createDataSourcePedTable(DataSourceVo dataSource) {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataSourceTableName(dataSource));
		sb.append(" (uid text PRIMARY KEY, famId text,fatherId text,motherId text,sex text,pheno text,geno list<int>)");
		cassandraOperations.execute(sb.toString());
	}

	public void dropDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC) {
		StringBuffer sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataSourceTableName(dataSource));
		cassandraOperations.execute(sb.toString());
	}

	public void dropDataSourceTable(DataSourceVo dataSource) {
		StringBuffer sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataSourceTableName(dataSource));
		cassandraOperations.execute(sb.toString());
	}

	public void insertMap(String tableName, int id, int chormosome, String snp, double bp, int distance) {
		Insert insert = QueryBuilder.insertInto(tableName);
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("chromosome", chormosome);
		insert.value("snp", snp);
		insert.value("bp", bp);
		insert.value("distance", distance);
		cassandraOperations.execute(insert);
	}

	public void insertPed(String tableName, int id, int chormosome, String snp, double bp, int distance) {
		Insert insert = QueryBuilder.insertInto(tableName);
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("chromosome", chormosome);
		insert.value("snp", snp);
		insert.value("bp", bp);
		insert.value("distance", distance);
		cassandraOperations.execute(insert);
	}

	public void createPlinkDataCenterTables(DataCenterVo dataCenterVo) {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo) + "_SAMPLE_ENTRIES");
		sb.append(" (uid text PRIMARY KEY, famId text,fatherId text,motherId text,sex text,pheno text)");
		cassandraOperations.execute(sb.toString());

		sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo) + "_VARIANT_ALLELES");
		sb.append(" (id int PRIMARY KEY, snp text, chromosome text, allele list<text>)");
		cassandraOperations.execute(sb.toString());
	}

	public void dropPlinkDataCenterTables(DataCenterVo dataCenterVo) {
		StringBuffer sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo) + "_SAMPLE_ENTRIES");
		cassandraOperations.execute(sb.toString());

		sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo) + "_VARIANT_ALLELES");
		cassandraOperations.execute(sb.toString());
	}

	public void insertPlinkSampleData(DataCenterVo dataCenterVo, Sample sample) {
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo) + "_SAMPLE_ENTRIES");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("uid", sample.getId());
		insert.value("famId", sample.getFamilyId());
		insert.value("fatherId", sample.getFamilyId());
		insert.value("motherId", sample.getMotherId());
		insert.value("sex", sample.getSex().toString());
		insert.value("pheno", sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME).toString());
		cassandraOperations.execute(insert);
	}

	public void insertPlinkGenomicData(DataCenterVo dataCenterVo, int id, String snp, String chromosome, List<String> alleles) {
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo) + "_VARIANT_ALLELES");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("snp", snp);
		insert.value("chromosome", chromosome);
		insert.value("allele", alleles);
		cassandraOperations.execute(insert);
	}

	public void createPlinkBedTable(int[] data, DataCenterVo dataCenterVo) {

		String tableName = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_BED";

		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " ( id int PRIMARY KEY ");

		for (int i = 0; i < data.length;) {
			sb.append(" , h" + (++i) + " int");
		}

		sb.append(");");

		cassandraOperations.execute(sb.toString());

	}
	
	public void createPlinkFamTable(DataCenterVo dataCenterVo) {
		
		String tableName = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_FAM";

//		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " ( id int PRIMARY KEY, ");
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " ( id int, ");
		
		sb.append(" famId varchar, individualId varchar,fatherId varchar,motherId varchar,sex varchar,pheno varchar,");
		
//		sb.append(" PRIMARY KEY (id, individualId)");
		sb.append(" PRIMARY KEY (individualId)");

		sb.append(");");

		cassandraOperations.execute(sb.toString());
	}
	
	public void createPlinkBimTable(DataCenterVo dataCenterVo) {

		String tableName = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_BIM";

		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " ( id int PRIMARY KEY, ");

		sb.append(" chromosome int, snp varchar, cm int, base_position int, allele_1 varchar, allele_2 varchar ");

		sb.append(");");

		cassandraOperations.execute(sb.toString());
	}
	
	public void createPlinkIndividualResultTable(DataCenterVo dataCenterVo, int colCount) {

		String tableName = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_IND_SNP_RESULT";

		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " ( individualId varchar PRIMARY KEY, ");

		for (int i = 0; i < colCount;) {
			sb.append(" , r" + (++i) + " int");
		}

		sb.append(");");

		cassandraOperations.execute(sb.toString());
	}
 

	public void insertPlinkBedTable(int id, int[] data, DataCenterVo dataCenterVo) {

		String table = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_BED";
		StringBuffer sb = new StringBuffer("INSERT INTO " + table + " (id");

		StringBuffer sbh = new StringBuffer();
		StringBuffer sbv = new StringBuffer("VALUES (?");

		for (int i = 0; i < data.length;) {
			sbh.append(" , h" + (++i));
			sbv.append(" , ?");
		}
		sbh.append(") ");
		sbv.append("); ");

		sb.append(sbh.toString());
		sb.append(sbv.toString());

		PreparedStatement statement = cassandraOperations.getSession().prepare(sb.toString());

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.setInt(0, id);
		for (int i = 0, j = 0; i < data.length; ++i) {
			boundStatement.setInt((++j), data[i]);
		}
		cassandraOperations.execute(boundStatement);
	}
	
	
	
	public void insertPlinkFamTable(int id, String[] famArray, DataCenterVo dataCenterVo){
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_FAM");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("famId", famArray[0]);
		insert.value("individualId", famArray[1]);
		insert.value("fatherId", famArray[2]);
		insert.value("motherId", famArray[3]);
		insert.value("sex", famArray[4]);
		insert.value("pheno", famArray[5]);
		
		cassandraOperations.execute(insert);
	}
	
	public void insertPlinkBimTable(int id, String[] bedArray, DataCenterVo dataCenterVo){
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_BIM");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("chromosome", Integer.parseInt(bedArray[0]));
		insert.value("snp", bedArray[1]);
		insert.value("cm", Integer.parseInt(bedArray[2]));
		insert.value("base_position", Integer.parseInt(bedArray[3]));
		insert.value("allele_1", String.valueOf(bedArray[4].charAt(0)));
		insert.value("allele_2", String.valueOf(bedArray[5].charAt(0)));
		cassandraOperations.execute(insert);
	}
	
	public int queryResultFamilyTablePosition(DataCenterVo dataCenterVo){
		
		int id=0;
		
		String table= Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_FAM";
		
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("individualid", dataCenterVo.getIndividualId()));
		
		
		
		ResultSet results = cassandraOperations.getSession().execute(statement);

		for ( Row row : results ) { 	
			id = row.getInt("id");
		}
		return id;
	}
	
	public long tableSnpCount(DataCenterVo dataCenterVo){
		long count=0;
		String table= Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_BIM";
		
		Select select = QueryBuilder.select()
				.countAll()
		        .from(table);
		
		ResultSet rs=cassandraOperations.getSession().execute(select);
		
		count = rs.all().get(0).getLong(0);
		
		return count;
	}
	
	@Override
	public int getSnpNumber(DataCenterVo dataCenterVo,String column, int id) {
		int snp=0;
		String table= Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_BED";
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("id", id));
		
		
		ResultSet results = cassandraOperations.getSession().execute(statement);
		
		for ( Row row : results ) { 	
			snp =  row.getInt(column);
		}
		
		return snp;
	}
	
	public String[] getSnpAlleles(int snpId, DataCenterVo dataCenterVo){
		String alleles[] = new String[2];
		
		String table = Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_BIM";
		
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("id", snpId));
		ResultSet results = cassandraOperations.getSession().execute(statement);
		
		for ( Row row : results ) { 	
			alleles[0]=row.getString("allele_1");
			alleles[1]=row.getString("allele_2");
		}
		return alleles;
	}
	
	
	public void insertSnpIndividualResult(int[] data, DataCenterVo dataCenterVo){
		String table = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_IND_SNP_RESULT";
		StringBuffer sb = new StringBuffer("INSERT INTO " + table + " (individualid");

		StringBuffer sbh = new StringBuffer();
		StringBuffer sbv = new StringBuffer("VALUES (?");

		for (int i = 0; i < data.length;) {
			sbh.append(" , r" + (++i));
			sbv.append(" , ?");
		}
		sbh.append(") ");
		sbv.append("); ");

		sb.append(sbh.toString());
		sb.append(sbv.toString());

		PreparedStatement statement = cassandraOperations.getSession().prepare(sb.toString());

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.setString(0, dataCenterVo.getIndividualId());
		for (int i = 0, j = 0; i < data.length; ++i) {
			boundStatement.setInt((++j), data[i]);
		}
		cassandraOperations.execute(boundStatement);
	}
	
	public void dropIndividualResult(DataCenterVo dataCenterVo){
		String table = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_IND_SNP_RESULT";
		Statement statement = QueryBuilder.delete()
		        .all()
		        .from(table)
		        .where(eq("individualid", dataCenterVo.getIndividualId()));
		cassandraOperations.getSession().execute(statement);
	}
	
	public boolean isResultExists(DataCenterVo dataCenterVo){
		boolean exists=false;
		String table = Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_IND_SNP_RESULT";
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("individualid", dataCenterVo.getIndividualId()));
		ResultSet results = cassandraOperations.getSession().execute(statement);
		
		if(results.iterator().hasNext()){
			exists=true;
		}
		return exists;
	}
	
	public int[] getIndividualResult(DataCenterVo dataCenterVo, int colCount){
		int[] result=new int[colCount];
		String table = Constants.dataCenterTablePrefix(dataCenterVo) + "_PLINK_IND_SNP_RESULT";
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("individualid", dataCenterVo.getIndividualId()));
		ResultSet results = cassandraOperations.getSession().execute(statement);
		
		for ( Row row : results ) {
			for(int i=0,j = 0; j < colCount;++i){
				result[i] = row.getInt("r"+(++j));
			}		
		}
		
		return result;
		
	}
	
	public String[] getIndividualFamilyDetails(DataCenterVo dataCenterVo){
		String[] familyDetails=new String[6];
		
		String table= Constants.dataCenterTablePrefix(dataCenterVo)+ "_PLINK_FAM";
		
		Statement statement = QueryBuilder.select()
		        .all()
		        .from(table)
		        .where(eq("individualid", dataCenterVo.getIndividualId()));
		
		ResultSet results = cassandraOperations.getSession().execute(statement);
		
		for ( Row row : results ) { 	
			
			familyDetails[0]= row.getString("famId");
			familyDetails[1]= row.getString("individualId");
			familyDetails[2]= row.getString("fatherId");
			familyDetails[3]= row.getString("motherId");
			familyDetails[4]= row.getString("sex");
			familyDetails[5]= row.getString("pheno");
			
		}
		
		return familyDetails;
	}
	
	
}
