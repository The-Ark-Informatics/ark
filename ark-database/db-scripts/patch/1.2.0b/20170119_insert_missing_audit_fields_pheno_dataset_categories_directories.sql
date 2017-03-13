use audit;

-- Get package ID
SET @packagePheno=(SELECT ID FROM audit_package where name='Pheno');

-- create new entity

INSERT INTO `audit_entity` (`CLASS_IDENTIFIER`, `NAME`, `PACKAGE_ID`)
VALUES	('au.org.theark.core.model.pheno.entity.PhenoDataSetCategory','Pheno DataSet Category',@packagePheno);

INSERT INTO `audit_entity` (`CLASS_IDENTIFIER`, `NAME`, `PACKAGE_ID`)
VALUES	('au.org.theark.core.model.pheno.entity.PhenoDataSetField','Pheno DataSet Field',@packagePheno);

-- create field.

SET @entityIDPhenoCategory=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetCategory');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('name','Name',@entityIDPhenoCategory),
	('description','Description',@entityIDPhenoCategory);

SET @entityIDPhenoField=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetField');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('name','Name',@entityIDPhenoField),
	('fieldType','Pheno field type',@entityIDPhenoField),
	('description','Description',@entityIDPhenoField),
	('unitType','Type of unit',@entityIDPhenoField),
	('unitTypeInText','Type of unit in Text',@entityIDPhenoField),
	('required','required or not',@entityIDPhenoField),
	('allowMultiselect','allow select multiple times',@entityIDPhenoField);





