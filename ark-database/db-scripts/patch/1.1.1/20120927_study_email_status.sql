CREATE  TABLE `study`.`email_status` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
COMMENT = 'Status (something like  Unverified, Verified, Bounced, Unknown)';



