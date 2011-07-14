package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioTransaction;

public interface IBioTransactionDao
{
	/**
	 * Look up a LIMS bioTransaction based on the supplied Long id that represents the primary key
	 * @param id
	 * @return BioTransaction
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up a List of LIMS BioTransaction(s) based on the supplied bioTransaction object
	 * @param bioTransaction
	 * @return List<au.org.theark.core.model.lims.entity.BioTransaction>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioTransaction> searchBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction) throws ArkSystemException;
	
	/**
	 * Create a LIMS BioTransaction based on the supplied bioTransaction
	 * @param bioTransaction
	 */
	public void createBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction);
	
	/**
	 * Update a LIMS BioTransaction based on the supplied bioTransaction
	 * @param bioTransaction
	 */
	public void updateBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction);
	
	/**
	 * Delete a LIMS BioTransaction based on the supplied bioTransaction
	 * @param bioTransaction
	 */
	public void deleteBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction);

}
