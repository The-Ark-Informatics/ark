package au.org.theark.web.rest.model;

public enum ValidationType {
	INVALID_STUDY_ID("Study id should be a number."),
	
	SUBJECT_UID_ALREADY_EXISTS("Subject uid already exists."),

	NOT_EXSISTING_STUDY("Study should be in the system."),
	
	NO_GENDERTYPE("No gender type assigned to subject."),

	INVALID_GENDER_TYPE("Gender type should be male or female."),

	NO_VITALTYPE("No vital type assigned to subject."),

	INVALID_VITAL_TYPE("Vital type should be alive or deceased."),
	
	NO_SUBJECT_STATUS("No subject status assigned to subject."),

	INVALID_SUBJECT_STATUS("Invalid subject status assigned."),
	
	MISMATCH_SUBJECT_UID_WITH_SUBJECT_ID("The long id and the subject uid mis matched."),
	
	SUCCESSFULLY_VALIDATED("Validation is successful."),
	
	//Relation ship vaidation
	
	SUBJECT_UID_NOT_EXISTS("Subject uid not exists."),
	
	RELATIVE_SUBJECT_UID_NOT_EXISTS("relative subject uid not exists."),
	
	NO_PARENT_TYPE("No parent type assigned."),
	
	INVALID_PARENT_TYPE("Invalid parent type assigned."),
	
	NO_TWIN_TYPE("No twin type assigned."),
	
	INVALID_TWIN_TYPE("Invalid twin type assigned."),
	
	CIRCULAR_VALIDATION_UNSUCCESSFUL("Circular validation Unsuccessful."),
	
	NOT_A_SIBLING("Relative not recognise as a sibling."),
	
	PARENT_RELATION_SHIP_ALREADY_EXISTS("This parent relationship already exist."),
	
	TWIN_RELATION_SHIP_ALREADY_EXISTS("This Twin relationship already exist."),
	
	SUBJECT_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP("Subject status not allowed to create relation ship."),
	
	RELATION_STATUS_NOT_ALLOWED_TO_CREATE_RELATIONSHIP("relative status not allowed to create relation ship."),
	
	INVALID_PARENT_RELATION_SHIP("Invalid relation ship."),
	
	INVALID_TWIN_RELATION_SHIP("Invalid Twin relation ship.");
	
	private final String name;

	ValidationType(String s) {
        name = s;
    }

	public String getName() {
		return name;
	}
}
