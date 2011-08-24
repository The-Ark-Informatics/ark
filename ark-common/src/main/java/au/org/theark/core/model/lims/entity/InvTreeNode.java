package au.org.theark.core.model.lims.entity;

import java.util.List;

public interface InvTreeNode<T> {
	public List<T> getChildren();
	public String getNodeName();
	public String getNodeType();
}
