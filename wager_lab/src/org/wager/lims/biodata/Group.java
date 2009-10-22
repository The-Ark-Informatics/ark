package org.wager.lims.biodata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "IX_BIODATA_GROUP")
public class Group implements Serializable {
	
@Id
@Column(name = "GROUPKEY")
private BigDecimal groupKey;
@Column(name = "GROUP_NAME")
private String	groupName;

@ManyToMany
@JoinTable(
name = "IX_BIODATA_FIELD_GROUP",
joinColumns = {@JoinColumn(name = "GROUPKEY")},
inverseJoinColumns = {@JoinColumn(name = "FIELDKEY")}
)
@org.hibernate.annotations.IndexColumn(name = "POSITION")
private List<Field> fields = new ArrayList<Field>();

public void setGroupName(String groupName) {
	this.groupName = groupName;
}
public String getGroupName() {
	return groupName;
}

public void setGroupKey(BigDecimal groupKey) {
	this.groupKey = groupKey;
}

 
public BigDecimal getGroupKey() {
	return groupKey;
}

public List<Field> getFields() {
	return fields;
}


	

}
