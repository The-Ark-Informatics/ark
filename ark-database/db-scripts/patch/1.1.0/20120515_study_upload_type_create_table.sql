CREATE  TABLE `study`.`upload_type` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NULL ,
  `DESCRIPTION` VARCHAR(255) NULL )
ENGINE = InnoDB
COMMENT = 'Reference table to describe the type of an upload';

ALTER TABLE `study`.`upload` ADD COLUMN `UPLOAD_TYPE_ID` INT(11) NULL  AFTER `STATUS` ;


ALTER TABLE `study`.`upload` 
  ADD CONSTRAINT `fk_upload_upload_type`
  FOREIGN KEY (`UPLOAD_TYPE_ID` )
  REFERENCES `study`.`upload_type` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_upload_type` (`UPLOAD_TYPE_ID` ASC) ;

