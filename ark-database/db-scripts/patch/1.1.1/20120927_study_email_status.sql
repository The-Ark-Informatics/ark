CREATE  TABLE `study`.`email_status` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
COMMENT = 'Status (something like  Unverified, Verified, Bounced, Unknown)';


INSERT INTO `study`.`email_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0, 'Unknown', 'A default where status has not been specified');
INSERT INTO `study`.`email_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1, 'Verified', 'Verified as a valid email');
INSERT INTO `study`.`email_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (2, 'Unverified', 'Not verified as a valid email address');
INSERT INTO `study`.`email_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (3, 'Bounced', 'An email to this address bounced');


ALTER TABLE `study`.`person` 
	ADD COLUMN `OTHER_EMAIL_STATUS` INT NULL DEFAULT 0  AFTER `OTHER_ID` , 
	ADD COLUMN `PREFERRED_EMAIL_STATUS` INT NULL DEFAULT 0  AFTER `OTHER_EMAIL_STATUS` ;



ALTER TABLE `study`.`person` 
  ADD CONSTRAINT `fk_person_other_email_status`
  FOREIGN KEY (`OTHER_EMAIL_STATUS` )
  REFERENCES `study`.`email_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `fk_person_preferred_email_status`
  FOREIGN KEY (`PREFERRED_EMAIL_STATUS` )
  REFERENCES `study`.`email_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_person_other_email_status` (`OTHER_EMAIL_STATUS` ASC) 
, ADD INDEX `fk_person_preferred_email_status` (`PREFERRED_EMAIL_STATUS` ASC) ;

