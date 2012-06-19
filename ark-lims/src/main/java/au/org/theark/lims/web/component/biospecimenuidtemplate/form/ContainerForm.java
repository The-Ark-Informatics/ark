/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.lims.web.component.biospecimenuidtemplate.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author cellis
 * 
 */
public class ContainerForm extends AbstractContainerForm<BiospecimenUidTemplate> {


	private static final long	serialVersionUID	= -6773446229285764994L;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id, CompoundPropertyModel<BiospecimenUidTemplate> cpmModel) {
		super(id, cpmModel);
	}
}
