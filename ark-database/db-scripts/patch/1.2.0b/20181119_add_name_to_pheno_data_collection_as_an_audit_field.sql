use audit;
SET @entityIDPhenoDataSetCollection=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetCollection');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('name','Name',@entityIDPhenoDataSetCollection);

