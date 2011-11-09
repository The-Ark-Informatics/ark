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
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Phone;

/**
 * @author nivedann
 * 
 */
public class PhoneVO implements Serializable {

	private Phone					phone;

	private Collection<Phone>	phoneList;

	public PhoneVO() {
		phone = new Phone();
		phoneList = new ArrayList<Phone>();
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Collection<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(Collection<Phone> phoneList) {
		this.phoneList = phoneList;
	}

}
