package au.org.theark.web.rest.model;

public enum ValidationType {
	INVALID_STUDY_ID("Study id should be a number."),
	
	SUBJECT_UID_ALREADY_EXISTS("Subject uid already exists."),

	NOT_EXSISTING_STUDY("Study should be in the system."),

	NO_GENDERTYPE("No gender type assigned to subject."),

	INVALID_GENDER_TYPE("Gender type should be male or female."),

	NO_VITALTYPE("No vital type assigned to subject."),

	INVALID_VITAL_TYPE("Vital type should be alive or deceased."),
	
	SUCCESSFULLY_VALIDATED("Validation is successful."),
	
	//Relation ship vaidation
	
	SUBJECT_UID_NOT_EXISTS("Subject uid not exists."),
	
	RELATIVE_SUBJECT_UID_NOT_EXISTS("relative subject uid not exists."),
	
	NO_RELATION_TYPE("No relation type assigned."),
	
	INVALID_RELATION_TYPE("Invalid relation type assigned."),
	
	CIRCULAR_VALIDATION_UNSUCCESSFUL("Circular validation Unsuccessful.");
	
	private final String name;

	ValidationType(String s) {
        name = s;
    }

	public String getName() {
		return name;
	}
}
