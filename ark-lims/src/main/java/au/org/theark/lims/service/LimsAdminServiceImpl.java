package au.org.theark.lims.service;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.dao.ILimsAdminDao;

/**
 * The implementation of IBarcodeService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author cellis
 * 
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
public class LimsAdminServiceImpl implements ILimsAdminService {
	private static final Logger	log			= LoggerFactory.getLogger(LimsAdminServiceImpl.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private ILimsAdminDao			iLimsAdminDao;
	private VelocityEngine			velocityEngine;
	static private final String	REAL_NUMBER	= "^[-+]?\\d+(\\.\\d+)?$";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	
	/**
	 * @param iArkCommonService
	 *           the iArkCommonService to set
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}
	
	public ILimsAdminDao getiLimsAdminDao() {
		return iLimsAdminDao;
	}
	
	@Autowired
	public void setiLimsAdminDao(ILimsAdminDao iLimsAdminDao) {
		this.iLimsAdminDao = iLimsAdminDao;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void createBarcodePrinter(BarcodePrinter barcodePrinter) {
		iLimsAdminDao.createBarcodePrinter(barcodePrinter);
	}

	public void deleteBarcodePrinter(BarcodePrinter barcodePrinter) {
		iLimsAdminDao.createBarcodePrinter(barcodePrinter);
	}

	public void updateBarcodePrinter(BarcodePrinter barcodePrinter) {
		iLimsAdminDao.updateBarcodePrinter(barcodePrinter);
	}

	public void createBarcodeLabel(BarcodeLabel barcodeLabel) {
		iLimsAdminDao.createBarcodeLabel(barcodeLabel);
	}

	public void createBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iLimsAdminDao.createBarcodeLabelData(barcodeLabelData);
	}

	public void deleteBarcodeLabel(BarcodeLabel barcodeLabel) {
		iLimsAdminDao.deleteBarcodeLabel(barcodeLabel);
	}

	public void deleteBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iLimsAdminDao.deleteBarcodeLabelData(barcodeLabelData);
	}

	public void updateBarcodeLabel(BarcodeLabel BarcodeLabel) {
		iLimsAdminDao.updateBarcodeLabel(BarcodeLabel);
	}

	public void updateBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iLimsAdminDao.updateBarcodeLabelData(barcodeLabelData);
	}

	public BarcodeLabel searchBarcodeLabel(BarcodeLabel barcodeLabel) {
		return iLimsAdminDao.searchBarcodeLabel(barcodeLabel);
	}

	public BarcodeLabelData searchBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		return iLimsAdminDao.searchBarcodeLabelData(barcodeLabelData);
	}

	public BarcodePrinter searchBarcodePrinter(BarcodePrinter barcodePrinter) {
		return iLimsAdminDao.searchBarcodePrinter(barcodePrinter);
	}

	public String createBioCollectionLabelTemplate(BioCollection bioCollection, BarcodeLabel barcodeLabel) {
		/* lets make a Context and put data into it */
		VelocityContext context = getBioCollectionLabelContext(bioCollection);

		/* lets render a template */
		StringWriter stringWriter = new StringWriter();

		/* lets make our own string to render */
		String template = getBarcodeLabelTemplate(barcodeLabel);

		stringWriter = new StringWriter();

		Velocity.evaluate(context, stringWriter, "bioCollectionLabelTemplate", template);
		log.info("Created bioCollectionLabelTemplate: " + stringWriter);
		return stringWriter.toString();
	}

	public String createBiospecimenLabelTemplate(Biospecimen biospecimen, BarcodeLabel barcodeLabel) {
		/* lets make a Context and put data into it */
		VelocityContext context = getBiospecimenLabelContext(biospecimen);
		
		/* lets render a template */
		StringWriter stringWriter = new StringWriter();

		/* lets make our own string to render */
		String template = getBarcodeLabelTemplate(barcodeLabel);

		stringWriter = new StringWriter();

		Velocity.evaluate(context, stringWriter, "biospecimenLabelTemplate", template);
		log.info("Created biospecimenLabelTemplate: " + stringWriter);
		return stringWriter.toString();
	}

	public VelocityContext getBiospecimenLabelContext(Biospecimen biospecimen) {
		/* lets make a Context and put data into it */
		VelocityContext velocityContext = new VelocityContext();
		String lastLineOfCircle = "";
		String secondLineOfCircle = "";
		String biospecimenUid = biospecimen.getBiospecimenUid();
		
		String dateOfBirth = "";
		if (biospecimen.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
			dateOfBirth = simpleDateFormat.format(biospecimen.getLinkSubjectStudy().getPerson().getDateOfBirth());
		}

		// Ok first check and see if last character is a number.
		String lastChar = biospecimenUid.substring(biospecimenUid.length() - 1);
		// Need to find out where the study code ends
		// Trim off the year component
		String withoutYear = biospecimenUid.substring(2);
		Pattern pat = Pattern.compile("\\d");
		Matcher matcher = pat.matcher(withoutYear);
		boolean gotMatch = matcher.find();
		int indexOfnum = -1;
		if (gotMatch) {
			indexOfnum = matcher.start() + 2;
		}
		if (lastChar.matches(REAL_NUMBER)) {
			// We have a clone, therefore go one backwards.
			lastLineOfCircle = biospecimenUid.substring(biospecimenUid.length() - 2);
			secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 2);
		}
		else {
			lastLineOfCircle = lastChar;
			secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 1);
		}
		String secondLine = secondLineOfCircle;

		try {
			secondLine = new Integer(secondLineOfCircle).toString();
		}
		catch (NumberFormatException e) {
			log.error(e.getMessage());
		}

		velocityContext.put("biospecimenUid", biospecimenUid);
		velocityContext.put("firstLineOfCircle", biospecimenUid.substring(0, indexOfnum));
		velocityContext.put("secondLineOfCircle", secondLine);
		velocityContext.put("lastLineOfCircle", lastLineOfCircle);
		if (dateOfBirth != null) {
			velocityContext.put("dateOfBirth", dateOfBirth);
		}
		return velocityContext;
	}
	
	public VelocityContext getBioCollectionLabelContext(BioCollection bioCollection) {
		VelocityContext velocityContext = new VelocityContext();
		LinkSubjectStudy linkSubjectStudy = null;
		try {
			linkSubjectStudy = iArkCommonService.getSubjectByUID(bioCollection.getLinkSubjectStudy().getSubjectUID(), bioCollection.getStudy());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		String subjectUid = linkSubjectStudy.getSubjectUID();
		//TODO: need to get custom field data
		String familyId = "FAMILYID";
		//TODO: need to get custom field data
		String asrbno = "ASRBNO";
		String collectionDate = new String();
		if(bioCollection.getCollectionDate() != null) {
			collectionDate = simpleDateFormat.format(bioCollection.getCollectionDate());
		}
		String refDoctor = new String();
		if(bioCollection.getRefDoctor() != null) {
			refDoctor = bioCollection.getRefDoctor();
		}
		String dateOfBirth = new String();
		if(bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
			dateOfBirth= simpleDateFormat.format(bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth());
		}
		String sex = new String();
		if(bioCollection.getLinkSubjectStudy().getPerson().getGenderType() != null) {
			bioCollection.getLinkSubjectStudy().getPerson().getGenderType().getName();
		}
		
		velocityContext.put("subjectUid", subjectUid);
		velocityContext.put("familyId", familyId);
		velocityContext.put("asrbno", asrbno);
		velocityContext.put("collectionDate", collectionDate);
		velocityContext.put("refDoctor", refDoctor);
		velocityContext.put("dateOfBirth", dateOfBirth);
		velocityContext.put("sex", sex);
		
		return velocityContext;
	}
	
	/**
	 * Build the barcode label commands into a template string for merging via Velocity
	 * @param barcodeLabel
	 * @return
	 */
	public String getBarcodeLabelTemplate(BarcodeLabel barcodeLabel) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(barcodeLabel.getLabelPrefix());
		
		List<BarcodeLabelData> data = barcodeLabel.getBarcodeLabelData();
		for (Iterator<BarcodeLabelData> iterator = data.iterator(); iterator.hasNext();) {
			BarcodeLabelData barcodeLabelData = (BarcodeLabelData) iterator.next();
			
			sb.append(barcodeLabelData.getCommand());
			sb.append(barcodeLabelData.getXCoord());
			sb.append(",");
			sb.append(barcodeLabelData.getYCoord());
			sb.append(",");
			
			if(barcodeLabelData.getP1() != null) {
				sb.append(barcodeLabelData.getP1());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP2() != null) {
				sb.append(barcodeLabelData.getP2());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP3() != null) {
				sb.append(barcodeLabelData.getP3());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP4() != null) {
				sb.append(barcodeLabelData.getP4());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP5() != null) {
				sb.append(barcodeLabelData.getP5());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP6() != null) {
				sb.append(barcodeLabelData.getP6());
				sb.append(",");
			}
			
			if(barcodeLabelData.getP7() != null) {
				sb.append(barcodeLabelData.getP7());
				sb.append(",");
			}
			
			// Quote the data
			sb.append(barcodeLabelData.getQuoteLeft());
			
			// Add the data/text
			sb.append(barcodeLabelData.getData());
			
			// End quote the data
			sb.append(barcodeLabelData.getQuoteRight());
			
			// Add a line feed
			sb.append(barcodeLabelData.getLineFeed());
		}
		
		// add label suffix
		sb.append(barcodeLabel.getLabelSuffix());

		return sb.toString();	
	}

	public int getBarcodeLabelCount(BarcodeLabel object) {
		return iLimsAdminDao.getBarcodeLabelCount(object);
	}

	public int getBarcodePrinterCount(BarcodePrinter object) {
		return iLimsAdminDao.getBarcodePrinterCount(object);
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count) {
		return iLimsAdminDao.searchPageableBarcodeLabels(object, first, count);
	}

	public List<BarcodePrinter> searchPageableBarcodePrinters(BarcodePrinter object, int first, int count) {
		return iLimsAdminDao.searchPageableBarcodePrinters(object, first, count);
	}

	public List<BarcodePrinter> getBarcodePrinters(List<Study> studyListForUser) {
		return iLimsAdminDao.getBarcodePrinters(studyListForUser);
	}

	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		iLimsAdminDao.createBiospecimenUidTemplate(biospecimenUidTemplate);		
	}

	public void deleteBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		iLimsAdminDao.deleteBiospecimenUidTemplate(biospecimenUidTemplate);
	}

	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study) {
		return iLimsAdminDao.getBiospecimenUidTemplate(study);
	}

	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		iLimsAdminDao.updateBiospecimenUidTemplate(biospecimenUidTemplate);
	}

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars() {
		return iLimsAdminDao.getBiospecimenUidPadChars();
	}

	public int getBiospecimenUidTemplateCount(BiospecimenUidTemplate modelObject) {
		return iLimsAdminDao.getBiospecimenUidTemplateCount(modelObject);
	}

	public List<BiospecimenUidToken> getBiospecimenUidTokens() {
		return iLimsAdminDao.getBiospecimenUidTokens();
	}

	public BiospecimenUidTemplate searchBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate) {
		return iLimsAdminDao.searchBiospecimenUidTemplate(biospecimenUidTemplate);
	}

	public List<BiospecimenUidTemplate> searchPageableBiospecimenUidTemplates(BiospecimenUidTemplate object, int first, int count) {
		return iLimsAdminDao.searchPageableBiospecimenUidTemplates(object, first, count);
	}
}
