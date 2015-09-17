ALTER TABLE `study`.`custom_field_type` 
ADD COLUMN `ARK_MODULE_ID` INT(11) NULL AFTER `ID`,
ADD INDEX `fk_custom_field_type_1_idx` (`ARK_MODULE_ID` ASC);
ALTER TABLE `study`.`custom_field_type` 
ADD CONSTRAINT `fk_custom_field_type_1`
  FOREIGN KEY (`ARK_MODULE_ID`)
  REFERENCES `study`.`ark_module` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

set @subject_id=(SELECT ID FROM `study`.`custom_field_type` where name='SUBJECT');

set @family_id=(SELECT ID FROM `study`.`custom_field_type` where name='FAMILY');

UPDATE `study`.`custom_field_type` 
SET `ARK_MODULE_ID`=(SELECT id FROM study.ark_module where name='Study') 
WHERE ID =@subject_id;

UPDATE `study`.`custom_field_type` 
SET `ARK_MODULE_ID`=(SELECT id FROM study.ark_module where name='Study') 
WHERE ID =@family_id;


INSERT INTO `study`.`custom_field_type` (`ARK_MODULE_ID`, `NAME`, `DESCRIPTION`) 
VALUES ((SELECT id FROM study.ark_module where name='LIMS'), 'BIOSPECIMEN', 'LIMS Biospecimen details');
INSERT INTO `study`.`custom_field_type` (`ARK_MODULE_ID`, `NAME`, `DESCRIPTION`)
VALUES ((SELECT id FROM study.ark_module where name='LIMS'), 'BIOCOLLECTION', 'LIMS BioCollection details');

