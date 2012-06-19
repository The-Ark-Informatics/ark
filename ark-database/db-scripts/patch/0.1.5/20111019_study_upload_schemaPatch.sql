use study;
ALTER TABLE `study`.`upload` DROP COLUMN `UPDATE_TIME` , DROP COLUMN `UPDATE_USER_ID` ;

ALTER TABLE `study`.`upload` ADD COLUMN `ARK_FUNCTION_ID` INT NULL  AFTER `UPLOAD_REPORT` , 
  ADD CONSTRAINT `fk_upload_ark_function_id`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_ark_function_id` (`ARK_FUNCTION_ID` ASC) ;

/* Update the ark_function_id with value 10 */
update study.upload set ark_function_id = 10 where  id >= 1;

/* Set the ark_function_id to not null */
ALTER TABLE `study`.`upload` DROP FOREIGN KEY `fk_upload_ark_function_id` ;

ALTER TABLE `study`.`upload` CHANGE COLUMN `ARK_FUNCTION_ID` `ARK_FUNCTION_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_upload_ark_function_id`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

