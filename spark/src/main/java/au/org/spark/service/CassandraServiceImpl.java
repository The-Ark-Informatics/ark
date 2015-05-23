package au.org.spark.service;

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

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

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
	
	public void createDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC){
		cassandraOperations.execute("CREATE TABLE IF NOT EXISTS TEST_TABLE (id int PRIMARY KEY,chromosome int,snp text,bp double,distance int)");
	}
	
	public void createDataSourceMapTable(DataSourceVo dataSource){
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataSourceTableName(dataSource));
		sb.append(" (id int PRIMARY KEY,chromosome int,snp text,bp double,distance int)");
		cassandraOperations.execute(sb.toString());
	}
	
	public void createDataSourcePedTable(DataSourceVo dataSource){
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataSourceTableName(dataSource));
		sb.append(" (uid text PRIMARY KEY, famId text,fatherId text,motherId text,sex text,pheno text,geno list<int>)" );
		cassandraOperations.execute(sb.toString());
	}
	
	public void dropDataSourceTable(DataSourceVo dataSource, DATA_CENTERS DC){
		StringBuffer sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataSourceTableName(dataSource));
		cassandraOperations.execute(sb.toString());
	}
	
	public void dropDataSourceTable(DataSourceVo dataSource){
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
	
	public void createPlinkDataCenterTables(DataCenterVo dataCenterVo){
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo)+"_SAMPLE_ENTRIES");
		sb.append(" (uid text PRIMARY KEY, famId text,fatherId text,motherId text,sex text,pheno text)" );
		cassandraOperations.execute(sb.toString());
		
		sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo)+"_VARIANT_ALLELES");
		sb.append(" (id int PRIMARY KEY, snp text, chromosome text, allele list<text>)" );
		cassandraOperations.execute(sb.toString());
	}
	
	public void dropPlinkDataCenterTables(DataCenterVo dataCenterVo){
		StringBuffer sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo)+"_SAMPLE_ENTRIES");
		cassandraOperations.execute(sb.toString());	
		
		sb = new StringBuffer("DROP TABLE ");
		sb.append(Constants.dataCenterTablePrefix(dataCenterVo)+"_VARIANT_ALLELES");
		cassandraOperations.execute(sb.toString());
	}

	public void insertPlinkSampleData(DataCenterVo dataCenterVo,Sample sample){
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo)+"_SAMPLE_ENTRIES");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("uid", sample.getId());
		insert.value("famId", sample.getFamilyId());
		insert.value("fatherId", sample.getFamilyId());		
		insert.value("motherId", sample.getMotherId());		
		insert.value("sex", sample.getSex().toString());		
		insert.value("pheno", sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME).toString());		
		cassandraOperations.execute(insert);
	}
	
	public void insertPlinkGenomicData(DataCenterVo dataCenterVo,int id,String snp, String chromosome, List<String> alleles){
		Insert insert = QueryBuilder.insertInto(Constants.dataCenterTablePrefix(dataCenterVo)+"_VARIANT_ALLELES");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", id);
		insert.value("snp", snp);
		insert.value("chromosome", chromosome);
		insert.value("allele", alleles);
		cassandraOperations.execute(insert);
	}

}
