use study;
CREATE  TABLE `study`.`function_type` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(1000) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
COMMENT = 'Determines the type of function as a Report or Non-Report function/usecase.';
