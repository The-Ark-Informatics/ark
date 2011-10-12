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
package au.org.theark.geno.util;

import au.org.theark.geno.exception.DataAcceptorIOException;

/**
 * IMapDataAcceptor is an interface that should be implemented by a class to  
 * accept PED data provided by the PedMapImport processMap(..) method.
 * The motivation is to separate PedMapImport from the back-end storage method.
 * 
 * @author elam
 *
 */
public interface IMapDataAcceptor {

	/**
	 * Called each time a new set of marker data is ready to be accepted
	 */
	void init() throws DataAcceptorIOException;

	//String getMarkerName();
	/**
	 * Called to accept the marker name
	 */
	void setMarkerName(String mkrName);
	
	//long getChromoNum();
	/**
	 * Called to accept the associated chromosome number
	 */
	void setChromosome(String chromoNum);
	
	//long getGeneDist();
	/**
	 * Called to accept the associated genetic distance (morgans)
	 */
	void setGeneDist(long geneDist);
	
	//long getBpPos();
	/**
	 * Called to accept the associated base-pair position (bp units)
	 */
	void setBpPos(long bpPos);
	
	/**
	 * Called when all data is provided and ready to be synchronised
	 * (e.g. committed to database, written to file)
	 * If the sync fails, then the an exception can be thrown.
	 */
	void sync() throws DataAcceptorIOException;
	
}
