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
package au.org.theark.admin.web.component;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.web.form.AbstractContainerForm;

public class ContainerForm extends AbstractContainerForm<AdminVO> {


	private static final long	serialVersionUID	= 5677467747747142811L;

	public ContainerForm(String id, CompoundPropertyModel<AdminVO> model) {
		super(id, model);
	}

}
