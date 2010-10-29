/**
 * 
 */
package au.org.theark.geno.util;

import au.org.theark.geno.exception.DataAcceptorIOException;
import au.org.theark.geno.model.entity.MarkerGroup;

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
	 * Called to move to a marker in preparation to accept the genotype 
	 */
	void gotoMarkerName(String markerName);

	//long getAllele1();
	/**
	 * Called to accept an allele 1 at the current marker position
	 */
	void setAllele1(String allele1);
	
	//long getAllele2();
	/**
	 * Called to accept an allele 1 at the current marker position
	 */
	void setAllele2(String allele2);

	/**
	 * Called when all data is provided and ready to be committed to storage.
	 * If the commit fails, then the an exception can be thrown.
	 */
	void commit() throws DataAcceptorIOException;
	
}
