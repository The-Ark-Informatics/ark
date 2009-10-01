/*
 * CBiospecimenNew.java
 *
 * Created on 15 March 2005, 15:51
 *
 *
 *
 * Description : Clean room implementation of the Biospecimen Channel
 *
 * @author : Daniel Murley dmurley@neuragenix.com
 *
 */

package neuragenix.bio.biospecimen;

import org.jasig.portal.IChannel;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelRuntimeProperties;
import org.jasig.portal.IMimeResponse;
import org.jasig.portal.PortalEvent;
import org.jasig.portal.PortalException;
import org.jasig.portal.UPFileSpec;
import org.jasig.portal.utils.XSLT;
import org.wager.barcode.BarcodeManager;
import org.xml.sax.ContentHandler;
import org.jasig.portal.security.*;
import org.jasig.portal.services.LogService;

import org.jasig.portal.PropertiesManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.StringBuffer;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.LinkedList;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import neuragenix.security.*;
import neuragenix.dao.*;
import neuragenix.security.exception.*;
import neuragenix.common.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import neuragenix.bio.biospecimen.batchactions.BatchCreationManager;
import neuragenix.bio.utilities.*;

public class CBiospecimen implements IChannel, IMimeResponse {
	private ChannelRuntimeData runtimeData = null;
	private ChannelStaticData staticData = null;
	private NGXRuntimeProperties rp = new NGXRuntimeProperties();
	private Hashtable hashChannelsNotToClearLock = null;

	// -- Core Module

	private BiospecimenCore bcBiospecimen = null;
	private BatchCloneManager mgrBatchClone = null;
	private VialCalculationManager vcManager = null;
	private BatchAllocation mgrBatchAllocation = null;
	private BatchQuantityManager mgrBatchQuantity = null;
	private BatchCreationManager bcreatManager = null;
	private BatchSFResultsManager mgrSFBatchGen = null;

	// -- Extention Modules
	private AttachmentsAndNotes attAttachments = null;
	private String strErrors;

	// -- Locking

	private LockRequest lckBiospecimen;
	private LockRequest lckPatient;
	private LockRequest lckInventory;

	// GUI Options

	private final int DEFAULT_PAGE_SIZE;
	private int intCurrentPagingSize;

	// use for Last Domain Search
	private int intLastDomainSearch = BiospecimenCore.DOMAIN_BIOSPECIMEN;

	// XSL styleheets
	private final static String XSL_VIAL_CALCULATION = "vial_calculation";
	private final static String XSL_AVAILABLE_TRAY = "available_tray_list";
	private final static String XSL_VIAL_CALCULATION_HISTORY = "vial_history";
	private final static String XSL_CONFIRM_VIAL_CALCULATION = "confirm_vial_calculation";

	// -- Constant messages

	private final String ERROR_MESSAGE_PERMISSION_DENIED = "You do not have the required permissions for this activity";
	private final String ERROR_MESSAGE_PERMISSION_DENIED_BIOSPECIMEN = "You do not have the required permissions to view this record";

	/** Creates a new instance of CBiospecimenNew */
	public CBiospecimen() {
		int pageSize = -1;
		try {
			pageSize = PropertiesManager
					.getPropertyAsInt("neuragenix.bio.RecordPerPage");

		} catch (Exception e) {
			e.printStackTrace();
			// set default
		}

		if (pageSize != -1) {
			DEFAULT_PAGE_SIZE = pageSize;
		} else {
			DEFAULT_PAGE_SIZE = 20;
		}

		intCurrentPagingSize = DEFAULT_PAGE_SIZE;

	}

	public ChannelRuntimeProperties getRuntimeProperties() {
		return new ChannelRuntimeProperties();
	}

	public void receiveEvent(PortalEvent ev) {
		try {

			if ((ev.getEventNumber() == ev.SESSION_DONE)
					|| (ev.getEventNumber() == ev.UNSUBSCRIBE)) {

				try {
					if (lckBiospecimen != null) {

						lckBiospecimen.unlock();
						lckBiospecimen = null;
					}

					if (lckPatient != null) {
						lckPatient.unlock();
						lckPatient = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				/*
				 * if((null!=lrBiospecLock)&&(lrBiospecLock.isValid()))
				 * 
				 * {
				 * 
				 * lrBiospecLock.unlock();
				 * 
				 * }
				 * 
				 * if((null!=lrPatientLock)&&(lrPatientLock.isValid()))
				 * 
				 * {
				 * 
				 * lrPatientLock.unlock();
				 * 
				 * }
				 */

			}

			// unlockAttachmentsRecord();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void renderXML(ContentHandler out) throws PortalException {
		// System.out.println(rp.getXML());

		// Create a new XSLT styling engine
		XSLT xslt = new XSLT(this);

		if (rp.getErrorMessage() != null && (!rp.getErrorMessage().equals(""))) {
			if (rp.getXML() == null || rp.getXML().trim().equals("")) {
				// No XML -- only errors!
				rp.addXML("<ErrorOnly></ErrorOnly>");
			}
		}
		xslt.setXML(rp.getXML());

		// Specify the stylesheet we're going to use
		xslt.setXSL("CBiospecimens.ssl", rp.getStylesheet(), runtimeData
				.getBrowserInfo());

		// Specify the

		xslt.setStylesheetParameter("baseActionURL", runtimeData
				.getBaseActionURL());
		xslt
				.setStylesheetParameter("strErrorMessage", rp
						.getErrorMessage(true));
		xslt.setStylesheetParameter("lastViewedID", bcBiospecimen
				.getLastViewedBiospecimenID()
				+ "");
		// System.out.println("ID is" +
		// bcBiospecimen.getLastViewedBiospecimenID());

		String strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();

		org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(
				runtimeData.getBaseActionURL(true));
		upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
				"CDownload"));
		xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
		xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(
				strSessionUniqueID, "CDownload"));

		// End Secure

		upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());

		upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
				"CInventory"));
		xslt.setStylesheetParameter("inventoryChannelURL", upfTmp.getUPFile());
		xslt.setStylesheetParameter("inventoryChannelTabOrder", SessionManager
				.getTabOrder(rp.getAuthToken(), "CInventory"));

		upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
				"CPatient"));
		xslt.setStylesheetParameter("patientChannelURL", upfTmp.getUPFile());
		xslt.setStylesheetParameter("patientChannelTabOrder", SessionManager
				.getTabOrder(rp.getAuthToken(), "CPatient"));

		upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
				"CStudy"));
		xslt.setStylesheetParameter("studyChannelURL", upfTmp.getUPFile());
		xslt.setStylesheetParameter("surveyChannelURL", upfTmp.getUPFile());

		upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
				"CBiospecimen"));
		xslt.setStylesheetParameter("biospecimenChannelTabOrder",
				SessionManager.getTabOrder(rp.getAuthToken(), "CBiospecimen"));

		/*
		 * upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID,
		 * "CSmartform")); xslt.setStylesheetParameter("smartformChannelURL",
		 * upfTmp.getUPFile()); xslt.setStylesheetParameter(
		 * "smartformChannelTabOrder", SessionManager.getTabOrder(
		 * rp.getAuthToken(), "CSmartform") );
		 */
		String baseWorkerURL = runtimeData
		.getBaseWorkerURL(UPFileSpec.FILE_DOWNLOAD_WORKER, true);
		String barcodeURL = baseWorkerURL.replaceFirst("uP$", "prn");
		
		xslt.setStylesheetParameter("baseWorkerURL", baseWorkerURL);
		xslt.setStylesheetParameter("barcodeURL", barcodeURL);
		
		xslt.setTarget(out);

		// do the deed

		xslt.transform();

		// destroy the old XML

		rp.clearXML();

	}

	private Hashtable htCurrentSearchCriteria = null;

	public static void printRuntimeData(ChannelRuntimeData runtimeData) {
		/*
		 * System.out.println("\n----------------");
		 * System.out.println("Runtime Data : "); Enumeration e =
		 * runtimeData.keys(); while (e.hasMoreElements()) { String keyValue =
		 * (String) e.nextElement(); System.out.println(keyValue + ":\t\t" +
		 * runtimeData.getParameter(keyValue)); }
		 * System.out.println("----------------");
		 */
	}

	public void setRuntimeData(ChannelRuntimeData rd) throws PortalException {

		// update the runtime data
		runtimeData = rd;
		boolean fatalError = false;
		int blAddedMulti = 0;
		// System.out.println("\n\n");
		// Utilities.printRuntimeData(rd);

		// Setup the good-ole locks

		if (lckBiospecimen == null)
			lckBiospecimen = new LockRequest(rp.getAuthToken());

		// Event handler

		// decide which mode the system is in

		String strModule = runtimeData.getParameter("module");

		printRuntimeData(rd);

		System.err.println("Biospecimen Channel : Current Module is - "
				+ strModule);

		/*
		 * Batch Quantity Allocation
		 * 
		 * 
		 * 
		 * Connector to transfer us to ISearch
		 */

		String strBQCurrent = runtimeData.getParameter("current");
		if (strBQCurrent != null) {
			if ((strBQCurrent.equals("build_isearch") || strBQCurrent
					.equals("isearch_result"))
					&& runtimeData.getParameter("ISEARCH_finish") == null
					&& mgrBatchQuantity != null) {
				rp.clearXML();
				rp.addXML(mgrBatchQuantity.doISearch(runtimeData));
				rp.setStylesheet(mgrBatchQuantity.getISearchStylesheet());
				strModule = "IGNORE_HANDLER"; // dummy to fool the handler

			}
		}

		/*
		 * Connector to bring us back into the batch processing code
		 */

		if (runtimeData.getParameter("ISEARCH_finish") != null) {
			// need to get isearch to generate the final set of flags

			// System.out.println ("Called ISearch finish");

			mgrBatchQuantity.setISearchFlags(runtimeData);
			runtimeData.remove("stage");
			runtimeData.remove("module");
			runtimeData.setParameter("stage", "ISEARCH_FINISH");
			runtimeData.setParameter("module", "BATCH_ALLOCATE");
			runtimeData.setParameter("current", "bp_allocate");
			strModule = "BATCH_ALLOCATE";

			// System.out.println ("Everything is set!");
		}

		/*
		 * ----------------------------------- End Batch Quantity Allocation
		 * code
		 */

		/*
		 * All new request handler Expects there to be a "module" parameter in
		 * everything otherwise it will default to search
		 */

		if (strModule == null) {
			System.err.println("Module is null -- Default handler");

			// clear all the locks as we're only searching

			if (lckBiospecimen != null) {
				lckBiospecimen.emptyAllLocks(rp.getAuthToken()
						.getSessionUniqueID());
			}

			String reset = runtimeData.getParameter("reset");

			if (htCurrentSearchCriteria != null && reset == null) {
				boolean isTransSearch = false;
				if (intLastDomainSearch == BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC)
					isTransSearch = true;

				rp.clearXML();
				rp.addXML("<biospecimen>");
				rp.addXML(bcBiospecimen.getSearchCriteriaXML());
				rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
						intLastDomainSearch, htCurrentSearchCriteria, true, 0,
						this.intCurrentPagingSize, false, -1, isTransSearch));
				rp.addXML("</biospecimen>");
				rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
			} else {
				// no previous search, show search screen
				rp.clearXML();
				rp.addXML("<biospecimen>"
						+ bcBiospecimen.getSearchCriteriaXML()
						+ FlagManager.getFlaggedRecordsXML(
								FlagManager.DOMAIN_BIOSPECIMEN, null)
						+ "</biospecimen>");
				rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
			}

			/*
			 * // show search screen rp.clearXML(); if
			 * (checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_SEARCH,
			 * rp.getAuthToken())) { rp.addXML("<biospecimen>");
			 * rp.addXML(bcBiospecimen.getSearchCriteriaXML() +
			 * FlagManager.getFlaggedRecordsXML(FlagManager.DOMAIN_BIOSPECIMEN,
			 * null)); rp.addXML("</biospecimen>"); } else {
			 * rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED); }
			 * 
			 * rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_SEARCH);
			 */

		} else if (strModule.equalsIgnoreCase("BATCH_CREATION")) {
			if (bcreatManager == null)
				bcreatManager = new BatchCreationManager(rp);

			bcreatManager.processRuntimeData(runtimeData);
			// XML and stylesheet has already been set in the rp.
		} else if (strModule.equalsIgnoreCase("CORE")) {
			// System.out.println("Core Module called");

			String strAction = runtimeData.getParameter("action");

			// any re-arrangement of the action to be called goes here.
			if (strAction == null && runtimeData.getParameter("save") != null) {
				strAction = "save_biospecimen";
			}

			if (strAction == null) {
				// System.out.println("Warning action null");
			} else if (strAction.equalsIgnoreCase("view_biospecimen")) {
				int intBiospecimenKey = -1;

				try {
					intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
				} catch (Exception ne) {
					ne.printStackTrace();
				}

				if (intBiospecimenKey != -1) {

					// Lock the biospecimen

					boolean blGotLock = false;
					try {
						if (lckBiospecimen.isValid())
							lckBiospecimen.unlock();
						lckBiospecimen = new LockRequest(rp.getAuthToken());
						lckBiospecimen.addLock("BIOSPECIMEN", intBiospecimenKey
								+ "", LockRecord.READ_WRITE);
						lckBiospecimen.lockDelayWrite();
						blGotLock = true;
					} catch (Exception le) {
						// System.out.println
						// ("Warning : Unable to acquire read only lock on biospecimen "
						// + intBiospecimenKey);
						le.printStackTrace();

					}

					rp.clearXML();

					if (checkPermission(
							BiospecimenCore.ACTION_BIOSPECIMEN_VIEW, rp
									.getAuthToken())
							&& rp.getAuthToken().hasRecordAccess(0,
									intBiospecimenKey)) {
						rp.addXML("<biospecimen>");
						if (blGotLock) {
							String bioXML = bcBiospecimen
									.buildViewBiospecimenXML(intBiospecimenKey,
											runtimeData);
							if (bioXML == null) {
								System.err
										.println("Biospecimen data returned is null");
								// TODO: Something needs to be neater here -
								// this is where ugly empty forms show up.
							} else
								rp.addXML(bioXML);
						} else
							rp
									.setErrorMessage("Unable to acquire a view lock on this specimen");
						rp.addXML(FlagManager.getFlaggedRecordsXML(
								FlagManager.DOMAIN_BIOSPECIMEN, null)
								+ "</biospecimen>");
					} else {
						System.err
								.println("Do not have permission for this activity. doh");
						rp
								.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED_BIOSPECIMEN);
					}

					rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
					// Perform the delete calculation
					String subAction = runtimeData.getParameter("subAction");
					if (subAction != null
							&& subAction
									.compareToIgnoreCase("deleteVialCalculation") == 0) {
						// /Tom start delete vialCalculation

						int intBiospecimenToDelete = -1;

						try {
							intBiospecimenToDelete = Integer
									.parseInt(runtimeData
											.getParameter("BIOSPECIMEN_intBiospecimenID"));
						} catch (Exception de) {
							de.printStackTrace();
						}

						String strReturnValue = null;

						if (checkPermission(
								BiospecimenCore.ACTION_BIOSPECIMEN_DELETE, rp
										.getAuthToken())) {
							if (intBiospecimenToDelete != -1) {
								// Lock the biospecimen
								if (lckBiospecimen.isValid()) {
									boolean blLockAcquired = true;

									try {
										blLockAcquired = lckBiospecimen
												.lockWrites();
									} catch (Exception le) {
										blLockAcquired = false;
									}

									if (blLockAcquired == true) {
										strReturnValue = bcBiospecimen
												.deleteVialCalculation(
														intBiospecimenToDelete,
														runtimeData);
									} else {
										// System.out.println
										// ("Unable to get a lock on this biospecimen");
										strReturnValue = "Another user is viewing this record.  Please try again later.";
									}
								} else {
									strReturnValue = "Your lock on this record has timed out.";
								}

							}
						} else {
							strReturnValue = ERROR_MESSAGE_PERMISSION_DENIED;
						}

						if (strReturnValue != null
								|| intBiospecimenToDelete == -1)

						{
							// System.out.println("Inside View Biospecimen after failure");
							rp.clearXML();
							if (strReturnValue == null)
								rp
										.setErrorMessage("Unable to delete at this time.");
							else
								rp.setErrorMessage(strReturnValue);

							rp.addXML("<biospecimen>"
									+ bcBiospecimen
											.buildViewBiospecimenXML(
													intBiospecimenToDelete,
													runtimeData)
									+ FlagManager.getFlaggedRecordsXML(
											FlagManager.DOMAIN_BIOSPECIMEN,
											null) + "</biospecimen>");

							rp
									.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);

						} else {

							setSearchRuntimeProperties();
						}

						// /Tom stop delete vialCalculation
					}
				}
			} else if (strAction.equalsIgnoreCase("refresh_view_biospecimen")) {
				// when the type is changed

				int intBiospecimenKey = -1;

				try {
					intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
				} catch (Exception ne) {
					ne.printStackTrace();
				}

				if (intBiospecimenKey != -1) {
					try {
						if (lckBiospecimen.isValid() == false) {
							// attempt to renew the lock
							try {
								// System.out.println
								// ("Adding a new lock on the refresh!");
								lckBiospecimen.unlock();
								lckBiospecimen = new LockRequest(rp
										.getAuthToken());
								lckBiospecimen.addLock("BIOSPECIMEN",
										intBiospecimenKey + "",
										LockRecord.READ_WRITE);
								lckBiospecimen.lockDelayWrite();
							} catch (Exception le) {
								// System.out.println
								// ("There was an error renewing the lock");
								le.printStackTrace();
							}

						}
						String strCurrentSubType = runtimeData
								.getParameter("BIOSPECIMEN_strSampleSubType");
						bcBiospecimen.setIsRefresh(true);
						// set it to null
						runtimeData.setParameter(
								"BIOSPECIMEN_strSampleSubType", (String) null);

						rp.clearXML();
						if (checkPermission(
								BiospecimenCore.ACTION_BIOSPECIMEN_VIEW, rp
										.getAuthToken())) {
							rp.addXML("<biospecimen>");
							rp.addXML(bcBiospecimen.buildViewBiospecimenXML(
									intBiospecimenKey, runtimeData));
							// add the sub type section accordingly
							if ((runtimeData.getParameter("keepSubType") != null)
									&& (!runtimeData
											.getParameter("keepSubType")
											.equalsIgnoreCase("true"))) {
								rp
										.addXML(QueryChannel
												.buildLOVXMLFromParent(
														"BIOSPECIMEN_strSampleSubType",
														"",
														runtimeData
																.getParameter("BIOSPECIMEN_strSampleType")
																+ ""));

								String strFirstSubType = QueryChannel
										.getFirstItemInLOVs(
												"BIOSPECIMEN_strSampleSubType",
												runtimeData
														.getParameter("BIOSPECIMEN_strSampleType")
														+ "");
								if (BiospecimenCore.blUseSubTypeLR) {
									if (strFirstSubType != null) {
										rp
												.addXML(QueryChannel
														.buildLOVXMLFromParent(
																"BIOSPECIMEN_strSubTypeLR",
																"",
																strFirstSubType));
									}
								}
							} else {
								rp
										.addXML(QueryChannel
												.buildLOVXMLFromParent(
														"BIOSPECIMEN_strSampleSubType",
														strCurrentSubType,
														runtimeData
																.getParameter("BIOSPECIMEN_strSampleType")
																+ ""));
								if (BiospecimenCore.blUseSubTypeLR) {
									rp.addXML(QueryChannel
											.buildLOVXMLFromParent(
													"BIOSPECIMEN_strSubTypeLR",
													"", strCurrentSubType));
								}
							}

							rp.addXML(FlagManager.getFlaggedRecordsXML(
									FlagManager.DOMAIN_BIOSPECIMEN, null)
									+ "</biospecimen>");
						} else {
							rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
						}
						rp
								.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
						bcBiospecimen.setIsRefresh(false);
					} catch (Exception en) {
						System.err
								.println("[CPatients] Error when build refresh view");
						en.printStackTrace();
					}
				}

			} else if (strAction.equalsIgnoreCase("save_biospecimen")) {

				String strCurrent = runtimeData.getParameter("current");
				if (strCurrent == null)
					strCurrent = "";

				int intBiospecimenKey = -1;
				boolean addingMultiples = false;
				try {

					String strResult = null;

					if (checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_ADD,
							rp.getAuthToken())) {

						try {
							int multiple = 1;
							boolean blLockSet = true;
							String value[] = null;
							int valueLength = 0;

							String strInternalBiospecimenID = runtimeData
									.getParameter("BIOSPECIMEN_intBiospecimenID");
							if (strInternalBiospecimenID != null) {

								if (lckBiospecimen.lockWrites() == false) {
									// System.out.println
									// ("[Biospecimen Save] - Unable to lock writes");
									blLockSet = false;
								}

							} else {
								value = runtimeData
										.getParameterValues("BIOSPECIMEN_strSampleType");
								if (value != null) {
									System.err.println("GOT MULTI : "
											+ value.length);
									valueLength = value.length;
								} else
									valueLength = 0;
								if (runtimeData
										.getParameter("BIOSPECIMEN_intMultiple") != null) {
									Integer mult = new Integer(
											runtimeData
													.getParameter("BIOSPECIMEN_intMultiple"));
									if (mult != null) {
										multiple = mult.intValue();
									}
								}
							}

							// Multiple bio add - careful!

							String strIntBiospecimenID = runtimeData
									.getParameter("BIOSPECIMEN_intBiospecimenID");

							if (blLockSet == true) {

								int intPatientID = new Integer(
										runtimeData
												.getParameter("BIOSPECIMEN_intPatientID"))
										.intValue();
								String strEncounter = runtimeData
										.getParameter("BIOSPECIMEN_strEncounter");
								if (strIntBiospecimenID == null) {
									if (valueLength == 0) {
										strResult = "Please select a sample type.";
									} else {
										if (multiple < 10) {
											blAddedMulti = value.length
													* multiple;

											for (int i = 0; i < value.length; i++) {
												runtimeData
														.setParameter(
																"BIOSPECIMEN_strSampleType",
																value[i]);
												for (int j = 0; j < multiple; j++) {

													// System.err.println("** LAST SUFFIX: "
													// +
													// BiospecimenUtilities.getLastBioAddedSuffix(
													// intPatientID,
													// strEncounter,
													// strSampleType));

													// if (j==0) //ie the first.
													strResult = bcBiospecimen
															.saveBiospecimen2(runtimeData);
													// else
													// bcBiospecimen.cloneBiospecimen2(bcBiospecimen.getLastBiospecimenAddedID());
												}

											}
											lckBiospecimen.unlockWrites();
										} else
											strResult = "Only ten copies are allowed.";
									}
								} else {

									strResult = bcBiospecimen
											.saveBiospecimen2(runtimeData);
									lckBiospecimen.unlockWrites();
								}
							} else {
								strResult = "There was an error locking the record for updates";
							}

						} catch (Exception le) {
							strResult = "There was an error locking the record";
							// System.out.println
							// ("There was an error doing the locking");
							le.printStackTrace();
						}
					} else {
						strResult = ERROR_MESSAGE_PERMISSION_DENIED;
					}

					rp.clearXML();

					if (strResult == null) // display the biospecimen
					{

						// lock up the new specimen

						if (lckBiospecimen.isValid())
							lckBiospecimen.unlock();
						lckBiospecimen = new LockRequest(rp.getAuthToken());
						lckBiospecimen.addLock("BIOSPECIMEN", bcBiospecimen
								.getLastBiospecimenAddedID()
								+ "", LockRecord.READ_WRITE);
						lckBiospecimen.lockDelayWrite();

						rp.addXML("<biospecimen>"
								+ bcBiospecimen.buildViewBiospecimenXML(
										bcBiospecimen
												.getLastBiospecimenAddedID(),
										runtimeData)
								+ FlagManager.getFlaggedRecordsXML(
										FlagManager.DOMAIN_BIOSPECIMEN, null)
								+ "</biospecimen>");
						if (blAddedMulti > 1) {
							rp.setErrorMessage(blAddedMulti
									+ " biospecimens saved");
						} else
							rp.setErrorMessage("Biospecimen saved");
						rp
								.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
					} else {
						rp.setErrorMessage(strResult);
						rp.clearXML();
						rp.addXML("<biospecimen>");
						if (strCurrent.equalsIgnoreCase("biospecimen_view")) {

							int intTempBiospecimenID = Integer
									.parseInt(runtimeData
											.getParameter("BIOSPECIMEN_intBiospecimenID"));
							rp.addXML(bcBiospecimen.buildViewBiospecimenXML(
									intTempBiospecimenID, runtimeData)
									+ FlagManager.getFlaggedRecordsXML(
											FlagManager.DOMAIN_BIOSPECIMEN,
											null));
							rp
									.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);

						}

						else {

							rp
									.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_ADD);
							rp.addXML(bcBiospecimen.buildAddBiospecimenXML(
									null, null, runtimeData));
						}
						rp.addXML("</biospecimen>");

					}

				} catch (Exception en) {
					System.err.println("Error when saving biopscimen");

				}

			} else if (strAction.equalsIgnoreCase("add_biospecimen")) {

				String strInternalPatientKey = runtimeData
						.getParameter("BIOSPECIMEN_intPatientID");
				String strInternalBiospecimenParentKey = runtimeData
						.getParameter("BIOSPECIMEN_intParentID");

				try {
					if (strInternalBiospecimenParentKey != null) {
						if (lckBiospecimen == null) {
							lckBiospecimen = new LockRequest(rp.getAuthToken());
						}
						// lock the parent
						lckBiospecimen.emptyAllLocks(rp.getAuthToken()
								.getSessionUniqueID());
						lckBiospecimen.addLock("BIOSPECIMEN",
								strInternalBiospecimenParentKey,
								LockRecord.READ_ONLY);

					}

					if (strInternalPatientKey != null) {
						if (lckPatient == null) {
							lckPatient = new LockRequest(rp.getAuthToken());

						}

						// clear out any existing patient locks this user has
						lckPatient.emptyAllLocks(rp.getAuthToken()
								.getSessionUniqueID());

						lckPatient.addLock("PATIENT", strInternalPatientKey,
								LockRecord.READ_ONLY);

					}
				} catch (Exception le) {
					// System.out.println ("Unable to acquire locks");
					le.printStackTrace();
				}

				// System.out.println("strInternalBiospecParent: " +
				// strInternalBiospecimenParentKey);
				int intPatientKey = 0;

				rp.clearXML();
				if (checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_ADD, rp
						.getAuthToken())) {
					rp.addXML("<biospecimen>");
					rp.addXML(bcBiospecimen.buildAddBiospecimenXML(
							strInternalPatientKey,
							strInternalBiospecimenParentKey, runtimeData));
					rp.addXML("</biospecimen>");
				} else {
					rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
				}
				rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_ADD);

			}

			else if (strAction.equalsIgnoreCase("delete_biospecimen")) {
				int intBiospecimenToDelete = -1;
				int intParentKey = -1;

				try {
					intBiospecimenToDelete = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
					intParentKey = BiospecimenUtilities
							.getBiospecimenParentKey(intBiospecimenToDelete);
				} catch (Exception de) {
					de.printStackTrace();
				}

				String strReturnValue = null;

				if (checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_DELETE,
						rp.getAuthToken())) {
					if (intBiospecimenToDelete != -1) {
						// Lock the biospecimen
						if (lckBiospecimen.isValid()) {
							boolean blLockAcquired = true;

							try {
								blLockAcquired = lckBiospecimen.lockWrites();
							} catch (Exception le) {
								blLockAcquired = false;
							}

							if (blLockAcquired == true) {
								strReturnValue = bcBiospecimen
										.deleteBiospecimen(intBiospecimenToDelete);
							} else {
								// System.out.println
								// ("Unable to get a lock on this biospecimen");
								strReturnValue = "Another user is viewing this record.  Please try again later.";
							}
						} else {
							strReturnValue = "Your lock on this record has timed out.";
						}

					}
				} else {
					strReturnValue = ERROR_MESSAGE_PERMISSION_DENIED;
				}

				if (strReturnValue != null || intBiospecimenToDelete == -1)

				{
					// System.out.println("Inside View Biospecimen after failure");
					rp.clearXML();
					if (strReturnValue == null)
						rp.setErrorMessage("Unable to delete at this time.");
					else
						rp.setErrorMessage(strReturnValue);

					rp.addXML("<biospecimen>"
							+ bcBiospecimen.buildViewBiospecimenXML(
									intBiospecimenToDelete, runtimeData)
							+ FlagManager.getFlaggedRecordsXML(
									FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");

					rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);

				} else {
					if (intParentKey != -1) {
						// System.out.println("Inside View Biospecimen after failure");
						rp.clearXML();

						rp.addXML("<biospecimen>"
								+ bcBiospecimen.buildViewBiospecimenXML(
										intParentKey, runtimeData)
								+ FlagManager.getFlaggedRecordsXML(
										FlagManager.DOMAIN_BIOSPECIMEN, null)
								+ "</biospecimen>");

						rp
								.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
					} else {
						// setSearchRuntimeProperties();
						if (htCurrentSearchCriteria != null) {
							boolean isTransSearch = false;
							if (intLastDomainSearch == BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC)
								isTransSearch = true;

							rp.clearXML();
							rp.addXML("<biospecimen>");
							rp.addXML(bcBiospecimen.getSearchCriteriaXML());
							rp.addXML(bcBiospecimen
									.getBiospecimenSearchResultsXML(
											intLastDomainSearch,
											htCurrentSearchCriteria, true, 0,
											this.intCurrentPagingSize, false,
											-1, isTransSearch));
							rp.addXML("</biospecimen>");
							rp
									.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
						} else {
							// no previous search, show search screen
							rp.clearXML();
							rp.addXML("<biospecimen>"
									+ bcBiospecimen.getSearchCriteriaXML()
									+ FlagManager.getFlaggedRecordsXML(
											FlagManager.DOMAIN_BIOSPECIMEN,
											null) + "</biospecimen>");
							rp
									.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
						}

					}
				}
			} else if (strAction.equalsIgnoreCase("save_quantity")) {
				// System.out.println("About to save a new quantity");
				String result = null;
				try {
					int intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));

					if (intBiospecimenKey != -1) {
						if (lckBiospecimen.isValid()) {
							if (!lckBiospecimen.lockWrites()) {
								result = "Another user is currently viewing this biospecimen";
							}
						} else {
							result = "Your lock on this Biospecimen has expired";
						}
						int sitekey = InventoryUtilities
								.getSiteKeyforBiospecimen(new Integer(
										intBiospecimenKey).intValue());
						Integer ISitekey = new Integer(sitekey);
						if (!rp.getAuthToken().getSiteList().contains(ISitekey)
								&& sitekey != -1) {
							result = "You cannot edit this biospecimen as it is currently off-site.";
						}
					}

					// save the transaction
					if (result == null) // locks have been acquired
					{
						runtimeData.setParameter(
								"BIOSPECIMEN_TRANSACTIONS_strType", "Manual");
						result = bcBiospecimen.doSaveQuantity(intBiospecimenKey
								+ "", runtimeData);
						lckBiospecimen.unlock();
						lckBiospecimen = new LockRequest(rp.getAuthToken());
						lckBiospecimen.addLock("BIOSPECIMEN", intBiospecimenKey
								+ "", LockRecord.READ_WRITE);
						lckBiospecimen.lockDelayWrite();

					}
					if (result != null) {
						// System.out.println("Error Message" + result);
						rp.setErrorMessage(result);
					}
					// rebuild the view
					rp.clearXML();
					rp.addXML("<biospecimen>"
							+ bcBiospecimen.buildViewBiospecimenXML(
									intBiospecimenKey, runtimeData)
							+ FlagManager.getFlaggedRecordsXML(
									FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");
					// System.out.println("Successfully save a transaction");

				} catch (Exception ne) {
					ne.printStackTrace();
				}

			} else if (strAction.equalsIgnoreCase("delete_quantity")) {
				// System.out.println("About to delete a transaction");
				int intBiospecimenTransactionID = -1;
				int intBiospecimenKey = -1;
				try {
					intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
					intBiospecimenTransactionID = Integer
							.parseInt(runtimeData
									.getParameter("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID"));
				} catch (Exception ne) {
					ne.printStackTrace();
				}

				if (intBiospecimenTransactionID != -1
						&& intBiospecimenKey != -1) {
					// Acquire some locks -- everyone loves locks

					String strResult = null;

					if (lckBiospecimen.isValid()) {

						boolean blLockAcquired = true;

						try {
							blLockAcquired = lckBiospecimen.lockWrites();
						} catch (neuragenix.common.UnsupportedLockException le) {
							// System.out.println
							// ("Unable to acquire a lock on the biospecimen");
							le.printStackTrace();
							blLockAcquired = false;
						}

						if (blLockAcquired == false) {
							strResult = "Unable to acquire lock on biospecimen";
						} else {
							if (checkPermission(
									BiospecimenCore.ACTION_BIOSPECIMEN_DELETE,
									rp.getAuthToken())) {
								strResult = bcBiospecimen.doDeleteTransaction(
										intBiospecimenKey,
										intBiospecimenTransactionID);
							} else {
								strResult = ERROR_MESSAGE_PERMISSION_DENIED;
							}

							try {
								lckBiospecimen.unlockWrites();
							} catch (neuragenix.common.UnsupportedLockException le) {
								// System.out.println
								// ("Unable to remove lock!");
								le.printStackTrace();
							}

						}
					}

					if (strResult != null)
						rp.setErrorMessage(strResult);
					rp.clearXML();
					rp.addXML("<biospecimen>"
							+ bcBiospecimen.buildViewBiospecimenXML(
									intBiospecimenKey, runtimeData)
							+ FlagManager.getFlaggedRecordsXML(
									FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");
					// System.out.println("Successfully deleted a transaction");

				}

			} else if (strAction.equalsIgnoreCase("upgrade_quantity")) {
				// System.out.println("upgrading a transaction");
				int intBiospecimenTransactionID = -1;
				int intBiospecimenKey = -1;
				try {
					intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
					intBiospecimenTransactionID = Integer
							.parseInt(runtimeData
									.getParameter("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID"));
				} catch (Exception ne) {
					rp
							.setErrorMessage("Can not upgrade a transaction because transaction ID or biospecimenID is invalid");
					ne.printStackTrace();
				}
				if (intBiospecimenTransactionID != -1
						&& intBiospecimenKey != -1) {

					// Acquire the lock

					String strResult = null;

					if (lckBiospecimen.isValid()) {
						try {
							if (lckBiospecimen.lockWrites()) {
								if (checkPermission(
										BiospecimenCore.PERMISSION_DELIVER_QUANTITY,
										rp.getAuthToken())) {
									strResult = bcBiospecimen
											.doTransactionUpgrade(intBiospecimenTransactionID);
								} else {
									strResult = ERROR_MESSAGE_PERMISSION_DENIED;
								}
								lckBiospecimen.unlockWrites();
							} else {
								strResult = "Unable to upgrade - another user is viewing this biospecimen";
							}
						} catch (neuragenix.common.UnsupportedLockException le) {
							strResult = "Unable to acquire locks";
							// System.out.println
							// ("Locking exception has occured");
							le.printStackTrace();
						}

					}

					// do the upgrade

					if (strResult != null)
						rp.setErrorMessage(strResult);

					rp.clearXML();
					rp.addXML("<biospecimen>"
							+ bcBiospecimen.buildViewBiospecimenXML(
									intBiospecimenKey, runtimeData)
							+ FlagManager.getFlaggedRecordsXML(
									FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");
					// System.out.println("Successfully upgraded a transaction");

				}

			}

			else if (strAction.equalsIgnoreCase("clone_biospecimen")) {

				// -- Shouldn't need to lock here, as we should already have a
				// read lock on the biospecimen

				int intBiospecimenKey = -1;
				int intStudyKey = -1;
				try {
					intBiospecimenKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intBiospecimenID"));
					intStudyKey = Integer.parseInt(runtimeData
							.getParameter("BIOSPECIMEN_intStudyKey"));
				} catch (Exception ne) {
					System.err
							.println("[CBiospecimen] Can not parse Bio spec ID or study key");
					ne.printStackTrace();
				}

				if (intBiospecimenKey != -1 && intStudyKey != -1) {
					String strResult = null;
					if (checkPermission(
							BiospecimenCore.ACTION_BIOSPECIMEN_CLONE, rp
									.getAuthToken())) {
						strResult = bcBiospecimen.cloneBiospecimen2(
								intBiospecimenKey, runtimeData);
					} else {
						strResult = ERROR_MESSAGE_PERMISSION_DENIED;
					}

					// lock the new record

					if (strResult == null) {
						try {
							lckBiospecimen.unlock();
							lckBiospecimen = new LockRequest(rp.getAuthToken());
							lckBiospecimen.addLock("BIOSPECIMEN", bcBiospecimen
									.getLastBiospecimenAddedID()
									+ "", LockRecord.READ_WRITE);
							lckBiospecimen.lockDelayWrite();
						} catch (neuragenix.common.UnsupportedLockException le) {
							// System.out.println
							// ("There was an error doing the locking");
							le.printStackTrace();
						}
					}

					// XXX: This strikes me as a bug if the clone fails
					// it shouldnt attempt to load the biospecimen if the clone
					// didnt occur

					rp.clearXML();
					if (strResult != null)
						rp.setErrorMessage(strResult);

					rp.addXML("<biospecimen>");
					if (strResult != null) {
						rp.addXML(bcBiospecimen.buildViewBiospecimenXML(
								bcBiospecimen.getLastViewedBiospecimenID(),
								runtimeData));
					} else {
						rp.addXML(bcBiospecimen.buildViewBiospecimenXML(
								bcBiospecimen.getLastBiospecimenAddedID(),
								runtimeData));
					}

					rp.addXML(FlagManager.getFlaggedRecordsXML(
							FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");
				} else {
					System.err
							.println("Can not clone because Study Key or Biospecimen Key is invalid");
					String strErrors = "Can not clone because Study Key or Biospecimen Key is invalid";
					rp.clearXML();
					rp.setErrorMessage(strErrors);
				}

			}
			// core covers search//view//add//single clone//add sub specimen

			// pass the runtime data to the core handler
		} else if (strModule.equalsIgnoreCase("BATCH_SFRESULTS_GENERATION")) {
			// if
			// (checkPermission(BatchSFResultsManager.PERMISSION_BATCH_SFRESULTS,
			// rp.getAuthToken()))

			if (mgrSFBatchGen == null)
				mgrSFBatchGen = new BatchSFResultsManager(rp);

			rp = mgrSFBatchGen.processRuntimeData(runtimeData);

		} else if (strModule.equalsIgnoreCase("VIAL_CALCULATION")) {
			// delete the job to vc manager, including set XML and Stylesheet
			vcManager.processRuntimeData(runtimeData);
		} else if (strModule.equalsIgnoreCase("ATTACHMENTS")) {
			// System.out.println("Attachment module called");

			int intBiospecimenKey = -1;
			try {
				intBiospecimenKey = Integer.parseInt(runtimeData
						.getParameter("BIOSPECIMEN_intBiospecimenID"));
				// System.out.println("BIOSPECIMEN_id is \""+runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID")+"\"");
			} catch (Exception ne) {
				ne.printStackTrace();
			}

			String strAction = runtimeData.getParameter("action");

			boolean blError = false;
			if (strAction != null
					&& strAction.equalsIgnoreCase("add_attachment")) {
				String strResult = null;

				if (checkPermission(
						AttachmentsAndNotes.PERMISSION_BIOSPECIMEN_ATTACHMENTS_ADD,
						rp.getAuthToken())) {
					strResult = attAttachments.saveAttachment(runtimeData);
				} else {
					strResult = ERROR_MESSAGE_PERMISSION_DENIED;
					rp
							.addXML("<biospecimenAttachments></biospecimenAttachments>");
					blError = true;
				}

				if (strResult != null)
					rp.setErrorMessage(strResult);
				else
					rp.setErrorMessage("Attachment Added");

			} else if (strAction != null
					&& strAction.equalsIgnoreCase("delete_attachment")) {
				String strResult = null;

				if (checkPermission(
						AttachmentsAndNotes.PERMISSION_BIOSPECIMEN_ATTACHMENTS_DELETE,
						rp.getAuthToken())) {
					strResult = attAttachments.deleteAttachment(runtimeData);
				} else {
					rp
							.addXML("<biospecimenAttachments></biospecimenAttachments>");
					strResult = ERROR_MESSAGE_PERMISSION_DENIED;
					blError = true;
				}
				if (strResult != null)
					rp.setErrorMessage(strResult);
			}

			// build the view as a default action
			rp.clearXML();
			if (checkPermission(
					AttachmentsAndNotes.PERMISSION_BIOSPECIMEN_ATTACHMENTS_VIEW,
					rp.getAuthToken())) {
				rp.addXML("<biospecimenAttachments>"
						+ attAttachments.buildViewAttachmentsXML(runtimeData)
						+ bcBiospecimen.getBiospecimenDetailsXML(
								intBiospecimenKey, runtimeData)
						+ "</biospecimenAttachments>");
			} else {
				rp.addXML("<biospecimenAttachments></biospecimenAttachments>");
				rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
				blError = true;
			}

			// set the style sheet
			if (!blError) {
				// System.out.println("[Attachment and Note] branch1");
				rp.setStylesheet("biospecimen_attachments");
			} else {
				rp.setStylesheet("biospecimen_action_denied");
			}

		}

		// dealing with notes
		else if (strModule.equalsIgnoreCase("NOTES")) {
			// System.out.println("Attachment module called");

			int intBiospecimenKey = -1;
			try {
				intBiospecimenKey = Integer.parseInt(runtimeData
						.getParameter("BIOSPECIMEN_intBiospecimenID"));

			} catch (Exception ne) {
				ne.printStackTrace();
			}

			String strAction = runtimeData.getParameter("action");

			boolean blError = false;

			if (strAction != null && strAction.equalsIgnoreCase("add_notes")) {
				String strResult = null;

				if (checkPermission(
						AttachmentsAndNotes.PERMISSION_BIOSPECIMEN_NOTES_ADD,
						rp.getAuthToken())) {
					strResult = attAttachments.saveNote(runtimeData);
				} else {
					strResult = ERROR_MESSAGE_PERMISSION_DENIED;

					blError = true;
				}

				if (strResult != null)
					rp.setErrorMessage(strResult);
				else
					rp.setErrorMessage("Notes Added");

			} else if (strAction != null
					&& strAction.equalsIgnoreCase("delete_notes")) {
				String strResult = null;

				if (checkPermission(
						AttachmentsAndNotes.PERMISSION_BIOSPECIMEN_NOTES_DELETE,
						rp.getAuthToken())) {
					strResult = attAttachments.deleteNote(runtimeData);
				} else {

					strResult = ERROR_MESSAGE_PERMISSION_DENIED;
					blError = true;
				}
				if (strResult != null)
					rp.setErrorMessage(strResult);
			}

			// build the view as a default action
			rp.clearXML();

			if (intBiospecimenKey != -1) {

				// Lock the biospecimen
				boolean blGotLock = false;
				try {
					if (lckBiospecimen.isValid())
						lckBiospecimen.unlock();
					lckBiospecimen = new LockRequest(rp.getAuthToken());
					lckBiospecimen.addLock("BIOSPECIMEN", intBiospecimenKey
							+ "", LockRecord.READ_WRITE);
					lckBiospecimen.lockDelayWrite();
					blGotLock = true;
				} catch (Exception le) {
					// System.out.println
					// ("Warning : Unable to acquire read only lock on biospecimen "
					// + intBiospecimenKey);
					le.printStackTrace();

				}

				if (checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW, rp
						.getAuthToken())) {
					rp.addXML("<biospecimen>");
					if (blGotLock) {
						rp.addXML(bcBiospecimen.buildViewBiospecimenXML(
								intBiospecimenKey, runtimeData));
					} else
						rp
								.setErrorMessage("Unable to acquire a view lock on this specimen");
					rp.addXML(FlagManager.getFlaggedRecordsXML(
							FlagManager.DOMAIN_BIOSPECIMEN, null)
							+ "</biospecimen>");
				} else {
					rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
				}

				rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);

			}

		}

		else if (strModule.equalsIgnoreCase("BIOSPECIMEN_SEARCH")) {
			// System.out.println("\n Biospecimen Search handler activated");

			String strSearchDomain = null;
			if (runtimeData.getParameter("search_domain") != null) {
				strSearchDomain = runtimeData.getParameter("search_domain");
			}

			String strAction = runtimeData.getParameter("action");

			if (!checkPermission(BiospecimenCore.ACTION_BIOSPECIMEN_SEARCH, rp
					.getAuthToken())) {
				strAction = "NO_PERMISSION";
			}

			else if (strAction != null) {
				if (strAction.equals("open_node")) {
					int intExpandID = Integer.parseInt(runtimeData
							.getParameter("expandID"));
					if (htCurrentSearchCriteria != null) // not that it should
															// be
					{
						String strCurrentSearchPage = runtimeData
								.getParameter("PAGING_currentPage");
						int intPageToDisplay = 0;

						if (strCurrentSearchPage == null) {
							// System.out.println("Was unable to retrieve the current page");
							rp
									.setErrorMessage("Unable to retreive current page.  Can not move pages");
						} else {
							intPageToDisplay = Integer
									.parseInt(strCurrentSearchPage);
						}

						rp.clearXML();
						rp.addXML("<biospecimen>");
						if (intLastDomainSearch == BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC) {
							rp.addXML(bcBiospecimen
									.getBiospecimenSearchResultsXML(
											intLastDomainSearch,
											htCurrentSearchCriteria, false,
											intPageToDisplay,
											this.intCurrentPagingSize, false,
											intExpandID, true));
						} else {
							rp.addXML(bcBiospecimen
									.getBiospecimenSearchResultsXML(
											intLastDomainSearch,
											htCurrentSearchCriteria, false,
											intPageToDisplay,
											this.intCurrentPagingSize, false,
											intExpandID));
						}
						rp.addXML(bcBiospecimen.getSearchCriteriaXML());
						rp.addXML("</biospecimen>");
						rp
								.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
						// System.out.println ("good for open node display");
					} else {
						// System.out.println("search criteria is null");
					}
				}
				if (strAction.equals("close_node")) {
					int intExpandID = -1;
					if (htCurrentSearchCriteria != null) // not that it should
															// be
					{
						String strCurrentSearchPage = runtimeData
								.getParameter("PAGING_currentPage");
						int intPageToDisplay = 0;

						if (strCurrentSearchPage == null) {
							// System.out.println("Was unable to retrieve the current page");
							rp
									.setErrorMessage("Unable to retreive current page.  Can not move pages");
						} else {
							intPageToDisplay = Integer
									.parseInt(strCurrentSearchPage);
						}

						rp.clearXML();
						rp.addXML("<biospecimen>");
						if (intLastDomainSearch == BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC) {
							rp.addXML(bcBiospecimen
									.getBiospecimenSearchResultsXML(
											intLastDomainSearch,
											htCurrentSearchCriteria, false,
											intPageToDisplay,
											this.intCurrentPagingSize, false,
											intExpandID, true));
						} else {
							rp.addXML(bcBiospecimen
									.getBiospecimenSearchResultsXML(
											intLastDomainSearch,
											htCurrentSearchCriteria, false,
											intPageToDisplay,
											this.intCurrentPagingSize, false,
											intExpandID));
						}
						rp.addXML(bcBiospecimen.getSearchCriteriaXML());
						rp.addXML("</biospecimen>");
						rp
								.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
						// System.out.println ("good for open node display");
					} else {
						// System.out.println("search criteria is null");
					}
				} else if (strAction.equals("patient_entry")) {
					// Called when coming from the patient channel into the
					// biospecimen channel for doing things such as adding, etc
					// System.out.println("Patient Entry Point has been activated");
					this.intLastDomainSearch = BiospecimenCore.DOMAIN_PATIENT;
					htCurrentSearchCriteria = null;
					htCurrentSearchCriteria = new Hashtable();

					// get the patient id we're interested in and set it.

					String strInternalPatientKey = runtimeData
							.getParameter("intInternalPatientID");

					boolean hasCollection = true;
					boolean blCollectionTest = false;
					if (strInternalPatientKey != null) {
						htCurrentSearchCriteria.put("BIOSPECIMEN_intPatientID",
								strInternalPatientKey);

						try {
							blCollectionTest = PropertiesManager
									.getPropertyAsBoolean("neuragenix.bio.Biospecimen.requireCollection");
						} catch (Exception e) {
							e.printStackTrace();
						}

						hasCollection = PatientUtilities
								.checkForAdmissions(Integer
										.parseInt(strInternalPatientKey));

					}
					rp.clearXML();
					rp.addXML("<biospecimen>");
					if (blCollectionTest == true) {
						rp.addXML("<hasCollection>" + hasCollection
								+ "</hasCollection>");
					}
					rp.addXML(bcBiospecimen.getSearchCriteriaXML());
				
					rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
							BiospecimenCore.DOMAIN_PATIENT,
							htCurrentSearchCriteria, true, 0,
							this.intCurrentPagingSize, false, -1));
					rp.addXML("</biospecimen>");
					rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
					// System.out.println ("good for patient entry");

				} else if (strAction.equals("studyalloc")) {
					this.intLastDomainSearch = BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC;
					htCurrentSearchCriteria = null;
					htCurrentSearchCriteria = new Hashtable();
					if (runtimeData.getParameter("BIOSPECIMEN_strSampleType") != null)
						htCurrentSearchCriteria
								.put(
										"BIOSPECIMEN_strSampleType",
										runtimeData
												.getParameter("BIOSPECIMEN_strSampleType"));
					if (runtimeData
							.getParameter("BIOSPECIMEN_strSampleSubType") != null
							&& ((String) runtimeData
									.getParameter("BIOSPECIMEN_strSampleSubType"))
									.trim().equalsIgnoreCase("") != true) {
						htCurrentSearchCriteria
								.put(
										"BIOSPECIMEN_strSampleSubType",
										runtimeData
												.getParameter("BIOSPECIMEN_strSampleSubType"));
					}
					if (runtimeData.getParameter("BIOSPECIMEN_intStudyID") != null)
						htCurrentSearchCriteria
								.put(
										"BIOSPECIMEN_TRANSACTIONS_intStudyKey",
										runtimeData
												.getParameter("BIOSPECIMEN_intStudyID"));
					if (runtimeData
							.getParameter("BIOSPECIMEN_TRANSACTIONS_searchType") != null)
						htCurrentSearchCriteria
								.put(
										"BIOSPECIMEN_TRANSACTIONS_searchType",
										runtimeData
												.getParameter("BIOSPECIMEN_TRANSACTIONS_searchType"));

					rp.clearXML();
					rp.addXML("<biospecimen>");
					rp.addXML(bcBiospecimen.getSearchCriteriaXML());
					rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
							BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC,
							htCurrentSearchCriteria, true, 0,
							this.intCurrentPagingSize, false, -1, true));
					rp.addXML("<intStudyID>"
							+ runtimeData
									.getParameter("BIOSPECIMEN_intStudyID")
							+ "</intStudyID>");
					rp.addXML("</biospecimen>");

					rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);

				} else if (strAction.equals("PAGING_nextPage")) {

					String strCurrentSearchPage = runtimeData
							.getParameter("PAGING_currentPage");
					int intPageToDisplay = 0;

					if (strCurrentSearchPage == null) {
						rp
								.setErrorMessage("Unable to retreive current page.  Can not move pages");
					} else {
						intPageToDisplay = Integer
								.parseInt(strCurrentSearchPage);
						intPageToDisplay++;
					}

					setSearchResultsRuntimeProperties(intLastDomainSearch,
							intPageToDisplay, htCurrentSearchCriteria);

				} else if (strAction.equals("PAGING_previousPage")) {
					String strCurrentSearchPage = runtimeData
							.getParameter("PAGING_currentPage");
					int intPageToDisplay = 0;

					if (strCurrentSearchPage == null) {
						rp
								.setErrorMessage("Unable to retreive current page.  Can not move pages");
					} else {
						intPageToDisplay = Integer
								.parseInt(strCurrentSearchPage);
						intPageToDisplay--;
					}

					setSearchResultsRuntimeProperties(intLastDomainSearch,
							intPageToDisplay, htCurrentSearchCriteria);
				} else if (strAction.equals("PAGING_setPagingSize")) {

					// System.out.println("Set pagingsize  has been called");
					String strNewPagingSize = runtimeData
							.getParameter("PAGING_pagingSize");
					String strCurrentSearchPage = runtimeData
							.getParameter("PAGING_currentPage");

					int intPageToDisplay = 0;

					if (strNewPagingSize != null) {
						intCurrentPagingSize = Integer
								.parseInt(strNewPagingSize);
					} else {
						// System.out.println("was unable to set the paging size!");
						rp.setErrorMessage("Unable to set new paging size");
					}

					intPageToDisplay = Integer.parseInt(strCurrentSearchPage);
					int intOldPagingSize = Integer.parseInt(runtimeData
							.getParameter("PAGING_intOldRecordPerPage"));
					int intOldTotalPages = Integer.parseInt(runtimeData
							.getParameter("PAGING_intOldTotalPages"));

					// this to avoid the new target page overpasses the total
					// page
					// System.out.println(intPageToDisplay + "*" +
					// intCurrentPagingSize+ "=" + intPageToDisplay *
					// intCurrentPagingSize);
					// System.out.println(intOldPagingSize + "*" +
					// intOldTotalPages + "=" + intOldPagingSize *
					// intOldPagingSize );
					if (intPageToDisplay * intCurrentPagingSize > intOldPagingSize
							* intOldTotalPages)
						;
					intPageToDisplay = 0;

					setSearchResultsRuntimeProperties(intLastDomainSearch,
							intPageToDisplay, htCurrentSearchCriteria);

				} else if (strAction.equals("PAGING_setPage")) {
					String strNewSearchPage = runtimeData
							.getParameter("currentPage");

					int intPageToDisplay = 0;

					if (strNewSearchPage == null) {
						rp
								.setErrorMessage("Unable to retreive new page.  Can not move pages");
					} else {
						intPageToDisplay = Integer.parseInt(strNewSearchPage);
						intPageToDisplay--;
					}

					setSearchResultsRuntimeProperties(intLastDomainSearch,
							intPageToDisplay, htCurrentSearchCriteria);
				} else if (strAction.equalsIgnoreCase("redo_last_search")) {

					if (htCurrentSearchCriteria != null) {
						boolean isTransSearch = false;
						if (intLastDomainSearch == BiospecimenCore.DOMAIN_STUDYALLOC_ALLOC)
							isTransSearch = true;

						rp.clearXML();
						rp.addXML("<biospecimen>");
						rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
								intLastDomainSearch, htCurrentSearchCriteria,
								true, 0, this.intCurrentPagingSize, false, -1,
								isTransSearch));
						rp.addXML(bcBiospecimen.getSearchCriteriaXML());
						rp.addXML("</biospecimen>");

						rp
								.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
					} else {
						// no previous search, show search screen
						rp.clearXML();
						rp.addXML("<biospecimen>"
								+ bcBiospecimen.getSearchCriteriaXML()
								+ FlagManager.getFlaggedRecordsXML(
										FlagManager.DOMAIN_BIOSPECIMEN, null)
								+ "</biospecimen>");
						rp
								.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
					}

				} else if (strAction.equals("NO_PERMISSION")) {
					rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
				}
			} else {
				// System.err.println("Got Here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				// get the page requirements from runtime data
				Hashtable htCriteria = new Hashtable();

				Vector vtSearchFields = DatabaseSchema
						.getFormFields("cbiospecimen_biospecimen_search");

				for (int i = 0; i < vtSearchFields.size(); i++) {
					String fieldName = (String) vtSearchFields.get(i);
					QueryChannel.updateDateValuesInRuntimeData(vtSearchFields,
							runtimeData);
					String rtValue = runtimeData.getParameter(fieldName);
				
					if (rtValue != null && (!rtValue.equals(""))) {
						if (fieldName.equals("BIOSPECIMEN_strBiospecimenID"))
							rtValue = rtValue.toUpperCase();
						htCriteria.put(fieldName, rtValue);
					}
				}

				// Save the users search criteria
				htCurrentSearchCriteria = htCriteria;
				this.intLastDomainSearch = BiospecimenCore.DOMAIN_BIOSPECIMEN;
				rp.clearXML();
				long timenow = System.currentTimeMillis();
				System.err.println("Starting query");
				rp.addXML("<biospecimen>");
				rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
						BiospecimenCore.DOMAIN_BIOSPECIMEN, htCriteria, true,
						0, this.intCurrentPagingSize, false, -1));
				long finishedsearch = System.currentTimeMillis();

				//rp.addXML(StudyUtilities.getListOfStudiesXML(rp.getAuthToken(),
				//		true));
				rp.addXML(bcBiospecimen.getSearchCriteriaXML());
				rp.addXML("</biospecimen>");
				long finishedstudylist = System.currentTimeMillis();
				double searchtime = ((double) (finishedsearch - timenow)) / 1000.0;
				double studytime = ((double) (finishedstudylist - finishedsearch)) / 1000.0;
				System.err.println("Search time: " + searchtime
						+ "\nStudy time: " + studytime);
				// System.err.println(rp.getXML());
				rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);

			}

			// send to core module

			// return values to channel.

		} else if (strModule.equalsIgnoreCase("BIOANALYSIS")) {

		} else if (strModule.equalsIgnoreCase("BIOSPECIMEN_SMARTFORMS")) {

		}

		else if (strModule.equalsIgnoreCase("BATCH_CLONE")) {
			// System.out.println("Batch Clone module called");

			if (checkPermission(BatchCloneManager.PERMISSION_BATCH_CLONE, rp
					.getAuthToken())) {
				if (mgrBatchClone == null)
					mgrBatchClone = new BatchCloneManager(rp);
				rp = mgrBatchClone.processRuntimeData(runtimeData);

			} else {
				rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
			}
		} else if (strModule.equalsIgnoreCase("BATCH_ALLOCATE")) {
			if (checkPermission(BatchQuantityManager.PERMISSION_BATCH_QUANTITY,
					rp.getAuthToken())) {
				if (mgrBatchAllocation == null) {
					mgrBatchAllocation = new BatchAllocation(rp);
				}
				rp = mgrBatchAllocation.processRuntimeData(runtimeData);
				// System.err.println(rp.getXML());
			} else {
				rp.setErrorMessage(ERROR_MESSAGE_PERMISSION_DENIED);
			}

		} else if (strModule.equalsIgnoreCase("ALLOCATE_CELL")) {
			String strAction = runtimeData.getParameter("action");
			int intBiospecimenID = Integer.parseInt(runtimeData
					.getParameter("BIOSPECIMEN_intBiospecimenID"));
			// allocate a new cell

			boolean blLockSet = true;
			try {
				if (lckBiospecimen.lockWrites() == false) {
					blLockSet = false;
				}
				if (blLockSet == true) {

					if (strAction.equalsIgnoreCase("allocate")) {
						// System.out.println("ALLOCATE_CELL called");
						try {

							int intCellID = Integer.parseInt(runtimeData
									.getParameter("CELL_intCellID"));
							InventoryUtilities.saveBiospecimenLocation(
									intBiospecimenID, intCellID, rp
											.getAuthToken());

						} catch (Exception ex) {
							System.err
									.println("Cant parse the CellID or BIOSPECIMEN ID in saving new cell");
							ex.printStackTrace();
						}
					}
					// unallocate and existing cell
					else if (strAction.equals("unallocate")) {
						try {
							InventoryUtilities.unallocateBiospecimenFromTray(
									intBiospecimenID, rp.getAuthToken());
						} catch (Exception ex) {
							System.err.println("Cant unallocate the cell");
							ex.printStackTrace();
						}

					}
					// relocate an existing cell to a new location
					else if (strAction.equalsIgnoreCase("relocate")) {

						try {
							InventoryUtilities.unallocateBiospecimenFromTray(
									intBiospecimenID, rp.getAuthToken());
							int intCellID = Integer.parseInt(runtimeData
									.getParameter("CELL_intCellID"));
							InventoryUtilities.saveBiospecimenLocation(
									intBiospecimenID, intCellID, rp
											.getAuthToken());

						} catch (Exception es) {
							System.err
									.println("Cant parse the CellID or BIOSPECIMEN ID in saving new cell");
							es.printStackTrace();
						}

					}
					// unlock writes
					lckBiospecimen.unlockWrites();
					// now back to viewing
					setViewBiospecimen(intBiospecimenID, runtimeData);

				}
			} catch (Exception ex) {

				rp.setErrorMessage("Errors when allocating inventory");
				ex.printStackTrace();
			}
		}

	}

	public boolean checkPermission(String strActivity, AuthToken authToken) {
		try {
			if (authToken.hasActivity(strActivity)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.err
					.println("[CBiospecimen] Error when checking permissions");
			e.printStackTrace();
			return false;
		}
	}

	public void setSearchRuntimeProperties() {
		rp.clearXML();
		rp.addXML("<biospecimen>"
				+ bcBiospecimen.getSearchCriteriaXML()
				+ FlagManager.getFlaggedRecordsXML(
						FlagManager.DOMAIN_BIOSPECIMEN, null)
				+ "</biospecimen>");
		// System.out.println("Rp xml : " + rp.getXML());
		rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);
	}

	// only to be used for paging.
	public void setSearchResultsRuntimeProperties(int intCallingDomain,
			int intPageToDisplay, Hashtable htCriteria) {
		// System.out.println("Search results RP");
		boolean transSearch = false;
		if (htCriteria.get("BIOSPECIMEN_TRANSACTIONS_searchType") != null)
			transSearch = true;

		rp.clearXML();
		rp.addXML("<biospecimen>");
		rp.addXML(bcBiospecimen.getBiospecimenSearchResultsXML(
				intCallingDomain, htCriteria, false, intPageToDisplay,
				this.intCurrentPagingSize, false, -1, transSearch));
		rp.addXML(bcBiospecimen.getSearchCriteriaXML());
		rp.addXML("</biospecimen>");
		rp.setStylesheet(BiospecimenCore.XSL_BIOSPECIMEN_TREE_VIEW);

	}

	public void setViewBiospecimen(int intBiospecimenKey,
			ChannelRuntimeData runtimeData) {

		try {
			intBiospecimenKey = Integer.parseInt(runtimeData
					.getParameter("BIOSPECIMEN_intBiospecimenID"));
		} catch (Exception ne) {
			ne.printStackTrace();
		}

		if (intBiospecimenKey != -1) {

			try {
				if (lckBiospecimen.isValid())
					lckBiospecimen.unlock();
				lckBiospecimen = new LockRequest(rp.getAuthToken());
				lckBiospecimen.addLock("BIOSPECIMEN", bcBiospecimen
						.getLastBiospecimenAddedID()
						+ "", LockRecord.READ_WRITE);
				lckBiospecimen.lockDelayWrite();

				rp.addXML("<biospecimen>"
						+ bcBiospecimen.buildViewBiospecimenXML(
								intBiospecimenKey, runtimeData)
						+ FlagManager.getFlaggedRecordsXML(
								FlagManager.DOMAIN_BIOSPECIMEN, null)
						+ "</biospecimen>");
				rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
			}

			catch (Exception le) {
				rp.setErrorMessage("Locking Error");
				le.printStackTrace();
			}
			/*
			 * rp.clearXML(); rp.addXML("<biospecimen>" +
			 * bcBiospecimen.buildViewBiospecimenXML
			 * (intBiospecimenKey,runtimeData) +
			 * FlagManager.getFlaggedRecordsXML(FlagManager.DOMAIN_BIOSPECIMEN,
			 * null) + "</biospecimen>");
			 * //System.out.println("View biospecimen XML : " + rp.getXML());
			 * rp.setStylesheet(BiospecimenCore.ACTION_BIOSPECIMEN_VIEW);
			 */
		}
	}

	public void setStaticData(ChannelStaticData sd) throws PortalException {

		IPerson ip = sd.getPerson();

		this.staticData = sd;

		// Set up the Runtime Properties
		rp.setAuthToken((AuthToken) sd.getPerson().getAttribute("AuthToken"));

		rp.setIPerson(ip);

		// Set up the Core Channel
		bcBiospecimen = new BiospecimenCore(rp.getAuthToken());
		attAttachments = new AttachmentsAndNotes(rp.getAuthToken());
		vcManager = new VialCalculationManager(rp, rp.getAuthToken());

		if (bcBiospecimen != null) {
			// System.out.println("Biospecimen Core is running!");
		}

		String strSessionUniqueID = "";

		// channel's ids
		Context globalIDContext = null;
		try {
			// Get the context that holds the global IDs for this user
			globalIDContext = (Context) staticData.getJNDIContext().lookup(
					"/channel-ids");
		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find subcontext /channel-ids in JNDI");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}

		try {
			strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();
			SessionManager.addSession(strSessionUniqueID);
			SessionManager.addChannelID(strSessionUniqueID, "CInventory",
					(String) globalIDContext.lookup("CInventory"));
		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find channel ID for fname=Inventory");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}

		try {
			strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();
			SessionManager.addSession(strSessionUniqueID);
			SessionManager.addChannelID(strSessionUniqueID, "CDownload",
					(String) globalIDContext.lookup("CDownload"));

		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find channel ID for fname=Download");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}

		try {
			strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();
			SessionManager.addChannelID(strSessionUniqueID, "CPatient",
					(String) globalIDContext.lookup("CPatient"));
		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find channel ID for fname=Patient");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}

		try {
			strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();
			SessionManager.addSession(strSessionUniqueID);
			SessionManager.addChannelID(strSessionUniqueID, "CSmartform",
					(String) globalIDContext.lookup("CSmartform"));
		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find channel ID for fname=CSmartform");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}

		// smartform
		try {
			strSessionUniqueID = rp.getAuthToken().getSessionUniqueID();
			SessionManager.addChannelID(strSessionUniqueID, "CStudy",
					(String) globalIDContext.lookup("CStudy"));
		} catch (NotContextException nce) {
			LogService.log(LogService.ERROR,
					"Could not find channel ID for fname=Study");
		} catch (NamingException e) {
			LogService.log(LogService.ERROR, e);
		}
		

		SessionManager.addLockRequestSession(strSessionUniqueID);

		// instantiate the hashChannelNotToClearLock
		hashChannelsNotToClearLock = SessionManager.getSharedChannels(rp
				.getAuthToken(), "CBiospecimen");
	}

	@Override
	public void downloadData(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return new String("text/prn");
	}
	
	

	@Override
	public Map getHeaders() {
		// TODO Auto-generated method stub
		Map headers = new HashMap();
		headers.put("Content-Type","text/prn");
		headers.put("X-Content-Type-Options","nosniff");
	//headers.put("Content-Disposition",
		//	"attachment; filename=\"Barcode.prn\"");
		
		return headers;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub

		AuthToken authToken = rp.getAuthToken();
		//Default to single biospecimen if no other choice is made a priori.
		if (runtimeData.getParameter("domain") == null) {
		runtimeData.setParameter("domain", "SINGLE_BIOSPECIMEN");
		}
		return BarcodeManager.generateBarcode(authToken, runtimeData);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Barcode.prn";
	}

}
