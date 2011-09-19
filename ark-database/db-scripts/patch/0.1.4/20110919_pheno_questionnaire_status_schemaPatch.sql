use pheno;
CREATE  TABLE `pheno`.`questionnaire_status` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(100) NULL ,
  `DESCRIPTION` VARCHAR(4255) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

INSERT INTO `pheno`.`questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1, 'In Progress', 'The Questionnaire is being provided with data and not yet completed.');
INSERT INTO `pheno`.`questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (2, 'Data Entry Completed', 'Questionnaire data entry is completed and awaiting review.');
INSERT INTO `pheno`.`questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (3, 'Review Ok', 'The Questionnaire data was reviewed successfully and questionnaire is now locked from further modification.');
INSERT INTO `pheno`.`questionnaire_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (4, 'Review Failed', 'The Questionnaire data failed review and is needs to be revisited for data correction.');


