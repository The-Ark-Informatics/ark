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

import au.org.theark.core.model.geno.entity.MarkerGroup;
import au.org.theark.geno.exception.DataAcceptorIOException;

/**
 * IPedDataAcceptor is an interface that should be implemented by a class to  
 * accept PED data provided by the GWASImport processPed(..) method.
 * The motivation is to separate GWASImport from the back-end storage method.
 * 
 * @author elam
 *
 */
public interface IPedDataAcceptor {

	/**
	 * Called each time a new set of marker data is ready to be accepted
	 */
	void init() throws DataAcceptorIOException;

	//String getFamilyId();
	/**
	 * Called to accept the family id
	 */
	void setFamilyId(String familyId);
	
	//String getIndivId();
	/**
	 * Called to accept the individual id
	 */
	void setIndivId(String indivId);

	//String getFatherId();
	/**
	 * Called to accept the father id
	 */
	void setFatherId(String fatherId);

	//String getMotherId();
	/**
	 * Called to accept the mother id
	 */
	void setMotherId(String motherId);

	//String getGender();
	/**
	 * Called to accept the gender
	 */
	void setGender(String gender);
	
	//long getPhenotype();
	/**
	 * Called to accept a phenotype for the individual
	 */
	void setPhenotype(String phenotype);

	//long getMarkerName();
	/**
	 * Called to set a marker in preparation to accept the genotype 
	 */
	void setMarkerName(String markerName);

	//long getAllele1();
	/**
	 * Called to accept an allele 1 at the current marker position
	 * @param markerIdx 
	 */
	void setAllele1(String allele1);
	
	//long getAllele2();
	/**
	 * Called to accept an allele 1 at the current marker position
	 * @param markerIdx 
	 */
	void setAllele2(String allele2);

	/**
	 * Called when all data is provided and ready to be synchronised
	 * (e.g. committed to database, written to file)
	 * If the sync fails, then the an exception can be thrown.
	 */
	void sync() throws DataAcceptorIOException;

}
