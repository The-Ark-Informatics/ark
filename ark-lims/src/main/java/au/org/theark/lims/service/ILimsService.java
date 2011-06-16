package au.org.theark.lims.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.lims.model.vo.LimsVO;

public interface ILimsService
{
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	public void deleteBioCollection(LimsVO modelObject);

	public void updateBioCollection(LimsVO modelObject);

	public void createBioCollection(LimsVO modelObject);

	public List<au.org.theark.core.model.lims.entity.BioCollection> searchBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) throws ArkSystemException;

	public au.org.theark.core.model.lims.entity.BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException;

}
