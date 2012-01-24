package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BiospecimenUidPadChar;
import au.org.theark.core.model.lims.entity.BiospecimenUidTemplate;
import au.org.theark.core.model.lims.entity.BiospecimenUidToken;
import au.org.theark.core.model.study.entity.Study;

public interface ILimsAdminDao {
	/**
	 * Gets the barcodePrinter from the database
	 * @param barcodePrinter
	 * @return
	 */
	public BarcodePrinter searchBarcodePrinter(BarcodePrinter barcodePrinter);
	
	/**
	 * Gets the barcodeLabel from the database
	 * @param barcodeLabel
	 * @return
	 */
	public BarcodeLabel searchBarcodeLabel(BarcodeLabel barcodeLabel);
	
	/**
	 * Gets the barcodeLabelData from the database
	 * @param barcodeLabelData
	 * @return
	 */
	public BarcodeLabelData searchBarcodeLabelData(BarcodeLabelData barcodeLabelData);
	
	/**
	 * Create a barcode printer based on the supplied BarcodePrinter
	 * 
	 * @param barcodePrinter
	 *           the BarcodePrinter object
	 */
	public void createBarcodePrinter(BarcodePrinter barcodePrinter);
	
	/**
	 * Update a barcode printer based on the supplied BarcodePrinter
	 * 
	 * @param barcodePrinter
	 *           the BarcodePrinter object
	 */
	public void updateBarcodePrinter(BarcodePrinter barcodePrinter);
	
	/**
	 * Delete a barcode printer based on the supplied BarcodePrinter
	 * 
	 * @param barcodePrinter
	 *           the BarcodePrinter object
	 */
	public void deleteBarcodePrinter(BarcodePrinter barcodePrinter);
	
	/**
	 * Create a barcode Label based on the supplied barcodeLabel
	 * 
	 * @param barcodeLabel
	 *           the BarcodeLabel object
	 */
	public void createBarcodeLabel(BarcodeLabel barcodeLabel);
	
	/**
	 * Update a barcode label based on the supplied BarcodeLabel
	 * 
	 * @param BarcodeLabel
	 *           the BarcodeLabel object
	 */
	public void updateBarcodeLabel(BarcodeLabel BarcodeLabel);
	
	/**
	 * Delete a barcode label based on the supplied BarcodeLabel
	 * 
	 * @param barcodeLabel
	 *           the BarcodeLabel object
	 */
	public void deleteBarcodeLabel(BarcodeLabel barcodeLabel);
	
	/**
	 * Create a barcode label data based on the supplied BarcodeLabelData
	 * 
	 * @param barcodeLabelData
	 *           the BarcodeLabelData object
	 */
	public void createBarcodeLabelData(BarcodeLabelData barcodeLabelData);
	
	/**
	 * Update a barcode label data based on the supplied BarcodeLabelData
	 * 
	 * @param barcodeLabelData
	 *           the BarcodeLabelData object
	 */
	public void updateBarcodeLabelData(BarcodeLabelData barcodeLabelData);
	
	/**
	 * Delete a barcode label data based on the supplied BarcodeLabelData
	 * 
	 * @param barcodeLabelData
	 *           the BarcodeLabelData object
	 */
	public void deleteBarcodeLabelData(BarcodeLabelData barcodeLabelData);
	
	/**
	 * Get the total count of the entities
	 * @param object
	 * @return
	 */
	public int getBarcodePrinterCount(BarcodePrinter object);

	/**
	 * Search the entities, restricted by a pageable count
	 * @param object
	 * @param first
	 * @param count
	 * @return
	 */
	public List<BarcodePrinter> searchPageableBarcodePrinters(BarcodePrinter object, int first, int count);
	
	/**
	 * Get the total count of the entities
	 * @param object
	 * @return
	 */
	public int getBarcodeLabelCount(BarcodeLabel object);

	/**
	 * Search the entities, restricted by a pageable count
	 * @param object
	 * @param first
	 * @param count
	 * @return
	 */
	public List<BarcodeLabel> searchPageableBarcodeLabels(BarcodeLabel object, int first, int count);
	
	/**
	 * Gets the list of barcodePrinters for the spcified list of studies
	 * @param studyListForUser
	 * @return
	 */
	public List<BarcodePrinter> getBarcodePrinters(List<Study> studyListForUser);
	
	/**
	 * Get the BiospeciemenUidTemplate for the specifies Study
	 * @param study
	 * @return
	 */
	public BiospecimenUidTemplate getBiospecimenUidTemplate(Study study);
	
	/**
	 * Create a biospecimenUid template based on the supplied BiospecimenUidTemplate
	 * 
	 * @param BiospecimenUidTemplate
	 *           the BiospecimenUidTemplate object
	 */
	public void createBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);
	
	/**
	 * Update a biospecimenUid template based on the supplied BiospecimenUidTemplate
	 * 
	 * @param BiospecimenUidTemplate
	 *           the BiospecimenUidTemplate object
	 */
	public void updateBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);
	
	/**
	 * Delete a biospecimenUid template based on the supplied BiospecimenUidTemplate
	 * 
	 * @param BiospecimenUidTemplate
	 *           the BiospecimenUidTemplate object
	 */
	public void deleteBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);
	
	public int getBiospecimenUidTemplateCount(BiospecimenUidTemplate modelObject);

	public List<BiospecimenUidToken> getBiospecimenUidTokens();

	public List<BiospecimenUidPadChar> getBiospecimenUidPadChars();

	public List<BiospecimenUidTemplate> searchPageableBiospecimenUidTemplates(BiospecimenUidTemplate object, int first, int count);

	public BiospecimenUidTemplate searchBiospecimenUidTemplate(BiospecimenUidTemplate biospecimenUidTemplate);

	/**
	 * Gets the list of barcodePrinters for the specified study
	 * @param study
	 * @return
	 */
	public List<BarcodePrinter> getBarcodePrintersByStudy(Study study);

	/**
	 * Gets a list of Studys that are already assigned to barcodePrinters
	 * @return
	 */
	public List<Study> getStudyListAssignedToBarcodePrinter();
	
	/**
	 * Gets a list of Studys that are already assigned to barcodeLabels
	 * @return
	 */
	public List<Study> getStudyListAssignedToBarcodeLabel();
	
	/**
	 * Gets a list of Studys that are already assigned to biospeicmenUidTemplates
	 * @return
	 */
	public List<Study> getStudyListAssignedToBiospecimenUidTemplate();
	
	
	/**
	 * Gets the list of barcodeLabelData for the specified barcodeLabel
	 * @param barcodeLabel
	 * @return
	 */
	public List<BarcodeLabelData> getBarcodeLabelDataByBarcodeLabel(BarcodeLabel barcodeLabel);

	/**
	 * Get the barcode labels for the specified study
	 * @param study
	 * @return
	 */
	public List<BarcodeLabel> getBarcodeLabelsByStudy(Study study);

	/**
	 * Get a list of BarcodeLabel templates
	 * @return
	 */
	public List<BarcodeLabel> getBarcodeLabelTemplates();

	/**
	 * Gets a list of Printers for the specified list of Studies
	 * @param studyListForUser
	 * @return
	 */
	public List<BarcodePrinter> getBarcodePrintersByStudyList(List<Study> studyListForUser);
}
