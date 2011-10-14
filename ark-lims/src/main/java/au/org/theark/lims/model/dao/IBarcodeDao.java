package au.org.theark.lims.model.dao;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.model.lims.entity.BarcodePrinter;

public interface IBarcodeDao {
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
}
