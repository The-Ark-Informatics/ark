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
package au.org.theark.core.dao;


import java.sql.Blob;

import java.io.InputStream;

import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository("lobUtil")
public class LobUtil extends HibernateSessionDao {

	public Blob createBlob(byte[] bytes) {
		Session session = getSession();
		if(session==null){
			System.err.println("Get Session Call is failing! Therefore going to try open and close");
			session = openSession();
			try{
				LobHelper helper = session.getLobHelper();
				return helper.createBlob(bytes);
			}
			finally{
				closeSession(session);
			}
		}
		else{
			LobHelper helper = session.getLobHelper();
			return helper.createBlob(bytes);
		}
	}
/*	public Blob createBlob(InputStream inputStream, long length) {
		return getSession().getLobHelper().createBlob(inputStream, length);
	}*/
	public Blob createBlob(InputStream inputStream, long length) {
		Session session = getSession();
		if(session==null){
			System.err.println("Get Session Call is failing! Therefore going to try open and close");
			session = openSession();
			try{
				LobHelper helper = session.getLobHelper();
				return helper.createBlob(inputStream, length);
			}
			finally{
				closeSession(session);
			}
		}
		else{
			LobHelper helper = session.getLobHelper();
			return helper.createBlob(inputStream, length);
		}
	}
}
