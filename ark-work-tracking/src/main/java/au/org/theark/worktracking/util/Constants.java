package au.org.theark.worktracking.util;

public class Constants {
	
	//Menu Constants
	public static final String RESEARCHER 											= "RESEARCHER";
	public static final String TAB_MODULE_RESEARCHER 								= "tab.module.work.researcher";
	
	public static final String BILLABLE_ITEM_TYPE 									= "BILLABLE_ITEM_TYPE";
	public static final String TAB_MODULE_BILLABLE_ITEM_TYPE 						= "tab.module.work.billableitemtype";
	
	public static final String WORK_REQUEST 										= "WORK_REQUEST";
	public static final String TAB_MODULE_WORK_REQUEST 								= "tab.module.work.workrequest";
	
	public static final String BILLABLE_ITEM	 									= "BILLABLE_ITEM";
	public static final String TAB_MODULE_BILLABLE_ITEM		 						= "tab.module.work.billableitem";
	
	public static final String WORK_SUBMENU 										= "WorkSubMenus";
	
	
	//CRUD opperation constants
	public static final String			SEARCH_FORM														= "searchForm";
	public static final int				MODE_NEW														= 1;
	public static final int				MODE_EDIT														= 2;

	/* Spring Bean names */
	public static final String			WORK_TRACKING_SERVICE											= "workTrackingService";
	public static final String			WORK_TRACKING_DAO												= "workTrackingDao";
	
	/* Common Constants */
	public static final String			ID																= "id";
	public static final String			NAME															= "name";
	public static final String			STUDY_ID														= "studyId";
	public static final String			YES																= "Yes";
	public static final String			NO																= "No";
	public static final String			Y																= "Y";
	public static final String			N																= "N";
	
	
	/* Researcher Constants */	
	public static final String			FIRST_NAME														= "firstName";
	public static final String			LAST_NAME														= "lastName";
	public static final String			EMAIL															= "email";
	public static final String			PHONE_NUMBER													= "phoneNumber";
	public static final String			ORGANIZATION													= "organization";
	public static final String			ROLE													        = "researcherRole";
	public static final String			STATUS													    	= "researcherStatus";
	public static final String			CREATED_DATE											    	= "createdDate";
		
	public static final String			RESEARCHER_ID													= "researcher.id";
	public static final String			RESEARCHER_FIRST_NAME											= "researcher.firstName";
	public static final String			RESEARCHER_LAST_NAME											= "researcher.lastName";
	public static final String			RESEARCHER_ORGANIZATION											= "researcher.organization";
	public static final String			RESEARCHER_CREATED_DATE											= "researcher.createdDate";
	public static final String			RESEARCHER_STATUS											    = "researcher.researcherStatus";
	public static final String			RESEARCHER_ROLE											        = "researcher.researcherRole";	
	public static final String			RESEARCHER_OFFICE_PHONE											= "researcher.officePhone";
	public static final String			RESEARCHER_MOBILE											    = "researcher.mobile";
	public static final String			RESEARCHER_EMAIL											    = "researcher.email";
	public static final String			RESEARCHER_FAX											        = "researcher.fax";
	public static final String			RESEARCHER_ADDRESS											    = "researcher.address";
	public static final String			RESEARCHER_COMMENT											    = "researcher.comment";
	public static final String			RESEARCHER_FULL_NAME											= "researcher.fullName";
	public static final String			RESEARCHER_TITLE												= "researcher.titleType";
	
	public static final String			RESEARCHER_ACCOUNT_NUMBER										= "researcher.accountNumber";
	public static final String			RESEARCHER_BILLING_TYPE											= "researcher.billingType";
	public static final String			RESEARCHER_BANK											        = "researcher.bank";
	public static final String			RESEARCHER_BSB											        = "researcher.bsb";
	public static final String			RESEARCHER_ACCOUNT_NAME											= "researcher.accountName";

	public static final String RESEARCHER_ACCOUNT_NAME_TAG 												= "Researcher Account Name";
	public static final String ERROR_WORK_RESEARCHER_ACCOUNTNAME_LENGTH 								= "error.work.researcher.accountname.length";	
	public static final String RESEARCHER_BANK_TAG 														= "Researcher Bank";
	public static final String ERROR_WORK_RESEARCHER_BANK_LENGTH 										= "error.work.researcher.bank.length";
	public static final String RESEARCHER_BSB_TAG 														= "Researcher BSB";
	public static final String ERROR_WORK_RESEARCHER_BSB_LENGTH 										= "error.work.researcher.bsb.length";
	public static final String RESEARCHER_ACCOUNT_NUMBER_TAG 											= "Researcher Account Number";
	public static final String ERROR_WORK_RESEARCHER_ACCOUNTNUMBER_LENGTH 								= "error.work.researcher.accountnumber.length";
	public static final String RESEARCHER_COMMENT_TAG 													= "Researcher Comment";
	public static final String ERROR_WORK_RESEARCHER_COMMENT_LENGTH 									= "error.work.researcher.comment.length";
	public static final String RESEARCHER_EMAIL_TAG 													= "Researcher Email";
	public static final String ERROR_WORK_RESEARCHER_EMAIL_LENGTH 										= "error.work.researcher.email.length";
	public static final String RESEARCHER_FAX_TAG 														= "Researcher Fax";
	public static final String ERROR_WORK_RESEARCHER_FAX_LENGTH 										= "error.work.researcher.fax.length";
	public static final String RESEARCHER_MOBILE_PHONE_TAG 												= "Researcher Mobile Phone";
	public static final String ERROR_WORK_RESEARCHER_MOBILEPHONE_LENGTH 								= "error.work.researcher.mobilephone.length";
	public static final String RESEARCHER_OFFICE_PHONE_TAG 												= "Researcher Office Phone";
	public static final String ERROR_WORK_RESEARCHER_OFFICEPHONE_LENGTH 								= "error.work.researcher.officephone.length";
	public static final String RESEARCHER_STATUS_TAG 													= "Researcher Status";
	public static final String ERROR_WORK_RESEARCHER_STATUS_REQUIRED 									= "error.work.researcher.status.required";
	public static final String RESEARCHER_ROLE_TAG 														= "Researcher Role";
	public static final String ERROR_WORK_RESEARCHER_ROLE_REQUIRED 										= "error.work.researcher.role.required";
	public static final String ERROR_WORK_RESEARCHER_ADDRESS_LENGTH 									= "error.work.researcher.address.length";
	public static final String RESEARCHER_ADDRESS_TAG 													= "Researcher Address";
	public static final String ERROR_WORK_RESEARCHER_ADDRESS_REQUIRED 									= "error.work.researcher.address.required";
	public static final String ERROR_WORK_RESEARCHER_ORGANIZATION_LENGTH 								= "error.work.researcher.organization.length";
	public static final String RESEARCHER_ORGANIZATION_TAG 												= "Researcher Organization";
	public static final String ERROR_WORK_RESEARCHER_ORGANIZATION_REQUIRED 								= "error.work.researcher.organization.required";
	public static final String ERROR_WORK_RESEARCHER_LASTNAME_LENGTH 									= "error.work.researcher.lastname.length";
	public static final String RESEARCHER_LAST_NAME_TAG 												= "Researcher Last Name";
	public static final String ERROR_WORK_RESEARCHER_LASTNAME_REQUIRED 									= "error.work.researcher.lastname.required";
	public static final String ERROR_WORK_RESEARCHER_FIRSTNAME_LENGTH 									= "error.work.researcher.firstname.length";
	public static final String RESEARCHER_FIRST_NAME_TAG 												= "Researcher First Name";
	public static final String ERROR_WORK_RESEARCHER_FIRSTNAME_REQUIRED 								= "error.work.researcher.firstname.required";
	public static final String RESEARCHER_TITLE_TYPE_TAG 												= "Title";
	public static final String ERROR_WORK_RESEARCHER_TITLE_TYPE_REQUIRED 								= "error.work.researcher.titletype.required";	
	
	//Billable Item  Type Constants
	public static final String			BILLABLE_ITEM_TYPE_ID											= "billableItemType.id";
	public static final String			BILLABLE_ITEM_TYPE_ITEM_NAME									= "billableItemType.itemName";
	public static final String			BILLABLE_ITEM_TYPE_QUANTITY_PER_UNIT							= "billableItemType.quantityPerUnit";
	public static final String			BILLABLE_ITEM_TYPE_UNIT_PRICE									= "billableItemType.unitPrice";
	public static final String			BILLABLE_ITEM_TYPE_GST											= "billableItemType.gst";
	public static final String			BILLABLE_ITEM_TYPE_DESCRIPTION									= "billableItemType.description";
	
	public static final String			BIT_ITEM_NAME													= "itemName";
	public static final String			BIT_QUANTITY_PER_UNIT											= "quantityPerUnit";
	public static final String			BIT_UNIT_PRICE													= "unitPrice";
	public static final String			BIT_GST															= "gst";
	public static final String			BIT_STATUS														= "billableItemTypeStatus";
	
	public static final String			BILLABLE_ITEM_TYPE_CUSTOM										= "CUSTOM";
	public static final String			BILLABLE_ITEM_TYPE_DEFAULT										= "DEFAULT";
	
	public static final String			BILLABLE_ITEM_TYPE_ACTIVE										= "ACTIVE";
	public static final String			BILLABLE_ITEM_TYPE_INACTIVE										= "INACTIVE";
	
	public static final String BILLABLE_ITEM_TYPE_ITEM_NAME_TAG 										= "Item Name";
	public static final String ERROR_BILLABLE_ITEM_TYPE_ITEM_NAME_REQUIRED 								= "error.work.billableitemtype.itemname.required";
	public static final String ERROR_BILLABLE_ITEM_TYPE_ITEM_NAME_LENGTH 								= "error.work.billableitemtype.itemname.length";
	public static final String BILLABLE_ITEM_TYPE_DESCRIPTION_TAG 										= "Description";
	public static final String ERROR_BILLABLE_ITEM_TYPE_DESCRIPTION_LENGTH 								= "error.work.billableitemtype.description.length";
	public static final String BILLABLE_ITEM_TYPE_FOUR_DECIMAL_PATTERN 									= "\\d*||\\d*.\\d{1,4}";
	public static final String ERROR_BILLABLE_ITEM_TYPE_GST			 									= "error.work.billableitemtype.gst";
	public static final String BILLABLE_ITEM_TYPE_TWO_DECIMAL_PATTERN 									= "\\d*||\\d*.\\d{1,2}";
	public static final String BILLABLE_ITEM_TYPE_UNIT_PRICE_TAG			 							= "Unit Price";
	public static final String ERROR_BILLABLE_ITEM_TYPE_UNIT_PRICE		 								= "error.work.billableitemtype.unitprice";
	public static final String ERROR_BILLABLE_ITEM_TYPE_UNIT_PRICE_REQUIRED		 						= "error.work.billableitemtype.unitprice.required";
	
	public static final String BILLABLE_ITEM_TYPE_QUNATITY_PER_UNIT_TAG 								= "Quantity Per Unit";
	public static final String ERROR_BILLABLE_ITEM_TYPE_QUNATITY_PER_UNIT_REQUIRED 						= "error.work.billableitemtype.quantityperunit.required";
	
	public static final String BILLABLE_ITEM_TYPE_GST_TAG 												= "GST";
	public static final String ERROR_BILLABLE_ITEM_TYPE_GST_REQUIRED 									= "error.work.billableitemtype.gst.required";
	

	
	//Work Request Constants
	
	public static final String			WORK_REQUEST_ID													= "workRequest.id";
	public static final String			WORK_REQUEST_ITEM_NAME											= "workRequest.name";
	public static final String			WORK_REQUEST_REQUESTED_DATE										= "workRequest.requestedDate";
	public static final String			WORK_REQUEST_REQUEST_STATUS										= "workRequest.requestStatus";
	public static final String			WORK_REQUEST_COMMENCED_DATE										= "workRequest.commencedDate";
	public static final String			WORK_REQUEST_COMPLETED_DATE										= "workRequest.completedDate";
	public static final String			WORK_REQUEST_DESCRIPTION										= "workRequest.description";
	public static final String			WORK_REQUEST_RESEARCHER											= "workRequest.researcher";
	
	public static final String			WR_REQUESTED_DATE												= "requestedDate";
	public static final String			WR_COMMENCED_DATE												= "commencedDate";
	public static final String			WR_COMPLETED_DATE												= "completedDate";
	public static final String			WR_STATUS														= "requestStatus";
	public static final String			WR_RESEARCHER													= "researcher";
	
	public static final String WORK_REQUEST_ITEM_NAME_TAG 												= "Work Request Name";
	public static final String ERROR_WORK_REQUEST_ITEM_NAME_REQUIRED 									= "error.work.workrequest.name.required";
	public static final String ERROR_WORK_REQUEST_ITEM_NAME_LENGTH 										= "error.work.workrequest.name.length";
	public static final String WORK_REQUEST_DESCRIPTION_TAG 											= "Description";
	public static final String ERROR_WORK_REQUEST_DESCRIPTION_LENGTH 									= "error.work.workrequest.description.length";
	public static final String WORK_REQUEST_REQUESTED_DATE_TAG 											= "Requested Date";
	public static final String ERROR_WORK_REQUEST_REQUESTED_DATE_REQUIRED 								= "error.work.workrequest.requesteddate.required";
	public static final String WORK_REQUEST_STATUS_TAG 													= "Status";
	public static final String ERROR_WORK_REQUEST_STATUS_REQUIRED 										= "error.work.workrequest.requeststatus.required";
	public static final String WORK_REQUEST_RESEARCHER_TAG 												= "Researchr";
	public static final String ERROR_WORK_REQUEST_RESEARCHER_REQUIRED 								    = "error.work.workrequest.researcher.required";
	
	
	//Billable Item Constants
	
	public static final String			INVOICE															= "invoice";
	public static final String			BILLABLE_ITEM_MANUAL											= "MANUAL";
	public static final String			BILLABLE_ITEM_AUTOMATED											= "AUTOMATED";
	
	public static final String			DOWNLOAD_FILE													= "downloadFile";
	
	public static final String			BILLABLE_ITEM_ID												= "billableItem.id";
	public static final String			BILLABLE_ITEM_DESCRIPTION										= "billableItem.description";
	public static final String			BILLABLE_ITEM_WORK_REQUEST										= "billableItem.workRequest";
	public static final String			BILLABLE_ITEM_QUANTITY											= "billableItem.quantity";
	public static final String			BILLABLE_ITEM_INVOICE											= "billableItem.invoice";
	public static final String			BILLABLE_ITEM_COMMENCE_DATE										= "billableItem.commenceDate";
	public static final String			BILLABLE_ITEM_START_DATE										= "billableItem.startDate";
	public static final String			BILLABLE_ITEM_END_DATE											= "billableItem.endDate";
	public static final String			BILLABLE_ITEM_ITEM_COST											= "billableItem.itemCost";
	public static final String			BILLABLE_ITEM_ATTACHMENT_FILE_NAME								= "billableItem.attachmentFileName";
	
	public static final String			BILLABLE_ITEM_BILLABLE_ITEM_TYPE								= "billableItem.billableItemType";
	public static final String			BILLABLE_ITEM_ITEM_STATUS										= "billableItem.itemStatus";
	public static final String			FILE_NAME														= "fileName";
	public static final String			BILLABLE_ITEM_ITEM_TYPE											= "billableItem.type";
	
	public static final String			BILLABLE_ITEM_RESEARCHER_FULL_NAME								= "researcherFullName";
	public static final String			BILLABLE_ITEM_WORK_REQUEST_DESCRIPTION							= "workRequestDescription";
	public static final String			BILLABLE_ITEM_TOTAL_COST										= "totalCost";
	
	public static final String			BI_DESCRIPTION													= "description";
	public static final String			BI_QUANTITY														= "quantity";
	public static final String			BI_WORK_REQUEST													= "workRequest";
	public static final String			BI_INVOICE														= "invoice";
	public static final String			BI_ITEM_STATUS													= "itemStatus";
	
	public static final String BILLABLE_ITEM_DESCRIPTION_TAG											= "Description";
	public static final String ERROR_BILLABLE_ITEM_DESCRIPTION_REQUIRED 								= "error.work.billableitem.description.required";
	public static final String ERROR_BILLABLE_ITEM_DESCRIPTION_LENGTH 									= "error.work.billableitem.description.length";
	public static final String BILLABLE_ITEM_QUANTITY_TAG												= "Quantity";
	public static final String ERROR_BILLABLE_ITEM_QUANTITY_REQUIRED 									= "error.work.billableitem.quantity.required";
	public static final String BILLABLE_ITEM_WORK_REQUEST_TAG											= "Work Request";
	public static final String ERROR_BILLABLE_ITEM_WORK_REQUEST_REQUIRED 								= "error.work.billableitem.workrequest.required";
	public static final String BILLABLE_ITEM_INVOICE_TAG												= "Invoice";
	public static final String ERROR_BILLABLE_ITEM_INVOICE_REQUIRED 									= "error.work.billableitem.invoice.required";
	public static final String BILLABLE_ITEM_BILLABLE_ITEM_TYPE_TAG										= "Billable Item Type";
	public static final String ERROR_BILLABLE_ITEM_BILLABLE_ITEM_TYPE_REQUIRED 							= "error.work.billableitem.billableitemtype.required";
	public static final String BILLABLE_ITEM_ITEM_STATUS_TAG											= "Status";
	public static final String ERROR_BILLABLE_ITEM_ITEM_STATUS_REQUIRED 								= "error.work.billableitem.itemstatus.required";
	public static final String BILLABLE_ITEM_COMMENCE_DATE_TAG											= "Date";
	public static final String ERROR_BILLABLE_ITEM_COMMENCE_DATE_REQUIRED 								= "error.work.billableitem.commencedate.required";
}
