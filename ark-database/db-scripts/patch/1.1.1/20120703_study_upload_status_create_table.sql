CREATE  TABLE `study`.`upload_status` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `short_message` VARCHAR(128) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  `long_message` VARCHAR(256) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) );

ALTER TABLE `study`.`upload` DROP COLUMN `STATUS`;

ALTER TABLE `study`.`upload_status` ENGINE = InnoDB , CHANGE COLUMN `short_message` `SHORT_MESSAGE` VARCHAR(45) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)'  AFTER `NAME` , CHANGE COLUMN `id` `ID` INT(11) NOT NULL  , CHANGE COLUMN `name` `NAME` VARCHAR(45) NOT NULL  , CHANGE COLUMN `long_message` `LONG_MESSAGE` VARCHAR(45) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)'  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) 
, DROP INDEX `name_UNIQUE` 
, ADD UNIQUE INDEX `name_UNIQUE` (`NAME` ASC);

ALTER TABLE `study`.`upload` ADD COLUMN `STATUS_ID` INT(11) NOT NULL  AFTER `PAYLOAD_ID` , 
  ADD CONSTRAINT `fk_upload_status`
  FOREIGN KEY (`STATUS_ID` )
  REFERENCES `study`.`upload_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_status` (`STATUS_ID` ASC);



insert upload_status(id, name, short_message, long_message)
			values(0, 'STATUS_NOT_UPDATED', 'Status not defined', 'Status not defined.  This may predate our adding status to uploads');

insert upload_status(id, name, short_message, long_message)
			values(1, 'ERROR_IN_VALIDATION', 'Error while validating', 'Error while validating data, prior to uploading');

insert upload_status(id, name, short_message, long_message)
			values(2, 'ERROR_ON_UPLOAD', 'Error while uploading data', 'While the file passed validation, an error occured during the upload.  Please contact your system administrator.');

insert upload_status(id, name, short_message, long_message)
			values(3, 'VALIDATED', 'Successfully validatied', 'Successfully validatied.  Awaiting upload into fields');

insert upload_status(id, name, short_message, long_message)
			values(4, 'UPLOADED', 'Successfully uploaded', 'Successfully updated into fields');

insert upload_status(id, name, short_message, long_message)
			values(5, 'AWAITING_VALIDATION', 'Awaiting Validation', 'Successfully uploaded to our server, awaiting validation and upload to fields');


