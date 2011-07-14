package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;

public interface IBiospecimenDao
{
	/**
	 * Look up a LIMS biospecimen based on the supplied Long id that represents the primary key
	 * @param id
	 * @return Biospecimen
	 * @throws EntityNotFoundException
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException;
	
	/**
	 * Look up a List of LIMS Biospecimen(s) based on the supplied biospecimen object
	 * @param biospecimen
	 * @return List<au.org.theark.core.model.lims.entity.Biospecimen>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.Biospecimen> searchBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException;
	
	/**
	 * Create a LIMS Biospecimen based on the supplied biospecimen
	 * @param biospecimen
	 */
	public void createBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);
	
	/**
	 * Update a LIMS Biospecimen based on the supplied biospecimen
	 * @param biospecimen
	 */
	public void updateBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);
	
	/**
	 * Delete a LIMS Biospecimen based on the supplied biospecimen
	 * @param biospecimen
	 */
	public void deleteBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);

	/**
	 * Get count of the Biospecimens given the criteria
	 * @param Biospecimens criteria
	 * @return counts
	 */
	public int getBiospecimenCount(Biospecimen biospecimenCriteria);

	/**
	 * A generic interface that will return a list Biospecimens specified by a particular criteria, and a paginated reference point
	 * @param Biospecimens criteria
	 * @return Collection of Biospecimen
	 */
	public List<Biospecimen> searchPageableBiospecimens(
			Biospecimen biospecimenCriteria, int first, int count);
	
}
