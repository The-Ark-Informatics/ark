package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

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
	 * @param limsCollection
	 */
	public void createBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);
	
	/**
	 * Update a LIMS collection based on the supplied limsCollection
	 * @param limsCollection
	 */
	public void updateBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);
	
	/**
	 * Delete a LIMS collection based on the supplied limsCollection
	 * @param limsCollection
	 */
	public void deleteBioCollection(au.org.theark.core.model.lims.entity.BioCollection limsCollection);

	/**
	 * Get a list of all sampleTypes
	 * @return List
	 */
	public List<BioSampletype> getSampleTypes();

	/**
	 * Determine if provided linkSubjectStudy has any BioCollections associated
	 * @return true if provided linkSubjectStudy has one or more BioCollections
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy);

	/**
	 * Determine if provided bioCollection has any biospecimens associated
	 * @return true if provided bioCollection has one or more Biospecimens
	 */
	public Boolean hasBiospecimens(BioCollection bioCollection);

	/**
	 * Get count of the BioCollections given the criteria
	 * @param BioCollection criteria
	 * @return counts
	 */
	public int getBioCollectionCount(BioCollection bioCollectionCriteria);
	
	/**
	 * A generic interface that will return a list BioCollections specified by a particular criteria, and a paginated reference point
	 * @param BioCollection criteria
	 * @return Collection of SubjectVO
	 */
	public List<BioCollection> searchPageableBioCollections(BioCollection bioCollectionCriteria, int first, int count);

}