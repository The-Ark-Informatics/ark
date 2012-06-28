ALTER TABLE `study`.`upload_type` ADD COLUMN `ARK_MODULE_ID` INT(11)  DEFAULT 2  AFTER `DESCRIPTION` , 
  ADD CONSTRAINT `fk_upload_type_ark_module`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_type_ark_module` (`ARK_MODULE_ID` ASC) ;



update study.upload_type set ark_module_id = 3
where id = 3;
