package au.org.theark.web.rest.model;

public enum ValidationType {
	
	USER_AUTHENTICATION_INSUFFICIENT_PRIVILEGES("The authenticated user account does not have sufficient privileges to perform this action."),
	
	CREATED_SUCCESSFULLY(" created successfully."),
	
	UPDATED_SUCCESSFULLY(" updated successfully."),
	
	DELETED_SUCCESSFULLY(" deleted successfully."),
	
	FOUND_SUCCESSFULLY("Found successfully."),
	
	INVALID_STUDY_ID("Invalid Study ID."),
	
	SUBJECT_UID_ALREADY_EXISTS("A subject with this UID already exists."),

	NOT_EXISTING_STUDY("A study with this ID does not exist."),
	
	NO_GENDERTYPE("Gender not specified."),

	INVALID_GENDER_TYPE("Gender must be “male“ or “female“."),

	NO_VITAL_STATUS("Vital Status not specified."),

	INVALID_VITAL_STATUS("Vital Status must be “Alive”, “Deceased”, or “Unknown”."),
	
	NO_SUBJECT_STATUS("Subject Status not specified."),

	INVALID_SUBJECT_STATUS("Subject Status must be “Subject”, “Prospect”, “Withdrawn Subject”, “Archive”, or “Inactive”."),
	
	MISMATCH_SUBJECT_UID_WITH_SUBJECT_ID("The given Subject UID and Subject ID do not match."),
	
	SUCCESSFULLY_VALIDATED("Validation successful."),
	
	//Relation ship vaidation
	
	SUBJECT_UID_NOT_EXISTS("A subject with this UID does not exist."),
	
	RELATIVE_SUBJECT_UID_NOT_EXISTS("A relative with this subject UID does not exist."),
	
	NO_PARENT_RELATIONSHIP_EXISTS_FOR_STUDY("No parent relationship exists."),
	
	NO_TWINTYPE_RELATIONSHIP_EXISTS_FOR_STUDY("No twin relationship exists."),
	
	NO_PARENT_TYPE("Parent Type not specified."),
	
	INVALID_PARENT_TYPE("Parent Type must be “Mother” or “Father”."),
	
	NO_TWIN_TYPE("Twin Type not specified."),
	
	INVALID_TWIN_TYPE("Twin Type must be “MZ” or “DZ”."),
	
	CIRCULAR_VALIDATION_UNSUCCESSFUL("Consanguineous relationship detected in a study not configured to permit it."),
	
	NOT_A_SIBLING("Twin relationship indicated for a subject that is not a sibling."),
	
	PARENT_RELATIONSHIP_ALREADY_EXISTS("Parental relationship already exists."),
	
	TWIN_RELATIONSHIP_ALREADY_EXISTS("Twin relationship already exists."),
	
	SUBJECT_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP("Cannot create a relationship for a subject with this Subject Status."),
	
	RELATION_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP("Cannot create a relationship for a relative with this Subject Status."),
	
	INVALID_PARENT_RELATIONSHIP("Invalid parental relationship."),
	
	INVALID_TWIN_RELATIONSHIP("Invalid twin relationship."),
	
	PEDIGREE_VIEW_NOT_EXISTS("Pedigree view does not exists."),
	
	PEDIGREE_CONFIG_NOT_EXISTS("Pedigree configuration does not exists."),
	
	PEDIGREE_CONFIGURATION_SET_VALUE_NOT_ACCEPTED("Pedigree configuration expected boolean value “yes”, “true”, “no”, or “false”."),
	
	PEDIGREE_CONFIGURATION_CUSTOM_FIELD_NAME_NOT_FOUND("Study does not contain a custom field with this name."),
	
	PEDIGREE_CONFIGURATION_ALREADY_EXISTS_FOR_STUDY("Cannot create a pedigree configuration when one already exists for this study."),
	
	PEDIGREE_CONFIGURATION_CAN_NOT_UPDATE_FOR_STUDY("Cannot update a pedigree configuration when no configuration has been set previously."),
	
	PEDIGREE_MEMEBERS_CAN_NOT_FOUND("No Pedigree members exists.");
	
	private final String name;

	ValidationType(String s) {
        name = s;
    }

	public String getName() {
		return name;
	}
}
