use audit;

-- CustomField update.

SET @entityIDCustomField=(SELECT ID FROM audit_entity WHERE class_identifier='au.org.theark.core.model.study.entity.CustomField');

SET @updateID=(SELECT ID FROM audit_field WHERE FIELD_NAME='name' AND ENTITY_ID=@entityIDCustomField);
UPDATE audit_field SET NAME='Field Name' WHERE ID=@updateID;

SET @updateID1=(SELECT ID FROM audit_field WHERE FIELD_NAME='fieldType' AND ENTITY_ID=@entityIDCustomField);
UPDATE audit_field SET NAME='Data Type' WHERE ID=@updateID1;


SET @updateID2=(SELECT ID FROM audit_field WHERE FIELD_NAME='fieldLabel' AND ENTITY_ID=@entityIDCustomField);
UPDATE audit_field SET NAME='Question' WHERE ID=@updateID2;

SET @updateID3=(SELECT ID FROM audit_field WHERE FIELD_NAME='unitType' AND ENTITY_ID=@entityIDCustomField);
UPDATE audit_field SET NAME='Units' WHERE ID=@updateID3;

SET @updateID4=(SELECT ID FROM audit_field WHERE FIELD_NAME='unitTypeInText' AND ENTITY_ID=@entityIDCustomField);
UPDATE audit_field SET NAME='Units' WHERE ID=@updateID4;


-- PhenoDataset update.

SET @entityIDPhenoDatasetField=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetField');


SET @updateID5=(SELECT ID FROM audit_field WHERE FIELD_NAME='name' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Field Name' WHERE ID=@updateID5;

SET @updateID6=(SELECT ID FROM audit_field WHERE FIELD_NAME='fieldType' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Data Type' WHERE ID=@updateID6;

SET @updateID7=(SELECT ID FROM audit_field WHERE FIELD_NAME='fieldLabel' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Question' WHERE ID=@updateID7;

SET @updateID8=(SELECT ID FROM audit_field WHERE FIELD_NAME='unitType' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Units' WHERE ID=@updateID8;

SET @updateID9=(SELECT ID FROM audit_field WHERE FIELD_NAME='unitTypeInText' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Units' WHERE ID=@updateID9;

SET @updateID10=(SELECT ID FROM audit_field WHERE FIELD_NAME='allowMultiselect' AND ENTITY_ID=@entityIDPhenoDatasetField);
UPDATE audit_field SET NAME='Allow Multiple Selections' WHERE ID=@updateID10;

