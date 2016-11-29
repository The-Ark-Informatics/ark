use audit;
SET @entityIDPhone=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.Phone');
SET @entityIDAddress=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.Address');
SET @entityIDPerson=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.Person');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('validFrom','Valid From',@entityIDPhone),
	('validTo','Valid to',@entityIDPhone),
	('preferredPhoneNumber','Preferred Phone Number',@entityIDPhone);

INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('validFrom','Valid From',@entityIDAddress),
	('validTo','Valid to',@entityIDAddress);

INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('currentOrDeathAge','Current or Death Age',@entityIDPerson),
	('otherIDs','Other IDs',@entityIDPerson);

