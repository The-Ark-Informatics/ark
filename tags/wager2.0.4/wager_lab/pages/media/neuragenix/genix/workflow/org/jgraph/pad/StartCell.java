/**
 * StartCell.java
 * Copyright � 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 23/02/2004
 */

package media.neuragenix.genix.workflow.org.jgraph.pad;

/**
 * Class to model a starting activity
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

import org.jgraph.graph.DefaultGraphCell;

public class StartCell extends DefaultGraphCell {

	public StartCell() {
		this(null);
	}

	public StartCell(Object userObject) {
		super(userObject);
	}
}
