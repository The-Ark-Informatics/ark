package au.org.theark.lims.service;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.model.dao.IBioCollectionDao;
import au.org.theark.lims.model.dao.IBiospecimenDao;
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
	private static final Logger	log					= LoggerFactory.getLogger(LimsAdminServiceImpl.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private ILimsAdminDao			iLimsAdminDao;
	private IBiospecimenDao			iBiospecimenDao;
	private IBioCollectionDao		iBioCollectionDao;
	private VelocityEngine			velocityEngine;
//	static private final String	REAL_NUMBER			= "^[-+]?\\d+(\\.\\d+)?$";
	private SimpleDateFormat		simpleDateFormat	= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

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

	public IBiospecimenDao getiBiospecimenDao() {
		return iBiospecimenDao;
	}

	@Autowired
	public void setiBiospecimenDao(IBiospecimenDao iBiospecimenDao) {
		this.iBiospecimenDao = iBiospecimenDao;
	}

	public IBioCollectionDao getiBioCollectionDao() {
		return iBioCollectionDao;
	}

	@Autowired
	public void setiBioCollectionDao(IBioCollectionDao iBioCollectionDao) {
		this.iBioCollectionDao = iBioCollectionDao;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	public void createBarcodeLabel(BarcodeLabel barcodeLabel) {
		iLimsAdminDao.createBarcodeLabel(barcodeLabel);

		// Create child data
		for (Iterator<BarcodeLabelData> iterator = barcodeLabel.getBarcodeLabelData().iterator(); iterator.hasNext();) {
			BarcodeLabelData barcodeLabelData = (BarcodeLabelData) iterator.next();
			barcodeLabelData.setBarcodeLabel(barcodeLabel);
			createBarcodeLabelData(barcodeLabelData);
		}
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

	public void updateBarcodeLabel(BarcodeLabel barcodeLabel) {
		iLimsAdminDao.updateBarcodeLabel(barcodeLabel);

		// Update child data
		for (Iterator<BarcodeLabelData> iterator = barcodeLabel.getBarcodeLabelData().iterator(); iterator.hasNext();) {
			BarcodeLabelData barcodeLabelData = (BarcodeLabelData) iterator.next();
			barcodeLabelData.setBarcodeLabel(barcodeLabel);
			updateBarcodeLabelData(barcodeLabelData);
		}
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

	public List<String> createBioCollectionLabelTemplate(BioCollection bioCollection, BarcodeLabel barcodeLabel) {
		/* lets make a Context and put data into it */
		VelocityContext context = getBioCollectionLabelContext(bioCollection);

		/* lets make our own string to render */
		List<String> templates = getBarcodeLabelTemplate(barcodeLabel);
		List<String> result = new ArrayList<>(templates.size());

		for(String template : templates) {
			StringWriter stringWriter = new StringWriter();
			Velocity.evaluate(context, stringWriter, "bioCollectionLabelTemplate", template);
			log.info("Created bioCollectionLabelTemplate: " + stringWriter);
			result.add(stringWriter.toString());
		}

		return result;
	}

	public List<String> createBiospecimenLabelTemplate(Biospecimen biospecimen, BarcodeLabel barcodeLabel) {
		log.warn("about to print biospecimen " + biospecimen);
		/* lets make a Context and put data into it */
		VelocityContext context = getBiospecimenLabelContext(biospecimen);

		/* lets make our own string to render */
        List<String> templates = getBarcodeLabelTemplate(barcodeLabel);
        List<String> result = new ArrayList<>(templates.size());

		for(String template : templates) {
			StringWriter stringWriter = new StringWriter();
			Velocity.evaluate(context, stringWriter, "biospecimenLabelTemplate", template);
			log.info("Created biospecimenLabelTemplate: " + stringWriter);
			result.add(stringWriter.toString());
		}

		return result;
	}

	public VelocityContext getBiospecimenLabelContext(Biospecimen biospecimen) {
		/* lets make a Context and put data into it */
		VelocityContext velocityContext = new VelocityContext();

		String biospecimenUid = biospecimen.getBiospecimenUid();

		String dateOfBirth = "";
		if (biospecimen.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
			dateOfBirth = simpleDateFormat.format(biospecimen.getLinkSubjectStudy().getPerson().getDateOfBirth());
		}

		// // Ok first check and see if last character is a number.
		// String lastLineOfCircle = "";
		// String secondLineOfCircle = "";
		// String lastChar = biospecimenUid.substring(biospecimenUid.length() - 1);
		// // Need to find out where the study code ends
		// // Trim off the year component
		// String withoutYear = biospecimenUid.substring(2);
		// Pattern pat = Pattern.compile("\\d");
		// Matcher matcher = pat.matcher(withoutYear);
		// boolean gotMatch = matcher.find();
		// int indexOfnum = -1;
		// if (gotMatch) {
		// indexOfnum = matcher.start() + 2;
		// }
		// // if (lastChar.matches(REAL_NUMBER)) {
		// // // We have a clone, therefore go one backwards.
		// // lastLineOfCircle = biospecimenUid.substring(biospecimenUid.length() - 2);
		// // secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 2);
		// // }
		// // else {
		// lastLineOfCircle = lastChar;
		// secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 1);
		// // }
		// String secondLine = secondLineOfCircle;
		//
		// try {
		// secondLine = new Integer(secondLineOfCircle).toString();
		// }
		// catch (NumberFormatException e) {
		// log.error(e.getMessage());
		// }
		//
		log.info("velocity context should have specimen=" + biospecimenUid + " and dob=" + dateOfBirth);
		velocityContext.put("biospecimenUid", biospecimenUid);
		// velocityContext.put("firstLineOfCircle", biospecimenUid.substring(0, indexOfnum));
		// velocityContext.put("secondLineOfCircle", secondLine);
		// velocityContext.put("lastLineOfCircle", lastLineOfCircle);

		// Pad out to at least 15 characters
		biospecimenUid = org.apache.commons.lang.StringUtils.rightPad(biospecimenUid, 15);

		// Split biospecimenUid into 3 equal strings
		velocityContext.put("firstLineOfCircle", biospecimenUid.substring(0, 5).trim());
		velocityContext.put("secondLineOfCircle", biospecimenUid.substring(5, 11));
		velocityContext.put("lastLineOfCircle", biospecimenUid.substring(11, biospecimenUid.length()));
		
		// Sample type
		velocityContext.put("sampleType", biospecimen.getSampleType().getName());
		velocityContext.put("subjectUid", biospecimen.getLinkSubjectStudy().getSubjectUID());
		
		if (dateOfBirth != null) {
			velocityContext.put("dateOfBirth", dateOfBirth);
		}
		else{
			velocityContext.put("dateOfBirth", "noDOB");
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
		
		try {
			bioCollection = iBioCollectionDao.getBioCollection(bioCollection.getId());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		
		String subjectUid = linkSubjectStudy.getSubjectUID();

		//ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);

		// Custom field name "FAMILYID"
		BioCollectionCustomFieldData bioCollectionCustomFieldData = iBioCollectionDao.getBioCollectionCustomFieldData(bioCollection, arkFunction, "FAMILYID");
		String familyId = bioCollectionCustomFieldData.getTextDataValue();

		// Custom field name "ASRBNO"
		bioCollectionCustomFieldData = iBioCollectionDao.getBioCollectionCustomFieldData(bioCollection, arkFunction, "ASRBNO");
		String asrbno = bioCollectionCustomFieldData.getTextDataValue();


		
		String collectionDate = new String();
		if (bioCollection.getCollectionDate() != null) {
			collectionDate = simpleDateFormat.format(bioCollection.getCollectionDate());
		}
		String refDoctor = new String();
		if (bioCollection.getRefDoctor() != null) {
			refDoctor = bioCollection.getRefDoctor();
		}
		String dateOfBirth = new String();
		if (bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth() != null) {
			dateOfBirth = simpleDateFormat.format(bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth());
		}
		String sex = new String();
		if (bioCollection.getLinkSubjectStudy().getPerson().getGenderType() != null) {
			sex = bioCollection.getLinkSubjectStudy().getPerson().getGenderType().getName();
		}
		String collectionID = bioCollection.getBiocollectionUid();
		String subjectFirstName = linkSubjectStudy.getPerson().getFirstName();
		String subjectLastName = linkSubjectStudy.getPerson().getLastName();
		String initials = subjectFirstName.charAt(0) + "" +subjectLastName.charAt(0);
		
		velocityContext.put("initials", initials);
		velocityContext.put("firstName", subjectFirstName);
		velocityContext.put("lastName", subjectLastName);
		velocityContext.put("collectionID", collectionID);
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
	 * 
	 * @param barcodeLabel
	 * @return
	 */
	public List<String> getBarcodeLabelTemplate(BarcodeLabel barcodeLabel) {
		barcodeLabel.setBarcodeLabelData(getBarcodeLabelDataByBarcodeLabel(barcodeLabel));

		List<String> barcodeLabelCommands = new LinkedList<>();

		if (barcodeLabel != null && barcodeLabel.getLabelPrefix() != null) {

			barcodeLabelCommands.add(barcodeLabel.getLabelPrefix());

			List<BarcodeLabelData> data = barcodeLabel.getBarcodeLabelData();
			for (Iterator<BarcodeLabelData> iterator = data.iterator(); iterator.hasNext();) {
				BarcodeLabelData barcodeLabelData = (BarcodeLabelData) iterator.next();

                StringBuffer sb = new StringBuffer();

				sb.append(barcodeLabelData.getCommand());
				sb.append(barcodeLabelData.getXCoord());
				sb.append(",");
				sb.append(barcodeLabelData.getYCoord());
				sb.append(",");

				if (barcodeLabelData.getP1() != null) {
					sb.append(barcodeLabelData.getP1());
					sb.append(",");
				}

				if (barcodeLabelData.getP2() != null) {
					sb.append(barcodeLabelData.getP2());
					sb.append(",");
				}

				if (barcodeLabelData.getP3() != null) {
					sb.append(barcodeLabelData.getP3());
					sb.append(",");
				}

				if (barcodeLabelData.getP4() != null) {
					sb.append(barcodeLabelData.getP4());
					sb.append(",");
				}

				if (barcodeLabelData.getP5() != null) {
					sb.append(barcodeLabelData.getP5());
					sb.append(",");
				}

				if (barcodeLabelData.getP6() != null) {
					sb.append(barcodeLabelData.getP6());
					sb.append(",");
				}

				if (barcodeLabelData.getP7() != null) {
					sb.append(barcodeLabelData.getP7());
					sb.append(",");
				}

				if (barcodeLabelData.getP8() != null) {
					sb.append(barcodeLabelData.getP7());
					sb.append(",");
				}

				// Quote the data
				sb.append(barcodeLabelData.getQuoteLeft()==null?"":barcodeLabelData.getQuoteLeft());

				// Add the data/text
				sb.append(barcodeLabelData.getData()==null?"":barcodeLabelData.getData());

				// End quote the data
				sb.append(barcodeLabelData.getQuoteRight()==null?"":barcodeLabelData.getQuoteRight());

				// Add a line feed
				// sb.append(barcodeLabelData.getLineFeed());
				// sb.append("\n");

				barcodeLabelCommands.add(sb.toString());

			}

			// add label suffix
            barcodeLabelCommands.add(barcodeLabel.getLabelSuffix());
		}

		return barcodeLabelCommands;
	}

	public long getBarcodeLabelCount(BarcodeLabel object) {
		return iLimsAdminDao.getBarcodeLabelCount(object);
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count) {
		return iLimsAdminDao.searchPageableBarcodeLabels(object, first, count);
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

	public long getBiospecimenUidTemplateCount(BiospecimenUidTemplate modelObject) {
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

	public List<Study> getStudyListAssignedToBarcodeLabel() {
		return iLimsAdminDao.getStudyListAssignedToBarcodeLabel();
	}

	public List<Study> getStudyListAssignedToBiospecimenUidTemplate() {
		return iLimsAdminDao.getStudyListAssignedToBiospecimenUidTemplate();
	}

	public boolean studyHasBiospecimens(Study study) {
		return iBiospecimenDao.studyHasBiospecimens(study);
	}

	public List<BarcodeLabelData> getBarcodeLabelDataByBarcodeLabel(BarcodeLabel barcodeLabel) {
		return iLimsAdminDao.getBarcodeLabelDataByBarcodeLabel(barcodeLabel);
	}

	public List<BarcodeLabel> getBarcodeLabelsByStudy(Study study) {
		return iLimsAdminDao.getBarcodeLabelsByStudy(study);
	}

	public List<BarcodeLabel> getBarcodeLabelTemplates() {
		return iLimsAdminDao.getBarcodeLabelTemplates();
	}

	public Long getBarcodeLabelCount(BarcodeLabel object, List<Study> studyListForUser) {
		return iLimsAdminDao.getBarcodeLabelCount(object, studyListForUser);
	}

	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count, List<Study> studyListForUser) {
		return iLimsAdminDao.searchPageableBarcodeLabels(object, first, count, studyListForUser);
	}
}
