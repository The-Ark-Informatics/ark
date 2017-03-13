use audit;
SET @entityBiospecimen=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.lims.entity.Biospecimen');

INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('bioCollection','BioCollection',@entityBiospecimen),
	('amount','Amount',@entityBiospecimen);
	


