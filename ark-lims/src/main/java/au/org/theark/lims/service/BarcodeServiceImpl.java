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

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.lims.model.dao.IBarcodeDao;

/**
 * The implementation of IBarcodeService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author cellis
 * 
 */
@Transactional
@Service(au.org.theark.lims.web.Constants.LIMS_BARCODE_SERVICE)
public class BarcodeServiceImpl implements IBarcodeService {
	private static final Logger	log			= LoggerFactory.getLogger(BarcodeServiceImpl.class);
	private IBarcodeDao				iBarcodeDao;
	private VelocityEngine			velocityEngine;
	static private final String	REAL_NUMBER	= "^[-+]?\\d+(\\.\\d+)?$";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

	public IBarcodeDao getiBarcodeDao() {
		return iBarcodeDao;
	}

	@Autowired
	public void setiBarcodeDao(IBarcodeDao iBarcodeDao) {
		this.iBarcodeDao = iBarcodeDao;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void createBarcodePrinter(BarcodePrinter barcodePrinter) {
		iBarcodeDao.createBarcodePrinter(barcodePrinter);
	}

	public void deleteBarcodePrinter(BarcodePrinter barcodePrinter) {
		iBarcodeDao.createBarcodePrinter(barcodePrinter);
	}

	public void updateBarcodePrinter(BarcodePrinter barcodePrinter) {
		iBarcodeDao.updateBarcodePrinter(barcodePrinter);
	}

	public void createBarcodeLabel(BarcodeLabel barcodeLabel) {
		iBarcodeDao.createBarcodeLabel(barcodeLabel);
	}

	public void createBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iBarcodeDao.createBarcodeLabelData(barcodeLabelData);
	}

	public void deleteBarcodeLabel(BarcodeLabel barcodeLabel) {
		iBarcodeDao.deleteBarcodeLabel(barcodeLabel);
	}

	public void deleteBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iBarcodeDao.deleteBarcodeLabelData(barcodeLabelData);
	}

	public void updateBarcodeLabel(BarcodeLabel BarcodeLabel) {
		iBarcodeDao.updateBarcodeLabel(BarcodeLabel);
	}

	public void updateBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		iBarcodeDao.updateBarcodeLabelData(barcodeLabelData);
	}

	public BarcodeLabel searchBarcodeLabel(BarcodeLabel barcodeLabel) {
		return iBarcodeDao.searchBarcodeLabel(barcodeLabel);
	}

	public BarcodeLabelData searchBarcodeLabelData(BarcodeLabelData barcodeLabelData) {
		return iBarcodeDao.searchBarcodeLabelData(barcodeLabelData);
	}

	public BarcodePrinter searchBarcodePrinter(BarcodePrinter barcodePrinter) {
		return iBarcodeDao.searchBarcodePrinter(barcodePrinter);
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
		String subjectUid = bioCollection.getLinkSubjectStudy().getSubjectUID();
		//TODO: need to get custom field data
		String familyId = "FAMILYID";
		//TODO: need to get custom field data
		String asrbno = "ASRBNO";
		String collectionDate = simpleDateFormat.format(bioCollection.getCollectionDate());
		String refDoctor = bioCollection.getRefDoctor();
		String dateOfBirth = simpleDateFormat.format(bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth());
		String sex = bioCollection.getLinkSubjectStudy().getPerson().getGenderType().getName();
		
		velocityContext.put("subjectUid", subjectUid);
		velocityContext.put("familyId", familyId);
		velocityContext.put("asrbno", asrbno);
		velocityContext.put("collectionDate", collectionDate);
		velocityContext.put("refDoctor", refDoctor);
		if (dateOfBirth != null) {
			velocityContext.put("dateOfBirth", dateOfBirth);
		}
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
}
