use audit;

-- Get package ID
SET @packageStudy=(SELECT ID FROM audit_package where name='Study');

-- create new entity
INSERT INTO `audit_entity` (`CLASS_IDENTIFIER`, `NAME`, `PACKAGE_ID`)
VALUES	('au.org.theark.core.model.study.entity.CustomFieldCategory','Custom Field Category',@packageStudy);

-- create field.
SET @entityIDCategory=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.CustomFieldCategory');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('name','Name',@entityIDCategory),
	('customFieldType','Custom Field Type',@entityIDCategory),
	('description','Description',@entityIDCategory),
	('orderNumber','Order Number',@entityIDCategory);


