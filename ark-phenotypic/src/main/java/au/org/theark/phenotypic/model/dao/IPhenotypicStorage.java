/**
 * 
 */
package au.org.theark.phenotypic.model.dao;

import java.util.Date;

import au.org.theark.phenotypic.exception.StorageIOException;

/**
 * IMapStorage is an interface that should be implemented by a class to  
 * store map data provided by the PhenotypicImport processFile(..) method.
 * The motivation is to separate PhenotypicImport from the back-end storage method.
 * 
 * @author cellis
 *
 */
public interface IPhenotypicStorage {

	/**
	 * Called each time a new set of marker data is ready to be accepted
	 */
	void init() throws StorageIOException;

	//String getFieldName();
	/**
	 * Called to accept the field name
	 */
	void setFieldName(String fieldName);
	
	//String getSubjectId();
	/**
	 * Called to accept the subject id
	 */
	void setSubjectId(String subjectId);
	
	//String getDateCollected();
	/**
	 * Called to accept the date collected
	 */
	void setDateCollected(Date dateCollected);
	
	/**
	 * Called when all data is provided and ready to be committed to storage.
	 * If the commit fails, then the an exception can be thrown.
	 */
	void commit() throws StorageIOException;
	
}
