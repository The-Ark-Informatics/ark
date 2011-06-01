USE study;
CREATE  TABLE `study`.`ark_function_type` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(1000) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
COMMENT = 'Determines the type of function as a Report or Non-Report function/usecase.';

INSERT INTO `study`.`ark_function_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1, 'NON-REPORT', 'A function that is not a report.');
INSERT INTO `study`.`ark_function_type` (`ID`, `NAME`, `DESCRIPTION`) VALUES (2, 'REPORT', ' A function that is a report.');
