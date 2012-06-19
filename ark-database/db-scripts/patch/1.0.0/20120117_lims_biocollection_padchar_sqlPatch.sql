
use lims;
CREATE  TABLE `lims`.`biocollection_padchar` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

ALTER TABLE `lims`.`biocollection_padchar` RENAME TO  `lims`.`biocollectionuid_padchar` ;

INSERT INTO `biocollectionuid_padchar` (`ID`, `NAME`) VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10');

