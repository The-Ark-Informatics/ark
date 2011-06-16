package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;

public interface IBioCollectionDao
{
	/**
	 * Look up a LIMS Collection based on the supplied Long id that represents the primary key
	 * @param id
	 * @return au.org.theark.core.model.lims.entity.Collection
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public au.org.theark.core.model.lims.entity.BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up a List of LIMS Collection(s) based on the supplied limsCollection object
	 * @param limsCollection
	 * @return List<au.org.theark.core.model.lims.entity.Collection>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioCollection> searchBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection) throws ArkSystemException;
	
	/**
	 * Create a LIMS collection based on the supplied limsCollection
	 * @param modelObject
	 */
	public void createBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);
	
	/**
	 * Update a LIMS collection based on the supplied limsCollection
	 * @param modelObject
	 */
	public void updateBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);
	
	/**
	 * Delete a LIMS collection based on the supplied limsCollection
	 * @param modelObject
	 */
	public void deleteBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);
}