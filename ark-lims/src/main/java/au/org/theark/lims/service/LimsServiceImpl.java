/**
 * 
 */
package au.org.theark.lims.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.lims.model.dao.IBioCollectionDao;
import au.org.theark.lims.model.dao.IBioTransactionDao;
import au.org.theark.lims.model.dao.IBiospecimenDao;
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
	
	private IStudyDao iStudyDao;
	private IBioCollectionDao iBioCollectionDao;
	private IBiospecimenDao iBiospecimenDao;
	private IBioTransactionDao iBioTransactionDao;

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

	/**
	 * @param iBiospeciemenDao the iBiospeciemenDao to set
	 */
	@Autowired
	public void setiBiospeciemenDao(IBiospecimenDao iBiospecimenDao)
	{
		this.iBiospecimenDao = iBiospecimenDao;
	}

	/**
	 * @param iBioTransactionDao the iBioTransactionDao to set
	 */
	@Autowired
	public void setiBioTransactionDao(IBioTransactionDao iBioTransactionDao)
	{
		this.iBioTransactionDao = iBioTransactionDao;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#createCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBioCollection(LimsVO modelObject)
	{
		log.debug("Creating bioCollection: " + modelObject.getBioCollection().getName());
		iBioCollectionDao.createBioCollection(modelObject.getBioCollection());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#deleteBioCollection(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBioCollection(LimsVO modelObject)
	{
		log.debug("Deleting bioCollection: " + modelObject.getBioCollection().getName());
		iBioCollectionDao.deleteBioCollection(modelObject.getBioCollection());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getBioCollection(au.org.theark.core.model.lims.entity.Collection)
	 */
	public BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException
	{
		log.debug("Getting bioCollection: " + id.intValue());
		return iBioCollectionDao.getBioCollection(id);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getPerson(java.lang.Long)
	 */
	public Person getPerson(Long id) throws EntityNotFoundException, ArkSystemException
	{
		log.debug("Getting Person: " + id.intValue());
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
		log.debug("Updating bioCollection: " + modelObject.getBioCollection().getName());
		iBioCollectionDao.updateBioCollection(modelObject.getBioCollection());
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getBiospecimen(Long)
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException
	{
		return iBiospecimenDao.getBiospecimen(id);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#searchBiospecimen(Biospecimen)
	 */
	public List<Biospecimen> searchBiospecimen(Biospecimen biospecimen) throws ArkSystemException
	{
		return iBiospecimenDao.searchBiospecimen(biospecimen);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#createBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBiospecimen(LimsVO modelObject)
	{
		iBiospecimenDao.createBiospecimen(modelObject.getBiospecimen());
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#updateBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBiospecimen(LimsVO modelObject)
	{
		iBiospecimenDao.updateBiospecimen(modelObject.getBiospecimen());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#deleteBiospecimen(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBiospecimen(LimsVO modelObject)
	{
		iBiospecimenDao.deleteBiospecimen(modelObject.getBiospecimen());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getBioTransaction(Long)
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException
	{
		return iBioTransactionDao.getBioTransaction(id);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#searchBioTransaction(BioTransaction)
	 */
	public List<BioTransaction> searchBioTransaction(BioTransaction bioTransaction) throws ArkSystemException
	{
		return iBioTransactionDao.searchBioTransaction(bioTransaction);
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#createBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void createBioTransaction(LimsVO modelObject)
	{
		iBioTransactionDao.createBioTransaction(modelObject.getBioTransaction());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#deleteBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void deleteBioTransaction(LimsVO modelObject)
	{
		iBioTransactionDao.deleteBioTransaction(modelObject.getBioTransaction());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#updateBioTransaction(au.org.theark.lims.model.vo.LimsVO)
	 */
	public void updateBioTransaction(LimsVO modelObject)
	{
		iBioTransactionDao.updateBioTransaction(modelObject.getBioTransaction());
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getSampleTypes()
	 */
	public List<BioSampletype> getBioSampleTypes()
	{
		return iBioCollectionDao.getSampleTypes();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#hasBioCollections()
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy)
	{
		return iBioCollectionDao.hasBioCollections(linkSubjectStudy);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#hasBiospecimens(BioCollection bioCollection)
	 */
	public Boolean hasBiospecimens(BioCollection bioCollection)
	{
		return iBioCollectionDao.hasBiospecimens(bioCollection);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#getBioCollectionCount(BioCollection bioCollection)
	 */
	public int getBioCollectionCount(BioCollection bioCollectionCriteria) {
		return iBioCollectionDao.getBioCollectionCount(bioCollectionCriteria);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.lims.service.ILimsService#searchPageableBioCollections()
	 */
	public List<BioCollection> searchPageableBioCollections(
			BioCollection bioCollectionCriteria, int first, int count) {
		return iBioCollectionDao.searchPageableBioCollections(bioCollectionCriteria, first, count);
	}

	public int getBiospecimenCount(Biospecimen biospecimenCriteria) {
		return iBiospecimenDao.getBiospecimenCount(biospecimenCriteria);
	}

	public List<Biospecimen> searchPageableBiospecimens(
			Biospecimen biospecimenCriteria, int first, int count) {
		return iBiospecimenDao.searchPageableBiospecimens(biospecimenCriteria, first, count);
	}
}
