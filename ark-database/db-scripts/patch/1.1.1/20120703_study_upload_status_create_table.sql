USE study;
CREATE  TABLE `study`.`upload_status` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NOT NULL ,
  `SHORT_MESSAGE` VARCHAR(128) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  `LONG_MESSAGE` VARCHAR(256) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `name_UNIQUE` (`NAME` ASC) );
ALTER TABLE `study`.`upload_status` ENGINE = InnoDB ;

ALTER TABLE `study`.`upload` DROP COLUMN `STATUS`;

ALTER TABLE `study`.`upload` ADD COLUMN `STATUS_ID` INT(11) NOT NULL  AFTER `PAYLOAD_ID` , 
  ADD CONSTRAINT `fk_upload_status`
  FOREIGN KEY (`STATUS_ID` )
  REFERENCES `study`.`upload_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_status` (`STATUS_ID` ASC);

INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (-3,'ERROR_ON_DATA_IMPORT','Error while importing data','While the file passed validation, an error occured during the import of data.  Please contact your system administrator.');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (-2,'ERROR_IN_DATA_VALIDATION','Error while validating data','Error while validating data, prior to uploading');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (-1,'ERROR_IN_FILE_VALIDATION','Error validation file','Error in file format or header values.');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (0,'STATUS_NOT_DEFINED','Status not defined','Status not defined.  This may predate our adding status to uploads');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (1,'AWAITING_VALIDATION','Awaiting Validation','Successfully uploaded to our server, awaiting validation and upload to fields');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (2,'VALIDATED','Successfully validated','Successfully validated.  Awaiting upload into fields');
INSERT INTO `study`.`upload_status` (ID,NAME,SHORT_MESSAGE,LONG_MESSAGE) VALUES (3,'COMPLETED','Successfully completed','Successfully completed upload');

