/**
 * 
 */
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
