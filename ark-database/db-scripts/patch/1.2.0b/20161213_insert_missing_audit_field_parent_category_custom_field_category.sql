use audit;

-- create field.
SET @entityIDCategory=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.study.entity.CustomFieldCategory');
INSERT INTO `audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`)
VALUES
	('parentCategory','Parent Category',@entityIDCategory);

