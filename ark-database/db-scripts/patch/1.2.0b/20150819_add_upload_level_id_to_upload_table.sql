ALTER TABLE `study`.`upload` 
ADD COLUMN `UPLOAD_LEVEL_ID` INT(11) NULL AFTER `STATUS_ID`,
ADD INDEX `fk_upload_upload_level_idx` (`UPLOAD_LEVEL_ID` ASC);
ALTER TABLE `study`.`upload` 
ADD CONSTRAINT `fk_upload_upload_level`
  FOREIGN KEY (`UPLOAD_LEVEL_ID`)
  REFERENCES `study`.`upload_level` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
