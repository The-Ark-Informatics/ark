package au.org.spark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RingMember;
import org.springframework.stereotype.Service;
import org.springframework.data.cassandra.core.CassandraOperations;

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
		// TODO Auto-generated method stub
		List<RingMember> members = cassandraOperations.describeRing();

		for (RingMember mem : members) {
			result += mem.DC;
		}
		return result;
	}

	public void insert() {	
		//CREATE TABLE IF NOT EXISTS geno_map (id int PRIMARY KEY,chromosome int,snp text,bp double,distance int);
		Insert insert = QueryBuilder.insertInto("geno_map");
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		insert.value("id", 1);
		insert.value("chromosome", 10);
		insert.value("snp", "Dd");
		insert.value("bp", 1.12);
		insert.value("distance", 2);
		cassandraOperations.execute(insert);

	}

}
