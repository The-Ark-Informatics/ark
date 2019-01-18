use audit;
SET @entityIDLinkSubjectStudy=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.LinkSubjectStudy');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('consentExpiryDate','Consent Expiry Date',@entityIDLinkSubjectStudy),('consentDateOfLastChange','Consent Date of Change',@entityIDLinkSubjectStudy);

