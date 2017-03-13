use audit;

-- create field.
SET @entityIDCustomField=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.CustomField');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('customFieldType','Custom Field Type',@entityIDCustomField),
	('customFieldCategory','Custom Field Category',@entityIDCustomField),
	('orderNumber','Order Number',@entityIDCustomField);
