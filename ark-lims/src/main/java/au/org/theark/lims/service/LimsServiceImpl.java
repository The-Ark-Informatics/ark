/**
 * 
 */
package au.org.theark.lims.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.dao.IBioCollectionDao;
import au.org.theark.lims.model.vo.LimsVO;

/**
 * @author cellis
 *
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_SERVICE)
public class LimsServiceImpl implements ILimsService
{
	private static Logger log = LoggerFactory.getLogger(LimsServiceImpl.class);
	
	private IArkCommonService<Void> arkCommonService;
	private IStudyDao iStudyDao;
	private IBioCollectionDao iBioCollectionDao;

	/**
	 * @param arkCommonService the arkCommonService to set
	 */
	@Autowired
	public void setArkCommonService(IArkCommonService<Void> arkCommonService)
	{
		this.arkCommonService = arkCommonService;
	}

	/**
	 * @param iStudyDao the iStudyDao to set
	 */
	@Autowired
	public void setiStudyDao(IStudyDao iStudyDao)
	{
		this.iStudyDao = iStudyDao;
	}
	
	/**
	 * @param iBioCollectionDao the iBioCollectionDao to set
	 */
	@Autowired
	public void setiBioCollectionDao(IBioCollectionDao iBioCollectionDao)
	{
		this.iBioCollectionDao = iBioCollectionDao;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#createCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBioCollection(LimsVO modelObject)
	{
		log.info("Creating bioCollection: " + modelObject.getBioCollection().getName());
		iBioCollectionDao.createBioCollection(modelObject.getBioCollection());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#deleteCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBioCollection(LimsVO modelObject)
	{
		log.info("Deleting bioCollection: " + modelObject.getBioCollection().getName());
		iBioCollectionDao.deleteBioCollection(modelObject.getBioCollection());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getCollection(au.org.theark.core.model.lims.entity.Collection)
	 */
	public BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException
	{
		log.info("Getting bioCollection: " + id.intValue());
		return iBioCollectionDao.getBioCollection(id);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getPerson(java.lang.Long)
	 */
	public Person getPerson(Long id) throws EntityNotFoundException, ArkSystemException
	{
		log.info("Getting Person: " + id.intValue());
		return iStudyDao.getPerson(id);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#searchLimsCollection(au.org.theark.core.model.lims.entity.Collection)
	 */
	public java.util.List<BioCollection> searchBioCollection(BioCollection bioCollection) throws ArkSystemException
	{
		return iBioCollectionDao.searchBioCollection(bioCollection);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#updateCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBioCollection(LimsVO modelObject)
	{
		// TODO Auto-generated method stub

	}

}
