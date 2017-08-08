ALTER TABLE `study`.`email_account` 
DROP FOREIGN KEY `email_account_ibfk_1`,
DROP FOREIGN KEY `FK8641D44A104DA269`;

ALTER TABLE `study`.`email_account` 
DROP INDEX `EMAIL_ACCOUNT_EMA_FK1` ;

ALTER TABLE `study`.`email_account_type` 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT ;


ALTER TABLE `study`.`email_status` 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `study`.`person` 
DROP FOREIGN KEY `fk_person_preferred_email_status`,
DROP FOREIGN KEY `fk_person_other_email_status`;

ALTER TABLE `study`.`person` 
DROP INDEX `fk_person_preferred_email_status` ,
DROP INDEX `fk_person_other_email_status` ;


ALTER TABLE `study`.`email_account` 
ADD INDEX `fk_email_account_status_idx` (`EMAIL_STATUS_ID` ASC);
ALTER TABLE `study`.`email_account` 
ADD CONSTRAINT `fk_email_account_type`
  FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID`)
  REFERENCES `study`.`email_account_type` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_email_account_status`
  FOREIGN KEY (`EMAIL_STATUS_ID`)
  REFERENCES `study`.`email_status` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

INSERT INTO `study`.`email_account_type` (`NAME`, `DESCRIPTION`) VALUES ('Personal', 'Personal Emal Account');
INSERT INTO `study`.`email_account_type` (`NAME`, `DESCRIPTION`) VALUES ('Office', 'Office Email Account');

