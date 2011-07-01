USE study;

CREATE  TABLE `study`.`phone_status` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(500) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (1, 'Current');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (2, 'Current Alternative');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (3, 'Current Under Investigation');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (4, 'Valid Past');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (5, 'Incorrect or Disconnected');

ALTER TABLE `study`.`phone` ADD COLUMN `PHONE_STATUS_ID` INT NULL  AFTER `PHONE_TYPE_ID` , ADD COLUMN `SOURCE` VARCHAR(500) NULL  AFTER `PHONE_STATUS_ID` , ADD COLUMN `DATE_RECEIVED` DATE NULL  AFTER `SOURCE` , ADD COLUMN `COMMENT` VARCHAR(1000) NULL  AFTER `DATE_RECEIVED` , ADD COLUMN `SILENT` INT NULL  AFTER `COMMENT` , 
  ADD CONSTRAINT `phone_ibfk_3`
  FOREIGN KEY (`PHONE_STATUS_ID` )
  REFERENCES `study`.`phone_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `phone_ibfk_4`
  FOREIGN KEY (`SILENT` )
  REFERENCES `study`.`yes_no` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `phone_ibfk_3` (`PHONE_STATUS_ID` ASC) 
, ADD INDEX `phone_ibfk_4` (`SILENT` ASC) ;


